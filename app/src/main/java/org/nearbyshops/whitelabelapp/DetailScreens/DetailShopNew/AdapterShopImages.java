package org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.ModelImages.ShopImage;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderImages.ViewHolderShopImage;

import java.util.List;


public class AdapterShopImages extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Object> dataset;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_IMAGE = 1;

    private boolean loadMore;





    public AdapterShopImages(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        if (viewType == VIEW_TYPE_IMAGE) {

            return ViewHolderShopImage.create(viewGroup,context,fragment,ViewHolderShopImage.LAYOUT_TYPE_SLIDER);
        }

        return null;
    }






    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderShopImage)
        {
            ((ViewHolderShopImage) holder).setShopImage((ShopImage) dataset.get(position),false);
        }
    }






    @Override
    public int getItemCount() {
        return dataset.size();
    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);


        if (dataset.get(position) instanceof ShopImage) {

            return VIEW_TYPE_IMAGE;
        }

        return -1;
    }






    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }



}
