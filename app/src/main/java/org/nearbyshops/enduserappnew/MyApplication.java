package org.nearbyshops.enduserappnew;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.libraries.places.api.Places;
import com.mapbox.mapboxsdk.Mapbox;




/**
 * Created by sumeet on 12/5/16.
 */
public class MyApplication extends MultiDexApplication {

    private static Context context;
    public static MyApplication application;


    public static String SORT_DESCENDING = " DESC NULLS LAST ";
    public static String SORT_ASCENDING = " ASC NULLS LAST ";






    // end-user notifications
    public static final int ORDER_PLACED = 21;
    public static final int ORDER_CONFIRMED = 22;
    public static final int ORDER_PACKED = 23;
    public static final int ORDER_OUT_FOR_DELIVERY = 24;

    public static final int ORDER_CANCELLED_BY_SHOP = 25;





    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }




    public void onCreate() {

        super.onCreate();

        MyApplication.context = getApplicationContext();
        MyApplication.application = this;

        Places.initialize(getApplicationContext(), "AIzaSyAHjmh3U3OVYngo6huNoEpYhscFqcV9CFA");


        ApplicationState.getInstance().setMyApplication(this);
        Mapbox.getInstance(this,getString(R.string.fake_key));
        createNotificationChannel();
    }



    public static Context getAppContext() {
        return MyApplication.context;
    }




    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }



        System.out.println("Notification Channel Created !");
    }


}
