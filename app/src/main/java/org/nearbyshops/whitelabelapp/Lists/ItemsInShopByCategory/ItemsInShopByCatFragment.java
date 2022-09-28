package org.nearbyshops.whitelabelapp.Lists.ItemsInShopByCategory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.API.CartItemService;
import org.nearbyshops.whitelabelapp.API.CartStatsService;
import org.nearbyshops.whitelabelapp.API.ItemCategoryService;
import org.nearbyshops.whitelabelapp.API.ShopItemService;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailShopItem.ShopItemDetail;
import org.nearbyshops.whitelabelapp.Interfaces.ShowFragment;
import org.nearbyshops.whitelabelapp.CartAndOrder.CartItemList.CartItemList;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.CartItem;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.CartStats;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailItem.ItemDetailFragment;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightsBuilder;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterItemsInShop;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.ViewHolderFilterItemsInShop;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.whitelabelapp.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderItemCategorySmall;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop.ViewHolderShopInfo;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShopItem.ViewHolderShopItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShopItem.ViewHolderShopItemButton;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShopItem.ViewHolderShopItemInstacart;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop.ViewHolderShopMedium;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopHome;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop.ViewHolderShopSmall;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderWhatsApp;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderFullScreenProgressBar;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelShop;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by sumeet on 2/12/16.
 */



public class ItemsInShopByCatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderItemCategorySmall.ListItemClick, ViewHolderShopItemButton.ListItemClick,
        ViewHolderItemCategory.ListItemClick, ViewHolderShopItemInstacart.ListItemClick,
        ViewHolderShopItem.ListItemClick,
        NotifyBackPressed, NotifySort, NotifySearch,
        ViewHolderShopMedium.ListItemClick,  ViewHolderShopSmall.ListItemClick,
        ViewHolderFilterItemsInShop.ListItemClick , ViewHolderShopInfo.ListItemClick {



    public static final String SCREEN_MODE_INTENT_KEY = "screen_mode_key";


    public static final int MODE_ITEMS_IN_SHOP_SINGLE_VENDOR = 54;
    public static final int MODE_SHOP_HOME = 55;
    public static final int MODE_ITEMS_IN_SHOP_MULTI_VENDOR = 56;


    private int current_mode;
    private boolean shopEnabled = true;

//    int shopID = 0;




    @BindView(R.id.shop_profile_photo) ImageView itemImage;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_address) TextView shopAddress;

    @BindView(R.id.toolbar) Toolbar toolbar;




    private boolean isDestroyed = false;
    boolean show = true;

    int item_count_item_category = 0;

    private int limit_item = 30;
    private int offset_item = 0;
    private int item_count_item;
    private int fetched_items_count = 0;



    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView itemCategoriesList;



//    @BindView(R.id.cart_stats)
//    CardView cartStatsBlock;

    private ArrayList<Object> dataset = new ArrayList<>();


    @BindView(R.id.itemsInCart) public TextView itemsInCart;
    @BindView(R.id.cartTotal) public TextView cartTotal;


    private GridLayoutManager layoutManager;
    private Adapter listAdapter;

    private ViewModelShop viewModelShop;
    private ProgressDialog progressDialog;





    @Inject
    ItemCategoryService itemCategoryService;

    @Inject
    ShopItemService shopItemService;


    private ItemCategory currentCategory = null;




    public ItemsInShopByCatFragment() {
        super();
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
//        currentCategory.setItemCategoryID(4);
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);

    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_items_in_shop_by_cat_backup, container, false);

        ButterKnife.bind(this,rootView);


        if(getActivity()!=null)
        {
            shopEnabled = getActivity().getIntent().getBooleanExtra("shop_enabled",true);
        }


        if (getArguments() != null) {

            current_mode = getArguments().getInt(SCREEN_MODE_INTENT_KEY,MODE_ITEMS_IN_SHOP_MULTI_VENDOR);
//            shopEnabled = getArguments().getBoolean("shop_enabled",true);
        }



        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        Shop shop = PrefShopHome.getShop(getActivity());


        if(((AppCompatActivity) getActivity()).getSupportActionBar()!=null && shop!=null)
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(shop.getShopName());
        }






        setupRecyclerView();
        setupSwipeContainer();
        setupShop();





        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }



