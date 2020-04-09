package org.nearbyshops.enduserappnew.adminModule.ChangeParent;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.API.ItemCategoryService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by sumeet on 19/12/15.
 */





public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{



    @Inject
    ItemCategoryService itemCategoryService;

    private List<ItemCategory> dataset;
    private Context context;
    private ItemCategoriesParent itemCategoriesParent;
    private Integer selectedPosition = null;
    private NotificationReceiver notificationReceiver;


    final String IMAGE_ENDPOINT_URL = "/api/Images";

    public Adapter(List<ItemCategory> dataset, Context context, ItemCategoriesParent activity
                            , NotificationReceiver notificationReceiver) {


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.itemCategoriesParent = activity;

        if(this.dataset == null)
        {
            this.dataset = new ArrayList<ItemCategory>();
        }

    }




    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);

        return new ViewHolder(v);
    }





    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, final int position) {

        holder.categoryName.setText(dataset.get(position).getCategoryName());
//        holder.categoryDescription.setText(dataset.get(position).getCategoryDescription());

        if(selectedPosition!=null)
        {
            if(selectedPosition==position){

                holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.gplus_color_2));

                notificationReceiver.notifyItemSelected();

            }
            else
            {
                holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.white));
            }

//            holder.itemCategoryListItem.animate().rotation(90);
        }else
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.white));

        }





        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                + dataset.get(position).getImagePath() + ".jpg";


        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Drawable compat = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);

        Picasso.get()
                .load(imagePath)
                .placeholder(compat)
                .into(holder.categoryImage);




    }


    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {



        @BindView(R.id.name) TextView categoryName;
        @BindView(R.id.itemCategoryListItem) ConstraintLayout itemCategoryListItem;
        @BindView(R.id.categoryImage) ImageView categoryImage;




        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }



        @OnLongClick(R.id.itemCategoryListItem)
        public boolean listItemLongClick()
        {

            int previousPosition = -1;



            if(selectedPosition!=null)
            {
                previousPosition = selectedPosition;
            }


//            showToastMessage("Long Click !");

            selectedPosition = getLayoutPosition();


//
            if(previousPosition!=selectedPosition)
            {

//                notifyItemChanged(previousPosition);
//                notifyItemChanged(selectedPosition);

                // item Selected
                notifyDataSetChanged();


            }
            else
            {
                selectedPosition = null;


                if(previousPosition!=-1)
                {
                    notifyItemChanged(previousPosition);
                }

            }

//            notifyDataSetChanged();



//            itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.cyan900));

            return true;
        }



        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {

            if (dataset == null) {

                return;
            }

            if(dataset.size()==0)
            {
                return;
            }



            selectedPosition = null;
            itemCategoriesParent.notifyRequestSubCategory(dataset.get(getLayoutPosition()));
        }




    }// ViewHolder Class declaration ends






    public interface requestSubCategory
    {
        // method for notifying the list object to request sub category
        void notifyRequestSubCategory(ItemCategory itemCategory);
    }




    public void clearSelection()
    {

        selectedPosition = null;
    }



    public ItemCategory getSelection()
    {
        if(selectedPosition!=null)
        {
            return dataset.get(selectedPosition);
        }
        else
        {
            return null;
        }
    }


    interface NotificationReceiver{

        void notifyItemSelected();
        void notifyItemDeleted();
    }

}