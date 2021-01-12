package org.nearbyshops.enduserappnew.Utility;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;
import org.nearbyshops.enduserappnew.PushFirebase.MessagingService;


/**
 * Created by sumeet on 10/7/17.
 */

public class UtilityFunctions {

    public static final String TAG_LOG = "app_log";






    /* Utility Functions */
    public static Gson provideGson() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
    }




    public static String refinedString(double number)
    {
        if(number % 1 !=0)
        {
            // contains decimal numbers

            return String.format("%.2f",number);
        }
        else
        {
            return String.format("%.0f",number);
        }
    }


    public static String refinedStringWithDecimals(double number)
    {
        return String.format("%.2f",number);
    }






    public static void updateFirebaseSubscriptions()
    {


        // update topic subscriptions for fcm
        User user = PrefLogin.getUser(MyApplication.getAppContext());
        Market localConfig = PrefServiceConfig.getServiceConfigLocal(MyApplication.getAppContext());


        if(user==null || localConfig==null || localConfig.getRt_market_id_for_fcm()==null)
        {

            System.out.println("Subscription Failed ... Returned ! ");
            return;
        }



        System.out.println("Update FCM Subscription ! ");


        FirebaseApp.initializeApp(MyApplication.getAppContext());



        FirebaseMessaging.getInstance()
                .subscribeToTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_END_USER_WITH_USER_ID + user.getUserID())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_END_USER_WITH_USER_ID + user.getUserID());

                    }
                });




        FirebaseMessaging.getInstance()
                .subscribeToTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_END_USER)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_END_USER);

                    }
                });






        if(user.getRole()==User.ROLE_ADMIN_CODE||user.getRole()==User.ROLE_STAFF_CODE)
        {

            FirebaseMessaging.getInstance()
                    .subscribeToTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_MARKET_STAFF)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Subscribed to : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_MARKET_STAFF);

                        }
                    });
        }





        if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE||user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {

            FirebaseMessaging.getInstance()
                    .subscribeToTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_SHOP_STAFF)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Subscribed to : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_SHOP_STAFF);

                        }
                    });
        }




        if(user.getRole()==User.ROLE_DELIVERY_GUY_CODE)
        {

            FirebaseMessaging.getInstance()
                    .subscribeToTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_DELIVERY_STAFF)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Subscribed to : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_DELIVERY_STAFF);

                        }
                    });
        }


    }



    public static void updateFirebaseSubscriptionsForShop()
    {
//        FirebaseApp.getInstance().delete();
//        FirebaseApp.initializeApp(getApplicationContext());



        Shop shop = PrefShopAdminHome.getShop(MyApplication.getAppContext());
        Market localConfig = PrefServiceConfig.getServiceConfigLocal(MyApplication.getAppContext());


        if(shop==null || localConfig==null || localConfig.getRt_market_id_for_fcm()==null)
        {
            return;
        }



        System.out.println("Update FCM Subscription for Shop ! ");


        try {


            FirebaseMessaging.getInstance()
                    .subscribeToTopic(localConfig.getRt_market_id_for_fcm() + MessagingService.CHANNEL_SHOP_WITH_SHOP_ID + shop.getShopID())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Subscribed to : " + localConfig.getRt_market_id_for_fcm() + MessagingService.CHANNEL_SHOP_WITH_SHOP_ID + shop.getShopID());

                        }
                    });


        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();

        }


    }



    public static void unsubscribeFCM_Topics()
    {


        // update topic subscriptions for fcm
        User user = PrefLogin.getUser(MyApplication.getAppContext());
        Market localConfig = PrefServiceConfig.getServiceConfigLocal(MyApplication.getAppContext());


        if(user==null || localConfig==null || localConfig.getRt_market_id_for_fcm()==null)
        {

            System.out.println("Unsubscribe Failed ... Returned ! ");
            return;
        }



        System.out.println("Unsubscribe FCM Topics ! ");


        FirebaseApp.initializeApp(MyApplication.getAppContext());


        FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_END_USER_WITH_USER_ID + user.getUserID())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("UnSubscribed from : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_END_USER_WITH_USER_ID + user.getUserID());

                    }
                });





        FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_END_USER)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Unsubscribed from  : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_END_USER);

                    }
                });






        if(user.getRole()==User.ROLE_ADMIN_CODE||user.getRole()==User.ROLE_STAFF_CODE)
        {


            FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_MARKET_STAFF)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Unsubscribed from : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_MARKET_STAFF);

                        }
                    });
        }





        if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE||user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {


            FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_SHOP_STAFF)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Unsubscribed from : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_SHOP_STAFF);
                        }
                    });
        }




        if(user.getRole()==User.ROLE_DELIVERY_GUY_CODE)
        {


            FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_DELIVERY_STAFF)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Unsubscribed from : " + localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_DELIVERY_STAFF);

                        }
                    });
        }


    }





    public static void logout(Context context)
    {

        unsubscribeFCM_Topics();

        // log out
        PrefLogin.saveUserProfile(null,context);
        PrefLogin.saveCredentialsPassword(context,null,null);

        PrefLoginGlobal.saveUserProfile(null,context);
        PrefLoginGlobal.saveCredentialsPassword(context,null,null);

        PrefShopHome.saveShop(null,context);
        PrefShopAdminHome.saveShop(null,context);


//        FirebaseApp.getInstance().delete();
    }





    public static void openURL(String url, Context activity)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }




    public static void dialPhoneNumber(String phoneNumber,Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }







    public static void showToastMessage(Context context,String message)
    {
        if(context!=null)
        {
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
        }
    }


}
