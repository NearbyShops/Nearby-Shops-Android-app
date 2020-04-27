package org.nearbyshops.enduserappnew.aSDSAdminModule.MarketsList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarketsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        NotifySort, NotifySearch {





    public static final String TAG_SLIDING_LAYER = "sliding_layer_orders";
    public static final String IS_FILTER_BY_SHOP = "IS_FILTER_BY_SHOP";


    private RecyclerView recyclerView;
    private Adapter adapter;

    public List<Object> dataset = new ArrayList<>();
    private SwipeRefreshLayout swipeContainer;





    private ViewModelMarkets viewModelMarkets;



    @BindView(R.id.service_name) TextView serviceName;



    public MarketsListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_orders_new, container, false);
        ButterKnife.bind(this,rootView);


        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeContainer = rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        toolbar.setTitle(getString(R.string.app_name));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);




        viewModelMarkets = new ViewModelMarkets(MyApplication.application);




        if(PrefGeneral.getMultiMarketMode(getActivity()) && PrefServiceConfig.getServiceName(getActivity())!=null)
        {
            serviceName.setVisibility(View.VISIBLE);
            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
        }
        else
        {
            serviceName.setVisibility(View.GONE);
        }


        setupRecyclerView();
        setupSwipeContainer();


        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


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

        adapter = new Adapter(getActivity(),this);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        viewModelMarkets.getArticleLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<Object>>() {
            @Override
            public void onChanged(PagedList<Object> objects) {

                adapter.submitList(objects);


                swipeContainer.setRefreshing(false);
            }
        });

    }





    @Override
    public void onRefresh() {


        viewModelMarkets.refresh();
    }







    private void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                if(!isVisible())
                {
                    return;
                }



                onRefresh();
            }
        });
    }







    public static MarketsListFragment newInstance(boolean filterOrdersByUser, boolean filterOrdersByShop, boolean filterOrdersByDeliveryID) {
        MarketsListFragment fragment = new MarketsListFragment();
        Bundle args = new Bundle();

        args.putBoolean("filter_orders_by_user",filterOrdersByUser);
        args.putBoolean("filter_orders_by_shop",filterOrdersByShop);
        args.putBoolean("filter_orders_by_delivery",filterOrdersByDeliveryID);

        fragment.setArguments(args);
        return fragment;
    }









    @OnClick(R.id.button_try_again)
    void tryAgainClick()
    {
        makeRefreshNetworkCall();
    }






    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

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




}
