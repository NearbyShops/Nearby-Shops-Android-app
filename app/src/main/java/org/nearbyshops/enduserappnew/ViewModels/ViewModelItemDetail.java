package org.nearbyshops.enduserappnew.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.API.FavouriteItemService;
import org.nearbyshops.enduserappnew.API.ItemService;
import org.nearbyshops.enduserappnew.API.ShopItemService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.FavouriteItemEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ItemImageEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelImages.ItemImage;
import org.nearbyshops.enduserappnew.Model.ModelReviewItem.FavouriteItem;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewModelItemDetail extends AndroidViewModel {

    private MutableLiveData<List<Object>> datasetLive;
    private List<Object> dataset;


    private MutableLiveData<ItemImageEndPoint> imageEndpointLive;
    private ItemImageEndPoint imageEndPoint;


    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;




    public static int EVENT_RESPONSE_OK = 1;



    public static int EVENT_ADD_FAVOURITE_SUCCESS = 2;
    public static int EVENT_REMOVE_FAVOURITE_SUCCESS = 3;

    public static int EVENT_NETWORK_FAILED = 4;

    public static int EVENT_IS_FAVOURITE = 6;
    public static int EVENT_IS_NOT_FAVOURITE = 7;




    @Inject
    FavouriteItemService favouriteItemService;

    @Inject
    ItemService itemService;



    @Inject
    ShopItemService shopItemService;


    @Inject
    Gson gson;





    public ViewModelItemDetail(@NonNull Application application) {
        super(application);

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();
        datasetLive = new MutableLiveData<>();
        dataset = new ArrayList<>();

        imageEndpointLive = new MutableLiveData<>();
        imageEndPoint = new ItemImageEndPoint();


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }


    public MutableLiveData<ItemImageEndPoint> getImageEndpointLive() {
        return imageEndpointLive;
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




    public void getItemDetails(int itemID, boolean getItemDetails)
    {

        User user = PrefLogin.getUser(MyApplication.application);

        String authHeader=null;

        if(user!=null)
        {
            authHeader = PrefLogin.getAuthorizationHeaders(MyApplication.application);
        }


        Call<ItemImageEndPoint> endPointCall = null;

        endPointCall = itemService.getItemDetailsForEndUser(
                authHeader,
                getItemDetails,itemID, ItemImage.IMAGE_ORDER,null,0
        );




        endPointCall.enqueue(new Callback<ItemImageEndPoint>() {
            @Override
            public void onResponse(Call<ItemImageEndPoint> call, Response<ItemImageEndPoint> response) {

                if(response.code()==200)
                {
                    imageEndpointLive.postValue(response.body());
                }
                else
                {
                    event.postValue(EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ItemImageEndPoint> call, Throwable t) {

                event.postValue(EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });

    }




    public void getShopItemDetails(int itemID,int shopID, boolean getDetails)
    {

        User user = PrefLogin.getUser(MyApplication.application);

        String authHeader=null;

        if(user!=null)
        {
            authHeader = PrefLogin.getAuthorizationHeaders(MyApplication.application);
        }


        Call<ItemImageEndPoint> endPointCall = null;


        endPointCall = shopItemService.getShopItemDetails(
                authHeader,
                getDetails,itemID,shopID,
                ItemImage.IMAGE_ORDER,null,0
        );



        endPointCall.enqueue(new Callback<ItemImageEndPoint>() {
            @Override
            public void onResponse(Call<ItemImageEndPoint> call, Response<ItemImageEndPoint> response) {

                if(response.code()==200)
                {
                    imageEndpointLive.postValue(response.body());
                }
                else
                {
                    event.postValue(EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ItemImageEndPoint> call, Throwable t) {

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


        FavouriteItem favouriteItem = new FavouriteItem();
        favouriteItem.setItemID(itemID);
        favouriteItem.setEndUserID(user.getUserID());

        Call<FavouriteItem> call = favouriteItemService.insertFavouriteItem(favouriteItem);

        call.enqueue(new Callback<FavouriteItem>() {
            @Override
            public void onResponse(Call<FavouriteItem> call, Response<FavouriteItem> response) {

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
            public void onFailure(Call<FavouriteItem> call, Throwable t) {

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


        Call<ResponseBody> call = favouriteItemService.deleteFavouriteItem(itemID,
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



    public void checkFavourite(int itemID)
    {

        User user = PrefLogin.getUser(MyApplication.application);


        if(user==null)
        {
            return;
        }



        Call<FavouriteItemEndpoint> call = favouriteItemService.getFavouriteItems(
                itemID, user.getUserID(),null,
                null,null,null
        );


        call.enqueue(new Callback<FavouriteItemEndpoint>() {
            @Override
            public void onResponse(Call<FavouriteItemEndpoint> call, Response<FavouriteItemEndpoint> response) {


                if(response.body()!=null)
                {


                    if(response.body().getItemCount()>=1)
                    {
                        event.postValue(EVENT_ADD_FAVOURITE_SUCCESS);
                    }
                    else if(response.body().getItemCount()==0)
                    {

                        event.postValue(EVENT_REMOVE_FAVOURITE_SUCCESS);
                    }
                }

            }

            @Override
            public void onFailure(Call<FavouriteItemEndpoint> call, Throwable t) {

                message.postValue("Check favourite Failed !");
            }
        });

    }

}



