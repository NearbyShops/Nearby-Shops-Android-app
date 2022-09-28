package org.nearbyshops.whitelabelapp.InventoryOrders.InventoryPickFromShop.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyTitleChanged;
import org.nearbyshops.whitelabelapp.Interfaces.RefreshAdjacentFragment;
import org.nearbyshops.whitelabelapp.Interfaces.RefreshFragment;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.OrderEndPoint;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.PreferencesSort.PrefSortOrders;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderInventory.ViewHolderOrderShopAdminPFS;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InventoryPFSFragmentNew extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderOrderShopAdminPFS.ListItemClick, ViewHolderOrder.ListItemClick,
        NotifySort, NotifySearch, RefreshFragment{



    @Inject
    OrderService orderService;




    private RecyclerView recyclerView;
    private Adapter adapter;

    public List<Object> dataset = new ArrayList<>();



    private SwipeRefreshLayout swipeContainer;


    final private int limit = 5;
    private int offset = 0;
    private int item_count = 0;


    private boolean isDestroyed;



    public InventoryPFSFragmentNew() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }




    public static InventoryPFSFragmentNew newInstance(int orderStatus) {
        InventoryPFSFragmentNew fragment = new InventoryPFSFragmentNew();
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


        Integer orderStatusPFS = null;
        orderStatusPFS = orderStatus;


        int shopID = PrefShopAdminHome.getShop(getActivity()).getShopID();



        Call<OrderEndPoint> call;



        call = orderService.getOrders(
                PrefLogin.getAuthorizationHeader(getActivity()),
                shopID, null,
                Order.DELIVERY_MODE_PICKUP_FROM_SHOP,
                orderStatusPFS,
                null,
                false,false,false,
                searchQuery, current_sort,
                limit,offset,
                clearDataset, false
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
        UtilityFunctions.showToastMessage(getActivity(),message);
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

    }





    @Override
    public void statusUpdateSuccessful(Order order, int position) {

        refreshNeighboringFragment();
        dataset.remove(order);
        item_count = item_count - 1;
        adapter.notifyItemRemoved(position);
        notifyTitleChanged();

    }


    @Override
    public void notifyCancelOrder(Order order) {

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


}
