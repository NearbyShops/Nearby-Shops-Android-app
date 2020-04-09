package org.nearbyshops.enduserappnew.aSellerModule.InventoryOrders.Fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderButtonSingle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderSelectable;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderWithDeliveryStaff;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;

    
    public static final int VIEW_TYPE_ORDER = 1;
    public static final int VIEW_TYPE_ORDER_WITH_BUTTON = 2;
    public static final int VIEW_TYPE_ORDER_SELECTABLE = 3;
    public static final int VIEW_TYPE_ORDER_DELIVERY_PROFILE = 4;

    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;


    private Map<Integer, Order> selectedOrders = new HashMap<>();
    private Fragment fragment;

    private boolean loadMore;



    Adapter(List<Object> dataset, Fragment fragment) {
        this.dataset = dataset;
        this.fragment = fragment;
        selectedOrders.clear();
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
            return ViewHolderOrderButtonSingle.create(parent,parent.getContext(),fragment,false);
        }
        else if(viewType == VIEW_TYPE_ORDER_SELECTABLE)
        {
            return ViewHolderOrderSelectable.create(parent,parent.getContext(),fragment,selectedOrders,this);
        }
        else if(viewType == VIEW_TYPE_ORDER_DELIVERY_PROFILE)
        {
            return ViewHolderOrderWithDeliveryStaff.create(parent,parent.getContext(),fragment);
        }
        else if (viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR) {

            return LoadingViewHolder.create(parent,parent.getContext());
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,parent.getContext());
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

            if(((Order) dataset.get(position)).isPickFromShop())
            {
                if(((Order) dataset.get(position)).getStatusPickFromShop()== OrderStatusPickFromShop.DELIVERED)
                {
                    return VIEW_TYPE_ORDER;
                }
                else
                {
                    return VIEW_TYPE_ORDER_WITH_BUTTON;
                }

            }
            else
            {
                if(((Order) dataset.get(position)).getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PACKED)
                {
                    return VIEW_TYPE_ORDER_SELECTABLE;
                }
                else if(((Order) dataset.get(position)).getStatusHomeDelivery()==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
                {

                    return VIEW_TYPE_ORDER_DELIVERY_PROFILE;
                }
                else if(((Order) dataset.get(position)).getStatusHomeDelivery()==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
                {

                    return VIEW_TYPE_ORDER_DELIVERY_PROFILE;
                }
                else if(((Order) dataset.get(position)).getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURN_REQUESTED)
                {

                    return VIEW_TYPE_ORDER_DELIVERY_PROFILE;
                }
                else if(((Order) dataset.get(position)).getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURNED_ORDERS)
                {

                    return VIEW_TYPE_ORDER_SELECTABLE;
                }
                else if(((Order) dataset.get(position)).getStatusHomeDelivery()==OrderStatusHomeDelivery.DELIVERED)
                {

                    return VIEW_TYPE_ORDER_DELIVERY_PROFILE;
                }
                else if(((Order) dataset.get(position)).getStatusHomeDelivery()==OrderStatusHomeDelivery.PAYMENT_RECEIVED)
                {

                    return VIEW_TYPE_ORDER;
                }
                else
                {
                    return VIEW_TYPE_ORDER_WITH_BUTTON;
                }

            }

        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
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
        else if(holder instanceof ViewHolderOrder)
        {
            ((ViewHolderOrder) holder).setItem((Order) dataset.get(position));
        }
        else if(holder instanceof ViewHolderOrderSelectable)
        {
            ((ViewHolderOrderSelectable)holder).setItem((Order) dataset.get(position));
        }
        else if(holder instanceof ViewHolderOrderWithDeliveryStaff)
        {
            ((ViewHolderOrderWithDeliveryStaff)holder).setItem((Order) dataset.get(position));
        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));
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






    Map<Integer, Order> getSelectedOrders() {
        return selectedOrders;
    }


    private String getButtonTitle(Order order)
    {
        if(order.isPickFromShop())
        {
            if(order.getStatusPickFromShop()== OrderStatusPickFromShop.ORDER_PLACED)
            {
                return " Confirm ";
            }
            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_CONFIRMED)
            {
                return " Packed ";
            }
            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_PACKED)
            {
                return " Ready for Pickup ";
            }
            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
            {
                return " Payment Received ";
            }
        }
        else
        {

            if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PLACED)
            {
                return " Confirm ";
            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
            {
                return " Packed ";
            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURNED_ORDERS)
            {
                return " Unpack Order ";
            }
        }

        return " - - - ";
    }


}
