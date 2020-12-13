package org.nearbyshops.enduserappnew.PushFirebase;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.nearbyshops.enduserappnew.HomeSingleMarket;
import org.nearbyshops.enduserappnew.R;


public class MessagingService extends FirebaseMessagingService
{

    public static final String NOTIFICATION_TYPE_ORDER_RECEIVED = "NOTIFICATION_TYPE_ORDER_RECEIVED";
    public static final String NOTIFICATION_TYPE_ORDER_PACKED = "NOTIFICATION_TYPE_ORDER_PACKED";
    public static final String NOTIFICATION_TYPE_ORDER_UPDATES = "NOTIFICATION_TYPE_ORDER_UPDATES";
    public static final String NOTIFICATION_TYPE_GENERAL = "NOTIFICATION_TYPE_GENERAL";


    // public notification channels for firebase push notifications
    public static final String CHANNEL_END_USER = "_end_user";
    public static final String CHANNEL_SHOP_STAFF = "_shop_staff";
    public static final String CHANNEL_MARKET_STAFF = "market_staff";
    public static final String CHANNEL_DELIVERY_STAFF = "delivery_staff";


    public static final String CHANNEL_SHOP_WITH_SHOP_ID = "shop_";
    public static final String CHANNEL_END_USER_WITH_USER_ID = "end_user_";






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
        Intent intent = new Intent(this, HomeSingleMarket.class);


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
