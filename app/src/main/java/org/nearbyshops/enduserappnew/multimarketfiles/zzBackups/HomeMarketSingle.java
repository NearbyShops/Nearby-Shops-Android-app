package org.nearbyshops.enduserappnew.multimarketfiles.zzBackups;


import android.Manifest;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import org.nearbyshops.enduserappnew.Interfaces.MarketSelected;
import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.ShowFragment;
import org.nearbyshops.enduserappnew.Lists.CartItemList.CartItemListFragment;
import org.nearbyshops.enduserappnew.Lists.CartsList.CartsListFragment;
import org.nearbyshops.enduserappnew.Lists.ItemsByCategory.ItemsByCatFragment;
import org.nearbyshops.enduserappnew.Lists.ItemsInShopByCategory.ItemsInShopByCatFragment;
import org.nearbyshops.enduserappnew.multimarketfiles.Markets.MarketsFragmentNew;
import org.nearbyshops.enduserappnew.multimarketfiles.Markets.ViewModelMarkets;
import org.nearbyshops.enduserappnew.Lists.OrderHistory.OrdersHistoryFragment;
import org.nearbyshops.enduserappnew.Lists.ProfileScreen.ProfileFragmentNew;
import org.nearbyshops.enduserappnew.Lists.ShopsList.FragmentShopsList;
import org.nearbyshops.enduserappnew.Login.LoginPlaceholder.FragmentSignInMessage;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.ProfileFragment;
import org.nearbyshops.enduserappnew.PushOneSignal.PrefOneSignal;
import org.nearbyshops.enduserappnew.PushOneSignal.UpdateOneSignalID;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Services.LocationService;
import org.nearbyshops.enduserappnew.Services.UpdateServiceConfiguration;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;


public class HomeMarketSingle extends AppCompatActivity implements ShowFragment, NotifyAboutLogin, MarketSelected {



    public static final String TAG_LOGIN = "tag_login";
    public static final String TAG_PROFILE = "tag_profile_fragment";

    public static final String TAG_ITEMS_FRAGMENT = "tag_items_fragment";
    public static final String TAG_SHOPS_FRAGMENT = "tag_shops_fragment";
    public static final String TAG_CARTS_FRAGMENT = "tag_carts_fragment";
    public static final String TAG_ORDERS_FRAGMENT = "tag_orders_fragment";

    public static final String TAG_MARKET_FRAGMENT = "tag_market_fragment";


    private static final int REQUEST_CHECK_SETTINGS = 100;



    BottomNavigationView bottomBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);

        bottomBar = findViewById(R.id.bottom_navigation);

//        bottomBar.setDefaultTab(R.id.tab_search);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);



        startService(new Intent(this,UpdateServiceConfiguration.class));



        FirebaseApp.initializeApp(getApplicationContext());
        UtilityFunctions.updateFirebaseSubscriptions();




        setupViewModel();
        setupLocalBroadcastManager();



//        if (PrefGeneral.isMultiMarketEnabled(this)) {
//
//            bottomBar.getMenu().getItem(4).setTitle("Markets");
//            bottomBar.getMenu().getItem(4).setIcon(R.drawable.ic_business_24);
//
//        }
//        else
//        {
//
//            bottomBar.getMenu().getItem(4).setTitle("Profile");
//        }




        if(getResources().getBoolean(R.bool.single_vendor_mode_enabled))
        {
//            bottomBar.getMenu().getItem(0).setVisible(false);
            bottomBar.getMenu().getItem(0).setTitle("Home");
            bottomBar.getMenu().getItem(0).setVisible(false);
        }
        else
        {
            if(!getResources().getBoolean(R.bool.show_items_screen_in_multi_vendor))
            {
                bottomBar.getMenu().getItem(1).setVisible(false);
            }
        }





        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


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


//        bottomBar.setSelectedItemId(R.id.bottom_tab_items);




        if(savedInstanceState==null)
        {
//            showToast("Home : OnSaveInstanceState");
            initialFragmentSetup();
        }



//        startSettingsCheck();
        checkLocationSettings();



        checkPermissions();
