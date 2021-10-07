package org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.PreferencesSort;

import android.content.Context;
import android.content.SharedPreferences;

import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.SlidingLayerSortItemsAdmin;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 29/9/16.
 */

public class PrefSortItemsByCategoryAdmin {



    public static void saveSort(Context context, String sort_by)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_items_by_category", sort_by);
        editor.apply();
    }


    public static String getSort(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String sort_by = sharedPref.getString("sort_items_by_category", SlidingLayerSortItemsAdmin.SORT_BY_ITEM_RATING);

        return sort_by;
    }



    public static void saveAscending(Context context, String descending)
    {

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_descending_items_by_category",descending);
        editor.apply();
    }



    public static String getAscending(Context context)
    {

//        if(context==null)
//        {
//            context = MyApplication.getAppContext();
//        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String descending = sharedPref.getString("sort_descending_items_by_category", SlidingLayerSortItemsAdmin.SORT_DESCENDING);

        return descending;
    }

}
