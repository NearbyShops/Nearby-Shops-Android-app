package org.nearbyshops.enduserappnew.Lists.ShopsList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.EditDataScreens.EditBannerImage.EditBannerImage;
import org.nearbyshops.enduserappnew.EditDataScreens.EditBannerImage.EditBannerImageFragment;
import org.nearbyshops.enduserappnew.Interfaces.MarketSelected;
import org.nearbyshops.enduserappnew.Lists.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduserappnew.Lists.ItemsInShopByCategory.ItemsInShopByCat;
import org.nearbyshops.enduserappnew.multimarketfiles.Markets.MarketsList;
import org.nearbyshops.enduserappnew.multimarketfiles.Markets.ViewModelMarkets;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Services.LocationService;
import org.nearbyshops.enduserappnew.Services.UpdateServiceConfiguration;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.UtilityScreens.BannerSlider.Model.BannerImageList;
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
import org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerMapbox.PickLocation;
import org.nearbyshops.enduserappnew.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.Models.FilterShopsData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.ViewHolderFilterShops;
import org.nearbyshops.enduserappnew.multimarketfiles.ViewHolderMarket.ViewHolderMarket;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopSmall;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.CreateShopData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.ShopSuggestionsData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderAddItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderCreateShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SetLocationManually;
import org.nearbyshops.enduserappnew.multimarketfiles.SwitchMarketData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;
import org.nearbyshops.enduserappnew.multimarketfiles.ViewHolderSwitchMarket;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;





public class FragmentShopsList extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, NotifySort, NotifySearch ,
        ViewHolderShopSmall.ListItemClick , ViewHolderSetLocationManually.ListItemClick,
        AdapterFilterItemCategory.ListItemClick,
        ViewHolderEmptyScreenListItem.ListItemClick , ViewHolderCreateShop.ListItemClick ,
        ViewHolderMarket.ListItemClick , ViewHolderFilterShops.ListItemClick, ViewHolderSwitchMarket.ListItemClick,
        ViewHolderAddItem.ListItemClick {


//    private static final String TAG_SLIDING = "tag_sliding_layer_sort_shops";
    private ArrayList<Object> dataset = new ArrayList<>();

        boolean isSaved;



        @Inject
        Gson gson;


//        @Inject
//        ShopService shopService;


        private RecyclerView recyclerView;
        private Adapter adapter;

        SwipeRefreshLayout swipeContainer;

        final private int limit = 10;
        private int offset = 0;
        private int item_count = 0;

        private boolean switchMade = false;
        private boolean isDestroyed;






    private ViewModelMarkets viewModel;






    //    @BindView(R.id.icon_list) ImageView mapIcon;
//    @BindView(R.id.shop_count_indicator) TextView shopCountIndicator;
//    @BindView(R.id.slidingLayer) SlidingLayer slidingLayer;

//    @BindView(R.id.empty_screen) LinearLayout emptyScreen;
//    @BindView(R.id.progress_bar_fetching_location) LinearLayout progressBarFetchingLocation;

//    @BindView(R.id.empty_screen_message) TextView emptyScreenMessage;
    //    @BindView(R.id.button_try_again) TextView buttonTryAgain;


    @BindView(R.id.app_name) TextView toolbarHeader;
    @BindView(R.id.service_name) TextView serviceName;








        public FragmentShopsList() {
            // inject dependencies through dagger
            DaggerComponentBuilder.getInstance()
                    .getNetComponent()
                    .Inject(this);

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

            if(getArguments()!=null)
            {
                switchMade = getArguments().getBoolean("switch",false);
            }



            Toolbar toolbar = rootView.findViewById(R.id.toolbar);

            if(getActivity()!=null)
            {
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            }



            setupViewModel();



            boolean filterShopsByCategory = getActivity().getIntent().getBooleanExtra("filter_shops_by_category",false);
            String categoryName = getActivity().getIntent().getStringExtra("item_category_name");



            if(filterShopsByCategory)
            {
                serviceName.setVisibility(View.GONE);
                toolbarHeader.setText(categoryName + " Shops");
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


            setMarketName();


//            setupSlidingLayer();






//            if(!PrefLocation.isLocationSetByUser(getActivity()))
//            {
//                getActivity().startService(new Intent(getActivity(), LocationService.class));
//            }


            setupLocalBroadcastManager();


            return rootView;
        }






        void setMarketName()
        {
            if(PrefServiceConfig.getServiceName(getActivity())!=null)
            {
                serviceName.setVisibility(View.VISIBLE);
                serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
            }
            else
            {
                serviceName.setVisibility(View.GONE);
            }

        }





        private void setupLocalBroadcastManager()
        {


            IntentFilter filter = new IntentFilter();

            filter.addAction(UpdateServiceConfiguration.INTENT_ACTION_MARKET_CONFIG_FETCHED);
            filter.addAction(LocationService.INTENT_ACTION_LOCATION_UPDATED);

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

                                if(intent.getAction().equals(LocationService.INTENT_ACTION_LOCATION_UPDATED))
                                {
                                    countDownTimer.cancel();
                                    countDownTimer.start();
                                }
                            }
                        });
                    }




                }
            },filter);
        }







        


        private CountDownTimer countDownTimer = new CountDownTimer(1000, 500) {

            public void onTick(long millisUntilFinished) {

    //            logMessage("Timer onTick()");
            }


            public void onFinish() {

                makeRefreshNetworkCall();
            }
        };






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






