package org.nearbyshops.enduserappnew.ItemCatalogue.ItemsDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.nearbyshops.enduserappnew.API.ItemCategoryService;
import org.nearbyshops.enduserappnew.API.ItemService;
import org.nearbyshops.enduserappnew.API.ShopItemService;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItem.EditItem;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItem.EditItemFragmentNew;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItem.PrefItem;
import org.nearbyshops.enduserappnew.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.Interfaces.NotifyFABClick;
import org.nearbyshops.enduserappnew.Interfaces.NotifyIndicatorChanged;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.ToggleFab;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
import org.nearbyshops.enduserappnew.SlidingLayerSort.PreferencesSort.PrefSortItems;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItemCategorySmall;
import org.nearbyshops.enduserappnew.aSellerModule.ViewHolders.ViewHolderItemInShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 2/12/16.
 */

public class ItemsDatabaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderItemCategory.ListItemClick,
        ViewHolderItemInShop.ListItemClick,
        ViewHolderItemCategorySmall.ListItemClick,
        NotifyBackPressed, NotifySort, NotifyFABClick, NotifySearch {





    private Map<Integer, ShopItem> shopItemMapTemp = new HashMap<>();

    private boolean isDestroyed = false;
    private  boolean show;


    private int limit_item = 10;
    private int offset_item = 0;
    private int item_count_item;
    private int fetched_items_count = 0;



    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView itemCategoriesList;

    private ArrayList<Object> dataset = new ArrayList<>();


    private GridLayoutManager layoutManager;
    private Adapter listAdapter;


    @BindView(R.id.add_remove_buttons) LinearLayout addRemoveButtons;



    @Inject
    ItemCategoryService itemCategoryService;


    @Inject
    ShopItemService shopItemService;

    @Inject
    ItemService itemService;


    private ItemCategory currentCategory = null;


    public ItemsDatabaseFragment() {

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
        View rootView = inflater.inflate(R.layout.fragment_items_database, container, false);

        ButterKnife.bind(this,rootView);


        setupRecyclerView();
        setupSwipeContainer();


        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }
        else
        {
            // add this at every rotation
            listAdapter.shopItemMap.putAll(shopItemMapTemp);
        }


        notifyItemHeaderChanged();

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

        layoutManager = new GridLayoutManager(getActivity(),6, RecyclerView.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);



        // Code for Staggered Grid Layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {


            @Override
            public int getSpanSize(int position) {


                if(position == dataset.size())
                {

                }
                else if(dataset.get(position) instanceof ItemCategory)
                {
                       final DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

                    if(spanCount==0){
                        spanCount = 1;
                    }

//                    (6/spanCount)
                    return 3;

                }
                else if(dataset.get(position) instanceof Item)
                {

                    return 6;
                }
                else if(dataset.get(position) instanceof HeaderTitle)
                {
                    return 6;
                }

                return 6;
            }
        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);


//        int spanCount = (int) (metrics.widthPixels/(150 * metrics.density));
//
//        if(spanCount==0){
//            spanCount = 1;
//        }

//        layoutManager.setSpanCount(spanCount);


        /*final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        return (3/spanCount);*/


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

                        makeRequestItem(false);
                    }

                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }


        });

    }


