package org.nearbyshops.whitelabelapp.UtilityScreens.FavouriteShops;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.ModelReviewShop.FavouriteShop;

import java.util.List;


/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterFavouriteShop extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_FAVOURITE_SHOP = 1;





    public AdapterFavouriteShop(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType== VIEW_TYPE_FAVOURITE_SHOP)
        {
            return ViewHolderFavouriteShop.create(parent,context,fragment);
        }

        return null;
    }






    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

      if(holder instanceof ViewHolderFavouriteShop)
        {

            ((ViewHolderFavouriteShop) holder).setItem((FavouriteShop) dataset.get(position));
        }

    }






    @Override
    public int getItemViewType(int position) {

        if(dataset.get(position) instanceof FavouriteShop)
        {
            return VIEW_TYPE_FAVOURITE_SHOP;
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