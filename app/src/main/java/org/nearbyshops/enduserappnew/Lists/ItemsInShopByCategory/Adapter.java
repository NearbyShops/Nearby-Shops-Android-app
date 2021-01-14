package org.nearbyshops.enduserappnew.Lists.ItemsInShopByCategory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.UtilityScreens.BannerSlider.AdapterBannerImages;
import org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model.Highlights;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.Models.FilterItemsInShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.ViewHolderFilterItemsInShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.enduserappnew.Lists.ItemsByCategory.AdapterItemCatHorizontalList;
import org.nearbyshops.enduserappnew.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopInfo;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopItemButton;

import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopItemInstacart;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopSmall;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.WhatsMessageData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderWhatsApp;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderHeader;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderHorizontalList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by sumeet on 19/12/15.
 */



public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    public Map<Integer, CartItem> cartItemMap = new HashMap<>();
    public CartStats cartStats = new CartStats();


    private List<Object> dataset;
    private Context context;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_ITEM_CATEGORY_LIST = 2;
    public static final int VIEW_TYPE_SHOP_ITEM = 3;

    public static final int VIEW_TYPE_SHOP = 4;
    public static final int VIEW_TYPE_MESSAGE_ON_WHATSAPP = 5;

    public static final int VIEW_TYPE_SHOP_ITEM_INSTA = 6;


    public static final int VIEW_TYPE_HEADER = 10;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 11;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 12;


    public static final int VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM = 13;

    public static final int VIEW_TYPE_FILTER_ITEMS_IN_SHOP = 14;


    public static final int VIEW_TYPE_HIGHLIGHTS = 15;


    public static final int VIEW_TYPE_SHOP_INFO = 16;






    private boolean loadMore;
    private Fragment fragment;



    public Adapter(List<Object> dataset,
                   Context context,
                   Fragment fragment
    )
    {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == VIEW_TYPE_ITEM_CATEGORY)
        {
            return ViewHolderItemCategory.create(parent,context,fragment,this);
        }
        else if(viewType == VIEW_TYPE_HIGHLIGHTS)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment,ViewHolderHorizontalList.LAYOUT_TYPE_SLIDER);
        }
        else if(viewType == VIEW_TYPE_ITEM_CATEGORY_LIST)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_SHOP_ITEM)
        {

            int layoutType = ViewHolderFilterItemsInShop.getLayoutType(context);

            if(layoutType==ViewHolderFilterItemsInShop.LAYOUT_TYPE_FULL_WIDTH)
            {
                return ViewHolderShopItem.create(parent,context,fragment,this,cartItemMap,cartStats,ViewHolderShopItem.LAYOUT_TYPE_FULL_WIDTH);
//                return ViewHolderShopItemInstacart.create(parent,context,fragment,this,cartItemMap,cartStats,ViewHolderShopItemInstacart.LAYOUT_TYPE_FULL_WIDTH);
            }
            else if(layoutType==ViewHolderFilterItemsInShop.LAYOUT_TYPE_HALF_WIDTH)
            {
                if(context.getResources().getBoolean(R.bool.use_add_button_layout_for_item_in_shop))
                {
                    return ViewHolderShopItem.create(parent,context,fragment,this,cartItemMap,cartStats,ViewHolderShopItem.LAYOUT_TYPE_HALF_WIDTH);
                }
                else
                {
                    return ViewHolderShopItemInstacart.create(parent,context,fragment,this,cartItemMap,cartStats,ViewHolderShopItemInstacart.LAYOUT_TYPE_GRID);
                }
            }



        }
        else if(viewType == VIEW_TYPE_SHOP)
        {
            return ViewHolderShopInfo.create(parent,context,fragment,this);
        }
        else if(viewType == VIEW_TYPE_MESSAGE_ON_WHATSAPP)
        {
            return ViewHolderWhatsApp.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeader.createWhite(parent,context);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM)
        {
            return ViewHolderEmptyScreenListItem.create(parent,context, fragment);
        }
        else if(viewType==VIEW_TYPE_FILTER_ITEMS_IN_SHOP)
        {
            return ViewHolderFilterItemsInShop.create(parent,context, fragment);
        }
        else if(viewType==VIEW_TYPE_SHOP_INFO)
        {
            return ViewHolderShopInfo.create(parent,context,fragment,this);
        }

        return null;
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if(holder instanceof ViewHolderShopSmall)
        {
            ((ViewHolderShopSmall) holder).setItem((Shop) dataset.get(position),false);
        }
        else if(holder instanceof ViewHolderItemCategory)
        {
            ((ViewHolderItemCategory) holder).bindItemCategory((ItemCategory) dataset.get(position));
        }
        else if(holder instanceof ViewHolderHorizontalList)
        {


            if(getItemViewType(position)==VIEW_TYPE_HIGHLIGHTS)
            {
                Highlights highlights = ((Highlights)dataset.get(position));

                List<Object> list = highlights.getHighlightList();
                AdapterBannerImages adapterBannerImages = new AdapterBannerImages(list,context,fragment);
                ((ViewHolderHorizontalList) holder).setItem(adapterBannerImages, highlights.getListTitle());

            }
            else if(getItemViewType(position)==VIEW_TYPE_ITEM_CATEGORY_LIST)
            {
                ItemCategoriesList categoriesList = (ItemCategoriesList)dataset.get(position);

                List<ItemCategory> list = categoriesList.getItemCategories();

                ((ViewHolderHorizontalList) holder).setItem(new AdapterItemCatHorizontalList(list,context,fragment),null);
                ((ViewHolderHorizontalList) holder).scrollToPosition(categoriesList.getScrollPositionForSelected());
            }

        }
        else if(holder instanceof ViewHolderShopItemInstacart)
        {
            ((ViewHolderShopItemInstacart) holder).bindShopItems((ShopItem) dataset.get(position));
        }
        else if(holder instanceof ViewHolderShopInfo)
        {
            ((ViewHolderShopInfo) holder).bindShop((Shop) dataset.get(position));
        }
        else if(holder instanceof ViewHolderShopItem)
        {
            ((ViewHolderShopItem) holder).bindShopItems((ShopItem) dataset.get(position));
        }
        else if(holder instanceof ViewHolderShopItemButton)
        {
            ((ViewHolderShopItemButton) holder).bindShopItems((ShopItem) dataset.get(position));
        }
        else if(holder instanceof ViewHolderWhatsApp)
        {
            ((ViewHolderWhatsApp) holder).setItem((WhatsMessageData) dataset.get(position));

        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((HeaderTitle) dataset.get(position));
            }

        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);

        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));
        }
        else if(holder instanceof ViewHolderEmptyScreenListItem)
        {
            ((ViewHolderEmptyScreenListItem) holder).setItem((EmptyScreenDataListItem) dataset.get(position));
        }
        else if(holder instanceof ViewHolderFilterItemsInShop)
        {
            ((ViewHolderFilterItemsInShop) holder).setItem((FilterItemsInShop) dataset.get(position));
        }


    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof Shop)
        {
            return VIEW_TYPE_SHOP;
        }
        else if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY;
        }
        else if(dataset.get(position) instanceof ItemCategoriesList)
        {
            return VIEW_TYPE_ITEM_CATEGORY_LIST;
        }
        else if (dataset.get(position) instanceof ShopItem)
        {
            return VIEW_TYPE_SHOP_ITEM;
        }
        else if (dataset.get(position) instanceof WhatsMessageData)
        {
            return VIEW_TYPE_MESSAGE_ON_WHATSAPP;

        }
        else if(dataset.get(position) instanceof Highlights)
        {
            return VIEW_TYPE_HIGHLIGHTS;
        }
        else if(dataset.get(position) instanceof HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if(dataset.get(position) instanceof EmptyScreenDataListItem)
        {
            return VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM;
        }
        else if(dataset.get(position) instanceof FilterItemsInShop)
        {
            return VIEW_TYPE_FILTER_ITEMS_IN_SHOP;
        }


        return -1;
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