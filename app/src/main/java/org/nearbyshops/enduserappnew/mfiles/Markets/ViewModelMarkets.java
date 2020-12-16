package org.nearbyshops.enduserappnew.mfiles.Markets;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;

import org.nearbyshops.enduserappnew.API.LoginUsingOTPService;
import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.API.API_SDS.MarketService;
import org.nearbyshops.enduserappnew.mfiles.ViewHolderMarket.Model.MarketsListData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile.Model.RoleDashboardMarkerSDS;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.CreateShopData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SignInMarker;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ServiceConfigurationEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
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

public class ViewModelMarkets extends AndroidViewModel {



    private MutableLiveData<List<Object>> datasetLive;
    private List<Object> dataset;
    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;


    public static int EVENT_LOCAL_CONFIG_FETCHED = 1;
    public static int EVENT_LOGGED_IN_TO_LOCAL_SUCCESS = 2;
    public static int EVENT_NETWORK_FAILED = 3;

    public static int EVENT_NO_MARKET_AVAILABLE = 4;




    final private int limit = 50;
    private int offset = 0;
    private int item_count = 0;


    @Inject
    Gson gson;




    public ViewModelMarkets(@NonNull Application application) {
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





    public void getNearbyMarketsList(final boolean getFavouriteMarkets, boolean isSelectionMode)
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

                call = retrofit.create(MarketService.class).getMarketsList(
                        PrefLocation.getLatitude(getApplication()), PrefLocation.getLongitude(getApplication()),
                        null,
                        null,
                        sortBy,
                        limit,offset);

            }
            else
            {

                call = retrofit.create(MarketService.class).getMarketsList(
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




                            dataset.clear();
//                            savedMarkets.clear();


//                            dataset.add(PrefServiceConfig.getServiceConfigLocal(getActivity()));




                            if(response.body().getSavedMarkets()!=null && PrefGeneral.isMultiMarketEnabled(getApplication()))
                            {
//                                savedMarkets.addAll(response.body().getSavedMarkets());
//                                dataset.add(response.body().getSavedMarkets());

                                dataset.add(new MarketsListData("Favourites",response.body().getSavedMarkets()));
                            }






                            if(PrefGeneral.getServiceURL(getApplication())!=null && isSelectionMode)
                            {

                                User userLocal = PrefLogin.getUser(getApplication());


                                if(userLocal!=null)
                                {
                                    if(userLocal.getRole()==User.ROLE_END_USER_CODE)
                                    {
                                        dataset.add(new CreateShopData());
                                    }
                                    else
                                    {
                                        dataset.add(new RoleDashboardMarker());
                                    }

                                }
                            }




                            User userGlobal = PrefLoginGlobal.getUser(getApplication());




                            if(userGlobal!=null)
                            {

                                if(userGlobal.getRole()==User.ROLE_ADMIN_CODE)
                                {
                                    dataset.add(new RoleDashboardMarkerSDS());
                                }


                                if(isSelectionMode)
                                {
//                                    dataset.add(userGlobal);
                                }
                            }
                            else
                            {
                                if(PrefGeneral.getServiceURL(getApplication())!=null && isSelectionMode)
                                {

                                    dataset.add(new SignInMarker());
                                }
                            }






                            if(response.body().getResults()!=null)
                            {

//                            if(item_count>0)
//                            {
//                                dataset.add(new HeaderItemsList());
//                            }

                                dataset.add(new SetLocationManually());

                                if(response.body().getResults().size()>0 && PrefGeneral.isMultiMarketEnabled(getApplication()))
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


//                                    if(isProfileScreen)
//                                    {
                                        dataset.add(EmptyScreenDataListItem.getCreateMarketData());
//                                    }


                                }
                                else
                                {
//                                    dataset.add(EmptyScreenDataListItem.getCreateMarketData());
                                    dataset.add(EmptyScreenDataListItem.createMarketNoMarketsAvailable());
                                }






//                                dataset.add(new MarketsList("Markets in your Area",response.body().getResults()));
                            }





                            if(PrefGeneral.getServiceURL(getApplication())==null && isSelectionMode)
                            {
                                userGlobal = PrefLoginGlobal.getUser(getApplication());


                                if(userGlobal==null)
                                {
                                    dataset.add(new SignInMarker());
                                }
                                else
                                {
                                    dataset.add(userGlobal);
                                }
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

//                    message.setValue("Failed ... please check your network connection !");



                    dataset.clear();
                    dataset.add(EmptyScreenDataFullScreen.getOffline());
                    datasetLive.postValue(dataset);


                }
            });

        }




    public void getNearbyMarketsHorizontal()
    {

        String sortBy = " distance ";


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



        Call<ServiceConfigurationEndPoint> call;


        call = retrofit.create(MarketService.class).getMarketsList(
                PrefLocation.getLatitude(getApplication()), PrefLocation.getLongitude(getApplication()),
                null,
                null,
                sortBy,
                limit,offset);






        call.enqueue(new Callback<ServiceConfigurationEndPoint>() {
            @Override
            public void onResponse(Call<ServiceConfigurationEndPoint> call, Response<ServiceConfigurationEndPoint> response) {


                if(response.code()==200)
                {
                    item_count = response.body().getItemCount();


                    dataset.clear();


                    dataset.add(new MarketsListData("Favourites",response.body().getResults()));
                    datasetLive.postValue(dataset);


                }
                else
                {
                    message.setValue("Failed : code : " + response.code());

                }




            }

            @Override
            public void onFailure(Call<ServiceConfigurationEndPoint> call, Throwable t) {

            }
        });

    }



    public void getNearestMarket()
    {

        String sortBy = " distance ";


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



        Call<ServiceConfigurationEndPoint> call;


        call = retrofit.create(MarketService.class).getMarketsList(
                PrefLocation.getLatitude(getApplication()), PrefLocation.getLongitude(getApplication()),
                null,
                null,
                sortBy,
                1,0);






        call.enqueue(new Callback<ServiceConfigurationEndPoint>() {
            @Override
            public void onResponse(Call<ServiceConfigurationEndPoint> call, Response<ServiceConfigurationEndPoint> response) {


                if(response.code()==200)
                {
//                    item_count = response.body().getItemCount();


                    dataset.clear();


//                    message.postValue("Markets Size : " + response.body().getResults().size());
//                    System.out.println("Markets Size : " + response.body().getResults().size());



                    if(response.body().getResults().size()>0)
                    {
                        Market market = response.body().getResults().get(0);
                        fetchLocalConfiguration(market);


                    }
                    else
                    {
                        event.postValue(ViewModelMarkets.EVENT_NO_MARKET_AVAILABLE);
                    }

                }
                else
                {
                    message.postValue("Failed : code : " + response.code());

                }




            }

            @Override
            public void onFailure(Call<ServiceConfigurationEndPoint> call, Throwable t) {

            }
        });

    }





    public void fetchLocalConfiguration(final Market configurationGlobal)
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

        Call<Market> call = service.getServiceConfiguration(0.0,0.0);



