package org.nearbyshops.enduserappnew.Checkout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import org.json.JSONObject;
import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.API.OrderService;
import org.nearbyshops.enduserappnew.API.RazorPayService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.HomePleaseSelectMarket;
import org.nearbyshops.enduserappnew.Lists.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduserappnew.Model.ModelBilling.RazorPayOrder;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import java.sql.Date;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class PlaceOrderFragment extends Fragment implements View.OnClickListener, PaymentResultListener {


    Order order = new Order();
    double orderTotal;




    @Inject
    CartStatsService cartStatsService;


    @Inject
    OrderService orderService;



    @Inject
    RazorPayService transactionService;



//    CartStats cartStats;
    CartStats cartStats;

    int shopID;


    String razorPayKey;




    TextView addOrSaveAddress;
    DeliveryAddress selectedAddress;




    // Total Fields
    TextView subTotal;
    TextView deliveryCharges;
    @BindView(R.id.market_fee) TextView marketFee;
    TextView total;


    @BindView(R.id.free_delivery_info) TextView freeDeliveryInfo;
    @BindView(R.id.radioPickFromShop) RadioButton pickFromShopCheck;
    @BindView(R.id.radioHomeDelivery) RadioButton homeDelieryCheck;
    @BindView(R.id.placeOrder) TextView placeOrder;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.delivery_instructions) TextView deliveryInstructions;

    @BindView(R.id.instructions_date_selection) TextView instructionsDateSelection;
    @BindView(R.id.delivery_mode_block) LinearLayout deliveryModeBlock;
    @BindView(R.id.instructions_slot_selection) TextView instructionsSlotSelection;


    @BindView(R.id.delivery_asap) TextView deliveryASAP;
    @BindView(R.id.delivery_today) TextView deliveryToday;
    @BindView(R.id.delivery_tomorrow) TextView deliveryTomorrow;


    @BindView(R.id.delivery_by_shop) TextView deliveryByShop;
    @BindView(R.id.delivery_by_market) TextView deliveryByMarket;






    // address views
    TextView name;
    TextView deliveryAddressView;
//    TextView city;
//    TextView pincode;
//    TextView landmark;
    TextView phoneNumber;
    LinearLayout addressContainer;
    @BindView(R.id.selected_payment_block) LinearLayout selectedPaymentBlock;
    @BindView(R.id.selected_payment_method) TextView selectedPayment;

    // address views ends



    public final static String CART_STATS_INTENT_KEY = "cart_stats_intent_key";

    @BindView(R.id.service_name) TextView serviceName;



    public PlaceOrderFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_place_order, container, false);
        ButterKnife.bind(this, rootView);

//        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        addOrSaveAddress = rootView.findViewById(R.id.pickFromSavedAddresses);
        addOrSaveAddress.setOnClickListener(this);


        name = rootView.findViewById(R.id.name);
        deliveryAddressView = rootView.findViewById(R.id.deliveryAddress);
        phoneNumber = rootView.findViewById(R.id.phoneNumber);
        addressContainer = rootView.findViewById(R.id.selectedDeliveryAddress);


        // Total Fields
        subTotal = rootView.findViewById(R.id.subTotal);
        deliveryCharges = rootView.findViewById(R.id.deliveryCharges);
        total = rootView.findViewById(R.id.total);

        pickFromShopCheck = rootView.findViewById(R.id.radioPickFromShop);
        homeDelieryCheck = rootView.findViewById(R.id.radioHomeDelivery);



        // Bind View Ends

