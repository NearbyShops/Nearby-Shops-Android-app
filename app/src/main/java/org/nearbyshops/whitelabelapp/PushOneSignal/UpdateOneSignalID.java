package org.nearbyshops.whitelabelapp.PushOneSignal;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import okhttp3.ResponseBody;

import org.nearbyshops.whitelabelapp.API.UserService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

/**
 * Created by sumeet on 7/8/17.
 */

public class UpdateOneSignalID extends IntentService {

    @Inject
    UserService userService;

    int retryCount = 0;



    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * name Used to name the worker thread, important only for debugging.
     */
    public UpdateOneSignalID() {
        super("update_firebase_token");

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        if(PrefLogin.getUser(getApplicationContext())==null)
        {
            logMessage(" User not logged in .. when trying to update firebaseID ");
            return;
        }



        if(PrefOneSignal.getToken(getApplicationContext())!=null)
        {

            Call<ResponseBody> call = userService.updateOneSignalID(
                    PrefLogin.getAuthorizationHeader(getApplicationContext()),
                    PrefOneSignal.getToken(getApplicationContext())
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {
                        PrefOneSignal.saveToken(getApplicationContext(),null);
                        logMessage("Update OneSignalToken : OneSignal token updated " );


                    }
                    else
                    {
                        // retry atleast 3 times when failed
                        if(retryCount<3)
                        {
                            onHandleIntent(null);
                            retryCount = retryCount + 1;
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    // retry atleast 3 times when failed
                    if(retryCount<3)
                    {
                        onHandleIntent(null);
                        retryCount = retryCount + 1;
                    }

                }
            });

        }
    }






    void logMessage(String message)
    {
        Log.d("one_signal",message);
    }



}
