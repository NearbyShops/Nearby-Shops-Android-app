package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderWithBill extends RecyclerView.ViewHolder {


    @BindView(R.id.delivery_type) TextView deliveryTypeDescription;

    @BindView(R.id.item_total_value) TextView itemTotal;
    @BindView(R.id.delivery_charge_value) TextView deliveryCharge;
    @BindView(R.id.app_service_charge_value) TextView appServiceCharge;
    @BindView(R.id.net_payable_value) TextView netPayable;
    @BindView(R.id.savings_value) TextView savingsOverMRP;

    @BindView(R.id.savings_block) LinearLayout savingsBlock;

    @BindView(R.id.delivery_otp) TextView deliveryOTP;

    @BindView(R.id.customer_name) TextView customerName;
    @BindView(R.id.customer_address) TextView customerAddress;
    @BindView(R.id.customer_phone) TextView customerPhone;
    @BindView(R.id.payment_mode) TextView paymentMode;


    private Context context;
    private Order order;
    private Fragment fragment;



    public static ViewHolderOrderWithBill create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_order_detail,parent,false);
        return new ViewHolderOrderWithBill(view,context,fragment);
    }





    public ViewHolderOrderWithBill(View itemView, Context context, Fragment fragment) {

        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }





    public void setItem(Order order)
    {
        this.order = order;
//        super.setItem(order);


        if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
        {
            deliveryTypeDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.orangeDark));
            deliveryTypeDescription.setText(context.getString(R.string.delivery_type_description_pick_from_shop));
        }
        else
        {
            deliveryTypeDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
            deliveryTypeDescription.setText(context.getString(R.string.delivery_type_description_home_delivery));
        }


        if(order.getDeliveryOTP()!=0)
        {
            deliveryOTP.setVisibility(View.VISIBLE);
            deliveryOTP.setText("Delivery OTP : " + String.valueOf(order.getDeliveryOTP()));
        }
        else
        {
            deliveryOTP.setVisibility(View.GONE);
        }



        // bind billing details
        itemTotal.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getItemTotal()));
        deliveryCharge.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getDeliveryCharges()));

        appServiceCharge.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f", order.getAppServiceCharge()));


        netPayable.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getNetPayable()));

        if(order.getSavingsOverMRP()>0)
        {
            savingsBlock.setVisibility(View.VISIBLE);
            savingsOverMRP.setText(PrefGeneral.getCurrencySymbol(context ) + " " + String.format("%.2f",order.getSavingsOverMRP()));
        }
        else
        {
            savingsBlock.setVisibility(View.GONE);
        }

        DeliveryAddress address = order.getDeliveryAddress();

        if(address!=null)
        {
            customerName.setText(address.getName());
            customerAddress.setText(address.getDeliveryAddress());
            customerPhone.setText(String.valueOf(address.getPhoneNumber()));
        }


        if(order.getPaymentMode()==Order.PAYMENT_MODE_CASH_ON_DELIVERY)
        {
            paymentMode.setText("Payment Mode : COD (Cash On Delivery)");
        }
        else if(order.getPaymentMode()==Order.PAYMENT_MODE_PAY_ONLINE_ON_DELIVERY)
        {
            paymentMode.setText("Payment Mode : POD (Pay Online On Delivery)");
        }
        else if(order.getPaymentMode()==Order.PAYMENT_MODE_RAZORPAY)
        {
            paymentMode.setText("Payment Mode : Paid");
        }

    }







    public interface ListItemClick
    {
        void listItemClick(Order order, int position);
        boolean listItemLongClick(View view, Order order, int position);
    }
}

