package org.nearbyshops.enduserappnew.aSellerModule.InventoryDeliveryByVendor.Fragment;

import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.nearbyshops.enduserappnew.API.OrderService;
import org.nearbyshops.enduserappnew.API.OrderServiceDeliveryPersonSelf;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.OrderEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.OrderDetail;
import org.nearbyshops.enduserappnew.Interfaces.NotifyLocation;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.NotifyTitleChangedNew;
import org.nearbyshops.enduserappnew.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.SlidingLayerSort.PreferencesSort.PrefSortOrders;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.Deprecated.ViewHolderOrderButtonDouble;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.Deprecated.ViewHolderOrderButtonSingle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeliveryByVendorFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderOrderButtonSingle.ListItemClick, ViewHolderOrder.ListItemClick,
        ViewHolderOrderButtonDouble.ListItemClick,
        NotifySort, NotifySearch, RefreshFragment, NotifyLocation {


    @Inject
    OrderService orderService;

    @Inject
    OrderServiceDeliveryPersonSelf orderServiceDelivery;


    private RecyclerView recyclerView;
    private Adapter adapter;

    public List<Object> dataset = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;


    final private int limit = 5;
    private int offset = 0;
    private int item_count = 0;


    private boolean isDestroyed;



    public DeliveryByVendorFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }


    public static DeliveryByVendorFragment newInstance(int orderStatus) {
        DeliveryByVendorFragment fragment = new DeliveryByVendorFragment();
        Bundle args = new Bundle();
        args.putInt("order_status",orderStatus);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_inventory_delivery_guy, container, false);


        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeContainer = rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();


        return rootView;
    }







    private void setupSwipeContainer()
    {
        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }






    private void setupRecyclerView()
    {

        adapter = new Adapter(dataset,this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);



        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(offset + limit > layoutManager.findLastVisibleItemPosition() + 1 - 1)
                {
                    return;
                }


                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1 + 1)
                {
                    // trigger fetch next page

//                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
//                    {
//                        return;
//                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }

//                    previous_position = layoutManager.findLastVisibleItemPosition();

                }

            }
        });
    }



//    int previous_position = -1;



    @Override
    public void onRefresh() {

        offset = 0;
        makeNetworkCall(true);
    }




    private void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                onRefresh();
            }
        });
    }





    private void makeNetworkCall(final boolean clearDataset)
    {
        User user = PrefLogin.getUser(getActivity());



        String current_sort = "";
        current_sort = PrefSortOrders.getSort(getContext()) + " " + PrefSortOrders.getAscending(getContext());


        int orderStatus = getArguments().getInt("order_status");

        Integer orderStatusHD = null;
        orderStatusHD = orderStatus;



        Integer deliveryGuyID = null;
        deliveryGuyID = user.getUserID();




        if(orderStatusHD== OrderStatusHomeDelivery.ORDER_PACKED)
        {
            // order status of home delivery order is order packed then do not filter using delivery ID
            deliveryGuyID=null;
        }



        // sort by
        // filter by date
        // filter by delivery slot
        // filter by shop
        // filter by delivery person
        // filter by delivery mode (delivery by vendor or delivery by market)



        Call<OrderEndPoint> call = orderService.getOrdersForDelivery(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                null,
                false,
                null,
                null,

                false,
                deliveryGuyID,
                Order.DELIVERY_MODE_HOME_DELIVERY,

                null,
                orderStatusHD,
                PrefLocation.getLatitude(getActivity()), PrefLocation.getLongitude(getActivity()),

                false,
                false,
                true,

                searchQuery,
                current_sort,
                limit,offset,
                clearDataset,false
        );



        call.enqueue(new Callback<OrderEndPoint>() {
            @Override
            public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.code()==200)
                {


                    if(clearDataset)
                    {
                        item_count = response.body().getItemCount();
                        dataset.clear();
                    }

                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
                    }


                    notifyTitleChanged();



                    if(offset+limit >= item_count)
                    {
                        adapter.setLoadMore(false);
                    }
                    else
                    {
                        adapter.setLoadMore(true);
                    }


                }
                else
                {
                    showToastMessage("Failed Code : "  + String.valueOf(response.code()));
                }


                swipeContainer.setRefreshing(false);





                if(item_count==0)
                {
                    dataset.clear();
                    dataset.add(EmptyScreenDataFullScreen.emptyScreenPFSINventory());
                }


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<OrderEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }



                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
            }
        });
    }





    @Override
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
        isDestroyed=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }





    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }





    private void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChangedNew)
        {
            ((NotifyTitleChangedNew)getActivity())
                    .NotifyTitleChanged(
                            "(" + dataset.size()
                                    + "/" + item_count + ")");

        }
    }





    // Refresh the Confirmed PlaceholderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }




    @Override
    public void notifyOrderSelected(Order order) {

//        PrefOrderDetail.saveOrder(order,getActivity());
//        getActivity().startActivity(new Intent(getActivity(), OrderDetail.class));

        startActivity(OrderDetail.getLaunchIntent(order.getOrderID(),getActivity()));
    }

    @Override
    public void notifyCancelOrder(Order order, int position) {

    }






    @Override
    public void buttonClicked(Order order, int position, TextView button, ProgressBar progressBar) {


        if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
        {
            acceptHandover(order,position,button,progressBar);

        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_PACKED)
        {
            pickupOrder(order,position,button,progressBar);
        }

    }


    @Override
    public void notifyCancelOrder(Order order) {

    }









    public void acceptHandover(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceDelivery.acceptOrder(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    showToastMessage("Confirmed !");

                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + response.code());
                }



                if(item_count==0)
                {
                    dataset.add(EmptyScreenDataFullScreen.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                showToastMessage("Network request failed. Check your connection !");


                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }




    public void pickupOrder(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceDelivery.startPickup(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID());



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);




                if(response.code()==200)
                {
                    showToastMessage("Successful !");

                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + response.code());
                }



                if(item_count==0)
                {
                    dataset.add(EmptyScreenDataFullScreen.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                showToastMessage("Network request failed. Check your connection !");


                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }




    @Override
    public void buttonLeftClick(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceDelivery.handoverToUser(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID(),0);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    showToastMessage("Confirmed !");

                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + response.code());
                }



                if(item_count==0)
                {
                    dataset.add(EmptyScreenDataFullScreen.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                showToastMessage("Network request failed. Check your connection !");


                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }





    @Override
    public void buttonRightClick(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceDelivery.returnOrderPackage(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    showToastMessage("Confirmed !");

                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + response.code());
                }



                if(item_count==0)
                {
                    dataset.add(EmptyScreenDataFullScreen.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                showToastMessage("Network request failed. Check your connection !");


                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });


    }





    
    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }






    private String searchQuery = null;

    @Override
    public void search(final String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }


    

    @Override
    public void refreshFragment() {
        makeRefreshNetworkCall();
    }




    @Override
    public void fetchedLocation(Location location) {
        makeRefreshNetworkCall();
    }


}
