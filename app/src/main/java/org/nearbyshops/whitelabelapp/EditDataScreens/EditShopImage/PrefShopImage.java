package org.nearbyshops.whitelabelapp.EditDataScreens.EditShopImage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


import org.nearbyshops.whitelabelapp.Model.ModelImages.ShopImage;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */



public class PrefShopImage {

    public static final String TAG_SHOP_IMAGE_PREF = "tag_shop_image";


    public static void saveItemImage(ShopImage itemImage, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(itemImage);
        prefsEditor.putString(TAG_SHOP_IMAGE_PREF, json);


        prefsEditor.apply();
    }




    public static ShopImage getItemImage(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString(TAG_SHOP_IMAGE_PREF, null);


        return gson.fromJson(json, ShopImage.class);
    }



    // Item for Item ID






}
