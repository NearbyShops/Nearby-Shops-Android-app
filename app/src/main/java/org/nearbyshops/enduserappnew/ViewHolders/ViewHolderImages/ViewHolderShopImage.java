package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderImages;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.ModelImages.ShopImage;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderShopImage extends RecyclerView.ViewHolder{



    @BindView(R.id.title) TextView imageTitle;
    @BindView(R.id.description) TextView imageDescription;
    @BindView(R.id.copyright_info) TextView copyrights;
    @BindView(R.id.image) ImageView shopImageView;
    @BindView(R.id.list_item) ConstraintLayout listItem;
    @BindView(R.id.check_icon) ImageView checkIcon;



    @BindView(R.id.popup_menu) ImageView popupMenu;


    private Context context;
    private ShopImage shopImage;
    private Shop shop;

    private Fragment fragment;

    public static int LAYOUT_TYPE_SLIDER = 1;
    public static int LAYOUT_TYPE_NORMAL = 2;





    public static ViewHolderShopImage create(ViewGroup parent, Context context, Fragment fragment,int layoutType)
    {
        View view = null;

        if(layoutType==LAYOUT_TYPE_NORMAL)
        {
            view  = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_shop_image,parent,false);

        }
        else if(layoutType==LAYOUT_TYPE_SLIDER)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_shop_image_slider,parent,false);
        }


        return new ViewHolderShopImage(view,context,fragment);
    }





    public ViewHolderShopImage(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }





    public void setShopImage(ShopImage shopImage, boolean isAdminMode)
    {

        this.shopImage = shopImage;


        imageTitle.setText(shopImage.getCaptionTitle());
        imageDescription.setText(shopImage.getCaption());
        copyrights.setText(shopImage.getCopyrights());


        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);



        String imagePathSmall = PrefGeneral.getServiceURL(context) + "/api/v1/ShopImage/Image/five_hundred_" + shopImage.getImageFilename() + ".jpg";
        String imagePathMedium = PrefGeneral.getServiceURL(context) + "/api/v1/ShopImage/Image/nine_hundred_" + shopImage.getImageFilename() + ".jpg";
        String imagePathFullSize = PrefGeneral.getServiceURL(context) + "/api/v1/ShopImage/Image/" + shopImage.getImageFilename();


        Picasso.get()
                .load(imagePathMedium)
                .placeholder(drawable)
                .into(shopImageView);




        if(isAdminMode)
        {
            popupMenu.setVisibility(View.VISIBLE);
        }
        else
        {
            popupMenu.setVisibility(View.GONE);
        }


    }




    public void setShop(Shop shop)
    {

        this.shop = shop;


        imageTitle.setText(shop.getShopName());

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);


        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/five_hundred_"
                + shop.getLogoImagePath() + ".jpg";

        Picasso.get()
                .load(imagePath)
                .placeholder(drawable)
                .into(shopImageView);


        popupMenu.setVisibility(View.GONE);

    }




    @OnClick(R.id.list_item)
    void listItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick)fragment).listItemClick(shopImage,getAdapterPosition());
        }
    }







    @OnClick(R.id.popup_menu)
    void popupClick(View v)
    {
        PopupMenu menu = new PopupMenu(context,v);

        menu.getMenuInflater().inflate(R.menu.menu_shop_images_list_item,menu.getMenu());

        menu.show();
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.delete :

                        if(fragment instanceof ListItemClick)
                        {
                            ((ListItemClick) fragment).deleteClick(shopImage,getAdapterPosition());
                        }



                        break;


                    case R.id.edit :



                        if(fragment instanceof ListItemClick)
                        {
                            ((ListItemClick) fragment).editClick(shopImage,getAdapterPosition());
                        }


                        break;
                }


                return false;
            }
        });

    }




    public interface ListItemClick
    {
        void listItemClick(ShopImage shopImage, int position);
        boolean listItemLongClick(View view, ShopImage shopImage, int position);
        void deleteClick(ShopImage shopImage, int position);
        void editClick(ShopImage shopImage, int position);
    }

}

