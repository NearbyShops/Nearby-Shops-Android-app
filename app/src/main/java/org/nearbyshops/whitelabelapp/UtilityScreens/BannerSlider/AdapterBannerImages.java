package org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.Model.BannerImage;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderAddItem;

import java.util.List;


public class AdapterBannerImages extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<Object> dataset;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_ADD_ITEM = 1;
    public static final int VIEW_TYPE_BANNER_ITEM = 2;



    private boolean loadMore;





    public AdapterBannerImages(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        if (viewType == VIEW_TYPE_ADD_ITEM) {

            return ViewHolderAddItem.create(viewGroup,context,fragment);
        }
        else if(viewType==VIEW_TYPE_BANNER_ITEM)
        {
            return ViewHolderBannerListItem.create(viewGroup,context,fragment,this);
        }


        return ViewHolderAddItem.create(viewGroup,context,fragment);
    }






    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderBannerListItem)
        {
            ((ViewHolderBannerListItem) holder).setItem((BannerImage) dataset.get(position));
        }

    }






    @Override
    public int getItemCount() {

        return dataset.size();
    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (dataset.get(position) instanceof ViewHolderAddItem.AddItemData)
        {
            return VIEW_TYPE_ADD_ITEM;
        }
        else if (dataset.get(position) instanceof BannerImage) {

            return VIEW_TYPE_BANNER_ITEM;
        }

        return -1;
    }






    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }



}
