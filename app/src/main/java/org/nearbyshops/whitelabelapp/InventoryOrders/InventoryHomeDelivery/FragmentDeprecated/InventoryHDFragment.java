package org.nearbyshops.whitelabelapp.InventoryOrders.InventoryHomeDelivery.FragmentDeprecated;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderService;
import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderServiceShopStaff;
import org.nearbyshops.whitelabelapp.Interfaces.RefreshAdjacentFragment;
import org.nearbyshops.whitelabelapp.Lists.UsersList.UsersListFragment;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.OrderEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.ApplicationState;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.CartAndOrder.DetailOrder.OrderDetail;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyTitleChanged;
import org.nearbyshops.whitelabelapp.Interfaces.RefreshFragment;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.Deprecated.ViewHolderOrderButtonDouble;
import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryHomeDelivery.HomeDelivery;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.PreferencesSort.PrefSortOrders;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.InventoryOrders.FilterDeliveryGuy.FilterDeliveryGuy;
import org.nearbyshops.whitelabelapp.Lists.UsersList.UsersList;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.Deprecated.ViewHolderOrderButtonSingle;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.Deprecated.ViewHolderOrderSelectable;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InventoryHDFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderOrderButtonSingle.ListItemClick, ViewHolderOrder.ListItemClick,
        ViewHolderOrderButtonDouble.ListItemClick,
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


    private SwipeRefreshLayout swipeContainer;


    final private int limit = 5;
    private int offset = 0;
    private int item_count = 0;


    private boolean isDestroyed;




    Date selectedDate;
    Integer deliverySlotID;
    Integer deliveryGuyID;
    boolean isASAPDelivery = false;




    public InventoryHDFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }



    public static InventoryHDFragment newInstance(int orderStatus) {
        InventoryHDFragment fragment = new InventoryHDFragment();
        Bundle args = new Bundle();
        args.putInt("order_status",orderStatus);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);



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



        String current_sort = "";
        current_sort = PrefSortOrders.getSort(getContext()) + " " + PrefSortOrders.getAscending(getContext());


        int orderStatus = getArguments().getInt("order_status");


        Integer orderStatusHD = null;
        orderStatusHD = orderStatus;


        int shopID = PrefShopAdminHome.getShop(getActivity()).getShopID();




        Call<OrderEndPoint> call;



        call = orderService.getOrdersForDelivery(
                PrefLogin.getAuthorizationHeader(getActivity()),
                null,
                false,
                null,

                shopID,

                false,
                deliveryGuyID,

                null,

                null,
                orderStatusHD,

                PrefLocation.getLatitudeSelected(getActivity()), PrefLocation.getLongitudeSelected(getActivity()),
                false,false,false,
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
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }

                swipeContainer.setRefreshing(false);




                if(item_count==0)
                {
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
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

        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged)getActivity())
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

//        PrefOrderDetail.saveOrder(order,getActivity());
//        getActivity().startActivity(new Intent(getActivity(), OrderDetail.class));

        startActivity(OrderDetail.getLaunchIntent(order.getOrderID(),getActivity()));
    }





    @Override
    public void notifyCancelOrder(Order order) {

    }




    @Override
    public void notifyCancelOrder(final Order order, final int position) {


    }





    @Override
    public void buttonLeftClick(Order order, int position, TextView button, ProgressBar progressBar) {

        if(order.getStatusCurrent()==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
        {
            deliverOrderHD(order,position,button,progressBar);
        }
    }




    @Override
    public void buttonRightClick(Order order, int position, TextView button, ProgressBar progressBar) {

        if(order.getStatusCurrent()==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
        {
            returnOrderHD(order,position,button,progressBar);
        }
    }







    @Override
    public void buttonClicked(Order order, int position, TextView button, ProgressBar progressBar) {


        if(order.getStatusCurrent()== OrderStatusHomeDelivery.ORDER_PLACED)
        {
            confirmOrderHD(order,position,button,progressBar);
        }
        else if(order.getStatusCurrent()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
        {
            setOrderPackedHD(order,position,button,progressBar);
        }
        else if(order.getStatusCurrent()==OrderStatusHomeDelivery.ORDER_PACKED)
        {
            deliverBySelfHD(order,position,button,progressBar);
        }
        else if(order.getStatusCurrent()==OrderStatusHomeDelivery.RETURN_REQUESTED)
        {
            acceptReturnHD(order,position,button,progressBar);
        }
        else if(order.getStatusCurrent()==OrderStatusHomeDelivery.RETURNED_ORDERS)
        {
            unpackOrderHD(order,position,button,progressBar);
        }
        else if(order.getStatusCurrent()==OrderStatusHomeDelivery.DELIVERED)
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






    public void confirmOrderHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceShopStaff.confirmOrder(
                PrefLogin.getAuthorizationHeader(getActivity()),
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });


    }



    public void setOrderPackedHD(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceShopStaff.setOrderPacked(
                PrefLogin.getAuthorizationHeader(getActivity()),
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }




    public void deliverBySelfHD(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceShopStaff.setOutForDelivery(
                PrefLogin.getAuthorizationHeader(getActivity()),
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }




    public void deliverOrderHD(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceShopStaff.deliverOrder(
                PrefLogin.getAuthorizationHeader(getActivity()),
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }



    public void returnOrderHD(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceShopStaff.returnOrder(
                PrefLogin.getAuthorizationHeader(getActivity()),
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }






    public void cancelHandoverHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.undoHandover(
                PrefLogin.getAuthorizationHeader(getActivity()),
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }



    public void acceptReturnHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.acceptReturn(
                PrefLogin.getAuthorizationHeader(getActivity()),
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }


    @Override
    public void unpackOrderHD(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<ResponseBody> call = orderServiceShopStaff.unpackOrder(
                PrefLogin.getAuthorizationHeader(getActivity()),
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }



    public void paymentReceivedHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.paymentReceived(
                PrefLogin.getAuthorizationHeader(getActivity()),
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenPFSINventory());
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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
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
                PrefLogin.getAuthorizationHeader(getActivity()),
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
            filterDeliveryGuy();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
        {
            filterDeliveryGuy();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.RETURN_REQUESTED)
        {
            filterDeliveryGuy();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.RETURNED_ORDERS)
        {
            handoverToDeliveryStart();
        }
    }



    private void filterDeliveryGuy()
    {

        int orderStatus = getArguments().getInt("order_status");


        Intent intent = new Intent(getActivity(), FilterDeliveryGuy.class);
        intent.putExtra("order_status",orderStatus);
        startActivityForResult(intent,562);
    }



}
