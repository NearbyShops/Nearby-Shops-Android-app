package org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForAdmin;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForAdmin.ViewHolders.ViewHolderItemCategoryAdmin;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;

import java.util.List;
import java.util.Map;


public class AdapterItemCatHorizontalListAdmin extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<ItemCategory> dataset;
    private Context context;
    private Fragment fragment;


    private Map<Integer, ItemCategory> selectedItemCategories;
    private Map<Integer, Item> selectedItems;





    public AdapterItemCatHorizontalListAdmin(List<ItemCategory> dataset, Context context, Fragment fragment,
                                             Map<Integer, Item> selectedItems,
                                             Map<Integer, ItemCategory> selectedItemCategories) {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
        this.selectedItems = selectedItems;
        this.selectedItemCategories = selectedItemCategories;
    }







    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return ViewHolderItemCategoryAdmin.createSmall(viewGroup,context,fragment,this,selectedItems,selectedItemCategories);
    }






    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        if(viewHolder instanceof ViewHolderItemCategoryAdmin)
        {
            ((ViewHolderItemCategoryAdmin) viewHolder).setItem(dataset.get(i));
        }
    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }


}
