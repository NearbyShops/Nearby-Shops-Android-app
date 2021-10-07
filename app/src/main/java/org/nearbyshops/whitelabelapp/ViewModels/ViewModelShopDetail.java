package org.nearbyshops.whitelabelapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.API.FavouriteShopService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopDetailService;
import org.nearbyshops.whitelabelapp.API.ShopReviewService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelImages.ShopImage;
import org.nearbyshops.whitelabelapp.Model.ModelReviewShop.FavouriteShop;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;






public class ViewModelShopDetail extends AndroidViewModel {

    private MutableLiveData<List<Object>> datasetLive;
    private List<Object> dataset;


    private MutableLiveData<ShopImageEndPoint> endpointLive;
    private ShopImageEndPoint endPoint;


    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;



    public static int EVENT_RESPONSE_OK = 1;


    public static int EVENT_ADD_FAVOURITE_SUCCESS = 2;
    public static int EVENT_REMOVE_FAVOURITE_SUCCESS = 3;

    public static int EVENT_NETWORK_FAILED = 4;

    public static int EVENT_IS_FAVOURITE = 6;
    public static int EVENT_IS_NOT_FAVOURITE = 7;



    @Inject
    ShopReviewService shopReviewService;

    @Inject
    FavouriteShopService favouriteShopService;


    @Inject
    ShopService shopService;


    @Inject
    ShopDetailService shopDetailService;



    @Inject
    Gson gson;





    public ViewModelShopDetail(@NonNull Application application) {
        super(application);

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();
        datasetLive = new MutableLiveData<>();
        dataset = new ArrayList<>();

        endpointLive = new MutableLiveData<>();
        endPoint = new ShopImageEndPoint();


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }




    public MutableLiveData<ShopImageEndPoint> getEndpointLive() {
        return endpointLive;
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







    public void getShopImages(int shopID, boolean getShopDetails)
    {

        User user = PrefLogin.getUser(MyApplication.application);

        String authHeader=null;

        if(user!=null)
        {
            authHeader = PrefLogin.getAuthorizationHeader(MyApplication.application);
        }





        Call<ShopImageEndPoint> endPointCall = null;



        endPointCall = shopDetailService.getShopDetailsForDetailScreen(
                authHeader, getShopDetails, shopID,
                PrefLocation.getLatitudeSelected(getApplication()),PrefLocation.getLongitudeSelected(getApplication()),
                ShopImage.IMAGE_ORDER,null,0
        );



        endPointCall.enqueue(new Callback<ShopImageEndPoint>() {
            @Override
            public void onResponse(Call<ShopImageEndPoint> call, Response<ShopImageEndPoint> response) {



                if(response.code()==200)
                {
                    endpointLive.postValue(response.body());
                }
                else
                {
                    event.postValue(EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ShopImageEndPoint> call, Throwable t) {


                event.postValue(EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });

    }






    public void insertFavourite(int itemID)
    {


        User user = PrefLogin.getUser(MyApplication.application);

        if(user==null)
        {
            return;
        }


        FavouriteShop favouriteItem = new FavouriteShop();
        favouriteItem.setShopID(itemID);
        favouriteItem.setEndUserID(user.getUserID());

        Call<FavouriteShop> call = favouriteShopService.insertFavouriteShop(favouriteItem);

        call.enqueue(new Callback<FavouriteShop>() {
            @Override
            public void onResponse(Call<FavouriteShop> call, Response<FavouriteShop> response) {


                if(response.code()==201)
                {
                    event.postValue(EVENT_ADD_FAVOURITE_SUCCESS);
                }
                else
                {
                    message.postValue("Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<FavouriteShop> call, Throwable t) {

                message.postValue("Setting favourite failed !");

            }
        });


    }




    public void deleteFavourite(int itemID)
    {


        User user = PrefLogin.getUser(MyApplication.application);

        if(user==null)
        {
            return;
        }


        Call<ResponseBody> call = favouriteShopService.deleteFavouriteShop(itemID,
                user.getUserID());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {
                    event.postValue(EVENT_REMOVE_FAVOURITE_SUCCESS);
                }
                else
                {
                    message.postValue("Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                message.postValue("Remove favourite failed !");
            }
        });

    }





    public void checkFavourite(int shopID)
    {

        User user = PrefLogin.getUser(MyApplication.application);


        if(user==null)
        {
            return;
        }


        Call<ResponseBody> call = favouriteShopService.checkFavourite(
                shopID, user.getUserID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {
                    event.postValue(EVENT_IS_FAVOURITE);
                }
                else
                {
                    event.postValue(EVENT_IS_NOT_FAVOURITE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                message.postValue("Check favourite Failed !");
            }
        });

    }



}



