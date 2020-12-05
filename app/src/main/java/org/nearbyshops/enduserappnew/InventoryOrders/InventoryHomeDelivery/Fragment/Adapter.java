package org.nearbyshops.enduserappnew.InventoryOrders.InventoryHomeDelivery.Fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderShopAdminHD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;

    
    public static final int VIEW_TYPE_ORDER = 1;

    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;

    public static final int VIEW_TYPE_ORDER_SHOP_ADMIN = 7;


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


        if(viewType==VIEW_TYPE_ORDER_SHOP_ADMIN)
        {
            return ViewHolderOrderShopAdminHD.create(parent,parent.getContext(),fragment);
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
            return VIEW_TYPE_ORDER_SHOP_ADMIN;

        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }


        return -1;
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderOrderShopAdminHD)
        {
            Order order = (Order) dataset.get(position);
            ((ViewHolderOrderShopAdminHD) holder).setItem(order);

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



}
