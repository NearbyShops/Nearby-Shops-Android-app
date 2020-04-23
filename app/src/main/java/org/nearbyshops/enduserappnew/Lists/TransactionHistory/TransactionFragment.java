package org.nearbyshops.enduserappnew.Lists.TransactionHistory;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;


import org.nearbyshops.enduserappnew.API.TransactionService;
import org.nearbyshops.enduserappnew.Lists.TransactionHistory.ViewHolders.ViewHolderTransactionDetail;
import org.nearbyshops.enduserappnew.Model.ModelBilling.Transaction;
import org.nearbyshops.enduserappnew.Model.ModelBilling.TransactionEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
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

public class TransactionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewHolderTransactionDetail.ListItemClick {

    boolean isDestroyed = false;


    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;


    @Inject
    TransactionService service;

    GridLayoutManager layoutManager;
    Adapter listAdapter;

    ArrayList<Object> dataset = new ArrayList<>();


    // flags
    boolean clearDataset = false;

    boolean getRowCountVehicle = false;
    boolean resetOffsetVehicle = false;


    private int limit_vehicle = 30;
    int offset_vehicle = 0;
    public int item_count_vehicle = 0;


//    @BindView(R.id.drivers_count) TextView driversCount;
//    int i = 1;

    public TransactionFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);
        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//        toolbar.setTitle("Trip History");
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        setupSwipeContainer();
        setupRecyclerView();

        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }


//        driversCount.setText("Drivers COunt : " + String.valueOf(++i));

        return rootView;
    }



//    @OnClick({R.id.filters})
//    void showFilters()
//    {
////        FilterTaxi filterTaxi = new FilterTaxi();
////        filterTaxi.show(getActivity().getSupportFragmentManager(),"filter_taxi");
//    }




    void setupSwipeContainer()
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



    void setupRecyclerView()
    {

        listAdapter = new Adapter(dataset,getActivity(),this);
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

                        getTransactions();
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



    void makeRefreshNetworkCall()
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

        getTransactions();
    }








    void getTransactions()
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




        Call<TransactionEndpoint> call = service.getTransactions(
                PrefLogin.getAuthorizationHeaders(getActivity()),null,null,
                Transaction.TIMESTAMP_OCCURRED + " desc",limit_vehicle,offset_vehicle,getRowCountVehicle,false
        );



        call.enqueue(new Callback<TransactionEndpoint>() {
            @Override
            public void onResponse(Call<TransactionEndpoint> call, Response<TransactionEndpoint> response) {



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

                        dataset.add(new HeaderTitle("Transactions"));
                    }


                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
                    }

                    listAdapter.notifyDataSetChanged();
                }
                else if(response.code()==401 || response.code() == 403)
                {
                    showToastMessage("Not Permitted");
                }
                else
                {
                    showToastMessage("Failed Code : " + response.code());
                }


                if(item_count_vehicle==0)
                {

                    dataset.add(EmptyScreenDataFullScreen.getNoTransactions());


                    listAdapter.notifyDataSetChanged();
                }

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<TransactionEndpoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }



                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                listAdapter.notifyDataSetChanged();


//                showToastMessage("Network Connection Failed !");

                swipeContainer.setRefreshing(false);
            }
        });
    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }








    @Override
    public void listItemClick(Transaction transaction, int position) {

        Gson gson = new Gson();
        String jsonString = gson.toJson(transaction);
//
//        Intent intent = new Intent(getActivity(), TripHistoryDetail.class);
//        intent.putExtra(TripHistoryDetail.TRIP_HISTORY_DETAIL_INTENT_KEY,jsonString);
//        startActivity(intent);
    }






}
