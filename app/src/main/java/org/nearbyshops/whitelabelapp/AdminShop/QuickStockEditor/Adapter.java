package org.nearbyshops.whitelabelapp.AdminShop.QuickStockEditor;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForShop.AdapterHorizontalList;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.AdminShop.ViewHolders.ViewHolderShopItemSeller;
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.AdapterBannerImages;
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightList;
import org.nearbyshops.whitelabelapp.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHorizontalList;

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
    public static final int VIEW_TYPE_ITEM_CATEGORY = 2;
    public static final int VIEW_TYPE_ITEM_CATEGORY_LIST = 3;

    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 5;
    public static final int VIEW_TYPE_HIGHLIGHTS = 10;



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
        else if(viewType == VIEW_TYPE_ITEM_CATEGORY_LIST)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_HIGHLIGHTS)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_ITEM_CATEGORY)
        {
            return ViewHolderItemCategory.create(parent,context,fragment,this);
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




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        if(holder instanceof ViewHolderShopItemSeller)
        {
            ((ViewHolderShopItemSeller) holder).setShopItem((ShopItem) dataset.get(position));
        }
        else if(holder instanceof ViewHolderItemCategory)
        {
            ((ViewHolderItemCategory) holder).bindItemCategory((ItemCategory) dataset.get(position));
        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen) dataset.get(position));
        }
        else if(holder instanceof ViewHolderHorizontalList)
        {

            if(getItemViewType(position)==VIEW_TYPE_HIGHLIGHTS)
            {
                HighlightList highlightList = ((HighlightList)dataset.get(position));

                List<Object> list = highlightList.getHighlightItemList();
                AdapterBannerImages adapterBannerImages = new AdapterBannerImages(list,context,fragment);
                ((ViewHolderHorizontalList) holder).setItem(adapterBannerImages, highlightList.getListTitle());
            }
            else if(getItemViewType(position)==VIEW_TYPE_ITEM_CATEGORY_LIST)
            {

                List<ItemCategory> list = ((ItemCategoriesList)dataset.get(position)).getItemCategories();
                ((ViewHolderHorizontalList) holder).setItem(new AdapterHorizontalList(list,context,fragment),null);

            }

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
        else if(dataset.get(position) instanceof ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if(dataset.get(position) instanceof ShopItem)
        {
            return VIEW_TYPE_SHOP_ITEM;
        }
        else if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY;
        }
        else if(dataset.get(position) instanceof ItemCategoriesList)
        {
            return VIEW_TYPE_ITEM_CATEGORY_LIST;
        }
        else if(dataset.get(position) instanceof HighlightList)
        {
            return VIEW_TYPE_HIGHLIGHTS;
        }


        return -1;
    }





    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }


}
