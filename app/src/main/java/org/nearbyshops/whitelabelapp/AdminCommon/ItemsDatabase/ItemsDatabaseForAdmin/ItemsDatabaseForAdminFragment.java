package org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForAdmin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.nearbyshops.whitelabelapp.API.ItemCategoryService;
import org.nearbyshops.whitelabelapp.API.ItemService;
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForAdmin.ViewHolders.ViewHolderItemAdmin;
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForAdmin.ViewHolders.ViewHolderItemCategoryAdmin;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItem.EditItem;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItem.EditItemFragmentNew;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItem.PrefItem;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItemCategory.EditItemCategory;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItemCategory.EditItemCategoryFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItemCategory.PrefItemCategory;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyFABClickAdmin;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyHeaderChanged;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Interfaces.ToggleFab;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.PreferencesSort.PrefSortItemsByCategoryAdmin;
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ChangeParent.ItemCategoriesParent;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

public class ItemsDatabaseForAdminFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderItemCategoryAdmin.ListItemClick, ViewHolderItemAdmin.ListItemClick,
        NotifyBackPressed, NotifySort, NotifyFABClickAdmin, NotifySearch {

    private boolean isDestroyed = false;
    private boolean show = true;


    private boolean isFirstChangeParent = true;




    private int limit_item = 10;
    private int offset_item = 0;
    private int item_count_item;
    private int fetched_items_count = 0;




    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView itemCategoriesList;

    private ArrayList<Object> dataset = new ArrayList<>();
//    private ArrayList<ItemCategory> datasetCategory = new ArrayList<>();
//    private ArrayList<Item> datasetItems = new ArrayList<>();




    private GridLayoutManager layoutManager;
    private Adapter listAdapter;

    private ItemCategory changeParentRequestedItemCat;
    private Item changeParentRequestedItem;

    @Inject
    ItemCategoryService itemCategoryService;


    @Inject
    ItemService itemService;


    private ItemCategory currentCategory = null;


    public ItemsDatabaseForAdminFragment() {

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
        View rootView = inflater.inflate(R.layout.fragment_items_database_for_admin, container, false);

        ButterKnife.bind(this,rootView);


        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();
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
                else if(dataset.get(position) instanceof ItemCategoriesList)
                {
                    return 6;
                }
                else if(dataset.get(position) instanceof Item)
                {

                    return 6;
                }
                else if(dataset.get(position) instanceof ViewHolderHeader.HeaderTitle)
                {
                    return 6;
                }

                return 3;
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

                /*if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset_item + limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestItem(false,false);
                    }

                }
*/



                if(offset_item + limit_item > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }


                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1+1)
                {

                    // trigger fetch next page

//                    if(layoutManager.findLastVisibleItemPosition()== previous_position)
//                    {
//                        return;
//                    }


                    // trigger fetch next page

                    if((offset_item+limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestItem(false);
                    }

//                    previous_position = layoutManager.findLastVisibleItemPosition();

                }
            }




            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if(dy > 20)
                {

                    boolean previous = show;

                    show = false ;

                    if(show!=previous)
                    {
                        // changed
                        Log.d("scrolllog","show");

                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).hideFab();
                        }
                    }

                }else if(dy < -20)
                {

                    boolean previous = show;

                    show = true;

                    if(show!=previous)
                    {
                        Log.d("scrolllog","hide");

                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).showFab();
                        }
                    }
                }


            }


        });

    }








    @Override
    public void onRefresh() {

        makeRequestItem(true);

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
    public void onDestroy() {
        super.onDestroy();


        if(mActionMode!=null)
        {
            mActionMode.finish();
        }
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
        UtilityFunctions.showToastMessage(getActivity(),message);
    }









    private void makeRequestItem(final boolean clearDataset)
    {


        if(clearDataset)
        {
            offset_item = 0;
        }



        String current_sort = "";
        current_sort = PrefSortItemsByCategoryAdmin.getSort(getContext()) + " " + PrefSortItemsByCategoryAdmin.getAscending(getContext());

        Call<ItemEndPoint> endPointCall = null;

        if(searchQuery==null)
        {
            endPointCall = itemService.getItemsOuterJoin(
                    currentCategory.getItemCategoryID(),
                    clearDataset,
                    null,
                    current_sort,
                    limit_item,offset_item,
                    clearDataset,false
            );
        }
        else
        {

            endPointCall = itemService.getItemsOuterJoin(
                    null,
                    clearDataset,
                    searchQuery,
                    current_sort,
                    limit_item,offset_item,
                    clearDataset,false
            );

        }





        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }



                if(response.code()==200)
                {

                    if(clearDataset)
                    {
                        dataset.clear();
                        item_count_item = response.body().getItemCount();



                        if(response.body().getSubcategories()!=null && response.body().getSubcategories().size()>0)
                        {


                            
                            ViewHolderHeader.HeaderTitle headerItemCategory = new ViewHolderHeader.HeaderTitle();

                            if (currentCategory.getParentCategoryID() == -1) {
                                headerItemCategory.setHeading("Item Categories");
                            } else {
                                headerItemCategory.setHeading(currentCategory.getCategoryName() + " Subcategories");
                            }

                            dataset.add(headerItemCategory);





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






                        ViewHolderHeader.HeaderTitle headerItem = new ViewHolderHeader.HeaderTitle();


                        if(response.body().getResults().size()>0)
                        {
                            headerItem.setHeading(currentCategory.getCategoryName() + " Items");
                        }
                        else
                        {
                            headerItem.setHeading("No Items in this category");
                        }



                        dataset.add(headerItem);

                    }



                    dataset.addAll(response.body().getResults());
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

                swipeContainer.setRefreshing(false);


            }
        });

    }








    @Override
    public void notifyDeleteItemCat(ItemCategory itemCategory, final int position) {

        Call<ResponseBody> call = itemCategoryService.deleteItemCategory(
                PrefLogin.getAuthorizationHeader(getActivity()),
                itemCategory.getItemCategoryID()
        );

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {

                    showToastMessage("Removed !");
                    dataset.remove(position);
                    listAdapter.notifyItemRemoved(position);

                }else if(response.code()==304)
                {
                    showToastMessage("Delete failed !");

                }else
                {
                    showToastMessage("Server Error !");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network request failed ! Please check your connection!");
            }
        });


    }





    @Override
    public void notifyDeleteItem(Item item, final int position) {

        Call<ResponseBody> call = itemService.deleteItem(
                PrefLogin.getAuthorizationHeader(getActivity()),
                item.getItemID()
        );


        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {
                    dataset.remove(position);
                    listAdapter.notifyItemRemoved(position);
                    showToastMessage("Removed !");

                }else if(response.code()==304)
                {
                    showToastMessage("Delete failed !");

                }else
                {
                    showToastMessage("Server Error !");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network request failed ! Please check your connection!");
            }
        });
    }






    @Override
    public void notifyItemCategorySelected() {

//        if(getActivity() instanceof ToggleFab)
//        {
//            ((ToggleFab)getActivity()).showFab();
//            show=true;
//        }


        activateActionMode();

    }

    @Override
    public void notifyItemSelected() {

//        if(getActivity() instanceof ToggleFab)
//        {
//            ((ToggleFab)getActivity()).showFab();
//            show=true;
//        }

        activateActionMode();
    }




    @Override
    public void detachItemCat(final ItemCategory itemCategory) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Detach Item Categories !")
                .setMessage("Are you sure you want to detach selected category ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemCategory.setParentCategoryID(-1);
                        makeRequestUpdateItemCat(itemCategory);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }






    @Override
    public void changeParentItemCat(ItemCategory itemCategory) {


        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);

