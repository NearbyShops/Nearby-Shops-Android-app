package org.nearbyshops.enduserappnew.Checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.API.OrderService;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Lists.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduserappnew.Home;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity implements View.OnClickListener{


    Order order = new Order();

    @Inject
    CartStatsService cartStatsService;


    @Inject
    OrderService orderService;



    CartStats cartStats;
    CartStats cartStatsFromNetworkCall;

    TextView addPickAddress;
    DeliveryAddress selectedAddress;




    // Total Fields
    TextView subTotal;
    TextView deliveryCharges;
    TextView total;


    @BindView(R.id.free_delivery_info) TextView freeDeliveryInfo;
    @BindView(R.id.radioPickFromShop) RadioButton pickFromShopCheck;
    @BindView(R.id.radioHomeDelivery) RadioButton homeDelieryCheck;
    @BindView(R.id.placeOrder) TextView placeOrder;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.delivery_instructions) TextView deliveryInstructions;


    // address views
    TextView name;
    TextView deliveryAddressView;
//    TextView city;
//    TextView pincode;
//    TextView landmark;
    TextView phoneNumber;
    LinearLayout addressContainer;

    // address views ends



    public final static String CART_STATS_INTENT_KEY = "cart_stats_intent_key";

    @BindView(R.id.service_name) TextView serviceName;



    public PlaceOrderActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // findViewByID'// STOPSHIP: 11/6/16

        addPickAddress = findViewById(R.id.pickFromSavedAddresses);
        addPickAddress.setOnClickListener(this);


        name = findViewById(R.id.name);
        deliveryAddressView = findViewById(R.id.deliveryAddress);
//        city = findViewById(R.id.city);
//        pincode = findViewById(R.id.pincode);
//        landmark = findViewById(R.id.landmark);
        phoneNumber = findViewById(R.id.phoneNumber);
        addressContainer = findViewById(R.id.selectedDeliveryAddress);

        // Total Fields


        subTotal = findViewById(R.id.subTotal);
        deliveryCharges = findViewById(R.id.deliveryCharges);
        total = findViewById(R.id.total);

        pickFromShopCheck = findViewById(R.id.radioPickFromShop);
        homeDelieryCheck = findViewById(R.id.radioHomeDelivery);



        // Bind View Ends


