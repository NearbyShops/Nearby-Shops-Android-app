package org.nearbyshops.whitelabelapp;


import android.Manifest;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.CartAndOrder.CartsList.CartsListFragment;
import org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory.OrdersHistoryFragment;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyAboutLogin;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.Interfaces.ShowFragment;
import org.nearbyshops.whitelabelapp.Lists.ItemsByCategory.ItemsByCatFragment;
import org.nearbyshops.whitelabelapp.Lists.ProfileScreen.ProfileFragmentNew;
import org.nearbyshops.whitelabelapp.Lists.ShopsList.FragmentShopsList;
import org.nearbyshops.whitelabelapp.Login.LoginPlaceholder.FragmentSignInMessage;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Services.GetAppSettings;
import org.nearbyshops.whitelabelapp.Services.LocationService;
import org.nearbyshops.whitelabelapp.Utility.UtilityFCMTopicSubscriptions;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.databinding.ActivityHomeNewBinding;
import org.nearbyshops.whitelabelapp.zDeprecatedScreens.ProfileFragment;

import javax.inject.Inject;


public class Home extends AppCompatActivity implements ShowFragment, NotifyAboutLogin, SetToolbarText {



    public static final String TAG_LOGIN = "tag_login";
    public static final String TAG_PROFILE = "tag_profile_fragment";

    public static final String TAG_ITEMS_FRAGMENT = "tag_items_fragment";
    public static final String TAG_SHOPS_FRAGMENT = "tag_shops_fragment";
    public static final String TAG_CARTS_FRAGMENT = "tag_carts_fragment";
    public static final String TAG_ORDERS_FRAGMENT = "tag_orders_fragment";

    public static final String TAG_MARKET_FRAGMENT = "tag_market_fragment";


//    BottomNavigationView bottomBar;

    @Inject
    Gson gson;


//    Toolbar toolbar;
//    TextView toolbarHeader;
//    TextView toolbarText;

    ActivityHomeNewBinding binding;






    public Home() {

    }



    void bindViews()
    {
//        bottomBar = findViewById(R.id.bottom_navigation);
//        toolbarHeader = findViewById(R.id.toolbar_header);
//        toolbarText = findViewById(R.id.toolbar_text);
//        toolbar = findViewById(R.id.toolbar);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_home_new);

        binding = ActivityHomeNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bindViews();

        if(savedInstanceState==null)
        {
            initialFragmentSetup();
        }

        initialize();


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);





        if(getResources().getBoolean(R.bool.demo_mode_enabled))
        {
            showDemoInstructions();
        }

    }







    void initialize()
    {
        startService(new Intent(this, LocationService.class));

        //        bottomBar.setDefaultTab(R.id.tab_search);
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        startService(new Intent(this, GetAppSettings.class));

        FirebaseApp.initializeApp(getApplicationContext());

        UtilityFCMTopicSubscriptions.subscribeToAllTopics();
        setupLocalBroadcastManager();

//        binding.bottomNavigation.getMenu().getItem(4).setTitle("Profile");

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

                if(menuItem.getItemId()== R.id.bottom_tab_items)
                {
                    showItemsFragment();
                }
                else if(menuItem.getItemId()== R.id.bottom_tab_shops)
                {
                    showShopsFragment();
                }
                else if(menuItem.getItemId()== R.id.bottom_tab_cart)
                {
                    showCartFragment();
                }
//                else if(menuItem.getItemId()== R.id.bottom_tab_orders)
//                {
//                    showOrdersFragment();
//                }
                else if(menuItem.getItemId()== R.id.bottom_tab_profile)
                {
                    showProfileFragment();
                }


                return true;
            }
        });


        checkPermissions();


//        if (PrefGeneral.getServerURL(this) != null
//                && PrefOneSignal.getToken(this) != null
//                && PrefLogin.getUser(this)!=null) {
//
//            startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
//        }
//
//
//        if (PrefAppSettings.getAppConfig(this) == null && PrefGeneral.getServerURL(this) != null) {
//            // get service configuration when its null ... fetches config at first install or changing service
//            startService(new Intent(getApplicationContext(), GetAppSettings.class));
//        }


