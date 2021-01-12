package org.nearbyshops.enduserappnew.EditDataScreens.EditItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.Model.ModelImages.ItemImage;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.AddItemData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderAddItem;

import java.util.List;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterItemImages extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;



    public static final int VIEW_TYPE_ADD_IMAGE = 2;
    public static final int VIEW_TYPE_IMAGE = 1;





    public AdapterItemImages(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType==VIEW_TYPE_IMAGE)
        {
            return ViewHolderItemImage.create(parent,context,fragment);
        }
        else if(viewType == VIEW_TYPE_ADD_IMAGE)
        {
            return ViewHolderAddItem.create(parent,context,fragment);
        }


        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

      if(holder instanceof ViewHolderItemImage)
        {
            ((ViewHolderItemImage) holder).setItem((ItemImage) dataset.get(position));
        }

    }




    @Override
    public int getItemViewType(int position) {

        if(dataset.get(position) instanceof AddItemData)
        {
            return VIEW_TYPE_ADD_IMAGE;
        }
        else if(dataset.get(position) instanceof ItemImage)
        {
            return VIEW_TYPE_IMAGE;
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


}