//        String cartStatsJson = getActivity().getIntent().getStringExtra(CART_STATS_INTENT_KEY);
//        cartStats = UtilityFunctions.provideGson().fromJson(cartStatsJson, CartStats.class);

        shopID = getActivity().getIntent().getIntExtra("shop_id",0);



        if(cartStats ==null)
        {
            getCartStats();
        }




        bindDeliveryAddress();

        bindPaymentMode();




        if(PrefServiceConfig.getServiceName(getActivity())!=null) {
            serviceName.setVisibility(View.VISIBLE);
            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
        }




        deliveryTodayClick();


        // Preload razorpay apis for fast access
        Checkout.preload(getApplicationContext());


        return rootView;
    }







    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(), DeliveryAddressActivity.class);
        startActivityForResult(intent,1);
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == 2 && data != null)
        {
//            String jsonString = data.getStringExtra("output");
//            selectedAddress = UtilityFunctions.provideGson().fromJson(jsonString,DeliveryAddress.class);

            bindDeliveryAddress();

        }
        else if(requestCode==7896 && resultCode == 2 && data!=null)
        {
            int paymentMode = data.getIntExtra("output",Order.PAYMENT_MODE_CASH_ON_DELIVERY);
            order.setPaymentMode(paymentMode);

            razorPayKey = data.getStringExtra("razor_pay_key_id");


            if(paymentMode==Order.PAYMENT_MODE_RAZORPAY)
            {
                Checkout.preload(getApplicationContext());
            }

            bindPaymentMode();
        }
    }








    void bindPaymentMode()
    {


        if(order.getPaymentMode()==0)
        {
            selectedPaymentBlock.setVisibility(View.GONE);
        }
        else
        {
            selectedPaymentBlock.setVisibility(View.VISIBLE);

            if(order.getPaymentMode()==Order.PAYMENT_MODE_CASH_ON_DELIVERY)
            {
                selectedPayment.setText("Cash On Delivery");
            }
            else if(order.getPaymentMode()==Order.PAYMENT_MODE_PAY_ONLINE_ON_DELIVERY)
            {
                selectedPayment.setText("Pay Online on Delivery");
            }
            else if(order.getPaymentMode()==Order.PAYMENT_MODE_RAZORPAY)
            {
                selectedPayment.setText("Pay using RazorPay");
            }
        }
    }






    void bindDeliveryAddress()
    {

        selectedAddress = PrefLocation.getDeliveryAddress(getActivity());


        if(selectedAddress != null)
        {
            addressContainer.setVisibility(View.VISIBLE);
            addOrSaveAddress.setText(R.string.change_address);


            name.setText(selectedAddress.getName());

            String address  = selectedAddress.getDeliveryAddress() + " ";


//            String address  = selectedAddress.getDeliveryAddress() + ", "
//                    + selectedAddress.getCity() + ", " + selectedAddress.getPincode();


            deliveryAddressView.setText(address);

//            city.setText(deliveryAddress.getCity());
//            pincode.setText(String.valueOf(deliveryAddress.getPincode()));
//            landmark.setText(deliveryAddress.getLandmark());

            phoneNumber.setText("Phone : " + String.valueOf(selectedAddress.getPhoneNumber()));
        }
        else
        {
            addOrSaveAddress.setText(R.string.pick_address);
            addressContainer.setVisibility(View.GONE);
        }


    }





    void getCartStats() {


        User user = PrefLogin.getUser(getActivity());


        Call<CartStats> call = cartStatsService.getCartStats(
                user.getUserID(),shopID,
                true,true
        );


        call.enqueue(new Callback<CartStats>() {
            @Override
            public void onResponse(Call<CartStats> call, Response<CartStats> response) {

                if(response.code()==200)
                {
                    cartStats = response.body();
                    displayTotal();
                    setRadioVisibility();
                }
            }

            @Override
            public void onFailure(Call<CartStats> call, Throwable t) {

                showToastMessage("Network connection failed. Check Internet connectivity !");
            }
        });

    }







    void setRadioVisibility()
    {
        if(cartStats !=null)
        {
            Shop shop = cartStats.getShop();

            if(shop!=null)
            {
                homeDelieryCheck.setEnabled(shop.getHomeDeliveryAvailable());
                pickFromShopCheck.setEnabled(shop.getPickFromShopAvailable());
            }
        }
    }





    void displayTotal()
    {

        if(cartStats ==null)
        {
            return;
        }



        subTotal.setText("Item Total: " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + UtilityFunctions.refinedStringWithDecimals(cartStats.getCart_Total()));
        deliveryCharges.setText("Delivery Charges : N/A");

        //total.setText("Total : " + cartStats.getCart_Total()+ );

        if(pickFromShopCheck.isChecked())
        {
            double marketFeeValue = 0;

            if(cartStats.getDeliveryConfig().getMarketFeeForPickup()>0 && cartStats.getDeliveryConfig().isMarketFeeAddedToBill())
            {
                marketFeeValue = cartStats.getDeliveryConfig().getMarketFeeForPickup();

                marketFee.setVisibility(View.VISIBLE);
                marketFee.setText("Market Fee : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.format("%.2f",marketFeeValue));
            }


            orderTotal = cartStats.getCart_Total() + marketFeeValue;

            total.setText("Net Payable : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.format( "%.2f", orderTotal));
            deliveryCharges.setText("Delivery Charges : "+ PrefGeneral.getCurrencySymbol(getActivity()) + " " + 0);
        }






        if(homeDelieryCheck.isChecked())
        {
            double marketFeeValue = 0;

            if(cartStats.getDeliveryConfig().getMarketFeeForDelivery()>0 && cartStats.getDeliveryConfig().isMarketFeeAddedToBill())
            {
                marketFee.setVisibility(View.VISIBLE);
                marketFee.setText("Market Fee : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.format("%.2f",cartStats.getDeliveryConfig().getMarketFeeForDelivery()));

                marketFeeValue = cartStats.getDeliveryConfig().getMarketFeeForDelivery();
            }





            if(cartStats.getDeliveryConfig().isUseStandardDeliveryCharge())
            {
//                freeDeliveryInfo.setVisibility(View.VISIBLE);
//                freeDeliveryInfo.setText("Delivery Fee : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + cartStats.getDeliveryConfig().getMarketDeliveryCharge());


                orderTotal = cartStats.getCart_Total() + cartStats.getDeliveryConfig().getMarketDeliveryCharge() + marketFeeValue;

                total.setText("Net Payable : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.format( "%.2f", orderTotal));
                deliveryCharges.setText("Delivery Charges : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + cartStats.getDeliveryConfig().getMarketDeliveryCharge());

            }
            else
            {


                freeDeliveryInfo.setVisibility(View.VISIBLE);
                freeDeliveryInfo.setText("Free delivery is offered above the order of " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + cartStats.getShop().getBillAmountForFreeDelivery());



                if(cartStats.getCart_Total() < cartStats.getShop().getBillAmountForFreeDelivery())
                {
                    orderTotal = cartStats.getCart_Total() + cartStats.getShop().getDeliveryCharges() + marketFeeValue;

                    total.setText("Net Payable : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.format( "%.2f", orderTotal));
                    deliveryCharges.setText("Delivery Charges : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + cartStats.getShop().getDeliveryCharges());
                }
                else
                {

                    orderTotal = cartStats.getCart_Total()+ marketFeeValue;

                    deliveryCharges.setText("Delivery Charges : Zero " + "(Delivery is free above the order of : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + cartStats.getShop().getBillAmountForFreeDelivery() + " )");
                    total.setText("Net Payable : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.format( "%.2f", orderTotal)) ;
                }

            }
        }
    }







    @OnClick({R.id.radioPickFromShop, R.id.radioHomeDelivery})
    void radioCheckClicked()
    {
        displayTotal();


        if(pickFromShopCheck.isChecked())
        {
//            addOrSaveAddress.setText("Pick Address");
            deliveryInstructions.setText("You need to pick the order from the shop. ");

            instructionsDateSelection.setText("Select Pickup Date");
            deliveryModeBlock.setVisibility(View.GONE);
            instructionsSlotSelection.setText("Select Pickup Time");

            order.setDeliveryMode(Order.DELIVERY_MODE_PICKUP_FROM_SHOP);


//            getDeliverySlots();


        }
        else
        {
//            addOrSaveAddress.setText("Pick Delivery Address");
            deliveryInstructions.setText("Your order will be delivered to your home at your given address.");


            instructionsDateSelection.setText("Select Delivery Date");
//            deliveryModeBlock.setVisibility(View.VISIBLE);

            instructionsSlotSelection.setText("Select Delivery Slot");



            order.setDeliveryMode(Order.DELIVERY_MODE_HOME_DELIVERY);


//            deliveryByShopClick();
        }
    }





    void clearDeliveryType()
    {
        deliveryByShop.setBackgroundColor(getResources().getColor(R.color.light_grey));
        deliveryByShop.setTextColor(getResources().getColor(R.color.blueGrey800));

        deliveryByMarket.setBackgroundColor(getResources().getColor(R.color.light_grey));
        deliveryByMarket.setTextColor(getResources().getColor(R.color.blueGrey800));
    }






    void clearDeliveryDate()
    {
        deliveryASAP.setBackgroundColor(getResources().getColor(R.color.light_grey));
        deliveryASAP.setTextColor(getResources().getColor(R.color.blueGrey800));

        deliveryToday.setBackgroundColor(getResources().getColor(R.color.light_grey));
        deliveryToday.setTextColor(getResources().getColor(R.color.blueGrey800));

        deliveryTomorrow.setBackgroundColor(getResources().getColor(R.color.light_grey));
        deliveryTomorrow.setTextColor(getResources().getColor(R.color.blueGrey800));
    }



    @OnClick(R.id.delivery_asap)
    void deliveryASAP()
    {
        clearDeliveryDate();

        deliveryASAP.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        deliveryASAP.setTextColor(getResources().getColor(R.color.white));


        order.setDeliveryDate(null);
        order.setDeliverySlotID(0);
    }




    @OnClick(R.id.delivery_today)
    void deliveryTodayClick()
    {
        clearDeliveryDate();

        deliveryToday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        deliveryToday.setTextColor(getResources().getColor(R.color.white));


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);

        order.setDeliveryDate(new Date(calendar.getTimeInMillis()));


    }





    @OnClick(R.id.delivery_tomorrow)
    void deliveryTomorrowClick()
    {
        clearDeliveryDate();

        deliveryTomorrow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        deliveryTomorrow.setTextColor(getResources().getColor(R.color.white));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.add(Calendar.DATE,1);

        order.setDeliveryDate(new Date(calendar.getTimeInMillis()));
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

        if(order.getPaymentMode()==0)
        {
            showToastMessage("Please select payment method");
            return;
        }


        if(cartStats ==null)
        {
            showToastMessage("Network problem. Try again !");
            return;
        }



        order.setEndUserID(PrefLogin.getUser(getActivity()).getUserID());

        order.setDeliveryAddressID(selectedAddress.getId());
        order.setNetPayable(orderTotal);
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



        placeOrderHD(false);
    }






    void placeOrderHD(boolean paymentDone)
    {

        placeOrder.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


//        if(order.getPaymentMode()==Order.PAYMENT_MODE_PAY_ONLINE_ON_DELIVERY && !paymentDone)
//        {
//            startUPIPayment();
//            return;
//        }

        if(order.getPaymentMode()==Order.PAYMENT_MODE_RAZORPAY && !paymentDone)
        {
            createOrderRazorPay();
            return;
        }



        Call<ResponseBody> call = orderService.postOrder(order, cartStats.getCartID());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (isDetached()) {
                    return;
                }


                placeOrder.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                if(response.code() == 201)
                {
                    showToastMessage("Successful !");


                    

                    Intent i = new Intent(getActivity(), HomePleaseSelectMarket.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);


//
//                    if(getActivity()!=null)
//                    {
//                        getActivity().finish();
//                    }


                }
                else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if (isDetached()) {
                    return;
                }



                placeOrder.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                showToastMessage("Network connection Failed !");

            }
        });

    }








    void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }







    @OnClick(R.id.select_payment)
    void selectPaymentClick()
    {
        Intent intent = new Intent(getActivity(),SelectPayment.class);
        startActivityForResult(intent,7896);
    }



    RazorPayOrder razorPayOrder;



    public void createOrderRazorPay()
    {
        razorPayOrder = new RazorPayOrder();



        Call<RazorPayOrder> call = transactionService.createOrder(orderTotal);

        call.enqueue(new Callback<RazorPayOrder>() {
            @Override
            public void onResponse(Call<RazorPayOrder> call, Response<RazorPayOrder> response) {


                if(response.code()==200)
                {
                    razorPayOrder.setRzpOrderID(response.body().getRzpOrderID());
                    startPaymentRazorpay();
                }
                else
                {


                    placeOrder.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    showToastMessage("RazorPay failed ... Code : " + response.code() + " Choose another method !");
                }
            }

            @Override
            public void onFailure(Call<RazorPayOrder> call, Throwable t) {

                showToastMessage("Create Order Failed !");



                placeOrder.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }






    public void startPaymentRazorpay() {


        Checkout checkout = new Checkout();
        checkout.setKeyID(razorPayKey);



//        checkout.setImage(R.drawable.logo);

        final Activity activity = getActivity();

        try {
            JSONObject options = new JSONObject();


            options.put("name", "Merchant Name");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", razorPayOrder.getRzpOrderID());
            options.put("currency", "INR");
            options.put("amount", String.valueOf(orderTotal));

            checkout.open(activity, options);



        } catch(Exception e) {

            Log.e("Razorpay", "Error in starting Razorpay Checkout", e);
        }
    }




    void startUPIPayment()
    {
        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(getActivity())
                .setPayeeVpa(getString(R.string.vpa_for_upi_payment))
                .setPayeeName("Sumeet")
                .setTransactionId("1fadfoaidjf6")
                .setTransactionRefId("ijiaerwrf")
                .setDescription("Payment for Nearby Shops")
                .setAmount(String.valueOf(orderTotal))
                .build();

//        easyUpiPayment.setDefaultPaymentApp(PaymentApp.GOOGLE_PAY);
        easyUpiPayment.startPayment();


        easyUpiPayment.setPaymentStatusListener(new PaymentStatusListener() {
            @Override
            public void onTransactionCompleted(TransactionDetails transactionDetails) {

            }

            @Override
            public void onTransactionSuccess() {

                showToastMessage("Successful !");
                placeOrderHD(true);

            }

            @Override
            public void onTransactionSubmitted() {

            }

            @Override
            public void onTransactionFailed() {


                showToastMessage("Payment Failed !");
            }

            @Override
            public void onTransactionCancelled() {

                showToastMessage("Payment Cancelled !");
            }

            @Override
            public void onAppNotFound() {

                showToastMessage("App not found !");
            }
        });
    }




    @Override
    public void onPaymentSuccess(String s) {


        placeOrder.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);


//        showToastMessage(s);

        razorPayOrder.setRzpPaymentID(s);
        order.setRazorPayOrder(razorPayOrder);


        placeOrderHD(true);
    }



    @Override
    public void onPaymentError(int i, String s) {



        placeOrder.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);


        showToastMessage(s);
    }
}
