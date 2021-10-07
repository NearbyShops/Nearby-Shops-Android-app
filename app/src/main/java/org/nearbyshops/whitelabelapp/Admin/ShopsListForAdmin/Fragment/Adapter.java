package org.nearbyshops.whitelabelapp.Admin.ShopsListForAdmin.Fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Admin.ViewHolders.ViewHolderShopForAdmin;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.AdapterHighlights;
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightList;
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.ViewHolderHighlightList;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterShopsAdminData;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.ViewHolderFilterShopsAdmin;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import java.util.List;

/**
 * Created by sumeet on 25/5/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_SHOP = 1;

    public static final int VIEW_TYPE_HEADER = 4;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 5;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 6;

    public static final int VIEW_TYPE_FILTER_SHOPS = 7;
    public static final int VIEW_TYPE_HIGHLIGHTS = 8;



    private boolean loadMore;
    private int screenMode;




    public Adapter(List<Object> dataset, Context context, Fragment fragment, int screenMode) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
        this.screenMode = screenMode;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;


        if(viewType == VIEW_TYPE_SHOP)
        {
            return ViewHolderShopForAdmin.create(parent,context,fragment,dataset,this,screenMode);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeader.create(parent,context);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_FILTER_SHOPS)
        {
            return ViewHolderFilterShopsAdmin.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_HIGHLIGHTS)
        {
            return ViewHolderHighlightList.Companion.create(parent, context, fragment);
        }


        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderShopForAdmin)
        {
            ((ViewHolderShopForAdmin) holder).setItem((Shop) dataset.get(position));
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
        else if(holder instanceof ViewHolderFilterShopsAdmin)
        {
            ((ViewHolderFilterShopsAdmin) holder).setItem((FilterShopsAdminData) dataset.get(position));
        }
        else if(holder instanceof ViewHolderHighlightList)
        {

            HighlightList highlights = (HighlightList) dataset.get(position);
            List<Object> list = highlights.getHighlightItemList();

            ((ViewHolderHighlightList) holder).setItem(
                    new AdapterHighlights(list, context, fragment),
                    highlights
            );

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
        else if(dataset.get(position) instanceof ViewHolderHeader.HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if(dataset.get(position) instanceof FilterShopsAdminData)
        {
            return VIEW_TYPE_FILTER_SHOPS;
        }
        else if (dataset.get(position) instanceof HighlightList) {

            return VIEW_TYPE_HIGHLIGHTS;
        }

        return -1;
    }


    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }

}
