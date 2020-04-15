package org.nearbyshops.enduserappnew.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;

import org.nearbyshops.enduserappnew.API.LoginUsingOTPService;
import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.API.API_SDS.ServiceConfigService;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.Model.MarketsList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SignInMarker;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ServiceConfigurationEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile.Model.RoleDashboardMarker;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SetLocationManually;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MarketViewModel extends AndroidViewModel {

    private MutableLiveData<List<Object>> datasetLive;
    private List<Object> dataset;
    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;


    public static int EVENT_LOCAL_CONFIG_FETCHED = 1;
    public static int EVENT_LOGGED_IN_TO_LOCAL_SUCCESS = 2;
    public static int EVENT_NETWORK_FAILED = 3;





    final private int limit = 50;
    private int offset = 0;
    private int item_count = 0;


    @Inject
    Gson gson;




    public MarketViewModel(@NonNull Application application) {
        super(application);

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();
        datasetLive = new MutableLiveData<>();
        dataset = new ArrayList<>();


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





    public void loadData(final boolean clearDataset)
    {

            String sortBy = " distance ";


            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();



            Call<ServiceConfigurationEndPoint> call;




            if(PrefLoginGlobal.getUser(getApplication())==null)
            {

                call = retrofit.create(ServiceConfigService.class).getShopListSimple(
                        PrefLocation.getLatitude(getApplication()), PrefLocation.getLongitude(getApplication()),
                        null,
                        null,
                        sortBy,
                        limit,offset);

            }
            else
            {

                call = retrofit.create(ServiceConfigService.class).getShopListSimple(
                        PrefLoginGlobal.getAuthorizationHeaders(getApplication()),
                        PrefLocation.getLatitude(getApplication()), PrefLocation.getLongitude(getApplication()),
                        null,
                        null,
                        sortBy,
                        limit,offset);
            }






//        PrefLocation.getLatitude(getActivity()),
//                PrefLocation.getLongitude(getActivity()),

//        filterOfficial,filterVerified,
//                serviceType,

            call.enqueue(new Callback<ServiceConfigurationEndPoint>() {
                @Override
                public void onResponse(Call<ServiceConfigurationEndPoint> call, Response<ServiceConfigurationEndPoint> response) {


                    if(response.code()==200)
                    {
                        item_count = response.body().getItemCount();
//                        adapter.setTotalItemsCount(item_count);


                        if(clearDataset)
                        {

                            dataset.clear();
//                            savedMarkets.clear();


//                            dataset.add(PrefServiceConfig.getServiceConfigLocal(getActivity()));




                            if(response.body().getSavedMarkets()!=null)
                            {
//                                savedMarkets.addAll(response.body().getSavedMarkets());
//                                dataset.add(response.body().getSavedMarkets());

                                dataset.add(new MarketsList("Favourites",response.body().getSavedMarkets()));
                            }





                            if(PrefGeneral.getServiceURL(getApplication())!=null)
                            {
                                User userGlobal = PrefLoginGlobal.getUser(getApplication());
                                User userLocal = PrefLogin.getUser(getApplication());


                                if(userLocal!=null)
                                {
                                    dataset.add(new RoleDashboardMarker());
                                }


                                if(userGlobal==null)
                                {
                                    dataset.add(new SignInMarker());
                                }
                                else
                                {
                                    dataset.add(userGlobal);
                                }

                            }








                            if(response.body().getResults()!=null)
                            {

//                            if(item_count>0)
//                            {
//                                dataset.add(new HeaderItemsList());
//                            }

                                dataset.add(new SetLocationManually());

                                if(response.body().getResults().size()>0)
                                {

                                    if(PrefGeneral.getServiceURL(getApplication())==null)
                                    {
                                        dataset.add(new HeaderTitle("Please Select a Market"));
                                    }
                                    else
                                    {
                                        dataset.add(new HeaderTitle("Markets in your Area"));
//                                        Market In Your Area
                                    }


                                    dataset.addAll(response.body().getResults());
                                    dataset.add(EmptyScreenDataListItem.getCreateMarketData());
                                }
                                else
                                {
                                    dataset.add(EmptyScreenDataListItem.createMarketNoMarketsAvailable());
                                }






//                                dataset.add(new MarketsList("Markets in your Area",response.body().getResults()));
                            }





                            if(PrefGeneral.getServiceURL(getApplication())==null)
                            {
                                User userGlobal = PrefLoginGlobal.getUser(getApplication());


                                if(userGlobal==null)
                                {
                                    dataset.add(new SignInMarker());
                                }
                                else
                                {
                                    dataset.add(userGlobal);
                                }
                            }







//
//                            ServiceConfigurationLocal configurationLocal = PrefServiceConfig.getServiceConfigLocal(getApplication());
//
//                            if(configurationLocal!=null)
//                            {
//                                dataset.add(configurationLocal);
//                            }






//                        Log.d(UtilityFunctions.TAG_LOG,UtilityFunctions.provideGson().toJson(response.body().getSavedMarkets()));
//                        Log.d(UtilityFunctions.TAG_LOG,"Saved Markets List Size : " + String.valueOf(savedMarkets.size()));











//
                        }






                        datasetLive.postValue(dataset);
//                        datasetLive.setValue(dataset);



//                        message.setValue("Response OK");

                    }
                    else
                    {
                        message.setValue("Failed : code : " + response.code());

                    }




                }

                @Override
                public void onFailure(Call<ServiceConfigurationEndPoint> call, Throwable t) {

                    message.setValue("Failed ... please check your network connection !");

                }
            });

        }







    public void fetchLocalConfiguration(final ServiceConfigurationGlobal configurationGlobal)
    {

//            PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),getApplicationContext());
//            PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());



//            PrefGeneral.getServiceURL(MyApplicationCoreNew.getAppContext())


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(configurationGlobal.getServiceURL())
                .client(new OkHttpClient().newBuilder().build())
                .build();



//        message.postValue("Fetch configuration OnGoing !");






        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(0.0,0.0);



//        selectMarket.setVisibility(View.INVISIBLE);
//        progressBarSelect.setVisibility(View.VISIBLE);


        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {


//                selectMarket.setVisibility(View.VISIBLE);
//                progressBarSelect.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {

                    PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),getApplication());
                    PrefServiceConfig.saveServiceConfigLocal(response.body(),getApplication());


                    ServiceConfigurationLocal config = response.body();

                    if(config!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",config.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),getApplication());
                    }




                    event.postValue(MarketViewModel.EVENT_LOCAL_CONFIG_FETCHED);


                }
                else
                {


                    message.postValue("Failed Code : " + response.code());
//                    message.postValue("Service URL : " + configurationGlobal.getServiceURL());



                    event.postValue(MarketViewModel.EVENT_NETWORK_FAILED);
                }






            }



            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {


//                selectMarket.setVisibility(View.VISIBLE);
//                progressBarSelect.setVisibility(View.INVISIBLE);

                    event.postValue(MarketViewModel.EVENT_NETWORK_FAILED);
                    message.postValue("Failed ... Please check your network ! ");
            }
        });
    }



    public void loginToLocalEndpoint(final ServiceConfigurationGlobal configurationGlobal)
    {

//        final String phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();




//        selectMarket.setVisibility(View.INVISIBLE);
//        progressBarSelect.setVisibility(View.VISIBLE);



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(configurationGlobal.getServiceURL())
                .client(new OkHttpClient().newBuilder().build())
                .build();





        Call<User> call = retrofit.create(LoginUsingOTPService.class).loginWithGlobalCredentials(
                PrefLoginGlobal.getAuthorizationHeaders(getApplication()),
                PrefServiceConfig.getServiceURL_SDS(getApplication()),
                123,
                false,false,
                0,true,false
        );




        System.out.println(PrefLoginGlobal.getToken(getApplication()));




        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {




//                selectMarket.setVisibility(View.VISIBLE);
//                progressBarSelect.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    // save username and password




                    PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),getApplication());



                    User user = response.body();


