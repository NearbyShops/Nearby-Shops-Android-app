package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

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

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.OrderItem;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderItem extends RecyclerView.ViewHolder{



    @BindView(R.id.itemImage) ImageView itemImage;
    @BindView(R.id.itemName) TextView itemName;
    @BindView(R.id.quantity) TextView quantity;
    @BindView(R.id.pincode) TextView itemPrice;
    @BindView(R.id.item_total) TextView itemTotal;
    @BindView(R.id.item_id) TextView itemID;


    private Context context;
    private OrderItem orderItem;
    private Fragment fragment;




    public static ViewHolderOrderItem create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_item_order_detail,parent,false);
        return new ViewHolderOrderItem(view,context,fragment);
    }





    public ViewHolderOrderItem(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }





    public void setItem(OrderItem orderItem)
    {

        this.orderItem = orderItem;

        Item item = orderItem.getItem();


        itemID.setText("Item ID : " + orderItem.getItemID());
        itemName.setText(item.getItemName());
        quantity.setText("Item Quantity : " + orderItem.getItemQuantity() + " "  + item.getQuantityUnit());
        itemPrice.setText("Item Price : " + PrefGeneral.getCurrencySymbol(context) + " " + orderItem.getItemPriceAtOrder() + " per "  + item.getQuantityUnit());
        itemTotal.setText("Item Total : " + PrefGeneral.getCurrencySymbol(context) + " " + UtilityFunctions.refinedStringWithDecimals(orderItem.getItemPriceAtOrder() * orderItem.getItemQuantity()));



        String imagePath = PrefGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/five_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(itemImage);

    }





    @OnClick(R.id.list_item)
    void listItemClick()
    {
        Item item = orderItem.getItem();
        ((ListItemClick)fragment).listItemClick(item,getAdapterPosition());
    }



    public interface ListItemClick
    {
        void listItemClick(Item item,int position);
    }

}

