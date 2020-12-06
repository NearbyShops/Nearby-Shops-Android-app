package org.nearbyshops.enduserappnew.EditDataScreens.EditItem;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.Model.ModelImages.ItemImage;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterItemImages extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<ItemImage> dataset;
    private Context context;
    private AppCompatActivity activity;
    private notificationsFromAdapter notificationReceiver;


//    final String IMAGE_ENDPOINT_URL = "/api/Images";


    public AdapterItemImages(List<ItemImage> dataset, Context context,
                             notificationsFromAdapter notificationReceiver) {


        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.activity = activity;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType==VIEW_TYPE_IMAGE)
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_image,parent,false);
            return new ViewHolderItemImage(v);
        }
        else if(viewType == VIEW_TYPE_ADD_IMAGE)
        {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_add_item,parent,false);
            return new ViewHolderAddItem(v);
        }


        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderGiven, final int position) {

        if(holderGiven instanceof ViewHolderItemImage)
        {
            ViewHolderItemImage holder = (ViewHolderItemImage) holderGiven;


            holder.copyrights.setText(dataset.get(position-1).getImageCopyrights());


            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ItemImage/Image/five_hundred_"
                    + dataset.get(position-1).getImageFilename() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.itemImage);
        }


    }

    public static final int VIEW_TYPE_ADD_IMAGE = 2;
    public static final int VIEW_TYPE_IMAGE = 1;

    @Override
    public int getItemViewType(int position) {

        if(position==0)
        {
            return VIEW_TYPE_ADD_IMAGE;
        }
        else
        {
            return VIEW_TYPE_IMAGE;
        }
    }

    @Override
    public int getItemCount() {

        return dataset.size()+1;
    }


    public class ViewHolderAddItem extends  RecyclerView.ViewHolder
    {
        @BindView(R.id.list_item) RelativeLayout listItem;

        public ViewHolderAddItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.list_item)
        void listItemClick()
        {
//            showToastMessage("Add Item Click!");
            notificationReceiver.addItemImage();
        }
    }







    public class ViewHolderItemImage extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {



        @BindView(R.id.list_item) RelativeLayout listItem;
        @BindView(R.id.item_image) ImageView itemImage;
        @BindView(R.id.copyright_info) TextView copyrights;


        public ViewHolderItemImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }



        @OnClick(R.id.list_item)
        public void itemCategoryListItemClick()
        {

        }



        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
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

//                    showToastMessage("Remove");
                    notificationReceiver.removeItemImage(dataset.get(getLayoutPosition()-1),getLayoutPosition()-1);

                    break;

                case R.id.action_edit:

//                    showToastMessage("Edit");
                    notificationReceiver.editItemImage(dataset.get(getLayoutPosition()-1),getLayoutPosition()-1);

                    break;


                default:
                    break;

            }

            return false;
        }



    }// ViewHolder Class declaration ends




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    public interface notificationsFromAdapter
    {
        // method for notifying the list object to request sub category
        void addItemImage();
        void editItemImage(ItemImage itemImage, int position);
        void removeItemImage(ItemImage itemImage, int position);
    }
}