//        if(shop==null)
//        {
            setupViewModel();
            getShopDetails();
//        }









        notifyItemIndicatorChanged();

        return rootView;
    }






    public static ItemsInShopByCatFragment newInstance(boolean singleVendorEnabled,int screenMode) {

        ItemsInShopByCatFragment fragment = new ItemsInShopByCatFragment();
        Bundle args = new Bundle();
        args.putBoolean("single_vendor_enabled",singleVendorEnabled);
        args.putInt(ItemsInShopByCatFragment.SCREEN_MODE_INTENT_KEY,screenMode);
        fragment.setArguments(args);

        return fragment;
    }




    public static ItemsInShopByCatFragment newInstance(boolean singleVendorEnabled) {

        ItemsInShopByCatFragment fragment = new ItemsInShopByCatFragment();
        Bundle args = new Bundle();
        args.putBoolean("single_vendor_enabled",singleVendorEnabled);
        fragment.setArguments(args);

        return fragment;
    }








    private void setupShop()
    {

        boolean singleVendorEnabled = getArguments().getBoolean("single_vendor_enabled");


//        if(singleVendorEnabled)
//        {
//            cartStatsBlock.setVisibility(View.GONE);
//            swipeContainer.setPadding(15,0,15,0);
//        }
//        else
//        {
//            cartStatsBlock.setVisibility(View.VISIBLE);
////            swipeContainer.setPadding(5,0,5,95);
//        }



        Shop shop = PrefShopHome.getShop(getActivity());

        if(shop==null)
        {
            return;
        }



        shopName.setText(shop.getShopName());

        if(getResources().getBoolean(R.bool.single_vendor_mode_enabled))
        {
            shopAddress.setVisibility(View.GONE);
            itemImage.setVisibility(View.GONE);
        }
        else
        {
            shopAddress.setText(String.format("%.2f Km",shop.getRt_distance()) + " | " + shop.getShopAddress());
        }



        String imagePath = PrefGeneral.getServerURL(getActivity()) + "/api/v1/Shop/Image/five_hundred_"
                + shop.getLogoImagePath() + ".jpg";


        Drawable placeholder = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_nature_people_white_48px, getActivity().getTheme());


        Picasso.get().load(imagePath)
                .placeholder(placeholder)
                .into(itemImage);



        if(getResources().getBoolean(R.bool.single_vendor_mode_enabled))
        {
            itemImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        else
        {
            itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }


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



//        itemCategoriesList.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));



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
                    return 3;
                }
                else if(dataset.get(position) instanceof ShopItem)
                {

                    int layoutType = ViewHolderFilterItemsInShop.getLayoutType(getActivity());

                    if(layoutType==ViewHolderFilterItemsInShop.LAYOUT_TYPE_FULL_WIDTH)
                    {
                        return 6;
                    }
                    else if(layoutType==ViewHolderFilterItemsInShop.LAYOUT_TYPE_HALF_WIDTH)
                    {
                        return 3;
                    }

                }
                else if(dataset.get(position) instanceof ViewHolderHeader.HeaderTitle)
                {
                    return 6;
                }

                return 6;
            }
        });




