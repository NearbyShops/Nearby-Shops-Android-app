package org.nearbyshops.enduserappnew.DetailScreens.DetailItemNew;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelImages.ItemImage;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderImages.ViewHolderItemImage;

import java.util.List;


public class AdapterItemImages extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<Object> dataset;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_ITEM_IMAGE = 1;

    private boolean loadMore;





    public AdapterItemImages(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        if (viewType == VIEW_TYPE_ITEM_IMAGE) {

            return ViewHolderItemImage.create(viewGroup,context,fragment,ViewHolderItemImage.LAYOUT_TYPE_SLIDER);
        }

        return null;
    }






    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderItemImage)
        {
            ((ViewHolderItemImage) holder).setItemImage((ItemImage) dataset.get(position));
        }
    }






    @Override
    public int getItemCount() {
        return dataset.size();
    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);


        if (dataset.get(position) instanceof ItemImage) {

            return VIEW_TYPE_ITEM_IMAGE;
        }

        return -1;
    }






    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }



}
