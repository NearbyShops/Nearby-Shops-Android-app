package org.nearbyshops.enduserappnew.aSuperAdminModule.MarketsList;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.API.API_SDS.MarketService;
import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.EditDataScreens.EditMarket.EditMarket;
import org.nearbyshops.enduserappnew.EditDataScreens.EditMarket.EditMarketFragment;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ServiceConfigurationEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.Models.FilterMarkets;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.ViewHolderFilterMarkets;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.aSuperAdminModule.MarketsListPagingLib.ViewHolder.ViewHolderMarketAdmin;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 14/6/17.
 */

public class MarketFragmentList extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderMarketAdmin.ListItemClick, ViewHolderFilterMarkets.ListItemClick, NotifySearch
{


    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;


    @Inject
    UserService userService;


    @BindView(R.id.fab) FloatingActionButton fab;


    @Inject
    Gson gson;


    private GridLayoutManager layoutManager;
    private Adapter listAdapter;

    private ArrayList<Object> dataset = new ArrayList<>();


    // flags
    private boolean clearDataset = false;


    private int limit = 10;
    private int offset = 0;
    public int item_count = 0;



    public MarketFragmentList() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_users_list, container, false);
        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        toolbar.setTitle("Users List");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        setupSwipeContainer();
        setupRecyclerView();

        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }



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

                        getMarkets();
                    }


                }
            }
        });

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
        getMarkets();
    }






    private void getMarkets()
    {

        if(clearDataset)
        {
            offset = 0;
        }




        String sortBy = " distance ";


        sortBy = ViewHolderFilterMarkets.getSortString(MyApplication.getAppContext());
        boolean filterByPingStatus = ViewHolderFilterMarkets.getPingStatusFilter(MyApplication.getAppContext());



        User user = PrefLoginGlobal.getUser(getActivity());


        if(user ==null)
        {
            swipeContainer.setRefreshing(false);
            showToastMessage("Markets null !");
            return;
        }



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();


        Call<ServiceConfigurationEndPoint> call = retrofit.create(MarketService.class).getMarketsList(
                PrefLoginGlobal.getAuthorizationHeaders(MyApplication.getAppContext()),
                PrefLocation.getLatitude(MyApplication.getAppContext()), PrefLocation.getLongitude(MyApplication.getAppContext()),
                null,searchQuery,
                null,null,null,
                sortBy,filterByPingStatus,
                limit,offset,
                true,false
        );


        call.enqueue(new Callback<ServiceConfigurationEndPoint>() {
            @Override
            public void onResponse(Call<ServiceConfigurationEndPoint> call, Response<ServiceConfigurationEndPoint> response) {


                if(response.code() == 200 && response.body()!=null) {


                    if (clearDataset) {
                        dataset.clear();
                        clearDataset = false;

                        item_count = response.body().getItemCount();



                        dataset.add(new FilterMarkets());


                        if(item_count>0)
                        {
                            dataset.add(new HeaderTitle("Markets List : " + item_count + " ( Total )"));
                        }
                    }



                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
                    }



                    if(offset + limit >= item_count)
                    {
                        listAdapter.setLoadMore(false);
                    }
                    else
                    {
                        listAdapter.setLoadMore(true);
                    }
                }
                else
                {
                    showToastMessage("Failed Code : " + response.code());
                }




//                showToastMessage("Item Count : " + item_count);

                if(item_count==0)
                {
                    dataset.add(getEmptyScreen());
                }



                listAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ServiceConfigurationEndPoint> call, Throwable t) {


                swipeContainer.setRefreshing(false);


                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                listAdapter.notifyDataSetChanged();
            }
        });



    }







    private EmptyScreenDataFullScreen getEmptyScreen()
    {
        return EmptyScreenDataFullScreen.emptyUsersList();
    }




    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }




    @Override
    public void filtersUpdated() {
        makeRefreshNetworkCall();
    }



    private String searchQuery = null;
    @Override
    public void search(String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }



    @Override
    public void listItemClick(Market market, int position) {

        Intent intent = new Intent(getActivity(), EditMarket.class);
        String jsonString = UtilityFunctions.provideGson().toJson(market);
        intent.putExtra("market_profile",jsonString);
        intent.putExtra(EditMarketFragment.EDIT_MODE_INTENT_KEY,EditMarketFragment.MODE_UPDATE_BY_SUPER_ADMIN);

        startActivity(intent);
    }
}
