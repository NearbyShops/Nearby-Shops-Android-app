package org.nearbyshops.whitelabelapp.AdminDelivery;


import android.Manifest;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import org.nearbyshops.whitelabelapp.Admin.ViewModel.ViewModelDeliveryGuy;
import org.nearbyshops.whitelabelapp.AdminDelivery.InventoryDeliveryPerson.DeliveryFragmentPager;
import org.nearbyshops.whitelabelapp.AdminShop.ManageShop;
import org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory.OrdersHistoryFragment;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyAboutLogin;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.Lists.ItemsByCategory.ItemsByCatFragment;
import org.nearbyshops.whitelabelapp.Lists.MarketsList.ListUIFragment;
import org.nearbyshops.whitelabelapp.Lists.ProfileScreen.ProfileFragmentNew;
import org.nearbyshops.whitelabelapp.Lists.ShopsList.FragmentShopsList;
import org.nearbyshops.whitelabelapp.Login.LoginPlaceholder.FragmentSignInMessage;
import org.nearbyshops.whitelabelapp.Model.ModelStaff.DeliveryGuyData;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefDeliveryGuyHome;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Services.GetAppSettings;
import org.nearbyshops.whitelabelapp.Services.LocationService;
import org.nearbyshops.whitelabelapp.Services.NonStopServices.PersistentLocationService;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.databinding.ActivityDeliveryDashBottomNavBinding;


public class DeliveryDashboard extends AppCompatActivity implements NotifyAboutLogin, SetToolbarText {



    public static final String TAG_LOGIN = "tag_login";
    public static final String TAG_PROFILE = "tag_profile_fragment";

    public static final String TAG_ITEMS_FRAGMENT = "tag_items_fragment";
    public static final String TAG_HOME_FRAGMENT = "tag_home_fragment";
    public static final String TAG_ORDERS_FRAGMENT = "tag_orders_fragment";
    public static final String TAG_MANAGE_FRAGMENT = "tag_manage_fragment";

    public static final String TAG_DELIVERY_OFF = "tag_delivery_off";


    ActivityDeliveryDashBottomNavBinding binding;

    ViewModelDeliveryGuy viewModelDeliveryGuy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_delivery_dash_bottom_nav);

        binding = ActivityDeliveryDashBottomNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initialize();

        setupViewModelDeliveryGuy();
        viewModelDeliveryGuy.getDeliveryGuyDetails();


        if(savedInstanceState==null)
        {
            binding.bottomNavigation.setSelectedItemId(R.id.bottom_tab_home);
        }


        startService(new Intent(this, PersistentLocationService.class));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        UtilityFunctions.resetRoutine(this);

        if(PrefDeliveryGuyHome.getDeliveryStatus(this)==DeliveryGuyData.STATUS_OFFLINE)
        {
                    stopService(new Intent(this, PersistentLocationService.class));
        }

    }





    void setupViewModelDeliveryGuy()
    {
        viewModelDeliveryGuy = new ViewModelDeliveryGuy(MyApplication.application);

        viewModelDeliveryGuy.getDataLive().observe(this, new Observer<DeliveryGuyData>() {
            @Override
            public void onChanged(DeliveryGuyData deliveryGuyData) {

                PrefDeliveryGuyHome.saveDeliveryGuyData(deliveryGuyData,DeliveryDashboard.this);
            }
        });
    }



    void initialize()
    {
        startService(new Intent(this, LocationService.class));

        //        bottomBar.setDefaultTab(R.id.tab_search);
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        startService(new Intent(this, GetAppSettings.class));

        FirebaseApp.initializeApp(getApplicationContext());
        setupLocalBroadcastManager();



//        BadgeDrawable badgeDrawable  = bottomBar.getOrCreateBadge(1);
//        badgeDrawable.setNumber(2);


//        BadgeDrawable badge = bottomBar.getOrCreateBadge(R.id.bottom_tab_orders);
//        badge.setVisible(true);
//        badge.setNumber(10);
//        badge.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));



        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                setSupportActionBar(binding.toolbar);

                if(menuItem.getItemId()== R.id.bottom_tab_home)
                {
                    showHomeFragment();
                }
                else if(menuItem.getItemId()== R.id.bottom_tab_orders)
                {
                    showOrdersFragment();
                }
                else if(menuItem.getItemId()== R.id.bottom_tab_profile)
                {
                    showProfileFragment();
                }


                return true;
            }
        });


        checkPermissions();
        setSupportActionBar(binding.toolbar);
    }





    @Override
    public void loginSuccess() {


        getSupportFragmentManager().popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
        );

        binding.bottomNavigation.setSelectedItemId(binding.bottomNavigation.getSelectedItemId());
    }




    @Override
    public void loggedOut() {
        showProfileFragment();
    }







    public void showLoginFragment() {


        if(getSupportFragmentManager().findFragmentByTag(TAG_LOGIN)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FragmentSignInMessage(), TAG_LOGIN)
                    .commit();
        }

    }





    void checkPermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            //
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);
            return;
        }


