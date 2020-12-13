package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.Deprecated;

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
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderButtonDouble extends ViewHolderOrder {


    @BindView(R.id.button_left) TextView buttonLeft;
    @BindView(R.id.progress_left) ProgressBar progressLeft;

    @BindView(R.id.button_right) TextView buttonRight;
    @BindView(R.id.progress_right) ProgressBar progressRight;

    @BindView(R.id.pickup_directions) TextView pickupDirections;





    private Context context;
    private Order order;
    private Fragment fragment;



    public static ViewHolderOrderButtonDouble create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_double,parent,false);

        return new ViewHolderOrderButtonDouble(view,context,fragment);
    }







    public ViewHolderOrderButtonDouble(View itemView, Context context, Fragment fragment) {
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






    public void setItem (Order order, String buttonLeftName, String buttonRightName)
    {
        super.setItem(order);
        this.order = order;


        buttonLeft.setText(buttonLeftName);
        buttonRight.setText(buttonRightName);


        buttonLeft.setVisibility(View.VISIBLE);
        buttonRight.setVisibility(View.VISIBLE);
        pickupDirections.setVisibility(View.VISIBLE);



        if(order.getDeliveryAddress()!=null)
        {
            pickupDirections.setText(String.format("%.2f Kms",order.getRt_deliveryDistance()));
//            distance.setText(String.format("%.2f Kms",order.getDeliveryAddress().getRt_distance()));
        }
    }









    @OnClick(R.id.button_left)
    void leftButtonClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).buttonLeftClick(order,getAdapterPosition(), buttonLeft, progressLeft);
        }
    }




    @OnClick(R.id.button_right)
    void rightButtonClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).buttonRightClick(order,getAdapterPosition(), buttonRight,progressRight);
        }
    }










    @OnClick({R.id.pickup_directions})
    void getDirectionsClick()
    {
        DeliveryAddress deliveryAddress = order.getDeliveryAddress();

        if(deliveryAddress!=null)
        {
            getDirections(deliveryAddress.getLatitude(),deliveryAddress.getLongitude());
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





    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order, int position);

        void buttonLeftClick(Order order, int position, TextView button, ProgressBar progressBar);
        void buttonRightClick(Order order, int position, TextView button, ProgressBar progressBar);

    }


}


