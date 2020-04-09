package org.nearbyshops.enduserappnew.ViewHolders;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class ViewHolderCart extends RecyclerView.ViewHolder implements View.OnClickListener {



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


    private Context context;
    private CartStats cartStats;
    private Fragment fragment;


    public static ViewHolderCart create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
        return new ViewHolderCart(view, context, fragment);
    }


    public ViewHolderCart(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);

        this.context = context;
        this.fragment = fragment;



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







    public void setItem(CartStats cartStats) {
        this.cartStats = cartStats;


        String imagePath = "http://example.com";

        Shop shop = cartStats.getShop();

        itemsInCart.setText(cartStats.getItemsInCart() + " Items in Cart");
        cartTotal.setText("Cart Total : " + PrefGeneral.getCurrencySymbol(context) + " " + cartStats.getCart_Total());


        if (shop != null) {


            deliveryCharge.setText("Delivery " + PrefGeneral.getCurrencySymbol(context) + " " + shop.getDeliveryCharges() + " Per Order");
            distance.setText(String.format("%.2f", shop.getRt_distance()) + " Km");

            shopName.setText(shop.getShopName());


            if (shop.getPickFromShopAvailable()) {
                pickFromShopIndicator.setVisibility(View.VISIBLE);
            } else {
                pickFromShopIndicator.setVisibility(View.GONE);
            }



            if (shop.getHomeDeliveryAvailable()) {
                homeDeliveryIndicator.setVisibility(View.VISIBLE);
            } else {
                homeDeliveryIndicator.setVisibility(View.GONE);
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
                .into(shopImage);

    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.carts_list_item:

                Intent intent = new Intent(context, CartItemList.class);

//                    intent.putExtra(CartItemListFragment.SHOP_INTENT_KEY,dataset.get(getLayoutPosition()).getShopDetails());
//                    intent.putExtra(CartItemListFragment.CART_STATS_INTENT_KEY,dataset.get(getLayoutPosition()));


                String shopJson = UtilityFunctions.provideGson().toJson(cartStats.getShop());
                intent.putExtra(CartItemListFragment.SHOP_INTENT_KEY,shopJson);


                String cartStatsJson = UtilityFunctions.provideGson().toJson(cartStats);
                intent.putExtra(CartItemListFragment.CART_STATS_INTENT_KEY,cartStatsJson);


                context.startActivity(intent);

                break;

            default:

                break;
        }
    }





}
