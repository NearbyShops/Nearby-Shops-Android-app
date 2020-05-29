package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.Backups;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderButtonDouble28May20 extends ViewHolderOrder {


    @BindView(R.id.button_left) TextView buttonLeft;
    @BindView(R.id.progress_left) ProgressBar progressBar;

    @BindView(R.id.button_right) TextView buttonRight;
    @BindView(R.id.progress_right) ProgressBar progressRight;

    @BindView(R.id.distance) TextView distance;





    private Context context;
    private Order order;
    private Fragment fragment;



    public static ViewHolderOrderButtonDouble28May20 create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_double,parent,false);

        return new ViewHolderOrderButtonDouble28May20(view,context,fragment);
    }







    public ViewHolderOrderButtonDouble28May20(View itemView, Context context, Fragment fragment) {
        super(itemView,context,fragment);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;
    }






    @OnClick(R.id.close_button)
    void closeButton(View view) {

        if (fragment instanceof ListItemClick) {
            ((ListItemClick) fragment).notifyCancelOrder(order,getAdapterPosition());
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
        super.setItem(order);
        this.order = order;
//        orderID.append(" - Append from Buttons");


        if(!order.isPickFromShop())
        {

            if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
            {
                buttonLeft.setText(" Delivered ");
                buttonRight.setText(" Return ");


                if(order.getDeliveryAddress()!=null)
                {
                    distance.setText(String.format("%.2f Kms",order.getDeliveryAddress().getRt_distance()));
                }

            }

        }


    }








    @OnClick(R.id.button_left)
    void leftButtonClick()
    {
        if(fragment instanceof ListItemClick)
        {

            if(!order.isPickFromShop())
            {
                if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
                {
                    ((ListItemClick) fragment).deliveredHD(order,getAdapterPosition(), buttonLeft,progressBar);
                }
            }
        }
    }




    @OnClick(R.id.button_right)
    void rightButtonClick()
    {
        if(fragment instanceof ListItemClick)
        {

            if(!order.isPickFromShop())
            {
                if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
                {
                    ((ListItemClick) fragment).returnOrderHD(order,getAdapterPosition(), buttonRight,progressRight);
                }
            }
        }
    }









    @OnClick(R.id.get_directions)
    void getDirectionsClick()
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





    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order, int position);

        void deliveredHD(Order order, int position, TextView button, ProgressBar progressBar);
        void returnOrderHD(Order order, int position, TextView button, ProgressBar progressBar);

    }


}


