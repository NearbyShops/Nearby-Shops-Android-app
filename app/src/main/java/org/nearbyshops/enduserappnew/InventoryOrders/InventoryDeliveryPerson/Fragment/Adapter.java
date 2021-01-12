package org.nearbyshops.enduserappnew.InventoryOrders.InventoryDeliveryPerson.Fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.InventoryOrders.OrderFilters.ViewHolderFilterOrders;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.Models.FilterOrders;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderDeliveryGuy;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderMarketAdmin;

import java.util.List;


/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;

    public static final int VIEW_TYPE_ORDER_DELIVERY_GUY = 4;

    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;


    public static final int VIEW_TYPE_FILTER_ORDERS= 10;



    private Fragment fragment;
    private boolean loadMore;
    private int screenMode;




    Adapter(List<Object> dataset, Fragment fragment, int screenMode) {
        this.dataset = dataset;
        this.fragment = fragment;
        this.screenMode =screenMode;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;



        if(viewType == VIEW_TYPE_ORDER_DELIVERY_GUY)
        {
            if(screenMode == DeliveryFragmentNew.SCREEN_MODE_MARKET_ADMIN)
            {
                return ViewHolderOrderMarketAdmin.create(parent,parent.getContext(),fragment);
            }
            else
            {
                return ViewHolderOrderDeliveryGuy.create(parent,parent.getContext(),fragment);
            }

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
            return VIEW_TYPE_ORDER_DELIVERY_GUY;
        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
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

        if(holder instanceof ViewHolderOrderDeliveryGuy)
        {
            ((ViewHolderOrderDeliveryGuy) holder).setItem((Order) dataset.get(position));
        }
        else if(holder instanceof ViewHolderOrderMarketAdmin)
        {

            ((ViewHolderOrderMarketAdmin) holder).setItem((Order) dataset.get(position));
        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));
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


}
