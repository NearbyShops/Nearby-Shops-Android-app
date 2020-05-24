package org.nearbyshops.enduserappnew.Utility;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
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


//        FirebaseApp.getInstance().delete();

        User user = PrefLogin.getUser(MyApplication.getAppContext());
        Market localConfig = PrefServiceConfig.getServiceConfigLocal(MyApplication.getAppContext());


        if(user==null || localConfig==null || localConfig.getRt_market_id_for_fcm()==null)
        {

            System.out.println("Subscription Failed ... Returned ! ");
            return;
        }



        System.out.println("Update FCM Subscription ! ");


        FirebaseApp.initializeApp(MyApplication.getAppContext());

        String topic_name = localConfig.getRt_market_id_for_fcm()  + "end_user_" + user.getUserID();

        FirebaseMessaging.getInstance().subscribeToTopic(topic_name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + topic_name);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        System.out.println("Failed : " + e.toString());
                    }
                })
        .addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                System.out.println("Cancelled : ");
            }
        })
        ;



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


        String topic_name = localConfig.getRt_market_id_for_fcm() + "shop_" + shop.getShopID();


        System.out.println("Update FCM Subscription for Shop ! ");


        try {


            FirebaseMessaging.getInstance().subscribeToTopic(topic_name)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            System.out.println("Subscribed to : " + topic_name);

                        }
                    });


        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();

        }


    }




    public static void showToastMessage(String message, Context context)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }






    public static void logout(Context context)
    {
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


}
