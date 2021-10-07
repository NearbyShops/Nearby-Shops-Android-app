package org.nearbyshops.whitelabelapp.CartAndOrder.CartItemList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.nearbyshops.whitelabelapp.API.CartItemService;
import org.nearbyshops.whitelabelapp.API.CartStatsService;
import org.nearbyshops.whitelabelapp.CartAndOrder.DeliveryAddress.DeliveryAddressSelectionFragment;
import org.nearbyshops.whitelabelapp.CartAndOrder.Checkout.PlaceOrder;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditDeliveryAddress.EditAddressFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditDeliveryAddress.EditDeliveryAddress;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.CartItem;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.CartStats;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderCartItemNew;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderDeliveryAddress.ViewHolderDeliveryAddress;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CartItemListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderCartItemNew.ListItemClick , ViewHolderButton.ListItemClick, ViewHolderDeliveryAddress.ListItemClick
{




    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;

    private RecyclerView recyclerView;
    private Adapter adapter;
    private SwipeRefreshLayout swipeContainer;


    private List<Object> dataset = new ArrayList<>();
//    private Shop shop = null;

    private int shopID;
    private CartStats cartStats = null;


    public final static String SHOP_INTENT_KEY = "shop_cart_item";
    public final static String CART_STATS_INTENT_KEY = "cart_stats";


    private TextView totalValue;
    private TextView estimatedTotal;
    private double cartTotal = 0;



    @BindView(R.id.savings_over_mrp) TextView savingsOverMRP;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.empty_screen) LinearLayout emptyScreen;
    @BindView(R.id.bottom_bar) ConstraintLayout bottomBar;
    @BindView(R.id.shop_name) TextView shopName;






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

//        shopName.setText(shop.getShopName());



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



//        String shopJson = getActivity().getIntent().getStringExtra(SHOP_INTENT_KEY);
//        shop = UtilityFunctions.provideGson().fromJson(shopJson, Shop.class);

