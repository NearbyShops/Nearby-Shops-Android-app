package org.nearbyshops.enduserappnew.Lists.ShopsList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Lists.Markets.AdapterSavedMarkets;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.Model.MarketsList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.ViewHolderMarketSmall;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopSmall;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopMedium;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.CreateShopData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderCreateShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SetLocationManually;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderHeader;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderHorizontalList;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;
import org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.AdapterHighlights;
import org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model.Highlights;

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
            return ViewHolderShopSmall.create(parent,context,fragment,this);
        }
        else if(viewType == VIEW_TYPE_HIGHLIGHTS)
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
            return ViewHolderEmptyScreenListItem.create(parent,context, fragment);
        }
        else if(viewType==VIEW_TYPE_SET_LOCATION_MANUALLY)
        {
            return ViewHolderSetLocationManually.create(parent,context,fragment);
        }
        else if(viewType==view_type_nearby_markets_list)
        {
            return ViewHolderHorizontalList.create(parent,fragment.getActivity(),fragment);
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
            ((ViewHolderCreateShop) holder).setItem((CreateShopData) dataset.get(position));

        }
        else if(holder instanceof ViewHolderSetLocationManually)
        {
            ((ViewHolderSetLocationManually)holder).bindDashboard();
        }
        else if(holder instanceof ViewHolderHorizontalList) {

            if(getItemViewType(position)==VIEW_TYPE_HIGHLIGHTS)
            {
                Highlights highlights = ((Highlights)dataset.get(position));

                List<Object> list = highlights.getHighlightList();
                AdapterHighlights adapterHighlights = new AdapterHighlights(list,context,fragment);
                ((ViewHolderHorizontalList) holder).setItem(adapterHighlights, highlights.getListTitle());
            }
            else if(getItemViewType(position)==view_type_nearby_markets_list)
            {

                MarketsList marketsList = (MarketsList) dataset.get(position);
                AdapterSavedMarkets adapter = new AdapterSavedMarkets(marketsList.getDataset(),fragment.getActivity(),fragment, ViewHolderMarketSmall.LAYOUT_TYPE_COVERED);
                ((ViewHolderHorizontalList) holder).setItem(adapter,"Markets in your Area");
            }

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
        else if(dataset.get(position) instanceof CreateShopData)
        {
            return VIEW_TYPE_CREATE_SHOP;
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
        else if(dataset.get(position) instanceof MarketsList)
        {
            return view_type_nearby_markets_list;
        }


        return -1;
    }




    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }

}
