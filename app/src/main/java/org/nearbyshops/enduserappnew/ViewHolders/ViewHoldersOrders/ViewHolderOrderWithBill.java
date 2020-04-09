package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderWithBill extends ViewHolderOrder {


    @BindView(R.id.delivery_type) TextView deliveryTypeDescription;

    @BindView(R.id.item_total_value) TextView itemTotal;
    @BindView(R.id.delivery_charge_value) TextView deliveryCharge;
    @BindView(R.id.app_service_charge_value) TextView appServiceCharge;
    @BindView(R.id.net_payable_value) TextView netPayable;


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
        super(itemView,context,fragment);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }





    public void setItem(Order order)
    {
        this.order = order;
        super.setItem(order);


        if(order.isPickFromShop())
        {
            deliveryTypeDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.orangeDark));
            deliveryTypeDescription.setText(context.getString(R.string.delivery_type_description_pick_from_shop));
        }
        else
        {
            deliveryTypeDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
            deliveryTypeDescription.setText(context.getString(R.string.delivery_type_description_home_delivery));
        }




        // bind billing details
        itemTotal.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getItemTotal()));
        deliveryCharge.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getDeliveryCharges()));
        appServiceCharge.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f", order.getAppServiceCharge()));
        netPayable.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getNetPayable()));

    }







    @OnClick(R.id.list_item)
    void listItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick)fragment).listItemClick(order,getAdapterPosition());
        }
    }




    public interface ListItemClick
    {
        void listItemClick(Order order, int position);
        boolean listItemLongClick(View view, Order order, int position);
    }
}

