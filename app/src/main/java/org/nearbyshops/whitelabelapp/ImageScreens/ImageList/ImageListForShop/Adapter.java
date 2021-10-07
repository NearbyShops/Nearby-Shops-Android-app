package org.nearbyshops.whitelabelapp.ImageScreens.ImageList.ImageListForShop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderImages.ViewHolderShopImage;
import org.nearbyshops.whitelabelapp.Model.ModelImages.ShopImage;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import java.util.List;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;

    private boolean isAdminMode = false;



    public static final int VIEW_TYPE_ITEM_IMAGE = 1;


    public static final int VIEW_TYPE_HEADER = 5;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 7;



    private boolean loadMore;




    public Adapter(List<Object> dataset, Context context, Fragment fragment, boolean isAdminMode) {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
        this.isAdminMode= isAdminMode;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == VIEW_TYPE_ITEM_IMAGE) {

            return ViewHolderShopImage.create(parent,context,fragment,ViewHolderShopImage.LAYOUT_TYPE_NORMAL);
        }
        else if (viewType == VIEW_TYPE_HEADER) {


            return ViewHolderHeader.create(parent,context);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolderShopImage) {

            if(dataset.get(position) instanceof ShopImage)
            {
                ((ViewHolderShopImage) holder).setShopImage((ShopImage) dataset.get(position),isAdminMode);
            }
            else if(dataset.get(position) instanceof Shop)
            {
                ((ViewHolderShopImage) holder).setShop((Shop) dataset.get(position));
            }



        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

                ViewHolderHeader.HeaderTitle header = (ViewHolderHeader.HeaderTitle) dataset.get(position);
                ((ViewHolderHeader) holder).setItem(header);
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
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (position == dataset.size()) {

            return VIEW_TYPE_PROGRESS_BAR;
        }
        else if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

            return VIEW_TYPE_HEADER;
        }
        else if (dataset.get(position) instanceof ShopImage || dataset.get(position) instanceof Shop) {

            return VIEW_TYPE_ITEM_IMAGE;
        }
        else if(dataset.get(position) instanceof ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }


        return -1;
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