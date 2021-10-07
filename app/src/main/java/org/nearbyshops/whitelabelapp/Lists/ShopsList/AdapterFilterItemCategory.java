package org.nearbyshops.whitelabelapp.Lists.ShopsList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AdapterFilterItemCategory extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;

    private int itemCategoryID = -1;


    public static final int VIEW_TYPE_ITEM_CATEGORY_FILTER = 1;



    public AdapterFilterItemCategory(List<Object> dataset, Context context, Fragment fragment,int selectedCategoryID) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
        this.itemCategoryID=selectedCategoryID;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType== VIEW_TYPE_ITEM_CATEGORY_FILTER)
        {

//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category_small_filter,parent,false);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category_card_round,parent,false);
            return new ViewHolderShopCategory(view,context,fragment);
        }


        return null;
    }






    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

      if(holder instanceof ViewHolderShopCategory)
        {
            ((ViewHolderShopCategory) holder).setItem((ItemCategory) dataset.get(position));
        }

    }






    @Override
    public int getItemViewType(int position) {

        if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY_FILTER;
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





    public class ViewHolderShopCategory extends RecyclerView.ViewHolder {




        
//        @BindView(R.id.check_icon) ImageView checkIcon;
        @BindView(R.id.categoryImage) ImageView categoryImage;
        @BindView(R.id.name) TextView categoryName;



        private Context context;
        private ItemCategory itemCategory;
        private Fragment fragment;




        public ViewHolderShopCategory(@NonNull View itemView, Context context, Fragment fragment) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            this.context = context;
            this.fragment = fragment;
        }





        public void setItem(ItemCategory shop)
        {
            this.itemCategory = shop;

            categoryName.setText(itemCategory.getCategoryName());

            String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                    + itemCategory.getImagePath() + ".jpg";


            Picasso.get()
                    .load(imagePath)
                    .into(categoryImage);

            bindCheckIcon(false);
        }







        void bindCheckIcon(boolean notifyChange)
        {
            if(itemCategory.getItemCategoryID()== itemCategoryID)
            {
//                checkIcon.setVisibility(View.VISIBLE);
            }
            else
            {
//                checkIcon.setVisibility(View.INVISIBLE);
            }


            if(notifyChange)
            {
                notifyDataSetChanged();
            }
        }








        @OnClick({R.id.list_item,R.id.cardview})
        public void itemCategoryListItemClick()
        {

            if(itemCategory.getItemCategoryID()== itemCategoryID)
            {
                itemCategoryID =-1;
            }
            else
            {
                itemCategoryID = itemCategory.getItemCategoryID();
            }



            bindCheckIcon(true);


            if(fragment instanceof ListItemClick)
            {
                ((ListItemClick) fragment).listItemFilterShopClick(itemCategory.getItemCategoryID(),itemCategory,getLayoutPosition());
            }

        }



    }// ViewHolder Class declaration ends








    public interface ListItemClick
    {
        void listItemFilterShopClick(int itemCategoryID, ItemCategory itemCategory, int layoutPosition);
    }

}