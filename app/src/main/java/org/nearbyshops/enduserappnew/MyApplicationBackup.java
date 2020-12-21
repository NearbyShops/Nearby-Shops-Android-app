package org.nearbyshops.enduserappnew;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.libraries.places.api.Places;
import com.mapbox.mapboxsdk.Mapbox;


//import com.onesignal.OSNotification;
//import com.onesignal.OSNotificationOpenResult;
//import com.onesignal.OneSignal;


/**
 * Created by sumeet on 12/5/16.
 */
public class MyApplicationBackup extends MultiDexApplication {

    private static Context context;
    public static MyApplicationBackup application;


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

        MyApplicationBackup.context = getApplicationContext();
        MyApplicationBackup.application = this;




        //LeakCanary.install(this);
//        ApplicationState.getInstance().setMyApplication(this);
        Mapbox.getInstance(this,getString(R.string.fake_key));
        createNotificationChannel();


//
//
//        OneSignal.startInit(this)
//                .setNotificationReceivedHandler(new OneSignal.NotificationReceivedHandler() {
//                    @Override
//                    public void notificationReceived(OSNotification notification) {
//
//
////                        Log.d("one_signal",notification.toJSONObject().toString());
//
//                        Log.d("one_signal",notification.payload.additionalData.toString());
//
//                        JSONObject data = notification.payload.additionalData;
//
//
////                        int screenToOpen = -1;
//                        int notificationType = -1;
//
//
//                        if (data != null) {
//
////                            screenToOpen = data.optInt("screen_to_open",-1);
//                            notificationType = data.optInt("notification_type",-1);
//
//
//
//
//                            if(notificationType== MyApplicationCoreNew.ORDER_PLACED ||
//                                    notificationType== MyApplicationCoreNew.ORDER_PACKED ||
//                                    notificationType== MyApplicationCoreNew.ORDER_CONFIRMED||
//                                    notificationType== MyApplicationCoreNew.ORDER_OUT_FOR_DELIVERY)
//                            {
//                                PrefBadgeCount.saveBadgeCountOrders(PrefBadgeCount.getBadgeCountOrders(getApplicationContext())+1,
//                                        getApplicationContext());
//                            }
//
//
//
//
////                            if(notificationType==MyApplicationCoreNew.PICKUP_STARTED ||
////                                    notificationType== MyApplicationCoreNew.TRIP_STARTED ||
////                                    notificationType==MyApplicationCoreNew.TRIP_FINISHED ||
////                                    notificationType==MyApplicationCoreNew.TRIP_CANCELLED_BY_DRIVER)
////                            {
////
////                                PrefBadgeCount.saveBadgeCountCarts(PrefBadgeCount.getBadgeCountCarts(getApplicationContext())+1,
////                                        getApplicationContext());
////                            }
//
//
//
//                            NotificationEvent event = new NotificationEvent(notificationType);
//                            EventBus.getDefault().post(event);
//
//                        }
//
//
//
//
//                    }
//                })
//                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
//                    @Override
//                    public void notificationOpened(OSNotificationOpenResult result) {
//
//                        JSONObject data = result.notification.payload.additionalData;
//
////                        Gson gson = UtilityFunctions.provideGson();
////                        OneSignalData oneSignalData= gson.fromJson(data.toString(), OneSignalData.class);
//
//                        int screenToOpen = -1;
//                        int notificationType = -1;
//
//
//                        if (data != null) {
//                            screenToOpen = data.optInt("screen_to_open",-1);
//                            notificationType = data.optInt("notification_type",-1);
//
//                            if (screenToOpen != -1)
//                                Log.i("one_signal", "Screen To Open: " + String.valueOf(screenToOpen));
//                            Log.i("one_signal", "Notificaion Type : " + String.valueOf(notificationType));
//                        }
//
//
//
//
//                        Intent intent = new Intent(getApplicationContext(), Home.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        intent.putExtra("screen_to_open",screenToOpen);
//
//                        startActivity(intent);
//
//                        Log.d("one_signal","Notification Opened !");
//
//
//
//                    }
//                })
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();




//        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//            @Override
//            public void idsAvailable(String userId, String registrationId) {
//
//
//                Log.d("one_signal", "User:" + userId);
//                Log.d("one_signal", "registrationId:" + registrationId);
//
//
//                PrefOneSignal.saveLastToken(getApplicationContext(),userId);
//                PrefOneSignal.saveToken(getApplicationContext(),userId);
//
//
//
////                if (PrefGeneral.getServiceURL(getApplicationContext())!=null) {
////                    startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
////                }
//
//
//
////                getApplicationContext().startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
//            }
//
//        });
    }



    public static Context getAppContext() {
        return MyApplicationBackup.context;
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
