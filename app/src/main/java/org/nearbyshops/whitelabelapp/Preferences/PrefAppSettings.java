package org.nearbyshops.whitelabelapp.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import org.nearbyshops.whitelabelapp.Model.ModelMarket.MarketSettings;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;


import static android.content.Context.MODE_PRIVATE;

import com.google.gson.Gson;

/**
 * Created by sumeet on 20/4/17.
 */




public class PrefAppSettings {

    // simple or advanced at service selection screen
    // role selected at login screen


    // constants
    public static final int LAUNCH_SCREEN_MAIN = 1; // customer app interface
    public static final int LAUNCH_SCREEN_SHOP_ADMIN = 2; // shop admin and shop staff
    public static final int LAUNCH_SCREEN_DELIVERY = 3; // delivery boy
    public static final int LAUNCH_SCREEN_ADMIN = 5; // admin and staff



    private static final String TAG_PREF_SETTINGS = "pref_market_settings";
    private static final String TAG_LAUNCH_SCREEN = "launch_screen";




    public static void setLaunchScreen(int launchScreen, Context context)
    {
        context = MyApplication.getAppContext();

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        prefsEditor.putInt(TAG_LAUNCH_SCREEN,launchScreen);
        prefsEditor.apply();
    }




    public static int getLaunchScreen(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getInt(TAG_LAUNCH_SCREEN,LAUNCH_SCREEN_MAIN);
    }




    public static void saveMarketSettings(MarketSettings user, Context context)
    {
        context = MyApplication.getAppContext();

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(user);
        prefsEditor.putString(TAG_PREF_SETTINGS, json);

        prefsEditor.apply();
    }





    public static User getMarketSettings(Context context)
    {
        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString(TAG_PREF_SETTINGS, null);

        return gson.fromJson(json, User.class);
    }




}
