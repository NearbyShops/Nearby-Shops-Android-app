package org.nearbyshops.enduserappnew.PushFirebase;

import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.nearbyshops.enduserappnew.R;


public class MessagingService extends FirebaseMessagingService
{



    public static final String NOTIFICATION_ORDER_RECIEVED = "NOTIFICATION_ORDER_RECIEVED";
    public static final String NOTIFICATION_ORDER_PACKED = "NOTIFICATION_ORDER_PACKED";



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


//        System.out.println("OnMessageReceived()");


//        if(remoteMessage.getNotification()!=null)
//        {
//            System.out.println("Message : " + remoteMessage.getNotification().getBody());
//            System.out.println("Data : " + remoteMessage.getData());
//            System.out.println("Notification Type :" + remoteMessage.getData().get("notification_type"));
//        }



        String notificationType = remoteMessage.getData().get("notification_type");




        if(notificationType!=null && notificationType.equals(NOTIFICATION_ORDER_RECIEVED))
        {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.attention_please_order_received);
            mp.start();


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.ic_shopping_basket_blue)
                    .setContentTitle("Order Received")
                    .setContentText("You have Received an Order. Please check the order in the shop dashboard !")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(1, builder.build());
        }
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
