package org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.ButterKnife;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderService;
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.OrderEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Interfaces.RefreshFragment;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.CartAndOrder.DetailOrder.OrderDetail;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.PreferencesSort.PrefSortOrders;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.SlidingLayerSortOrders;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderFullScreenProgressBar;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class OrdersHistoryFragment extends Fragment implements ViewHolderOrder.ListItemClick, SwipeRefreshLayout.OnRefreshListener,
        NotifySort, NotifySearch, RefreshFragment{





    public static final String USER_MODE_INTENT_KEY = "order_history_mode_key";




    public static final int MODE_ADMIN = 51;
    public static final int MODE_MARKET_ADMIN = 52;
    public static final int MODE_SHOP_ADMIN = 53;
    public static final int MODE_END_USER = 54;

    int currentMode ;


    public static final String TAG_SLIDING_LAYER = "sliding_layer_orders";
    public static final String IS_FILTER_BY_SHOP = "IS_FILTER_BY_SHOP";

//    @Inject
//    OrderService orderService;


    @Inject
    Gson gson;


    private RecyclerView recyclerView;
    private Adapter adapter;


    public List<Object> dataset = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;


    final private int limit = 10;
    private int offset = 0;
    int item_count = 0;


    private boolean isDestroyed;



    public OrdersHistoryFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_orders_list, container, false);
        ButterKnife.bind(this,rootView);


        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeContainer = rootView.findViewById(R.id.swipeContainer);



        if(getArguments()!=null)
        {
            currentMode = getArguments().getInt("current_mode", MODE_END_USER);
        }



        setupRecyclerView();
        setupSwipeContainer();
        setupToolbar();

        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }






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

        adapter = new Adapter(dataset,this,getActivity(),currentMode);

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


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }


                }

            }
        });
    }



//    int previous_position = -1;



    @Override
    public void onRefresh() {

        if(item_count==0)
        {
            dataset.clear();
            dataset.add(new ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData());
            adapter.notifyDataSetChanged();
        }

        offset = 0;
        makeNetworkCall(true);
    }







    private void makeRefreshNetworkCall()
    {
        onRefresh();
    }

