package org.nearbyshops.enduserappnew.Lists.Markets;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.Interfaces.MarketSelected;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.ViewHolderMarket;
import org.nearbyshops.enduserappnew.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.LocationUpdated;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.DetailScreens.DetailMarket.MarketDetail;
import org.nearbyshops.enduserappnew.DetailScreens.DetailMarket.MarketDetailFragment;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSignIn;
import org.nearbyshops.enduserappnew.ViewModels.MarketViewModel;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelUser;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;




public class MarketsFragmentNew extends Fragment implements
        ViewHolderMarket.ListItemClick, SwipeRefreshLayout.OnRefreshListener,
        NotifySort, NotifySearch, LocationUpdated, ViewHolderSignIn.VHSignIn,
        ViewHolderEmptyScreenListItem.VHEmptyScreen , ViewHolderSetLocationManually.ListItemClick {



    @Inject Gson gson;


    private AdapterMarkets adapter;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    public List<Object> dataset = new ArrayList<>();



//    boolean initialized = false;


    boolean isDestroyed;


    @BindView(R.id.service_name) TextView serviceName;


    private MarketViewModel viewModel;
    private ViewModelUser viewModelUser;





    public MarketsFragmentNew() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }


    public static MarketsFragmentNew newInstance() {
        MarketsFragmentNew fragment = new MarketsFragmentNew();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


    //        setRetainInstance(true);
            View rootView = inflater.inflate(R.layout.fragment_markets, container, false);
            ButterKnife.bind(this,rootView);



            if(savedInstanceState==null)
            {
                makeRefreshNetworkCall();
            }


            setupRecyclerView();
            setupSwipeContainer();




//            viewModel  = ViewModelProviders.of(this).get(MarketViewModel.class);

            viewModel = new MarketViewModel(MyApplication.application);
            viewModelUser = new ViewModelUser(MyApplication.application);




            viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Object>>() {
                    @Override
                    public void onChanged(@Nullable List<Object> objects) {

                        dataset.clear();

                        if(objects!=null)
                        {
                            dataset.addAll(objects);
                        }


                        adapter.setLoadMore(false);
                        adapter.notifyDataSetChanged();


                        swipeContainer.setRefreshing(false);
                    }
                });





            viewModel.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {

                    showToastMessage(s);

                    swipeContainer.setRefreshing(false);
                }
            });





                viewModelUser.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {

                        if(integer == ViewModelUser.EVENT_profile_fetched)
                        {
                            adapter.notifyDataSetChanged();
                        }

                    }
                });




                viewModelUser.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {

                            showToastMessage(s);
                        }
                });




        if(PrefServiceConfig.getServiceName(getActivity())!=null) {

            serviceName.setVisibility(View.VISIBLE);
            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
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

        adapter = new AdapterMarkets(dataset,this);
        recyclerView.setAdapter(adapter);

        adapter.setLoadMore(false);



        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }



//    int previous_position = -1;






    @Override
    public void onRefresh() {

        viewModel.loadData(true);
//        showToastMessage("OnRefresh()");

        viewModelUser.getUserProfile();


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





//
//    @Override
//    public void onResume() {
//        super.onResume();
//        isDestroyed=false;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        isDestroyed=true;
//    }




    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }








    // Refresh the Confirmed PlaceholderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
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
    public void listItemClick(ServiceConfigurationGlobal configurationGlobal, int position) {


        //        showToastMessage("List item click !");
        //        showToastMessage(json);


        String json = UtilityFunctions.provideGson().toJson(configurationGlobal);
        Intent intent = new Intent(getActivity(), MarketDetail.class);
        intent.putExtra(MarketDetailFragment.TAG_JSON_STRING,json);
        startActivity(intent);

    }





    @Override
    public void selectMarketSuccessful(ServiceConfigurationGlobal configurationGlobal, int position) {


        if(getActivity() instanceof MarketSelected)
        {
            ((MarketSelected) getActivity()).marketSelected();
        }

    }





    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }




    @Override
    public void permissionGranted() {

    }



    @Override
    public void locationUpdated() {


        makeRefreshNetworkCall();

//        showToastMessage("Markets : Location updated !");
    }






    @OnClick(R.id.fab)
    void fabClick()
    {
        showDialogSubmitURL();
    }





    private void showDialogSubmitURL()
    {
        FragmentManager fm = getChildFragmentManager();
        SubmitURLDialog submitURLDialog = new SubmitURLDialog();
        submitURLDialog.show(fm,"serviceUrl");
    }


    @Override
    public void signInClick() {

        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent,123);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123)
        {
            makeRefreshNetworkCall();
        }
        else if(resultCode==405)
        {
            makeRefreshNetworkCall();
        }
        else if(requestCode==3 && resultCode==6)
        {
            if(data!=null)
            {
                PrefLocation.saveLatLonCurrent(data.getDoubleExtra("lat_dest",0.0),data.getDoubleExtra("lon_dest",0.0),
                        getActivity());

                PrefLocation.setLocationSetByUser(true,getActivity());
                makeRefreshNetworkCall();
            }

        }
        else if(requestCode==890)
        {
            makeRefreshNetworkCall();
        }
    }





    @Override
    public void buttonClick(String url) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);


    }



    @Override
    public void changeLocationClick() {
//        Intent intent = new Intent(getActivity(), PickLocation.class);
        Intent intent = new Intent(getActivity(), GooglePlacePicker.class);
        intent.putExtra("lat_dest", PrefLocation.getLatitude(getActivity()));
        intent.putExtra("lon_dest",PrefLocation.getLongitude(getActivity()));
        startActivityForResult(intent,3);
    }
}