//        String cartStatsJson = getActivity().getIntent().getStringExtra(CART_STATS_INTENT_KEY);
//        cartStats = UtilityFunctions.provideGson().fromJson(cartStatsJson, CartStats.class);




        if(!getResources().getBoolean(R.bool.single_vendor_mode_enabled))
        {

            shopID = getActivity().getIntent().getIntExtra("shop_id",0);
            String shopNameText = getActivity().getIntent().getStringExtra("shop_name");


            if(shopNameText!=null)
            {
                shopName.setVisibility(View.VISIBLE);
                shopName.setText(shopNameText);
            }
        }





        setupSwipeContainer();
        setupRecyclerView();


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        return rootView;
    }




    public void setShopID(int shopID) {
        this.shopID = shopID;
    }





    private void fetchCartStats()
    {



        User endUser = PrefLogin.getUser(getActivity());

        if(endUser==null || shopID==0)
        {
//            showLogin();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        totalValue.setVisibility(View.INVISIBLE);

        Call<CartStats> call = cartStatsService.getCartStats(endUser.getUserID(),shopID,false,false);

        call.enqueue(new Callback<CartStats>() {
            @Override
            public void onResponse(Call<CartStats> call, Response<CartStats> response) {

                progressBar.setVisibility(View.GONE);
                totalValue.setVisibility(View.VISIBLE);


                if(response.code()==200 && response.body()!=null)
                {
                    cartStats = response.body();
                    displayCartStats();
                }


            }

            @Override
            public void onFailure(Call<CartStats> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                totalValue.setVisibility(View.VISIBLE);

            }
        });



    }







    private void displayCartStats()
    {

        if(cartStats!=null)
        {
            cartTotal = cartStats.getCart_Total();
            totalValue.setText(PrefCurrency.getCurrencySymbol(getActivity()) + " " + String.format("%.2f", cartTotal));


            if(cartStats.getSavingsOverMRP()>0)
            {
                savingsOverMRP.setText("Savings : " + PrefCurrency.getCurrencySymbol(getActivity()) + " " + UtilityFunctions.refinedStringWithDecimals(cartStats.getSavingsOverMRP()));

                savingsOverMRP.setVisibility(View.VISIBLE);
            }
            else
            {
                savingsOverMRP.setVisibility(View.GONE);
            }

        }
    }







    @OnClick(R.id.confirm)
    void confirmItemsClick()
    {

        DeliveryAddressSelectionFragment.Companion.newInstance()
                .show(getChildFragmentManager(), "address_selection");
    }




    @Override
    public void listItemClick(@org.jetbrains.annotations.Nullable DeliveryAddress address, int position) {

        PrefLocation.saveDeliveryAddress(address,getActivity());
        dismissAndLaunch();
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
        if(getActivity()==null)
        {
            return;
        }

        adapter = new Adapter(dataset,getActivity(),this);
        recyclerView.setAdapter(adapter);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL));
    }





    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }




    @Override
    public void onRefresh() {

        makeNetworkCall();
        fetchCartStats();
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



        if(shopID==0)
        {
            swipeContainer.setRefreshing(false);
            showToastMessage("Shop Id not provided !");
            return;
        }


        emptyScreen.setVisibility(View.GONE);
        bottomBar.setVisibility(View.VISIBLE);




        Call<List<CartItem>> call = cartItemService.getCartItem(
                endUser.getUserID(),shopID,
                Item.TABLE_NAME + "." + Item.ITEM_NAME,
                null,null
        );



        call.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {

                if(response.body()!=null)
                {
                    dataset.clear();
                    dataset.add(new ViewHolderButton.ButtonData("Clear all Items"));
                    dataset.addAll(response.body());



                    if(dataset.size()==0)
                    {
//                        emptyScreen.setVisibility(View.VISIBLE);

                        dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.cartEmpty());
                        bottomBar.setVisibility(View.GONE);
                    }


                    adapter.notifyDataSetChanged();


                }else
                {
//                    emptyScreen.setVisibility(View.VISIBLE);


                    bottomBar.setVisibility(View.GONE);

                    dataset.clear();
                    dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.cartEmpty());
                    adapter.notifyDataSetChanged();
                }



                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {


//                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);

//                emptyScreen.setVisibility(View.VISIBLE);


                dataset.clear();
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();

                bottomBar.setVisibility(View.GONE);

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



    @Override
    public void notifyUpdate(CartItem cartItem) {

        fetchCartStats();
    }




    @Override
    public void notifyRemove(CartItem cartItem) {

        fetchCartStats();
    }








    @Override
    public void notifyTotal(double total) {

//        cartTotal = total;
//        estimatedTotal.setText("Estimated Total (Before Update) : "  + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.format("%.2f", cartTotal));
    }








    private void showLogin()
    {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }



    private void removeItems()
    {
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = cartItemService.deleteCartItem(cartStats.getCartID(),null,0,0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBar.setVisibility(View.GONE);


                if(response.code() == 200)
                {
                    makeRefreshNetworkCall();
                    showToastMessage("Item Removed");
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Remove failed. Please Try again !");

            }
        });
    }





    @Override
    public void buttonClick(ViewHolderButton.ButtonData data) {




        if(getActivity()==null)
        {
            return;
        }



        if(data.getLayoutType()==ViewHolderButton.LAYOUT_TYPE_ADD_NEW_ADDRESS)
        {
            Intent intent = new Intent(getActivity(), EditDeliveryAddress.class);
            intent.putExtra(EditAddressFragment.EDIT_MODE_INTENT_KEY, EditAddressFragment.MODE_ADD);
            intent.putExtra("select_added_address",true);
            startActivityForResult(intent,25);
        }
        else
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            dialog.setTitle("Remove all Items ?")
                    .setMessage("Do you want to remove all items from your cart")
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            removeItems();

                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            showToastMessage("Cancelled !");
                        }
                    })
                    .show();
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==25 && resultCode==50)
        {
            dismissAndLaunch();
        }
    }




    void dismissAndLaunch()
    {

        Fragment addressBottomSheet = getChildFragmentManager().findFragmentByTag("address_selection");

        if(addressBottomSheet instanceof DeliveryAddressSelectionFragment)
        {
            ((DeliveryAddressSelectionFragment) addressBottomSheet).dismiss();
        }

        startActivity(PlaceOrder.getLaunchIntent(shopID,shopName.getText().toString(),getActivity()));
    }

    @Override
    public void changeAddressClick() {

    }
}
