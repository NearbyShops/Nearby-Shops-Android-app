package org.nearbyshops.enduserappnew.aSDSAdminModule.MarketsList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.aSDSAdminModule.MarketsList.ViewHolder.ViewHolderMarketAdmin;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends PagedListAdapter<Object,RecyclerView.ViewHolder> {



    public static final int VIEW_TYPE_MARKET = 1;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 3;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;




    private boolean loadMore;

    private Context context;
    private Fragment fragment;





    public Adapter(Context context, Fragment fragment) {

        super(DIFF_CALLBACK_OBJECT);

//        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;


        if(viewType==VIEW_TYPE_MARKET)
        {
            return ViewHolderMarketAdmin.create(parent,context,fragment);
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

        Object item = getItem(position);


        if(position == getCurrentList().size())
        {
            return VIEW_TYPE_PROGRESS_BAR;
        }
        else if(item instanceof Market)
        {
            return VIEW_TYPE_MARKET;
        }
        else if(item instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }



        return -1;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {


        if(holderVH instanceof ViewHolderMarketAdmin)
        {
            if(getItem(position) instanceof Market)
            {
                ((ViewHolderMarketAdmin) holderVH).setItem((Market) getItem(position));
            }

        }
        else if (holderVH instanceof LoadingViewHolder) {


            ((LoadingViewHolder) holderVH).setLoading(true);

        }
        else if(holderVH instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holderVH).setItem((EmptyScreenDataFullScreen) getItem(position));
        }

    }



    void showLog(String message)
    {
        Log.d("order_status",message);
    }




    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }






    public static DiffUtil.ItemCallback<Order> DIFF_CALLBACK = new DiffUtil.ItemCallback<Order>() {


        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getOrderID()== newItem.getOrderID();
        }



        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return false;
        }


    };



    public static DiffUtil.ItemCallback<Object> DIFF_CALLBACK_OBJECT = new DiffUtil.ItemCallback<Object>() {


        @Override
        public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {

            if(oldItem instanceof Order && newItem instanceof Order)
            {
                return ((Order)oldItem).getOrderID()== ((Order)newItem).getOrderID();
            }
            else
            {

                return false;
            }
        }

        @Override
        public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
            return false;
        }



    };
}
