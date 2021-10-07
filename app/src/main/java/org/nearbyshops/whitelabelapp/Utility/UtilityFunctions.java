package org.nearbyshops.whitelabelapp.Utility;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings;
import org.nearbyshops.whitelabelapp.Preferences.PrefDeliveryGuyHome;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefMarketAdminHome;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopHome;
import org.nearbyshops.whitelabelapp.R;

import java.util.Currency;
import java.util.Locale;

import static org.nearbyshops.whitelabelapp.Utility.UtilityFCMTopicSubscriptions.unsubscribeFromTopics;


/**
 * Created by sumeet on 10/7/17.
 */

public class UtilityFunctions {

    public static final String TAG_LOG = "app_log";


    public static String countryCodeToEmoji(String countryCode) {

        if(countryCode==null)
        {
            return null;
        }

        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }



    /* Utility Functions */
    public static Gson provideGson() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
    }




    public static String refinedString(double number)
    {
        if(number % 1 !=0)
        {
            // contains decimal numbers

            return String.format("%.2f",number);
        }
        else
        {
            return String.format("%.0f",number);
        }
    }


    public static String refinedStringWithDecimals(double number)
    {
        return String.format("%.2f",number);
    }




    public static void logout(Context context)
    {

        unsubscribeFromTopics();

        // log out
        PrefLogin.saveUserProfile(null,context);
        PrefLogin.saveCredentialsPassword(context,null,null);

        PrefShopHome.saveShop(null,context);
        PrefShopAdminHome.saveShop(null,context);
        PrefMarketAdminHome.saveMarket(null,context);
        PrefDeliveryGuyHome.saveDeliveryGuyData(null,context);

        PrefAppSettings.setLaunchScreen(PrefAppSettings.LAUNCH_SCREEN_MAIN,context);
    }



    public static void openURL(String url, Context activity)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }



    public static void dialPhoneNumber(String phoneNumber,Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }



    public static String getCurrencySymbolFromISOCountryCode(String isoCountryCode)
    {
        Currency currency = Currency.getInstance(new Locale("",isoCountryCode));
        return currency.getSymbol();
    }




    public static void showToastMessage(Context context,String message)
    {
        if(context!=null)
        {
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
        }
    }



    public static void showLogMessage(String tag,String message)
    {
        Log.d(tag, message);
    }



    //  fired in the onDestroy function of the home screen
    public static void resetRoutine(Context context)
    {
        PrefLocation.setLocationSetByUser(false,context);

        if(PrefLogin.getUser(context)!=null)
        {   // logged in
            PrefLocation.saveDeliveryAddress(null,context);
        }


        if(!context.getResources().getBoolean(R.bool.demo_mode_enabled))
        {
            PrefLocation.saveLatLonSelected(PrefLocation.getLatitudeCurrent(context),PrefLocation.getLongitudeCurrent(context),context);
        }
    }




    public static String getAppHelpline(Context context)
    {
        String appName = "";

        if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_main_app))
        {
            appName = context.getString(R.string.app_helpline);
        }
        else if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_vendor_app))
        {
            appName = context.getString(R.string.app_helpline_vendor_app);
        }
        else if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_market_admin_app))
        {
            appName = context.getString(R.string.app_helpline_market_admin_app);
        }
        else if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_delivery_app))
        {
            appName = context.getString(R.string.app_helpline_delivery_app);
        }

        return appName;
    }




    public static String getAppName(Context context)
    {
        String appName = "";

        if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_main_app))
        {
            appName = context.getString(R.string.app_name_main_app);
        }
        else if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_vendor_app))
        {
            appName = context.getString(R.string.app_name_vendor_app);
        }
        else if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_market_admin_app))
        {
            appName = context.getString(R.string.app_name_market_admin_app);
        }
        else if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_delivery_app))
        {
            appName = context.getString(R.string.app_name_delivery_app);
        }

        return appName;
    }




    public static String getAppLink(Context context)
    {
        String url = "";

        if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_main_app))
        {
            url = context.getString(R.string.app_download_link);
        }
        else if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_vendor_app))
        {
            url = context.getString(R.string.app_download_link_vendor_app);
        }
        else if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_market_admin_app))
        {
            url = context.getString(R.string.app_download_link_market_admin_app);
        }
        else if(context.getResources().getInteger(R.integer.app_type)==context.getResources().getInteger(R.integer.app_type_delivery_app))
        {
            url = context.getString(R.string.app_download_link_delivery_app);
        }

        return url;
    }


}
