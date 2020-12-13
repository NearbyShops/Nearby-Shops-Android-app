package org.nearbyshops.enduserappnew.Lists.DeliveryAddress;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderDeliveryAddress;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.ButtonData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderButton;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;

import java.util.List;

/**
 * Created by sumeet on 6/6/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;
    private ViewHolderDeliveryAddress.ListItemClick listItemClick;



    private final int VIEW_TYPE_EMPTY_SCREEN = 1;
    private final int VIEW_TYPE_DELIVERY_ADDRESS = 2;
    private final int VIEW_TYPE_BUTTON = 3;



    public Adapter(List<Object> dataset, Context context, ViewHolderDeliveryAddress.ListItemClick listItemClick) {

        this.dataset = dataset;
        this.context = context;
        this.listItemClick = listItemClick;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if(viewType==VIEW_TYPE_DELIVERY_ADDRESS)
        {
            return ViewHolderDeliveryAddress.create(parent,context, listItemClick);
        }
        else if(viewType == VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context,null);
        }
        else if(viewType==VIEW_TYPE_BUTTON)
        {
            return ViewHolderButton.create(parent,context,fragment,ViewHolderButton.LAYOUT_TYPE_ADD_NEW_LOCATION);
        }


        return ViewHolderEmptyScreenFullScreen.create(parent,context,null);
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderDeliveryAddress)
        {
            ((ViewHolderDeliveryAddress) holder).setItem((DeliveryAddress) dataset.get(position));
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));

        }
        else if(holder instanceof ViewHolderButton)
        {
            ((ViewHolderButton) holder).setItem((ButtonData) dataset.get(position));
        }


    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }





    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        int viewType = 0;

        if(dataset.get(position) instanceof EmptyScreenDataListItem)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if(dataset.get(position) instanceof DeliveryAddress)
        {
            return VIEW_TYPE_DELIVERY_ADDRESS;
        }
        else if(dataset.get(position) instanceof ButtonData)
        {
            return VIEW_TYPE_BUTTON;
        }


        return viewType;
    }



}
