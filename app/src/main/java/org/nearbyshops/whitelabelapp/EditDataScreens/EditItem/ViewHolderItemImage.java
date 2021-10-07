package org.nearbyshops.whitelabelapp.EditDataScreens.EditItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.Model.ModelImages.ItemImage;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class ViewHolderItemImage extends RecyclerView.ViewHolder implements android.widget.PopupMenu.OnMenuItemClickListener {



    @BindView(R.id.list_item) RelativeLayout listItem;
    @BindView(R.id.item_image) ImageView itemImageView;
    @BindView(R.id.copyright_info) TextView copyrights;



    private Context context;
    private ItemImage itemImage;
    private Fragment fragment;



    public ViewHolderItemImage(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }




    public static ViewHolderItemImage create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_image,parent,false);
        return new ViewHolderItemImage(view,context,fragment);
    }





    public void setItem(ItemImage item)
    {
        this.itemImage = item;

        copyrights.setText(item.getImageCopyrights());

        String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/ItemImage/Image/five_hundred_"
                + item.getImageFilename() + ".jpg";


        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(itemImageView);
    }






    @OnClick(R.id.list_item)
    public void itemCategoryListItemClick()
    {

    }



    @OnClick(R.id.more_options)
    void optionsOverflowClick(View v)
    {
        android.widget.PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.item_image_item_overflow, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }






    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.action_remove:


                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick) fragment).removeItemImage(itemImage,getAdapterPosition());
                }


                break;

            case R.id.action_edit:

                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick) fragment).editItemImage(itemImage,getAdapterPosition());
                }


                break;


            default:
                break;

        }

        return false;
    }




    public interface ListItemClick
    {
        // method for notifying the list object to request sub category
        void editItemImage(ItemImage itemImage, int position);
        void removeItemImage(ItemImage itemImage, int position);
    }


}// ViewHolder Class declaration ends

