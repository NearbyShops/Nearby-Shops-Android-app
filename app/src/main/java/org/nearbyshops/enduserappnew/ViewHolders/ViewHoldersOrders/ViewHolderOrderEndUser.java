package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.OrderDetail;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.ViewModelOrders;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderEndUser extends RecyclerView.ViewHolder {



    @BindView(R.id.shop_image) ImageView shopImage;
    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_address) TextView shopAddress;

    @BindView(R.id.order_id) TextView orderID;
    @BindView(R.id.order_date) TextView orderDate;
    @BindView(R.id.order_total) TextView orderTotal;


    @BindView(R.id.delivery_mode)
    public TextView deliveryMode;

    @BindView(R.id.delivery_slot_name)
    TextView deliverySlotName;


    @BindView(R.id.order_status) TextView orderStatus;

    @BindView(R.id.cancel_order) TextView cancelOrder;

    @BindView(R.id.cancelled_image)
    public ImageView cancelledImage;


    public int statusUpdateInitiated = 0;



    private Context context;
    private Order order;
    private Fragment fragment;






    public static ViewHolderOrderEndUser create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order_end_user,parent,false);
        return new ViewHolderOrderEndUser(view,context,fragment);
    }





    public ViewHolderOrderEndUser(View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.context = context;
        this.fragment = fragment;


        setupViewModel();
    }




    @OnClick(R.id.list_item)
    void listItemClick ()
    {

        fragment.startActivity(OrderDetail.getLaunchIntent(order.getOrderID(),fragment.getActivity()));


        if (fragment instanceof ListItemClick) {

            ((ListItemClick) fragment).notifyOrderSelected(order);
        }
    }





    public void setItem (Order order)
    {
        this.order = order;

        Shop shop = order.getShop();

        orderID.setText("# Order ID : " + order.getOrderID());
        orderDate.setText("Placed : " + order.getDateTimePlaced().toLocaleString());
        orderTotal.setText(order.getItemCount() + " Items | Total : " + PrefGeneral.getCurrencySymbol(context) + String.format(" %.2f", order.getNetPayable()));


        if(shop!=null)
        {
            shopName.setText(shop.getShopName());
            shopAddress.setText(shop.getShopAddress());


            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                    + shop.getLogoImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_items_grey, context.getTheme());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                shopImage.setClipToOutline(true);
            }

            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(shopImage);
        }


        bindStatusNew();
        setupCancelButton();
        setupCancelImage();
        bindDeliveryMode();
    }



    void bindStatusNew()
    {
        String status = "";


        if(order.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY) {


            status = OrderStatusHomeDelivery.getStatusString(order.getStatusHomeDelivery());

        }
        else if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
        {

            status = OrderStatusPickFromShop.getStatusString(order.getStatusPickFromShop());
        }


        orderStatus.setText("" + status);
    }







    void setupCancelButton()
    {
        cancelOrder.setVisibility(View.GONE);


        if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
        {

            if(order.getStatusPickFromShop()== OrderStatusPickFromShop.ORDER_PLACED||
                    order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_CONFIRMED||
                    order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_PACKED||
                    order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
            {
                cancelOrder.setVisibility(View.VISIBLE);
            }

        }
        else
        {


            if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_PLACED||
                    order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED||
                    order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_PACKED)
            {
                cancelOrder.setVisibility(View.VISIBLE);
            }

        }



        if(context.getResources().getBoolean(R.bool.hide_cancel_button_from_customers))
        {
            cancelOrder.setVisibility(View.GONE);
        }

    }





    void setupCancelImage()
    {
        if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
        {
            int statusCode = order.getStatusPickFromShop();


            if (statusCode == OrderStatusPickFromShop.CANCELLED) {
                cancelledImage.setVisibility(View.VISIBLE);
            } else {
                cancelledImage.setVisibility(View.GONE);
            }


        }
        else
        {

            int statusCode = order.getStatusHomeDelivery();

            if (statusCode == OrderStatusHomeDelivery.CANCELLED_WITH_DELIVERY_GUY ||
                    statusCode == OrderStatusHomeDelivery.CANCELLED) {
                cancelledImage.setVisibility(View.VISIBLE);
            } else {
                cancelledImage.setVisibility(View.GONE);
            }

        }

    }





    void bindDeliveryMode()
    {


        if(order.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY)
        {
//            deliveryMode.setBackgroundColor(ContextCompat.getColor(context, R.color.mapbox_blue));
            deliveryMode.setBackgroundResource(R.drawable.tag_delivery);
            deliveryMode.setText("Delivery");
        }
        else if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
        {
//            deliveryMode.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
            deliveryMode.setBackgroundResource(R.drawable.tag_pickup);
            deliveryMode.setText("Pickup");
        }



        String deliveySlotString = "";

        DeliverySlot deliverySlot = order.getDeliverySlot();

        if(deliverySlot!=null && deliverySlot.getSlotName()!=null)
        {
            deliveySlotString  = deliverySlot.getSlotName();

            if(order.getDeliveryDate()!=null)
            {
                deliveySlotString = deliveySlotString + " | " + order.getDeliveryDate().toLocaleString();
            }
        }
        else
        {
            deliveySlotString = " ASAP ";
        }


        deliverySlotName.setText(deliveySlotString);

    }




    @OnClick(R.id.cancel_order)
    void closeButton(View view) {


        User user = PrefLogin.getUser(context);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm Cancel Order !")
                .setMessage("Are you sure you want to cancel this order !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        statusUpdateInitiated = OrderStatusHomeDelivery.CANCELLED;

                        viewModelOrders.cancelOrderByEndUser(order.getOrderID(),user.getUserID());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage(" Not Cancelled !");

                    }
                })
                .show();

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
                    if(fragment instanceof ViewHolderOrderShopAdminHD.ListItemClick)
                    {
                        ((ViewHolderOrderShopAdminHD.ListItemClick) fragment).statusUpdateSuccessful(order,getAdapterPosition());
                    }


                    if(statusUpdateInitiated!=0)
                    {
                        order.setStatusHomeDelivery(statusUpdateInitiated);
                        order.setStatusPickFromShop(statusUpdateInitiated);

                        ViewHolderOrderEndUser.this.setItem(order);
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




    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order);
    }


}


