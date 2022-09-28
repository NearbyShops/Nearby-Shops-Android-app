package org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.PreferencesSort;

import android.content.Context;
import android.content.SharedPreferences;

import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.SlidingLayerSortItemsInShopForShopAdmin;
import org.nearbyshops.whitelabelapp.R;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 29/9/16.
 */

public class PrefSortItemsInShopSeller {


    public static final String TAG_SORT = "sort_items_in_stock";
    public static final String TAG_SORT_ASCENDING = "sort_order_items_in_stock";


    public static void saveSort(Context context, String sort_by)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TAG_SORT, sort_by);
        editor.apply();
    }


    public static String getSort(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String sort_by = sharedPref.getString(TAG_SORT, SlidingLayerSortItemsInShopForShopAdmin.SORT_BY_ITEM_AVAILABLE);

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
        editor.putString(TAG_SORT_ASCENDING,descending);
        editor.apply();
    }



    public static String getAscending(Context context)
    {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String descending = sharedPref.getString(TAG_SORT_ASCENDING, SlidingLayerSortItemsInShopForShopAdmin.SORT_DESCENDING);

        return descending;
    }

}
