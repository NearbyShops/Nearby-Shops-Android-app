package org.nearbyshops.whitelabelapp.Services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;


import org.nearbyshops.whitelabelapp.API.API_Admin.MarketSettingService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelMarket.MarketSettings;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;

public class GetAppSettings extends IntentService {



    @Inject
    Gson gson;

    public static String INTENT_ACTION_MARKET_CONFIG_FETCHED = "market_config_fetched";



    public GetAppSettings() {
        super("update_service_configuration");

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        getLocalConfig();
    }







    void getLocalConfig()
    {


        if(PrefGeneral.getServerURL(getApplicationContext())==null)
        {
            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServerURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();





        MarketSettingService service = retrofit.create(MarketSettingService.class);
        Call<MarketSettings> call = service.getSettings(
                PrefLogin.getAuthorizationHeader(getApplicationContext())
        );

        call.enqueue(new Callback<MarketSettings>() {
            @Override
            public void onResponse(Call<MarketSettings> call, Response<MarketSettings> response) {

                if(response.code()==200)
                {
                    PrefAppSettings.saveMarketSettings(response.body(),getApplicationContext());



                    MarketSettings appSettings = response.body();

                    if(appSettings!=null)
                    {
//                        Currency currency = Currency.getInstance(new Locale("",appSettings.getISOCountryCode()));
//                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),getApplicationContext());
                    }


                    Intent intent = new Intent();
                    intent.setAction(INTENT_ACTION_MARKET_CONFIG_FETCHED);
//                    sendBroadcast(intent);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                }
                else
                {
//                    PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<MarketSettings> call, Throwable t) {

                //                PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
            }
        });


    }

}
