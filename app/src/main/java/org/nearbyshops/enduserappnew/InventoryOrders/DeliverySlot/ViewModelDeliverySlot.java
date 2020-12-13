package org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.API.DeliverySlotService;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.Model.DeliverySlotEndpoint;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModelDeliverySlot extends AndroidViewModel {


    private MutableLiveData<List<Object>> datasetLive;
    private List<Object> dataset;

    private DeliverySlot deliverySlot;
    private MutableLiveData<DeliverySlot> deliverySlotLive;



    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;


    public static int EVENT_DELIVERY_SLOT_LIST_FETCHED = 1;
    public static int EVENT_DELIVERY_SLOT_DELETED = 2;
    public static int EVENT_DELIVERY_SLOT_UPDATED = 3;
    public static int EVENT_DELIVERY_SLOT_ADDED = 4;


    public static int EVENT_DELIVERY_SLOT_ENABLE_SUCCESS = 10;

    public static int EVENT_NETWORK_FAILED = 21;




    @Inject
    Gson gson;


    @Inject
    DeliverySlotService slotService;







    public ViewModelDeliverySlot(@NonNull Application application) {
        super(application);

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();
        datasetLive = new MutableLiveData<>();
        dataset = new ArrayList<>();

        deliverySlot = new DeliverySlot();
        deliverySlotLive = new MutableLiveData<>();


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


    public LiveData<DeliverySlot> getDeliverySlot()
    {
        return deliverySlotLive;
    }





    public void createDeliverySlot(DeliverySlot deliverySlot)
    {

        Call<DeliverySlot> call = slotService.createSlot(
                deliverySlot
        );


        call.enqueue(new Callback<DeliverySlot>() {
            @Override
            public void onResponse(Call<DeliverySlot> call, Response<DeliverySlot> response) {

                if(response.code()==201)
                {
                    dataset.clear();
                    message.postValue("Added Successfully !");
                    deliverySlotLive.postValue(response.body());
                    event.postValue(EVENT_DELIVERY_SLOT_ADDED);
                }
                else
                {
                    message.postValue("Failed Code : " + response.code());
                    event.postValue(EVENT_NETWORK_FAILED);
                }

            }

            @Override
            public void onFailure(Call<DeliverySlot> call, Throwable t) {

                message.postValue("Failed : Check your network connection ! ");
                event.postValue(EVENT_NETWORK_FAILED);
            }
        });
    }



    public void updateDeliverySlot(DeliverySlot deliverySlot)
    {

        Call<ResponseBody> call = slotService.updateSlot(
                deliverySlot.getSlotID(),deliverySlot
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    event.postValue(EVENT_DELIVERY_SLOT_UPDATED);
                    message.postValue("Updated !");
                }
                else
                {
                    message.postValue("Failed Code : " + response.code());
                    event.postValue(EVENT_NETWORK_FAILED);
                }
                
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                message.postValue("Network Failed !");
                event.postValue(EVENT_NETWORK_FAILED);

            }
        });
    }





    public void enableSlot(int slotID, boolean isEnabled)
    {

        Call<ResponseBody> call = slotService.enableSlot(
                slotID,isEnabled
        );




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    event.postValue(EVENT_DELIVERY_SLOT_ENABLE_SUCCESS);
                    message.postValue("Updated !");
                }
                else
                {
                    message.postValue("Failed Code : " + response.code());
                    event.postValue(EVENT_NETWORK_FAILED);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                message.postValue("Network Failed !");
                event.postValue(EVENT_NETWORK_FAILED);

            }
        });
    }



    public void deleteDeliverySlot(int deliverySlotID)
    {

        Call<ResponseBody> call = slotService.deleteSlot(deliverySlotID);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    message.postValue("Deleted !");
                    event.postValue(EVENT_DELIVERY_SLOT_DELETED);
                }
                else
                {
                    message.postValue("Failed Code : " + response.code());
                    event.postValue(EVENT_NETWORK_FAILED);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                message.postValue("Failed ... ");
                event.postValue(EVENT_NETWORK_FAILED);
            }
        });
    }




    public void fetchDeliverySlots(Integer shopID, boolean isShopNull, String searchString, String sortBy)
    {


        Call<DeliverySlotEndpoint> call = slotService.getItems(
                shopID,
                isShopNull,
                searchString, sortBy,
                null,0,
                false,false
        );



        call.enqueue(new Callback<DeliverySlotEndpoint>() {
            @Override
            public void onResponse(Call<DeliverySlotEndpoint> call, Response<DeliverySlotEndpoint> response) {


                if(response.code()==200 && response.body()!=null)
                {
                    dataset.clear();
                    dataset.addAll(response.body().getResults());
                    datasetLive.postValue(dataset);
                }
                else
                {
                    message.postValue("Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<DeliverySlotEndpoint> call, Throwable t) {


                message.postValue("Failed please check your Network ");
            }
        });
    }




    public void getAvailableSlots(Integer shopID, Date deliveryDate, boolean isPickupSlot, boolean isDeliverySlot)
    {

        Call<DeliverySlotEndpoint> call = slotService.getAvailableSlots(
                shopID,
                isPickupSlot,isDeliverySlot,
                deliveryDate,
                null,
                false,false
        );



        call.enqueue(new Callback<DeliverySlotEndpoint>() {
            @Override
            public void onResponse(Call<DeliverySlotEndpoint> call, Response<DeliverySlotEndpoint> response) {

                if(response.code()==200)
                {
                    dataset.clear();
                    dataset.addAll(response.body().getResults());
                    datasetLive.postValue(dataset);
                }
                else
                {
                    message.postValue("Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<DeliverySlotEndpoint> call, Throwable t) {


                message.postValue("Failed please check your Network ");
            }
        });
    }





}


