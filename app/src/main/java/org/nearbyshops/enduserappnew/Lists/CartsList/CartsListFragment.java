package org.nearbyshops.enduserappnew.Lists.CartsList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CartsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    @Inject
    CartStatsService cartStatsService;

    private RecyclerView recyclerView;
    private Adapter adapter;
    private SwipeRefreshLayout swipeContainer;



    private List<Object> dataset = new ArrayList<>();
    private boolean isDestroyed = false;


    @BindView(R.id.empty_screen) LinearLayout emptyScreen;
    @BindView(R.id.service_name) TextView serviceName;


    public CartsListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_carts_list, container, false);
        ButterKnife.bind(this, rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        toolbar.setTitle("Carts");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//



        // findViewByID's
        swipeContainer = rootView.findViewById(R.id.swipeContainer);
        recyclerView = rootView.findViewById(R.id.recyclerView);



        serviceName.setVisibility(View.VISIBLE);
        serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));


//        if(PrefGeneral.getMultiMarketMode(getActivity()) && PrefServiceConfig.getServiceName(getActivity())!=null)
//        {
//            serviceName.setVisibility(View.VISIBLE);
//            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
//        }
//        else
//        {
//            serviceName.setVisibility(View.VISIBLE);
//        }



        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }



        setupSwipeContainer();
        setupRecyclerView();

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
        adapter = new Adapter(dataset,getActivity(),this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }





    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onRefresh() {

        makeNetworkCall();
    }





    private void makeNetworkCall()
    {


        User endUser = PrefLogin.getUser(getActivity());

        if(endUser==null)
        {
            showLoginScreen();

            swipeContainer.setRefreshing(false);
            return;
        }



        Call<List<CartStats>> call = cartStatsService.getCart(
                endUser.getUserID(),null,null,true,
                PrefLocation.getLatitude(getActivity()),
                PrefLocation.getLongitude(getActivity())
        );


        emptyScreen.setVisibility(View.GONE);


        call.enqueue(new Callback<List<CartStats>>() {
            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.body()!=null)
                {
                    dataset.clear();
                    dataset.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if(response.body().size()==0)
                    {
                        emptyScreen.setVisibility(View.VISIBLE);
                    }


                }
                else
                {
                    dataset.clear();
                    adapter.notifyDataSetChanged();

                    emptyScreen.setVisibility(View.VISIBLE);
                }




                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                emptyScreen.setVisibility(View.VISIBLE);

                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);
            }
        });

    }







    @OnClick(R.id.button_try_again)
    void tryAgainClick()
    {
        makeRefreshNetworkCall();
    }








    private void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                makeNetworkCall();
            }
        });
    }








    @Override
    public void onResume() {
        super.onResume();
        isDestroyed=false;

        makeRefreshNetworkCall();
    }





    @Override
    public void onStop() {
        super.onStop();
        isDestroyed=true;
    }



    private void showLoginScreen()
    {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }


}
