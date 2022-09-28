package org.nearbyshops.whitelabelapp.AdminShop.zBottomDashboard.Deprecated;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.whitelabelapp.API.ShopItemService;
import org.nearbyshops.whitelabelapp.AdminShop.QuickStockEditor.Adapter;
import org.nearbyshops.whitelabelapp.AdminShop.ViewHolders.ViewHolderShopItemSeller;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyTitleChangedWithTab;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderItemCategorySmall;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentItemsInShop extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, ViewHolderShopItemSeller.ShopItemUpdates,
        ViewHolderItemCategorySmall.ListItemClick,
        NotifyBackPressed, NotifySearch {


    @Inject
    ShopItemService shopItemService;
    private RecyclerView recyclerView;
    private Adapter adapter;


    public ArrayList<Object> dataset = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;


    public static String ARG_MODE_KEY = "mode_key";


    public static int MODE_OUT_OF_STOCK = 1;
    public static int MODE_LOW_STOCK = 2;
    public static int MODE_RECENTLY_UPDATED = 3;
    public static int MODE_RECENTLY_ADDED = 4;
    public static int MODE_PRICE_NOT_SET = 5;



    private ItemCategory currentCategory = null;



    private int limit = 10;
    private int offset = 0;
    private int item_count = 0;




    public FragmentItemsInShop() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);
    }






    public static FragmentItemsInShop newInstance(int mode) {
        FragmentItemsInShop fragment = new FragmentItemsInShop();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE_KEY,mode);
        fragment.setArguments(args);
        return fragment;
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_items_in_shop_seller_bottom, container, false);
        setRetainInstance(true);

        swipeContainer = rootView.findViewById(R.id.swipeContainer);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        if(savedInstanceState == null)
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


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(offset + limit > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }

                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
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








    private void makeNetworkCall(final boolean clearDataset) {

        if(clearDataset) {
            offset = 0;
        }





        int mode = getArguments().getInt(ARG_MODE_KEY);


        Shop currentShop = PrefShopAdminHome.getShop(getContext());




        Call<ShopItemEndPoint> call = null;


        if (mode == MODE_OUT_OF_STOCK) {



            call = shopItemService.getShopItemsByShop(currentCategory.getItemCategoryID(),
                    false,clearDataset,
                    currentShop.getShopID(),null,null,null,
                    null,null,
                    true,null,null,null,null,
                    null,null,
                    "LAST_UPDATE_DATE_TIME",
                    limit,offset,clearDataset,
                    false
            );



        }
        else if (mode == MODE_LOW_STOCK)
        {

            call = shopItemService.getShopItemsByShop(
                    currentCategory.getItemCategoryID(),true,clearDataset,
                    currentShop.getShopID(),null,
                    null,null,null,
                    null,null,
                    null,
                    null,null,
                    null,
                    false,null,
                    "available_item_quantity",
                    limit,offset,
                    clearDataset, false
            );

        }
        else if (mode == MODE_RECENTLY_ADDED)
        {


            call = shopItemService.getShopItemsByShop(
                    currentCategory.getItemCategoryID(),true,clearDataset,
                    currentShop.getShopID(),null,
                    null,null,null,
                    null,null,
                    null,
                    null,null,
                    null,
                    false,null,
                    "date_time_added desc",
                    limit,offset,
                    clearDataset, false
            );



        }else if (mode == MODE_RECENTLY_UPDATED)
        {

            call = shopItemService.getShopItemsByShop(
                    currentCategory.getItemCategoryID(),
                    true,clearDataset,
                    currentShop.getShopID(),null,
                    null,null,null,
                    null,null,
                    null,
                    null,null,
                    null,
                    false,false,
                    " last_update_date_time desc ",
                    limit,offset,
                    clearDataset, false
            );

        }
        else if (mode == MODE_PRICE_NOT_SET)
        {


            call = shopItemService.getShopItemsByShop(
                    currentCategory.getItemCategoryID(),
                    true,clearDataset,
                    currentShop.getShopID(),null,
                    null,null,null,
                    null,null,
                    true,
                    null,null,
                    null,
                    false,false,
                    " LAST_UPDATE_DATE_TIME ",
                    limit,offset,
                    clearDataset, false
            );
        }



        if(call == null)
        {
            return;
        }


        call.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {

                if(!isVisible())
                {
                    return;
                }


                if(response.body()!= null)
                {
                    if(clearDataset)
                    {
                        dataset.clear();
                        item_count = response.body().getItemCount();

                        if(item_count==0)
                        {
                            dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenQuickStockEditor());
                        }

                    }


                    if(response.body().getResults().size()==0)
                    {
                        dataset.addAll(response.body().getSubcategories());
                    }
                    else
                    {

                        ItemCategoriesList list = new ItemCategoriesList();
                        list.setItemCategories(response.body().getSubcategories());

                        dataset.add(list);

                    }


                    dataset.addAll(response.body().getResults());





                    if(offset+limit >= item_count)
                    {
                        adapter.setLoadMore(false);
                    }
                    else
                    {
                        adapter.setLoadMore(true);
                    }

                }




                notifyTitleChanged();
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                if(!isVisible())
                {
                    return;
                }


                swipeContainer.setRefreshing(false);

                dataset.clear();
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();
            }
        });
    }





    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }




    @Override
    public void notifyShopItemUpdated(final ShopItem shopItem) {

    }







    @Override
    public void notifyShopItemRemoved(final ShopItem shopItem) {

        offset = offset - 1;
        item_count = item_count -1;
        notifyTitleChanged();
    }





    private void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChangedWithTab)
        {

            int mode = getArguments().getInt(ARG_MODE_KEY);


            if (mode == MODE_OUT_OF_STOCK) {

//            call = shopItemService.getShopItems(currentShop.getShopID(), null, null, true, null);

                ((NotifyTitleChangedWithTab)getActivity())
                        .NotifyTitleChanged(
                                "Out of Stock (" + dataset.size()
                                        + "/" + item_count + ")",1);




            } else if (mode == MODE_LOW_STOCK)
            {
                ((NotifyTitleChangedWithTab)getActivity())
                        .NotifyTitleChanged(
                                "Low Stock (" + dataset.size()
                                        + "/" + item_count + ")",0);


            }
            else if (mode == MODE_RECENTLY_ADDED)
            {
                ((NotifyTitleChangedWithTab)getActivity())
                        .NotifyTitleChanged(
                                "Recently Added (" + dataset.size()
                                        + "/" + item_count + ")",3);



            }else if (mode == MODE_RECENTLY_UPDATED)
            {


                ((NotifyTitleChangedWithTab)getActivity())
                        .NotifyTitleChanged(
                                "Recently Updated (" + dataset.size()
                                        + "/" + item_count + ")",4);

            }
            else if (mode == MODE_PRICE_NOT_SET)
            {
                ((NotifyTitleChangedWithTab)getActivity())
                        .NotifyTitleChanged(
                                "Price not Set (" + dataset.size()
                                        + "/" + item_count + ")",2);

            }



        }
    }




    @Override
    public boolean backPressed() {

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1


        if(currentCategory!=null) {


            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if (currentCategoryID != -1) {
                makeRefreshNetworkCall();
            }
        }

        return currentCategoryID == -1;
    }

    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory, int scrollPosition) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        makeRefreshNetworkCall();

        // End Search Mode
        searchQuery = null;
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
