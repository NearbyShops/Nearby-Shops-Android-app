package org.nearbyshops.enduserappnew.Preferences.PreferencesDeprecated;

public class PrefDeprecatedCode {



//    public static final String LAT_CENTER_KEY = "latCenterKey";
//    public static final String LON_CENTER_KEY = "lonCenterKey";
//    public static final String DELIVERY_RANGE_MAX_KEY = "deliveryRangeMaxKey";
//    public static final String DELIVERY_RANGE_MIN_KEY = "deliveryRagneMinKey";
//    public static final String PROXIMITY_KEY = "proximityKey";





//
//
//
//    public static void saveServiceLightStatus(Context context, int status)
//    {
//
//        // get a handle to shared Preference
//        SharedPreferences sharedPref;
//
//        sharedPref = context.getSharedPreferences(
//                context.getString(R.string.preference_file_name),
//                MODE_PRIVATE);
//
//        // write to the shared preference
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt("service_light_status",status);
//        editor.apply();
//    }
//
//
//
//    public static int getServiceLightStatus(Context context)
//    {
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//        return sharedPref.getInt("service_light_status", 3);
//    }





//    public static void saveInSharedPrefFloat(String key,float value)
//    {
//        Context context = MyApplicationCoreNew.getAppContext();
//
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        // write to the shared preference
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putFloat(key,value);
//        editor.apply();
//    }
//
//
//    public static float getFromSharedPrefFloat(String key,float defaultValue)
//    {
//        Context context = MyApplicationCoreNew.getAppContext();
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        // read from shared preference
//
//        return sharedPref.getFloat(key, defaultValue);
//    }
//
//
//
//
//    public static float getFromSharedPrefFloat(String key)
//    {
//        Context context = MyApplicationCoreNew.getAppContext();
//        // Get a handle to shared preference
//        SharedPreferences sharedPref;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        // read from shared preference
//
//        return sharedPref.getFloat(key,0.0f);
//    }





//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }


//    public static String getImageEndpointURL(Context context)
//    {
//        return PrefGeneral.getServiceURL(context) + "/api/Images";
//    }


//    public static String getConfigImageEndpointURL(Context context)
//    {
//        return PrefGeneral.getServiceURL(context) + "/api/ServiceConfigImages";
//    }
//
//
//    public DisplayMetrics getDisplayMetrics(Activity activity)
//    {
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        return metrics;
//    }






//    public static void saveEndUser(EndUser endUser, Context context)
//    {
//
//        if(context == null)
//        {
//            return;
//        }
//
//        //Creating a shared preference
//
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        SharedPreferences.Editor prefsEditor = sharedPref.edit();
//
//        if(endUser == null)
//        {
//            prefsEditor.putString("admin", "null");
//
//        }
//        else
//        {
//            Gson gson = new Gson();
//            String json = gson.toJson(endUser);
//            prefsEditor.putString("admin", json);
//        }
//
//        prefsEditor.apply();
//    }
//
//
//    public static EndUser getEndUser(Context context)
//    {
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        Gson gson = new Gson();
//        String json = sharedPref.getString("admin", "null");
//
//        if(json.equals("null"))
//        {
//
//            return null;
//
//        }else
//        {
//            return gson.fromJson(json, EndUser.class);
//        }
//
//    }
//







}
