package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

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

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;


public class ViewHolderOrder extends RecyclerView.ViewHolder {



    @BindView(R.id.order_id)
    public TextView orderID;

    @BindView(R.id.dateTimePlaced)
    public TextView dateTimePlaced;

    @BindView(R.id.is_pick_from_shop)
    public TextView isPickFromShop;

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
        //                OrderStats orderStats = order.getOrderStats();



        orderID.setText("Order ID : " + order.getOrderID());
        dateTimePlaced.setText("" + order.getDateTimePlaced().toLocaleString());



        if(order.getDeliveryAddress().getName()==null)
        {
            deliveryAddressName.setText("Delivery Address Deleted\nor Not provided");
            deliveryAddress.setText(" - ");
            deliveryAddressPhone.setText(" - ");

        }
        else
        {
            deliveryAddressName.setText(deliveryAddressLocal.getName());

            deliveryAddress.setText(deliveryAddressLocal.getDeliveryAddress() + ",\n"
                    + deliveryAddressLocal.getCity() + " - " + deliveryAddressLocal.getPincode());

            deliveryAddressPhone.setText("Phone : " + deliveryAddressLocal.getPhoneNumber());

            //                holder.numberOfItems.setText(orderStats.getItemCount() + " Items");
        }



        numberOfItems.setText(order.getItemCount() + " Items");
        //               holder.orderTotal.setText("| Total : " +String.valueOf(PrefGeneral.getCurrencySymbol(context)) + " " + (orderStats.getItemTotal() + order.getDeliveryCharges()));
        orderTotal.setText("| Total : " + PrefGeneral.getCurrencySymbol(context) + String.format(" %.2f", order.getNetPayable()));
        //holder.currentStatus.setText();


        //                String status = UtilityOrderStatus.getEvent(order.getStatusHomeDelivery(),order.getDeliveryReceived(),order.getPaymentReceived());
        String status = "";


        //                showLog("Order PickfromShop : " + String.valueOf(order.getPickFromShop()));
        //                showLog("Order Status Home Delivery : "  + String.valueOf(order.getStatusHomeDelivery()));
        //                showLog("Order Status Pick from Shop : " + String.valueOf(order.getStatusPickFromShop()));


        if (order.isPickFromShop()) {

            isPickFromShop.setBackgroundColor(ContextCompat.getColor(context, R.color.orangeDark));
            isPickFromShop.setText("Pick from Shop");


            status = OrderStatusPickFromShop.getStatusString(order.getStatusPickFromShop());

            //                    showLog("Status : " + OrderStatusPickFromShop.getStatusString(order.getStatusPickFromShop()));

            int statusCode = order.getStatusPickFromShop();

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


        } else {
            isPickFromShop.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
            isPickFromShop.setText("Home Delivery");

            status = OrderStatusHomeDelivery.getStatusString(order.getStatusHomeDelivery());

            //                    showLog("Status : " + OrderStatusHomeDelivery.getStatusString(order.getStatusHomeDelivery()));

            int statusCode = order.getStatusHomeDelivery();

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

        currentStatus.setText("Current Status : " + status);
    }







    public interface ListItemClick {


        //        void confirmOrderPFS(Order order);
        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order);
    }


}