//            @OnClick({R.id.icon_sort, R.id.text_sort})
//            void sortClick()
//            {
//                slidingLayer.openLayer(true);
//        //        showToastMessage("Sort Clicked");
//            }





//
//    private void setupSlidingLayer()
//    {
//
//        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
//        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);
//
//        if(slidingLayer!=null)
//        {
//            slidingLayer.setChangeStateOnTap(true);
//            slidingLayer.setSlidingEnabled(true);
////            slidingLayer.setPreviewOffsetDistance(15);
//            slidingLayer.setOffsetDistance(30);
//            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);
//
//            DisplayMetrics metrics = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);
//
//            //slidingContents.setLayoutParams(layoutParams);
//
//            //slidingContents.setMinimumWidth(metrics.widthPixels-50);
//
//
//
//            if(getChildFragmentManager().findFragmentByTag(TAG_SLIDING)==null)
//            {
////                System.out.println("Item Cat Simple : New Sliding Layer Loaded !");
//                getChildFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.slidinglayerfragment,new SlidingLayerSortShops(),TAG_SLIDING)
//                        .commit();
//            }
//        }
//
//    }
//








    private void setupRecyclerView()
    {

        if(recyclerView == null)
        {
            return;
        }



        adapter = new Adapter(dataset,getActivity(),this);
        recyclerView.setAdapter(adapter);


        final LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearlayoutManager);



