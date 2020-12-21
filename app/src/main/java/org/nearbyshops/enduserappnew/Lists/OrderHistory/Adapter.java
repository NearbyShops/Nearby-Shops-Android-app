package org.nearbyshops.enduserappnew.Lists.OrderHistory;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderEndUser;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderMarketAdmin;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderShopAdminHD;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderShopAdminPFS;

import java.util.List;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;


    public static final int VIEW_TYPE_ORDER = 1;
    public static final int VIEW_TYPE_ORDER_SHOP_ADMIN = 2;
    public static final int VIEW_TYPE_ORDER_SHOP_ADMIN_PFS = 3;
    public static final int VIEW_TYPE_ORDER_END_USER = 4;
    public static final int VIEW_TYPE_ORDER_MARKET_ADMIN = 5;


    public static final int VIEW_TYPE_EMPTY_SCREEN = 10;
    private final static int VIEW_TYPE_PROGRESS_BAR = 11;


    public static final int VIEW_TYPE_SWITCH_MARKET = 16;






    private boolean loadMore;

    private Context context;
    private Fragment fragment;
    int currentMode;




    Adapter(List<Object> dataset, Fragment fragment, Context context, int currentMode) {
        this.dataset = dataset;
        this.fragment = fragment;
        this.context = context;
        this.currentMode = currentMode;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;


        if(viewType==VIEW_TYPE_ORDER)
        {

            return ViewHolderOrder.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_ORDER_SHOP_ADMIN)
        {
            return ViewHolderOrderShopAdminHD.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_ORDER_SHOP_ADMIN_PFS)
        {
            return ViewHolderOrderShopAdminPFS.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_ORDER_END_USER)
        {
            return ViewHolderOrderEndUser.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_ORDER_MARKET_ADMIN)
        {
            return ViewHolderOrderMarketAdmin.create(parent,context,fragment);
        }
        else if (viewType == VIEW_TYPE_PROGRESS_BAR) {

            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context,fragment);
        }


        return null;
    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof Order)
        {

            Order order = (Order) dataset.get(position);

            if(currentMode==OrdersHistoryFragment.MODE_SHOP_ADMIN)
            {

                return VIEW_TYPE_ORDER;

//                if(order.getDeliveryMode()==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
//                {
//                    return VIEW_TYPE_ORDER_SHOP_ADMIN_PFS;
//                }
//                else
//                {
//                    return VIEW_TYPE_ORDER_SHOP_ADMIN;
//                }
            }
            else if(currentMode==OrdersHistoryFragment.MODE_END_USER)
            {
                return VIEW_TYPE_ORDER_END_USER;
            }
            else
            {
                return VIEW_TYPE_ORDER;
            }


        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }

        return -1;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {



        if(holderVH instanceof ViewHolderOrderShopAdminHD)
        {
            ((ViewHolderOrderShopAdminHD) holderVH).setItem((Order) dataset.get(position));
        }
        else if(holderVH instanceof ViewHolderOrderShopAdminPFS)
        {
            ((ViewHolderOrderShopAdminPFS) holderVH).setItem((Order) dataset.get(position));
        }
        else if(holderVH instanceof ViewHolderOrderEndUser)
        {
            ((ViewHolderOrderEndUser) holderVH).setItem((Order) dataset.get(position));
        }
        else if(holderVH instanceof ViewHolderOrder)
        {
            ((ViewHolderOrder) holderVH).setItem((Order) dataset.get(position));
        }
        else if (holderVH instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holderVH).setLoading(loadMore);

        }
        else if(holderVH instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holderVH).setItem((EmptyScreenDataFullScreen) dataset.get(position));
        }

    }



    void showLog(String message)
    {
        Log.d("order_status",message);
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
