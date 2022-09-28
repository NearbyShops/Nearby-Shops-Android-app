package org.nearbyshops.whitelabelapp.Admin

import android.Manifest
import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.FirebaseApp
import org.nearbyshops.whitelabelapp.Admin.ViewModel.ViewModelMarketsForAdmin
import org.nearbyshops.whitelabelapp.AdminCommon.ShopsListForAdmin.ShopListPagerFragment
import org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory.OrdersHistoryFragment
import org.nearbyshops.whitelabelapp.Interfaces.*
import org.nearbyshops.whitelabelapp.Lists.ItemsByCategory.ItemsByCatFragment
import org.nearbyshops.whitelabelapp.Lists.ProfileScreen.ProfileFragmentNew
import org.nearbyshops.whitelabelapp.Lists.ShopsList.FragmentShopsList
import org.nearbyshops.whitelabelapp.Login.LoginPlaceholder.FragmentSignInMessage
import org.nearbyshops.whitelabelapp.MyApplication
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin
import org.nearbyshops.whitelabelapp.Preferences.PrefMarketAdminHome
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Services.LocationService
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.ActivityMarketAdminBottomNavSmBinding


class AdminDashboardBottom : AppCompatActivity(), NotifyAboutLogin, SetToolbarText {


    private lateinit var binding:ActivityMarketAdminBottomNavSmBinding
    private lateinit var viewModelMarketForAdmin: ViewModelMarketsForAdmin



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //        setContentView(R.layout.activity_market_admin_bottom_nav)
        binding = ActivityMarketAdminBottomNavSmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()

        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.bottom_tab_home
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        UtilityFunctions.resetRoutine(this)
    }



    fun initialize() {


        startService(Intent(this, LocationService::class.java))
        FirebaseApp.initializeApp(applicationContext)
//        UtilityFCMTopicSubscriptions.subscribeToMarketStaffTopics()
        setupLocalBroadcastManager()

        setupViewModel()

        viewModelMarketForAdmin.getMarketForMarketStaff()


//        BadgeDrawable badgeDrawable  = bottomBar.getOrCreateBadge(1);
//        badgeDrawable.setNumber(2);


//        BadgeDrawable badge = bottomBar.getOrCreateBadge(R.id.bottom_tab_orders);
//        badge.setVisible(true);
//        badge.setNumber(10);
//        badge.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->

            setSupportActionBar(binding.toolbar)

            var user = PrefLogin.getUser(baseContext);


            if (user == null) {

                showLoginFragment()
            }
            else
            {

                if (menuItem.itemId == R.id.bottom_tab_home) {
                    showHomeFragment()
                } else if (menuItem.itemId == R.id.bottom_tab_orders) {
                    showOrdersFragment()
                } else if (menuItem.itemId == R.id.bottom_tab_shops) {
                    showShopsFragment()
                } else if (menuItem.itemId == R.id.bottom_tab_manage) {
                    showManageFragment()
                } else if (menuItem.itemId == R.id.bottom_tab_profile) {
                    showProfileFragment()
                }
            }


            true
        }




        checkPermissions()
        setSupportActionBar(binding.toolbar)
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==890)
        {
            initialize()
        }
    }




    override fun loginSuccess() {
        supportFragmentManager.popBackStackImmediate(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        binding.bottomNavigation.selectedItemId = binding.bottomNavigation.selectedItemId
    }


    override fun loggedOut() {
        showProfileFragment()
    }




    private fun showLoginFragment() {

        if (supportFragmentManager.findFragmentByTag(TAG_LOGIN) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, FragmentSignInMessage(), TAG_LOGIN)
                .commit()
        }
    }






    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 2)
            return
        }
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        //
//        if(requestCode==2)
//        {
//            // If request is cancelled, the result arrays are empty.
//
//
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            //                startService(new Intent(this,LocationUpdateServiceLocal.class));
            showToastMessage("Permission Granted !")
            startService(Intent(this, LocationService::class.java))
        }
    }

    fun showProfileFragment() {

        // single market mode
        if (supportFragmentManager.findFragmentByTag(TAG_PROFILE) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ProfileFragmentNew(), TAG_PROFILE)
                .commit()
        }
    }

    fun showOrdersFragment() {
        if (PrefLogin.getUser(baseContext) == null) {
            showLoginFragment()
        } else {


            if (supportFragmentManager.findFragmentByTag(TAG_ORDERS_FRAGMENT) == null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container, OrdersHistoryFragment.newInstance(0, 0, OrdersHistoryFragment.MODE_MARKET_ADMIN), TAG_ORDERS_FRAGMENT
                    )
                    .commitNow()
            }


//            val market = PrefMarketAdminHome.getMarket(this)
//
//            if(market!=null && market.registrationStatus == Market.REGISTRATION_STATUS_ENABLED)
//            {
//
//
//
//            }
//            else{
//
//                showApprovalPending()
//
//            }


        }
    }



    fun showShopsFragment() {

        if (PrefLogin.getUser(baseContext) == null) {
            showLoginFragment()
        } else {



            if (supportFragmentManager.findFragmentByTag(TAG_SHOP_FRAGMENT) == null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, ShopListPagerFragment(), TAG_SHOP_FRAGMENT)
                    .commitNow()
            }

//            val market = PrefMarketAdminHome.getMarket(this)
//
//            if(market!=null && market.registrationStatus == Market.REGISTRATION_STATUS_ENABLED)
//            {
//            }
//            else{
//
//                showApprovalPending()
//
//            }
        }
    }



    fun showHomeFragment() {

        if (PrefLogin.getUser(baseContext) == null) {

            showLoginFragment()
        }
        else {

            if (supportFragmentManager.findFragmentByTag(TAG_HOME_FRAGMENT) == null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, MarketHome(), TAG_HOME_FRAGMENT)
                    .commitNow()
            }

//            val market = PrefMarketAdminHome.getMarket(this)
//
//            if(market!=null && market.registrationStatus == Market.REGISTRATION_STATUS_ENABLED)
//            {
//
//
//            }
//            else{
//
//                showApprovalPending()
//            }


        }
    }

    fun showManageFragment() {
        if (PrefLogin.getUser(baseContext) == null) {
            showLoginFragment()
        } else {

            if (supportFragmentManager.findFragmentByTag(TAG_MANAGE_FRAGMENT) == null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, ManageMarket(), TAG_MANAGE_FRAGMENT)
                    .commitNow()
            }

//            val market = PrefMarketAdminHome.getMarket(this)
//
//            if(market!=null && market.registrationStatus == Market.REGISTRATION_STATUS_ENABLED)
//            {
//
//
//            }
//            else
//            {
//                showApprovalPending()
//            }

        }
    }



    fun showToastMessage(message: String?) {
        UtilityFunctions.showToastMessage(this, message)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is NotifyBackPressed) {
            if ((fragment as NotifyBackPressed).backPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_items_by_cat_simple, menu)


        // Associate searchable configuration with the SearchView
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))


