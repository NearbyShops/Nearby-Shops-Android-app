package org.nearbyshops.enduserappnew.Lists.ShopsList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.android.gms.location.LocationServices;
import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.Lists.ItemsInShopByCategory.ItemsInShopByCat;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Services.LocationUpdateService;
import org.nearbyshops.enduserappnew.Services.UpdateServiceConfiguration;
import org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.ShowFragment;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.SlidingLayerSort.PreferencesSort.PrefSortShopsByCategory;
import org.nearbyshops.enduserappnew.SlidingLayerSort.SlidingLayerSortShops;
import org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerMapbox.PickLocation;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopSmall;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.CreateShopData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderCreateShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SetLocationManually;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;




//import icepick.State;

/**
 * Created by sumeet on 25/5/16.
 */
public class FragmentShopsList extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, NotifySort, NotifySearch ,
        ViewHolderShopSmall.ListItemClick , ViewHolderSetLocationManually.ListItemClick,
        ViewHolderEmptyScreenListItem.ListItemClick , ViewHolderCreateShop.ListItemClick {


    private static final String TAG_SLIDING = "tag_sliding_layer_sort_shops";
    private ArrayList<Object> dataset = new ArrayList<>();

        boolean isSaved;


        @Inject
        ShopService shopService;


        private RecyclerView recyclerView;
        private Adapter adapter;
//        GridLayoutManager layoutManager;

        SwipeRefreshLayout swipeContainer;

        final private int limit = 10;
        private int offset = 0;
        private int item_count = 0;

        private boolean switchMade = false;
        private boolean isDestroyed;








//    @BindView(R.id.icon_list) ImageView mapIcon;
    @BindView(R.id.shop_count_indicator) TextView shopCountIndicator;
    @BindView(R.id.slidingLayer) SlidingLayer slidingLayer;



    @BindView(R.id.empty_screen) LinearLayout emptyScreen;
    @BindView(R.id.progress_bar_fetching_location) LinearLayout progressBarFetchingLocation;

    @BindView(R.id.empty_screen_message) TextView emptyScreenMessage;


    @BindView(R.id.service_name) TextView serviceName;


    @BindView(R.id.button_try_again) TextView buttonTryAgain;





    public FragmentShopsList() {
            // inject dependencies through dagger
            DaggerComponentBuilder.getInstance()
                    .getNetComponent()
                    .Inject(this);

            Log.d("applog","Shop Fragment Constructor");

        }









        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentShopsList newInstance(boolean switchMade) {

            FragmentShopsList fragment = new FragmentShopsList();
            Bundle args = new Bundle();
//            args.putParcelable("itemCat",itemCategory);
            args.putBoolean("switch",switchMade);
            fragment.setArguments(args);

            return fragment;
        }






//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
//
//






    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            setRetainInstance(true);
            View rootView = inflater.inflate(R.layout.fragment_shops_new, container, false);
            ButterKnife.bind(this,rootView);


            recyclerView = rootView.findViewById(R.id.recyclerView);
            swipeContainer = rootView.findViewById(R.id.swipeContainer);
            switchMade = getArguments().getBoolean("switch");


            Toolbar toolbar = rootView.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


//            toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//            toolbar.setTitle(getString(R.string.app_name));




    //

            serviceName.setVisibility(View.VISIBLE);
            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));



            if(PrefGeneral.getMultiMarketMode(getActivity()) && PrefServiceConfig.getServiceName(getActivity())!=null)
            {

                buttonTryAgain.setText("Change Market");
                emptyScreenMessage.setText("" +
                        "Uh .. oh .. no shops available at your location .. change your market ... and explore other markets !");
            }
            else
            {

                emptyScreenMessage.setText("Uh .. oh .. no shops available at your location .. change your location ... and try again");
                buttonTryAgain.setText("Try Again");
            }




            if(savedInstanceState==null && !switchMade)
            {
                // ensure that there is no swipe to right on first fetch
//                    isbackPressed = true;

                makeRefreshNetworkCall();
                isSaved = true;
            }



            setupRecyclerView();
            setupSwipeContainer();
