package org.nearbyshops.enduserappnew.Lists.ItemsByCategory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.UtilityScreens.BannerSlider.AdapterBannerImages;
import org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model.Highlights;
import org.nearbyshops.enduserappnew.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.Models.FilterItemsInMarket;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.ViewHolderFilterItems;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SetLocationManually;
import org.nearbyshops.enduserappnew.mfiles.SwitchMarketData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderHeader;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderHorizontalList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;
import org.nearbyshops.enduserappnew.mfiles.ViewHolderSwitchMarket;

import java.util.List;

/**
 * Created by sumeet on 19/12/15.
 */




public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset;
    private Context context;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_ITEM_CATEGORY_LIST = 2;
    public static final int VIEW_TYPE_ITEM = 3;


    public static final int VIEW_TYPE_HEADER = 4;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;

    public static final int VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM = 7;

    public static final int VIEW_TYPE_SET_LOCATION_MANUALLY = 9;
    public static final int VIEW_TYPE_HIGHLIGHTS = 10;

    public static final int VIEW_TYPE_FILTER_ITEMS = 11;


    public static final int VIEW_TYPE_SWITCH_MARKET = 16;



    private boolean loadMore;



    private Fragment fragment;

    public Adapter(List<Object> dataset, Context context, Fragment fragment) {


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
        else if(viewType == VIEW_TYPE_ITEM_CATEGORY_LIST)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_HIGHLIGHTS)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment,ViewHolderHorizontalList.LAYOUT_TYPE_SLIDER);
        }
        else if(viewType==VIEW_TYPE_SET_LOCATION_MANUALLY)
        {
            return ViewHolderSetLocationManually.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_ITEM)
        {

            return ViewHolderItem.create(parent,context,fragment,ViewHolderItem.LAYOUT_TYPE_GRID);
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
        else if(viewType==VIEW_TYPE_FILTER_ITEMS)
        {
            return ViewHolderFilterItems.create(parent,context, fragment);
        }
        else if(viewType==VIEW_TYPE_SWITCH_MARKET)
        {
            return ViewHolderSwitchMarket.create(parent,fragment.getActivity(),fragment);
        }


        return null;
    }








    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if(holder instanceof ViewHolderItemCategory)
        {
            ((ViewHolderItemCategory) holder).bindItemCategory((ItemCategory) dataset.get(position));
        }
        else if(holder instanceof ViewHolderHorizontalList)
        {
            if(getItemViewType(position)==VIEW_TYPE_ITEM_CATEGORY_LIST)
            {

                ItemCategoriesList categoriesList = ((ItemCategoriesList)dataset.get(position));
                List<ItemCategory> list = categoriesList.getItemCategories();
                ((ViewHolderHorizontalList) holder).setItem(new AdapterItemCatHorizontalList(list,context,fragment),null);
                ((ViewHolderHorizontalList) holder).scrollToPosition(categoriesList.getScrollPositionForSelected());


            }
            else if(getItemViewType(position)==VIEW_TYPE_HIGHLIGHTS)
            {

                Highlights highlights = ((Highlights)dataset.get(position));

                List<Object> list = highlights.getHighlightList();
                AdapterBannerImages adapterBannerImages = new AdapterBannerImages(list,context,fragment);
                ((ViewHolderHorizontalList) holder).setItem(adapterBannerImages, highlights.getListTitle());
            }


        }
        else if(holder instanceof ViewHolderSetLocationManually)
        {
            ((ViewHolderSetLocationManually)holder).bindDashboard();
        }
        else if(holder instanceof ViewHolderItem)
        {

            ((ViewHolderItem) holder).setItem((Item) dataset.get(position));

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
        else if(holder instanceof ViewHolderFilterItems)
        {
            ((ViewHolderFilterItems) holder).setItem((FilterItemsInMarket) dataset.get(position));
        }
        else if(holder instanceof ViewHolderSwitchMarket)
        {
            ((ViewHolderSwitchMarket) holder).bindDashboard();

        }


    }





    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);



        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY;
        }
        else if(dataset.get(position) instanceof ItemCategoriesList)
        {
            return VIEW_TYPE_ITEM_CATEGORY_LIST;
        }
        else if (dataset.get(position) instanceof Item)
        {
            return VIEW_TYPE_ITEM;
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
        else if(dataset.get(position) instanceof Highlights)
        {
            return VIEW_TYPE_HIGHLIGHTS;
        }
        else if(dataset.get(position) instanceof SetLocationManually)
        {
            return VIEW_TYPE_SET_LOCATION_MANUALLY;
        }
        else if(dataset.get(position) instanceof FilterItemsInMarket)
        {
            return VIEW_TYPE_FILTER_ITEMS;
        }
        else if(dataset.get(position) instanceof SwitchMarketData)
        {

            return VIEW_TYPE_SWITCH_MARKET;
        }


        return VIEW_TYPE_HEADER;
    }




    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }



    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }


}