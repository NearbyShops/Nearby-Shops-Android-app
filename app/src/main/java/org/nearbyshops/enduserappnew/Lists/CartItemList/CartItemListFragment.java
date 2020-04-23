package org.nearbyshops.enduserappnew.Lists.CartItemList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.nearbyshops.enduserappnew.API.CartItemService;
import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderCartItem;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Checkout.PlaceOrderActivity;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CartItemListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, ViewHolderCartItem.ListItemClick {


//    TextView confirmItems;

    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;

    private RecyclerView recyclerView;
    private Adapter adapter;
    private SwipeRefreshLayout swipeContainer;


    private List<Object> dataset = new ArrayList<>();
    private Shop shop = null;
    private CartStats cartStats = null;


    public final static String SHOP_INTENT_KEY = "shop_cart_item";
    public final static String CART_STATS_INTENT_KEY = "cart_stats";


    private TextView totalValue;
    private TextView estimatedTotal;
    private double cartTotal = 0;



    @BindView(R.id.empty_screen) LinearLayout emptyScreen;
    @BindView(R.id.bottom_bar) ConstraintLayout bottomBar;

    @BindView(R.id.shop_name) TextView shopName;


    // header views
//    ImageView shopImage;
//    TextView shopName;
//    TextView rating;
//    TextView distance;
//    TextView deliveryCharge;
//    TextView itemsInCart;
//    TextView cartTotalHeader;
//    LinearLayout cartsListItem;


    public CartItemListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_cart_item_list, container, false);
        ButterKnife.bind(this, rootView);


        String shopJson = getActivity().getIntent().getStringExtra(SHOP_INTENT_KEY);
        shop = UtilityFunctions.provideGson().fromJson(shopJson, Shop.class);


        shopName.setText(shop.getShopName());



        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        toolbar.setTitle("Items in Cart");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//


        // findViewByID's
        swipeContainer = rootView.findViewById(R.id.swipeContainer);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        totalValue = rootView.findViewById(R.id.totalValue);
        estimatedTotal = rootView.findViewById(R.id.estimatedTotal);
//        confirmItems = (TextView) findViewById(R.id.confirm);


//        shopImage = (ImageView) findViewById(R.id.shopImage);
//        shopName = (TextView) findViewById(R.id.shopName);
//        rating = (TextView) findViewById(R.id.rating);
//        distance = (TextView) findViewById(R.id.distance);
//        deliveryCharge = (TextView) findViewById(R.id.deliveryCharge);
//        itemsInCart = (TextView) findViewById(R.id.itemsInCart);
//        cartTotalHeader = (TextView) findViewById(R.id.cartTotal);
//        cartsListItem = (LinearLayout)findViewById(R.id.carts_list_item);




        // get shop from intent

//        shop = getIntent().getParcelableExtra(SHOP_INTENT_KEY);
//        cartStats = getIntent().getParcelableExtra(CART_STATS_INTENT_KEY);



        String cartStatsJson = getActivity().getIntent().getStringExtra(CART_STATS_INTENT_KEY);
        cartStats = UtilityFunctions.provideGson().fromJson(cartStatsJson, CartStats.class);







        if(cartStats==null)
        {
            fetchCartStats();
        }



//        setupHeader();
        setupSwipeContainer();
        setupRecyclerView();



