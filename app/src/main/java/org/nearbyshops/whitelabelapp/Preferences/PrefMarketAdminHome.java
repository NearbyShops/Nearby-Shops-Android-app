package org.nearbyshops.whitelabelapp.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.Model.ModelMarket.Market;
import org.nearbyshops.whitelabelapp.Utility.UtilityFCMTopicSubscriptions;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */



public class PrefMarketAdminHome {


    public static void saveMarket(Market market, Context context)
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
        String json = gson.toJson(market);
        prefsEditor.putString("market_profile_for_market_admin", json);
        prefsEditor.apply();


        // update firebase subscriptions whenever shop information is updated
        UtilityFCMTopicSubscriptions.subscribeToStaffTopics();

    }


    public static Market getMarket(Context context)
    {
        context = MyApplication.getAppContext();


        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString("market_profile_for_market_admin", null);

        return gson.fromJson(json, Market.class);
    }

}