//        selectMarket.setVisibility(View.INVISIBLE);
//        progressBarSelect.setVisibility(View.VISIBLE);


        call.enqueue(new Callback<Market>() {
            @Override
            public void onResponse(Call<Market> call, Response<Market> response) {


//                selectMarket.setVisibility(View.VISIBLE);
//                progressBarSelect.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {

                    PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),getApplication());
                    PrefServiceConfig.saveServiceConfigLocal(response.body(),getApplication());


                    Market config = response.body();

                    if(config!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",config.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),getApplication());
                    }




                    event.postValue(ViewModelMarkets.EVENT_LOCAL_CONFIG_FETCHED);


                }
                else
                {


                    message.postValue("Failed Code : " + response.code());
//                    message.postValue("Service URL : " + configurationGlobal.getServiceURL());



                    event.postValue(ViewModelMarkets.EVENT_NETWORK_FAILED);
                }






            }



            @Override
            public void onFailure(Call<Market> call, Throwable t) {


//                selectMarket.setVisibility(View.VISIBLE);
//                progressBarSelect.setVisibility(View.INVISIBLE);

                    event.postValue(ViewModelMarkets.EVENT_NETWORK_FAILED);
                    message.postValue("Failed ... Please check your network ! ");
            }
        });
    }



    public void loginToLocalEndpoint(final Market configurationGlobal)
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




//        System.out.println(PrefLoginGlobal.getToken(getApplication()));




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



                    Market configurationLocal = user.getServiceConfigurationLocal();

                    PrefServiceConfig.saveServiceConfigLocal(configurationLocal,getApplication());




                    if(configurationLocal!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",configurationLocal.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),getApplication());

//                        String jsonString = UtilityFunctions.provideGson().toJson(configurationLocal);
//                        System.out.println(jsonString);
                    }
                    else
                    {
//                        System.out.println("Configuration Null ! ViewModelMarkets line no 622");
                    }



                    UtilityFunctions.updateFirebaseSubscriptions();

                    event.postValue(ViewModelMarkets.EVENT_LOGGED_IN_TO_LOCAL_SUCCESS);

                }
                else
                {
                    event.postValue(ViewModelMarkets.EVENT_NETWORK_FAILED);
                    message.postValue("Login Failed : Username or password is incorrect !");

                }

            }




            @Override
            public void onFailure(Call<User> call, Throwable t) {


                message.postValue("Failed ... Please check your network connection !");
                event.postValue(ViewModelMarkets.EVENT_NETWORK_FAILED);

//                selectMarket.setVisibility(View.VISIBLE);
//                progressBarSelect.setVisibility(View.INVISIBLE);


            }
        });
    }


}