//        fetchLocation();




        if (PrefGeneral.getServiceURL(this) != null
                && PrefOneSignal.getToken(this) != null
                && PrefLogin.getUser(this)!=null) {

                startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
        }





        if (PrefServiceConfig.getServiceConfigLocal(this) == null && PrefGeneral.getServiceURL(this) != null) {
            // get service configuration when its null ... fetches config at first install or changing service
            startService(new Intent(getApplicationContext(), UpdateServiceConfiguration.class));
        }



        setupBottomBarLights();
    }









    void setupBottomBarLights()
    {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if(fragment instanceof MarketsFragmentNew)
                {
//                    bottomBar.selectTabWithId(R.id.tab_profile);
//                    bottomBar.setSelectedItemId(R.id.tab_profile);

                    bottomBar.getMenu().getItem(4).setChecked(true);
                }
                else if(fragment instanceof ProfileFragment)
                {
                    bottomBar.getMenu().getItem(4).setChecked(true);
                }
                else if(fragment instanceof OrdersHistoryFragment)
                {
                    bottomBar.getMenu().getItem(3).setChecked(true);

                }
                else if(fragment instanceof CartsListFragment)
                {
                    bottomBar.getMenu().getItem(2).setChecked(true);
                }
                else if(fragment instanceof FragmentShopsList)
                {
                    bottomBar.getMenu().getItem(1).setChecked(true);
                }
                else if(fragment instanceof ItemsByCatFragment)
                {

                    bottomBar.getMenu().getItem(0).setChecked(true);

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

        marketSelected();
    }






    @Override
    public void loggedOut() {

//        showProfileFragment(true);
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
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            //                startService(new Intent(this,LocationUpdateServiceLocal.class));

            showToastMessage("Permission Granted !");


            if(!PrefLocation.isLocationSetByUser(this))
            {
                startService(new Intent(this, LocationService.class));
            }


//            fetchLocation();


            checkLocationSettings();


        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            showToastMessage("Permission Rejected");
        }
    }







    void checkLocationSettings()
    {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);



        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());




        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

                if(!PrefLocation.isLocationSetByUser(HomeMarketSingle.this))
                {
                    startService(new Intent(HomeMarketSingle.this, LocationService.class));
                }


            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(HomeMarketSingle.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }








    @Override
    public void showProfileFragment() {



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
                        .replace(R.id.fragment_container, OrdersHistoryFragment.newInstance(PrefLogin.getUser(this).getUserID(),0,OrdersHistoryFragment.MODE_END_USER), TAG_ORDERS_FRAGMENT)
                        .commitNow();

            }
        }
    }






    @Override
    public void showCartFragment() {


        if (PrefLogin.getUser(getBaseContext()) == null) {

            showLoginFragment();
            return;

        }

        else {


            if(!getResources().getBoolean(R.bool.single_vendor_mode_enabled))
            {

                if(getSupportFragmentManager().findFragmentByTag(TAG_CARTS_FRAGMENT)==null)
                {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new CartsListFragment(), TAG_CARTS_FRAGMENT)
                            .commitNow();

                }

            }
            else
            {

                CartItemListFragment fragment = new CartItemListFragment();
                fragment.setShopID(getResources().getInteger(R.integer.single_vendor_shop_id));


                if(getSupportFragmentManager().findFragmentByTag(TAG_CARTS_FRAGMENT)==null)
                {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,fragment
                                    ,TAG_CARTS_FRAGMENT)
                            .commitNow();


                }

            }

        }
    }





    @Override
    public void showShopsFragment() {


            boolean singleVendorEnabled = getResources().getBoolean(R.bool.single_vendor_mode_enabled);


            if(singleVendorEnabled)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, ItemsInShopByCatFragment.newInstance(true,
                                ItemsInShopByCatFragment.MODE_SHOP_HOME),
                                TAG_ITEMS_FRAGMENT)
                        .commitNow();


            }
            else
            {

                if (getSupportFragmentManager().findFragmentByTag(TAG_SHOPS_FRAGMENT) == null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, FragmentShopsList.newInstance(false), TAG_SHOPS_FRAGMENT)
                            .commitNow();
                }

            }

    }





    @Override
    public void showItemsFragment() {

        boolean singleVendorEnabled = getResources().getBoolean(R.bool.single_vendor_mode_enabled);


        if(singleVendorEnabled)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, ItemsInShopByCatFragment.newInstance(true,
                            ItemsInShopByCatFragment.MODE_ITEMS_IN_SHOP_SINGLE_VENDOR),
                            TAG_ITEMS_FRAGMENT)
                    .commitNow();


        }
        else
        {

            if (getSupportFragmentManager().findFragmentByTag(TAG_ITEMS_FRAGMENT) == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ItemsByCatFragment(), TAG_ITEMS_FRAGMENT)
                        .commitNow();
            }
        }


    }





    void initialFragmentSetup()
    {
        boolean singleVendorEnabled = getResources().getBoolean(R.bool.single_vendor_mode_enabled);

        if(singleVendorEnabled)
        {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,
                            ItemsInShopByCatFragment.newInstance(true, ItemsInShopByCatFragment.MODE_ITEMS_IN_SHOP_SINGLE_VENDOR),
                            TAG_ITEMS_FRAGMENT)
                    .commitNow();
        }
        else
        {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, FragmentShopsList.newInstance(false), TAG_SHOPS_FRAGMENT)
                    .commitNow();
        }
    }






    void showToastMessage(String message) {

        UtilityFunctions.showToastMessage(this,message);
    }


    boolean isDestroyed = false;




    boolean promptBackPressed = false;


    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


        if (fragment instanceof NotifyBackPressed) {

            if (((NotifyBackPressed) fragment).backPressed()) {

                if(getResources().getBoolean(R.bool.prompt_user_back_pressed))
                {
                    exitApp();
                }
                else
                {
                    super.onBackPressed();
                }

            }

        }
        else
            {


                if(getResources().getBoolean(R.bool.prompt_user_back_pressed))
                {
                    exitApp();
                }
                else
                {
                    super.onBackPressed();
                }
        }
    }





    void exitApp()
    {


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);


        dialog.setTitle("Do you want to exit ?")
                .setMessage("Do you want to Exit app")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HomeMarketSingle.super.onBackPressed();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }

