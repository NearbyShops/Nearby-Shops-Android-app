package org.nearbyshops.enduserappnew.InventoryOrders.OrderFilters.ShopFilter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.Shop;

import java.util.List;




public class AdapterFilterShops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewHolderFilterShop.ShopSelection {



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;

    private int shopID = -1;


    public static final int VIEW_TYPE_SHOP_FILTER = 1;



    public AdapterFilterShops(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType==VIEW_TYPE_SHOP_FILTER)
        {
            return ViewHolderFilterShop.create(parent,context,fragment,this,this);
        }


        return null;
    }






    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

      if(holder instanceof ViewHolderFilterShop)
        {
            ((ViewHolderFilterShop) holder).setItem((Shop) dataset.get(position));
        }

    }






    @Override
    public int getItemViewType(int position) {

        if(dataset.get(position) instanceof Shop)
        {
            return VIEW_TYPE_SHOP_FILTER;
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
    public void setShopID(int shopID) {
        this.shopID=shopID;
    }

    @Override
    public int getShopID() {
        return shopID;
    }
}