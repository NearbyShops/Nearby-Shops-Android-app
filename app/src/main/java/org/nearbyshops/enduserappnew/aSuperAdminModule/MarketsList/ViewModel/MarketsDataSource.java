package org.nearbyshops.enduserappnew.aSuperAdminModule.MarketsList.ViewModel;




import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.API.API_SDS.MarketService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ServiceConfigurationEndPoint;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.Models.FilterMarkets;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.ViewHolderFilterMarkets;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarketsDataSource extends PageKeyedDataSource<Long,Object> {




    @Inject
    Gson gson;



    private int item_count;
    int offset;
    int limit;



    public MarketsDataSource() {

        DaggerComponentBuilder.getInstance().getNetComponent()
                .Inject(this);

    }






    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Object> callback) {




        List<Object> dataset = new ArrayList<>();

        String sortBy = " distance ";


        sortBy = ViewHolderFilterMarkets.getSortString(MyApplication.getAppContext());
        boolean filterByPingStatus = ViewHolderFilterMarkets.getPingStatusFilter(MyApplication.getAppContext());


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();


        Call<ServiceConfigurationEndPoint> call = retrofit.create(MarketService.class).getMarketsList(
                PrefLoginGlobal.getAuthorizationHeaders(MyApplication.getAppContext()),
                PrefLocation.getLatitude(MyApplication.getAppContext()), PrefLocation.getLongitude(MyApplication.getAppContext()),
                null,null,
                null,null,null,
                sortBy,filterByPingStatus,
                params.requestedLoadSize,0,
                true,false
        );




        call.enqueue(new Callback<ServiceConfigurationEndPoint>() {
            @Override
            public void onResponse(Call<ServiceConfigurationEndPoint> call, Response<ServiceConfigurationEndPoint> response) {

                if (response.code() == 200) {


                    item_count = response.body().getItemCount();



                    if (response.body() != null) {



                        if (response.body().getResults() != null) {

                            dataset.add(new FilterMarkets());

                            dataset.addAll(response.body().getResults());
//                            callback.onResult(dataset,null, (long) params.requestedLoadSize);

                        }

                    }


                    if(item_count==0)
                    {
                        dataset.add(EmptyScreenDataFullScreen.getNoMarkets());
                    }


                }
                else
                {
                    dataset.add(EmptyScreenDataFullScreen.getError(response.code()));
                }

                callback.onResult(dataset,null, (long) params.requestedLoadSize);


            }

            @Override
            public void onFailure(Call<ServiceConfigurationEndPoint> call, Throwable t) {



                dataset.add(EmptyScreenDataFullScreen.getOffline());
            }
        });



    }


    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Object> callback) {

    }




    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Object> callback) {



        final List<Object> dataset = new ArrayList<>();




        String sortBy = " distance ";

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



        
        sortBy = ViewHolderFilterMarkets.getSortString(MyApplication.getAppContext());
        boolean filterByPingStatus = ViewHolderFilterMarkets.getPingStatusFilter(MyApplication.getAppContext());



        Call<ServiceConfigurationEndPoint> call = retrofit.create(MarketService.class).getMarketsList(
                PrefLoginGlobal.getAuthorizationHeaders(MyApplication.getAppContext()),
                PrefLocation.getLatitude(MyApplication.getAppContext()), PrefLocation.getLongitude(MyApplication.getAppContext()),
                null,null,
                null,null,null,
                sortBy, filterByPingStatus,
                params.requestedLoadSize,params.key.intValue(),
                false,false
        );



        System.out.println("Key Offset : "  + params.key);



        call.enqueue(new Callback<ServiceConfigurationEndPoint>() {
            @Override
            public void onResponse(Call<ServiceConfigurationEndPoint> call, Response<ServiceConfigurationEndPoint> response) {


                Long nextKey = null;

                if (response.code() == 200) {


                    if (response.body() != null) {



                        if (response.body().getResults() != null) {


                            dataset.addAll(response.body().getResults());



                            if(params.key<=item_count)
                            {
                                nextKey = params.key+params.requestedLoadSize;
                            }



                            callback.onResult(dataset,nextKey);


                        }

                    }

                }
                else
                {
                    dataset.add(EmptyScreenDataFullScreen.getError(response.code()));
                    callback.onResult(dataset,nextKey);
                }


            }

            @Override
            public void onFailure(Call<ServiceConfigurationEndPoint> call, Throwable t) {



                dataset.add(EmptyScreenDataFullScreen.getOffline());
                callback.onResult(dataset,null);



            }
        });

    }




}
