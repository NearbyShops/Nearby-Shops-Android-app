package org.nearbyshops.enduserappnew.Services.NonStopServices;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessaging;

import org.nearbyshops.enduserappnew.API.DeliveryGuyLoginService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelRoles.DeliveryGuyData;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by sumeet on 10/4/17.
 */


public class PersistentLocationService extends NonStopIntentService{


    LocationManager locationManager;
    LocationListener locationListener;


    @Inject
    DeliveryGuyLoginService guyLoginService;



    @Override
    public void onCreate() {
        super.onCreate();


        String NOTIFICATION_CHANNEL_ID = "";


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NOTIFICATION_CHANNEL_ID = "org.sarthika.driverapp.notify";
            String channelName = "Sarthika Location Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }



//        "channel_one"
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_account_box_black_24px)
                        .setContentTitle(getResources().getString(R.string.app_name) + " Location Service")
                        .setTicker("ticker text")
                        .setContentText("Your location is being updated.");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, mBuilder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
        }


        requestLocationUpdates();

        // Location_ code ends
    }





    public PersistentLocationService() {
        super("name");

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

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
            return;
        }



        logMessage("Location Permission Exist !");


        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                updateLocation(location);



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









    void updateLocation(Location location)
    {
        DeliveryGuyData data = new DeliveryGuyData();
        data.setLatCurrent(location.getLatitude());
        data.setLonCurrent(location.getLongitude());

        Call<ResponseBody> call = guyLoginService.updateLocation(PrefLogin.getAuthorizationHeaders(getApplicationContext()),
                data);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {
                    UtilityFunctions.showToastMessage(getApplicationContext(),"Location Updated !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }






    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }





    void logMessage(String message)
    {
        Log.d("location_service",message);
    }



    protected void stopLocationUpdates() {

        if(locationManager!=null && locationListener!=null)
        {
            locationManager.removeUpdates(locationListener);
            logMessage("location updates removed !");
        }
    }


}
