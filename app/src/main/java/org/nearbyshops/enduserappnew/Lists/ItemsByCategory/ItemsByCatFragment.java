package org.nearbyshops.enduserappnew.Lists.ItemsByCategory;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.wunderlist.slidinglayer.SlidingLayer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.nearbyshops.enduserappnew.API.ItemService;
import org.nearbyshops.enduserappnew.multimarketfiles.Markets.MarketsList;
import org.nearbyshops.enduserappnew.Lists.ShopsAvailableNew.ShopsAvailable;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.LocationUpdated;
import org.nearbyshops.enduserappnew.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.ShowFragment;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model.Highlights;
import org.nearbyshops.enduserappnew.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.Models.FilterItemsInMarket;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.ViewHolderFilterItems;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItemCategorySmall;
import org.nearbyshops.enduserappnew.Interfaces.NotifyHeaderChanged;
import org.nearbyshops.enduserappnew.SlidingLayerSort.SlidingLayerSortItems_;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.multimarketfiles.SwitchMarketData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.multimarketfiles.ViewHolderSwitchMarket;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by sumeet on 2/12/16.
 */






public class ItemsByCatFragment extends Fragment implements
        LocationUpdated,
        SwipeRefreshLayout.OnRefreshListener,
        ViewHolderItemCategorySmall.ListItemClick,
        ViewHolderItemCategory.ListItemClick, NotifyBackPressed, NotifySort, NotifySearch ,
        ViewHolderEmptyScreenListItem.ListItemClick ,
        ViewHolderFilterItems.ListItemClick, ViewHolderItem.ListItemClick,
        ViewHolderSwitchMarket.ListItemClick{


    private static final String TAG_SLIDING = "tag_sliding_sort";
    private boolean isDestroyed = false;


    private int limit_item = 30;
    private int offset_item = 0;
    private int item_count_item;
//    private int fetched_items_count;


    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView itemCategoriesList;


    private ArrayList<Object> dataset = new ArrayList<>();
//    private ArrayList<ItemCategory> datasetCategory = new ArrayList<>();
//    private ArrayList<Item> datasetItems = new ArrayList<>();


    private GridLayoutManager layoutManager;
    private Adapter listAdapter;



//
//    @Inject
//    ItemService itemService;


    @Inject
    Gson gson;

    @BindView(R.id.shop_count_indicator) TextView itemHeader;
    @BindView(R.id.slidingLayer) SlidingLayer slidingLayer;



    private ItemCategory currentCategory = null;


    private static final int REQUEST_CODE_ASK_PERMISSION = 55;

    @BindView(R.id.empty_screen) LinearLayout emptyScreen;
    @BindView(R.id.progress_bar_fetching_location) LinearLayout progressBarFetchingLocation;


    @BindView(R.id.service_name) TextView serviceName;




    public ItemsByCatFragment() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        resetCurrentCategory();
    }




    void resetCurrentCategory()
    {
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
        View rootView = inflater.inflate(R.layout.fragment_item_categories_simple, container, false);
        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);



        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();
        notifyItemHeaderChanged();

        setMarketName();


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





    @OnClick(R.id.toolbar)
    void toolbarClicked()
    {

        if(getActivity() instanceof ShowFragment)
        {
            ((ShowFragment) getActivity()).showProfileFragment();
        }
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
                        .replace(R.id.slidinglayerfragment,new SlidingLayerSortItems_(),TAG_SLIDING)
                        .commit();
            }
        }

    }






    @OnClick({R.id.icon_sort, R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }





//    @OnClick({R.id.icon_filter,R.id.text_filter})
    void filterClick()
    {
//        Intent intent = new Intent(getActivity(), FilterItemsActivity.class);
//        intent.putExtra("ItemCatID",currentCategory.getItemCategoryID());
//        startActivityForResult(intent,123);
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

                    return (6/spanCount);

                }
                else if(dataset.get(position) instanceof Item)
                {

                    return 3;
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

//
//                    String log = "Dataset Size : " + String.valueOf(dataset.size()) + "\n"
//                            + "Last Visible Item Position : " + layoutManager.findLastVisibleItemPosition() + "\n"
//                            + "Previous Position : " + previous_position + "\n"
//                            + "Offset Item : " + offset_item + "\n"
//                            + "Limit Item : " + limit_item + "\n"
//                            + "Item Count Item : " + item_count_item;

//                    System.out.println(log);
//                    Log.d("log_scroll",log);




//                    if(layoutManager.findLastVisibleItemPosition()== previous_position)
//                    {
//                        return;
//                    }


                    // trigger fetch next page

                    if((offset_item + limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestItem(false);
                    }


//                    previous_position = layoutManager.findLastVisibleItemPosition();

                }
            }

        });

    }




    private int previous_position = -1;


    private void resetPreviousPosition()
    {
        previous_position = -1;
    }









    @Override
    public void onRefresh() {

        emptyScreen.setVisibility(View.GONE);
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
    public void onDestroyView() {
        super.onDestroyView();
//        isDestroyed = true;
    }




    @Override
    public void onResume() {
        super.onResume();
        isDestroyed=false;
        EventBus.getDefault().register(this);
    }




    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }












    @OnClick(R.id.button_try_again)
    void tryAgainClick()
    {
        makeRefreshNetworkCall();
    }







    private void makeRequestItem(boolean clearDataset)
    {

        if(clearDataset)
        {
            offset_item = 0;
        }



        String current_sort = "";

//        current_sort = PrefSortItemsByCategory.getSort(getActivity()) + " " + PrefSortItemsByCategory.getAscending(getActivity());

        current_sort= ViewHolderFilterItems.getSortString(getActivity());

        Call<ItemEndPoint> endPointCall = null;




        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



        endPointCall = retrofit.create(ItemService.class).getItemsEndpoint(currentCategory.getItemCategoryID(),
                null,clearDataset,
                PrefLocation.getLatitude(getActivity()), PrefLocation.getLongitude(getActivity()),
                null,
                null,null, null, searchQuery,
                current_sort, limit_item,offset_item,clearDataset,false);


//        if(searchQuery==null)
//        {
//            endPointCall = itemService.getItemsEndpoint(currentCategory.getItemCategoryID(),
//                    null,clearDataset,
//                    PrefLocation.getLatitude(getActivity()), PrefLocation.getLongitude(getActivity()),
//                    null,
//                    null,null, null, searchQuery,
//                    current_sort, limit_item,offset_item,clearDataset,false);
//
//        }
//        else
//        {
//
//            endPointCall = itemService.getItemsEndpoint(null,
//                    null,false,
//                    PrefLocation.getLatitude(getActivity()), PrefLocation.getLongitude(getActivity()),
//                    null,
//                    null,null, null, searchQuery,
//                    current_sort, limit_item,offset_item,clearDataset,false);
//
//        }






        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {



                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200 && response.body()!=null && response.body().getResults()!=null)
                {

                    if(clearDataset)
                    {
                        dataset.clear();
                        item_count_item = response.body().getItemCount();




                        int i = 0;

//                        dataset.add(i++, new SetLocationManually());


                        if(PrefGeneral.isMultiMarketEnabled(getContext()))
                        {
                            dataset.add(i++, new SwitchMarketData());
                        }


                        if(Highlights.getHighlightsItemsScreen(getActivity())!=null && getResources().getBoolean(R.bool.slider_item_enabled))
                        {
                            dataset.add(i++, Highlights.getHighlightsItemsScreen(getActivity()));
                        }




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




//                            if(currentCategory.getParentCategoryID()==-1 || response.body().getResults().size()==0)
//                            {
//                                dataset.addAll(response.body().getSubcategories());
//                            }
//                            else
//                            {
//
//                                ItemCategoriesList list = new ItemCategoriesList();
//                                list.setItemCategories(response.body().getSubcategories());
//
//                                dataset.add(list);
//
//                            }



                            ItemCategoriesList list = new ItemCategoriesList();
                            list.setItemCategories(response.body().getSubcategories());
                            list.setScrollPositionForSelected(currentCategory.getRt_scroll_position());


                            dataset.add(list);


                        }
                        else if(item_count_item==0)
                        {

                            if(searchQuery==null)
                            {

                                if(PrefGeneral.isMultiMarketEnabled(getActivity()))
                                {
//                                    dataset.add(new CreateShopData());
                                    dataset.add(EmptyScreenDataListItem.getEmptyScreenShopsListMultiMarket());
                                }
                                else
                                {
//                                    dataset.add(new CreateShopData());
                                    dataset.add(EmptyScreenDataListItem.getEmptyScreenShopsListSingleMarket());
                                }

                            }
                            else
                            {
                                dataset.add(EmptyScreenDataListItem.noSearchResults());
                            }

                        }






                        HeaderTitle headerItem = new HeaderTitle();
                        FilterItemsInMarket filterItems = new FilterItemsInMarket();




                        if(searchQuery==null)
                        {
                            if(response.body().getResults()!=null && response.body().getResults().size()>0)
                            {
                                headerItem.setHeading(currentCategory.getCategoryName() + " Items : " + item_count_item);
//                                filterItems.setHeaderText(currentCategory.getCategoryName() + " Items : " + item_count_item);

                                filterItems.setHeaderText(item_count_item + " " + currentCategory.getCategoryName() + " Items");
                            }
                            else
                            {
                                if(response.body().getSubcategories()!=null && response.body().getSubcategories().size()>0)
                                {
                                    headerItem.setHeading("No Items in this category");
                                    filterItems.setHeaderText("No Items in this category");
                                }
                            }


                        }
                        else
                        {
                            if(response.body().getResults().size()>0)
                            {
                                headerItem.setHeading("Search Results");
                                filterItems.setHeaderText("Search Results");
                            }
                            else
                            {
                                headerItem.setHeading("No items for the given search !");
                                filterItems.setHeaderText("No items for the given search !");
                            }
                        }




//                        dataset.add(headerItem);
                        dataset.add(filterItems);

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


                if (isDetached()) {
                    return;
                }


                if(isDestroyed)
                {
                    return;
                }



                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                listAdapter.notifyDataSetChanged();


                swipeContainer.setRefreshing(false);
//                showToastMessage("Items: Network request failed. Please check your connection !");

            }
        });

    }









    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory,int scrollPosition) {

        ItemCategory temp = currentCategory;
        temp.setRt_scroll_position(scrollPosition);

        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        makeRefreshNetworkCall();
        resetPreviousPosition();


        // End Search Mode
        searchQuery = null;
    }





    boolean backPressed = false;



    @Override
    public boolean backPressed() {

        // previous position is a variable used for tracking scrolling
        resetPreviousPosition();

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

        int fetched_count;


        if(offset_item==0)
        {
            if(item_count_item<limit_item)
            {
                fetched_count = item_count_item;
            }
            else
            {
                fetched_count = limit_item;
            }


        }
        else
        {
            fetched_count = offset_item + limit_item;

            if(fetched_count>item_count_item)
            {
                fetched_count = item_count_item;
            }

        }



        if(getActivity() instanceof NotifyHeaderChanged)
        {
            ((NotifyHeaderChanged) getActivity()).notifyItemHeaderChanged(fetched_count + " out of " + item_count_item + " " + currentCategory.getCategoryName() + " Items");
        }


        if(currentCategory.getItemCategoryID()==1)
        {
            itemHeader.setText(fetched_count + " out of " + item_count_item + " Items");
        }
        else
        {
            itemHeader.setText(fetched_count + " out of " + item_count_item + " " + currentCategory.getCategoryName());
        }
//        + " Items"

    }




    @Override
    public void notifySortChanged() {

        makeRefreshNetworkCall();
    }







    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }







    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(Location location) {

        makeRefreshNetworkCall();
    }






