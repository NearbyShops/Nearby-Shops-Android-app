package org.nearbyshops.whitelabelapp.AdminShop.ItemsInShopByCatSeller;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.whitelabelapp.API.ItemCategoryService;
import org.nearbyshops.whitelabelapp.API.ShopItemService;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ItemCategoryEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyIndicatorChanged;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.PreferencesSort.PrefSortItemsInShop;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.whitelabelapp.AdminShop.ViewHolders.ViewHolderShopItemSeller;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 2/12/16.
 */

public class ItemsInShopByCatSellerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderShopItemSeller.ShopItemUpdates, ViewHolderItemCategory.ListItemClick, NotifyBackPressed, NotifySort, NotifySearch {




    private boolean isDestroyed = false;



    private int limit_item = 10;
    private int offset_item = 0;
    private int item_count_item;
    private int fetched_items_count = 0;

    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView itemCategoriesList;



    private ArrayList<Object> dataset = new ArrayList<>();
    private ArrayList<ItemCategory> datasetCategory = new ArrayList<>();
    private ArrayList<ShopItem> datasetShopItems = new ArrayList<>();



    private GridLayoutManager layoutManager;
    private Adapter listAdapter;


    @Inject
    ItemCategoryService itemCategoryService;

    @Inject
    ShopItemService shopItemService;



    private ItemCategory currentCategory = null;


    public ItemsInShopByCatSellerFragment() {

        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_items_in_shop_by_cat_seller, container, false);

        ButterKnife.bind(this,rootView);


        setupRecyclerView();
        setupSwipeContainer();


        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }


        notifyItemIndicatorChanged();

        return rootView;
    }



    private void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }




    private void setupRecyclerView()
    {


        listAdapter = new Adapter(dataset,getActivity(),this);
        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),6, LinearLayoutManager.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);



        // Code for Staggered Grid Layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {


            @Override
            public int getSpanSize(int position) {

                if(position == dataset.size())
                {
                    return 6;
                }
                else if(dataset.get(position) instanceof ItemCategory)
                {
                       final DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

                    if(spanCount==0){
                        spanCount = 1;
                    }

                    return (6/spanCount);

                }
                else if(dataset.get(position) instanceof Item)
                {

                    return 6;
                }
                else if(dataset.get(position) instanceof ViewHolderHeader.HeaderTitle)
                {
                    return 6;
                }

                return 6;
            }
        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(offset_item + limit_item > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }

                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    // trigger fetch next page

                    if((offset_item+limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;
                        makeRequestShopItem(false);
                    }

                }
            }

        });

    }


