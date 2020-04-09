package org.nearbyshops.enduserappnew.aSellerModule.QuickStockEditor;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.aSellerModule.ViewHolders.ViewHolderShopItemSeller;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;

import java.util.List;


/**
 * Created by sumeet on 13/6/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;

    private boolean loadMore;



    public static final int VIEW_TYPE_SHOP_ITEM = 1;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 2;

    public static final int VIEW_TYPE_EMPTY_SCREEN = 3;


    public Adapter(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if(viewType == VIEW_TYPE_SHOP_ITEM)
        {
            return ViewHolderShopItemSeller.create(parent,context,fragment,this,dataset);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context);
        }



        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolderShopItemSeller)
        {
            ((ViewHolderShopItemSeller) holder).setShopItem((ShopItem) dataset.get(position));

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



    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof ShopItem)
        {
            return VIEW_TYPE_SHOP_ITEM;
        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }

        return -1;
    }





    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }


}
