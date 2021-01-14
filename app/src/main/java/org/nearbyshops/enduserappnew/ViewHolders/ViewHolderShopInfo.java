package org.nearbyshops.enduserappnew.ViewHolders;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderShopInfo extends RecyclerView.ViewHolder
{


    @BindView(R.id.list_item) ConstraintLayout listItem;

    @BindView(R.id.shop_logo) ImageView shopLogo;
    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.rating_numeric) TextView ratingNumeric;
    @BindView(R.id.rating) RatingBar rating;
    @BindView(R.id.description) TextView shopDescription;



    private Context context;
    private Shop shop;
    private Fragment fragment;

    private RecyclerView.Adapter adapter;





    public static ViewHolderShopInfo create(ViewGroup parent, Context context, Fragment fragment,
                                            RecyclerView.Adapter adapter)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_info,parent,false);
        return new ViewHolderShopInfo(view,context,fragment,adapter);
    }








    public ViewHolderShopInfo(View itemView, Context context, Fragment fragment, RecyclerView.Adapter adapter
    )
    {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
    }






    @OnClick(R.id.list_item)
    public void itemCategoryListItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).shopInfoClick();
        }
    }







    public void bindShop(Shop shop)
    {
        this.shop = shop;

        shopName.setText(shop.getShopName());

        if(shop.getRt_rating_count()==0)
        {
            ratingNumeric.setText(" New ");
            rating.setVisibility(View.GONE);
        }
        else
        {
            ratingNumeric.setText(String.format("%.2f",shop.getRt_rating_avg()));
            rating.setRating(shop.getRt_rating_avg());
        }

        shopDescription.setText(shop.getLongDescription());



        if(shop.getLongDescription()==null || shop.getLongDescription().equals(""))
        {
            shopDescription.setVisibility(View.GONE);
        }



        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                + shop.getLogoImagePath() + ".jpg";

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(shopLogo);

    }







    public interface ListItemClick
    {
        void shopInfoClick();
    }



}// ViewHolderShopItemBackup Class declaration ends





