package org.nearbyshops.enduserappnew.Lists.ItemsInShopByCategory;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.API.CartItemService;
import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.API.ItemCategoryService;
import org.nearbyshops.enduserappnew.API.ShopItemService;
import org.nearbyshops.enduserappnew.Lists.CartItemList.CartItemList;
import org.nearbyshops.enduserappnew.Lists.CartItemList.CartItemListFragment;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.DetailScreens.DetailItem.ItemDetailFragment;
import org.nearbyshops.enduserappnew.DetailScreens.DetailItem.ItemDetail;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.enduserappnew.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItemCategorySmall;
import org.nearbyshops.enduserappnew.SlidingLayerSort.PreferencesSort.PrefSortItemsInShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopItemButton;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DetailScreens.DetailShop.ShopDetail;
import org.nearbyshops.enduserappnew.DetailScreens.DetailShop.ShopDetailFragment;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopMedium;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sumeet on 2/12/16.
 */



public class ItemsInShopByCatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderItemCategorySmall.ListItemClick, ViewHolderShopItemButton.ListItemClick,
        ViewHolderItemCategory.ListItemClick, ViewHolderShopItem.ListItemClick,
        NotifyBackPressed, NotifySort, NotifySearch,
        ViewHolderShopMedium.ListItemClick {






    @BindView(R.id.shop_profile_photo) ImageView itemImage;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;



    private boolean isDestroyed = false;
    boolean show = true;

    int item_count_item_category = 0;

    private int limit_item = 10;
    private int offset_item = 0;
    private int item_count_item;
    private int fetched_items_count = 0;



    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView itemCategoriesList;




    private ArrayList<Object> dataset = new ArrayList<>();


    @BindView(R.id.itemsInCart) public TextView itemsInCart;
    @BindView(R.id.cartTotal) public TextView cartTotal;


    private GridLayoutManager layoutManager;
    private Adapter listAdapter;





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
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);
    }




    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_address) TextView shopAddress;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_items_in_shop_by_cat, container, false);

        ButterKnife.bind(this,rootView);



        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        Shop shop = PrefShopHome.getShop(getActivity());
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(shop.getShopName());




        shopName.setText(shop.getShopName());
        shopAddress.setText(String.format("%.2f Km",shop.getRt_distance()) + " | " + shop.getShopAddress());




        String imagePath = PrefGeneral.getServiceURL(getActivity()) + "/api/v1/Shop/Image/five_hundred_"
                + shop.getLogoImagePath() + ".jpg";


        Drawable placeholder = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_nature_people_white_48px, getActivity().getTheme());


        Picasso.get().load(imagePath)
                .placeholder(placeholder)
                .into(itemImage);



        setupRecyclerView();
        setupSwipeContainer();


        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }
        else
        {
            // add this at every rotation
//            listAdapter.shopItemMap.putAll(shopItemMapTemp);
        }





        getCartStats(false,0,true);

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
                       final DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

                    if(spanCount==0){
                        spanCount = 1;
                    }

                    return (6/spanCount);

                }
                else if(dataset.get(position) instanceof ShopItem)
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


