package org.nearbyshops.enduserappnew.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */








public class PrefLoginGlobal {


    public static final Integer ROLE_ADMIN = 1;
    public static final Integer ROLE_STAFF = 2;

    private static final String TAG_USERNAME_GLOBAL = "username_global";
    private static final String TAG_PASSWORD_GLOBAL = "password_global";
    private static final String TAG_TOKEN_GLOBAL = "token_global";
    private static final String TAG_ROLE_GLOBAL = "role_global";
    private static final String TAG_USER_PROFILE_GLOBAL = "user_profile_global";


    public static void saveCredentialsPassword(Context context, String username, String password)
    {
        context = MyApplication.getAppContext();

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TAG_USERNAME_GLOBAL, username);
        editor.putString(TAG_PASSWORD_GLOBAL,password);
        editor.apply();
    }



    public static void saveToken(Context context, String username, String token)
    {
        context = MyApplication.getAppContext();

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TAG_USERNAME_GLOBAL, username);
        editor.putString(TAG_TOKEN_GLOBAL,token);
        editor.apply();
    }





    public static String getUsername(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString(TAG_USERNAME_GLOBAL, "");
    }

    public static String getPassword(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString(TAG_PASSWORD_GLOBAL, "");
    }



    public static String getToken(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString(TAG_TOKEN_GLOBAL, "");
    }




    public static String baseEncoding(String username,String password)
    {
        String credentials = username + ":" + password;
        // create Base64 encodet string
        String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return basic;
    }




    public static String getAuthorizationHeaders(Context context)
    {
        context = MyApplication.getAppContext();


        return PrefLoginGlobal.baseEncoding(
                PrefLoginGlobal.getUsername(context),
                PrefLoginGlobal.getToken(context));

    }




    public static String getAuthHeaderPassword(Context context)
    {
        context = MyApplication.getAppContext();


        return PrefLoginGlobal.baseEncoding(
                PrefLoginGlobal.getUsername(context),
                PrefLoginGlobal.getPassword(context));

    }





    public static void saveUserProfile(User user, Context context)
    {
        context = MyApplication.getAppContext();

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(user);
        prefsEditor.putString(TAG_USER_PROFILE_GLOBAL, json);

        prefsEditor.apply();
    }






    public static User getUser(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString(TAG_USER_PROFILE_GLOBAL, null);

        return gson.fromJson(json, User.class);
    }




    public static void saveUsername(Context context, String username)
    {
        context = MyApplication.getAppContext();

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TAG_USERNAME_GLOBAL, username);
//        editor.putString("password",password);
        editor.apply();
    }




    public static void savePassword(Context context, String password)
    {

        context = MyApplication.getAppContext();

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("username", username);
        editor.putString(TAG_PASSWORD_GLOBAL,password);
        editor.apply();
    }





    public static void setRoleID(Context context, int role_id)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(TAG_ROLE_GLOBAL, role_id);
        editor.apply();
    }





    public static int getRoleID(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getInt(TAG_ROLE_GLOBAL, -1);
    }

}