//        cartStats = getIntent().getParcelableExtra(CART_STATS_INTENT_KEY);

        String cartStatsJson = getIntent().getStringExtra(CART_STATS_INTENT_KEY);
        cartStats = UtilityFunctions.provideGson().fromJson(cartStatsJson, CartStats.class);


        if(savedInstanceState!=null)
        {
            selectedAddress = savedInstanceState.getParcelable("selectedAddress");
        }


        if(selectedAddress!=null)
        {
            addressContainer.setVisibility(View.VISIBLE);
            bindDataToViews(selectedAddress);
        }else
        {
            addressContainer.setVisibility(View.GONE);

        }




        if(PrefServiceConfig.getServiceName(this)!=null) {
            serviceName.setVisibility(View.VISIBLE);
            serviceName.setText(PrefServiceConfig.getServiceName(this));
        }



        if(cartStatsFromNetworkCall==null)
        {
            makeNetworkCall();
        }

    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, DeliveryAddressActivity.class);

        startActivityForResult(intent,1);



        //startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == 2 && data != null)
        {
            String jsonString = data.getStringExtra("output");
            selectedAddress = UtilityFunctions.provideGson().fromJson(jsonString,DeliveryAddress.class);

//            selectedAddress = data.getParcelableExtra("output");

            if(selectedAddress!=null)
            {
                addressContainer.setVisibility(View.VISIBLE);

                bindDataToViews(selectedAddress);

            }

        }

    }









    void bindDataToViews(DeliveryAddress deliveryAddress)
    {
        if(deliveryAddress != null)
        {
            name.setText(deliveryAddress.getName());

            String address  = deliveryAddress.getDeliveryAddress() + ", "
                    + deliveryAddress.getCity() + ", " + deliveryAddress.getPincode();



            deliveryAddressView.setText(address);

//            city.setText(deliveryAddress.getCity());
//            pincode.setText(String.valueOf(deliveryAddress.getPincode()));
//            landmark.setText(deliveryAddress.getLandmark());

            phoneNumber.setText("Phone : " + String.valueOf(deliveryAddress.getPhoneNumber()));
        }
    }









    void makeNetworkCall() {

        if (cartStats == null){

            return;
        }


        Call<List<CartStats>> call = cartStatsService.getCart(
                PrefLogin.getUser(this).getUserID(),null,
                cartStats.getShopID(),true,null,null
        );




        System.out.println("Cart ID : " + cartStats.getCartID());

        call.enqueue(new Callback<List<CartStats>>() {
            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {


                if(response!=null)
                {
                    cartStatsFromNetworkCall = response.body().get(0);
                    setTotal();
                    setRadioCheck();
                }
            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {

                showToastMessage("Network connection failed. Check Internet connectivity !");
            }
        });
    }







    void setRadioCheck()
    {
        if(cartStatsFromNetworkCall!=null)
        {
            Shop shop = cartStatsFromNetworkCall.getShop();

            if(shop!=null)
            {
                homeDelieryCheck.setEnabled(shop.getHomeDeliveryAvailable());
                pickFromShopCheck.setEnabled(shop.getPickFromShopAvailable());
            }
        }
    }





    void setTotal()
    {
        if(cartStatsFromNetworkCall!=null)
        {

            freeDeliveryInfo.setText("Free delivery is offered above the order of " + PrefGeneral.getCurrencySymbol(this) + " " + cartStatsFromNetworkCall.getShop().getBillAmountForFreeDelivery());


            subTotal.setText("Item Total: " + PrefGeneral.getCurrencySymbol(this) + " " + cartStats.getCart_Total());
            deliveryCharges.setText("Delivery Charges : N/A");

            //total.setText("Total : " + cartStats.getCart_Total()+ );

            if(pickFromShopCheck.isChecked())
            {
                total.setText("Net Payable : " + PrefGeneral.getCurrencySymbol(this) + " " + String.format( "%.2f", cartStats.getCart_Total()));
                deliveryCharges.setText("Delivery Charges : "+ PrefGeneral.getCurrencySymbol(this) + " " + 0);
            }



            if(homeDelieryCheck.isChecked())
            {


                if(cartStatsFromNetworkCall.getCart_Total() < cartStatsFromNetworkCall.getShop().getBillAmountForFreeDelivery())
                {

                    total.setText("Net Payable : " + PrefGeneral.getCurrencySymbol(this) + " " + String.format( "%.2f", cartStats.getCart_Total() + cartStats.getShop().getDeliveryCharges()));
                    deliveryCharges.setText("Delivery Charges : " + PrefGeneral.getCurrencySymbol(this) + " " + cartStats.getShop().getDeliveryCharges());
                }
                else
                {

                    deliveryCharges.setText("Delivery Charges : Zero " + "(Delivery is free above the order of : " + PrefGeneral.getCurrencySymbol(this) + " " + cartStatsFromNetworkCall.getShop().getBillAmountForFreeDelivery() + " )");
                    total.setText("Total : " + PrefGeneral.getCurrencySymbol(this) + " " + String.format( "%.2f", cartStats.getCart_Total()));
                }

            }
        }
    }







    @OnClick({R.id.radioPickFromShop, R.id.radioHomeDelivery})
    void radioCheckClicked()
    {
        setTotal();


        if(pickFromShopCheck.isChecked())
        {
            addPickAddress.setText("Pick Address");
            deliveryInstructions.setText("You need to pick the order from the shop. ");
        }
        else
        {
            addPickAddress.setText("Pick Delivery Address");
            deliveryInstructions.setText("Your order will be delivered to your home at your given address.");
        }
    }






    @OnClick(R.id.placeOrder)
    void placeOrderClick()
    {


        if(!pickFromShopCheck.isChecked() && !homeDelieryCheck.isChecked())
        {
            showToastMessage("Please select delivery type !");
            return;
        }


        if(selectedAddress==null)
        {
            showToastMessage("Please add/select Delivery Address !");
            return;
        }



        if(cartStatsFromNetworkCall==null)
        {
            showToastMessage("Network problem. Try again !");
            return;
        }



        order.setEndUserID(PrefLogin.getUser(this).getUserID());

        order.setDeliveryAddressID(selectedAddress.getId());
//        orderPFS.setDeliveryAddressID(selectedAddress.getId());

        if(pickFromShopCheck.isChecked())
        {
            order.setPickFromShop(true);
//            placeOrderPFS();
        }
        else if(homeDelieryCheck.isChecked())
        {
            order.setPickFromShop(false);
        }

        placeOrderHD();


//        order.setOrderStatus(1);

    }






    void placeOrderHD()
    {
        Call<ResponseBody> call = orderService.postOrder(order,cartStatsFromNetworkCall.getCartID());


        placeOrder.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                placeOrder.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                if(response.code() == 201)
                {
                    showToastMessage("Successful !");


                    Intent i = new Intent(PlaceOrderActivity.this, Home.class);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(i);

                }else
                {
                    showToastMessage("failed Code : !" + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                placeOrder.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                showToastMessage("Network connection Failed !");

            }
        });

    }






    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }




}
