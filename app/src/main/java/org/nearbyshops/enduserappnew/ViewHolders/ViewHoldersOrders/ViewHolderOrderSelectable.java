package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;


public class ViewHolderOrderSelectable extends ViewHolderOrder {



    private Context context;
    private Order order;
    private Fragment fragment;

    @BindView(R.id.list_item)
    ConstraintLayout listItem;

    private RecyclerView.Adapter adapter;
    private Map<Integer,Order> selectedOrders;


    @BindView(R.id.button_single) TextView buttonSingle;
    @BindView(R.id.progress_bar) ProgressBar progressBar;





    public static ViewHolderOrderSelectable create(
            ViewGroup parent, Context context,
            Fragment fragment, Map<Integer,Order> selectedOrders, RecyclerView.Adapter adapter)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_single,parent,false);

        return new ViewHolderOrderSelectable(view,context,fragment,selectedOrders, adapter);
    }




    public ViewHolderOrderSelectable(View itemView, Context context, Fragment fragment,
                                     Map<Integer,Order> selectedOrders, RecyclerView.Adapter adapter) {

        super(itemView,context,fragment);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;
        this.selectedOrders = selectedOrders;
        this.adapter = adapter;
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




    @OnLongClick(R.id.list_item)
    boolean listItemLongClick()
    {


        if (selectedOrders.containsKey(order.getOrderID()))
        {
            selectedOrders.remove(order.getOrderID());
        }
        else
        {
            selectedOrders.put(order.getOrderID(),order);
        }


        //notifyDataSetChanged();

//        adapter.notifyItemChanged(getLayoutPosition());
        bindOrder();


        if(selectedOrders.size()==0)
        {
            if(fragment instanceof ListItemClick)
            {
                ((ListItemClick) fragment).selectedStopped();
            }
        }
        else if(selectedOrders.size()==1)
        {

            if(fragment instanceof ListItemClick)
            {
                ((ListItemClick) fragment).selectionStarted();
            }
        }


        return true;
    }



    public void setItem (Order order)
    {
        super.setItem(order);
        this.order = order;


        if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.RETURNED_ORDERS)
        {
            buttonSingle.setVisibility(View.VISIBLE);
            buttonSingle.setText(" Unpack Order ");
        }
        else
        {
            buttonSingle.setVisibility(View.GONE);
        }


        bindOrder();
    }





    private void bindOrder()
    {
        if (selectedOrders.containsKey(order.getOrderID())) {

            listItem.setBackgroundResource(R.color.gplus_color_2);
        }
        else
        {
            listItem.setBackgroundResource(R.color.white);
        }
    }





    @OnClick(R.id.button_single)
    void leftButtonClick()
    {

        if(fragment instanceof ListItemClick)
        {

            if(!order.isPickFromShop())
            {
                if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURNED_ORDERS)
                {
                    ((ListItemClick) fragment).unpackOrderHD(order,getAdapterPosition(),buttonSingle,progressBar);
                }

            }
        }
    }




    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order, int position);


        void selectionStarted();
        void selectedStopped();
        void unpackOrderHD(Order order, int position, TextView button, ProgressBar progressBar);
    }


}


