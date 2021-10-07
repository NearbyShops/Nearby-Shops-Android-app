package org.nearbyshops.whitelabelapp.EditDataScreens.EditItem;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */



public class PrefItem {

    public static final String TAG_ITEM_PREF = "tag_item";


    public static void saveItem(Item item, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        if(item == null)
        {
            prefsEditor.putString(TAG_ITEM_PREF, "null");

        }
        else
        {
            Gson gson = new Gson();
            String json = gson.toJson(item);
            prefsEditor.putString(TAG_ITEM_PREF, json);
        }

        prefsEditor.apply();
    }


    public static Item getItem(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_ITEM_PREF, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, Item.class);
        }
    }


}
