package org.nearbyshops.whitelabelapp.ViewHolders;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.ListItemClickGeneral;


public class ViewHolderItemCategorySmall extends RecyclerView.ViewHolder
{

    @BindView(R.id.name) TextView categoryName;
    @BindView(R.id.categoryImage) ImageView categoryImage;



    private Context context;
    private ItemCategory itemCategory;
    private Fragment fragment;

    private RecyclerView.Adapter adapter;





    public static ViewHolderItemCategorySmall create(ViewGroup parent, Context context, Fragment fragment,
                                                     RecyclerView.Adapter adapter)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category_small_card,parent,false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category_wide,parent,false);
        return new ViewHolderItemCategorySmall(view,context,fragment,adapter);
    }








    public ViewHolderItemCategorySmall(View itemView, Context context, Fragment fragment, RecyclerView.Adapter adapter
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
//            itemCategory.setRt_scroll_position(getLayoutPosition());
            ((ListItemClick) fragment).notifyRequestSubCategory(itemCategory,getLayoutPosition());
        }
        else if(fragment instanceof ListItemClickGeneral)
        {
            ((ListItemClickGeneral) fragment).notifyRequestSubCategory(itemCategory,getLayoutPosition());
        }
    }







    public void bindItemCategory(ItemCategory itemCategory)
    {
        this.itemCategory = itemCategory;

        categoryName.setText(String.valueOf(itemCategory.getCategoryName()));


        String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                + itemCategory.getImagePath() + ".jpg";

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(categoryImage);
    }







    public interface ListItemClick
    {
        void notifyRequestSubCategory(ItemCategory itemCategory,int scrollPosition);
    }



}// ViewHolderShopItemBackup Class declaration ends





