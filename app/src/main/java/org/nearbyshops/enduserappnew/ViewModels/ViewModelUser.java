package org.nearbyshops.enduserappnew.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModelUser extends AndroidViewModel {

    private MutableLiveData<User> userProfileLive;
    private User userProfile;
    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;


    public static int EVENT_profile_fetched = 1;
    public static int EVENT_NETWORK_FAILED = 3;





    @Inject
    Gson gson;


    @Inject
    UserService userService;







    public ViewModelUser(@NonNull Application application) {
        super(application);

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();
        userProfileLive = new MutableLiveData<>();
        userProfile = new User();


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }










    public LiveData<Integer> getEvent()
    {

        return event;
    }





    public LiveData<String> getMessage()
    {

        return message;
    }







    public void getUserProfile()
    {



        User endUser = PrefLogin.getUser(getApplication());

        if(endUser==null)
        {
            return;
        }





        Call<User> call = userService.getProfile(
                PrefLogin.getAuthorizationHeaders(getApplication())
        );





        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {



                if(response.code()==200)
                {

                    PrefLogin.saveUserProfile(response.body(),getApplication());


//                    message.postValue("Successful !");
                    event.postValue(ViewModelUser.EVENT_profile_fetched);

                }
                else
                {
                    message.postValue("Failed code : " + response.code());
                }



            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {


//                message.postValue("Failed ! Check your network connection !");

            }
        });

    }


}


