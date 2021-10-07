package org.nearbyshops.whitelabelapp.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;


import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import static android.content.Context.MODE_PRIVATE;





public class PrefLocation {


    public static String KEY_LAT_CURRENT = "key_lat_current";
    public static String KEY_LON_CURRENT = "key_lon_current";

    public static String KEY_LAT_SELECTED = "key_lat_selected";
    public static String KEY_LON_SELECTED = "key_lon_selected";

    public static String KEY_LOCATION_SET_BY_USER = "key_location_set_by_user";
    public static String KEY_SELECTED_DELIVERY_ADDRESS = "key_selected_delivery_address";



    // latitude and longitude for demo mode
//    public static String KEY_LAT_DEMO = "key_lat_demo";
//    public static String KEY_LON_DEMO = "key_lon_demo";





    public static Location getLocationSelected(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        double longitude = (double) sharedPref.getFloat(KEY_LON_SELECTED, 0f);
        double latitude = (double)sharedPref.getFloat(KEY_LAT_SELECTED, 0f);


        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }



    public static void saveLatLonSelected(double lat, double lon, Context context)
    {
        context = MyApplication.getAppContext();

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        prefsEditor.putFloat(KEY_LAT_SELECTED, (float) lat);
        prefsEditor.putFloat(KEY_LON_SELECTED, (float) lon);

        prefsEditor.apply();
    }



    public static double getLatitudeSelected(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return (double)sharedPref.getFloat(KEY_LAT_SELECTED, 0f);
    }



    public static double getLongitudeSelected(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return (double) sharedPref.getFloat(KEY_LON_SELECTED, 0f);
    }






    public static void setLocationSetByUser(boolean isLocationSetByUser, Context context)
    {
        context = MyApplication.getAppContext();

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();


        prefsEditor.putBoolean(KEY_LOCATION_SET_BY_USER,isLocationSetByUser);
        prefsEditor.apply();
    }


    public static boolean isLocationSetByUser(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);


        if(context.getResources().getBoolean(R.bool.demo_mode_enabled))
        {
            return true;
        }
        else
        {
            return sharedPref.getBoolean(KEY_LOCATION_SET_BY_USER, false);
        }
    }




    public static void saveDeliveryAddress(DeliveryAddress address, Context context)
    {


        context = MyApplication.getAppContext();

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(address);
        prefsEditor.putString(KEY_SELECTED_DELIVERY_ADDRESS, json);

        prefsEditor.apply();


        // update selected location
        if(address!=null)
        {
            PrefLocation.saveLatLonSelected(address.getLatitude(),address.getLongitude(),context);
            PrefLocation.setLocationSetByUser(true,context);
        }
    }



    public static DeliveryAddress getDeliveryAddress(Context context)
    {
        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString(KEY_SELECTED_DELIVERY_ADDRESS, null);

        return gson.fromJson(json, DeliveryAddress.class);
    }





    public static void saveLatLonCurrent(double lat,double lon, Context context)
    {
        context = MyApplication.getAppContext();

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        prefsEditor.putFloat(KEY_LAT_CURRENT, (float) lat);
        prefsEditor.putFloat(KEY_LON_CURRENT, (float) lon);

        if(!isLocationSetByUser(context))
        {
            saveLatLonSelected(lat,lon,context);
        }

        prefsEditor.apply();
    }



    public static double getLongitudeCurrent(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return (double) sharedPref.getFloat(KEY_LON_CURRENT, 0f);
    }



    public static double getLatitudeCurrent(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return (double)sharedPref.getFloat(KEY_LAT_CURRENT, 0f);
    }



    public static Location getLocationCurrent(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        double longitude = (double) sharedPref.getFloat(KEY_LON_CURRENT, 0f);
        double latitude = (double)sharedPref.getFloat(KEY_LAT_CURRENT, 0f);

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }






    public static double getLatitudeDemo(Context context)
    {
        context = MyApplication.getAppContext();

        return (double) (18.5555);
    }



    public static double getLongitudeDemo(Context context)
    {
        context = MyApplication.getAppContext();
        return (double)(73.7555);
    }


}
