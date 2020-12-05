package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderImages;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelImages.ItemImage;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderItemImage extends RecyclerView.ViewHolder{


    @BindView(R.id.title) TextView imageTitle;
    @BindView(R.id.description) TextView imageDescription;
    @BindView(R.id.copyright_info) TextView copyrights;
    @BindView(R.id.taxi_image) ImageView shopImageView;
    @BindView(R.id.list_item) ConstraintLayout listItem;
    @BindView(R.id.check_icon) ImageView checkIcon;


    private Context context;
    private ItemImage itemImage;
    private Item item;

    private Fragment fragment;


    public static int LAYOUT_TYPE_SLIDER = 1;
    public static int LAYOUT_TYPE_NORMAL = 2;





    public static ViewHolderItemImage create(ViewGroup parent, Context context, Fragment fragment,int layoutType)
    {
        View view = null;

        if(layoutType==LAYOUT_TYPE_NORMAL)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_item_image_new,parent,false);
        }
        else if(layoutType==LAYOUT_TYPE_SLIDER)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_item_image_slider,parent,false);
        }


        return new ViewHolderItemImage(view,context,fragment);
    }





    public ViewHolderItemImage(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }





    public void setItemImage(ItemImage itemImage)
    {

        this.itemImage = itemImage;


        imageTitle.setText(itemImage.getCaptionTitle());
        imageDescription.setText(itemImage.getCaption());
        copyrights.setText(itemImage.getImageCopyrights());


        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);


        String imagePathSmall = PrefGeneral.getServiceURL(context) + "/api/v1/ItemImage/Image/nine_hundred_" + itemImage.getImageFilename() + ".jpg";



        Picasso.get()
                .load(imagePathSmall)
                .placeholder(drawable)
                .into(shopImageView);




        //            if(selectedItems.containsKey(taxiImage.getShopImageID()))
//            {
////                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
////                holder.listItem.animate().scaleXBy(-3);
////                holder.listItem.animate().scaleYBy(-3);
////                holder.listItem.animate().scaleY(-2);
//
//                holder.checkIcon.setVisibility(View.VISIBLE);
//
//            }
//            else
//            {
////                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
//
//                holder.checkIcon.setVisibility(View.INVISIBLE);
//            }

    }



    public void setItem(Item item)
    {
        this.item = item;

        imageTitle.setText(item.getItemName());
        copyrights.setText(item.getImageCopyrights());


        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);


        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Item/Image/nine_hundred_"
                + item.getItemImageURL() + ".jpg";


        Picasso.get()
                .load(imagePath)
                .placeholder(drawable)
                .into(shopImageView);
    }







    @OnClick(R.id.list_item)
    void listItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick)fragment).listItemClick(itemImage,getAdapterPosition());
        }
    }




    public interface ListItemClick
    {
        void listItemClick(ItemImage itemImage, int position);
        boolean listItemLongClick(View view, ItemImage itemImage, int position);
    }

}

