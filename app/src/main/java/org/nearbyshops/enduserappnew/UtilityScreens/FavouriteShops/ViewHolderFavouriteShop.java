package org.nearbyshops.enduserappnew.UtilityScreens.FavouriteShops;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DetailScreens.DetailShopNew.ShopDetail;
import org.nearbyshops.enduserappnew.Model.ModelReviewShop.FavouriteShop;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderFavouriteShop extends RecyclerView.ViewHolder {



    @BindView(R.id.shop_photo) RoundedImageView shopPhoto;
    @BindView(R.id.shop_name) TextView shopName;


    private Context context;
    private FavouriteShop favouriteShop;
    private Fragment fragment;



    public ViewHolderFavouriteShop(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }








    public static ViewHolderFavouriteShop create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favourite_shop,parent,false);
        return new ViewHolderFavouriteShop(view,context,fragment);
    }





    public void setItem(FavouriteShop favouriteShop)
    {
        this.favouriteShop = favouriteShop;

        Shop shopLocal = favouriteShop.getShopProfile();


        if(shopLocal!=null)
        {

            shopName.setText(shopLocal.getShopName());


            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                    + shopLocal.getLogoImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                shopPhoto.setClipToOutline(true);
            }



            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(shopPhoto);

        }


    }















    @OnClick(R.id.list_item)
    public void itemCategoryListItemClick()
    {

        Intent intent = new Intent(context, ShopDetail.class);
        intent.putExtra("shop_id",favouriteShop.getShopID());
        context.startActivity(intent);

    }







    void showToast(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }





    public interface ListItemClick
    {
        void listItemClick(int shopID);
    }


}// ViewHolder Class declaration ends

