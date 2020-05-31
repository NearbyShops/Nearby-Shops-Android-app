package org.nearbyshops.enduserappnew.aSellerModule.InventoryOrders.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.nearbyshops.enduserappnew.API.OrderService;
import org.nearbyshops.enduserappnew.API.OrderServiceShopStaff;
import org.nearbyshops.enduserappnew.Interfaces.RefreshAdjacentFragment;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersListFragment;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.OrderEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.ApplicationState;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.OrderDetail;
import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.PrefOrderDetail;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.NotifyTitleChangedNew;
import org.nearbyshops.enduserappnew.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.aSellerModule.InventoryOrders.HomeDeliveryInventory.HomeDelivery;
import org.nearbyshops.enduserappnew.SlidingLayerSort.PreferencesSort.PrefSortOrders;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.aSellerModule.FilterDeliveryGuy.FilterDeliveryGuy;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderButtonSingle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderSelectable;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderWithDeliveryStaff;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.aSellerModule.InventoryOrders.PickFromShopInventory.PickFromShopInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrdersInventoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderOrderButtonSingle.ListItemClick, ViewHolderOrder.ListItemClick,
        ViewHolderOrderWithDeliveryStaff.ListItemClick,
        NotifySort, NotifySearch, RefreshFragment,
        ViewHolderOrderSelectable.ListItemClick,
        HomeDelivery.HandoverClicked {


    @Inject
    OrderService orderService;

    @Inject
    OrderServiceShopStaff orderServiceShopStaff;

    private RecyclerView recyclerView;
    private Adapter adapter;

    public List<Object> dataset = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;


    final private int limit = 5;
    private int offset = 0;
    private int item_count = 0;


    private boolean isDestroyed;



    public OrdersInventoryFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }


    public static OrdersInventoryFragment newInstance(int orderStatus, boolean isPickFromShop) {
        OrdersInventoryFragment fragment = new OrdersInventoryFragment();
        Bundle args = new Bundle();
        args.putInt("order_status",orderStatus);
        args.putBoolean("is_pick_from_shop",isPickFromShop);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_inventory_orders, container, false);


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

