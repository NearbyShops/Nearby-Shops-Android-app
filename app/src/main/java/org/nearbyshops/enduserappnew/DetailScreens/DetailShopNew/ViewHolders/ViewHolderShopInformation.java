package org.nearbyshops.enduserappnew.DetailScreens.DetailShopNew.ViewHolders;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderShopInformation extends RecyclerView.ViewHolder{



    @BindView(R.id.shop_name) TextView shopName;

    @BindView(R.id.shop_rating_numeric) TextView shopRatingNumeric;
    @BindView(R.id.shop_rating) RatingBar ratingBar;
    @BindView(R.id.rating_count) TextView ratingCount;


    @BindView(R.id.shop_description) TextView shopDescription;
    @BindView(R.id.read_full_button) TextView readFullDescription;



    @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
    @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;


    int vibrant;
    int vibrantDark;




    private Context context;
    private Shop shop;
    private Fragment fragment;


    public static ViewHolderShopInformation create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_detail, parent, false);
        return new ViewHolderShopInformation(view, context, fragment);
    }





    public ViewHolderShopInformation(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


    }







    public void setShop(Shop shop) {
        this.shop = shop;

        shopName.setText(shop.getShopName());



        if(shop.getRt_rating_count()==0)
        {
            shopRatingNumeric.setText(" New ");
            shopRatingNumeric.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
            ratingBar.setVisibility(View.GONE);
            ratingCount.setVisibility(View.GONE);
        }
        else
        {
            shopRatingNumeric.setText(String.format("%.2f",shop.getRt_rating_avg()));
            ratingBar.setRating(shop.getRt_rating_avg());
            ratingCount.setText("(" + shop.getRt_rating_count() + " ratings )");
        }


        shopDescription.setText(shop.getLongDescription());





        if(shop.getPickFromShopAvailable())
        {
            pickFromShopIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            pickFromShopIndicator.setVisibility(View.GONE);
        }


        if(shop.getHomeDeliveryAvailable())
        {
            homeDeliveryIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            homeDeliveryIndicator.setVisibility(View.GONE);
        }

    }




    public void setColors(int vibrant, int vibrantDark)
    {
        this.vibrant = vibrant;
        this.vibrantDark = vibrantDark;

//        shopName.setTextColor(vibrant);
    }





    @OnClick(R.id.read_full_button)
    void readFullButtonClick() {

        shopDescription.setMaxLines(Integer.MAX_VALUE);
        readFullDescription.setVisibility(View.GONE);
    }


}
