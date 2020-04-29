package org.nearbyshops.enduserappnew.Services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;

import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.Currency;
import java.util.Locale;

public class UpdateServiceConfiguration extends IntentService {




    @Inject
    Gson gson;

//    @Inject
//    ServiceConfigurationService service;



    public UpdateServiceConfiguration() {
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


        if(PrefGeneral.getServiceURL(getApplicationContext())==null)
        {
            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();





        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<Market> call = service.getServiceConfiguration(0.0,0.0);





        call.enqueue(new Callback<Market>() {
            @Override
            public void onResponse(Call<Market> call, Response<Market> response) {



                if(response.code()==200)
                {
                    PrefServiceConfig.saveServiceConfigLocal(response.body(),getApplicationContext());



                    Market config = response.body();

                    if(config!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",config.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),getApplicationContext());
                    }
                }
                else
                {
//                    PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
                }
            }



            @Override
            public void onFailure(Call<Market> call, Throwable t) {

//                PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
            }
        });

    }






}
