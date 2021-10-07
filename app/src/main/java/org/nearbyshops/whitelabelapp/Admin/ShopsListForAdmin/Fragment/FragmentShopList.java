package org.nearbyshops.whitelabelapp.Admin.ShopsListForAdmin.Fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopService;
import org.nearbyshops.whitelabelapp.Admin.ViewHolders.ViewHolderShopForAdmin;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShop;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyLocation;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyTitleChangedWithTab;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctionsKotlin;
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightsBuilder;
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.ViewHolderHighlightListItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterShopsAdminData;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.ViewHolderFilterShopsAdmin;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentShopList extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        NotifySearch, NotifySort, NotifyLocation,
        ViewHolderFilterShopsAdmin.ListItemClick,
        ViewHolderShopForAdmin.ListItemClick, ViewHolderHighlightListItem.ListItemClick {

    private static final String ARG_REGISTRATION_STATUS = "arg_registration_status";



    @Inject
    ShopService shopService;

    private RecyclerView recyclerView;
    private Adapter adapter;

    public List<Object> dataset = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;


    final private int limit = 10;
    private int offset = 0;
    private int item_count = 0;

    int shopRegistrationStatus = Shop.STATUS_NEW_REGISTRATION;

    private boolean isDestroyed;


    public FragmentShopList() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    public static FragmentShopList newInstance(int sectionNumber) {
        FragmentShopList fragment = new FragmentShopList();
        Bundle args = new Bundle();
        args.putInt(ARG_REGISTRATION_STATUS, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_approvals, container, false);


        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeContainer = rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        if(getArguments()!=null)
        {
            shopRegistrationStatus = getArguments().getInt(ARG_REGISTRATION_STATUS);
        }


        setupRecyclerView();
        setupSwipeContainer();

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
        adapter = new Adapter(dataset,getActivity(),this, shopRegistrationStatus);
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);


//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(1);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);



                if(offset + limit > layoutManager.findLastVisibleItemPosition()+1-1)
                {
                    return;
                }

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1+1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }
                }

            }
        });

    }



    @Override
    public void onRefresh() {
        makeNetworkCall(true);
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







    private void makeNetworkCall(final boolean clearDataset)
    {
        if(clearDataset)
        {
            offset = 0;
        }


        Call<ShopEndPoint> call = null;

        String current_sort = "";
        current_sort = ViewHolderFilterShopsAdmin.getSortString(getActivity());


        Double latitude = null;
        Double longitude = null;


        latitude = PrefLocation.getLatitudeSelected(getActivity());
        longitude = PrefLocation.getLongitudeSelected(getActivity());


        if (getArguments() == null) {
            return;
        }




        call = shopService.getShopListSimple(
                PrefLogin.getAuthorizationHeader(getActivity()),
                shopRegistrationStatus,
                latitude,longitude,
                searchQuery,current_sort,
                limit,offset,
                clearDataset,false
        );




        if(call == null)
        {
            showToastMessage("Error fetching shop list !");
            return;
        }


        call.enqueue(new Callback<ShopEndPoint>() {
            @Override
            public void onResponse(Call<ShopEndPoint> call, Response<ShopEndPoint> response) {


                if(isDestroyed)
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



                        if(item_count>0)
                        {
                            dataset.add(new FilterShopsAdminData());
                        }

                        dataset.add(HighlightsBuilder.getHighlightsAddShopToMarket(requireContext()));

                    }


                    if (response.body() != null && response.body().getResults() != null) {

                        dataset.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        notifyTitleChanged();

                    }


                }
                else
                {
                    showToastMessage("Failed code : " + response.code());
                }


                if(item_count==0)
                {
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenShopsListForAdmin());
                }



                adapter.notifyDataSetChanged();


                if(offset + limit >= item_count)
                {
                    adapter.setLoadMore(false);
                }
                else
                {
                    adapter.setLoadMore(true);
                }


                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ShopEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }



//                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);


                dataset.clear();
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

            }
        });

    }




    @Override
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
    }





    private void showToastMessage(String message)
    {
//        if(getActivity()!=null)
//        {
//            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
//        }


        UtilityFunctions.showToastMessage(getActivity(),message);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }




    private void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChangedWithTab)
        {

            if(getArguments().getInt(ARG_REGISTRATION_STATUS)== Shop.STATUS_NEW_REGISTRATION)
            {
                ((NotifyTitleChangedWithTab)getActivity())
                        .NotifyTitleChanged(
                                "New (" + (dataset.size()-1)
                                        + "/" + item_count + ")",0);
            }
            else if(getArguments().getInt(ARG_REGISTRATION_STATUS)== Shop.STATUS_SHOP_ENABLED)
            {
                ((NotifyTitleChangedWithTab)getActivity())
                        .NotifyTitleChanged(
                                "Enabled (" + (dataset.size()-1)
                                        + "/" + item_count + ")",1);
            }
            else if(getArguments().getInt(ARG_REGISTRATION_STATUS)== Shop.STATUS_WAIT_LISTED)
            {
                ((NotifyTitleChangedWithTab)getActivity())
                        .NotifyTitleChanged(
                                "Waitlisted (" + (dataset.size()-1)
                                        + "/" + item_count + ")",2);
            }
            else if(getArguments().getInt(ARG_REGISTRATION_STATUS)==Shop.STATUS_DISABLED)
            {
                ((NotifyTitleChangedWithTab)getActivity())
                        .NotifyTitleChanged(
                                "Disabled (" + (dataset.size()-1)
                                        + "/" + item_count + ")",3);

            }

        }
    }


    // Refresh the Confirmed PlaceHolderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
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
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }



    @Override
    public void fetchedLocation(Location location) {
        makeRefreshNetworkCall();
    }







    @Override
    public void listItemClick(Shop shop, int position) {

        openEditShopScreen(shop);
    }

    @Override
    public void editClick(Shop shop, int position) {

        openEditShopScreen(shop);
    }





    private void openEditShopScreen(Shop shop)
    {

//        Gson gson = UtilityFunctions.provideGson();
//        String jsonString = gson.toJson(shop);

        Intent intent = new Intent(getActivity(), EditShop.class);
//        intent.putExtra("shop_profile",jsonString);
        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE_BY_ADMIN);
        intent.putExtra("shop_id",shop.getShopID());
        startActivity(intent);
    }




    @Override
    public void filterShopUpdated() {

        makeRefreshNetworkCall();
    }


    @Override
    public void listItemCLick(@NotNull Object item, int slideNumber) {

        if(slideNumber==1)
        {
            // register shop using vendor app

//            String url = getString(R.string.app_download_link_vendor_app);
//
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);


            // share invite link
            UtilityFunctionsKotlin.Companion.shareInviteLinkToVendors(requireContext());

        }
        else if(slideNumber==2)
        {
            // add Shops to market

        }
        else if(slideNumber==3)
        {
            // share your market
            UtilityFunctionsKotlin.Companion.shareMarketClick(requireContext());
        }
    }
}