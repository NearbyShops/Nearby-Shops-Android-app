package org.nearbyshops.enduserappnew.Lists.CartsList.Backups;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Lists.CartItemList.CartItemList;
import org.nearbyshops.enduserappnew.Lists.CartItemList.CartItemListFragment;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import java.util.List;

/**
 * Created by sumeet on 5/6/16.
 */
public class AdapterBackup extends RecyclerView.Adapter<AdapterBackup.ViewHolder> {


    private List<CartStats> dataset = null;
    private Context context;


    public AdapterBackup(List<CartStats> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart,parent,false);

        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String imagePath = "http://example.com";

        Shop shop = dataset.get(position).getShop();

        holder.itemsInCart.setText(dataset.get(position).getItemsInCart() + " Items in Cart");
        holder.cartTotal.setText("Cart Total : " + PrefGeneral.getCurrencySymbol(context) + " " + dataset.get(position).getCart_Total());


        if(shop!=null)
        {
            holder.deliveryCharge.setText("Delivery " + PrefGeneral.getCurrencySymbol(context) + " " + shop.getDeliveryCharges() + " Per Order");
            holder.distance.setText(String.format( "%.2f", shop.getRt_distance()) + " Km");

            holder.shopName.setText(shop.getShopName());

            if(shop.getPickFromShopAvailable())
            {
                holder.pickFromShopIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.pickFromShopIndicator.setVisibility(View.GONE);
            }



            if(shop.getHomeDeliveryAvailable())
            {
                holder.homeDeliveryIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.homeDeliveryIndicator.setVisibility(View.GONE);
            }


//            imagePath = UtilityGeneral.getImageEndpointURL(MyApplicationCoreNew.getAppContext())
//                    + dataset.get(position).getShopDetails().getLogoImagePath();

//            imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
//                    + shop.getLogoImagePath() + ".jpg";

            imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                    + shop.getLogoImagePath() + ".jpg";

        }


        System.out.println(imagePath);

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());



        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(holder.shopImage);

    }



    @Override
    public int getItemCount() {

        return dataset.size();
    }







    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView shopImage;
        TextView shopName;
        TextView rating;
        TextView distance;
        TextView deliveryCharge;
        TextView itemsInCart;
        TextView cartTotal;
        LinearLayout cartsListItem;
        TextView pickFromShopIndicator;
        TextView homeDeliveryIndicator;



        public ViewHolder(View itemView) {
            super(itemView);

            shopImage = itemView.findViewById(R.id.shopImage);
            shopName = itemView.findViewById(R.id.shopName);
            rating = itemView.findViewById(R.id.rating);
            distance = itemView.findViewById(R.id.distance);
            deliveryCharge = itemView.findViewById(R.id.deliveryCharge);
            itemsInCart = itemView.findViewById(R.id.itemsInCart);
            cartTotal = itemView.findViewById(R.id.cartTotal);

            pickFromShopIndicator = itemView.findViewById(R.id.indicator_pick_from_shop);
            homeDeliveryIndicator = itemView.findViewById(R.id.indicator_home_delivery);


            cartsListItem = itemView.findViewById(R.id.carts_list_item);

            cartsListItem.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId())
            {

                case R.id.carts_list_item:

                    Intent intent = new Intent(context, CartItemList.class);

//                    intent.putExtra(CartItemListFragment.SHOP_INTENT_KEY,dataset.get(getLayoutPosition()).getShopDetails());
//                    intent.putExtra(CartItemListFragment.CART_STATS_INTENT_KEY,dataset.get(getLayoutPosition()));


                    String shopJson = UtilityFunctions.provideGson().toJson(dataset.get(getLayoutPosition()).getShop());
                    intent.putExtra(CartItemListFragment.SHOP_INTENT_KEY,shopJson);

                    String cartStatsJson = UtilityFunctions.provideGson().toJson(dataset.get(getLayoutPosition()));
                    intent.putExtra(CartItemListFragment.CART_STATS_INTENT_KEY,cartStatsJson);


                    context.startActivity(intent);

                    break;

                default:

                    break;
            }
        }



    }

}
