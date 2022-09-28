package org.nearbyshops.whitelabelapp.Admin.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.API.FavouriteShopService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopUtilityService;
import org.nearbyshops.whitelabelapp.API.API_Admin.ShopServiceForAdmin;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.FavouriteShopEndpoint;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModelShopForAdmin extends AndroidViewModel {


    private Shop shop;
    private FavouriteShopEndpoint favouriteShopEndpoint;
    private List<Object> dataset;

    private MutableLiveData<Shop> shopLive;
    public MutableLiveData<FavouriteShopEndpoint> favouriteEndpointLive;
    private MutableLiveData<List<Object>> datasetLive;


    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;




    public static int EVENT_SHOP_NOT_CREATED = 4;
    public static int EVENT_NETWORK_FAILED = 21;

    public static int EVENT_SHOP_ADDED_TO_MARKET = 1;
    public static int EVENT_SHOP_ADDED_TO_MARKET_BY_REQUEST = 2;
    public static int EVENT_SHOP_REMOVED_FROM_MARKET = 3;
    public static int EVENT_JOIN_REQUEST_SUCCESSFUL = 4;

    public static int EVENT_SHOP_OPENED = 5;
    public static int EVENT_SHOP_CLOSED = 6;





    @Inject
    Gson gson;


    @Inject
    ShopService shopService;


    @Inject
    ShopServiceForAdmin shopServiceForAdmin;


    @Inject
    FavouriteShopService favouriteShopService;


    @Inject
    ShopUtilityService shopUtilityService;





    public ViewModelShopForAdmin(@NonNull Application application) {
        super(application);

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();
        datasetLive = new MutableLiveData<>();
        dataset = new ArrayList<>();

        shopLive = new MutableLiveData<>();
        shop = new Shop();

        favouriteEndpointLive = new MutableLiveData<>();
        favouriteShopEndpoint = new FavouriteShopEndpoint();


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


    public MutableLiveData<Shop> getShopLive() {
        return shopLive;
    }





    public void enableShop(int shopID)
    {

        Call<ResponseBody> call = shopServiceForAdmin.enableShop(
                PrefLogin.getAuthorizationHeader(getApplication()),
                shopID
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    event.postValue(ViewModelShopForAdmin.EVENT_SHOP_ADDED_TO_MARKET);
                    message.postValue("Shop Added To Market !");
                }
                else
                {
                    event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });
    }



    public void setWaitlisted(int shopID)
    {

        Call<ResponseBody> call = shopServiceForAdmin.setShopWaitlisted(
                PrefLogin.getAuthorizationHeader(getApplication()),
                shopID
        );



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    event.postValue(ViewModelShopForAdmin.EVENT_SHOP_ADDED_TO_MARKET_BY_REQUEST);
                    message.postValue("Shop Added To Market !");
                }
                else
                {
                    event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });


    }



    public void disableShop(int shopID)
    {

        Call<ResponseBody> call = shopServiceForAdmin.disableShop(
                PrefLogin.getAuthorizationHeader(getApplication()),
                shopID
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    event.postValue(ViewModelShopForAdmin.EVENT_SHOP_REMOVED_FROM_MARKET);
                    message.postValue("Shop Removed from Market !");
                }
                else
                {
                    event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });
    }




    public void setShopOpen()
    {
        Call<ResponseBody> call = shopUtilityService.updateShopOpen(
                PrefLogin.getAuthorizationHeader(getApplication())
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {

                    Shop shop = PrefShopAdminHome.getShop(MyApplication.getAppContext());
                    shop.setOpen(true);
                    PrefShopAdminHome.saveShop(shop,MyApplication.getAppContext());

                    event.postValue(ViewModelShopForAdmin.EVENT_SHOP_OPENED);
                    message.postValue("Shop Opened !");

                }
                else
                {
                    event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });

    }



    public void setShopClosed()
    {

        Call<ResponseBody> call = shopUtilityService.setShopClosed(
                PrefLogin.getAuthorizationHeader(getApplication())
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {

                    Shop shop = PrefShopAdminHome.getShop(MyApplication.getAppContext());
                    shop.setOpen(false);
                    PrefShopAdminHome.saveShop(shop,MyApplication.getAppContext());

                    event.postValue(ViewModelShopForAdmin.EVENT_SHOP_CLOSED);
                    message.postValue("Shop Closed !");
                }
                else
                {


                    event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                event.postValue(ViewModelShopForAdmin.EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });

    }


}


