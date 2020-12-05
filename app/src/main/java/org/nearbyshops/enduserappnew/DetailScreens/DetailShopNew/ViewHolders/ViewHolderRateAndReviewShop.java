package org.nearbyshops.enduserappnew.DetailScreens.DetailShopNew.ViewHolders;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.enduserappnew.R;

import butterknife.ButterKnife;


public class ViewHolderRateAndReviewShop extends RecyclerView.ViewHolder{


    int vibrant;
    int vibrantDark;

    private Context context;
    private ShopReview shopReview;
    private Fragment fragment;


    public static ViewHolderRateAndReviewShop create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_rate_and_review, parent, false);
        return new ViewHolderRateAndReviewShop(view, context, fragment);
    }






    public ViewHolderRateAndReviewShop(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


    }




    public void setShopReview(ShopReview shopReview) {
        this.shopReview = shopReview;
    }





    public void setColors(int vibrant, int vibrantDark)
    {
        this.vibrant = vibrant;
        this.vibrantDark = vibrantDark;
    }








}