//    @State int previous_position = -1;



    @Override
    public void onRefresh() {



        makeRequestItem(true);
        makeNetworkCallShopItem();
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









    private void makeRequestItem(final boolean clearDataset)
    {

        if(clearDataset)
        {
            offset_item = 0;
        }


        String current_sort = "";

        current_sort = PrefSortItems.getSort(getContext()) + " " + PrefSortItems.getAscending(getContext());
/*
        Call<ItemEndPoint> endPointCall = itemService.getItemsEndpoint(currentCategory.getItemCategoryID(),
                null,
                null,
                null,
                null,null, null, null,
                current_sort, limit_item,offset_item,null);*/

        Call<ItemEndPoint> endPointCall = null;



        if(searchQuery==null)
        {
            endPointCall = itemService.getItemsOuterJoin(
                    currentCategory.getItemCategoryID(),
                    clearDataset,null,
                    current_sort,
                    limit_item,offset_item, clearDataset,false);
        }
        else
        {
            endPointCall = itemService.getItemsOuterJoin(
                    null,
                    clearDataset, searchQuery,
                    current_sort,
                    limit_item,offset_item, clearDataset,false);
        }








        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }



                if(response.code() ==200)
                {
//                    showToastMessage("Response Code : " + String.valueOf(response.code()));


                    if(clearDataset)
                    {
                        dataset.clear();

                        item_count_item = response.body().getItemCount();
                        fetched_items_count = dataset.size();



                        if(response.body().getSubcategories()!=null && response.body().getSubcategories().size()>0)
                        {


                            if (searchQuery == null) {

                                HeaderTitle headerItemCategory = new HeaderTitle();

                                if (currentCategory.getParentCategoryID() == -1) {
                                    headerItemCategory.setHeading("Item Categories");
                                } else {
                                    headerItemCategory.setHeading(currentCategory.getCategoryName() + " Subcategories");
                                }

                                dataset.add(headerItemCategory);
                            }




                            if(currentCategory.getParentCategoryID()==-1 || response.body().getResults().size()==0)
                            {
                                dataset.addAll(response.body().getSubcategories());
                            }
                            else
                            {

                                ItemCategoriesList list = new ItemCategoriesList();
                                list.setItemCategories(response.body().getSubcategories());

                                dataset.add(list);

                            }


                        }






                        HeaderTitle headerItem = new HeaderTitle();



                        if(searchQuery==null)
                        {
                            if(response.body().getResults().size()>0)
                            {
                                headerItem.setHeading(currentCategory.getCategoryName() + " Items");
                            }
                            else
                            {

                                headerItem.setHeading("No Items in this category");


                                if(currentCategory.getItemCategoryID()!=1)
                                {
                                    EmptyScreenDataFullScreen data = EmptyScreenDataFullScreen.noItemsAndCategories();
                                    data.setTitle("No items in " + currentCategory.getCategoryName());

                                    dataset.add(data);
                                }

                            }


                        }
                        else
                        {
                            if(response.body().getResults().size()>0)
                            {
                                headerItem.setHeading("Search Results");
                            }
                            else
                            {
                                headerItem.setHeading("No items for the given search !");
                            }
                        }



                        dataset.add(headerItem);

                    }




                    fetched_items_count = fetched_items_count + response.body().getResults().size();
                    dataset.addAll(response.body().getResults());
                }
                else
                {
                    showToastMessage("Failed Code : " + response.code());
                }





                if(offset_item+limit_item >= item_count_item)
                {
                    listAdapter.setLoadMore(false);
                }
                else
                {
                    listAdapter.setLoadMore(true);
                }




                swipeContainer.setRefreshing(false);
                listAdapter.notifyDataSetChanged();
                notifyItemHeaderChanged();


            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }




                if(dataset.size()<=1)
                {
                    dataset.clear();
                }

                dataset.add(EmptyScreenDataFullScreen.getOffline());
                listAdapter.notifyDataSetChanged();


                swipeContainer.setRefreshing(false);


//                showToastMessage("Items: Network request failed. Please check your connection !");

            }
        });

    }




    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        makeRefreshNetworkCall();

        // End Search Mode
        searchQuery = null;

        // reset previous flag
//        resetPreviousPosition();

    }








    @Override
    public void notifyItemSelected() {

        if(getActivity() instanceof ToggleFab)
        {
            ((ToggleFab)getActivity()).showFab();
            show=true;
        }




        if(!listAdapter.getSelectedItems().isEmpty())
        {
            addRemoveButtons.setVisibility(View.VISIBLE);
        }
    }






    @Override
    public void notifyItemUnSelected() {

        if(listAdapter.getSelectedItems().isEmpty())
        {
            addRemoveButtons.setVisibility(View.GONE);
        }
    }




    @Override
    public boolean backPressed() {

        // reset previous flag
//        resetPreviousPosition();

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        // clear selected items
        listAdapter.selectedItems.clear();

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





    private void notifyItemHeaderChanged()
    {
        if(getActivity() instanceof NotifyIndicatorChanged)
        {
            ((NotifyIndicatorChanged) getActivity()).notifyItemIndicatorChanged(fetched_items_count + " out of " + item_count_item + " " + currentCategory.getCategoryName() + " Items");
        }
    }


    @Override
    public void notifySortChanged() {

        System.out.println("Notify Sort Clicked !");
        makeRefreshNetworkCall();
    }






    // display shop Item Status




    private void makeNetworkCallShopItem()
    {

        int currentShopID = PrefShopAdminHome.getShop(getContext()).getShopID();

//        Toast.makeText(getActivity(),"Shop ID : "  + String.valueOf(currentShopID),Toast.LENGTH_SHORT).show();

        if(currentCategory==null)
        {

            swipeContainer.setRefreshing(false);

            return;
        }

        Call<ShopItemEndPoint> call;


        if(searchQuery!=null)
        {
            /*call = shopItemService.getShopItemEndpoint(
                    null,
                    currentShopID,
                    null,null,null,null,null,null,null,null,null,null,null,null,
                    searchQuery,
                    null,null,null,
                    false,false
            );*/

            call = shopItemService.getShopItemsForShop(
                    null,
                    currentShopID,null,
                    searchQuery,
                    null,null,0
            );

        }
        else
        {

            /*call = shopItemService.getShopItemEndpoint(
                    currentCategory.getItemCategoryID(),
                    currentShopID,
                    null,null,null,null,null,null,null,null,null,null,null,null,
                    null,
                    null,null,null,
                    false,false
            );*/


            call = shopItemService.getShopItemsForShop(
                    currentCategory.getItemCategoryID(),
                    currentShopID,null,
                    null,
                    null,null,0
            );
        }



        call.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200 && response.body()!=null)
                {
                    listAdapter.shopItemMap.clear();

                    for(ShopItem shopItem: response.body().getResults())
                    {
                        listAdapter.shopItemMap.put(shopItem.getItemID(),shopItem);
                    }


                    // add this map into the temporary variable to save shopItems after rotation
                    shopItemMapTemp.putAll(listAdapter.shopItemMap);
                    listAdapter.notifyDataSetChanged();

                }
                else
                {
                    showToastMessage("Failed : " + response.code());
                }

            }




            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

