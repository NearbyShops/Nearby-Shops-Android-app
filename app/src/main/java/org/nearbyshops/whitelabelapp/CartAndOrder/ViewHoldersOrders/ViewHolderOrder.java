package org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.whitelabelapp.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.whitelabelapp.Model.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;




public class ViewHolderOrder extends RecyclerView.ViewHolder {


    @BindView(R.id.order_id)
    public TextView orderID;

    @BindView(R.id.dateTimePlaced)
    public TextView dateTimePlaced;

    @BindView(R.id.delivery_mode)
    public TextView deliveryMode;

    @BindView(R.id.delivery_slot_name)
    TextView deliverySlotName;


    @BindView(R.id.deliveryAddressName)
    public TextView deliveryAddressName;

    @BindView(R.id.deliveryAddress)
    public TextView deliveryAddress;

    @BindView(R.id.deliveryAddressPhone)
    public TextView deliveryAddressPhone;


    @BindView(R.id.numberOfItems)
    public TextView numberOfItems;

    @BindView(R.id.orderTotal)
    public TextView orderTotal;

    @BindView(R.id.currentStatus)
    public TextView currentStatus;

//        @Bind(R.id.confirmOrderButton)
//        TextView confirmOrderButton;

    @BindView(R.id.close_button)
    public ImageView closeButton;

    @BindView(R.id.cancelled_image)
    public ImageView cancelledImage;

    @BindView(R.id.payment_mode)
    TextView paymentMode;


    private Context context;
    private Order order;
    private Fragment fragment;






    public static ViewHolderOrder create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order,parent,false);
        return new ViewHolderOrder(view,context,fragment);
    }





    public ViewHolderOrder(View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.context = context;
        this.fragment = fragment;
    }




    @OnClick(R.id.deliveryAddressPhone)
    void phoneClick()
    {
        UtilityFunctions.dialPhoneNumber(String.valueOf(order.getDeliveryAddress().getPhoneNumber()),context);
    }




    @OnClick(R.id.close_button)
    void closeButton(View view) {

        if (fragment instanceof ListItemClick) {
            ((ListItemClick) fragment).notifyCancelOrder(order);
        }
    }






    @OnClick(R.id.list_item)
    void listItemClick ()
    {

        if (fragment instanceof ListItemClick) {

            ((ListItemClick) fragment).notifyOrderSelected(order);
        }
    }





    public void setItem (Order order)
    {
        this.order = order;

        DeliveryAddress deliveryAddressLocal = order.getDeliveryAddress();


        orderID.setText("Order ID : " + order.getOrderID());
        dateTimePlaced.setText("Placed : " + order.getDateTimePlaced().toLocaleString());



        if(order.getDeliveryAddress().getName()==null)
        {
            deliveryAddressName.setText("Delivery Address Deleted\nor Not provided");
            deliveryAddress.setText(" - ");
            deliveryAddressPhone.setText(" - ");

        }
        else
        {

            deliveryAddressName.setText(deliveryAddressLocal.getName());

//            deliveryAddress.setText(deliveryAddressLocal.getDeliveryAddress() + ", "
//                    + deliveryAddressLocal.getCity() + " - " + deliveryAddressLocal.getPincode());


            deliveryAddress.setText(deliveryAddressLocal.getDeliveryAddress());

            User user = PrefLogin.getUser(context);


            if(context.getResources().getBoolean(R.bool.hide_customer_phone_from_vendors) &&
                    (user.getRole()==User.ROLE_SHOP_ADMIN_CODE || user.getRole()==User.ROLE_SHOP_STAFF_CODE || user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE))
            {
                deliveryAddressPhone.setVisibility(View.GONE);
            }
            else
            {
                deliveryAddressPhone.setText("Phone : " + deliveryAddressLocal.getPhoneNumber());
            }

        }



        numberOfItems.setText(order.getItemCount() + " Items");
        orderTotal.setText("| Total : " + PrefCurrency.getCurrencySymbol(context) + String.format(" %.2f", order.getNetPayable()));


        bindStatusNew();
        bindDeliveryMode();
        bindCancelButton();
        bindPaymentMode();
    }






    void bindPaymentMode()
    {
        if(order.getPaymentMode()==Order.PAYMENT_MODE_CASH_ON_DELIVERY)
        {
            paymentMode.setText(" COD ");
        }
        else if(order.getPaymentMode()==Order.PAYMENT_MODE_PAY_ONLINE_ON_DELIVERY)
        {
            paymentMode.setText(" POD ");
        }
        else if(order.getPaymentMode()==Order.PAYMENT_MODE_RAZORPAY)
        {
            paymentMode.setText(" Paid ");
        }
    }




    void bindDeliveryMode()
    {


        if(order.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY)
        {
            deliveryMode.setBackgroundColor(ContextCompat.getColor(context, R.color.mapbox_blue));
            deliveryMode.setText("Home Delivery");
        }
        else if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
        {
            deliveryMode.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
            deliveryMode.setText("Pick From Shop");
        }



//        String deliveySlotString = "";
//
//        DeliverySlot deliverySlot = order.getDeliverySlot();
//
//        if(deliverySlot!=null && deliverySlot.getSlotName()!=null)
//        {
//            deliveySlotString  = deliverySlot.getSlotName();
//
//            if(order.getDeliveryDate()!=null)
//            {
//                deliveySlotString = deliveySlotString + " | " + order.getDeliveryDate().toLocaleString();
//            }
//        }
//        else
//        {
//            deliveySlotString = " ASAP ";
//        }
//
//
//        deliverySlotName.setText(deliveySlotString);

    }





    void bindStatusNew()
    {
        String status = "";


        if(order.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY) {


            status = OrderStatusHomeDelivery.getStatusString(order.getStatusCurrent());

        }
        else if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
        {

            status = OrderStatusPickFromShop.getStatusString(order.getStatusCurrent());
        }


        currentStatus.setText("Current Status : " + status);
    }




    void bindCancelButton()
    {


        if (order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP) {


            int statusCode = order.getStatusCurrent();

            if (statusCode == OrderStatusPickFromShop.ORDER_PLACED ||
                    statusCode == OrderStatusPickFromShop.ORDER_CONFIRMED ||
                    statusCode == OrderStatusPickFromShop.ORDER_PACKED) {
//                closeButton.setVisibility(View.VISIBLE);
            } else {
                closeButton.setVisibility(View.GONE);
            }


            if (statusCode == OrderStatusPickFromShop.CANCELLED) {
                cancelledImage.setVisibility(View.VISIBLE);
            } else {
                cancelledImage.setVisibility(View.GONE);
            }


        }
        else if (order.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY)
        {


            int statusCode = order.getStatusCurrent();

            if (statusCode == OrderStatusHomeDelivery.ORDER_PLACED ||
                    statusCode == OrderStatusHomeDelivery.ORDER_CONFIRMED ||
                    statusCode == OrderStatusHomeDelivery.ORDER_PACKED) {
//                closeButton.setVisibility(View.VISIBLE);
            } else {
                closeButton.setVisibility(View.GONE);
            }


            if (statusCode == OrderStatusHomeDelivery.CANCELLED_WITH_DELIVERY_GUY ||
                    statusCode == OrderStatusHomeDelivery.CANCELLED) {
                cancelledImage.setVisibility(View.VISIBLE);
            } else {
                cancelledImage.setVisibility(View.GONE);
            }

        }


    }






    public interface ListItemClick {


        //        void confirmOrderPFS(Order order);
        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order);
    }


}


