package org.nearbyshops.whitelabelapp.Admin.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.nearbyshops.whitelabelapp.API.API_Admin.MarketSettingService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelMarket.Market;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModelMarketsForAdmin extends AndroidViewModel {



    private List<Object> dataset;

    public MutableLiveData<Market> marketLive;
    private MutableLiveData<List<Object>> datasetLive;


    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;



    public static int EVENT_NETWORK_FAILED = 21;


    public static int EVENT_MARKET_ENABLED = 1;
    public static int EVENT_MARKET_DISABLED = 2;
    public static int EVENT_MARKET_DELETED = 3;
    public static int EVENT_MARKET_STATUS_CHANGED = 4;
    public static int EVENT_MARKET_DETAIL_FETCHED = 5;



    @Inject
    MarketSettingService marketsServiceForAdmin;




    public ViewModelMarketsForAdmin(@NonNull Application application) {
        super(application);

        dataset = new ArrayList<>();

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();

        datasetLive = new MutableLiveData<>();
        marketLive = new MutableLiveData<>();

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }




    public MutableLiveData<List<Object>> getData()
    {
        return datasetLive;
    }



    public LiveData<Integer> getEvent()
    {

        return event;
    }


    public LiveData<String> getMessage()
    {

        return message;
    }






    public void setMarketOpen(boolean isOpen)
    {

        Call<ResponseBody> call = marketsServiceForAdmin.setMarketOpen(
                PrefLogin.getAuthorizationHeader(getApplication()),
                isOpen
        );




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    event.postValue(ViewModelMarketsForAdmin.EVENT_MARKET_STATUS_CHANGED);

                    if(isOpen)
                    {
//                        message.postValue("Delivery Open !");
                    }
                    else
                    {
//                        message.postValue("Delivery Closed !");
                    }

                }
                else
                {
                    event.postValue(ViewModelMarketsForAdmin.EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                event.postValue(ViewModelMarketsForAdmin.EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });
    }




    public void getMarketForMarketStaff()
    {

        Call<Market> call = marketsServiceForAdmin.getMarketForDashboard(
                PrefLogin.getAuthorizationHeader(getApplication())
        );


        call.enqueue(new Callback<Market>() {
            @Override
            public void onResponse(Call<Market> call, Response<Market> response) {

                if(response.code()==200)
                {
                    event.postValue(ViewModelMarketsForAdmin.EVENT_MARKET_DETAIL_FETCHED);
                    marketLive.postValue(response.body());

                }
                else
                {
                    event.postValue(ViewModelMarketsForAdmin.EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Market> call, Throwable t) {


                event.postValue(ViewModelMarketsForAdmin.EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });

    }



}


