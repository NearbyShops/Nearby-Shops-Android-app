package org.nearbyshops.whitelabelapp.Lists.ShopsAvailableNew;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.API.CartItemService;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.CartItem;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderAvailableShop;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


/**
 * Created by sumeet on 6/6/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;


    @Inject
    CartItemService cartItemService;



    public Map<Integer, CartItem> cartItemMap = new HashMap<>();


    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_AVAILABLE_SHOP = 2;

    public static final int VIEW_TYPE_HEADER = 4;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;

    public static final int VIEW_TYPE_BUTTON = 7;


    private boolean loadMore;




    public Adapter(List<Object> dataset, Context context, Fragment fragment) {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;


        if(viewType == VIEW_TYPE_ITEM)
        {
            return ViewHolderItem.create(parent,context,fragment,ViewHolderItem.LAYOUT_TYPE_FULL_WIDTH);
        }
        else if(viewType==VIEW_TYPE_AVAILABLE_SHOP)
        {
            return ViewHolderAvailableShop.create(cartItemMap, parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeader.create(parent,context,ViewHolderHeader.LAYOUT_TYPE_MARGIN_10);
        }
        else if(viewType == VIEW_TYPE_BUTTON)
        {
            return ViewHolderButton.create(parent,context,fragment,ViewHolderButton.LAYOUT_TYPE_CLEAR_ALL);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context,fragment);
        }


        return null;
    }








//    void setCartStats(CartStats cartStats)
//    {
//        this.cartStats = cartStats;
//        cartTotal = cartStats.getCart_Total();
//    }
//




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        if(holder instanceof ViewHolderItem)
        {
            ((ViewHolderItem) holder).setItem((Item) dataset.get(position));

        }
        else if(holder instanceof ViewHolderAvailableShop)
        {
            ((ViewHolderAvailableShop) holder).setItem((ShopItem) dataset.get(position));

        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((ViewHolderHeader.HeaderTitle) dataset.get(position));
            }
        }
        else if (holder instanceof ViewHolderButton) {

            if (dataset.get(position) instanceof ViewHolderButton.ButtonData) {

                ((ViewHolderButton) holder).setItem((ViewHolderButton.ButtonData) dataset.get(position));
            }
        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);

        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen) dataset.get(position));
        }

    }




    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }




    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof Item)
        {
            return VIEW_TYPE_ITEM;
        }
        else if(dataset.get(position) instanceof ShopItem)
        {
            return VIEW_TYPE_AVAILABLE_SHOP;
        }
        else if(dataset.get(position) instanceof ViewHolderHeader.HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof ViewHolderButton.ButtonData)
        {
            return VIEW_TYPE_BUTTON;
        }
        else if(dataset.get(position) instanceof ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen)
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