//    swipeContainer.post(new Runnable() {
//        @Override
//        public void run() {
//            swipeContainer.setRefreshing(true);
//
//            if(!isVisible())
//            {
//                return;
//            }
//
//
//
//
//        }
//    });








    public static OrdersHistoryFragment newInstance(int endUserID, int shopID, int currentMode) {
        OrdersHistoryFragment fragment = new OrdersHistoryFragment();
        Bundle args = new Bundle();

        args.putInt("end_user_id", endUserID);
        args.putInt("shop_id", shopID);
        args.putInt("current_mode",currentMode);

        fragment.setArguments(args);
        return fragment;
    }








    private void makeNetworkCall(final boolean clearDataset)
    {


            User endUser = null;


            if(getActivity()!=null)
            {
                endUser = PrefLogin.getUser(getActivity());
            }


            if(endUser==null)
            {
                showLoginDialog();

                swipeContainer.setRefreshing(false);
                return;
            }


            String current_sort = "";

            if(getActivity()!=null)
            {
                current_sort = PrefSortOrders.getSort(getActivity()) + " " + PrefSortOrders.getAscending(getActivity());
            }



//            Integer shopID = getArguments().getInt("shop_id",0);

            Integer endUserID = null;
            Integer deliveryMode = null;


            if(getArguments()!=null)
            {
                endUserID = getArguments().getInt("end_user_id", 0);
                deliveryMode = getArguments().getInt("delivery_mode",0);



                if(endUserID==0)
                {
                    endUserID=null;
                }

                if(deliveryMode==0)
                {
                    deliveryMode=null;
                }

            }


            Integer shopID = null;


            if(currentMode==OrdersHistoryFragment.MODE_SHOP_ADMIN )
            {
                if(PrefShopAdminHome.getShop(requireActivity())==null)
                {
                    return;
                }

                shopID = PrefShopAdminHome.getShop(getActivity()).getShopID();
            }





            Boolean pickFromShop = null;




            if(getActivity()!=null && PrefSortOrders.getFilterByDeliveryType(getActivity())==SlidingLayerSortOrders.FILTER_BY_PICK_FROM_SHOP)
            {
                pickFromShop=true;
            }
            else if(getActivity()!=null && PrefSortOrders.getFilterByDeliveryType(getActivity())==SlidingLayerSortOrders.FILTER_BY_HOME_DELIVERY)
            {
                pickFromShop=false;
            }



            Boolean ordersPending = null;

            if(getActivity()!=null && PrefSortOrders.getFilterByOrderStatus(getActivity())==SlidingLayerSortOrders.FILTER_BY_STATUS_PENDING)
            {
                ordersPending = true;
            }
            else if(getActivity()!=null && PrefSortOrders.getFilterByOrderStatus(getActivity())==SlidingLayerSortOrders.FILTER_BY_STATUS_COMPLETE)
            {
                ordersPending = false;
            }


            Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServerURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



            Call<OrderEndPoint> call;

            if(currentMode==MODE_END_USER)
            {

                call = retrofit.create(OrderService.class).getOrdersForEndUser(
                        PrefLogin.getAuthorizationHeader(getActivity()),
                        shopID,
                        endUserID,
                        deliveryMode,
                        null,null,
                        null,
                        ordersPending,

                        false,false,false,

                        searchQuery,
                        current_sort,
                        limit,offset,
                        clearDataset,false
                );


            }
            else
            {

                call = retrofit.create(OrderService.class).getOrders(
                        PrefLogin.getAuthorizationHeader(getActivity()),
                        shopID,
                        endUserID,
                        deliveryMode,
                        null,
                        ordersPending,

                        false,false,false,

                        searchQuery,
                        current_sort,
                        limit,offset,
                        clearDataset,false
                );

            }



            call.enqueue(new Callback<OrderEndPoint>() {
                @Override
                public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }


                    if(response.code()==200)
                    {
                        if(response.body()!= null)
                        {


                            if(clearDataset)
                            {
                                dataset.clear();
                                item_count = response.body().getItemCount();



                                int i = 0;

                            }

                            if(response.body().getResults()!=null)
                            {
                                dataset.addAll(response.body().getResults());
                            }



                            if(item_count==0)
                            {
                                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenOrders());
                            }

                            adapter.notifyDataSetChanged();
                        }

                    }
                    else
                    {

                        showToastMessage("Failed Code : " + response.code());

                    }



                    if(offset+limit >= item_count)
                    {
                        adapter.setLoadMore(false);
                    }
                    else
                    {
                        adapter.setLoadMore(true);
                    }



                    swipeContainer.setRefreshing(false);
                }




                @Override
                public void onFailure(Call<OrderEndPoint> call, Throwable t) {

                    if(isDestroyed)
                    {
                        return;
                    }


//                    emptyScreen.setVisibility(View.VISIBLE);
//                    showToastMessage("Network Request failed !");


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
        isDestroyed=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        isDestroyed=true;
    }





    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }








    @Override
    public void notifyOrderSelected(Order order) {

//        PrefOrderDetail.saveOrder(order,getActivity());
        startActivity(OrderDetail.getLaunchIntent(order.getOrderID(),getActivity()));
    }





    @Override
    public void notifyCancelOrder(final Order order) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Cancel Order !")
                .setMessage("Are you sure you want to cancel this order !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        cancelOrder(order);
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







    public static final String TAG_LOGIN_DIALOG = "tag_login_dialog";

    private void showLoginDialog()
    {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }



    @Override
    public void refreshFragment() {
        makeRefreshNetworkCall();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3262 && resultCode ==3121)
        {
           makeRefreshNetworkCall();
        }
    }




    void setupToolbar()
    {
        if(getActivity() instanceof SetToolbarText)
        {
            ((SetToolbarText) getActivity()).setToolbar(true,"Order History",false,null);
        }
    }
}