//        if(((AppCompatActivity) getActivity()).getSupportActionBar()!=null)
//        {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }



        if(savedInstanceState==null)
        {
            swipeRefresh();
        }




        displayCartStats();


        fetchCartStats();


        return rootView;
    }








    private void fetchCartStats()
    {

        User endUser = PrefLogin.getUser(getActivity());

        if(endUser==null || shop==null)
        {
//            showLogin();
            return;
        }


        Call<List<CartStats>> call = cartStatsService.getCart(endUser.getUserID(),null,shop.getShopID(),false,null,null);

        call.enqueue(new Callback<List<CartStats>>() {

            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {

                if(response.code()==200 && response.body()!=null)
                {
                    if(response.body().size()>0)
                    {
                        cartStats = response.body().get(0);
                        cartStats.setShop(shop);
                        displayCartStats();
                    }
                }


            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {

            }
        });

    }







    private void displayCartStats()
    {

        if(cartStats!=null)
        {
            cartTotal = cartStats.getCart_Total();
            totalValue.setText("Total : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.format("%.2f", cartTotal));
//            adapter.setCartStats(cartStats);
        }
    }







    @OnClick(R.id.confirm)
    void confirmItemsClick()
    {
        Intent intent = new Intent(getActivity(),PlaceOrderActivity.class);
        //        intent.putExtra(PlaceOrderActivity.CART_STATS_INTENT_KEY,cartStats);

        String cartStatsJson = UtilityFunctions.provideGson().toJson(cartStats);
        intent.putExtra(PlaceOrderActivity.CART_STATS_INTENT_KEY,cartStatsJson);


        startActivity(intent);
    }





    /*void setupHeader()
    {

        if(cartStats!=null)
        {
            itemsInCart.setText(cartStats.getItemsInCart() + " Items in Cart");
            cartTotalHeader.setText("Cart Total : Rs " + cartStats.getCart_Total());
        }

        if(shop!=null)
        {
            deliveryCharge.setText("Delivery\nRs " + shop.getDeliveryCharges() + "\nPer Order");
            distance.setText(String.format( "%.2f", shop.getDistance())
                    + " Km");

            shopName.setText(shop.getShopName());


            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplicationCoreNew.getAppContext())
                    + shop.getImagePath();

            Picasso.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.nature_people)
                    .into(shopImage);
        }

    }
*/




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
    }





    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

        makeNetworkCall();
    }







    private void makeNetworkCall()
    {

        User endUser = PrefLogin.getUser(getActivity());

        if(endUser==null)
        {
            showLogin();
            swipeContainer.setRefreshing(false);
            return;
        }



        if(shop==null)
        {
            swipeContainer.setRefreshing(false);
            showToastMessage("Shop null !");
            return;
        }


        emptyScreen.setVisibility(View.GONE);
        bottomBar.setVisibility(View.VISIBLE);


        Call<List<CartItem>> call = cartItemService.getCartItem(null,null,
                endUser.getUserID(),shop.getShopID(),true);


        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {

                if(response.body()!=null)
                {
                    dataset.clear();
                    dataset.addAll(response.body());

                    adapter.notifyDataSetChanged();

                    if(dataset.size()==0)
                    {
                        emptyScreen.setVisibility(View.VISIBLE);
                        bottomBar.setVisibility(View.GONE);
                    }

                }else
                {
                    emptyScreen.setVisibility(View.VISIBLE);
                    bottomBar.setVisibility(View.GONE);

                    dataset.clear();
                    adapter.notifyDataSetChanged();
                }



                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {


                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);

                emptyScreen.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.GONE);

            }
        });




//        if(UtilityGeneral.isNetworkAvailable(this))
//        {
//
//
//
//        }
//        else
//        {
//            showToastMessage("No network. Application is Offline !");
//            swipeContainer.setRefreshing(false);
//        }

    }






    @Override
    public void onResume() {
        super.onResume();
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



    @Override
    public void notifyUpdate(CartItem cartItem) {

//        Call<ResponseBody> call = cartItemService.updateCartItem(cartItem,0,0);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if(response.code() == 200)
//                {
//                    showToastMessage("Item Updated !");
//
//                    totalValue.setText(" : Rs " + String.format("%.2f", cartTotal));
//                    cartStats.setCart_Total(cartTotal);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                showToastMessage("Update failed. Try again !");
//            }
//        });





        fetchCartStats();
    }




    @Override
    public void notifyRemove(CartItem cartItem) {

//        Call<ResponseBody> call = cartItemService.deleteCartItem(cartItem.getCartID(),cartItem.getItemID(),0,0);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if(response.code() == 200)
//                {
//                    showToastMessage("Item Deleted");
//
//                    // refresh the list
//                    makeNetworkCall();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                showToastMessage("Remove failed. Try again !");
//
//            }
//        });
    }






    @Override
    public void notifyTotal(double total) {

        cartTotal = total;
        estimatedTotal.setText("Estimated Total (Before Update) : "  + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.format("%.2f", cartTotal));
    }








    private void showLogin()
    {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }

}
