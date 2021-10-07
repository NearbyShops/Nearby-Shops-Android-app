package org.nearbyshops.whitelabelapp.AdminDelivery.ButtonDashboard.FragmentDeprecated;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterOrders;
import org.nearbyshops.whitelabelapp.InventoryOrders.OrderFilters.ViewHolderFilterOrders;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.Deprecated.ViewHolderOrderButtonDouble;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.Deprecated.ViewHolderOrderButtonSingle;

import java.util.List;




/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;

    public static final int VIEW_TYPE_ORDER = 1;
    public static final int VIEW_TYPE_ORDER_WITH_BUTTON = 2;
    public static final int VIEW_TYPE_ORDER_BUTTON_DOUBLE = 3;


    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;


    public static final int VIEW_TYPE_FILTER_ORDERS= 10;



    private Fragment fragment;
    private boolean loadMore;
    private int currentMode;




    Adapter(List<Object> dataset, Fragment fragment, int currentMode) {
        this.dataset = dataset;
        this.fragment = fragment;
        this.currentMode=currentMode;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType==VIEW_TYPE_ORDER)
        {
            return ViewHolderOrder.create(parent,parent.getContext(),fragment);
        }
        else if(viewType == VIEW_TYPE_ORDER_WITH_BUTTON)
        {
            return ViewHolderOrderButtonSingle.create(parent,parent.getContext(),fragment);
        }
        else if(viewType == VIEW_TYPE_ORDER_BUTTON_DOUBLE)
        {
            return ViewHolderOrderButtonDouble.create(parent,parent.getContext(),fragment);
        }
        else if (viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR) {

            return LoadingViewHolder.create(parent,parent.getContext());
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,parent.getContext(),fragment);
        }
        else if(viewType==VIEW_TYPE_FILTER_ORDERS)
        {
            return ViewHolderFilterOrders.create(parent,parent.getContext(),fragment);
        }



        return null;
    }







    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof Order)
        {
            Order order = (Order) dataset.get(position);


            if(currentMode==OrderStatusHomeDelivery.DELIVERY_BOY_UNASSIGNED)
            {
                return VIEW_TYPE_ORDER_WITH_BUTTON;
            }
            else if(currentMode==OrderStatusHomeDelivery.DELIVERY_BOY_ASSIGNED)
            {

                if(order.getStatusCurrent()<OrderStatusHomeDelivery.ORDER_PACKED)
                {
                    return VIEW_TYPE_ORDER;
                }
                else if(order.getStatusCurrent()==OrderStatusHomeDelivery.ORDER_PACKED)
                {
                    return VIEW_TYPE_ORDER_WITH_BUTTON;
                }

            }
            else if(order.getStatusCurrent()==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
            {

                return VIEW_TYPE_ORDER_BUTTON_DOUBLE;
            }
            else if((order.getStatusCurrent()==OrderStatusHomeDelivery.RETURN_REQUESTED))
            {
                return VIEW_TYPE_ORDER;
            }
            else if(order.getStatusCurrent()==OrderStatusHomeDelivery.DELIVERED)
            {

                return VIEW_TYPE_ORDER;
            }


        }
        else if(dataset.get(position) instanceof ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if(dataset.get(position) instanceof FilterOrders)
        {
            return VIEW_TYPE_FILTER_ORDERS;
        }



        return -1;
    }








    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolderOrderButtonSingle)
        {
            Order order = (Order) dataset.get(position);

            ((ViewHolderOrderButtonSingle) holder).setItem(order,getButtonTitle(order));
        }
        else if(holder instanceof ViewHolderOrderButtonDouble)
        {
            ((ViewHolderOrderButtonDouble) holder).setItem((Order) dataset.get(position),"Delivered","Return");
        }
        else if(holder instanceof ViewHolderOrder)
        {
            ((ViewHolderOrder) holder).setItem((Order) dataset.get(position));
        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen) dataset.get(position));
        }
        else if(holder instanceof ViewHolderFilterOrders)
        {
            ((ViewHolderFilterOrders) holder).setItem((FilterOrders) dataset.get(position));
        }


    }




    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }








    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }




//    Map<Integer, Order> getSelectedOrders() {
//        return selectedOrders;
//    }






    private String getButtonTitle(Order order)
    {


//        if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
//        {
////            Accept Handover
//            return " Pickup Order ";
//
//        }
//        else if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PACKED)
//        {
////            Pickup Order
//
//            return " Start Pickup ";
//        }






        if(order.getStatusCurrent() <= OrderStatusHomeDelivery.ORDER_PACKED)
        {

            if(currentMode==OrderStatusHomeDelivery.DELIVERY_BOY_UNASSIGNED)
            {
                return " Start Pickup ";
            }
            else if(currentMode==OrderStatusHomeDelivery.DELIVERY_BOY_ASSIGNED)
            {
                return " Pickup Order ";
            }
        }



        return " - - - ";
    }



}
