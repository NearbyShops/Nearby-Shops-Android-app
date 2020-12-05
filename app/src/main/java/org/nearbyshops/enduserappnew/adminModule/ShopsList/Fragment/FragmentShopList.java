package org.nearbyshops.enduserappnew.adminModule.ShopsList.Fragment;

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


import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShop.EditShop;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.enduserappnew.Interfaces.NotifyLocation;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.Models.FilterShopsAdminData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.ViewHolderFilterShopsAdmin;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopSmall;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentShopList extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        NotifySearch, NotifySort, NotifyLocation,
        ViewHolderFilterShopsAdmin.ListItemClick,
        ViewHolderShopSmall.ListItemClick {

    private static final String ARG_SECTION_NUMBER = "section_number";


    private Location location;

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


    private boolean isDestroyed;

    public static final int MODE_ENABLED = 1;
    public static final int MODE_DISABLED = 2;
    public static final int MODE_WAITLISTED = 3;
    public static final int MODE_NEW = 4;



    public FragmentShopList() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    public static FragmentShopList newInstance(int sectionNumber) {
        FragmentShopList fragment = new FragmentShopList();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
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

        adapter = new Adapter(dataset,getActivity(),this);

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
//        current_sort = PrefSortShopsAdmin.getSort(getContext()) + " " + PrefSortShopsAdmin.getAscending(getContext());
        current_sort = ViewHolderFilterShopsAdmin.getSortString(getActivity());


        Double latitude = null;
        Double longitude = null;


        latitude = PrefLocation.getLatitude(getActivity());
        longitude = PrefLocation.getLongitude(getActivity());




        if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_NEW)
        {
            call = shopService.getShopListSimple(
                    true,
                    null,null,
                    latitude,longitude,
                    searchQuery,current_sort,limit,offset,
                    clearDataset,false);
        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_ENABLED)
        {

            call = shopService.getShopListSimple(
                    null,
                    true,null,
                    latitude,longitude,
                    searchQuery,current_sort,
                    limit,offset,
                    clearDataset,false
            );


        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER) == MODE_DISABLED)
        {

            call = shopService.getShopListSimple(
                    null,
                    false,false,
                    latitude,longitude,
                    searchQuery,current_sort,
                    limit,offset,
                    clearDataset,false
            );
        }
        else if (getArguments().getInt(ARG_SECTION_NUMBER) == MODE_WAITLISTED)
        {

            call = shopService.getShopListSimple(
                    null,
                    false,true,
                    latitude,longitude,
                    searchQuery,current_sort,
                    limit,offset,
                    clearDataset,false
            );

        }




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

                        dataset.add(new FilterShopsAdminData());
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
                    dataset.add(EmptyScreenDataFullScreen.emptyScreenShopsListForAdmin());
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
                dataset.add(EmptyScreenDataFullScreen.getOffline());
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

        if(getActivity() instanceof NotifyTitleChanged)
        {

            if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_NEW)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "New (" + (dataset.size()-1)
                                        + "/" + item_count + ")",0);
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_ENABLED)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Enabled (" + (dataset.size()-1)
                                        + "/" + item_count + ")",1);

            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_DISABLED)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Disabled (" + (dataset.size()-1)
                                        + "/" + item_count + ")",2);
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_WAITLISTED)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Waitlisted (" + (dataset.size()-1)
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



}