//        requestedChangeParent = dataset.get(getLayoutPosition());

        changeParentRequestedItemCat = itemCategory;


        // add the selected item category in the exclude list so that it does not get showed up as an option.
        // This is required to prevent an item category to assign itself or its children as its parent.
        // This should not happen because it would be erratic.

        ItemCategoriesParent.clearExcludeList(); // it is a safe to clear the list before adding any items in it.
        ItemCategoriesParent.excludeList
                .put(itemCategory.getItemCategoryID(),itemCategory);

        startActivityForResult(intentParent,1,null);

    }







    @Override
    public void detachItem(final Item item) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Detach Item Categories !")
                .setMessage("Are you sure you want to detach selected category ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item.setItemCategoryID(-1);
//                        makeRequestUpdate(itemCategory);
                        makeUpdateRequestItem(item);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }


    @Override
    public void changeParentItem(Item item) {

        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);

        changeParentRequestedItem = item;

//        requestedChangeParent = dataset.get(getLayoutPosition());

        // add the selected item category in the exclude list so that it does not get showed up as an option.
        // This is required to prevent an item category to assign itself or its children as its parent.
        // This should not happen because it would be erratic.

        ItemCategoriesParent.clearExcludeList(); // it is a safe to clear the list before adding any items in it.

//                    ItemCategoriesParent.excludeList
//                            .put(requestedChangeParent.getItemID(),requestedChangeParent);

        startActivityForResult(intentParent,3,null);

    }


    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);
        makeRefreshNetworkCall();
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








    private void notifyItemHeaderChanged()
    {
        if(getActivity() instanceof NotifyHeaderChanged)
        {
            ((NotifyHeaderChanged) getActivity()).notifyItemHeaderChanged(fetched_items_count + " out of " + item_count_item + " " + currentCategory.getCategoryName() + " Items");
        }
    }


    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }





    @Override
    public void detachSelectedClick() {
        detachedSelectedDialog();
    }







    private void detachedSelectedDialog()
    {

        if(listAdapter.selectedItems.size()==0&& listAdapter.selectedItemCategories.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Detach Item Categories !")
                .setMessage("Are you sure you want to remove / detach parent for the selected Categories ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        detachSelected();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }






    private void detachSelected()
    {

        /*if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }*/

        List<Item> tempList = new ArrayList<>();

        for(Map.Entry<Integer,Item> entry : listAdapter.selectedItems.entrySet())
        {
            entry.getValue().setItemCategoryID(-1);
            tempList.add(entry.getValue());
        }

//        makeRequestUpdateBulk(tempList);
        makeUpdateRequestItemsBulk(tempList);



        List<ItemCategory> tempListItemCat = new ArrayList<>();

        for(Map.Entry<Integer,ItemCategory> entry : listAdapter.selectedItemCategories.entrySet())
        {
            entry.getValue().setParentCategoryID(-1);
            tempListItemCat.add(entry.getValue());
        }

        makeRequestUpdateItemCatBulk(tempListItemCat);

    }



    @Override
    public void changeParentForSelected() {
//        showToastMessage("Change Parent");
        changeParentBulk();
    }




    @Override
    public void addItem() {
//        showToastMessage("add item");

        Intent intent = new Intent(getActivity(), EditItem.class);
        intent.putExtra(EditItemFragmentNew.EDIT_MODE_INTENT_KEY, EditItemFragmentNew.MODE_ADD);
//        intent.putExtra(EditItemFragmentNew.ITEM_CATEGORY_INTENT_KEY,currentCategory);

        String jsonString = UtilityFunctions.provideGson().toJson(currentCategory);
        intent.putExtra(EditItemFragmentNew.ITEM_CATEGORY_INTENT_KEY,jsonString);


        startActivity(intent);
    }




    @Override
    public void addItemCategory() {


        Intent intent = new Intent(getActivity(), EditItemCategory.class);

        String jsonString = UtilityFunctions.provideGson().toJson(currentCategory);
        intent.putExtra(EditItemCategoryFragment.ITEM_CATEGORY_INTENT_KEY,jsonString);

        intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY,EditItemCategoryFragment.MODE_ADD);
        startActivity(intent);
    }



    @Override
    public void addfromGlobal() {

//        Intent intent  = new Intent(getActivity(), AddFromGlobal.class);
//        intent.putExtra(AddFromGlobal.INTENT_KEY_ITEM_CAT_PARENT,currentCategory);
//        startActivity(intent);


    }





    private void changeParentBulk()
    {

        if(listAdapter.selectedItemCategories.size()==0 && listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        // make an exclude list. Put selected items to an exclude list. This is done to preven a category to make itself or its
        // children its parent. This is logically incorrect and should not happen.

        ItemCategoriesParent.clearExcludeList();
        ItemCategoriesParent.excludeList.putAll(listAdapter.selectedItemCategories);

        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);
        startActivityForResult(intentParent,2,null);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if(requestCode == 3)
        {
            if(resultCode == Activity.RESULT_OK)
            {
//                ItemCategory parentCategory = data.getParcelableExtra("result");

                String jsonString = data.getStringExtra("result");
                ItemCategory parentCategory = UtilityFunctions.provideGson().fromJson(jsonString,ItemCategory.class);

                if(parentCategory!=null)
                {


                    if(parentCategory.isAbstractNode())
                    {
                        showToastMessage(parentCategory.getCategoryName()
                                + " is an abstract category you cannot add item to an abstract category");
                        return;
                    }

//                    listAdapter.getRequestedChangeParent().setParentCategoryID(parentCategory.getItemCategoryID());

//                    listAdapter.getRequestedChangeParent().setItemCategoryID(parentCategory.getItemCategoryID());
//                    makeUpdateRequest(listAdapter.getRequestedChangeParent());


                    changeParentRequestedItem.setItemCategoryID(parentCategory.getItemCategoryID());
                    makeUpdateRequestItem(changeParentRequestedItem);


                }
            }
        }
        else if(requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
//                ItemCategory parentCategory = data.getParcelableExtra("result");

                String jsonString = data.getStringExtra("result");
                ItemCategory parentCategory = UtilityFunctions.provideGson().fromJson(jsonString,ItemCategory.class);

                if(parentCategory!=null)
                {

                    changeParentRequestedItemCat.setParentCategoryID(parentCategory.getItemCategoryID());
                    makeRequestUpdateItemCat(changeParentRequestedItemCat);

                }
            }
        }
        else if(requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
//                ItemCategory parentCategory = data.getParcelableExtra("result");

                String jsonString = data.getStringExtra("result");
                ItemCategory parentCategory = UtilityFunctions.provideGson().fromJson(jsonString,ItemCategory.class);

                if(parentCategory!=null)
                {

                    // update Item Categories
                    List<ItemCategory> tempList = new ArrayList<>();

                    for(Map.Entry<Integer,ItemCategory> entry : listAdapter.selectedItemCategories.entrySet())
                    {
                        entry.getValue().setParentCategoryID(parentCategory.getItemCategoryID());
                        tempList.add(entry.getValue());
                    }

                    makeRequestUpdateItemCatBulk(tempList);




                    // update Items
                    if(parentCategory.isAbstractNode())
                    {
                        showToastMessage(parentCategory.getCategoryName()
                                + " is an abstract category you cannot add item to an abstract category");

                        return;
                    }

                    List<Item> tempListItems = new ArrayList<>();

                    for(Map.Entry<Integer,Item> entry : listAdapter.selectedItems.entrySet())
                    {
                        entry.getValue().setItemCategoryID(parentCategory.getItemCategoryID());
                        tempListItems.add(entry.getValue());
                    }

                    makeUpdateRequestItemsBulk(tempListItems);

                }


            }
        }

    }// on activity result ends




    private void makeRequestUpdateItemCat(ItemCategory itemCategory)
    {
        Call<ResponseBody> call = itemCategoryService.changeParent(PrefLogin.getAuthorizationHeader(getContext()),
                itemCategory,itemCategory.getItemCategoryID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200)
                {
                    showToastMessage("Successful !");

                    makeRefreshNetworkCall();

                }else
                {
                    showToastMessage("Failed Code : " + response.code());
                }

//                listAdapter.setRequestedChangeParent(null);

                changeParentRequestedItemCat=null;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network request failed. Please check your connection !");

//                listAdapter.setRequestedChangeParent(null);
                changeParentRequestedItemCat=null;

            }
        });
    }







    private void makeRequestUpdateItemCatBulk(final List<ItemCategory> list)
    {
        Call<ResponseBody> call = itemCategoryService.changeParentBulk(PrefLogin.getAuthorizationHeader(getActivity()),
                list);


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

                    clearSelectedItemCat();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check for data changes !");

                    clearSelectedItemCat();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }


                /*dataset.clear();
                offset = 0 ; // reset the offset
                makeRequestRetrofit(false);*/



                if(isFirstChangeParent)
                {
                    isFirstChangeParent = false;
                }
                else
                {
                    // is last
                    makeRefreshNetworkCall();
                    isFirstChangeParent = true;// reset the flag
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Request failed. Check your internet / network connection !");

                if(isFirstChangeParent)
                {
                    isFirstChangeParent = false;
                }
                else
                {
                    // is last

                    makeRefreshNetworkCall();
                    isFirstChangeParent = true;// reset the flag
                }

            }
        });

    }







    private void clearSelectedItemCat()
    {
        // clear the selected items
        listAdapter.selectedItemCategories.clear();
    }









    private void makeUpdateRequestItem(Item item)
    {

//        Call<ResponseBody> call2 = itemCategoryService.updateItemCategory(itemCategory,itemCategory.getItemCategoryID());

        Call<ResponseBody> call = itemService.changeParent(PrefLogin.getAuthorizationHeader(getContext()),
                item,item.getItemID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showToastMessage("Change Parent Successful !");

                    onRefresh();

                }else
                {
                    showToastMessage("Change Parent Failed !");
                }

//                listAdapter.setRequestedChangeParent(null);
                changeParentRequestedItem=null;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");

//                listAdapter.setRequestedChangeParent(null);
                changeParentRequestedItem=null;

            }
        });

    }











    private void makeUpdateRequestItemsBulk(final List<Item> list)
    {
//        Call<ResponseBody> call = itemService.updateItemCategoryBulk(list);

        Call<ResponseBody> call = itemService.changeParentBulk(PrefLogin.getAuthorizationHeader(getContext()),
                list);
//        Call<ResponseBody> call = null;
//
//
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

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }


