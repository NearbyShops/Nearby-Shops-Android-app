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

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.EditDataScreens.EditItemCategory.EditItemCategory;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItemCategory.EditItemCategoryFragment;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItemCategory.PrefItemCategory;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class ViewHolderItemCategoryAdmin extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {




    @BindView(R.id.name) TextView categoryName;
    @BindView(R.id.itemCategoryListItem) ConstraintLayout itemCategoryListItem;
    @BindView(R.id.categoryImage) ImageView categoryImage;
    @BindView(R.id.cardview) CardView cardView;
    @BindView(R.id.more_options) ImageView moreOptions;




    private Context context;
    private ItemCategory itemCategory;
    private Fragment fragment;

    private RecyclerView.Adapter adapter;




    private Map<Integer, ItemCategory> selectedItemCategories;
    private Map<Integer, Item> selectedItems;





    public static ViewHolderItemCategoryAdmin create(ViewGroup parent, Context context, Fragment fragment,
                                        RecyclerView.Adapter adapter,
                                        Map<Integer, Item> selectedItems,
                                        Map<Integer, ItemCategory> selectedItemCategories

    )
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);
        return new ViewHolderItemCategoryAdmin(view,context,fragment,adapter,selectedItems,selectedItemCategories);
    }





    public static ViewHolderItemCategoryAdmin createSmall(ViewGroup parent, Context context, Fragment fragment,
                                                     RecyclerView.Adapter adapter,
                                                     Map<Integer, Item> selectedItems,
                                                     Map<Integer, ItemCategory> selectedItemCategories

    )
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category_small,parent,false);
        return new ViewHolderItemCategoryAdmin(view,context,fragment,adapter,selectedItems,selectedItemCategories);
    }








    public ViewHolderItemCategoryAdmin(View itemView,Context context, Fragment fragment, RecyclerView.Adapter adapter,
                                       Map<Integer, Item> selectedItems,
                                       Map<Integer, ItemCategory> selectedItemCategories) {
        super(itemView);
        ButterKnife.bind(this,itemView);


        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
        this.selectedItems = selectedItems;
        this.selectedItemCategories = selectedItemCategories;
    }






    public void setItem(ItemCategory itemCategory)
    {

        this.itemCategory = itemCategory;


        categoryName.setText(String.valueOf(itemCategory.getCategoryName()));
        moreOptions.setVisibility(View.VISIBLE);


        if(selectedItemCategories.containsKey(itemCategory.getItemCategoryID()))
        {
            //context.getResources().getColor(R.color.gplus_color_2)
            itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));
        }
        else
        {
            //context.getResources().getColor(R.color.white)
            itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }



        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                + itemCategory.getImagePath() + ".jpg";

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(categoryImage);


    }






    @OnClick(R.id.itemCategoryListItem)
    public void itemCategoryListItemClick()
    {

        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyRequestSubCategory(itemCategory);
        }


        selectedItemCategories.clear();
        selectedItems.clear();
    }



    @OnLongClick(R.id.itemCategoryListItem)
    boolean listItemLongClick()
    {


        if(selectedItemCategories.containsKey(
                itemCategory.getItemCategoryID()
        ))
        {
            selectedItemCategories.remove(itemCategory.getItemCategoryID());

        }else
        {

            selectedItemCategories.put(itemCategory.getItemCategoryID(),itemCategory);


        }


        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyItemCategorySelected();
        }

        adapter.notifyItemChanged(getLayoutPosition());

        return true;
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
    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.action_remove:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirm Delete Item Category !")
                        .setMessage("Do you want to delete this Item Category ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(fragment instanceof ListItemClick)
                                {
                                    ((ListItemClick) fragment).notifyDeleteItemCat(
                                            itemCategory,getLayoutPosition()
                                    );
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

                    PrefItemCategory.saveItemCategory(itemCategory,context);

                    Intent intent = new Intent(context, EditItemCategory.class);
                    intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY,EditItemCategoryFragment.MODE_UPDATE);
                    context.startActivity(intent);


                break;



//                case R.id.action_detach:
//
////                    showToastMessage("Detach");
//
//                    if(dataset.get(getLayoutPosition()) instanceof ItemCategory)
//                    {
//                        notificationReceiver.detachItemCat((ItemCategory) dataset.get(getLayoutPosition()));
//                    }
//
//                break;



            case R.id.action_change_parent:


                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick) fragment).changeParentItemCat(itemCategory);
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
        // method for notifying the list object to request sub category
        void notifyRequestSubCategory(ItemCategory itemCategory);
        void notifyItemCategorySelected();

        void detachItemCat(ItemCategory itemCategory);
        void notifyDeleteItemCat(ItemCategory itemCategory, int position);
        void changeParentItemCat(ItemCategory itemCategory);
    }



}