//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearlayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(linearlayoutManager.findLastVisibleItemPosition()==dataset.size())
                {
                    // trigger fetch next page


                    if(offset + limit > linearlayoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall();
                    }
                }
            }
        });


    }






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

            clearDataset=true;

            makeNetworkCall();

            if(PrefGeneral.isMultiMarketEnabled(getActivity()))
            {
                // multi-market mode enabled
//                viewModel.getNearbyMarketsHorizontal();
            }

        }







        Integer itemCategoryID;
        int layoutPositionSelected = 0;



        boolean clearDataset = false;



        private void makeNetworkCall()
        {

            if(clearDataset)
            {
                offset = 0;
            }




            boolean filterShopsByCategory = getActivity().getIntent().getBooleanExtra("filter_shops_by_category",false);

            if(filterShopsByCategory)
            {
                itemCategoryID = getActivity().getIntent().getIntExtra("category_id",123);
            }



            String current_sort = "";
            current_sort = PrefSortShopsByCategory.getSort(getActivity()) + " " + PrefSortShopsByCategory.getAscending(getActivity());


            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();




            Call<ShopEndPoint> callEndpoint;


            callEndpoint = retrofit.create(ShopService.class).filterShopsByItemCategory(
                    itemCategoryID,
                    PrefLocation.getLatitude(getActivity()), PrefLocation.getLongitude(getActivity()),
                    null,
                    clearDataset,
                    clearDataset && !filterShopsByCategory,
                    searchQuery,
                    ViewHolderFilterShops.getSortString(getActivity()),
                    limit,offset,
                    clearDataset,false
            );


//            Call<ShopEndPoint> callEndpoint = shopService.getShops(
//                    null,
//                    PrefLocation.getLatitude(getActivity()),
//                    PrefLocation.getLongitude(getActivity()),
//                    null, null, null,
//                    searchQuery,current_sort,
//                    limit,offset,
//                    clearDataset,false
//            );




//            emptyScreen.setVisibility(View.GONE);



            callEndpoint.enqueue(new Callback<ShopEndPoint>() {
                @Override
                public void onResponse(Call<ShopEndPoint> call, Response<ShopEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }


                    if(getActivity()==null)
                    {
                        return;
                    }


                    if(response.code()==200)
                    {

                        if(clearDataset)
                        {
                            dataset.clear();


                            if(response.body().getItemCount()!=null)
                            {
                                item_count = response.body().getItemCount();
                            }


                            clearDataset=false;


                            boolean showCreateShop = MyApplication.getAppContext().getResources().getBoolean(R.bool.show_create_shop);



                            dataset.addAll(response.body().getResults());


                            int i = 0;







                            if(!filterShopsByCategory)
                            {



                                if(PrefGeneral.isMultiMarketEnabled(getContext()))
                                {
                                    dataset.add(i++, new SwitchMarketData());
                                }
                                else
                                {
                                    dataset.add(i++, new SetLocationManually());
                                }



//                                if(Highlights.getHighlightsFrontScreen(getActivity())!=null && getResources().getBoolean(R.bool.slider_front_enabled))
//                                {
//                                    dataset.add(i++,Highlights.getHighlightsFrontScreen(getActivity()));
//                                }

                                if(response.body().getBannerImages()!=null && getResources().getBoolean(R.bool.slider_front_enabled))
                                {
                                    BannerImageList bannerImageList = new BannerImageList();
                                    bannerImageList.setBannerImageList(response.body().getBannerImages());
                                    dataset.add(i++,bannerImageList);
                                }




                            }






                            if(PrefGeneral.isMultiMarketEnabled(getContext()))
                            {
//                                    i++;
//                                dataset.add(i++,new MarketsListData());
                            }




                            if(item_count==0)
                            {


                                User user = PrefLogin.getUser(getActivity());


                                    if(user!=null)
                                    {
                                        if(user.getRole()==User.ROLE_END_USER_CODE)
                                        {
                                            if(showCreateShop) {
                                                dataset.add(new CreateShopData());
                                            }
                                        }
                                        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
                                        {
                                            dataset.add(new ShopSuggestionsData());
                                        }
                                    }
                                    else
                                    {
                                        if(showCreateShop) {
                                            dataset.add(new CreateShopData());
                                        }
                                    }





                                if(PrefGeneral.isMultiMarketEnabled(getActivity()))
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


                                if(!filterShopsByCategory)
                                {
                                    ItemCategoriesList list = new ItemCategoriesList();
                                    list.setItemCategories(response.body().getSubcategories());
                                    list.setSelectedCategoryID(itemCategoryID);
                                    list.setScrollPositionForSelected(layoutPositionSelected);


//                                i++;

                                    if(getResources().getBoolean(R.bool.show_categories_in_shops_screen))
                                    {

                                        dataset.add(i++,list);
                                    }
                                }




                                dataset.add(i++,new FilterShopsData());



                                if(!filterShopsByCategory)
                                {
                                    User user = PrefLogin.getUser(getActivity());

                                    if(user!=null)
                                    {
                                        if(user.getRole()==User.ROLE_END_USER_CODE)
                                        {

                                            if(showCreateShop) {
                                                dataset.add(i++,new CreateShopData());
                                            }

                                        }
                                        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
                                        {
                                            dataset.add(i++,new ShopSuggestionsData());
                                        }
                                    }
                                    else
                                    {

                                        if(showCreateShop) {
                                            dataset.add(i++, new CreateShopData());
                                        }
                                    }


                                }



                            }
                        }
                        else
                        {
                            dataset.addAll(response.body().getResults());
                        }


                    }
                    else
                    {
                        showToastMessage("Failed Code : " + response.code());
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

                    if(getActivity()==null)
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




        private void tryAgainClick()
        {

            if(PrefGeneral.isMultiMarketEnabled(getActivity()))
            {

                if(getActivity() instanceof ShowFragment)
                {
                    ((ShowFragment) getActivity()).showProfileFragment();
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
            UtilityFunctions.showToastMessage(getActivity(),message);
        }










    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        isDestroyed = true;
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
        intent.putExtra("shop_id",shop.getShopID());
        startActivity(intent);
    }




    @Override
    public void editClick(Shop shop, int position) {

    }







    @Override
    public void changeLocationClick() {

        Intent intent = null;


        User user = PrefLogin.getUser(getActivity());

        if(user==null)
        {
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
        else
        {
            intent = new Intent(getActivity(), DeliveryAddressActivity.class);
            startActivityForResult(intent,3);
        }

    }










    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode==3 && resultCode==6)
        {

            PrefLocation.saveLatLon(data.getDoubleExtra("lat_dest",0.0),
                    data.getDoubleExtra("lon_dest",0.0),
                    getActivity()
            );


            PrefLocation.locationSetByUser(true,getActivity());
            makeRefreshNetworkCall();


        }
        else if(requestCode==3 && resultCode==2)
        {

            makeRefreshNetworkCall();

        }
        else if (requestCode==57121)
        {
            makeRefreshNetworkCall();
        }
        else if(requestCode==3262 && resultCode ==3121)
        {
            makeRefreshNetworkCall();
            setMarketName();
        }

    }




    @Override
    public void listItemClick() {

    }






    private void setupViewModel()
    {


        viewModel = new ViewModelMarkets(MyApplication.application);


        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> objects) {

                if(objects!=null)
                {
                    if(dataset.size()>2 )
                    {
                        dataset.addAll(2,objects);
                    }
                    else if(dataset.size()>1)
                    {
                        dataset.addAll(1,objects);
                    }
                    else
                    {
                        dataset.addAll(objects);
                    }
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

    }






    @Override
    public void listItemClick(Market configurationGlobal, int position) {

    }

    @Override
    public void selectMarketSuccessful(Market configurationGlobal, int position) {

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
    public void listItemFilterShopClick(int itemCategoryID, ItemCategory itemCategory, int layoutPosition) {


//        this.layoutPositionSelected = layoutPosition;
//
//        if(this.itemCategoryID!=null && this.itemCategoryID==itemCategoryID)
//        {
//            this.itemCategoryID=null;
//        }
//        else
//        {
//            this.itemCategoryID=itemCategoryID;
//        }
//
//
//        makeRefreshNetworkCall();


        Intent intent = new Intent(getActivity(),ShopsList.class);
        intent.putExtra("filter_shops_by_category",true);
        intent.putExtra("category_id",itemCategoryID);
        intent.putExtra("item_category_name",itemCategory.getCategoryName());

        startActivity(intent);

    }




    @Override
    public void filterShopUpdated() {

        makeRefreshNetworkCall();
    }



    @Override
    public void changeMarketClick() {


        Intent intent = new Intent(getActivity(), MarketsList.class);
        intent.putExtra("is_selection_mode",true);
        startActivityForResult(intent,3262);
    }




    @Override
    public void addItemClick() {

        Intent intent = new Intent(getActivity(), EditBannerImage.class);
        intent.putExtra(EditBannerImageFragment.EDIT_MODE_INTENT_KEY,EditBannerImageFragment.MODE_ADD);
        startActivity(intent);
    }
}