//    @State int previous_position = -1;










    @Override
    public void onRefresh() {
        makeRequestShopItem(true,true);
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





    private void makeRequestShopItem(final boolean clearDataset, boolean resetOffset)
    {

        if(resetOffset)
        {
            offset_item = 0;
        }


        String current_sort = "";

        current_sort = PrefSortItemsInShop.getSort(getContext()) + " " + PrefSortItemsInShop.getAscending(getContext());


        Call<ShopItemEndPoint> endPointCall = null;

        Shop currentShop = PrefShopHome.getShop(getContext());


        if(searchQuery==null)
        {




            endPointCall = shopItemService.getShopItemEndpoint(
                    currentCategory.getItemCategoryID(),clearDataset,
                    currentShop.getShopID(),
                    null,null,null,null,null,null,null,
                    null,null,
                    null,null,null,
                    null,true,current_sort,
                    limit_item,offset_item,clearDataset,
                    false);

        }
        else
        {


            endPointCall = shopItemService.getShopItemEndpoint(
                    null,clearDataset,
                    currentShop.getShopID(),
                    null,null,null,null,null,
                    null,null,null,null,
                    null,null,null,
                    searchQuery,
                    true,current_sort,
                    limit_item,offset_item,clearDataset,
                    false);
        }


        endPointCall.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {



                    if(clearDataset)
                    {
                        dataset.clear();



                        if(response.body()!=null)
                        {

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

                    }




                    dataset.addAll(response.body().getResults());
                    fetched_items_count = fetched_items_count + response.body().getResults().size();

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
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        makeRefreshNetworkCall();
        getCartStats(true,0,true);

        // End Search Mode
        searchQuery = null;

        // reset previous flag

    }



    @Override
    public boolean backPressed() {

        // reset previous flag

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        // clear selected items
//        listAdapter.selectedItems.clear();

//        getCartStats(true,0,true);



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






    @BindView(R.id.text_sub) TextView itemHeader;





    private void notifyItemIndicatorChanged()
    {

//        if(getActivity() instanceof NotifyIndicatorChanged)
//        {
//            ((NotifyIndicatorChanged) getActivity()).notifyItemIndicatorChanged(String.valueOf(fetched_items_count) + " out of " + String.valueOf(item_count_item) + " " + currentCategory.getCategoryName() + " Items in Shop");
//        }



        itemHeader.setText(fetched_items_count + " out of " + item_count_item + " " + currentCategory.getCategoryName() + " Items");
    }






    @Override
    public void notifySortChanged() {

//        System.out.println("Notify Sort Clicked !");
        makeRefreshNetworkCall();
    }



    // display shop Item Status




    @Override
    public void notifyItemImageClick(Item item) {

        Intent intent = new Intent(getActivity(), ItemDetail.class);
//        intent.putExtra(ItemDetail.ITEM_DETAIL_INTENT_KEY,item);

        String itemJson = UtilityFunctions.provideGson().toJson(item);
        intent.putExtra(ItemDetailFragment.TAG_JSON_STRING,itemJson);
        getActivity().startActivity(intent);
    }










    @Override
    public void showLogin() {

        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent,123);
    }




    @Override
    public void cartUpdated() {


        if(listAdapter.cartStats.getCartID()==0)
        {
            System.out.println("Cart ID Zero !");
            getCartStats(true,0,true);
        }

    }






    @Override
    public void setCartTotal(double cartTotalValue, boolean save) {



//        cartStatsBlock.setVisibility(View.VISIBLE);



//        cartTotal.setText("Cart Total : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.valueOf(cartTotalValue));

        cartTotal.setText(PrefGeneral.getCurrencySymbol(getActivity()) + " " + UtilityFunctions.refinedString(cartTotalValue));


        if(save)
        {
//            Shop shop = PrefShopHome.getShopDetails(getActivity());
//
//            CartStats cartStats = listAdapter.cartStatsMap.get(shop.getShopID());
//            cartStats.setCart_Total(cartTotalValue);
//            listAdapter.cartStatsMap.put(shop.getShopID(),cartStats);

            listAdapter.cartStats.setCart_Total(cartTotalValue);
        }
        else
        {

//            if(listAdapter.cartStats.getCartID()==0)
//            {
//                getCartStats(true,0,true);
//            }

        }




    }








    @Override
    public void setItemsInCart(int itemsInCartValue, boolean save) {


//        if(itemsInCartValue==0)
//        {
//            cartStatsBlock.setVisibility(View.GONE);
//        }
//        else
//        {
//            cartStatsBlock.setVisibility(View.VISIBLE);
//        }



//        itemsInCart.setText(String.valueOf(itemsInCartValue) + " " + "Items in Cart");
        itemsInCart.setText(itemsInCartValue + " " + "Items");




//        if(itemsInCartValue==1 && listAdapter.cartStats.getCartID()==0)
//        {
//            getCartStats(true,0,true);
//        }


        if(save)
        {

//            Shop shop = PrefShopHome.getShopDetails(getActivity());

//            CartStats cartStats = listAdapter.cartStatsMap.get(shop.getShopID());
//            cartStats.setItemsInCart(itemsInCartValue);
//            listAdapter.cartStatsMap.put(shop.getShopID(),cartStats);

            listAdapter.cartStats.setItemsInCart(itemsInCartValue);


        }
    }


    @BindView(R.id.cart_stats) LinearLayout cartStatsBlock;






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==123 && resultCode == RESULT_OK)
        {
            // login success
            getCartStats(true,0,true);
        }
    }





    @Override
    public void listItemClick(Shop shop, int position) {

        Intent intent = new Intent(getActivity(), ShopDetail.class);
        String jsonString = UtilityFunctions.provideGson().toJson(shop);
        intent.putExtra(ShopDetailFragment.TAG_JSON_STRING,jsonString);
        startActivity(intent);
    }





    @OnClick({R.id.app_bar,R.id.collapsing_toolbar,R.id.toolbar})
    void shopClick()
    {
        Shop shop = PrefShopHome.getShop(getActivity());
        Intent intent = new Intent(getActivity(), ShopDetail.class);
        String jsonString = UtilityFunctions.provideGson().toJson(shop);
        intent.putExtra(ShopDetailFragment.TAG_JSON_STRING,jsonString);
        startActivity(intent);
    }






//    @OnClick({R.id.view_cart_icon,R.id.view_cart_text})
    @OnClick({R.id.cart_stats})
    void viewCartClick()
    {
//                Intent intent = new Intent(getActivity(), CartsList.class);
//        startActivity(intent);


        Intent intent = new Intent(getActivity(), CartItemList.class);


        String shopJson = UtilityFunctions.provideGson().toJson(PrefShopHome.getShop(getActivity()));
        intent.putExtra(CartItemListFragment.SHOP_INTENT_KEY,shopJson);

        String cartStatsJson = UtilityFunctions.provideGson().toJson(listAdapter.cartStats);
        intent.putExtra(CartItemListFragment.CART_STATS_INTENT_KEY,cartStatsJson);

        startActivity(intent);
    }








    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;








    public void getCartStats(final boolean notifyChange, final int position, final boolean notifyDatasetChanged)
    {



        Shop shop = PrefShopHome.getShop(getActivity());

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






        Call<List<CartItem>> cartItemCall = cartItemService.getCartItem(null,null,
                endUser.getUserID(),shop.getShopID(),false);




        cartItemCall.enqueue(new Callback<List<CartItem>>() {

            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {

                listAdapter.cartItemMap.clear();

                if(response.body()!=null)
                {
                    for(CartItem cartItem: response.body())
                    {
                        listAdapter.cartItemMap.put(cartItem.getItemID(),cartItem);
                    }
                }

                if(notifyChange)
                {
                    listAdapter.notifyItemChanged(position);
                }

                if(notifyDatasetChanged)
                {
                    listAdapter.notifyDataSetChanged();
                }






            }



            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {

                Toast.makeText(getActivity()," Unsuccessful !",Toast.LENGTH_SHORT).show();
            }
        });




        Call<List<CartStats>> listCall = cartStatsService
                .getCart(endUser.getUserID(), null,shop.getShopID(),
                        true,null,null);



        listCall.enqueue(new Callback<List<CartStats>>() {
            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {

//                listAdapter.cartStatsMap.clear();




                if(response.isSuccessful() && response.body()!=null)
                {
                    for(CartStats cartStats: response.body())
                    {


////                        listAdapter.cartStatsMap.put(cartStats.getShopID(),cartStats);

                        listAdapter.cartStats.setItemsInCart(cartStats.getItemsInCart());
                        listAdapter.cartStats.setCart_Total(cartStats.getCart_Total());
                        listAdapter.cartStats.setShopID(cartStats.getShopID());
                        listAdapter.cartStats.setCartID(cartStats.getCartID());
                        listAdapter.cartStats.setShop(cartStats.getShop());

//                        listAdapter.cartStats = cartStats;



//                        CartStats cartStatsLocal = listAdapter.cartStatsMap.get(shop.getShopID());

                        setItemsInCart(cartStats.getItemsInCart(),false);
                        setCartTotal(cartStats.getCart_Total(),false);

                    }
                }
                else
                {


//                    CartStats cartStatsLocal = new CartStats(0,0,shop.getShopID());
//                    listAdapter.cartStatsMap.put(cartStatsLocal.getShopID(),cartStatsLocal);

                    listAdapter.cartStats.setItemsInCart(0);
                    listAdapter.cartStats.setCart_Total(0);
                    listAdapter.cartStats.setShopID(shop.getShopID());

                    setItemsInCart(0,false);
                    setCartTotal(0,false);
                }





                if(notifyChange)
                {
                    listAdapter.notifyItemChanged(position);
                }



                if(notifyDatasetChanged)
                {
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {

                Toast.makeText(getActivity()," Unsuccessful !",Toast.LENGTH_SHORT).show();
            }
        });


    }



}
