package org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightListItem;

import java.util.List;


public class AdapterHighlights extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<Object> dataset;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_HIGHLIGHT_ITEM = 1;



    private boolean loadMore;





    public AdapterHighlights(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        if(viewType==VIEW_TYPE_HIGHLIGHT_ITEM)
        {
            return ViewHolderHighlightListItem.create(viewGroup,context,fragment,this);
        }


        return ViewHolderHighlightListItem.create(viewGroup,context,fragment,this);
    }






    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderHighlightListItem)
        {
            ((ViewHolderHighlightListItem) holder).setItem((HighlightListItem) dataset.get(position));
        }

    }






    @Override
    public int getItemCount() {

        return dataset.size();
    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (dataset.get(position) instanceof HighlightListItem) {

            return VIEW_TYPE_HIGHLIGHT_ITEM;
        }

        return -1;
    }






    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }



}
