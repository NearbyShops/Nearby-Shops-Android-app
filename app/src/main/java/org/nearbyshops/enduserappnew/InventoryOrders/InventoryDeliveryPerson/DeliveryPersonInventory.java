package org.nearbyshops.enduserappnew.InventoryOrders.InventoryDeliveryPerson;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import org.nearbyshops.enduserappnew.Interfaces.NotifyLocation;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserappnew.Interfaces.RefreshAdjacentFragment;
import org.nearbyshops.enduserappnew.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.InventoryOrders.InventoryDeliveryPerson.FragmentDeprecated.DeliveryInventoryFragment;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Services.NonStopServices.PersistentLocationService;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DeliveryPersonInventory extends AppCompatActivity implements NotifyTitleChanged, RefreshAdjacentFragment {



    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @BindView(R.id.tablayout) TabLayout tabLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_self_inventory);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


//        deliveryGuySelf = getIntent().getParcelableExtra(DELIVERY_GUY_INTENT_KEY_DASHBOARD);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        startService(new Intent(this, PersistentLocationService.class));
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(new Intent(this, PersistentLocationService.class));
    }





    public static void start(int screenMode, Context context)
    {

        Intent intent = new Intent(context, DeliveryPersonInventory.class);
        intent.putExtra(DeliveryInventoryFragment.SCREEN_MODE_INTENT_KEY,screenMode);
        context.startActivity(intent);
    }




    private void saveLocation(Location mLastLocation) {
//        locationResult=mLastLocation;
//        makeRefreshNetworkCall();

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(makeFragmentName(mViewPager.getId(),1));

        if(fragment instanceof NotifyLocation)
        {
            ((NotifyLocation) fragment).fetchedLocation(mLastLocation);
        }

    }






    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }






//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_vehicle_dashboard, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the ShopList/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {

        mPagerAdapter.setTitle(title,tabPosition);
    }


    void showToastMessage(String message)
    {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_history_hd, menu);


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

                Fragment fragment = getSupportFragmentManager()
                        .findFragmentByTag(
                                makeFragmentName(mViewPager.getId(),mViewPager.getCurrentItem())
                        );


                if(fragment instanceof NotifySearch)
                {
                    ((NotifySearch) fragment).endSearchMode();
                }


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

//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

            Fragment fragment = getSupportFragmentManager()
                    .findFragmentByTag(
                            makeFragmentName(mViewPager.getId(),mViewPager.getCurrentItem())
                    );


            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }





    LocationManager locationManager;
    LocationListener locationListener;

    LocationRequest mLocationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 2;






    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    startSettingsCheck();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.


                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }







    void requestLocationUpdates()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);


            return;
        }




        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                onLocationUpdate(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };





        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, locationListener);


//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);
//
//        locationManager.requestLocationUpdates( 15000, 100,criteria, locationListener,null);

    }






    public void onLocationUpdate(Location location) {


        PrefLocation.saveLatLon(
                location.getLatitude(),location.getLongitude(),
                getApplicationContext()
        );

        showToastMessage("Location Updated !");

        saveLocation(location);
    }





    protected void stopLocationUpdates() {

        if(locationManager!=null && locationListener!=null)
        {
            locationManager.removeUpdates(locationListener);
        }
    }









    void startSettingsCheck()
    {

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
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);

            return;
        }



        mLocationRequest = LocationRequest.create();
        mLocationRequest.setSmallestDisplacement(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        checkLocationSettings();
    }





    // code to check location settings and ask for location permission if it has not been already provided

    protected void checkLocationSettings() {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                requestLocationUpdates();
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
                        ResolvableApiException resolvable = (ResolvableApiException)e;
                        resolvable.startResolutionForResult(DeliveryPersonInventory.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }


                }
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }






    @Override
    public void refreshAdjacentFragment() {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(
                        makeFragmentName(mViewPager.getId(),mViewPager.getCurrentItem()+1)
                );


        if(fragment instanceof RefreshFragment)
        {
            ((RefreshFragment) fragment).refreshFragment();
        }
    }



}
