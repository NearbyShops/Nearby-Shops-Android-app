package org.nearbyshops.enduserappnew.EditDataScreens.Deprecated.EditShopForAdmin;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class PrefShopForAdmin {

    private static final String TAG_PREF_SHOP = "shop_for_admin";

    public static void saveShop(Shop shopAdmin, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(shopAdmin);
        prefsEditor.putString(TAG_PREF_SHOP, json);
        prefsEditor.apply();
    }


    public static Shop getShop(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_PREF_SHOP, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, Shop.class);
        }
    }

}
