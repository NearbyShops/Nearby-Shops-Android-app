package org.nearbyshops.whitelabelapp.Services;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;


public class LocationServiceBackup extends IntentService {


    public static String INTENT_ACTION_LOCATION_UPDATED = "intent_location_updated";


    public LocationServiceBackup() {
        super("location_update_service");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

//        showToastMessage("Fetch Location Started !");
        fetchLocation();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }





    private LocationRequest mLocationRequestTwo;
    private LocationCallback locationCallback;


    void fetchLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
                super.onLocationResult(locationResult);


                double lat = 0;
                double lon = 0;

                Location location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);

                lat = location.getLatitude();
                lon = location.getLongitude();


//                PrefLocation.saveLatLonCurrent(lat,lon,getApplicationContext());

                saveLocation(location);


                stopLocationUpdates();


            }


            @Override
            public void onLocationAvailability(com.google.android.gms.location.LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };


//        LocationServices.getFusedLocationProviderClient(getApplicationContext())
//                .requestLocationUpdates(mLocationRequestTwo, locationCallback, getMainLooper());


        LocationServices.getFusedLocationProviderClient(this)
                .getLastLocation()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }


                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .requestLocationUpdates(mLocationRequestTwo, locationCallback, getMainLooper());


                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {


                        if (location == null) {

                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }


                            LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                    .requestLocationUpdates(mLocationRequestTwo, locationCallback, getMainLooper());

                            return;
                        }




                        saveLocation(location);
                    }
                });
    }












    void saveLocation(Location location)
    {
        Location currentLocation = PrefLocation.getLocationSelected(this);

//        System.out.println("Distance To : " + currentLocation.distanceTo(location));


        if(currentLocation.distanceTo(location)>300 && !PrefLocation.isLocationSetByUser(getApplicationContext()))
        {
            // save location only if there is a significant change in location

            showToastMessage("Location Updated !");

            Intent intent = new Intent();
            intent.setAction(INTENT_ACTION_LOCATION_UPDATED);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

//            PrefLocation.saveLatitude((float) location.getLatitude(), getApplicationContext());
//            PrefLocation.saveLongitude((float) location.getLongitude(), getApplicationContext());
            PrefLocation.saveLatLonSelected(location.getLatitude(),location.getLongitude(),getApplicationContext());


            stopLocationUpdates();

        }


        PrefLocation.saveLatLonCurrent((float) location.getLatitude(),(float) location.getLongitude(),getApplicationContext());

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
        UtilityFunctions.showToastMessage(MyApplication.getAppContext(),message);
    }





}
