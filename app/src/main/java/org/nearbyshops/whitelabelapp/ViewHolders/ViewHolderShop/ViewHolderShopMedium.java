package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.R;

public class ViewHolderShopMedium extends RecyclerView.ViewHolder{



    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_address) TextView shopAddress;
    @BindView(R.id.shop_logo) ImageView shopLogo;
    @BindView(R.id.delivery) TextView delivery;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView rating_count;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
    @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;


    private Shop shop;
    private Context context;
    private Fragment fragment;





    public static ViewHolderShopMedium create(ViewGroup parent, Context context, Fragment fragment,
                                              RecyclerView.Adapter adapter)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop,parent,false);
        return new ViewHolderShopMedium(view,context,fragment);
    }






    public ViewHolderShopMedium(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
        this.fragment = fragment;


    }






    @OnClick(R.id.list_item_shop)
    void listItemClick()
    {
//        Intent shopHomeIntent = new Intent(context, ShopDashboard.class);
//        context.startActivity(shopHomeIntent);

        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemClick(shop,getAdapterPosition());
        }
    }





    public void setItem(Shop shop)
    {

        this.shop = shop;


        if(shop!=null)
        {

            shopName.setText(shop.getShopName());


            if(shop.getPickFromShopAvailable())
            {
                pickFromShopIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                pickFromShopIndicator.setVisibility(View.GONE);
            }



            if(shop.getHomeDeliveryAvailable())
            {
                homeDeliveryIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                homeDeliveryIndicator.setVisibility(View.GONE);
            }



            if(shop.getShopAddress()!=null)
            {
                shopAddress.setText(shop.getShopAddress() + ", " + shop.getCity()  + " - " + shop.getPincode());
            }

//                String imagePath = UtilityGeneral.getImageEndpointURL(MyApplicationCoreNew.getAppContext())
//                        + shop.getLogoImagePath();

            String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/Shop/Image/three_hundred_"
                    + shop.getLogoImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(shopLogo);




            String currency = "";
            currency = PrefCurrency.getCurrencySymbol(context);

            delivery.setText("Delivery : " + currency + " " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
            distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");


            if(shop.getRt_rating_count()==0)
            {
//                    holder.rating.setText("N/A");
                rating.setText(" New ");
                rating.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
                rating_count.setText("( Not Yet Rated )");
                rating_count.setVisibility(View.GONE);

            }
            else
            {
                rating_count.setVisibility(View.VISIBLE);
                rating.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));
                rating.setText(String.format("%.2f",shop.getRt_rating_avg()));
                rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
            }


            if(shop.getShortDescription()!=null)
            {
                description.setText(shop.getShortDescription());
            }

        }


    }









    //        @OnClick(R.id.shop_logo)
    void shopLogoClick()
    {
//            Intent intent = new Intent(context, MarketDetail.class);
//            intent.putExtra(MarketDetail.SHOP_DETAIL_INTENT_KEY,dataset.get(getLayoutPosition()));
//            context.startActivity(intent);
    }




    public interface ListItemClick
    {
        void listItemClick(Shop shop, int position);
    }

}
