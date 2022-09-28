package org.nearbyshops.whitelabelapp.Lists.ShopsAvailableForItem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.API.ShopItemService;
import org.nearbyshops.whitelabelapp.Lists.ShopsAvailableForItem.SlidingLayerSort.PrefSortShopItems;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.ItemStats;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailItem.ItemDetailFragment;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailItemNew.ItemDetail;
import org.nearbyshops.whitelabelapp.Lists.ItemsInShopByCategory.ItemsInShopByCat;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

//import org.nearbyshops.enduser.ItemsByCategoryTypeSimple.Adapter;

public class ShopItemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        Adapter.NotifyFilledCart, NotifySort
{



    private Item item;

    @Inject
    ShopItemService shopItemService;





    private ArrayList<ShopItem> dataset = new ArrayList<>();

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;


    private Adapter adapter;

    private boolean isDestroyed;




    // bindings for item
    @BindView(R.id.itemName) TextView categoryName;
//        TextView categoryDescription;

    //    @BindView(R.id.items_list_item) CardView itemCategoryListItem;
    @BindView(R.id.itemImage) ImageView categoryImage;
    @BindView(R.id.price_range) TextView priceRange;
    @BindView(R.id.price_average) TextView priceAverage;
    @BindView(R.id.shop_count) TextView shopCount;
    @BindView(R.id.item_rating) TextView itemRating;
    @BindView(R.id.rating_count) TextView ratingCount;




    private boolean clearDataset = true;

    private int limit = 10;
    private int offset = 0;
    private int item_count = 0;

    /*
    * Terminologies and concepts
    *
    * Each Shop has a seperate cart. If a customer buys 2 items from two shops that will create two separate carts. Two Shops do not share one cart.
    *
    *
    * Filled Carts : Carts in which items are already added
    * New Carts : Carts in which no items are added
    *
    *
    * */





    public ShopItemFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_shop_item, container, false);
        ButterKnife.bind(this,rootView);
        setRetainInstance(true);


//        item = getArguments().getParcelable("item");

        String jsonString = getActivity().getIntent().getStringExtra("item_json");
        Gson gson = UtilityFunctions.provideGson();
        item = gson.fromJson(jsonString, Item.class);


