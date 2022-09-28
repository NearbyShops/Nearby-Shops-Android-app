package org.nearbyshops.whitelabelapp.InventoryOrders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderService;
import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderServiceDeliveryPersonSelf;
import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderServiceShopStaff;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





public class ViewModelOrders extends AndroidViewModel {

    private MutableLiveData<List<Object>> datasetLive;
    private List<Object> dataset;


    private MutableLiveData<Order> shopLive;
    private Order shop;


    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;




    public static int EVENT_RESPONSE_OK = 1;
    public static int EVENT_NETWORK_FAILED = 2;





    @Inject
    Gson gson;



    @Inject
    OrderServiceShopStaff orderServiceShopStaff;


    @Inject
    OrderServiceDeliveryPersonSelf orderServiceDelivery;


    @Inject
    OrderService orderService;









    public ViewModelOrders(@NonNull Application application) {
        super(application);

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();
        datasetLive = new MutableLiveData<>();
        dataset = new ArrayList<>();

        shopLive = new MutableLiveData<>();
        shop = new Order();


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




    public void confirmOrderHD(int orderID) {


        Call<ResponseBody> call = orderServiceShopStaff.confirmOrder(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);


        executeCall(call);

    }


    public void setOrderPackedHD(int orderID) {

        Call<ResponseBody> call = orderServiceShopStaff.setOrderPacked(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }




    public void setOutForDelivery(int orderID) {


        Call<ResponseBody> call = orderServiceShopStaff.setOutForDelivery(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }




    public void deliverToUserBySelf(int orderID) {


        Call<ResponseBody> call = orderServiceShopStaff.deliverOrder(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }




    public void returnOrderBySelf(int orderID) {

        Call<ResponseBody> call = orderServiceShopStaff.returnOrder(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }



    public void acceptReturnHD(int orderID) {

        Call<ResponseBody> call = orderServiceShopStaff.acceptReturn(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }




    public void unpackOrderHD(int orderID) {


        Call<ResponseBody> call = orderServiceShopStaff.unpackOrder(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }





    public void paymentReceivedHD(int orderID) {

        Call<ResponseBody> call = orderServiceShopStaff.paymentReceived(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }







    public void cancelOrder(int orderID, int shopID) {


        Call<ResponseBody> call = orderService.cancelOrderByShop(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID,
                shopID
        );



        executeCall(call);
    }



    public void cancelOrderByEndUser(int orderID, int endUserID) {


        Call<ResponseBody> call = orderService.cancelOrderByEndUser(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID,
                endUserID
        );



        executeCall(call);
    }





    void executeCall(Call<ResponseBody> call)
    {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                if(response.code()==200)
                {

                    message.postValue("Successful !");
                    event.postValue(EVENT_RESPONSE_OK);

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    message.postValue("Not permitted !");
                    event.postValue(EVENT_NETWORK_FAILED);
                }
                else
                {
                    message.postValue("Failed with Error Code : " + response.code());
                    event.postValue(EVENT_NETWORK_FAILED);
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                message.postValue("Failed please check your network !");
                event.postValue(EVENT_NETWORK_FAILED);

            }
        });

    }



    /*
    * Functions for Pick for Shop Begins
    *
    * */



    public void confirmOrderPFS(int orderID) {

        Call<ResponseBody> call = orderServiceShopStaff.confirmOrderPFS(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID
        );


        executeCall(call);

    }


    public void setOrderPackedPFS(int orderID) {

        Call<ResponseBody> call = orderServiceShopStaff.setOrderPackedPFS(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }



    public void setReadyForPickupPFS(int orderID) {

        Call<ResponseBody> call = orderServiceShopStaff.setOrderReadyForPickupPFS(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }





    public void setPaymentReceivedPFS(int orderID) {

        Call<ResponseBody> call = orderServiceShopStaff.paymentReceivedPFS(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);

        executeCall(call);
    }





    /*
    * Functions for Pick from shop Ends
    * */



    /* Functions for Delivery Guy */




    public void pickupOrder(int orderID) {



        Call<ResponseBody> call = orderServiceDelivery.acceptOrder(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);



        executeCall(call);
    }




    public void startPickup(int orderID) {


        Call<ResponseBody> call = orderServiceDelivery.startPickup(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);



        executeCall(call);
    }





    public void declineOrder(int orderID) {


        Call<ResponseBody> call = orderServiceDelivery.declineOrder(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);


        executeCall(call);
    }





    public void deliverToUser(int orderID, int deliveryOTP) {


        Call<ResponseBody> call = orderServiceDelivery.handoverToUser(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID,
                deliveryOTP
        );

        executeCall(call);
    }






    public void returnOrder(int orderID) {


        Call<ResponseBody> call = orderServiceDelivery.returnOrderPackage(
                PrefLogin.getAuthorizationHeader(getApplication()),
                orderID);


        executeCall(call);
    }



    /* Functions for delivery guy Ends*/
}


