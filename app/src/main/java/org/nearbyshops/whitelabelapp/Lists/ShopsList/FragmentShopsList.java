package org.nearbyshops.whitelabelapp.Lists.ShopsList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import butterknife.ButterKnife;

import org.jetbrains.annotations.NotNull;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopService;
import org.nearbyshops.whitelabelapp.CartAndOrder.DeliveryAddress.DeliveryAddressSelectionFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditBannerImage.EditBannerImage;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditBannerImage.EditBannerImageFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditDeliveryAddress.EditAddressFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditDeliveryAddress.EditDeliveryAddress;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.AddShop;
import org.nearbyshops.whitelabelapp.Interfaces.RefreshFragment;
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.Lists.ItemsInShopByCategory.ItemsInShopByCat;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Services.LocationService;
import org.nearbyshops.whitelabelapp.Services.GetAppSettings;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.Model.BannerImageList;
import org.nearbyshops.whitelabelapp.PlacePickers.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderDeliveryAddress.ViewHolderDeliveryAddress;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterShopsData;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.ViewHolderFilterShops;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop.ViewHolderShopSmall;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderAddItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderCreateShop;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderFullScreenProgressBar;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;




public class FragmentShopsList extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, NotifySort, NotifySearch,
        RefreshFragment,
        ViewHolderShopSmall.ListItemClick , ViewHolderSetLocationManually.ListItemClick,
        AdapterFilterItemCategory.ListItemClick,
        ViewHolderEmptyScreenListItem.ListItemClick , ViewHolderCreateShop.ListItemClick,
        ViewHolderFilterShops.ListItemClick,
        ViewHolderAddItem.ListItemClick, ViewHolderButton.ListItemClick, ViewHolderDeliveryAddress.ListItemClick {



        private ArrayList<Object> dataset = new ArrayList<>();
        boolean isSaved;

        String marketName;
        int marketID;

        @Inject
        Gson gson;

        @Inject
        ShopService shopService;

        private RecyclerView recyclerView;
        private Adapter adapter;
        private SwipeRefreshLayout swipeContainer;

        final private int limit = 10;
        private int offset = 0;
        private int item_count = 0;

        private boolean switchMade = false;
        private boolean isDestroyed;

        private boolean marketEnabled = true;

        boolean filterShopsByCategory = false;




        public FragmentShopsList() {
            // inject dependencies through dagger
            DaggerComponentBuilder.getInstance()
                    .getNetComponent()
                    .Inject(this);

        }



        public static FragmentShopsList newInstance(boolean switchMade) {

            FragmentShopsList fragment = new FragmentShopsList();
            Bundle args = new Bundle();
            args.putBoolean("switch",switchMade);
            fragment.setArguments(args);

            return fragment;
        }







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

            if(getActivity()!=null)
            {
                marketEnabled = getActivity().getIntent().getBooleanExtra("market_enabled",true);
            }



            setToolbarText();
            setVariables();


            return rootView;
        }





        @Override
        public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            getIntentVariables();
            setupRecyclerView();
            setupSwipeContainer();
            setupLocalBroadcastManager();

            if(savedInstanceState==null && !switchMade)
            {
                onRefresh();
                isSaved = true;
            }

        }


        

        void getIntentVariables()
        {
            if(getActivity()==null)
            {
                return;
            }

            filterShopsByCategory = getActivity().getIntent().getBooleanExtra("filter_shops_by_category",false);


            if(filterShopsByCategory)
            {
                itemCategoryID = getActivity().getIntent().getIntExtra("category_id",123);
            }

        }




        private void setupLocalBroadcastManager()
        {



            IntentFilter filter = new IntentFilter();

            filter.addAction(GetAppSettings.INTENT_ACTION_MARKET_CONFIG_FETCHED);
            filter.addAction(LocationService.INTENT_ACTION_LOCATION_UPDATED);

            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {


                    if(getActivity()!=null)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(intent.getAction().equals(LocationService.INTENT_ACTION_LOCATION_UPDATED))
                                {
                                    if(item_count==0)
                                    {
                                        dataset.clear();
                                        dataset.add(new ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData());
                                        adapter.notifyDataSetChanged();
                                    }


                                    countDownTimer.cancel();
                                    countDownTimer.start();

//                                    onRefresh();
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

                if(isResumed())
                {
                    makeRefreshNetworkCall();
                }
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
            onRefresh();
        }







        @Override
        public void onRefresh() {

            if(item_count==0)
            {
                dataset.clear();
                dataset.add(new ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData());
                adapter.notifyDataSetChanged();
            }

            clearDataset=true;
            makeNetworkCall();
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




            Integer marketIDTemp = null;

            if(marketID>0)
            {
                marketIDTemp = marketID;
            }



            Call<ShopEndPoint> callEndpoint;




            callEndpoint = shopService.filterShopsByItemCategory(
                    itemCategoryID, marketIDTemp,marketEnabled,
                    PrefLocation.getLatitudeSelected(getActivity()), PrefLocation.getLongitudeSelected(getActivity()),
                    null,
                    clearDataset,
                    clearDataset && !filterShopsByCategory,
                    !PrefCurrency.isCurrencyValid(requireActivity()),
                    searchQuery,
                    ViewHolderFilterShops.getSortString(getActivity()),
                    limit,offset,
                    clearDataset,false
            );



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


                            if (response.body() != null) {

                                item_count = response.body().getItemCount();
                            }


                            clearDataset=false;


                            boolean showCreateShop = MyApplication.getAppContext().getResources().getBoolean(R.bool.show_create_shop);



                            dataset.addAll(response.body().getResults());


                            int i = 0;

                            if(!filterShopsByCategory)
                            {

                                if(marketID==0)
                                {
                                    // add location picker only on home screen not when browsing in market home
                                    dataset.add(i++, new ViewHolderSetLocationManually.SetLocationManually());
                                }

//                                if(Highlights.getHighlightsFrontScreen(getActivity())!=null && getResources().getBoolean(R.bool.slider_front_enabled))
//                                {
//                                    dataset.add(i++,Highlights.getHighlightsFrontScreen(getActivity()));
//                                }
                            }




                            if(item_count>0)
                            {
                                dataset.add(i++,new FilterShopsData());
                            }



                            if(!filterShopsByCategory)
                            {

                                if(response.body().getBannerImages()!=null && getResources().getBoolean(R.bool.banner_enabled))
                                {
                                    BannerImageList bannerImageList = new BannerImageList();
                                    bannerImageList.setBannerImageList(response.body().getBannerImages());
                                    dataset.add(i++,bannerImageList);

//                                    System.out.println("Banner Images not Null !");
                                }

                            }




                            if(item_count==0)
                            {


                                User user = PrefLogin.getUser(getActivity());


                                    if(user!=null)
                                    {
                                        if(user.getRole()==User.ROLE_END_USER_CODE)
                                        {
                                            if(showCreateShop) {
                                                dataset.add(new ViewHolderCreateShop.CreateShopData());
                                            }
                                        }
                                        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
                                        {
//                                            dataset.add(new ViewHolderShopSuggestions.ShopSuggestionsData());
                                        }
                                    }
                                    else
                                    {
                                        if(showCreateShop) {
                                            dataset.add(new ViewHolderCreateShop.CreateShopData());
                                        }
                                    }


                                    boolean showButton = false;



                                if(user !=null)
                                {
                                    if(user.getRole()==User.ROLE_END_USER_CODE)
                                    {
                                        showButton= true;
                                    }
                                }
                                else {

                                  showButton = true;
                                }


                                dataset.add(ViewHolderEmptyScreenListItem.EmptyScreenDataListItem.getNotAvailableAtYourLocation(showButton));
//                                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getNotAvailableAtCurrentLocation());

                            }
                            else
                            {




                                if(!filterShopsByCategory)
                                {
                                    ItemCategoriesList list = new ItemCategoriesList();
                                    list.setItemCategories(response.body().getSubcategories());
                                    list.setSelectedCategoryID(itemCategoryID);
                                    list.setScrollPositionForSelected(layoutPositionSelected);

//                                    i++;

                                    if(getResources().getBoolean(R.bool.show_categories_in_shops_screen))
                                    {

                                        dataset.add(i++,list);
                                    }
                                }






                                if(!filterShopsByCategory)
                                {
                                    User user = PrefLogin.getUser(getActivity());

                                    if(user!=null)
                                    {
                                        if(user.getRole()==User.ROLE_END_USER_CODE)
                                        {

                                            if(showCreateShop) {
                                                dataset.add(i++,new ViewHolderCreateShop.CreateShopData());
                                            }

                                        }
                                        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
                                        {

//                                            dataset.add(i++,new ViewHolderShopSuggestions.ShopSuggestionsData());
                                        }
                                    }
                                    else
                                    {

                                        if(showCreateShop) {
                                            dataset.add(i++, new ViewHolderCreateShop.CreateShopData());
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            dataset.addAll(response.body().getResults());
                        }





                        if(response.body()!=null && response.body().getIsoCountryCode()!=null)
                        {
                            PrefCurrency.saveCurrencyUsingISOCountryCode(
                                    PrefLocation.getLatitudeSelected(getActivity()), PrefLocation.getLongitudeSelected(getActivity()),
                                    response.body().getIsoCountryCode(),requireActivity()
                            );
                        }

                    }
                    else
                    {
                        showToastMessage("Failed Code : " + response.code());
                    }






                    if(offset + limit >= item_count)
                    {
                        adapter.setLoadMore(false);
//                        dataset.add(EmptyScreenDataListItem.getFullGraphicInfo(R.drawable.infographic_new));
                    }
                    else
                    {
                        adapter.setLoadMore(true);
                    }



                    String isoCountryCode = PrefCurrency.getISOCountryCode(requireActivity());

                    if(isoCountryCode.equals("IN") && PrefCurrency.countryOfOperation()==PrefCurrency.OPERATING_ONLY_OUTSIDE_INDIA)
                    {
                        dataset.clear();
                        dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getNotAvailableAtCurrentLocation());
//                        showToastMessage("Not available in India !");
                    }
                    else if(!isoCountryCode.equals("IN") && PrefCurrency.countryOfOperation()==PrefCurrency.OPERATING_ONLY_IN_INDIA)
                    {
                        dataset.clear();
                        dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getNotAvailableAtCurrentLocation());
//                        showToastMessage("Not available Outside India !");
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
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                }
            });

        }





        private void tryAgainClick()
        {
            makeRefreshNetworkCall();
        }




        @Override
        public void listItemButtonClick(String url) {
//                tryAgainClick();


            if (PrefLogin.getUser(requireActivity()) == null) {

                Intent intent = new Intent(requireActivity(), Login.class);
                startActivityForResult(intent, 57121);
                return;
            }



            Intent intent = new Intent(requireActivity(), AddShop.class);
            startActivity(intent);
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
        public void notifySortChanged() {
            makeRefreshNetworkCall();
        }






    private String searchQuery = null;

    @Override
    public void search(final String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
//        UtilityFunctions.showToastMessage(getActivity(),searchString);
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
//        UtilityFunctions.showToastMessage(getActivity(),"End Search");
    }




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
//                intent = new Intent(getActivity(), PickLocation.class);
            }


            intent.putExtra("lat_dest",PrefLocation.getLatitudeSelected(getActivity()));
            intent.putExtra("lon_dest",PrefLocation.getLongitudeSelected(getActivity()));
            startActivityForResult(intent,3);

        }
        else
        {

//            intent = new Intent(getActivity(), DeliveryAddressActivity.class);
//            startActivityForResult(intent,3);

            DeliveryAddressSelectionFragment.Companion.newInstance()
                    .show(getChildFragmentManager(), "address_selection");
        }

    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode==3 && resultCode==6)
        {

            PrefLocation.saveLatLonSelected(data.getDoubleExtra("lat_dest",0.0),
                    data.getDoubleExtra("lon_dest",0.0),
                    getActivity()
            );


            PrefLocation.setLocationSetByUser(true,getActivity());
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
        }
        if(requestCode==25 && resultCode==50)
        {
            dismissAddressBottomSheet();
            makeRefreshNetworkCall();
        }
        else if(requestCode==57121 || requestCode==890)
        {
            makeRefreshNetworkCall();
        }

    }




    @Override
    public void listItemClick() {

    }




    @Override
    public void listItemFilterShopClick(int itemCategoryID, ItemCategory itemCategory, int layoutPosition) {


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
    public void addItemClick() {

        Intent intent = new Intent(getActivity(), EditBannerImage.class);
        intent.putExtra(EditBannerImageFragment.EDIT_MODE_INTENT_KEY,EditBannerImageFragment.MODE_ADD);
        startActivity(intent);
    }




    void setVariables()
    {
        if(getActivity()!=null)
        {
            Intent intent = getActivity().getIntent();
            marketName = intent.getStringExtra("market_name");
            marketID = intent.getIntExtra("market_id",0);
        }
    }




    void setToolbarText()
    {
        boolean filterShopsByCategory = getActivity().getIntent().getBooleanExtra("filter_shops_by_category",false);
        String categoryName = getActivity().getIntent().getStringExtra("item_category_name");

        if(getActivity() instanceof SetToolbarText)
        {
            if(filterShopsByCategory)
            {
                ((SetToolbarText) getActivity()).setToolbar(true,categoryName + " Shops",true,null);
            }
            else
            {
                ((SetToolbarText) getActivity()).setToolbar(
                        true,getString(R.string.app_name), true,
                        getString(R.string.app_tag_line_for_main_app)
                );
            }
        }
    }




    @Override
    public void buttonClick(ViewHolderButton.ButtonData data) {


        Intent intent = new Intent(getActivity(), EditDeliveryAddress.class);
        intent.putExtra(EditAddressFragment.EDIT_MODE_INTENT_KEY, EditAddressFragment.MODE_ADD);
        intent.putExtra("select_added_address",true);
        startActivityForResult(intent,25);
    }





    @Override
    public void listItemClick(@org.jetbrains.annotations.Nullable DeliveryAddress address, int position) {

        dismissAddressBottomSheet();
        PrefLocation.saveDeliveryAddress(address,getActivity());
        makeRefreshNetworkCall();
    }


    void dismissAddressBottomSheet()
    {

        Fragment addressBottomSheet = getChildFragmentManager().findFragmentByTag("address_selection");

        if(addressBottomSheet instanceof DeliveryAddressSelectionFragment)
        {
            ((DeliveryAddressSelectionFragment) addressBottomSheet).dismiss();
        }
    }


    @Override
    public void changeAddressClick() {

    }

    @Override
    public void refreshFragment() {

        makeRefreshNetworkCall();
    }
}
