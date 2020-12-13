package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

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

import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.OrderDetail;
import org.nearbyshops.enduserappnew.InventoryOrders.ViewModelOrders;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderShopAdminHD extends ViewHolderOrder {


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



    public static ViewHolderOrderShopAdminHD create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_double,parent,false);

        return new ViewHolderOrderShopAdminHD(view,context,fragment);
    }







    public ViewHolderOrderShopAdminHD(View itemView, Context context, Fragment fragment) {
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
        if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_PLACED||
                order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED||
                order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_PACKED||
                order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURNED_ORDERS)
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

        if(order.getDeliveryGuySelfID()>0 && order.getRt_delivery_guy_profile()!=null)
        {
            deliveryBoyInfo.setVisibility(View.VISIBLE);

            User deliveryBoy = order.getRt_delivery_guy_profile();
            deliveryBoyInfo.setText("Delivered By : " + deliveryBoy.getName() + " | ID : " + String.valueOf(deliveryBoy.getUserID()) + "");
        }
        else
        {
            deliveryBoyInfo.setVisibility(View.GONE);
        }



        pickupDirections.setText(String.format("%.2f Kms | Get Pickup Directions",order.getRt_pickupDistance()));
        deliveryDirections.setText(String.format("%.2f Kms | Get Delivery Directions",order.getRt_deliveryDistance()));

        pickupDirections.setVisibility(View.GONE);
        deliveryDirections.setVisibility(View.GONE);




        if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PLACED)
        {
            buttonLeft.setText(" Confirm ");
            buttonRight.setText(" Cancel ");

            buttonLeft.setVisibility(View.VISIBLE);
            buttonRight.setVisibility(View.GONE);
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
        {

            buttonLeft.setText(" Packed ");


            buttonLeft.setVisibility(View.VISIBLE);
            buttonRight.setVisibility(View.GONE);
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_PACKED)
        {

            buttonLeft.setText(" Deliver By Self ");


            if(order.getDeliveryGuySelfID()==0)
            {
                buttonLeft.setVisibility(View.VISIBLE);
            }

        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
        {

            if(order.getDeliveryGuySelfID()==0)
            {
                buttonLeft.setText(" Deliver ");
                buttonRight.setText(" Return ");

                buttonLeft.setVisibility(View.VISIBLE);
                buttonRight.setVisibility(View.VISIBLE);


                deliveryDirections.setVisibility(View.VISIBLE);
            }

        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURNED_ORDERS)
        {

            buttonLeft.setText(" Cancel Order ");


            buttonLeft.setVisibility(View.VISIBLE);
            buttonRight.setVisibility(View.GONE);
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
        {

            buttonLeft.setText(" Cancel Handover ");


            buttonLeft.setVisibility(View.VISIBLE);
            buttonRight.setVisibility(View.VISIBLE);
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURN_REQUESTED)
        {

            buttonLeft.setText(" Order Received ");


            buttonLeft.setVisibility(View.VISIBLE);
            buttonRight.setVisibility(View.GONE);
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.DELIVERED)
        {
            buttonLeft.setText(" Payment Received ");


            buttonLeft.setVisibility(View.VISIBLE);
            buttonRight.setVisibility(View.GONE);
        }

    }





    @OnClick(R.id.button_left)
    void leftButtonClick()
    {

        if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PLACED)
        {
            viewModelOrders.confirmOrderHD(order.getOrderID());
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
        {
            viewModelOrders.setOrderPackedHD(order.getOrderID());
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_PACKED)
        {
            viewModelOrders.setOutForDelivery(order.getOrderID());
        }
        if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
        {
            viewModelOrders.deliverToUserBySelf(order.getOrderID());
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURN_REQUESTED)
        {
            viewModelOrders.acceptReturnHD(order.getOrderID());
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURNED_ORDERS)
        {
//            viewModelOrders.unpackOrderHD(order.getOrderID());
            closeButton(null);

        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.DELIVERED)
        {
            viewModelOrders.paymentReceivedHD(order.getOrderID());
        }



        buttonLeft.setVisibility(View.INVISIBLE);
        progressLeft.setVisibility(View.VISIBLE);


//        if(fragment instanceof ListItemClick)
//        {
//            ((ListItemClick) fragment).buttonLeftClick(order,getAdapterPosition(), buttonLeft, progressLeft);
//        }
    }




    @OnClick(R.id.button_right)
    void rightButtonClick()
    {

        if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
        {
            viewModelOrders.returnOrderBySelf(order.getOrderID());
        }
        else
        {
            return;
        }


        buttonRight.setVisibility(View.INVISIBLE);
        progressRight.setVisibility(View.VISIBLE);

//        if(fragment instanceof ListItemClick)
//        {
//            ((ListItemClick) fragment).buttonRightClick(order,getAdapterPosition(), buttonRight,progressRight);
//        }
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


