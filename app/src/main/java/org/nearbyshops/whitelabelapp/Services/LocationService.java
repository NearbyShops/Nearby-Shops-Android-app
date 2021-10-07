package org.nearbyshops.whitelabelapp.Services;


import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;


public class LocationService extends IntentService {


    public static String INTENT_ACTION_LOCATION_UPDATED = "intent_location_updated";

    LocationRequest locationRequest;
    LocationCallback locationCallback;

    LocationManager locationManager;
    LocationListener locationListener;


    int updateCount = 1;


    public LocationService() {
        super("location_update_service");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

//        showToastMessage("Fetching Location !");

        showLog("Fetching Location !");
        fetchLocationFused();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        stopLocationUpdates();
        stopLocationUpdatePrimitive();
    }


    void fetchLocationFusedBasic() {


//        showToastMessage("Fetching Location !");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            showToastMessage("Not Permitted !");
            return;
        }


        LocationServices.getFusedLocationProviderClient(this)
                .getLastLocation()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showLog("Get Last Location fetch Failed !");

                        fetchLocationByPrimitiveMethod();
                    }

                })
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            showLog("Get Last Location null !");
                            saveLocation(location);
                        }


                        fetchLocationByPrimitiveMethod();
                    }
                });
    }



    void fetchLocationFused() {


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setSmallestDisplacement(50);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);


                double lat = 0;
                double lon = 0;

//                Location location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);


                for (Location location : locationResult.getLocations()) {
                    saveLocation(location);
                }


                stopLocationUpdates();

            }


            @Override
            public void onLocationAvailability(com.google.android.gms.location.LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

//            showToastMessage("Not Permitted !- Location");
            return;
        }


        LocationServices.getFusedLocationProviderClient(this)
                .getLastLocation()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showLog("Get Last Location fetch Failed !");

                        if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                .requestLocationUpdates(locationRequest, locationCallback, getMainLooper());


                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            showLog("Last Location not null !");
                            saveLocation(location);
                        }

                        // sometimes the location fetched by previous method is not accurate therefore
                        // fetch location other way to ensure the location fetched is more accurate

                        if (ActivityCompat.checkSelfPermission(LocationService.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                .requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
                    }
                });


    }



    void fetchLocationByPrimitiveMethod()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }



        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                // Called when a new location is found by the network location provider.

                if(location==null)
                {
                    showLog("Location Basic is null !");
                }



                saveLocation(location);

                stopLocationUpdatePrimitive();



                if(updateCount==3)
                {
                }

                updateCount = updateCount+1;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };





        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);

//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);
//
//        locationManager.requestLocationUpdates( 15000, 100,criteria, locationListener,null);

    }







    void saveLocation(Location location)
    {
        Location currentLocation = PrefLocation.getLocationCurrent(this);

        showLog("Distance To : " + currentLocation.distanceTo(location));
        showLog("Saving Location !");

        if(currentLocation.distanceTo(location)>300)
        {
            // save location only if there is a significant change in location
            showLog("Location Updated !");
            showToastMessage("Location Updated !");

            Intent intent = new Intent();
            intent.setAction(INTENT_ACTION_LOCATION_UPDATED);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

//            PrefLocation.saveLatitude((float) location.getLatitude(), getApplicationContext());
//            PrefLocation.saveLongitude((float) location.getLongitude(), getApplicationContext());
//            PrefLocation.saveLatLonSelected(location.getLatitude(),location.getLongitude(),getApplicationContext());

            PrefLocation.saveLatLonCurrent((float) location.getLatitude(),(float) location.getLongitude(),getApplicationContext());

        }

    }





    protected void stopLocationUpdates() {

        if(locationCallback!=null)
        {
            LocationServices.getFusedLocationProviderClient(getApplicationContext())
                    .removeLocationUpdates(locationCallback);
        }


        fetchLocationByPrimitiveMethod();
    }




    void stopLocationUpdatePrimitive()
    {
        if(locationManager!=null && locationListener!=null)
        {
            locationManager.removeUpdates(locationListener);
        }
    }







    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(MyApplication.getAppContext(),message);
    }



    void showLog(String message)
    {
        System.out.println(message);
    }




}
