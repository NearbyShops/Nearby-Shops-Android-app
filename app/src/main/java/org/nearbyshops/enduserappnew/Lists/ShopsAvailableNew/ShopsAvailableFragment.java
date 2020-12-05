package org.nearbyshops.enduserappnew.Lists.ShopsAvailableNew;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.nearbyshops.enduserappnew.API.CartItemService;
import org.nearbyshops.enduserappnew.API.ShopItemService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.DetailScreens.DetailShopItem.ShopItemDetail;
import org.nearbyshops.enduserappnew.Lists.CartsList.CartsList;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderAvailableShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShopsAvailableFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, ViewHolderAvailableShop.ListItemClick {




    private RecyclerView recyclerView;
    private Adapter adapter;
    private SwipeRefreshLayout swipeContainer;


    private List<Object> dataset = new ArrayList<>();

    private int itemID;


    private boolean clearDataset = true;

    private int limit = 10;
    private int offset = 0;
    private int item_count = 0;


    @Inject
    ShopItemService shopItemService;


    Item item;




    public ShopsAvailableFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_available_shops_list, container, false);
        ButterKnife.bind(this, rootView);

//        shopName.setText(shop.getShopName());



        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        toolbar.setTitle("Items in Cart");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//


        // findViewByID's
        swipeContainer = rootView.findViewById(R.id.swipeContainer);
        recyclerView = rootView.findViewById(R.id.recyclerView);



        itemID = getActivity().getIntent().getIntExtra("item_id",0);
        String jsonString = getActivity().getIntent().getStringExtra("item_json");

        item = UtilityFunctions.provideGson().fromJson(jsonString,Item.class);


        setupSwipeContainer();
        setupRecyclerView();


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL));





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

                        makeNetworkCall(false);

                    }


                }
            }

        });

    }





    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }






    @OnClick({R.id.icon_checkout,R.id.text_checkout})
    void viewCartClick()
    {
        if(PrefLogin.getUser(getActivity())==null)
        {
            showLogin();
            return;
        }

        Intent intent = new Intent(getActivity(), CartsList.class);
        startActivity(intent);
    }




    @Override
    public void onRefresh() {

        makeNetworkCall(true);
        getCartStats();
    }







    private void makeNetworkCall(boolean clearDataset)
    {

        User endUser = PrefLogin.getUser(getActivity());

        Integer endUserID = null;

        if(endUser!=null)
        {
            endUserID = endUser.getUserID();
        }



        Call<ShopItemEndPoint> call = shopItemService.getAvailableShops(
                itemID,
                PrefLocation.getLatitude(getActivity()),PrefLocation.getLongitude(getActivity()),
                endUserID,true,
                null,true,
                null,limit,offset,
                clearDataset,false
        );




        call.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {

                swipeContainer.setRefreshing(false);

                if(response.code()==200)
                {

                    if(clearDataset)
                    {
                        dataset.clear();
//                        dataset.add(0,response.body().getItemDetails());
                        dataset.add(0,item);

                        dataset.add(1,new HeaderTitle("Available in Following Shops"));
                        item_count = response.body().getItemCount();
                    }




                    dataset.addAll(response.body().getResults());


                }
                else
                {
                    showToastMessage("Failed Code : "  + String.valueOf(response.code()));
                }




                if(offset + limit >= item_count)
                {
                    adapter.setLoadMore(false);
                }
                else
                {
                    adapter.setLoadMore(true);
                }


                adapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                swipeContainer.setRefreshing(false);

                showToastMessage("Failed !");
            }
        });



    }






    @Override
    public void onResume() {
        super.onResume();
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





    @Inject
    CartItemService cartItemService;



    void getCartStats()
    {

        adapter.cartItemMap.clear();

        User endUser = PrefLogin.getUser(getActivity());



        if(endUser == null)
        {
            return;
        }



        Call<List<CartItem>> cartItemCall = cartItemService.getCartItemAvailabilityByItem(
                endUser.getUserID(),
                itemID
        );



        cartItemCall.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {


                if(response.body()!=null)
                {

                    for(CartItem cartItem: response.body())
                    {
                        adapter.cartItemMap.put(cartItem.getCart().getShopID(),cartItem);
                    }

                    adapter.notifyDataSetChanged();


                }else
                {
                    adapter.cartItemMap.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {

                UtilityFunctions.showToastMessage(getActivity(),"Failed Please check network connection !");
            }
        });

    }






    private void showLogin()
    {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }




    @Override
    public void listItemClick(ShopItem shopItem, int position) {

        Intent intent = new Intent(getActivity(), ShopItemDetail.class);
        intent.putExtra("item_id",shopItem.getItemID());
        intent.putExtra("shop_id",shopItem.getShopID());

        startActivityForResult(intent,5676);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==5676 && resultCode==5679)
        {
            getCartStats();
        }
    }
}
