package org.nearbyshops.enduserappnew.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelStats.ItemStats;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;


public class ViewHolderItem extends RecyclerView.ViewHolder {



    @BindView(R.id.itemName) TextView categoryName;

    @BindView(R.id.items_list_item) CardView itemCategoryListItem;
    @BindView(R.id.itemImage) ImageView categoryImage;
    @BindView(R.id.price_range) TextView priceRange;
    @BindView(R.id.price_average) TextView priceAverage;
    @BindView(R.id.shop_count) TextView shopCount;
    @BindView(R.id.item_rating) TextView itemRating;
    @BindView(R.id.rating_count) TextView ratingCount;
    @BindView(R.id.discount_indicator) TextView discountIndicator;


    public static int LAYOUT_TYPE_FULL_WIDTH = 1;
    public static int LAYOUT_TYPE_GRID = 2;



    private Context context;
    private Item item;
    private Fragment fragment;



    public static ViewHolderItem create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item,parent,false);
        return new ViewHolderItem(view,context,fragment);
    }





    public static ViewHolderItem create(ViewGroup parent, Context context, Fragment fragment, int layoutType)
    {
        View view = null;

        if(layoutType==LAYOUT_TYPE_FULL_WIDTH)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item,parent,false);
        }
        else if(layoutType==LAYOUT_TYPE_GRID)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_new,parent,false);
        }


        return new ViewHolderItem(view,context,fragment);
    }




    public ViewHolderItem(View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
        this.fragment = fragment;
    }





    @OnClick(R.id.items_list_item)
    public void listItemClick()
    {
//
//        Intent intent = new Intent(context, ShopItemByItem.class);
//
//        Gson gson = UtilityFunctions.provideGson();
//        String jsonString = gson.toJson(item);
//        intent.putExtra("item_json",jsonString);
//
//        context.startActivity(intent);

//
//        Intent intent = new Intent(context, ShopsAvailable.class);
//        intent.putExtra("item_id",item.getItemID());
//        context.startActivity(intent);

        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemClick(item,getLayoutPosition());
        }
    }





    public void setItem(Item item)
    {
        this.item = item;



        categoryName.setText(item.getItemName());

        ItemStats itemStats = item.getItemStats();

        if(itemStats!=null)
        {
            String currency = "";
            currency = PrefGeneral.getCurrencySymbol(context);


//            priceRange.setText("Price Range :\n" + currency + " " + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
            priceRange.setText(currency + " " + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
            priceAverage.setText("Price Average :\n" + currency + " " + String.format("%.2f",itemStats.getAvg_price()) + " per " + item.getQuantityUnit());
            shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
//            System.out.println("Rating : " + itemStats.getRating_avg() + " : Ratings Count " + itemStats.getRatingCount());




            if(item.getListPrice()>0.0 && item.getListPrice() > itemStats.getAvg_price())
            {

                double discountPercent = ((item.getListPrice() - itemStats.getAvg_price())/item.getListPrice())*100;

                discountIndicator.setText(String.format("%.0f ",discountPercent) + " %\nOff");
                discountIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                discountIndicator.setVisibility(View.GONE);
            }
        }






        if(item.getRt_rating_count()==0)
        {
            itemRating.setText(" New ");
            itemRating.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
            ratingCount.setVisibility(View.GONE);
        }
        else
        {


            itemRating.setText(String.format("%.2f",item.getRt_rating_avg()));
            ratingCount.setText("( " + (int) item.getRt_rating_count() + " Ratings )");

            itemRating.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));
            ratingCount.setVisibility(View.VISIBLE);

        }




        String imagePath = PrefGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/five_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());



        Picasso.get()
                .load(imagePath)
                .placeholder(drawable)
                .into(categoryImage);
    }






    public interface ListItemClick {

        void listItemClick(Item item, int position);
    }



}// ViewHolder Class declaration ends