//    if(promptBackPressed)
//    {
//
//        super.onBackPressed();
//    }
//            else
//    {
//        showToastMessage("Press again to Exit !");
//        promptBackPressed=true;
//    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_by_cat_simple, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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
    public void marketSelected() {

//            bottomBar.selectTabWithId(R.id.tab_items);
//            bottomBar.selectTabAtPosition(bottomBar.getCurrentTabPosition());
//            showItemsFragment();


//        showToastMessage("Market Selected : Home ");



        getSupportFragmentManager().popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
        );



        int tabId = bottomBar.getSelectedItemId();


        if (tabId == R.id.bottom_tab_items) {
            showItemsFragment();
        } else if (tabId == R.id.bottom_tab_shops) {

//            showShopsFragment();


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, FragmentShopsList.newInstance(false), TAG_SHOPS_FRAGMENT)
                    .commitNow();

        } else if (tabId == R.id.bottom_tab_cart) {

            showCartFragment();
        } else if (tabId == R.id.bottom_tab_orders) {

            showOrdersFragment();

        } else if (tabId == R.id.bottom_tab_profile) {

//                showProfileFragment();
//                showItemsFragment();

            if(getResources().getBoolean(R.bool.single_vendor_mode_enabled))
            {
                bottomBar.setSelectedItemId(R.id.bottom_tab_items);
            }
            else
            {
                bottomBar.setSelectedItemId(R.id.bottom_tab_shops);
            }
        }

    }









    @Override
    protected void onDestroy() {
        super.onDestroy();
//        showToastMessage(" Home Activity Destroyed ");
        PrefLocation.locationSetByUser(false,this);
        PrefLocation.saveDeliveryAddress(null,this);
    }






    private ViewModelMarkets viewModel;


    private void setupViewModel()
    {


        viewModel = new ViewModelMarkets(MyApplication.application);


        viewModel.getEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {


                if(integer == ViewModelMarkets.EVENT_LOCAL_CONFIG_FETCHED)
                {
                    initialFragmentSetup();
                }

            }
        });



        viewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                showToastMessage(s);
            }
        });

    }






    private void setupLocalBroadcastManager()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationService.INTENT_ACTION_LOCATION_UPDATED);




        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(PrefGeneral.getServiceURL(HomeMarketSingle.this)==null)
                        {
//                            viewModel.getNearestMarket();
                        }

                    }
                });


            }
        },filter);
    }


}
