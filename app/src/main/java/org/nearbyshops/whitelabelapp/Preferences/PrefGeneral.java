package org.nearbyshops.whitelabelapp.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.R;

import static android.content.Context.MODE_PRIVATE;





/**
 * Created by sumeet on 5/5/16.
 */
public class PrefGeneral {



    public static final String SERVICE_URL_LOCAL_HOTSPOT = "http://192.168.43.233:5121";
    public static final String SERVICE_URL_NEARBYSHOPS_DEMO = "http://markets-blr.nearbyshops.org:5602";
    public static final String SERVICE_URL_LOCAL_ = "http://142.93.219.138:80";


    public static final String DEFAULT_SERVICE_URL = get_default_server_url();


    public static final String TAG_SERVICE_URL = "tag_pref_service_url";



    public static String get_default_server_url(){
        return MyApplication.getAppContext().getString(R.string.custom_url);
    }




    public static String getServerURL(Context context) {

        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString(TAG_SERVICE_URL, DEFAULT_SERVICE_URL);
    }



    public static void saveServerURL(String service_url, Context context)
    {
        context = MyApplication.getAppContext();

//        Context context = MyApplicationCoreNew.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);


        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TAG_SERVICE_URL, service_url);
        editor.apply();


        // log out the user
        PrefLogin.saveUserProfile(null,context);
        PrefLogin.saveCredentialsPassword(context,null,null);
    }


}