//                makeRequestRetrofit();



                if(isFirstChangeParent)
                {
                    isFirstChangeParent = false;
                }
                else
                {
                    // is last
                    makeRefreshNetworkCall();
                    isFirstChangeParent = true;// reset the flag
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Network Request failed. Check your internet / network connection !");

                if(isFirstChangeParent)
                {
                    isFirstChangeParent = false;
                }
                else
                {
                    // is last

                    makeRefreshNetworkCall();
                    isFirstChangeParent = true;// reset the flag
                }


            }
        });

    }






    private void clearSelectedItems()
    {
        // clear the selected items
        listAdapter.selectedItems.clear();
    }





    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.cab_menu_items_database, menu);

            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:

//                    shareCurrentItem();
//                    showToastMessage("Clicked !");


//
//                    Gson gson = new Gson();
//                    String jsonString = gson.toJson(listAdapter.selectedItemSingle);
//
//                    Intent intent = new Intent(getActivity(), EditCityLocation.class);
//                    intent.putExtra("location_instance",jsonString);
//                    intent.putExtra(FragmentEditService.EDIT_MODE_INTENT_KEY, FragmentEditCityLocation.MODE_UPDATE);
//                    startActivity(intent);



                    if(listAdapter.selectedItems.size()==1 && listAdapter.selectedItemCategories.size()==0)
                    {
                        Item selectedItem = null;

                        for(Item i : listAdapter.selectedItems.values())
                        {
                            selectedItem = i;
                        }


                        if(selectedItem!=null)
                        {


                            PrefItem.saveItem(selectedItem, getActivity());
                            Intent intentEdit = new Intent(getActivity(), EditItem.class);
                            intentEdit.putExtra(EditItemFragmentNew.EDIT_MODE_INTENT_KEY, EditItemFragmentNew.MODE_UPDATE);
                            startActivity(intentEdit);
                        }



                    }
                    else if (listAdapter.selectedItems.size()==0 && listAdapter.selectedItemCategories.size()==1)
                    {
                        ItemCategory selectedCat = null;

                        for(ItemCategory itemCat : listAdapter.selectedItemCategories.values())
                        {
                            selectedCat = itemCat;
                        }



                        if(selectedCat!=null)
                        {
                            PrefItemCategory.saveItemCategory(selectedCat,getActivity());
                            Intent intent = new Intent(getActivity(),EditItemCategory.class);
                            intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY,EditItemCategoryFragment.MODE_UPDATE);
                            startActivity(intent);

                        }

                    }



                    mode.finish(); // Action picked, so close the CAB

                    return true;

                case R.id.action_change_parent:


                    changeParentBulk();


                    return true;

//                case R.id.action_delete:

//                    deleteClick();

//                    mode.finish();


//                    return true;
                default:
                    return false;
            }


        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            listAdapter.selectedItemCategories.clear();
            listAdapter.selectedItems.clear();
            listAdapter.notifyDataSetChanged();
        }
    };



    // activate action mode
    private void activateActionMode()
    {
        if(mActionMode!=null) {

            int size = listAdapter.selectedItems.size();
            int sizeItemCat = listAdapter.selectedItemCategories.size();

            mActionMode.setTitle(String.valueOf(size + sizeItemCat));

            int sizeTotal = size + sizeItemCat;

            if (sizeTotal == 0) {
                mActionMode.finish();
            }
            else if(sizeTotal==1)
            {
                mActionMode.getMenu().findItem(R.id.action_edit).setVisible(true);

            }
            else if(sizeTotal > 1)
            {
                mActionMode.getMenu().findItem(R.id.action_edit).setVisible(false);

            }


        }
        else
        {
            mActionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(mActionModeCallback);
            mActionMode.setTitle(String.valueOf(listAdapter.selectedItemCategories.size() + listAdapter.selectedItems.size()));
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



}
