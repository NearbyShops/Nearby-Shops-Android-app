package org.nearbyshops.whitelabelapp.Admin.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.nearbyshops.whitelabelapp.API.DeliveryGuyService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelStaff.DeliveryGuyData;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModelDeliveryGuy extends AndroidViewModel {


    private DeliveryGuyData deliveryGuyData;
    private List<Object> dataset;

    private MutableLiveData<DeliveryGuyData> dataLive;
    private MutableLiveData<List<Object>> datasetLive;


    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;


    public static int EVENT_STATUS_UPDATED = 1;

    public static int EVENT_NETWORK_FAILED = 21;




    @Inject
    DeliveryGuyService deliveryGuyService;





    public ViewModelDeliveryGuy(@NonNull Application application) {
        super(application);

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();
        datasetLive = new MutableLiveData<>();
        dataset = new ArrayList<>();

        dataLive = new MutableLiveData<>();
        deliveryGuyData = new DeliveryGuyData();


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


    public MutableLiveData<DeliveryGuyData> getDataLive() {
        return dataLive;
    }




    public void getDeliveryGuyDetails()
    {

        Call<DeliveryGuyData> call = deliveryGuyService.getDeliveryGuyDetails(
                PrefLogin.getAuthorizationHeader(getApplication())
        );


        call.enqueue(new Callback<DeliveryGuyData>() {
            @Override
            public void onResponse(Call<DeliveryGuyData> call, Response<DeliveryGuyData> response) {


                if(response.code()==200)
                {
                    dataLive.postValue(response.body());
                }
                else
                {


                    event.postValue(ViewModelDeliveryGuy.EVENT_NETWORK_FAILED);
                    message.postValue("Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DeliveryGuyData> call, Throwable t) {


                event.postValue(ViewModelDeliveryGuy.EVENT_NETWORK_FAILED);
                message.postValue("Network Failed ! ");

            }
        });

    }



    public void setOnline()
    {

    }


}


