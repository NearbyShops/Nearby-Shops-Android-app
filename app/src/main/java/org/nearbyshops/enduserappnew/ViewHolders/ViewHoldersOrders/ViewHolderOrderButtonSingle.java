package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderButtonSingle extends ViewHolderOrder {


    @BindView(R.id.button_single) TextView buttonSingle;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    private Context context;
    private Order order;
    private Fragment fragment;
    private boolean isModeDelivery;



    public static ViewHolderOrderButtonSingle create(ViewGroup parent, Context context, Fragment fragment, boolean isModeDelivery)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_single,parent,false);

        return new ViewHolderOrderButtonSingle(view,context,fragment,isModeDelivery);
    }







    public ViewHolderOrderButtonSingle(View itemView, Context context, Fragment fragment, boolean isModeDelivery) {
        super(itemView,context,fragment);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;
        this.isModeDelivery = isModeDelivery;
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




    public void setItem (Order order, String buttonTitle)
    {
        super.setItem(order);
        this.order = order;
//        orderID.append(" - Append from Buttons");



        buttonSingle.setText(buttonTitle);



//
//        if(order.isPickFromShop())
//        {
//            if(order.getStatusPickFromShop()== OrderStatusPickFromShop.ORDER_PLACED)
//            {
//                buttonSingle.setText(" Confirm ");
//            }
//            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_CONFIRMED)
//            {
//                buttonSingle.setText(" Packed ");
//            }
//            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_PACKED)
//            {
//                buttonSingle.setText(" Ready for Pickup ");
//            }
//            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
//            {
//                buttonSingle.setText(" Payment Received ");
//            }
//        }
//        else
//        {
//
//            if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PLACED)
//            {
//                buttonSingle.setText(" Confirm ");
//            }
//            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
//            {
//                buttonSingle.setText(" Packed ");
//            }
//            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURNED_ORDERS)
//            {
//                buttonSingle.setText(" Unpack Order ");
//            }
//            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
//            {
//                if(isModeDelivery)
//                {
//                    buttonSingle.setText(" Accept Handover ");
//                }
//            }
//            else if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PACKED)
//            {
//                if(isModeDelivery)
//                {
//                    buttonSingle.setText(" Pickup Order ");
//                }
//            }
//
//
//        }


    }








    @OnClick(R.id.button_single)
    void leftButtonClick()
    {
        if(fragment instanceof ListItemClick)
        {

            if(order.isPickFromShop())
            {


                ((ListItemClick) fragment).buttonClicked(order,getAdapterPosition(),buttonSingle,progressBar);


//                if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_PLACED)
//                {
//                    ((ListItemClick) fragment).confirmOrderPFS(order,getAdapterPosition(),buttonSingle,progressBar);
//
//                }
//                else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_CONFIRMED)
//                {
//                    ((ListItemClick) fragment).setOrderPackedPFS(order,getAdapterPosition(),buttonSingle,progressBar);
//                }
//                else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_PACKED)
//                {
//
//                    ((ListItemClick) fragment).readyForPickupPFS(order,getAdapterPosition(),buttonSingle,progressBar);
//
//                }
//                else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
//                {
//                    ((ListItemClick) fragment).paymentReceivedPFS(order,getAdapterPosition(),buttonSingle,progressBar);
//                }

            }
            else
            {

                if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PLACED)
                {
                    ((ListItemClick) fragment).confirmOrderHD(order,getAdapterPosition(),buttonSingle,progressBar);
                }
                else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
                {
                    ((ListItemClick) fragment).setOrderPackedHD(order,getAdapterPosition(),buttonSingle,progressBar);
                }
                else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
                {

                    if(isModeDelivery)
                    {
                        ((ListItemClick) fragment).acceptHandover(order,getAdapterPosition(),buttonSingle,progressBar);
                    }
                }
                else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_PACKED)
                {

                    if(isModeDelivery)
                    {
                        ((ListItemClick) fragment).pickupOrder(order,getAdapterPosition(),buttonSingle,progressBar);
                    }
                }


            }
        }
    }











    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order, int position);


        void buttonClicked(Order order, int position, TextView button, ProgressBar progressBar);


//        void confirmOrderPFS(Order order, int position, TextView button, ProgressBar progressBar);
//        void setOrderPackedPFS(Order order, int position, TextView button, ProgressBar progressBar);
//        void readyForPickupPFS(Order order, int position, TextView button, ProgressBar progressBar);
//        void paymentReceivedPFS(Order order, int position, TextView button, ProgressBar progressBar);


        void confirmOrderHD(Order order, int position, TextView button, ProgressBar progressBar);
        void setOrderPackedHD(Order order, int position, TextView button, ProgressBar progressBar);

        void acceptHandover(Order order, int position, TextView button, ProgressBar progressBar);
        void pickupOrder(Order order, int position, TextView button, ProgressBar progressBar);

    }


}


