package org.nearbyshops.enduserappnew.DetailScreens.DetailOrder;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */




public class PrefOrderDetail {


    public static final String TAG_ORDER_DETAIL = "ORDER_DETAIL";

    public static void saveOrder(Order order, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        Gson gson = getGson();
        String json = gson.toJson(order);
        prefsEditor.putString(TAG_ORDER_DETAIL, json);

        System.out.println(json);

        prefsEditor.apply();
    }


    public static Order getOrder(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = getGson();

        String json = sharedPref.getString(TAG_ORDER_DETAIL, null);

        System.out.println(json);

        return gson.fromJson(json, Order.class);
    }




    private static Gson getGson() {

        final GsonBuilder gsonBuilder = new GsonBuilder();

        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
    }
}