//            Shop currentShop = UtilityShopHome.getShopDetails(getContext());


        String current_sort = "";
        current_sort = PrefSortOrders.getSort(getContext()) + " " + PrefSortOrders.getAscending(getContext());


        int orderStatus = getArguments().getInt("order_status");
        boolean isPickFromShop = getArguments().getBoolean("is_pick_from_shop");

        boolean getDeliveryProfile = false;


        Integer orderStatusHD = null;
        Integer orderStatusPFS = null;

        if(isPickFromShop)
        {
            orderStatusPFS = orderStatus;
        }
        else
        {
            orderStatusHD = orderStatus;

            if(orderStatusHD>OrderStatusHomeDelivery.ORDER_PACKED)
            {
                getDeliveryProfile = true;
            }
        }




        Call<OrderEndPoint> call = orderService.getOrders(
                    PrefLogin.getAuthorizationHeaders(getActivity()),
                true, false,
                getDeliveryProfile,
                    deliveryGuyID,
                    isPickFromShop, orderStatusHD, orderStatusPFS,
                PrefLocation.getLatitude(getActivity()),PrefLocation.getLongitude(getActivity()),
                    null,
                    searchQuery, current_sort,
                limit,offset,clearDataset, false
        );





        call.enqueue(new Callback<OrderEndPoint>() {
            @Override
            public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!= null)
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


//                    adapter.notifyDataSetChanged();
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

                swipeContainer.setRefreshing(false);




                if(item_count==0)
                {
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

//                showToastMessage("Network Request failed !");
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










    private void refreshNeighboringFragment()
    {

        if(getActivity() instanceof RefreshAdjacentFragment)
        {
            ((RefreshAdjacentFragment) getActivity()).refreshAdjacentFragment();
        }
    }






    @Override
    public void notifyOrderSelected(Order order) {

        PrefOrderDetail.saveOrder(order,getActivity());
        getActivity().startActivity(new Intent(getActivity(), OrderDetail.class));
    }





    @Override
    public void notifyCancelOrder(Order order) {

    }




    @Override
    public void notifyCancelOrder(final Order order, final int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Cancel Order !")
                .setMessage("Are you sure you want to cancel this order !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        cancelOrder(order, position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage(" Not Cancelled !");
                    }
                })
                .show();
    }




    @Override
    public void buttonClicked(Order order, int position, TextView button, ProgressBar progressBar) {

        if(order.isPickFromShop())
        {
            if(order.getStatusPickFromShop()== OrderStatusPickFromShop.ORDER_PLACED)
            {
                confirmOrderPFS(order,position,button,progressBar);

            }
            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_CONFIRMED)
            {
                setOrderPackedPFS(order,position,button,progressBar);
            }
            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_PACKED)
            {

                readyForPickupPFS(order,position,button,progressBar);

            }
            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
            {
                paymentReceivedPFS(order,position,button,progressBar);
            }

        } else {


            if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PLACED)
            {
                confirmOrderHD(order,position,button,progressBar);
            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
            {
                setOrderPackedHD(order,position,button,progressBar);
            }
        }

    }








    @Override
    public void buttonClickedWithStaff(Order order, int position, TextView button, ProgressBar progressBar) {


        if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
        {
            cancelHandoverHD(order,position,button,progressBar);
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURN_REQUESTED)
        {
            acceptReturnHD(order,position,button,progressBar);
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.DELIVERED)
        {
            paymentReceivedHD(order,position,button,progressBar);
        }

    }






    @Override
    public void selectionStarted() {

    }

    @Override
    public void selectedStopped() {

    }





    public void confirmOrderPFS(Order order, int position, TextView button, ProgressBar progressBar) {



        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<ResponseBody> call = orderServiceShopStaff.confirmOrderPFS(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );




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

                    refreshNeighboringFragment();
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


                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

//                showToastMessage("Network request failed. Check your connection !");
            }
        });
    }



    public void setOrderPackedPFS(Order order, int position, TextView button, ProgressBar progressBar) {



        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.setOrderPackedPFS(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );


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

                    refreshNeighboringFragment();
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



    public void readyForPickupPFS(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<ResponseBody> call = orderServiceShopStaff.setOrderReadyForPickupPFS(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );



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

                    refreshNeighboringFragment();
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



    public void paymentReceivedPFS(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.paymentReceivedPFS(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );



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

                    refreshNeighboringFragment();
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



    public void confirmOrderHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceShopStaff.confirmOrder(
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

                    refreshNeighboringFragment();
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



    public void setOrderPackedHD(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceShopStaff.setOrderPacked(
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
                    showToastMessage("Packed !");

                    refreshNeighboringFragment();
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




    public void cancelHandoverHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.undoHandover(
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
                    showToastMessage("Handover reversed !");

                    refreshNeighboringFragment();
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



    public void acceptReturnHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.acceptReturn(
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
                    showToastMessage("Return Accepted !");

                    refreshNeighboringFragment();
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
    public void unpackOrderHD(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<ResponseBody> call = orderServiceShopStaff.unpackOrder(
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
                    showToastMessage("Order Deleted !");

                    refreshNeighboringFragment();
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



    public void paymentReceivedHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.paymentReceived(
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
                    showToastMessage("Done !");

                    refreshNeighboringFragment();
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



    private void cancelOrder(final Order order, final int position) {


//        Call<ResponseBody> call = orderService.cancelOrderByShop(order.getOrderID());

        Call<ResponseBody> call = orderServiceShopStaff.cancelledByShop(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                if(response.code() == 200 )
                {
                    showToastMessage("Successful");
//                    makeRefreshNetworkCall();

                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();
                }
                else if(response.code() == 304)
                {
                    showToastMessage("Not Cancelled !");
                }
                else
                {
                    showToastMessage("Server Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Check your internet connection !");
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










    private int selectedDeliveryGuy = 0;
    private ArrayList<Order> selected = new ArrayList<>();



    private void handoverToDeliveryStart()
    {


        if(adapter.getSelectedOrders().size()==0)
        {
            showToastMessage("No Orders Selected !");

            return;
        }


        selected = new ArrayList<>();

        for (Map.Entry<Integer, Order> entry : adapter.getSelectedOrders().entrySet())
        {
//            System.out.println(entry.getKey() + "/" + entry.getValue());

            selected.add(entry.getValue());

        }

        //intent.putExtra("selected",selected);

        ApplicationState.getInstance().getSelectedOrdersForDelivery().clear();
        ApplicationState.getInstance().getSelectedOrdersForDelivery().addAll(selected);


//        Intent intent = new Intent(getActivity(), ConfirmItemsForDelivery.class);
//        getActivity().startActivity(intent);





        Intent intent = new Intent(getActivity(), UsersList.class);
//        intent.putExtra("select_delivery_guy",true);
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY, UsersListFragment.MODE_SELECT_DELIVERY_PERSON);
        startActivityForResult(intent,123);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123 && resultCode==456)
        {
            selectedDeliveryGuy = data.getIntExtra("delivery_guy_id",0);
            handoverTODelivery();
        }
        else if(requestCode==562 && resultCode==458)
        {

            deliveryGuyID=data.getIntExtra("delivery_guy_id",-1);

            if(deliveryGuyID==-1)
            {
                deliveryGuyID=null;
            }


            makeRefreshNetworkCall();
        }
    }





    private void handoverTODelivery()
    {

        Call<ResponseBody> call
                = orderServiceShopStaff.handoverToDelivery(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                selectedDeliveryGuy,
                selected);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code() == 200)
                {
                    showToastMessage("Handover Successful !");
                    adapter.getSelectedOrders().clear();

                    makeRefreshNetworkCall();
                }
                else
                {
                    showToastMessage("Error Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request failed. Try again !");
            }
        });

    }





    @Override
    public void handoverClicked() {

        int orderStatus = getArguments().getInt("order_status");
        boolean isPickFromShop = getArguments().getBoolean("is_pick_from_shop");


        if(!isPickFromShop && orderStatus == OrderStatusHomeDelivery.ORDER_PACKED)
        {
            handoverToDeliveryStart();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
        {
            selectDeliveryGuy();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
        {
            selectDeliveryGuy();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.RETURN_REQUESTED)
        {
            selectDeliveryGuy();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.RETURNED_ORDERS)
        {
            handoverToDeliveryStart();
        }


    }







    private Integer deliveryGuyID = null;

    private void selectDeliveryGuy()
    {

        int orderStatus = getArguments().getInt("order_status");


        Intent intent = new Intent(getActivity(), FilterDeliveryGuy.class);
        intent.putExtra("order_status",orderStatus);
        startActivityForResult(intent,562);
    }



}
