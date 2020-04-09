package org.nearbyshops.enduserappnew.adminModule.ItemsDatabaseForAdmin.ViewHolders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.EditDataScreens.EditItem.EditItem;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItem.EditItemFragmentNew;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItem.PrefItem;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelStats.ItemStats;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;


import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderItemAdmin extends RecyclerView.ViewHolder  implements PopupMenu.OnMenuItemClickListener{



    @BindView(R.id.itemName) TextView categoryName;

    @BindView(R.id.items_list_item) CardView itemCategoryListItem;
    @BindView(R.id.itemImage) ImageView categoryImage;
    @BindView(R.id.price_range) TextView priceRange;
    @BindView(R.id.price_average) TextView priceAverage;
    @BindView(R.id.shop_count) TextView shopCount;
    @BindView(R.id.item_rating) TextView itemRating;
    @BindView(R.id.rating_count) TextView ratingCount;




    private Context context;
    private Item item;
    private Fragment fragment;

    private RecyclerView.Adapter adapter;



    private Map<Integer, Item> selectedItems;







    public static ViewHolderItemAdmin create(ViewGroup parent, Context context, Fragment fragment,
                                             RecyclerView.Adapter adapter,
                                             Map<Integer, Item> selectedItems

    )
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_for_admin,parent,false);
        return new ViewHolderItemAdmin(view,context,fragment,adapter,selectedItems);
    }








    public ViewHolderItemAdmin(@NonNull View itemView, Context context, Fragment fragment, RecyclerView.Adapter adapter,
                               Map<Integer, Item> selectedItems) {
        super(itemView);

        ButterKnife.bind(this,itemView);

        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
        this.selectedItems = selectedItems;
    }






    public void setItem(Item item) {

        this.item = item;

        categoryName.setText(item.getItemName());

        ItemStats itemStats = item.getItemStats();

        priceRange.setText("Price Range :\n " + PrefGeneral.getCurrencySymbol(context) + String.format(" %.2f",itemStats.getMin_price()) + " - " + String.format("%.2f",itemStats.getMax_price()) + " per " + item.getQuantityUnit());
        priceAverage.setText("Price Average :\n " + PrefGeneral.getCurrencySymbol(context) + String.format(" %.2f",itemStats.getAvg_price()) + " per " + item.getQuantityUnit());
        shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
        itemRating.setText(String.format("%.2f",itemStats.getRating_avg()));
        ratingCount.setText("( " + itemStats.getRatingCount() + " Ratings )");


        if(selectedItems.containsKey(item.getItemID()))
        {
            //context.getResources().getColor(R.color.gplus_color_2)
            itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
        }
        else
        {
            //context.getResources().getColor(R.color.white)
            itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }



        String imagePath = PrefGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.get()
                .load(imagePath)
                .placeholder(drawable)
                .into(categoryImage);
    }






    @OnClick(R.id.items_list_item)
    public void listItemClick()
    {




        if(selectedItems.containsKey(
                item.getItemID()
        ))
        {
            selectedItems.remove(item.getItemID());
        }
        else
        {

            selectedItems.put(item.getItemID(),item);

        }



        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyItemSelected();
        }


        adapter.notifyItemChanged(getLayoutPosition());
    }


    @OnClick(R.id.more_options)
    void optionsOverflowClick(View v)
    {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.item_category_item_overflow, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.action_remove:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirm Delete Item !")
                        .setMessage("Do you want to delete this Item ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(fragment instanceof ListItemClick)
                                {
                                    ((ListItemClick) fragment).notifyDeleteItem(item,getAdapterPosition());
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                showToastMessage("Cancelled !");
                            }
                        })
                        .show();


                break;

            case R.id.action_edit:

                    PrefItem.saveItem(this.item, context);

                    Intent intentEdit = new Intent(context, EditItem.class);
                    intentEdit.putExtra(EditItemFragmentNew.EDIT_MODE_INTENT_KEY, EditItemFragmentNew.MODE_UPDATE);
                    context.startActivity(intentEdit);

                break;


//                case R.id.action_detach:
//
//                    if (dataset.get(getLayoutPosition()) instanceof Item) {
//                        notificationReceiver.detachItem((Item) dataset.get(getLayoutPosition()));
//                    }
//                    break;


            case R.id.action_change_parent:



                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick) fragment).changeParentItem(this.item);
                }


                break;

            default:
                break;

        }

        return false;
    }







    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }



    public interface ListItemClick
    {
        void notifyItemSelected();

        void detachItem(Item item);
        void notifyDeleteItem(Item item, int position);
        void changeParentItem(Item item);
    }

}