//                    PrefLogin.saveCredentials(
//                            context,
//                            user.getPhone(),
//                            user.getPassword()
//                    );

                    String username = "";

                    if(user.getPhone()!=null)
                    {
                        username = user.getPhone();
                    }
                    else if(user.getEmail()!=null)
                    {
                        username = user.getEmail();
                    }
                    else if(user.getUsername()!=null)
                    {
                        username = user.getUsername();
                    }
                    else if(user.getUserID()!=0)
                    {
                        username = String.valueOf(user.getUserID());
                    }

                    // local username can be different from the supplied username

                    PrefLogin.saveToken(
                            getApplication(),
                            username,
                            user.getToken()
                    );



//                    PrefLogin.saveCredentials(
//                            context,
//                            PrefLoginGlobal.getUsername(context),
//                            PrefLoginGlobal.getPassword(context)
//                    );


                    PrefLogin.saveUserProfile(
                            user,
                            getApplication()
                    );



                    ServiceConfigurationLocal configurationLocal = user.getServiceConfigurationLocal();

                    PrefServiceConfig.saveServiceConfigLocal(configurationLocal,getApplication());



                    if(configurationLocal!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",configurationLocal.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),getApplication());
                    }



                    UtilityFunctions.updateFirebaseSubscriptions();

                    event.postValue(MarketViewModel.EVENT_LOGGED_IN_TO_LOCAL_SUCCESS);

                }
                else
                {
                    event.postValue(MarketViewModel.EVENT_NETWORK_FAILED);
                    message.postValue("Login Failed : Username or password is incorrect !");

                }

            }




            @Override
            public void onFailure(Call<User> call, Throwable t) {


                message.postValue("Failed ... Please check your network connection !");
                event.postValue(MarketViewModel.EVENT_NETWORK_FAILED);

//                selectMarket.setVisibility(View.VISIBLE);
//                progressBarSelect.setVisibility(View.INVISIBLE);


            }
        });
    }


}


