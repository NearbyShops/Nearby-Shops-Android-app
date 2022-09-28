package org.nearbyshops.whitelabelapp.CartAndOrder.DetailOrder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.OrderItemEndPoint;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop.ViewHolderShopSmall;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail.ViewHolderOrderAddress;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail.ViewHolderOrderItem;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail.ViewHolderOrderTracker;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.OrderItem;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import java.util.List;



class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;


    public static final int TAG_VIEW_HOLDER_ORDER = 1;
    public static final int TAG_VIEW_HOLDER_SHOP = 2;
    public static final int TAG_VIEW_HOLDER_ORDER_ITEM = 3;
    public static final int TAG_VIEW_HOLDER_ORDER_TRACKER = 4;



    public static final int VIEW_TYPE_HEADER = 45;




    Adapter(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;


        if(viewType==TAG_VIEW_HOLDER_ORDER)
        {
            return ViewHolderOrderAddress.Companion.create(parent,context,fragment);
        }
        else if(viewType==TAG_VIEW_HOLDER_ORDER_TRACKER)
        {
            return ViewHolderOrderTracker.Companion.create(parent,context,fragment,this);
        }
        else if(viewType==TAG_VIEW_HOLDER_ORDER_ITEM)
        {
            return ViewHolderOrderItem.Companion.create(parent,context,fragment);
        }
        else if(viewType == TAG_VIEW_HOLDER_SHOP)
        {
            return ViewHolderShopSmall.create(parent,context,fragment,this,ViewHolderShopSmall.LAYOUT_TYPE_SIDE_BY_SIDE);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeader.createBoldAndBig(parent,context);
        }

        return ViewHolderHeader.createBoldAndBig(parent,context);
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderOrderAddress)
        {
            ((ViewHolderOrderAddress) holder).setItem((ViewHolderOrderAddress.Companion.OrderAddress) dataset.get(position));
        }
        else if(holder instanceof ViewHolderOrderItem)
        {
            ((ViewHolderOrderItem) holder).setItem((OrderItem) dataset.get(position));
        }
        else if(holder instanceof ViewHolderShopSmall)
        {
            ((ViewHolderShopSmall) holder).setItem((Shop) dataset.get(position),false);
        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((ViewHolderHeader.HeaderTitle) dataset.get(position));
            }
        }

    }





    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(dataset.get(position) instanceof OrderItemEndPoint)
        {
            return TAG_VIEW_HOLDER_ORDER;
        }
        else if(dataset.get(position) instanceof OrderItem)
        {
            return TAG_VIEW_HOLDER_ORDER_ITEM;
        }
        else if(dataset.get(position) instanceof Shop)
        {
            return TAG_VIEW_HOLDER_SHOP;
        }
        else if(dataset.get(position) instanceof ViewHolderHeader.HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }

        return -1;
    }





    @Override
    public int getItemCount() {
        return dataset.size();
    }






}
