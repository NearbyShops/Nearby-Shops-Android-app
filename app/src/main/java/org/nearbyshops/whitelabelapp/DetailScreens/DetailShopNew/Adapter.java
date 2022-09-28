package org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew.Model.ShopAddress;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew.ViewHolders.ViewHolderRateAndReviewShop;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew.ViewHolders.ViewHolderShopAddress;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew.ViewHolders.ViewHolderShopInformation;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHorizontalList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 25/5/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;



    int vibrant;
    int vibrantDark;




    public static final int VIEW_TYPE_SHOP_DETAILS = 1;
    public static final int VIEW_TYPE_SHOP_IMAGE = 2;
    public static final int VIEW_TYPE_HEADER = 3;

    public static final int VIEW_TYPE_SHOP_ADDRESS = 4;
    public static final int VIEW_TYPE_SHOP_REVIEW = 5;



    private boolean loadMore;




    public Adapter(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }



    void setColors(int vibrant, int vibrantDark)
    {
        this.vibrant = vibrant;
        this.vibrantDark = vibrantDark;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if(viewType == VIEW_TYPE_SHOP_IMAGE)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment,ViewHolderHorizontalList.LAYOUT_TYPE_NORMAL);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeader.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_SHOP_DETAILS)
        {
            return ViewHolderShopInformation.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_SHOP_ADDRESS)
        {
            return ViewHolderShopAddress.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_SHOP_REVIEW)
        {
            return ViewHolderRateAndReviewShop.create(parent,context,fragment);
        }


        return ViewHolderShopInformation.create(parent,context,fragment);
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        if(holder instanceof ViewHolderHorizontalList) {

            if(getItemViewType(position)==VIEW_TYPE_SHOP_IMAGE)
            {
                ShopImageEndPoint highlights = ((ShopImageEndPoint)dataset.get(position));
                AdapterShopImages adapterShopImages = new AdapterShopImages(new ArrayList<>(highlights.getResults()),context,fragment);
                ((ViewHolderHorizontalList) holder).setItem(adapterShopImages,null);
            }

        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((ViewHolderHeader.HeaderTitle) dataset.get(position));
            }

        }
        else if(holder instanceof ViewHolderShopInformation)
        {
            ((ViewHolderShopInformation) holder).setShop((Shop) dataset.get(position));
            ((ViewHolderShopInformation) holder).setColors(vibrant,vibrantDark);

        }
        else if(holder instanceof ViewHolderShopAddress)
        {
            ((ViewHolderShopAddress) holder).setShop((ShopAddress) dataset.get(position));
            ((ViewHolderShopAddress) holder).setColors(vibrant,vibrantDark);
        }
        else if(holder instanceof ViewHolderRateAndReviewShop)
        {
            ((ViewHolderRateAndReviewShop) holder).setShopReview((ShopReview) dataset.get(position));
            ((ViewHolderRateAndReviewShop) holder).setColors(vibrant,vibrantDark);
        }



    }





    @Override
    public int getItemCount() {

        return (dataset.size());
    }





    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(dataset.get(position) instanceof ViewHolderHeader.HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof ShopImageEndPoint)
        {
            return VIEW_TYPE_SHOP_IMAGE;
        }
        else if(dataset.get(position) instanceof Shop)
        {
            return VIEW_TYPE_SHOP_DETAILS;
        }
        else if(dataset.get(position) instanceof ShopAddress)
        {
            return VIEW_TYPE_SHOP_ADDRESS;
        }
        else if(dataset.get(position) instanceof ShopReview)
        {
            return VIEW_TYPE_SHOP_REVIEW;
        }


        return VIEW_TYPE_HEADER;
    }




}
