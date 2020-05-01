package org.nearbyshops.enduserappnew.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import org.nearbyshops.enduserappnew.R;

import static android.content.Context.MODE_PRIVATE;





public class PrefBadgeCount {



    public static final String TAG_BADGE_COUNT_ORDERS = "tag_badge_count_orders";
    public static final String TAG_BADGE_COUNT_CARTS = "tag_badge_count_carts";



    public static void saveBadgeCountOrders(int badgeCount, Context context)
    {
        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        prefsEditor.putInt(TAG_BADGE_COUNT_ORDERS, badgeCount);

        prefsEditor.apply();
    }



    public static int getBadgeCountOrders(Context context)
    {
        if(context==null)
        {
            return 0;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getInt(TAG_BADGE_COUNT_ORDERS, 0);
    }





    public static void saveBadgeCountCarts(int badgeCount, Context context)
    {
        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        prefsEditor.putInt(TAG_BADGE_COUNT_CARTS, badgeCount);

        prefsEditor.apply();
    }



    public static int getBadgeCountCarts(Context context)
    {
        if(context==null)
        {
            return 0;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getInt(TAG_BADGE_COUNT_CARTS, 0);
    }




}
