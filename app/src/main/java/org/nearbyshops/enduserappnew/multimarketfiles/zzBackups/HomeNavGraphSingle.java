package org.nearbyshops.enduserappnew.multimarketfiles.zzBackups;


import android.Manifest;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Lists.OrderHistory.OrdersHistoryFragment;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Services.LocationService;
import org.nearbyshops.enduserappnew.Services.UpdateServiceConfiguration;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;


public class HomeNavGraphSingle extends AppCompatActivity implements NotifyAboutLogin{



    BottomNavigationView bottomBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav_graph);

        bottomBar = findViewById(R.id.bottom_navigation);


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);



        FirebaseApp.initializeApp(getApplicationContext());
        UtilityFunctions.updateFirebaseSubscriptions();




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
                    showCarts();
                }
                else if(menuItem.getItemId()== R.id.bottom_tab_orders)
                {
                    showOrderHistory();
                }
                else if(menuItem.getItemId()== R.id.bottom_tab_profile)
                {
                    showProfileFragment();
                }


                return true;
            }
        });





//        NavigationUI.setupWithNavController(bottomBar,Navigation.findNavController(this,R.id.nav_host_fragment));


        checkPermissions();





        if (PrefServiceConfig.getServiceConfigLocal(this) == null && PrefGeneral.getServiceURL(this) != null) {
            // get service configuration when its null ... fetches config at first install or changing service
            startService(new Intent(getApplicationContext(), UpdateServiceConfiguration.class));
        }


    }



    void showLogMessage(String message) {
        Log.d("log_home_screen", message);
    }




    void showShopsFragment()
    {
        Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.fragmentShopsList);
    }




    void showItemsFragment()
    {
        Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.itemsByCatFragment);
    }





    void showOrderHistory()
    {

        User user = PrefLogin.getUser(this);

        if(user==null)
        {
            // not logged in so show
            Navigation.findNavController(HomeNavGraphSingle.this,R.id.nav_host_fragment).navigate(R.id.fragmentSignInMessage);
        }
        else
        {

            Bundle args = new Bundle();
            args.putInt("end_user_id", user.getUserID());
            args.putInt("shop_id", 0);
            args.putInt("current_mode",OrdersHistoryFragment.MODE_END_USER);

            Navigation.findNavController(HomeNavGraphSingle.this,R.id.nav_host_fragment)
                    .navigate(R.id.ordersHistoryFragment,args);

        }
    }




    void showCarts()
    {

        User user = PrefLogin.getUser(this);

        if(user==null)
        {
            // not logged in so show
            Navigation.findNavController(HomeNavGraphSingle.this,R.id.nav_host_fragment).navigate(R.id.fragmentSignInMessage);
        }
        else
        {
            Navigation.findNavController(HomeNavGraphSingle.this,R.id.nav_host_fragment).navigate(R.id.cartsListFragment);
        }
    }






    void showProfileFragment()
    {
        User user = PrefLogin.getUser(this);

        if(user==null)
        {
            // not logged in so show
            Navigation.findNavController(HomeNavGraphSingle.this,R.id.nav_host_fragment).navigate(R.id.fragmentSignInMessage);
        }
        else
        {
            Navigation.findNavController(HomeNavGraphSingle.this,R.id.nav_host_fragment).navigate(R.id.profileFragment);
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


            startService(new Intent(this, LocationService.class));
//            fetchLocation();




        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            showToastMessage("Permission Rejected");
        }



    }











    void showToastMessage(String message) {

        UtilityFunctions.showToastMessage(this,message);
    }


    boolean isDestroyed = false;




    boolean promptBackPressed = false;





    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (fragment != null) {
            fragment = fragment.getChildFragmentManager().getFragments().get(0);
        }



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

                        HomeNavGraphSingle.super.onBackPressed();

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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_by_cat_simple, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


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
    protected void onDestroy() {
        super.onDestroy();

        PrefLocation.locationSetByUser(false,this);
    }





    @Override
    public void loginSuccess() {

        Navigation.findNavController(this,R.id.nav_host_fragment).popBackStack(R.id.fragmentShopsList,true);

//        getSupportFragmentManager().popBackStackImmediate(
//                null,
//                FragmentManager.POP_BACK_STACK_INCLUSIVE
//        );
//
//        marketSelected();


        // refresh a fragment
        bottomBar.setSelectedItemId(bottomBar.getSelectedItemId());
    }




    @Override
    public void loggedOut() {

        // refresh a fragment
        bottomBar.setSelectedItemId(bottomBar.getSelectedItemId());
    }
}
