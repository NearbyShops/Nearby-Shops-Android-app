package org.nearbyshops.enduserappnew.Services;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Home;
import org.nearbyshops.enduserappnew.Interfaces.LocationUpdated;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationUpdateService extends IntentService {




    public static String INTENT_ACTION_LOCATION_UPDATED = "intent_location_updated";



    public LocationUpdateService() {
        super("location_update_service");
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        fetchLocation();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }





    private com.google.android.gms.location.LocationRequest mLocationRequestTwo;
    private com.google.android.gms.location.LocationCallback locationCallback;



    void fetchLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            System.out.println("We cannot access Location ... Please grant permission for Location access !");

            return;
        }





        mLocationRequestTwo = LocationRequest.create();
        mLocationRequestTwo.setInterval(1000);
        mLocationRequestTwo.setSmallestDisplacement(10);
        mLocationRequestTwo.setFastestInterval(1000);
        mLocationRequestTwo.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);



//        locationCallback = new MyLocationCallback();


        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
                super.onLocationResult(locationResult);


                double lat = 0;
                double lon = 0;

                Location location = locationResult.getLocations().get(locationResult.getLocations().size()-1);

                lat = location.getLatitude();
                lon = location.getLongitude();



//                PrefLocation.saveLatLonCurrent(lat,lon,getApplicationContext());

                saveLocation(location);


                stopLocationUpdates();

                showToastMessage("Location Updated !");


            }


            @Override
            public void onLocationAvailability(com.google.android.gms.location.LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };




        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                .requestLocationUpdates(mLocationRequestTwo,locationCallback, getMainLooper());



        com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this)
                .getLastLocation()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .requestLocationUpdates(mLocationRequestTwo,locationCallback, null);


                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {


                        if(location==null)
                        {

                            com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                    .requestLocationUpdates(mLocationRequestTwo,locationCallback, null);

                            return;
                        }

                        saveLocation(location);
                    }
                });



    }




    void saveLocation(Location location)
    {
        Location currentLocation = PrefLocation.getLocation(this);

//        System.out.println("Distance To : " + currentLocation.distanceTo(location));

        if(currentLocation.distanceTo(location)>300)
        {
            // save location only if there is a significant change in location

            Intent intent = new Intent();
            intent.setAction(INTENT_ACTION_LOCATION_UPDATED);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

            PrefLocation.saveLatitude((float) location.getLatitude(), getApplicationContext());
            PrefLocation.saveLongitude((float) location.getLongitude(), getApplicationContext());
        }

    }





    protected void stopLocationUpdates() {

        if(locationCallback!=null)
        {
            LocationServices.getFusedLocationProviderClient(getApplicationContext())
                    .removeLocationUpdates(locationCallback);
        }
    }






    private void showToastMessage(String message)
    {
        Toast.makeText(MyApplication.getAppContext(),message,Toast.LENGTH_SHORT).show();
    }





}
