package org.nearbyshops.whitelabelapp.Lists.ShopsAvailableForItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.ModelStats.CartStats;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderCart;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderCartNew;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import java.util.List;

/**
 * Created by sumeet on 5/6/16.
 */
public class AdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;



    public static final int VIEW_TYPE_CART = 1;

    public static final int VIEW_TYPE_HEADER = 2;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 4;



    public AdapterNew(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == VIEW_TYPE_CART) {

            return ViewHolderCartNew.create(parent,context,fragment);
        }
        else if (viewType == VIEW_TYPE_HEADER) {

            return ViewHolderHeader.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context,fragment);
        }


        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolderCart) {

            ((ViewHolderCart) holder).setItem((CartStats) dataset.get(position));

        }
        else if (holder instanceof ViewHolderCartNew) {

            ((ViewHolderCartNew) holder).setItem((CartStats) dataset.get(position));

        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen) dataset.get(position));
        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((ViewHolderHeader.HeaderTitle) dataset.get(position));
            }

        }
    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

            return VIEW_TYPE_HEADER;
        }
        else if (dataset.get(position) instanceof CartStats) {

            return VIEW_TYPE_CART;
        }
        else if(dataset.get(position) instanceof ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }


        return -1;
    }




    @Override
    public int getItemCount() {

        return dataset.size();
    }


}
