package org.nearbyshops.whitelabelapp.PushFirebase;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.nearbyshops.whitelabelapp.Home;
import org.nearbyshops.whitelabelapp.R;


public class MessagingService extends FirebaseMessagingService
{


    // notification types
    public static final String NOTIFICATION_TYPE_ORDER_RECEIVED = "NOTIFICATION_TYPE_ORDER_RECEIVED";
    public static final String NOTIFICATION_TYPE_ORDER_STATUS_UPDATES = "NOTIFICATION_TYPE_ORDER_STATUS_UPDATES";
    public static final String NOTIFICATION_TYPE_SHOP_CREATED = "NOTIFICATION_TYPE_SHOP_CREATED";
    public static final String NOTIFICATION_TYPE_DELIVERY_GUY_LOCATION_UPDATES = "NOTIFICATION_TYPE_DELIVERY_LOCATION_UPDATES";
    public static final String NOTIFICATION_TYPE_GENERAL = "NOTIFICATION_TYPE_GENERAL";


    // public notification channels for firebase push notifications
    public static final String CHANNEL_END_USER = "end_user_"; // for every user
    public static final String CHANNEL_END_USER_WITH_USER_ID = "end_user_"; // to a specific user with id

    public static final String CHANNEL_DELIVERY_STAFF = "delivery_staff_"; // for addressing all delivery boys on the platform
    public static final String CHANNEL_DELIVERY_STAFF_WITH_USER_ID = "delivery_staff_"; // for broadcasting location to the customer

    public static final String CHANNEL_SHOP = "shop_staff_"; // for addressing all the vendors on the platform
    public static final String CHANNEL_SHOP_WITH_SHOP_ID = "shop_staff_"; // shop with shop id

    public static final String CHANNEL_STAFF = "_staff"; // for admin and staff members





    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);



//        System.out.println("OnMessageReceived()");
//        System.out.println("Data : " + remoteMessage.getData());
//        System.out.println("Notification Type :" + remoteMessage.getData().get("notification_type"));



        String notificationType = remoteMessage.getData().get("notification_type");
        String notificationTitle = remoteMessage.getData().get("notification_title");
        String notificationMessage = remoteMessage.getData().get("notification_message");




        if(notificationType!=null && notificationType.equals(NOTIFICATION_TYPE_ORDER_RECEIVED))
        {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.attention_please_order_received);
            mp.start();
        }


        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, Home.class);


        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_shopping_basket_blue)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }



    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);

    }

    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }




}