//        SearchView.SearchAutoComplete autoComplete = searchView.findViewById(R.id.search_src_text);
//        autoComplete.setHintTextColor(getResources().getColor(R.color.blue_color));
//        autoComplete.setTextColor(getResources().getColor(R.color.darkGreen));
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is FragmentShopsList) {
            searchView.queryHint = "Enter Shop Name ..."
        } else if (fragment is MarketHome) {
            searchView.queryHint = "Enter Market Name ..."
        } else if (fragment is ItemsByCatFragment) {
            searchView.queryHint = "Enter Item Name ..."
        } else if (fragment is OrdersHistoryFragment) {
            searchView.queryHint = "Enter Order ID ... "
        }
        val item = menu.findItem(R.id.action_search)
        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (fragment is NotifySearch) {
                    (fragment as NotifySearch).endSearchMode()
                }

//                Toast.makeText(Home.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();
                return true
            }
        })
        return true
    }

    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow

//            Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (fragment is NotifySearch) {
                (fragment as NotifySearch).search(query)
            }
        }
    }



    private fun setupLocalBroadcastManager() {
        val filter = IntentFilter()

//        filter.addAction(UpdateServiceConfiguration.INTENT_ACTION_MARKET_CONFIG_FETCHED);
        filter.addAction(LocationService.INTENT_ACTION_LOCATION_UPDATED)
        LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                runOnUiThread { }
            }
        }, filter)
    }



    override fun setToolbar(
        toolbarVisible: Boolean,
        header: String,
        isheaderBold: Boolean,
        subtitle: String?
    ) {

        binding.appBar.setExpanded(true, true)

        if (toolbarVisible) {
            binding.toolbar.visibility = View.VISIBLE
        } else {
            binding.toolbar.visibility = View.GONE
        }

        binding.toolbarHeader.text = header

        if (isheaderBold) {
            binding.toolbarHeader.setTypeface(null, Typeface.BOLD)
        } else {
            binding.toolbarHeader.setTypeface(null, Typeface.NORMAL)
        }


        if (subtitle == null) {

            binding.toolbarText.visibility = View.GONE

        } else {
            binding.toolbarText.visibility = View.VISIBLE
            binding.toolbarText.text = subtitle
        }
    }






    fun setupViewModel() {

        viewModelMarketForAdmin = ViewModelMarketsForAdmin(MyApplication.application)
        viewModelMarketForAdmin.marketLive.observe(this,
            { market ->
                PrefMarketAdminHome.saveMarket(market,this)

                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

                if (fragment is RefreshFragment) {
                    (fragment as RefreshFragment).refreshFragment()
                }

                binding.bottomNavigation.selectedItemId = binding.bottomNavigation.selectedItemId

            })
    }





    companion object {

        const val TAG_APPROVAL_PENDING = "tag_approval_pending"

        const val TAG_LOGIN = "tag_login"
        const val TAG_PROFILE = "tag_profile_fragment"
        const val TAG_ITEMS_FRAGMENT = "tag_items_fragment"
        const val TAG_HOME_FRAGMENT = "tag_home_fragment"
        const val TAG_ORDERS_FRAGMENT = "tag_orders_fragment"
        const val TAG_SHOP_FRAGMENT = "tag_shops_fragment"
        const val TAG_MANAGE_FRAGMENT = "tag_manage_fragment"
    }
}