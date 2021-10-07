package org.nearbyshops.whitelabelapp.zSampleCode.OrderHistoryPaging.ViewModel;




import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.OrderEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.PreferencesSort.PrefSortOrders;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.SlidingLayerSortOrders;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersDataSource extends PageKeyedDataSource<Long,Object> {


    @Inject
    OrderService orderService;


    private int item_count;





    public OrdersDataSource() {
        DaggerComponentBuilder.getInstance().getNetComponent()
                .Inject(this);
    }






    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Object> callback) {



        User endUser = PrefLogin.getUser(MyApplication.getAppContext());

        List<Object> dataset = new ArrayList<>();



        if (endUser == null) {
            return;
        }



        String current_sort = "";

        current_sort = PrefSortOrders.getSort(MyApplication.getAppContext()) + " " + PrefSortOrders.getAscending(MyApplication.getAppContext());


//        boolean filterOrdersByUser = getArguments().getBoolean("filter_orders_by_user", false);
//        boolean filterOrdersByShop = getArguments().getBoolean("filter_orders_by_shop", false);
//        boolean filterOrdersByDelivery = getArguments().getBoolean("filter_orders_by_delivery", false);


        Boolean pickFromShop = null;


        if (MyApplication.getAppContext() != null && PrefSortOrders.getFilterByDeliveryType(MyApplication.getAppContext()) == SlidingLayerSortOrders.FILTER_BY_PICK_FROM_SHOP) {
            pickFromShop = true;
        } else if (MyApplication.getAppContext() != null && PrefSortOrders.getFilterByDeliveryType(MyApplication.getAppContext()) == SlidingLayerSortOrders.FILTER_BY_HOME_DELIVERY) {
            pickFromShop = false;
        }


        Boolean ordersPendingStatus = null;

        if (MyApplication.getAppContext() != null && PrefSortOrders.getFilterByOrderStatus(MyApplication.getAppContext()) == SlidingLayerSortOrders.FILTER_BY_STATUS_PENDING) {
            ordersPendingStatus = true;
        } else if (MyApplication.getAppContext() != null && PrefSortOrders.getFilterByOrderStatus(MyApplication.getAppContext()) == SlidingLayerSortOrders.FILTER_BY_STATUS_COMPLETE) {
            ordersPendingStatus = false;
        }



        Call<OrderEndPoint> call = orderService.getOrders(
                PrefLogin.getAuthorizationHeader(MyApplication.getAppContext()),
                null,
                null,
                null,
                null,
                ordersPendingStatus,
                false,false, false,
                null,
                current_sort,
                params.requestedLoadSize, 0,
                true,false
        );




        call.enqueue(new Callback<OrderEndPoint>() {
            @Override
            public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {


                if (response.code() == 200) {


                    item_count = response.body().getItemCount();



                    if (response.body() != null) {



                        if (response.body().getResults() != null) {

                            dataset.addAll(response.body().getResults());
                            callback.onResult(dataset,null, (long) params.requestedLoadSize);

                        }

                    }


                    if(item_count==0)
                    {
                        dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenOrders());
                    }


                }

            }


            @Override
            public void onFailure(Call<OrderEndPoint> call, Throwable t) {


            }
        });

    }


    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Object> callback) {

    }




    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Object> callback) {



        User endUser = PrefLogin.getUser(MyApplication.getAppContext());

        List<Object> dataset = new ArrayList<>();



        if (endUser == null) {
            return;
        }






        String current_sort = "";

        current_sort = PrefSortOrders.getSort(MyApplication.getAppContext()) + " " + PrefSortOrders.getAscending(MyApplication.getAppContext());


//        boolean filterOrdersByUser = getArguments().getBoolean("filter_orders_by_user", false);
//        boolean filterOrdersByShop = getArguments().getBoolean("filter_orders_by_shop", false);
//        boolean filterOrdersByDelivery = getArguments().getBoolean("filter_orders_by_delivery", false);


        Boolean pickFromShop = null;


        if (MyApplication.getAppContext() != null && PrefSortOrders.getFilterByDeliveryType(MyApplication.getAppContext()) == SlidingLayerSortOrders.FILTER_BY_PICK_FROM_SHOP) {
            pickFromShop = true;
        } else if (MyApplication.getAppContext() != null && PrefSortOrders.getFilterByDeliveryType(MyApplication.getAppContext()) == SlidingLayerSortOrders.FILTER_BY_HOME_DELIVERY) {
            pickFromShop = false;
        }


        Boolean ordersPendingStatus = null;

        if (MyApplication.getAppContext() != null && PrefSortOrders.getFilterByOrderStatus(MyApplication.getAppContext()) == SlidingLayerSortOrders.FILTER_BY_STATUS_PENDING) {
            ordersPendingStatus = true;
        } else if (MyApplication.getAppContext() != null && PrefSortOrders.getFilterByOrderStatus(MyApplication.getAppContext()) == SlidingLayerSortOrders.FILTER_BY_STATUS_COMPLETE) {
            ordersPendingStatus = false;
        }


        Call<OrderEndPoint> call = orderService.getOrders(
                PrefLogin.getAuthorizationHeader(MyApplication.getAppContext()),
                null,
                null,
                null,
                null,
                null,
                false,false,false,
                null,
                current_sort,
                params.requestedLoadSize, params.key.intValue(),
                false,false);




        System.out.println("Key Offset : "  + params.key);


        call.enqueue(new Callback<OrderEndPoint>() {
            @Override
            public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {


                if (response.code() == 200) {


                    if (response.body() != null) {



                        if (response.body().getResults() != null) {

//                            dataset.addAll(response.body().getResults());


                            List<Object> list = new ArrayList<>(response.body().getResults());




//                            Long nextKey = (params.key <= item_count) ? null : params.key + params.requestedLoadSize;

                            Long nextKey = null;

                            if(params.key<=item_count)
                            {
                                nextKey = params.key+params.requestedLoadSize;
                            }



                            callback.onResult(list,nextKey);


                        }

                    }

                }

            }


            @Override
            public void onFailure(Call<OrderEndPoint> call, Throwable t) {


            }
        });


    }




}
