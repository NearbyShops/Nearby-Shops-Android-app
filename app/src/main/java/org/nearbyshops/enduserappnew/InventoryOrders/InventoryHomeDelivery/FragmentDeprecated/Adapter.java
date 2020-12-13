package org.nearbyshops.enduserappnew.InventoryOrders.InventoryHomeDelivery.FragmentDeprecated;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.Deprecated.ViewHolderOrderButtonDouble;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.Deprecated.ViewHolderOrderButtonSingle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.Deprecated.ViewHolderOrderSelectable;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.Deprecated.ViewHolderOrderWithDeliveryStaff;
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

    public static final int VIEW_TYPE_ORDER_BUTTON_DOUBLE = 7;


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
            return ViewHolderOrderButtonSingle.create(parent,parent.getContext(),fragment);
        }
        else if(viewType == VIEW_TYPE_ORDER_BUTTON_DOUBLE)
        {
            return ViewHolderOrderButtonDouble.create(parent,parent.getContext(),fragment);
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
            return ViewHolderEmptyScreenFullScreen.create(parent,parent.getContext(),fragment);
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


            if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PACKED)
            {

                if(order.getDeliveryGuySelfID()==0  && order.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY)
                {
                    return VIEW_TYPE_ORDER_WITH_BUTTON;
                }
                else
                {
                    return VIEW_TYPE_ORDER;
                }

            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
            {

                return VIEW_TYPE_ORDER_WITH_BUTTON;
            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
            {

                if(order.getDeliveryGuySelfID()==0  && order.getDeliveryMode()==Order.DELIVERY_MODE_HOME_DELIVERY)
                {
                    return VIEW_TYPE_ORDER_BUTTON_DOUBLE;
                }
                else
                {
                    return VIEW_TYPE_ORDER;
                }

            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURN_REQUESTED)
            {

                return VIEW_TYPE_ORDER_WITH_BUTTON;
            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURNED_ORDERS)
            {
                return VIEW_TYPE_ORDER_WITH_BUTTON;
            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.DELIVERED)
            {

                return VIEW_TYPE_ORDER_WITH_BUTTON;

            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.PAYMENT_RECEIVED)
            {

                return VIEW_TYPE_ORDER;
            }
            else
            {
                return VIEW_TYPE_ORDER_WITH_BUTTON;
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
        else if(holder instanceof ViewHolderOrderButtonDouble)
        {
            ((ViewHolderOrderButtonDouble) holder).setItem((Order) dataset.get(position),"Delivered","Return");
        }
        else if(holder instanceof ViewHolderOrderSelectable)
        {
            ((ViewHolderOrderSelectable)holder).setItem((Order) dataset.get(position));
        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));
        }
        else if(holder instanceof ViewHolderOrder)
        {
            ((ViewHolderOrder) holder).setItem((Order) dataset.get(position));
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
        if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PLACED)
        {
            return " Confirm ";
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
        {
            return " Packed ";
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_PACKED)
        {
            return " Deliver By Self ";
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURNED_ORDERS)
        {
            return " Unpack Order ";
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
        {
            return " Cancel Handover ";
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.RETURN_REQUESTED)
        {
            return  " Order Received ";
        }
        else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.DELIVERED)
        {
            return  " Payment Received ";
        }



        return " - - - ";
    }


}