//                showToastMessage("Network request failed. Please check your network !");
            }
        });
    }






    private void addSelectedToShopClick()
    {

        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        List<ShopItem> tempShopItemList = new ArrayList<>();

        for(Map.Entry<Integer, Item> entry : listAdapter.selectedItems.entrySet())
        {
//            entry.getValue().setItemCategoryID(parentCategory.getItemCategoryID());


            ShopItem shopItem = new ShopItem();
            shopItem.setShopID(PrefShopAdminHome.getShop(getContext()).getShopID());
            shopItem.setItemID(entry.getValue().getItemID());

            tempShopItemList.add(shopItem);
        }

        makeShopItemCreateBulkRequest(tempShopItemList);

    }





    private void makeShopItemCreateBulkRequest(List<ShopItem> tempShopItemList) {


        Call<ResponseBody> call = shopItemService.createShopItemBulk(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                tempShopItemList
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");

                    clearSelectedItems();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check data changes !");

                    clearSelectedItems();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }
                else if(response.code()==401 || response.code()==403)
                {
                    showToastMessage("Not Permitted !!");
                }
                else
                {
                    showToastMessage("Failed Code - " + response.code());
                }

                makeNetworkCallShopItem();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


//                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }






    private void removeSeletedShopItemClick(){



        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        List<ShopItem> tempShopItemList = new ArrayList<>();

        for(Map.Entry<Integer, Item> entry : listAdapter.selectedItems.entrySet())
        {
//            entry.getValue().setItemCategoryID(parentCategory.getItemCategoryID());


            ShopItem shopItem = new ShopItem();
            shopItem.setShopID(PrefShopAdminHome.getShop(getContext()).getShopID());
            shopItem.setItemID(entry.getValue().getItemID());

            tempShopItemList.add(shopItem);
        }

        makeShopItemDeleteBulkRequest(tempShopItemList);
    }




    private void makeShopItemDeleteBulkRequest(List<ShopItem> tempShopItemList) {

        Call<ResponseBody> call = shopItemService.deleteShopItemBulk(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                tempShopItemList
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");
                    clearSelectedItems();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check data changes !");

                    clearSelectedItems();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }
                else if(response.code()==401 || response.code()==403)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Failed Code : " + response.code());
                }

                makeNetworkCallShopItem();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }



    private void clearSelectedItems()
    {
        // clear the selected items
        listAdapter.selectedItems.clear();
    }










    @Override
    public void addItem() {

        String jsonString = UtilityFunctions.provideGson().toJson(currentCategory);

        Intent intent = new Intent(getActivity(), EditItem.class);
        intent.putExtra(EditItemFragmentNew.EDIT_MODE_INTENT_KEY, EditItemFragmentNew.MODE_ADD);
        intent.putExtra(EditItemFragmentNew.ITEM_CATEGORY_INTENT_KEY, jsonString);
        startActivity(intent);
    }








    @Override
    public void editItem(Item item) {


        PrefItem.saveItem(item, getActivity());
        Intent intentEdit = new Intent(getActivity(), EditItem.class);
        intentEdit.putExtra(EditItemFragmentNew.EDIT_MODE_INTENT_KEY, EditItemFragmentNew.MODE_UPDATE);
        startActivity(intentEdit);
    }









    @Override
    public void removeSelectedFromShop() {
//        showToastMessage("Remove Selected");

//        removeSeletedShopItemClick();
    }




    @Override
    public void addSelectedToShop() {

//        showToastMessage("Add Selected !");
//        addSelectedToShopClick();
    }





    @OnClick(R.id.add_to_shop)
    void addToShopButtonClick()
    {
        addSelectedToShopClick();
        addRemoveButtons.setVisibility(View.GONE);
    }




    @OnClick(R.id.remove_from_shop)
    void removeFromShopClick()
    {
        removeSeletedShopItemClick();
        addRemoveButtons.setVisibility(View.GONE);
    }


}
