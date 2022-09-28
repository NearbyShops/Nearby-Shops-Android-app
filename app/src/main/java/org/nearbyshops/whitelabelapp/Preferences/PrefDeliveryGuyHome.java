package org.nearbyshops.whitelabelapp.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.Model.ModelStaff.DeliveryGuyData;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFCMTopicSubscriptions;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */



public class PrefDeliveryGuyHome {


    public static void saveDeliveryGuyStatus(int status, Context context)
    {
        context = MyApplication.getAppContext();

        if(context == null)
        {
            return;
        }

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        prefsEditor.putInt("delivery_guy_status", status);
        prefsEditor.apply();


        if(status==DeliveryGuyData.STATUS_ONLINE)
        {
            // update firebase subscriptions whenever shop information is updated
            UtilityFCMTopicSubscriptions.subscribeForDeliveryStaffTopics();
        }
        else
        {
            UtilityFCMTopicSubscriptions.unsubscribeDeliveryTopics();
        }
    }



    public static int getDeliveryStatus(Context context)
    {
        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getInt("delivery_guy_status", DeliveryGuyData.STATUS_ONLINE);
    }



    public static void saveDeliveryGuyData(DeliveryGuyData deliveryGuyData, Context context)
    {
        context = MyApplication.getAppContext();

        if(context == null)
        {
            return;
        }

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(deliveryGuyData);
        prefsEditor.putString("delivery_guy_data", json);
        prefsEditor.apply();


        // update firebase subscriptions whenever shop information is updated
//        UtilityFCMTopicSubscriptions.subscribeForDeliveryStaffTopics();
        saveDeliveryGuyStatus(getDeliveryStatus(context),context);

    }


    public static DeliveryGuyData getDeliveryData(Context context)
    {
        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString("delivery_guy_data", null);

        return gson.fromJson(json, DeliveryGuyData.class);
    }

}