//            notifyDataset();


            setupSlidingLayer();






            if(!PrefLocation.isLocationSetByUser(getActivity()))
            {
                getActivity().startService(new Intent(getActivity(), LocationUpdateService.class));
            }


            setupLocalBroadcastManager();


            return rootView;
        }




        private void setupLocalBroadcastManager()
        {


            IntentFilter filter = new IntentFilter();

            filter.addAction(UpdateServiceConfiguration.INTENT_ACTION_MARKET_CONFIG_FETCHED);
            filter.addAction(LocationUpdateService.INTENT_ACTION_LOCATION_UPDATED);

            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {


                    if(getActivity()!=null)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                serviceName.setVisibility(View.VISIBLE);
                                serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));

                                makeRefreshNetworkCall();

                            }
                        });
                    }


                }
            },filter);
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






            @OnClick({R.id.icon_sort, R.id.text_sort})
            void sortClick()
            {
                slidingLayer.openLayer(true);
        //        showToastMessage("Sort Clicked");
            }






    private void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
//            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(30);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);



            if(getChildFragmentManager().findFragmentByTag(TAG_SLIDING)==null)
            {
//                System.out.println("Item Cat Simple : New Sliding Layer Loaded !");
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.slidinglayerfragment,new SlidingLayerSortShops(),TAG_SLIDING)
                        .commit();
            }
        }

    }







    private void setupRecyclerView()
        {
            if(recyclerView == null)
            {
                return;
            }





            adapter = new Adapter(dataset,getActivity(),this);

            recyclerView.setAdapter(adapter);

//            layoutManager = new GridLayoutManager(getActivity(),1);


            final LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(linearlayoutManager);

//            recyclerView.addItemDecoration(new EqualSpaceItemDecoration(1));



//            recyclerView.addItemDecoration(
//                    new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL)
//            );


//            recyclerView.addItemDecoration(
//                    new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL_LIST)
//            );

//            itemCategoriesList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


//            DisplayMetrics metrics = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//
//            int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));
//
//            if(spanCount==0){
//                spanCount = 1;
//            }
//
//            layoutManager.setSpanCount(spanCount);

//            layoutManager.setSpanCount(metrics.widthPixels/350);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if(linearlayoutManager.findLastVisibleItemPosition()==dataset.size())
                    {
                        // trigger fetch next page

//                        if(dataset.size()== previous_position)
//                        {
//                            return;
//                        }


                        if(offset + limit > linearlayoutManager.findLastVisibleItemPosition())
                        {
                            return;
                        }


                        if((offset+limit)<=item_count)
                        {
                            offset = offset + limit;
                            makeNetworkCall(false,false);
                        }

//                        previous_position = dataset.size();

                    }
                }
            });
        }




//    int previous_position = -1;


    public int getItemCount()
    {
        return item_count;
    }





        private void makeRefreshNetworkCall() {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                    try {


                        onRefresh();

                    } catch (IllegalArgumentException ex)
                    {
                        ex.printStackTrace();

                    }
                }
            });

        }



        @Override
        public void onRefresh() {


            makeNetworkCall(true,true);
        }







        private void makeNetworkCall(final boolean clearDataset, boolean resetOffset)
        {

            if(resetOffset)
            {
                offset = 0;
            }



//            (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY)
//            (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
//                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY),



            String current_sort = "";
            current_sort = PrefSortShopsByCategory.getSort(getActivity()) + " " + PrefSortShopsByCategory.getAscending(getActivity());

            Call<ShopEndPoint> callEndpoint = shopService.getShops(
                    null,
                    null,
                    PrefLocation.getLatitude(getActivity()),
                    PrefLocation.getLongitude(getActivity()),
                    null, null, null,
                    searchQuery,current_sort,limit,offset,false
            );





            emptyScreen.setVisibility(View.GONE);




//            System.out.println("Lat : " + PrefLocation.getLatitude(getActivity())
//                                + "\nLon : " + PrefLocation.getLongitude(getActivity()));





            callEndpoint.enqueue(new Callback<ShopEndPoint>() {
                @Override
                public void onResponse(Call<ShopEndPoint> call, Response<ShopEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }

    //                dataset.clear();

                    if(response.body()!=null)
                    {

                        if(clearDataset)
                        {
                            dataset.clear();
//                            dataset.add(0, Highlights.getHighlightsCabRental());
                        }

                        dataset.addAll(response.body().getResults());


                        if(response.body().getItemCount()!=null)
                        {
                            item_count = response.body().getItemCount();
                        }



                        boolean showCreateShop = MyApplication.getAppContext().getResources().getBoolean(R.bool.show_create_shop);



                        if(response.body().getResults().size()==0)
                        {
                            dataset.add(0, new SetLocationManually());
//                            emptyScreen.setVisibility(View.VISIBLE);



                            User user = PrefLogin.getUser(getActivity());

                            if(showCreateShop)
                            {
                                if(user!=null)
                                {
                                    if(user.getRole()==User.ROLE_END_USER_CODE)
                                    {
                                        dataset.add(new CreateShopData());
                                    }
                                }
                                else
                                {
                                    dataset.add(new CreateShopData());
                                }
                            }




                            if(PrefGeneral.getMultiMarketMode(getActivity()))
                            {

                                dataset.add(EmptyScreenDataListItem.getEmptyScreenShopsListMultiMarket());
                            }
                            else
                            {


                                dataset.add(EmptyScreenDataListItem.getEmptyScreenShopsListSingleMarket());
                            }




                        }
                        else
                        {
                            if(dataset.size()>=1)
                            {
                                dataset.add(1, new SetLocationManually());



                                if(showCreateShop)
                                {
                                    User user = PrefLogin.getUser(getActivity());

                                    if(user!=null)
                                    {
                                        if(user.getRole()==User.ROLE_END_USER_CODE)
                                        {
                                            dataset.add(2, new CreateShopData());
                                        }
                                    }
                                    else
                                    {
                                        dataset.add(2, new CreateShopData());
                                    }
                                }


                            }
                        }


                        shopCountIndicator.setText(dataset.size() + " out of " + item_count + " Shops");

                    }




                    if(offset + limit >= item_count)
                    {
                        adapter.setLoadMore(false);
                    }
                    else
                    {
                        adapter.setLoadMore(true);
                    }


                    adapter.notifyDataSetChanged();

                    swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(Call<ShopEndPoint> call, Throwable t) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    dataset.clear();

                    dataset.add(EmptyScreenDataFullScreen.getOffline());



//                    emptyScreen.setVisibility(View.VISIBLE);


                    adapter.notifyDataSetChanged();

//                    showToastMessage(getString(R.string.network_request_failed));
                    swipeContainer.setRefreshing(false);
                }
            });

        }






