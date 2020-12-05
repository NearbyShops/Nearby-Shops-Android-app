package org.nearbyshops.enduserappnew.DetailScreens.DetailShopItem;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderShopItemDetail extends RecyclerView.ViewHolder{




    @BindView(R.id.item_name) TextView itemName;

    @BindView(R.id.item_rating_numeric) TextView itemRatingNumeric;
    @BindView(R.id.item_rating) RatingBar itemRating;
    @BindView(R.id.rating_count) TextView ratingCount;


    @BindView(R.id.item_description) TextView itemDescription;
    @BindView(R.id.read_full_button) TextView readFullButton;

    @BindView(R.id.discount_indicator) TextView discountIndicator;
    @BindView(R.id.list_price) TextView listPrice;
    @BindView(R.id.item_price) TextView itemPrice;


    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.available_quantity) TextView availableQuantity;


    @BindView(R.id.out_of_stock_indicator) TextView outOfStockIndicator;




    private Context context;
    private Item item;
    private Fragment fragment;


    public static ViewHolderShopItemDetail create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_item_detail, parent, false);
        return new ViewHolderShopItemDetail(view, context, fragment);
    }





    public ViewHolderShopItemDetail(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


    }







    public void setItem(Item item) {
        this.item = item;


        itemName.setText(this.item.getItemName());

//        itemRatingNumeric.setText(String.format("%.2f",item.getRt_rating_avg()));
//        itemRating.setRating(item.getRt_rating_avg());
//        ratingCount.setText("(" + item.getRt_rating_count() + " ratings )");

        itemDescription.setText(this.item.getItemDescriptionLong());


        if(item.getItemDescriptionLong()==null || item.getItemDescriptionLong().equals(""))
        {
            readFullButton.setVisibility(View.GONE);
        }



        if(this.item.getRt_rating_count()==0)
        {
            itemRatingNumeric.setText(" New ");
            itemRatingNumeric.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));

            ratingCount.setVisibility(View.GONE);
            itemRating.setVisibility(View.GONE);
        }
        else
        {
            itemRatingNumeric.setText(String.format("%.2f", this.item.getRt_rating_avg()));
            itemRatingNumeric.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));

            ratingCount.setText("( " + (int) this.item.getRt_rating_count() + " Ratings )");

            ratingCount.setVisibility(View.VISIBLE);
            itemRating.setVisibility(View.VISIBLE);
        }


        ShopItem shopItem = item.getShopItem();

        availableQuantity.setText("Available : " + String.valueOf(shopItem.getAvailableItemQuantity()) + " " + item.getQuantityUnit());


        itemPrice.setText(PrefGeneral.getCurrencySymbol(context)
                + " " + UtilityFunctions.refinedString(shopItem.getItemPrice()) + " / " + item.getQuantityUnit());



        if(item.getListPrice()>0.0 && item.getListPrice()>shopItem.getItemPrice())
        {
            listPrice.setText(PrefGeneral.getCurrencySymbol(context) + " " + UtilityFunctions.refinedString(item.getListPrice()));
            listPrice.setPaintFlags(listPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            listPrice.setVisibility(View.VISIBLE);


            double discountPercent = ((item.getListPrice() - shopItem.getItemPrice())/item.getListPrice())*100;
            discountIndicator.setText(String.format("%.0f ",discountPercent) + " %\nOff");

            discountIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            discountIndicator.setVisibility(View.GONE);
            listPrice.setVisibility(View.GONE);
        }



        if(shopItem.getAvailableItemQuantity()==0)
        {
            outOfStockIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            outOfStockIndicator.setVisibility(View.GONE);
        }




        Shop shop  = shopItem.getShop();

        shopName.setText(shop.getShopName());

    }




    @OnClick(R.id.read_full_button)
    void readFullButtonClick() {

        itemDescription.setMaxLines(Integer.MAX_VALUE);
        readFullButton.setVisibility(View.GONE);
    }




}
