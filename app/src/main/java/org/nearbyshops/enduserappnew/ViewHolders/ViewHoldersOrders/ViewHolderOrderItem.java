package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

import android.content.Context;
import android.graphics.Paint;
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

import static org.nearbyshops.enduserappnew.Utility.UtilityFunctions.refinedString;


public class ViewHolderOrderItem extends RecyclerView.ViewHolder{



    @BindView(R.id.itemImage) ImageView itemImage;
    @BindView(R.id.itemName) TextView itemName;
    @BindView(R.id.quantity) TextView quantity;
    @BindView(R.id.item_price) TextView itemPrice;
    @BindView(R.id.item_total) TextView itemTotal;
    @BindView(R.id.item_id) TextView itemID;


    @BindView(R.id.discount_indicator) TextView discountIndicator;
    @BindView(R.id.list_price) TextView listPrice;


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
        itemPrice.setText("Price : " + PrefGeneral.getCurrencySymbol(context) + " " + orderItem.getItemPriceAtOrder() + " per "  + item.getQuantityUnit());
        itemTotal.setText("Item Total : " + PrefGeneral.getCurrencySymbol(context) + " " + UtilityFunctions.refinedStringWithDecimals(orderItem.getItemPriceAtOrder() * orderItem.getItemQuantity()));



        String currency = "";
        currency = PrefGeneral.getCurrencySymbol(context);



        if(orderItem.getListPriceAtOrder()>0.0 && orderItem.getListPriceAtOrder() > orderItem.getItemPriceAtOrder())
        {
            listPrice.setText(currency + " " + refinedString(orderItem.getListPriceAtOrder()));
            listPrice.setPaintFlags(listPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            listPrice.setVisibility(View.VISIBLE);


            double discountPercent = ((orderItem.getListPriceAtOrder() - orderItem.getItemPriceAtOrder())/orderItem.getListPriceAtOrder())*100;
            discountIndicator.setText(String.format("%.0f ",discountPercent) + " %\nOff");


            discountIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            discountIndicator.setVisibility(View.GONE);
            listPrice.setVisibility(View.GONE);
        }


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