//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }






        if(savedInstanceState==null)
        {
            setupRecyclerView();
            swipeRefresh();
        }
        else
        {
//            onViewStateRestored(savedInstanceState);
            setupRecyclerView();
            adapter.getCartStats();
        }



        bindItem();


        return rootView;
    }









    private void setupRecyclerView()
    {
        adapter = new Adapter(dataset,getActivity(),item,this);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);





        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(offset + limit > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }




                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {



                    // trigger fetch next page

                    if((offset + limit)<=item_count)
                    {
                        offset = offset + limit;

//                        makeRequestItem(false,false);

                        fetchNewCartItems();

                    }


                }
            }

        });



    }









    private void swipeRefresh()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();

            }
        });
    }






    private void fetchNewCartItems()
    {
        // fetch shop items from shops with carts not filled

        User endUser = PrefLogin.getUser(getActivity());


//        if(endUser == null)
//        {
//            swipeContainer.setRefreshing(false);
//            return;
//        }


        if(item==null)
        {
            showToastMessage("Item null !");
            swipeContainer.setRefreshing(false);
            return;
        }



        Integer endUserID = null;


        if(endUser!=null)
        {
            endUserID = endUser.getUserID();
        }




        String current_sort = "";

        current_sort = PrefSortShopItems.getSort(getActivity())
                + " " + PrefSortShopItems.getAscending(getActivity());



        // Network Available
        Call<ShopItemEndPoint> call = shopItemService.getShopItemsByItem(
                null,false,null,item.getItemID(),
                PrefLocation.getLatitudeSelected(getActivity()), PrefLocation.getLongitudeSelected(getActivity()),
                endUserID,
                false,
                null,null,null,null,
                null,true,current_sort,
                limit,offset,
                clearDataset,false
        );



        call.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {
                    if(response.body()!=null)
                    {

                        if(clearDataset)
                        {
                            dataset.clear();
                            clearDataset = false;

                            item_count = response.body().getItemCount();
                        }


                        dataset.addAll(response.body().getResults());
                    }
                }
                else
                {
                    showToastMessage("Response Code : " + response.code());
                }




                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);
            }
        });


    }




    @Override
    public void onRefresh() {
        clearDataset = true;

        if(PrefLogin.getUser(getActivity())!=null)
        {
            // fetch only when logged in
            fetchFilledCartItems();
        }

        fetchNewCartItems();
    }







    private void fetchFilledCartItems()
    {
            // fetch shop items from shops with filled carts


            User endUser = PrefLogin.getUser(getActivity());



            if(item==null)
            {
                swipeContainer.setRefreshing(false);
                return;
            }





        String current_sort = "";

        current_sort = PrefSortShopItems.getSort(getActivity()) + " " + PrefSortShopItems.getAscending(getActivity());



        Call<ShopItemEndPoint> callEndpoint = shopItemService.getShopItemsByItem(
                    null,false,null,item.getItemID(),
                    PrefLocation.getLatitudeSelected(getActivity()), PrefLocation.getLongitudeSelected(getActivity()),
                    endUser.getUserID(),
                    true,
                    null,null,
                null,null,
                    null,
                    true, current_sort,
                    100,0,
                false,false
            );



            callEndpoint.enqueue(new Callback<ShopItemEndPoint>() {
                @Override
                public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {

                    if (isDestroyed) {
                        return;
                    }


                    if(response.code()==200)
                    {

                        if (response.body() != null)
                        {

                            if(clearDataset)
                            {
                                dataset.clear();
                                clearDataset=false;
                            }

                            dataset.addAll(0,response.body().getResults());

                            adapter.getCartStats();
                            adapter.notifyDataSetChanged();
                            swipeContainer.setRefreshing(false);


                        }
                    }
                    else
                    {
                        showToastMessage("Failed code : " + response.code());
                    }



                }

                @Override
                public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    showToastMessage("Network Request failed !");
                    swipeContainer.setRefreshing(false);


                }
            });
    }






    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }






    @Override
    public void notifyCartDataChanged() {

//        swipeRefresh();
    }







    @Override
    public void notifyShopLogoClick(Shop shop) {

//        Intent shopHomeIntent = new Intent(getActivity(), ShopDashboard.class);
//        PrefShopHome.saveShop(shop,getActivity());
//        startActivity(shopHomeIntent);


        PrefShopHome.saveShop(shop,getActivity());
        Intent intent = new Intent(getActivity(), ItemsInShopByCat.class);
        startActivity(intent);
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        isDestroyed = true;
    }




    @Override
    public void notifySortChanged() {
        onRefresh();
    }



    @OnClick(R.id.include)
    void listItemClick()
    {

        Intent intent = new Intent(getActivity(), ItemDetail.class);
        intent.putExtra("item_id",item.getItemID());

        String itemJson = UtilityFunctions.provideGson().toJson(item);
        intent.putExtra(ItemDetailFragment.TAG_JSON_STRING,itemJson);
//        startActivity(intent);
    }








    private void bindItem()
    {

        if(item==null)
        {
            return;
        }


        categoryName.setText(item.getItemName());

        ItemStats itemStats = item.getItemStats();

        if(itemStats!=null)
        {
            String currency = "";
            currency = PrefCurrency.getCurrencySymbol(getActivity());

            priceRange.setText("Price Range :\n" + currency + " " + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
            priceAverage.setText("Price Average :\n" + currency + " " + String.format("%.2f",itemStats.getAvg_price()) + " per " + item.getQuantityUnit());
            shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
        }


        itemRating.setText(String.format("%.2f",item.getRt_rating_avg()));
        ratingCount.setText("( " + item.getRt_rating_count() + " Ratings )");




        if(item.getRt_rating_count()==0)
        {
            itemRating.setText(" New ");
            itemRating.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));
            ratingCount.setVisibility(View.GONE);
        }
        else
        {

            ratingCount.setText("( " + item.getRt_rating_count() + " Ratings )");
            itemRating.setText(String.format(" %.2f ",item.getRt_rating_avg()));
            itemRating.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gplus_color_2));
            ratingCount.setVisibility(View.VISIBLE);
        }



        String imagePath = PrefGeneral.getServerURL(getActivity())
                + "/api/v1/Item/Image/five_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(getActivity().getResources(),
                        R.drawable.ic_nature_people_white_48px, getActivity().getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(drawable)
                .into(categoryImage);

    }











    @Override
    public void showLoginScreen() {

        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent,123);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123 && resultCode == RESULT_OK)
        {
            // login success
            adapter.getCartStats();
        }
    }



}

