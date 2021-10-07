package org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderInventory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import org.nearbyshops.whitelabelapp.CartAndOrder.DetailOrder.OrderDetail;
import org.nearbyshops.whitelabelapp.InventoryOrders.ViewModelOrders;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.whitelabelapp.Model.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderOrder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderShopAdminPFS extends ViewHolderOrder {


    @BindView(R.id.button_left) TextView buttonLeft;
    @BindView(R.id.progress_left) ProgressBar progressLeft;

    @BindView(R.id.button_right) TextView buttonRight;
    @BindView(R.id.progress_right) ProgressBar progressRight;

    @BindView(R.id.pickup_directions) TextView pickupDirections;
    @BindView(R.id.delivery_directions) TextView deliveryDirections;
    @BindView(R.id.delivery_boy_info) TextView deliveryBoyInfo;




    private Context context;
    private Order order;
    private Fragment fragment;



    public static ViewHolderOrderShopAdminPFS create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_double,parent,false);

        return new ViewHolderOrderShopAdminPFS(view,context,fragment);
    }







    public ViewHolderOrderShopAdminPFS(View itemView, Context context, Fragment fragment) {
        super(itemView,context,fragment);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;

        setupViewModel();
    }






    @OnClick(R.id.close_button)
    void closeButton(View view) {

        Shop shop = PrefShopAdminHome.getShop(context);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm Cancel Order !")
                .setMessage("Are you sure you want to cancel this order !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        viewModelOrders.cancelOrder(order.getOrderID(),shop.getShopID());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage(" Not Cancelled !");


                        buttonLeft.setVisibility(View.VISIBLE);
                        progressLeft.setVisibility(View.INVISIBLE);

                        if(buttonRight.getVisibility()==View.INVISIBLE)
                        {
                            buttonRight.setVisibility(View.VISIBLE);
                            progressRight.setVisibility(View.INVISIBLE);
                        }


                    }
                })
                .show();

    }






    public void setItem (Order order)
    {
        super.setItem(order);
        this.order = order;

        setupButtons();
        setupCancelButton();
    }






    void setupCancelButton()
    {
        if(order.getStatusCurrent()== OrderStatusPickFromShop.ORDER_PLACED||
                order.getStatusCurrent()==OrderStatusPickFromShop.ORDER_CONFIRMED||
                order.getStatusCurrent()==OrderStatusPickFromShop.ORDER_PACKED||
                order.getStatusCurrent()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
        {
            closeButton.setVisibility(View.VISIBLE);
        }
    }




    void setupButtons()
    {
        String buttonLeftTitle = "";
        String buttonRightTitle = "";


        buttonLeft.setVisibility(View.GONE);
        buttonRight.setVisibility(View.GONE);


//        pickupDirections.setText(String.format("%.2f Kms | Get Pickup Directions",order.getRt_pickupDistance()));
        deliveryDirections.setText(String.format("%.2f Kms | Get Delivery Directions",order.getRt_deliveryDistance()));

        pickupDirections.setVisibility(View.GONE);
        deliveryDirections.setVisibility(View.GONE);




        if(order.getStatusCurrent()== OrderStatusPickFromShop.ORDER_PLACED)
        {
            buttonLeft.setText(" Confirm ");
            buttonRight.setText(" Cancel ");

            buttonLeft.setVisibility(View.VISIBLE);
        }
        else if(order.getStatusCurrent()==OrderStatusPickFromShop.ORDER_CONFIRMED)
        {

            buttonLeft.setText(" Packed ");
            buttonLeft.setVisibility(View.VISIBLE);

        }
        else if(order.getStatusCurrent()==OrderStatusPickFromShop.ORDER_PACKED)
        {
            buttonLeft.setText(" Ready for Pickup ");
            buttonLeft.setVisibility(View.VISIBLE);
        }
        else if(order.getStatusCurrent()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
        {

            buttonLeft.setText(" Payment Received ");
            buttonLeft.setVisibility(View.VISIBLE);
        }



    }





    @OnClick(R.id.button_left)
    void leftButtonClick()
    {

        if(order.getStatusCurrent()== OrderStatusPickFromShop.ORDER_PLACED)
        {

            viewModelOrders.confirmOrderPFS(order.getOrderID());

        }
        else if(order.getStatusCurrent()==OrderStatusPickFromShop.ORDER_CONFIRMED)
        {
            viewModelOrders.setOrderPackedPFS(order.getOrderID());
        }
        else if(order.getStatusCurrent()==OrderStatusPickFromShop.ORDER_PACKED)
        {

            viewModelOrders.setReadyForPickupPFS(order.getOrderID());
        }
        else if(order.getStatusCurrent()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
        {
            viewModelOrders.setPaymentReceivedPFS(order.getOrderID());
        }



        buttonLeft.setVisibility(View.INVISIBLE);
        progressLeft.setVisibility(View.VISIBLE);
    }




    @OnClick(R.id.button_right)
    void rightButtonClick()
    {

    }






    @OnClick({R.id.pickup_directions})
    void getDirectionsClick()
    {

        Shop shop = order.getShop();

        if(shop!=null)
        {
            getDirections(shop.getLatCenter(),shop.getLonCenter());
        }
    }




    @OnClick({R.id.delivery_directions})
    void getDeliveryDirectionsClick()
    {


        DeliveryAddress deliveryAddress = order.getDeliveryAddress();

        if(deliveryAddress!=null)
        {
            getDirections(deliveryAddress.getLatitude(),deliveryAddress.getLongitude());
        }
    }





    private void getDirections(double lat, double lon)
    {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        fragment.startActivity(mapIntent);
    }







    void seeOnMap(double lat,double lon,String label)
    {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lon + "(" + label + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        fragment.startActivity(mapIntent);
    }







    private ViewModelOrders viewModelOrders;



    void setupViewModel()
    {

        viewModelOrders = new ViewModelOrders(MyApplication.application);


        viewModelOrders.getEvent().observe(fragment, new Observer<Integer>() {
            @Override
            public void onChanged(Integer event) {



                if(event == ViewModelOrders.EVENT_RESPONSE_OK)
                {
                    if(fragment instanceof ListItemClick)
                    {
                        ((ListItemClick) fragment).statusUpdateSuccessful(order,getAdapterPosition());
                    }

                }


                if(event == ViewModelOrders.EVENT_RESPONSE_OK || event== ViewModelOrders.EVENT_NETWORK_FAILED)
                {
                    buttonLeft.setVisibility(View.VISIBLE);
                    progressLeft.setVisibility(View.INVISIBLE);

                    if(buttonRight.getVisibility()==View.INVISIBLE)
                    {
                        buttonRight.setVisibility(View.VISIBLE);
                        progressRight.setVisibility(View.INVISIBLE);
                    }
                }


            }
        });




        viewModelOrders.getMessage().observe(fragment, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                showToastMessage(s);
            }
        });



    }







    void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(context,message);
    }





    @OnClick(R.id.list_item)
    void listItemClick ()
    {
        fragment.startActivity(OrderDetail.getLaunchIntent(order.getOrderID(),fragment.getActivity()));


        if (fragment instanceof ListItemClick) {

            ((ListItemClick) fragment).notifyOrderSelected(order);
        }
    }







    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void statusUpdateSuccessful(Order order, int position);
    }


}


