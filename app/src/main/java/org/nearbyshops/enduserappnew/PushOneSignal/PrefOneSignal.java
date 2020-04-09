package org.nearbyshops.enduserappnew.PushOneSignal;

import android.content.Context;
import android.content.SharedPreferences;
import org.nearbyshops.enduserappnew.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 7/8/17.
 */

public class PrefOneSignal {

    public static final String TAG_ONE_SIGNAL_TOKEN = "tag_one_signal_token";
    public static final String TAG_LAST_TOKEN_ONE_SIGNAL = "tag_last_token_one_signal";


    public static void saveToken(Context context, String token)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TAG_ONE_SIGNAL_TOKEN, token);
        editor.apply();
    }







    public static String getToken(Context context)
    {
        if(context==null)
        {
            return null;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString(TAG_ONE_SIGNAL_TOKEN, null);
    }




    public static void saveLastToken(Context context, String token)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TAG_LAST_TOKEN_ONE_SIGNAL, token);
        editor.apply();
    }


    public static String getLastToken(Context context)
    {
        if(context==null)
        {
            return null;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString(TAG_LAST_TOKEN_ONE_SIGNAL, null);
    }



}