//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void permissionGranted(LocationPermissionGranted granted) {
//
////        showToastMessage("Granted event bus !");
////        requestLocationUpdates();
//    }















    @Override
    public void permissionGranted() {
//        showToastMessage("Granted interface !");
//        requestLocationUpdates();
    }






    @Override
    public void locationUpdated() {

        showToastMessage("Location Updated !");
        makeRefreshNetworkCall();
    }






    private String searchQuery = null;

    @Override
    public void search(final String searchString) {

//        showToastMessage("Query : " + searchString);
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {

//        showToastMessage("Search Collapsed !");
        searchQuery = null;
        makeRefreshNetworkCall();
    }







    @Override
    public void buttonClick(String url) {

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
    public void filterShopUpdated() {

        makeRefreshNetworkCall();
    }






    @Override
    public void listItemClick(Item item, int position) {

        Intent intent = new Intent(getActivity(), ShopsAvailable.class);
        intent.putExtra("item_id",item.getItemID());
        intent.putExtra("item_json",UtilityFunctions.provideGson().toJson(item));

        startActivity(intent);
    }




    @Override
    public void changeMarketClick() {
        Intent intent = new Intent(getActivity(), MarketsList.class);
        intent.putExtra("is_selection_mode",true);
        startActivityForResult(intent,3262);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3262 && resultCode ==3121)
        {
            resetCurrentCategory();
            setMarketName();
            makeRefreshNetworkCall();

        }
    }


}
