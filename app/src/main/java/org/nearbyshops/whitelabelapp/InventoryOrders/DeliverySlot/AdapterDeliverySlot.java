package org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderAddItem;

import java.util.List;



/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterDeliverySlot extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewHolderDeliverySlot.SlotSelection{



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;
    private int deliverySlotMode;

    private int selectedDeliverySlotID = -1;



    public static final int VIEW_TYPE_ADD_IMAGE = 2;
    public static final int VIEW_TYPE_DELIVERY_SLOT = 1;





    public AdapterDeliverySlot(List<Object> dataset, Context context, Fragment fragment, int deliverySlotMode) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
        this.deliverySlotMode = deliverySlotMode;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType==VIEW_TYPE_DELIVERY_SLOT)
        {
            return ViewHolderDeliverySlot.create(parent,context,fragment,deliverySlotMode,this,this);
        }
        else if(viewType == VIEW_TYPE_ADD_IMAGE)
        {
            return ViewHolderAddItem.create(parent,context,fragment);
        }



        return null;
    }






    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

      if(holder instanceof ViewHolderDeliverySlot)
        {

            ((ViewHolderDeliverySlot) holder).setItem((DeliverySlot) dataset.get(position));
        }

    }






    @Override
    public int getItemViewType(int position) {

        if(dataset.get(position) instanceof ViewHolderAddItem.AddItemData)
        {
            return VIEW_TYPE_ADD_IMAGE;
        }
        else if(dataset.get(position) instanceof DeliverySlot)
        {
            return VIEW_TYPE_DELIVERY_SLOT;
        }


        return 0;

    }







    @Override
    public int getItemCount() {

        return dataset.size();
    }



    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }







    @Override
    public void setDeliverySlotID(int deliverySlotID) {

        selectedDeliverySlotID = deliverySlotID;
    }


    @Override
    public int getDeliverySlotID() {
        return selectedDeliverySlotID;
    }



}