//    @State int previous_position = -1;



    @Override
    public void onRefresh() {

        makeRequestItemCategory();
        makeRequestShopItem(true);
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





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroyed=false;
    }



    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
        }
    }



    boolean isFirst = true;



    private void makeRequestItemCategory()
    {

        /*Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                null,
                currentCategory.getItemCategoryID(),
                null,
                null,
                null,
                null,null,null,
                "ITEM_CATEGORY_NAME",null,null,false);*/

        Call<ItemCategoryEndPoint> endPointCall = null;

        Shop currentShop = PrefShopAdminHome.getShop(getContext());

        if(searchQuery == null)
        {
//            /endPointCall = itemCategoryService.getItemCategoriesQuerySimple(
//                    currentCategory.getItemCategoryID(),
//                    null,
//                    null,
//                    "id",null,null
//            );

            endPointCall = itemCategoryService.getItemCategories(
                    currentShop.getShopID(),
                    currentCategory.getItemCategoryID(),
                    null,null,null,null,null,null, ItemCategory.CATEGORY_ORDER,null,null,
                    false
            );

        }
        else
        {

//            endPointCall = itemCategoryService.getItemCategoriesQuerySimple(
//                    null,
//                    null,
//                    searchQuery,
//                    "id",null,null
//            );


            endPointCall = itemCategoryService.getItemCategories(
                    currentShop.getShopID(),
                    null,
                    null,null,null,null,null,null,
                    ItemCategory.CATEGORY_ORDER, null,null,
                    false
            );

        }



        endPointCall.enqueue(new Callback<ItemCategoryEndPoint>() {
            @Override
            public void onResponse(Call<ItemCategoryEndPoint> call, Response<ItemCategoryEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!=null)
                {

                    ItemCategoryEndPoint endPoint = response.body();
//                    item_count_item_category = endPoint.getItemCount();
                    datasetCategory.clear();
                    datasetCategory.addAll(endPoint.getResults());
                }


                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    // is last
                    refreshAdapter();
                    isFirst = true;// reset the flag
                }


//                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ItemCategoryEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network request failed. Please check your connection !");


                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    // is last
                    refreshAdapter();
                    isFirst = true;// reset the flag
                }



//                if(swipeContainer!=null)
//                {
//                    swipeContainer.setRefreshing(false);
//                }

            }
        });
    }







    private void refreshAdapter()
    {
        dataset.clear();

        ViewHolderHeader.HeaderTitle headerItemCategory = new ViewHolderHeader.HeaderTitle();

        if(searchQuery==null)
        {
            headerItemCategory.setHeading(currentCategory.getCategoryName() + " Subcategories");
        }
        else
        {
            headerItemCategory.setHeading( "Search Results (Subcategories)");
        }

        dataset.add(headerItemCategory);

        dataset.addAll(datasetCategory);

        ViewHolderHeader.HeaderTitle headerItem = new ViewHolderHeader.HeaderTitle();

        if(searchQuery==null)
        {
            headerItem.setHeading(currentCategory.getCategoryName() + " Items In Shop");
        }
        else
        {
            headerItem.setHeading("Search Results (Items In Shop)");
        }


        dataset.add(headerItem);
        dataset.addAll(datasetShopItems);
        listAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
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





    private void makeRequestShopItem(final boolean clearDataset)
    {

        if(clearDataset)
        {
            offset_item = 0;
        }



        String current_sort = "";
        current_sort = PrefSortItemsInShop.getSort(getContext()) + " " + PrefSortItemsInShop.getAscending(getContext());


        Call<ShopItemEndPoint> endPointCall = null;

        Shop currentShop = PrefShopAdminHome.getShop(getContext());



        if(searchQuery==null)
        {



            endPointCall = shopItemService.getShopItemsByShop(
                    currentCategory.getItemCategoryID(),false,
                    false,
                    currentShop.getShopID(),null,
                    null,null,null,
                    null,null,
                    null,null,null,
                    null,
                    false,null,
                    current_sort,
                    limit_item,offset_item,
                    clearDataset, false
            );


        }
        else
        {


            endPointCall = shopItemService.getShopItemsByShop(
                    null,false,false,
                    currentShop.getShopID(),
                    null,
                    null,null,null,null,null,null,null,null,
                    searchQuery,
                    false,false,
                    current_sort,
                    limit_item,offset_item,
                    clearDataset, false
            );

//
//            endPointCall = shopItemService.getShopItemEndpoint(
//                    null,false,
//                    currentShop.getShopID(),
//                    null,null,null,null,null,null,null,null,null,
//                    null,null,null,
//                    searchQuery,current_sort,
//                    limit_item,offset_item,
//                    clearDataset, false
//            );
        }




        endPointCall.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {

                    if(response.body()!=null)
                    {

                        datasetShopItems.clear();
                        datasetShopItems.addAll(response.body().getResults());
                        item_count_item = response.body().getItemCount();
                        fetched_items_count = datasetShopItems.size();

//                        if(response.body().getItemCount()!=null)
//                        {
//
//                        }
                    }


                    if(isFirst)
                    {
                        isFirst = false;
                    }
                    else
                    {
                        // is last
                        refreshAdapter();
                        isFirst = true;// reset the flag
                    }

                }
                else
                {
                    if(response.body()!=null)
                    {

                        dataset.addAll(response.body().getResults());
                        fetched_items_count = fetched_items_count + response.body().getResults().size();
//                        item_count_item = response.body().getItemCount();
                        listAdapter.notifyDataSetChanged();
                    }

                    swipeContainer.setRefreshing(false);
                }






                if(offset_item+limit_item >= item_count_item)
                {
                    listAdapter.setLoadMore(false);
                }
                else
                {
                    listAdapter.setLoadMore(true);
                }


                notifyItemIndicatorChanged();


            }

            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {

                    if(isFirst)
                    {
                        isFirst = false;
                    }
                    else
                    {
                        // is last
                        refreshAdapter();
                        isFirst = true;// reset the flag
                    }
                }
                else
                {
                    swipeContainer.setRefreshing(false);
                }


                showToastMessage("Items: Network request failed. Please check your connection !");


            }
        });

    }






    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory, int scrollPosition) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        makeRefreshNetworkCall();

        // End Search Mode
        searchQuery = null;

        // reset previous flag

    }







    @Override
    public void notifyShopItemUpdated(final ShopItem shopItem) {

//
//        Call<ResponseBody> call = shopItemService.putShopItem(
//                PrefLogin.getAuthorizationHeaders(getActivity()),
//                shopItem
//        );
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if(response.code()==200)
//                {
//
//                    if(shopItem.getItem()!=null)
//                    {
//                        showToastMessage(shopItem.getItem().getItemName() + " Updated !");
//
//                    }else
//                    {
//                        showToastMessage("Update Successful !");
//                    }
//
//                    //makeNetworkCall();
//                }
//                else if(response.code() == 403)
//                {
//                    showToastMessage("Not permitted !");
//                }
//                else if(response.code() == 401)
//                {
//                    showToastMessage("We are not able to identify you !");
//                }
//                else
//                {
//                    showToastMessage("Failed : Code " + String.valueOf(response.code()));
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                showToastMessage("Network Request Failed. Try again !");
//
//            }
//        });

    }



    @Override
    public void notifyShopItemRemoved(final ShopItem shopItem) {


        offset_item = offset_item -1;
        fetched_items_count = fetched_items_count -1;
        item_count_item = item_count_item -1;


//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//
//        dialog.setTitle("Confirm Remove Item !")
//                .setMessage("Do you want to remove this item from your shop !")
//                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        removeShopItem(shopItem);
//
//                    }
//                })
//                .setNegativeButton("No",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        showToastMessage("Cancelled !");
//                    }
//                })
//                .show();

    }



    void removeShopItem(final ShopItem shopItem)
    {

        Call<ResponseBody> responseBodyCall = shopItemService.deleteShopItem(
                PrefLogin.getAuthorizationHeader(getActivity()),
                shopItem.getShopID(),
                shopItem.getItemID()
        );

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    if(shopItem.getItem()!=null)
                    {
                        showToastMessage(shopItem.getItem().getItemName() + " Removed !");

                    }else
                    {
                        showToastMessage("Successful !");
                    }



                    int removedPosition = dataset.indexOf(shopItem);
                    dataset.remove(shopItem);
                    listAdapter.notifyItemRemoved(removedPosition);

                    offset_item = offset_item -1;
                    fetched_items_count = fetched_items_count -1;
                    item_count_item = item_count_item -1;
                    notifyItemIndicatorChanged();


                }else if(response.code() == 304) {

                    showToastMessage("Not removed !");

                }
                else if(response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else if(response.code() == 401)
                {
                    showToastMessage("We are not able to identify you !");
                }
                else
                {
                    showToastMessage("Server Error !");
                }


//                makeRefreshNetworkCall();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network request failed !");

            }
        });


    }




    @Override
    public boolean backPressed() {

        // reset previous flag

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        // clear selected items
//        listAdapter.selectedItems.clear();

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





    private void notifyItemIndicatorChanged()
    {
        if(getActivity() instanceof NotifyIndicatorChanged)
        {
            ((NotifyIndicatorChanged) getActivity())
                    .notifyItemIndicatorChanged(fetched_items_count + " out of " + item_count_item + " " + currentCategory.getCategoryName() + " Items");
        }
    }





    @Override
    public void notifySortChanged() {

        System.out.println("Notify Sort Clicked !");
        makeRefreshNetworkCall();
    }


    // display shop Item Status

}
