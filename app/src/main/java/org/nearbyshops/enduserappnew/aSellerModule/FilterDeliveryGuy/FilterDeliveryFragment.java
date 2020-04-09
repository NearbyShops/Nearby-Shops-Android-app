package org.nearbyshops.enduserappnew.aSellerModule.FilterDeliveryGuy;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.nearbyshops.enduserappnew.API.OrderServiceShopStaff;
import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.UserEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 14/6/17.
 */

public class FilterDeliveryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Adapter.NotificationsFromAdapter{

    private boolean isDestroyed = false;


    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;


    @Inject
    UserService userService;

    @Inject
    OrderServiceShopStaff service;

    private GridLayoutManager layoutManager;
    private Adapter listAdapter;

    private ArrayList<Object> dataset = new ArrayList<>();


    // flags
    private boolean clearDataset = false;

    private boolean getRowCountVehicle = false;
    private boolean resetOffsetVehicle = false;


    private int limit_vehicle = 10;
    private int offset_vehicle = 0;
    int item_count_vehicle = 0;



//    @BindView(R.id.drivers_count) TextView driversCount;
//    int i = 1;


    public FilterDeliveryFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_delivery_guy_list, container, false);
        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        toolbar.setTitle("Select Delivery");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//


        setupSwipeContainer();
        setupRecyclerView();

        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }


//        driversCount.setText("Drivers COunt : " + String.valueOf(++i));

        return rootView;
    }






    private void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }





    private void setupRecyclerView()
    {

        listAdapter = new Adapter(dataset,getActivity(),this,this);
        recyclerView.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    if(offset_vehicle + limit_vehicle > layoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }


                    // trigger fetch next page

                    if((offset_vehicle + limit_vehicle)<= item_count_vehicle)
                    {
                        offset_vehicle = offset_vehicle + limit_vehicle;

                        getDeliveryGuys();
                    }


                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
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




    @Override
    public void onRefresh() {

        clearDataset = true;
        getRowCountVehicle = true;
        resetOffsetVehicle = true;

        getDeliveryGuys();
    }







    private void getDeliveryGuys()
    {

        if(resetOffsetVehicle)
        {
            offset_vehicle = 0;
            resetOffsetVehicle = false;
        }


        User user = PrefLogin.getUser(getActivity());

        if(user ==null)
        {
            swipeContainer.setRefreshing(false);
            return;
        }



        int orderStatus = getActivity().getIntent().getIntExtra("order_status",-1);



        Call<UserEndpoint> call = service.fetchDeliveryGuys(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                orderStatus,
                null,null,null,true,false
        );





        call.enqueue(new Callback<UserEndpoint>() {
            @Override
            public void onResponse(Call<UserEndpoint> call, Response<UserEndpoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200 && response.body()!=null) {

                    if (clearDataset) {
                        dataset.clear();
                        clearDataset = false;

//                        dataset.add(new FilterSubmissions());
                    }


                    if (getRowCountVehicle) {

                        item_count_vehicle = response.body().getItemCount();
                        getRowCountVehicle = false;

//                            dataset.add(new HeaderData("Type of Data"));

                        dataset.add(new HeaderTitle("Delivery Staff"));
                    }


                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
                    }

                    listAdapter.notifyDataSetChanged();
                }


                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UserEndpoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Connection Failed !");

                swipeContainer.setRefreshing(false);
            }
        });


    }






    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }



//    @Override
//    public void taxiFiltersChanged() {
//        makeRefreshNetworkCall();
//    }



    @Override
    public void notifyTripRequestSelected() {

    }










    @Override
    public void listItemClick(User user, int position) {


        Intent data = new Intent();
        data.putExtra("delivery_guy_id",user.getUserID());
        getActivity().setResult(458,data);

        getActivity().finish();
    }




    @Override
    public boolean listItemLongClick(View view, User tripRequest, int position) {
        return false;
    }






}