//        setupBottomBarLights();
        setSupportActionBar(binding.toolbar);
    }





    void setupBottomBarLights()
    {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if(fragment instanceof ProfileFragment)
                {
                    binding.bottomNavigation.getMenu().getItem(4).setChecked(true);
                }
                else if(fragment instanceof OrdersHistoryFragment)
                {
                    binding.bottomNavigation.getMenu().getItem(3).setChecked(true);

                }
                else if(fragment instanceof CartsListFragment)
                {
                    binding.bottomNavigation.getMenu().getItem(2).setChecked(true);
                }
                else if(fragment instanceof FragmentShopsList)
                {
                    binding.bottomNavigation.getMenu().getItem(1).setChecked(true);
                }
                else if(fragment instanceof ItemsByCatFragment)
                {

                    binding.bottomNavigation.getMenu().getItem(0).setChecked(true);

                }
                else if(fragment instanceof FragmentSignInMessage)
                {
//                    bottomBar.getMenu().getItem(3).setChecked(true);
                }



            }
        });
    }




    void showLogMessage(String message) {
        Log.d("log_home_screen", message);
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







    @Override
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






    @Override
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



    @Override
    public void showOrdersFragment() {

        if (PrefLogin.getUser(getBaseContext()) == null) {

            showLoginFragment();

        }
        else {


            if(getSupportFragmentManager().findFragmentByTag(TAG_ORDERS_FRAGMENT)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                OrdersHistoryFragment.newInstance(PrefLogin.getUser(this).getUserID(),0,OrdersHistoryFragment.MODE_END_USER),
                                TAG_ORDERS_FRAGMENT)
                        .commitNow();

            }
        }
    }



    @Override
    public void showCartFragment() {

        if (PrefLogin.getUser(getBaseContext()) == null) {

            showLoginFragment();

        }
        else {


            if(getSupportFragmentManager().findFragmentByTag(TAG_CARTS_FRAGMENT)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CartsListFragment(), TAG_CARTS_FRAGMENT)
                        .commitNow();

            }
        }
    }





//    Fragment fragmentShop;

    @Override
    public void showShopsFragment() {

//
        if (getSupportFragmentManager().findFragmentByTag(TAG_SHOPS_FRAGMENT) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, FragmentShopsList.newInstance(false), TAG_SHOPS_FRAGMENT)
                    .commitNow();
        }

    }





    @Override
    public void showItemsFragment() {

//            new ItemsByCatFragment()
//        MarketListFragment.newInstance()
        if (getSupportFragmentManager().findFragmentByTag(TAG_ITEMS_FRAGMENT) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ItemsByCatFragment(), TAG_ITEMS_FRAGMENT)
                    .commitNow();
        }
    }





    void initialFragmentSetup()
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, FragmentShopsList.newInstance(false), TAG_SHOPS_FRAGMENT)
                .commitNow();
    }






    void showToastMessage(String message) {

        UtilityFunctions.showToastMessage(this,message);
    }



    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


        if (fragment instanceof NotifyBackPressed) {
//            showLogMessage("Fragment Instanceof NotifyBackPressed !");

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





    @Override
    protected void onResume() {
        super.onResume();

        if(getResources().getBoolean(R.bool.demo_mode_enabled))
        {
            // if demo mode is enabled then load lat lon values stored for demo
            PrefLocation.saveLatLonSelected(
                    PrefLocation.getLatitudeDemo(this),
                    PrefLocation.getLongitudeDemo(this),
                    this
            );


//            if(!(PrefLocation.getLatitudeDemo(this)==PrefLocation.getLatitudeSelected(this)
//                    && PrefLocation.getLongitudeDemo(this)==PrefLocation.getLongitudeSelected(this)))
//            {
//
//                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//
//                if(fragment instanceof RefreshFragment)
//                {
//                    ((RefreshFragment) fragment).refreshFragment();
//                }
//            }

        }

    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        UtilityFunctions.resetRoutine(this);
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



    void showDemoInstructions()
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.demo_instructions_title)
                .setMessage(R.string.demo_instructions)
                .setPositiveButton("I understand !", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })
                .setIcon(R.drawable.app_icon_main_round)
                .show();
    }


}
