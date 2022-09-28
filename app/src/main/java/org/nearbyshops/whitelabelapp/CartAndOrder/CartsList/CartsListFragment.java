package org.nearbyshops.whitelabelapp.CartAndOrder.CartsList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import butterknife.ButterKnife;

import org.nearbyshops.whitelabelapp.API.CartStatsService;
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.CartStats;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderFullScreenProgressBar;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CartsListFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener{


//    @Inject
//    CartStatsService cartStatsService;

    @Inject
    Gson gson;


    private RecyclerView recyclerView;
    private Adapter adapter;
    private SwipeRefreshLayout swipeContainer;



    private List<Object> dataset = new ArrayList<>();
    private boolean isDestroyed = false;


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


        // findViewByID's
        swipeContainer = rootView.findViewById(R.id.swipeContainer);
        recyclerView = rootView.findViewById(R.id.recyclerView);


        setupSwipeContainer();
        setupRecyclerView();
        setToolbarTitle();


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }

        return rootView;
    }


    void setToolbarTitle()
    {
        if(getActivity() instanceof SetToolbarText)
        {
            ((SetToolbarText) getActivity()).setToolbar(true,"Cart",false,null);
        }
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
        UtilityFunctions.showToastMessage(getActivity(),message);
    }



    @Override
    public void onRefresh() {


        dataset.clear();
        dataset.add(new ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData());
        adapter.notifyDataSetChanged();

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




        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServerURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();




        Call<List<CartStats>> call = retrofit.create(CartStatsService.class).getCartStatsList(
                endUser.getUserID(),null,null,true,
                PrefLocation.getLatitudeSelected(getActivity()),
                PrefLocation.getLongitudeSelected(getActivity())
        );





        call.enqueue(new Callback<List<CartStats>>() {
            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {




                dataset.clear();


                if(response.body()!=null)
                {


                    dataset.addAll(response.body());

                    if(response.body().size()==0)
                    {
//                        emptyScreen.setVisibility(View.VISIBLE);

                        dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.cartEmpty());
                    }


                }
                else
                {

//                     showToastMessage("Failed Code : " + String.valueOf(response.code()));
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.cartEmpty());

                }



                adapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {



//                emptyScreen.setVisibility(View.VISIBLE);
//                showToastMessage("Network Request failed !");


                dataset.clear();
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();


                swipeContainer.setRefreshing(false);
            }
        });

    }




    private void makeRefreshNetworkCall()
    {

        onRefresh();
//        swipeContainer.post(new Runnable() {
//            @Override
//            public void run() {
//
//                swipeContainer.setRefreshing(true);
//
//            }
//        });
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
//        isDestroyed=true;
    }



    private void showLoginScreen()
    {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3262 && resultCode ==3121)
        {
            makeRefreshNetworkCall();
        }
    }


}
