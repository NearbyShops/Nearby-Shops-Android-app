package org.nearbyshops.enduserappnew.UtilityScreens.FavouriteShops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelEndPoints.FavouriteShopEndpoint;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelShop;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ViewHolderFavouriteShopsList extends RecyclerView.ViewHolder {


    AdapterFavouriteShop adapterFavouriteShop;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.list_title) TextView listTitle;

    FavouriteShopEndpoint endpoint;


    private Context context;
    private Fragment fragment;



    private ViewModelShop viewModelShop;



    public static ViewHolderFavouriteShopsList create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_horizontal_list,parent,false);

        return new ViewHolderFavouriteShopsList(view,context,fragment);
    }





    public ViewHolderFavouriteShopsList(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


        setupViewModel();


        viewModelShop.getFavouriteShops(PrefLogin.getUser(context).getUserID());
    }



    public void setTitle(String listTitleString)
    {
//        this.endpoint = endpoint;

        if(listTitleString==null)
        {
            listTitle.setVisibility(View.GONE);
        }
        else
        {
            listTitle.setVisibility(View.VISIBLE);
            listTitle.setText(listTitleString);
        }

    }



    public void refreshList()
    {
        viewModelShop.getFavouriteShops(PrefLogin.getUser(context).getUserID());
    }






    void setupShopList()
    {

        adapterFavouriteShop = new AdapterFavouriteShop(new ArrayList<>(endpoint.getResults()),context,fragment);

        recyclerView.setAdapter(adapterFavouriteShop);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapterFavouriteShop.notifyDataSetChanged();
    }







    public void setTextSize(float size)
    {
        listTitle.setTextSize(size);
    }


    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(context,message);
    }




    void setupViewModel()
    {

        viewModelShop = new ViewModelShop(MyApplication.application);


        viewModelShop.favouriteEndpointLive.observe(fragment.getViewLifecycleOwner(), new Observer<FavouriteShopEndpoint>() {
            @Override
            public void onChanged(FavouriteShopEndpoint endpoint) {

                ViewHolderFavouriteShopsList.this.endpoint = endpoint;
                if(endpoint.getResults().size()>0)
                {
                    setTitle("Favourite Shops");
                }

                setupShopList();
            }
        });




        viewModelShop.getEvent().observe(fragment.getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {


                if(integer == ViewModelShop.EVENT_SHOP_DELETED)
                {
                    ViewHolderFavouriteShopsList.this.adapterFavouriteShop.notifyItemRemoved(getAdapterPosition());
                }

            }
        });




        viewModelShop.getMessage().observe(fragment. getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage(s);
            }
        });
    }

}
