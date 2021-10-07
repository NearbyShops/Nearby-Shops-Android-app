package org.nearbyshops.whitelabelapp.Lists.ShopsList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.Model.BannerImageList;
import org.nearbyshops.whitelabelapp.ViewHolders.Model.ItemCategoriesList;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterShopsData;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.ViewHolderFilterShops;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop.ViewHolderShopSmall;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop.ViewHolderShopMedium;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderAddItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderCreateShop;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderShopSuggestions;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderFullScreenProgressBar;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHorizontalList;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.AdapterBannerImages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 25/5/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_SHOP = 1;
    public static final int VIEW_TYPE_HIGHLIGHTS = 3;
    public static final int VIEW_TYPE_HEADER = 4;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;
    public static final int VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM = 7;
    public static final int VIEW_TYPE_SET_LOCATION_MANUALLY = 8;
    public static final int VIEW_TYPE_CREATE_SHOP = 9;
    private static final int view_type_nearby_markets_list = 10;
    public static final int VIEW_TYPE_SHOP_SUGGESTIONS = 11;
    public static final int VIEW_TYPE_ITEM_CATEGORY_LIST = 12;
    public static final int VIEW_TYPE_FILTER_SHOPS = 13;
    public static final int VIEW_TYPE_SWITCH_MARKET = 16;

    public static final int VIEW_TYPE_FULL_SCREEN_PROGRESS_INDICATOR = 17;

    public static final int VIEW_TYPE_LIST_ITEM_FULL_GRAPHIC = 19;


    private boolean loadMore;




    public Adapter(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;


        if(viewType == VIEW_TYPE_SHOP)
        {
            return ViewHolderShopSmall.create(parent,context,fragment,this,ViewHolderShopSmall.LAYOUT_TYPE_UBER_EATS);
        }
        else if(viewType == VIEW_TYPE_HIGHLIGHTS)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment,ViewHolderHorizontalList.LAYOUT_TYPE_SLIDER);
        }
        else if(viewType == VIEW_TYPE_ITEM_CATEGORY_LIST)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeader.create(parent,context);
        }
        else if(viewType == VIEW_TYPE_CREATE_SHOP)
        {
            return ViewHolderCreateShop.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context, fragment);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM)
        {
            return ViewHolderEmptyScreenListItem.create(parent,context, fragment,ViewHolderEmptyScreenListItem.LAYOUT_TYPE_GRAPHIC_WITH_TEXT);
        }
        else if(viewType==VIEW_TYPE_LIST_ITEM_FULL_GRAPHIC)
        {
            return ViewHolderEmptyScreenListItem.create(parent,context, fragment,ViewHolderEmptyScreenListItem.LAYOUT_TYPE_FULL_GRAPHIC);
        }
        else if(viewType==VIEW_TYPE_SET_LOCATION_MANUALLY)
        {
            return ViewHolderSetLocationManually.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_SHOP_SUGGESTIONS)
        {
            return ViewHolderShopSuggestions.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType==VIEW_TYPE_FILTER_SHOPS)
        {
            return ViewHolderFilterShops.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType==VIEW_TYPE_FULL_SCREEN_PROGRESS_INDICATOR)
        {
            return ViewHolderFullScreenProgressBar.Companion.create(parent,fragment.getActivity(),fragment);
        }


        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderShopSmall)
        {
            ((ViewHolderShopSmall) holder).setItem((Shop) dataset.get(position),false);
        }
        else if(holder instanceof ViewHolderShopMedium)
        {
            ((ViewHolderShopMedium) holder).setItem((Shop) dataset.get(position));
        }
        else if(holder instanceof ViewHolderCreateShop)
        {
            ((ViewHolderCreateShop) holder).setItem((ViewHolderCreateShop.CreateShopData) dataset.get(position));

        }
        else if(holder instanceof ViewHolderFilterShops)
        {
            ((ViewHolderFilterShops) holder).setItem((FilterShopsData) dataset.get(position));

        }
        else if(holder instanceof ViewHolderSetLocationManually)
        {
            ((ViewHolderSetLocationManually)holder).bindDashboard();
        }

        else if(holder instanceof ViewHolderHorizontalList) {

            if(getItemViewType(position)==VIEW_TYPE_HIGHLIGHTS)
            {
                BannerImageList highlights = ((BannerImageList)dataset.get(position));

                List<Object> list = new ArrayList<>(highlights.getBannerImageList());


                if(PrefLogin.getUser(context)!=null && PrefLogin.getUser(context).getRole()== User.ROLE_ADMIN_CODE)
                {
                    list.add(new ViewHolderAddItem.AddItemData());
                }


                AdapterBannerImages adapterBannerImages = new AdapterBannerImages(list,context,fragment);
                ((ViewHolderHorizontalList) holder).setItem(adapterBannerImages, highlights.getListTitle());


            }
            else if(getItemViewType(position)==VIEW_TYPE_ITEM_CATEGORY_LIST)
            {

//                List<ItemCategory> list = ((ItemCategoriesList)dataset.get(position)).getItemCategories();
//                ((ViewHolderHorizontalList) holder).setItem(new AdapterItemCatHorizontalList(list,context,fragment),"Filter Shops by Category");


                ItemCategoriesList itemCategoriesList = ((ItemCategoriesList) dataset.get(position));

                int selectedCategoryID = -1;

                if(itemCategoriesList.getSelectedCategoryID()!=null)
                {
                    selectedCategoryID=itemCategoriesList.getSelectedCategoryID();
                }


                ((ViewHolderHorizontalList) holder).setItem(
                        new AdapterFilterItemCategory(new ArrayList<>(itemCategoriesList.getItemCategories()),
                                context,fragment,selectedCategoryID), "Find Shops by Category");


                ((ViewHolderHorizontalList) holder).scrollToPosition(itemCategoriesList.getScrollPositionForSelected());
                ((ViewHolderHorizontalList) holder).setTextSize(17);

            }


//            else if(getItemViewType(position)==view_type_nearby_markets_list)
//            {
//
//                MarketsList marketsList = (MarketsList) dataset.get(position);
//                AdapterSavedMarkets adapter = new AdapterSavedMarkets(marketsList.getDataset(),fragment.getActivity(),fragment, ViewHolderMarketSmall.LAYOUT_TYPE_COVERED);
//                ((ViewHolderHorizontalList) holder).setItem(adapter,"Markets in your Area");
//            }

        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((ViewHolderHeader.HeaderTitle) dataset.get(position));
            }

        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);

        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen) dataset.get(position));
        }
        else if(holder instanceof ViewHolderEmptyScreenListItem)
        {
            ((ViewHolderEmptyScreenListItem) holder).setItem((ViewHolderEmptyScreenListItem.EmptyScreenDataListItem) dataset.get(position));
        }
        else if(holder instanceof ViewHolderShopSuggestions)
        {
            ((ViewHolderShopSuggestions) holder).setItem((ViewHolderShopSuggestions.ShopSuggestionsData) dataset.get(position));
        }
        else if(holder instanceof ViewHolderFullScreenProgressBar)
        {
            ((ViewHolderFullScreenProgressBar) holder).setItem((ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData) dataset.get(position));
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
        else if(dataset.get(position) instanceof Shop)
        {
            return VIEW_TYPE_SHOP;
        }
        else if(dataset.get(position) instanceof ViewHolderCreateShop.CreateShopData)
        {
            return VIEW_TYPE_CREATE_SHOP;
        }
        else if(dataset.get(position) instanceof ViewHolderHeader.HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof ItemCategoriesList)
        {
            return VIEW_TYPE_ITEM_CATEGORY_LIST;
        }
        else if(dataset.get(position) instanceof ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if(dataset.get(position) instanceof ViewHolderEmptyScreenListItem.EmptyScreenDataListItem)
        {
            if(((ViewHolderEmptyScreenListItem.EmptyScreenDataListItem) dataset.get(position)).getLayoutType()==ViewHolderEmptyScreenListItem.LAYOUT_TYPE_GRAPHIC_WITH_TEXT)
            {
                return VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM;
            }
//            else
//            {
//                return VIEW_TYPE_LIST_ITEM_FULL_GRAPHIC;
//            }

        }
        else if(dataset.get(position) instanceof BannerImageList)
        {
            return VIEW_TYPE_HIGHLIGHTS;
        }
        else if(dataset.get(position) instanceof ViewHolderSetLocationManually.SetLocationManually)
        {
            return VIEW_TYPE_SET_LOCATION_MANUALLY;
        }
        else if(dataset.get(position) instanceof ViewHolderShopSuggestions.ShopSuggestionsData)
        {
            return VIEW_TYPE_SHOP_SUGGESTIONS;
        }
        else if(dataset.get(position) instanceof FilterShopsData)
        {
            return VIEW_TYPE_FILTER_SHOPS;
        }
        else if(dataset.get(position) instanceof ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData)
        {
            return VIEW_TYPE_FULL_SCREEN_PROGRESS_INDICATOR;
        }


        return -1;
    }




    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }

}
