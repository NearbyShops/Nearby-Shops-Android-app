package org.nearbyshops.whitelabelapp.AdminCommon.SalesReport;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.API.TransactionService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelBilling.Transaction;
import org.nearbyshops.whitelabelapp.Model.ModelBilling.TransactionEndpoint;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.AdminCommon.SalesReport.ViewHolders.ViewHolderSalesReport;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

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

public class SalesReportFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewHolderSalesReport.ListItemClick {

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


    private int limit = 30;
    int offset = 0;
    public int item_count = 0;



    public SalesReportFragment() {

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

                    if(offset + limit > layoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }


                    // trigger fetch next page

                    if((offset + limit)<= item_count)
                    {
                        offset = offset + limit;

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
            offset = 0;
            resetOffsetVehicle = false;
        }


        User user = PrefLogin.getUser(getActivity());

        if(user ==null)
        {
            swipeContainer.setRefreshing(false);
            return;
        }




        Call<TransactionEndpoint> call = null;

        int userID = getActivity().getIntent().getIntExtra("user_id",0);

        if(userID==0)
        {
            call = service.getTransactions(
                    PrefLogin.getAuthorizationHeader(getActivity()),
                    null,null,
                    Transaction.TIMESTAMP_OCCURRED + " desc", limit, offset,getRowCountVehicle,false
            );

        }
        else
        {

            call = service.getUserTransactions(
                    PrefLogin.getAuthorizationHeader(getActivity()),
                    userID,
                    null,null,
                    Transaction.TIMESTAMP_OCCURRED + " desc",
                    limit, offset,
                    getRowCountVehicle,false
            );
        }




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

                        item_count = response.body().getItemCount();
                        getRowCountVehicle = false;

                        dataset.add(new ViewHolderHeader.HeaderTitle("Transactions"));
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


                if(item_count ==0)
                {

                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getNoTransactions());


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
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                listAdapter.notifyDataSetChanged();


//                showToastMessage("Network Connection Failed !");

                swipeContainer.setRefreshing(false);
            }
        });
    }




    void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
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
