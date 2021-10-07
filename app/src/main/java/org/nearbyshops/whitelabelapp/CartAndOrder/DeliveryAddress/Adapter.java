package org.nearbyshops.whitelabelapp.CartAndOrder.DeliveryAddress;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderDeliveryAddress.ViewHolderDeliveryAddressEditable;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;

import java.util.List;

/**
 * Created by sumeet on 6/6/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset = null;
    private Context context;
    private Fragment fragment;
    private ViewHolderDeliveryAddressEditable.ListItemClick listItemClick;



    private final int VIEW_TYPE_EMPTY_SCREEN = 1;
    private final int VIEW_TYPE_DELIVERY_ADDRESS = 2;
    private final int VIEW_TYPE_BUTTON = 3;



    public Adapter(List<Object> dataset, Context context, ViewHolderDeliveryAddressEditable.ListItemClick listItemClick) {

        this.dataset = dataset;
        this.context = context;
        this.listItemClick = listItemClick;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if(viewType==VIEW_TYPE_DELIVERY_ADDRESS)
        {
            return ViewHolderDeliveryAddressEditable.create(parent,context, listItemClick);
        }
        else if(viewType == VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context,null);
        }
        else if(viewType==VIEW_TYPE_BUTTON)
        {
            return ViewHolderButton.create(parent,context,fragment,ViewHolderButton.LAYOUT_TYPE_ADD_NEW_ADDRESS);
        }


        return ViewHolderEmptyScreenFullScreen.create(parent,context,null);
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderDeliveryAddressEditable)
        {
            ((ViewHolderDeliveryAddressEditable) holder).setItem((DeliveryAddress) dataset.get(position));
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen) dataset.get(position));

        }
        else if(holder instanceof ViewHolderButton)
        {
            ((ViewHolderButton) holder).setItem((ViewHolderButton.ButtonData) dataset.get(position));
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

        if(dataset.get(position) instanceof ViewHolderEmptyScreenListItem.EmptyScreenDataListItem)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if(dataset.get(position) instanceof DeliveryAddress)
        {
            return VIEW_TYPE_DELIVERY_ADDRESS;
        }
        else if(dataset.get(position) instanceof ViewHolderButton.ButtonData)
        {
            return VIEW_TYPE_BUTTON;
        }


        return viewType;
    }



}