//        final DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);





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
                        makeRequestShopItem(false,false);
                    }

                }
            }

        });

    }










    @Override
    public void onRefresh() {

        if(item_count_item==0)
        {
            dataset.clear();
            dataset.add(new ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData());
            listAdapter.notifyDataSetChanged();
        }

        makeRequestShopItem(true,true);
        getCartItemAvailability(true);
        fetchCartStats();
    }





    private void makeRefreshNetworkCall()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);

            }
        });

        onRefresh();
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





    private void makeRequestShopItem(final boolean clearDataset, boolean resetOffset)
    {

        if(resetOffset)
        {
            offset_item = 0;
        }



        String current_sort = "";

//        current_sort = PrefSortItemsInShop.getSort(getContext()) + " " + PrefSortItemsInShop.getAscending(getContext());
        current_sort= ViewHolderFilterItemsInShop.getSortString(getActivity());


        Call<ShopItemEndPoint> endPointCall = null;

        Shop currentShop = PrefShopHome.getShop(getContext());

        if(currentShop==null)
        {
            swipeContainer.setRefreshing(false);
            return;
        }


        boolean filterRecursively = true;

        if(current_mode==MODE_SHOP_HOME)
        {
            filterRecursively=false;
        }


        if(searchQuery==null)
        {

            endPointCall = shopItemService.getShopItemsByShop(
                    currentCategory.getItemCategoryID(),
                    filterRecursively,
                    clearDataset,
                    currentShop.getShopID(),
                    null,
                    null,null,
                    null,null,null,null,
                    null,null,
                    null,
                    shopEnabled,true,
                    current_sort,
                    limit_item,offset_item,clearDataset,
                    false
            );


        }
        else
        {

            endPointCall = shopItemService.getShopItemsByShop(
                    null,
                    filterRecursively,
                    clearDataset,
                    currentShop.getShopID(),
                    null,
                    null,null,
                    null,null,
                    null,null,null,null,
                    searchQuery,
                    shopEnabled,true,
                    current_sort,
                    limit_item, offset_item, clearDataset,
                    false
            );

        }





//        if(searchQuery==null)
//        {
//
//
//            endPointCall = shopItemService.getShopItemEndpoint(
//                    currentCategory.getItemCategoryID(),
//                    true,
//                    clearDataset,
//                    currentShop.getShopID(),
//                    null,
//                    null,null,
//                    null,null,null,null,
//                    null,null,
//                    null,null,null,
//                    null,true,current_sort,
//                    limit_item,offset_item,clearDataset,
//                    false);
//
//        }
//        else
//        {
//
//
//
//            endPointCall = shopItemService.getShopItemEndpoint(
//                    null,
//                    true,
//                    clearDataset,
//                    currentShop.getShopID(),
//                    null,
//                    null,null,
//                    null,null,
//                    null,null,null,null,
//                    null,null,null,
//                    searchQuery,
//                    true,current_sort,
//                    limit_item,offset_item,clearDataset,
//                    false);
//        }



        endPointCall.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


                if (isDetached()) {
                    return;
                }



                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {



                    if(clearDataset)
                    {
                        dataset.clear();



                        int i = 0;


//                        && current_mode==MODE_SHOP_HOME



                        if(HighlightsBuilder.getHighlightsFrontScreen(getActivity())!=null
                                && getResources().getBoolean(R.bool.banner_enabled)
                                && getResources().getBoolean(R.bool.single_vendor_mode_enabled)
                                && currentCategory.getParentCategoryID()==-1
                        )
                        {
                            dataset.add(HighlightsBuilder.getHighlightsFrontScreen(getActivity()));
                        }




//                        if(!getResources().getBoolean(R.bool.single_vendor_mode_enabled))
//                        {

//                        }

//                        dataset.add(PrefShopHome.getShop(getActivity()));



                        if(getResources().getBoolean(R.bool.show_whatsapp))
                        {
                            dataset.add(new ViewHolderWhatsApp.WhatsMessageData(PrefShopHome.getShop(getActivity())));
                        }


                        if(response.body()!=null)
                        {

                            item_count_item = response.body().getItemCount();
                            fetched_items_count = dataset.size();


                            if(response.body().getSubcategories()!=null && response.body().getSubcategories().size()>0)
                            {


                                if (searchQuery == null) {

                                    ViewHolderHeader.HeaderTitle headerItemCategory = new ViewHolderHeader.HeaderTitle();

                                    if (currentCategory.getParentCategoryID() == -1) {
                                        headerItemCategory.setHeading("Item Categories");
                                    } else {
                                        headerItemCategory.setHeading(currentCategory.getCategoryName() + " Subcategories");
                                    }

                                    dataset.add(headerItemCategory);
                                }




                                if(current_mode==MODE_SHOP_HOME)
                                {
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
                                else
                                {

                                        ItemCategoriesList list = new ItemCategoriesList();
                                        list.setItemCategories(response.body().getSubcategories());
                                        list.setScrollPositionForSelected(currentCategory.getRt_scroll_position());
                                        dataset.add(list);
                                }
                            }





                            ViewHolderHeader.HeaderTitle headerItem = new ViewHolderHeader.HeaderTitle();
                            FilterItemsInShop filterData = new FilterItemsInShop();



                            if(searchQuery==null)
                            {
                                if(response.body().getResults()!=null && response.body().getResults().size()>0)
                                {
//                                    headerItem.setHeading(currentCategory.getCategoryName() + " Items : " + item_count_item);
//                                    headerItem.setHeading(item_count_item + " " + currentCategory.getCategoryName() + " Items");

//                                    filterData.setHeaderText(currentCategory.getCategoryName() + " Items : " + item_count_item);
                                    filterData.setHeaderText(item_count_item + " " + currentCategory.getCategoryName() + " Items");
                                }
                                else
                                {
                                    headerItem.setHeading("No Items in this category");

                                    filterData.setHeaderText("No Items in this category");
                                    filterData.setHideFilters(true);
                                }


                            }
                            else
                            {
                                if(response.body().getResults() !=null && response.body().getResults().size()>0)
                                {
                                    headerItem.setHeading("Search Results");
                                    filterData.setHeaderText("Search Results");
                                }
                                else
                                {
                                    headerItem.setHeading("No items for the given search !");
                                    filterData.setHeaderText("No items for the given search !");
                                    filterData.setHideFilters(true);

//                                    dataset.add(EmptyScreenDataListItem.noSearchResults());

                                }
                            }



//                            dataset.add(headerItem);
                            dataset.add(filterData);


                        }

                    }




                    if (response.body() != null && response.body().getResults()!=null) {

                        dataset.addAll(response.body().getResults());
                        fetched_items_count = fetched_items_count + response.body().getResults().size();
                    }



                }
                else
                {
                    showToastMessage("Failed : code : " + response.code());
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
                notifyItemIndicatorChanged();
            }

            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {


                if (isDetached()) {
                    return;
                }


                if(isDestroyed)
                {
                    return;
                }



                swipeContainer.setRefreshing(false);
                showToastMessage("Items: Network request failed. Please check your connection !");

            }
        });

    }







    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory, int scrollPosition) {

        ItemCategory temp = currentCategory;
        temp.setRt_scroll_position(scrollPosition);

        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        makeRefreshNetworkCall();

        // End Search Mode
        searchQuery = null;
    }



    @Override
    public boolean backPressed() {

        // reset previous flag

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






//    @BindView(R.id.text_sub) TextView itemHeader;






    private void notifyItemIndicatorChanged()
    {
//        itemHeader.setText(fetched_items_count + " out of " + item_count_item + " " + currentCategory.getCategoryName() + " Items");
    }






    @Override
    public void notifySortChanged() {

//        System.out.println("Notify Sort Clicked !");
        makeRefreshNetworkCall();
    }



    // display shop Item Status








    @Override
    public void notifyItemImageClick(Item item) {

//        showToastMessage("Item Image Click !");


        Shop currentShop = PrefShopHome.getShop(getContext());


        Intent intent = new Intent(getActivity(), ShopItemDetail.class);
        intent.putExtra("item_id",item.getItemID());
        intent.putExtra("shop_id",currentShop.getShopID());


        String itemJson = UtilityFunctions.provideGson().toJson(item);
        intent.putExtra(ItemDetailFragment.TAG_JSON_STRING,itemJson);
        startActivityForResult(intent,5676);

    }










    @Override
    public void showLogin() {
        checkLogin();
    }


    void checkLogin()
    {


        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent,123);
    }





    @Override
    public void cartUpdated() {


        if(listAdapter.cartStats.getCartID()==0)
        {
            System.out.println("Cart ID Zero !");
        }


        fetchCartStats();
    }






    @Override
    public void setCartTotal(double cartTotalValue, boolean save) {

//        PrefGeneral.getCurrencySymbol(getActivity()
//        PrefShopHome.getCurrencySymbolForShop(getActivity())

        cartTotal.setText(PrefCurrency.getCurrencySymbol(getActivity()) + " " + UtilityFunctions.refinedString(cartTotalValue));


        if(save)
        {
            listAdapter.cartStats.setCart_Total(cartTotalValue);
        }
    }






    @Override
    public void setItemsInCart(int itemsInCartValue, boolean save) {

        itemsInCart.setText(itemsInCartValue + " " + "Items");

        if(save)
        {
            listAdapter.cartStats.setItemsInCart(itemsInCartValue);
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==123 && resultCode == RESULT_OK)
        {
            // login success
            getCartItemAvailability(true);
            fetchCartStats();

        }
        else if(requestCode==150)
        {
            getCartItemAvailability(true);
            fetchCartStats();
        }
        else if(requestCode==5676 && resultCode==5679)
        {
            getCartItemAvailability(true);
            fetchCartStats();
        }

    }






    @Override
    public void listItemClick(Shop shop, int position) {

//        Intent intent = new Intent(getActivity(), ShopDetail.class);
//        intent.putExtra("shop_id",shop.getShopID());

//        String jsonString = UtilityFunctions.provideGson().toJson(shop);
//        intent.putExtra(ShopDetailFragment.TAG_JSON_STRING,jsonString);

//        startActivity(intent);
    }

    @Override
    public void editClick(Shop shop, int position) {

    }





    @OnClick({R.id.app_bar,R.id.collapsing_toolbar,R.id.toolbar})
    void shopClick()
    {

//        Shop shop = PrefShopHome.getShop(getActivity());
//        Intent intent = new Intent(getActivity(), ShopDetail.class);
//        intent.putExtra("shop_id",shop.getShopID());

//        String jsonString = UtilityFunctions.provideGson().toJson(shop);
//        intent.putExtra(ShopDetailFragment.TAG_JSON_STRING,jsonString);

//        startActivity(intent);

    }




    @Override
    public void shopInfoClick() {
        shopClick();
    }




    @OnClick({R.id.cart_stats})
    void viewCartClick()
    {

        if(getResources().getBoolean(R.bool.single_vendor_mode_enabled))
        {
            if(getActivity() instanceof ShowFragment)
            {
                ((ShowFragment) getActivity()).showCartFragment();
            }

        }
        else
        {
            User user = PrefLogin.getUser(getActivity());

            if(user==null)
            {
                checkLogin();
                return;
            }



            Intent intent = new Intent(getActivity(), CartItemList.class);
            intent.putExtra("shop_id",PrefShopHome.getShop(getActivity()).getShopID());
            intent.putExtra("shop_name",PrefShopHome.getShop(getActivity()).getShopName());

            startActivityForResult(intent,150);

        }


    }











    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;






    public void getCartItemAvailability(final boolean notifyDatasetChanged)
    {



        Shop shop = PrefShopHome.getShop(getActivity());

        if(shop == null)
        {
            return;
        }

        listAdapter.cartItemMap.clear();
//        listAdapter.cartStatsMap.clear();



        User endUser = PrefLogin.getUser(getActivity());

        if(endUser == null)
        {

            listAdapter.cartStats.setItemsInCart(0);
            listAdapter.cartStats.setCart_Total(0);
            listAdapter.cartStats.setShopID(shop.getShopID());

            setItemsInCart(0,false);
            setCartTotal(0,false);

            return;
        }


//        showToastMessage("Fetch Availibitlity !");


        Call<List<CartItem>> cartItemCall = cartItemService.getCartItemAvailability(endUser.getUserID(),shop.getShopID());


        cartItemCall.enqueue(new Callback<List<CartItem>>() {

            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {


                if (isDetached()) {
                    return;
                }


                listAdapter.cartItemMap.clear();

                if(response.body()!=null)
                {
                    for(CartItem cartItem: response.body())
                    {
                        listAdapter.cartItemMap.put(cartItem.getItemID(),cartItem);
                    }
                }



                if(notifyDatasetChanged)
                {
                    listAdapter.notifyDataSetChanged();
                }

            }



            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {


                if (isDetached()) {
                    return;
                }



                showToastMessage(" Unsuccessful !");
            }
        });

    }






    private void fetchCartStats()
    {

        Shop shop = PrefShopHome.getShop(getActivity());

        if(shop ==null)
        {
            return;
        }

        User endUser = PrefLogin.getUser(getActivity());


        if(endUser == null)
        {

            listAdapter.cartStats.setItemsInCart(0);
            listAdapter.cartStats.setCart_Total(0);
            listAdapter.cartStats.setShopID(shop.getShopID());

            setItemsInCart(0,false);
            setCartTotal(0,false);

            return;
        }


//        showToastMessage("Fetch Cart Stats !");

        Call<List<CartStats>> listCall = cartStatsService
                .getCartStatsList(endUser.getUserID(), null,shop.getShopID(),
                        false,null,null);



        listCall.enqueue(new Callback<List<CartStats>>() {
            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {


                if (isDetached()) {
                    return;
                }


//                listAdapter.cartStatsMap.clear();




                if(response.isSuccessful() && response.body()!=null)
                {
                    for(CartStats cartStats: response.body())
                    {

                        listAdapter.cartStats.setItemsInCart(cartStats.getItemsInCart());
                        listAdapter.cartStats.setCart_Total(cartStats.getCart_Total());
                        listAdapter.cartStats.setShopID(cartStats.getShopID());
                        listAdapter.cartStats.setCartID(cartStats.getCartID());
                        listAdapter.cartStats.setShop(cartStats.getShop());


                        setItemsInCart(cartStats.getItemsInCart(),false);
                        setCartTotal(cartStats.getCart_Total(),false);

                    }
                }
                else
                {


                    listAdapter.cartStats.setItemsInCart(0);
                    listAdapter.cartStats.setCart_Total(0);
                    listAdapter.cartStats.setShopID(shop.getShopID());

                    setItemsInCart(0,false);
                    setCartTotal(0,false);
                }

            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {


                showToastMessage(" Unsuccessful !");
            }
        });
    }




    private void getShopDetails()
    {   int shopIDIntent = 0;

        shopIDIntent = getActivity().getIntent().getIntExtra("shop_id",0);

        viewModelShop.getShopDetailsForItemsInShopScreen(shopIDIntent);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ... Please wait !");
        progressDialog.show();
    }



    private void getShopDetailsFromDeepLink(int shopID)
    {
        viewModelShop.getShopDetailsForItemsInShopScreen(shopID);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ... Please wait !");
        progressDialog.show();
    }



    private void setupViewModel()
    {

        viewModelShop = new ViewModelShop(MyApplication.application);


        viewModelShop.getShopLive().observe(getViewLifecycleOwner(), new Observer<Shop>() {
            @Override
            public void onChanged(Shop shop) {

                PrefShopHome.saveShop(shop,ItemsInShopByCatFragment.this.getActivity());
                makeRefreshNetworkCall();
                setupShop();
            }
        });




        viewModelShop.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {


                if(integer == ViewModelShop.EVENT_SHOP_DETAILS_FETCHED || integer == ViewModelShop.EVENT_NETWORK_FAILED)
                {
                    if(progressDialog!=null)
                    {
                        progressDialog.dismiss();
                    }

                    swipeContainer.setRefreshing(false);
                }

            }
        });




        viewModelShop.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage("Message : " + s);
            }
        });

    }


    @Override
    public void filterShopUpdated() {

        setupRecyclerView();
        makeRefreshNetworkCall();

    }



}
