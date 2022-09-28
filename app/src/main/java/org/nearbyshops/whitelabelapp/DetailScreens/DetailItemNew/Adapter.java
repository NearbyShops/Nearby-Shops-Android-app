package org.nearbyshops.whitelabelapp.DetailScreens.DetailItemNew;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ItemImageEndPoint;
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


    public static final int VIEW_TYPE_ITEM_DETAILS = 1;
    public static final int VIEW_TYPE_ITEM_IMAGE = 2;
    public static final int VIEW_TYPE_HEADER = 3;



    private boolean loadMore;




    public Adapter(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if(viewType == VIEW_TYPE_ITEM_IMAGE)
        {
            return ViewHolderHorizontalList.create(parent,context,fragment,ViewHolderHorizontalList.LAYOUT_TYPE_NORMAL);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeader.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_ITEM_DETAILS)
        {
            return ViewHolderItemDetail.create(parent,context,fragment);
        }


        return ViewHolderItemDetail.create(parent,context,fragment);
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        if(holder instanceof ViewHolderHorizontalList) {

            if(getItemViewType(position)==VIEW_TYPE_ITEM_IMAGE)
            {
                ItemImageEndPoint highlights = ((ItemImageEndPoint)dataset.get(position));
                AdapterItemImages adapterItemImages = new AdapterItemImages(new ArrayList<>(highlights.getResults()),context,fragment);
                ((ViewHolderHorizontalList) holder).setItem(adapterItemImages,null);
            }

        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((ViewHolderHeader.HeaderTitle) dataset.get(position));
            }
        }
        else if(holder instanceof ViewHolderItemDetail)
        {
            ((ViewHolderItemDetail) holder).setItem((Item) dataset.get(position));
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
        else if(dataset.get(position) instanceof ItemImageEndPoint)
        {
            return VIEW_TYPE_ITEM_IMAGE;
        }
        else if(dataset.get(position) instanceof Item)
        {
            return VIEW_TYPE_ITEM_DETAILS;
        }

        return VIEW_TYPE_HEADER;
    }




}
