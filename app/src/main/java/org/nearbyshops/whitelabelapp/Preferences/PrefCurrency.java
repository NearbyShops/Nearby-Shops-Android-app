package org.nearbyshops.whitelabelapp.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;

import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.R;

import java.util.Currency;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class PrefCurrency {

    private static final String TAG_PREF_CURRENCY = "currency_symbol";
    private static final String TAG_ISO_COUNTRY_CODE = "iso_country_code";
//    private static final String TAG_PREF_CURRENCY_VALID = "currency_valid";


    public static String KEY_LAT_CURRENCY = "lat_currency";
    public static String KEY_LON_CURRENCY = "lon_currency";

    public static int OPERATING_GLOBALLY = 1;
    public static int OPERATING_ONLY_IN_INDIA = 2;
    public static int OPERATING_ONLY_OUTSIDE_INDIA = 3;




    public static void saveCurrencyLocation(double lat,double lon, Context context)
    {
        context = MyApplication.getAppContext();

        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        prefsEditor.putFloat(KEY_LAT_CURRENCY, (float) lat);
        prefsEditor.putFloat(KEY_LON_CURRENCY, (float) lon);

        prefsEditor.apply();
    }



    public static Location getCurrencyLocation(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        double longitude = (double) sharedPref.getFloat(KEY_LON_CURRENCY, 0f);
        double latitude = (double)sharedPref.getFloat(KEY_LAT_CURRENCY, 0f);

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }






    public static boolean isCurrencyValid(Context context)
    {
        int kilometers = 10;


        // if distance is greater than given limit then invalidate the currency

//        double distance = getCurrencyLocation(context).distanceTo(PrefLocation.getLocationSelected(context));

//        System.out.println("Currency Distance for Validity : " + distance);
//        System.out.println("Is currency Valid : " + isCurrencyValid);

//        return !(distance > (1000 * kilometers));

        return false;
    }



    public static void saveISOCountryCode(String isoCountryCode, Context context)
    {
        context = MyApplication.getAppContext();
        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(TAG_ISO_COUNTRY_CODE, isoCountryCode);
        prefsEditor.apply();
    }





    public static String getISOCountryCode(Context context)
    {
        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString(TAG_ISO_COUNTRY_CODE, "GB");
    }




    public static void saveCurrencySymbol(String symbol, Context context)
    {
        context = MyApplication.getAppContext();
        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(TAG_PREF_CURRENCY, symbol);
        prefsEditor.apply();
    }




    public static void saveCurrencyUsingISOCountryCode(double lat,double lon, String isoCountryCode,Context context)
    {
        Currency currency = Currency.getInstance(new Locale("",isoCountryCode));
        saveCurrencySymbol(currency.getSymbol(),context);
        saveISOCountryCode(isoCountryCode,context);
        saveCurrencyLocation(lat,lon,context);
    }




    public static String getCurrencySymbol(Context context)
    {
        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString(TAG_PREF_CURRENCY, context.getString(R.string.rupee_symbol));
    }



    public static boolean isPhoneLoginEnabled(Context context)
    {
        // disable phone login outside india
        return getISOCountryCode(context).equals("IN");
    }





    public static int countryOfOperation()
    {
        // disable phone login outside india
        return OPERATING_GLOBALLY;
    }


}