//        fetchLocation();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        //
//        if(requestCode==2)
//        {
//            // If request is cancelled, the result arrays are empty.
//
//
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            //                startService(new Intent(this,LocationUpdateServiceLocal.class));

            showToastMessage("Permission Granted !");

            startService(new Intent(this, LocationService.class));
        }

    }






    public void showProfileFragment() {

        // single market mode

        if(getSupportFragmentManager().findFragmentByTag(TAG_PROFILE)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragmentNew(), TAG_PROFILE)
                    .commit();
        }

    }





    public void showOrdersFragment() {

        if (PrefLogin.getUser(getBaseContext()) == null) {

            showLoginFragment();

        }
        else {


            int deliveryStatus = PrefDeliveryGuyHome.getDeliveryStatus(this);

            if(deliveryStatus==DeliveryGuyData.STATUS_ONLINE)
            {

                if(getSupportFragmentManager().findFragmentByTag(TAG_ORDERS_FRAGMENT)==null)
                {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new DeliveryFragmentPager(), TAG_ORDERS_FRAGMENT)
                            .commitNow();

                }

            }
            else {

                if(getSupportFragmentManager().findFragmentByTag(TAG_DELIVERY_OFF)==null)
                {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new DeliveryTurnedOFF(), TAG_DELIVERY_OFF)
                            .commitNow();

                }
            }

        }
    }




    public void showHomeFragment() {

        if (PrefLogin.getUser(getBaseContext()) == null) {

            showLoginFragment();

        }
        else {

//            new ShopDashboardFragment()
            if(getSupportFragmentManager().findFragmentByTag(TAG_HOME_FRAGMENT)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new DeliveryHome(), TAG_HOME_FRAGMENT)
                        .commitNow();

            }
        }
    }





    public void showManageFragment() {

        if (PrefLogin.getUser(getBaseContext()) == null) {

            showLoginFragment();
        }
        else {

            if(getSupportFragmentManager().findFragmentByTag(TAG_MANAGE_FRAGMENT)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ManageShop(), TAG_MANAGE_FRAGMENT)
                        .commitNow();

            }
        }
    }






    public void showItemsFragment() {

//        FragmentItemsInShop.newInstance(FragmentItemsInShop.MODE_LOW_STOCK)

        if (getSupportFragmentManager().findFragmentByTag(TAG_ITEMS_FRAGMENT) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,
                            new ListUIFragment(
                                    ListUIFragment.screen_type_items_in_shop_seller,
                                    ListUIFragment.MODE_LOW_STOCK
                            ), TAG_ITEMS_FRAGMENT
                    )
                    .commitNow();
        }
    }







    void showToastMessage(String message) {

        UtilityFunctions.showToastMessage(this,message);
    }



    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


        if (fragment instanceof NotifyBackPressed) {

            if (((NotifyBackPressed) fragment).backPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_by_cat_simple, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));



//        SearchView.SearchAutoComplete autoComplete = searchView.findViewById(R.id.search_src_text);
//        autoComplete.setHintTextColor(getResources().getColor(R.color.blue_color));
//        autoComplete.setTextColor(getResources().getColor(R.color.darkGreen));



        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(fragment instanceof FragmentShopsList)
        {
            searchView.setQueryHint("Enter Shop Name ...");
        }
        else if(fragment instanceof ItemsByCatFragment)
        {
            searchView.setQueryHint("Enter Item Name ...");
        }
        else if(fragment instanceof OrdersHistoryFragment)
        {
            searchView.setQueryHint("Enter Order ID ... ");
        }


        MenuItem item = menu.findItem(R.id.action_search);


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if (fragment instanceof NotifySearch) {
                    ((NotifySearch) fragment).endSearchMode();
                }

//                Toast.makeText(Home.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();

                return true;
            }
        });


        return true;
    }







    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

//            Toast.makeText(this,query,Toast.LENGTH_SHORT).show();

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (fragment instanceof NotifySearch) {
                ((NotifySearch) fragment).search(query);
            }
        }
    }











    private void setupLocalBroadcastManager()
    {
        IntentFilter filter = new IntentFilter();

//        filter.addAction(UpdateServiceConfiguration.INTENT_ACTION_MARKET_CONFIG_FETCHED);
        filter.addAction(LocationService.INTENT_ACTION_LOCATION_UPDATED);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });


            }
        },filter);
    }





    @Override
    public void setToolbar(boolean toolbarVisible, String header, boolean isheaderBold, String subtitle) {

        binding.appBar.setExpanded(true,true);

        if(toolbarVisible)
        {
            binding.toolbar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.toolbar.setVisibility(View.GONE);
        }

        binding.toolbarHeader.setText(header);

        if(isheaderBold)
        {
            binding.toolbarHeader.setTypeface(null, Typeface.BOLD);
        }
        else
        {
            binding.toolbarHeader.setTypeface(null, Typeface.NORMAL);
        }


        if(subtitle==null)
        {
            binding.toolbarText.setVisibility(View.GONE);
        }
        else
        {
            binding.toolbarText.setVisibility(View.VISIBLE);
            binding.toolbarText.setText(subtitle);
        }
    }
}
