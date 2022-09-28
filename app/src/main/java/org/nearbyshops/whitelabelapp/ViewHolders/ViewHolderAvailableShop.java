package org.nearbyshops.whitelabelapp.ViewHolders;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.CartItem;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewHolderAvailableShop extends RecyclerView.ViewHolder {




    @BindView(R.id.shop_image) ImageView shopImage;
    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_count) TextView shopCount;
    @BindView(R.id.item_price) TextView itemPrice;



    @BindView(R.id.item_quantity_indicator) TextView itemQuantityIndicator;
    @BindView(R.id.add_button) ImageView addLabel;

    @BindView(R.id.out_of_stock_indicator) TextView outOfStockIndicator;



    private Map<Integer, CartItem> cartItemMap;




    private Context context;
    private ShopItem shopItem;
    private Fragment fragment;


    public static ViewHolderAvailableShop create(  Map<Integer, CartItem> cartItemMap, ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_available_item, parent, false);
        return new ViewHolderAvailableShop(cartItemMap, view, context, fragment);
    }





    public ViewHolderAvailableShop(  Map<Integer, CartItem> cartItemMap, @NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
        this.fragment = fragment;
        this.cartItemMap = cartItemMap;
    }








    public void setItem(ShopItem shopItem) {
        this.shopItem = shopItem;


        Shop shop = shopItem.getShop();

        shopName.setText(shop.getShopName());
        itemPrice.setText(PrefCurrency.getCurrencySymbol(context) + " " + UtilityFunctions.refinedStringWithDecimals(shopItem.getItemPrice()));
        shopCount.setText("Available : " + shopItem.getAvailableItemQuantity());


        String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/Shop/Image/"
                + "five_hundred_" + shop.getLogoImagePath() + ".jpg";



        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(shopImage);





        if(shopItem.getAvailableItemQuantity()==0)
        {
            outOfStockIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            outOfStockIndicator.setVisibility(View.GONE);
        }



        CartItem cartItem = cartItemMap.get(shopItem.getShopID());




        if(cartItem!=null)
        {
            itemQuantityIndicator.setText(UtilityFunctions.refinedString(cartItem.getItemQuantity()));
        }
        else
        {

            itemQuantityIndicator.setText("0");
        }



    }






    @OnClick(R.id.list_item)
    void listItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemClick(shopItem,getLayoutPosition());
        }
    }



    public interface ListItemClick {

        void listItemClick(ShopItem item, int position);
    }




}
