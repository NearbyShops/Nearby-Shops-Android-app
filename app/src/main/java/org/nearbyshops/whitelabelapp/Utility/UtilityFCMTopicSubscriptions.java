package org.nearbyshops.whitelabelapp.Utility;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStaff.DeliveryGuyData;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefDeliveryGuyHome;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefMarketAdminHome;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.PushFirebase.MessagingService;



public class UtilityFCMTopicSubscriptions {



    public static void subscribeToAllTopics()
    {

        System.out.println("Update FCM Subscriptions ! ");


        FirebaseMessaging.getInstance()
                .subscribeToTopic(MessagingService.CHANNEL_END_USER)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : "  + MessagingService.CHANNEL_END_USER);

                    }
                });



        // update topic subscriptions for fcm
        User user = PrefLogin.getUser(MyApplication.getAppContext());


        if(user==null)
        {

            System.out.println("Subscription Failed ... due to user not logged in ... Returned ! ");
            return;
        }




        FirebaseApp.initializeApp(MyApplication.getAppContext());


        FirebaseMessaging.getInstance()
                .subscribeToTopic(MessagingService.CHANNEL_END_USER_WITH_USER_ID + user.getUserID())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + MessagingService.CHANNEL_END_USER_WITH_USER_ID + user.getUserID());

                    }
                });




        if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE||user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE)
        {
//            subscribeForDeliveryStaffTopics();
        }


        if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE||user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {
            subscribeToShopTopics();
        }



        if(user.getRole()==User.ROLE_ADMIN_CODE||user.getRole()==User.ROLE_STAFF_CODE)
        {
            subscribeToStaffTopics();
        }

    }



    public static void subscribeForDeliveryStaffTopics()
    {

        FirebaseMessaging.getInstance()
                .subscribeToTopic(MessagingService.CHANNEL_DELIVERY_STAFF)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + MessagingService.CHANNEL_DELIVERY_STAFF);

                    }
                });


        DeliveryGuyData deliveryData = PrefDeliveryGuyHome.getDeliveryData(MyApplication.getAppContext());

        if(deliveryData==null)
        {
            return;
        }

        FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.CHANNEL_DELIVERY_STAFF_WITH_USER_ID + deliveryData.getUserID())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + MessagingService.CHANNEL_DELIVERY_STAFF_WITH_USER_ID + deliveryData.getUserID());

                    }
                });
    }



    public static void subscribeToShopTopics()
    {
//        FirebaseApp.getInstance().delete();
//        FirebaseApp.initializeApp(getApplicationContext());


        FirebaseMessaging.getInstance()
                .subscribeToTopic(MessagingService.CHANNEL_SHOP)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + MessagingService.CHANNEL_SHOP);

                    }
                });


        Shop shop = PrefShopAdminHome.getShop(MyApplication.getAppContext());


        if(shop==null)
        {
            return;
        }



        FirebaseMessaging.getInstance()
                .subscribeToTopic(MessagingService.CHANNEL_SHOP_WITH_SHOP_ID + shop.getShopID())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + MessagingService.CHANNEL_SHOP_WITH_SHOP_ID + shop.getShopID());

                    }
                });


    }



    public static void subscribeToStaffTopics()
    {
        FirebaseMessaging.getInstance()
                .subscribeToTopic(MessagingService.CHANNEL_STAFF)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + MessagingService.CHANNEL_STAFF);

                    }
                });
    }



    public static void unsubscribeFromTopics()
    {


        // update topic subscriptions for fcm
        User user = PrefLogin.getUser(MyApplication.getAppContext());


        if(user==null)
        {

            System.out.println("Unsubscribe Failed ... Returned ! ");
            return;
        }



        System.out.println("Unsubscribe FCM Topics ! ");


        FirebaseApp.initializeApp(MyApplication.getAppContext());


        FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(MessagingService.CHANNEL_END_USER_WITH_USER_ID + user.getUserID())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("UnSubscribed from : "  + MessagingService.CHANNEL_END_USER_WITH_USER_ID + user.getUserID());

                    }
                });




        FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(MessagingService.CHANNEL_END_USER)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Unsubscribed from  : "  + MessagingService.CHANNEL_END_USER);

                    }
                });




        if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE)
        {
            unsubscribeDeliveryTopics();
        }




        if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE||user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {

            FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(MessagingService.CHANNEL_SHOP)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Unsubscribed from : " + MessagingService.CHANNEL_SHOP);

                        }
                    });



            Shop shop = PrefShopAdminHome.getShop(MyApplication.getAppContext());

            if(shop==null)
            {
                return;
            }



            FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(MessagingService.CHANNEL_SHOP_WITH_SHOP_ID + shop.getShopID())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("unsubscribed from : " + MessagingService.CHANNEL_SHOP_WITH_SHOP_ID + shop.getShopID());

                        }
                    });
        }




        if(user.getRole()==User.ROLE_ADMIN_CODE||user.getRole()==User.ROLE_STAFF_CODE)
        {
            FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(MessagingService.CHANNEL_STAFF)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Unsubscribed from : " + MessagingService.CHANNEL_STAFF);

                        }
                    });
        }


    }



    public static void unsubscribeDeliveryTopics()
    {


        FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(MessagingService.CHANNEL_DELIVERY_STAFF)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Unsubscribed from : " + MessagingService.CHANNEL_DELIVERY_STAFF);

                    }
                });




        DeliveryGuyData deliveryData = PrefDeliveryGuyHome.getDeliveryData(MyApplication.getAppContext());

        if(deliveryData==null)
        {
            return;
        }

        FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(MessagingService.CHANNEL_DELIVERY_STAFF_WITH_USER_ID + deliveryData.getUserID())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Unsubscribed from : " + MessagingService.CHANNEL_DELIVERY_STAFF_WITH_USER_ID + deliveryData.getUserID());

                    }
                });
    }


}