//        @OnClick(R.id.button_try_again)
        private void tryAgainClick()
        {

            if(PrefGeneral.getMultiMarketMode(getActivity()))
            {

                if(getActivity() instanceof ShowFragment)
                {
                    ((ShowFragment) getActivity()).showProfileFragment(false);
                }
            }
            else
            {

                makeRefreshNetworkCall();
            }
        }




    @Override
    public void buttonClick(String url) {
            tryAgainClick();
    }







        private void showToastMessage(String message)
        {
            Toast.makeText(MyApplication.getAppContext(),message,Toast.LENGTH_SHORT).show();
        }








    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
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



    // location code



    @Override
    public void onResume() {
        super.onResume();

        isDestroyed = false;
    }





    @Override
    public void listItemClick(Shop shop, int position) {

        PrefShopHome.saveShop(shop,getActivity());
        Intent intent = new Intent(getActivity(), ItemsInShopByCat.class);
        startActivity(intent);
    }




    @Override
    public void editClick(Shop shop, int position) {

    }




    @Override
    public void changeLocationClick() {

        Intent intent = null;


        if(getResources().getBoolean(R.bool.use_google_maps))
        {
            intent = new Intent(getActivity(), GooglePlacePicker.class);
        }
        else
        {
            intent = new Intent(getActivity(), PickLocation.class);
        }


        intent.putExtra("lat_dest",PrefLocation.getLatitude(getActivity()));
        intent.putExtra("lon_dest",PrefLocation.getLongitude(getActivity()));
        startActivityForResult(intent,3);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode==3 && resultCode==6)
        {
            if(data!=null)
            {
            }



            PrefLocation.saveLatLonCurrent(data.getDoubleExtra("lat_dest",0.0),data.getDoubleExtra("lon_dest",0.0),
                    getActivity());

            PrefLocation.setLocationSetByUser(true,getActivity());

            makeRefreshNetworkCall();

        }
        else if (requestCode==57121)
        {
            makeRefreshNetworkCall();
        }
    }




    @Override
    public void listItemClick() {

    }


    //    @Override
//    public void onPause() {
//        super.onPause();
//
//        // Unregister the listener when the application is paused
//
////        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(testReceiver);
//
//    }


//    @Override
//    public void onStop() {
//        super.onStop();
//
////        getActivity().stopService(new Intent(getActivity(), LocationUpdateService.class));
//
//    }


//
//    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
//
//            if (resultCode == RESULT_OK) {
//
////                String resultValue = intent.getStringExtra("resultValue");
////                Toast.makeText(getActivity(), resultValue, Toast.LENGTH_SHORT).show();
//
//                showToastMessage("Broadcast Received !");
//            }
//        }
//    };



}
