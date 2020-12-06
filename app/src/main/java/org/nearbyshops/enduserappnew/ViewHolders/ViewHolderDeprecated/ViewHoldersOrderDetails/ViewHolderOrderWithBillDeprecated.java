package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderDeprecated.ViewHoldersOrderDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DetailScreens.DetailShop.ShopDetail;
import org.nearbyshops.enduserappnew.DetailScreens.DetailShop.ShopDetailFragment;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderWithBillDeprecated extends RecyclerView.ViewHolder{



    @BindView(R.id.order_id) TextView orderID;
    @BindView(R.id.dateTimePlaced) TextView dateTimePlaced;
    @BindView(R.id.deliveryAddressName) TextView deliveryAddressName;
    @BindView(R.id.deliveryAddress) TextView deliveryAddress;
    @BindView(R.id.deliveryAddressPhone) TextView deliveryAddressPhone;
    @BindView(R.id.numberOfItems) TextView numberOfItems;
    @BindView(R.id.orderTotal) TextView orderTotal;
    @BindView(R.id.currentStatus) TextView currentStatus;
    @BindView(R.id.is_pick_from_shop) TextView isPickFromShop;

    @BindView(R.id.delivery_type) TextView deliveryTypeDescription;

    @BindView(R.id.item_total_value) TextView itemTotal;
    @BindView(R.id.delivery_charge_value) TextView deliveryCharge;
    @BindView(R.id.app_service_charge_value) TextView appServiceCharge;
    @BindView(R.id.net_payable_value) TextView netPayable;



    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_address) TextView shopAddress;
    @BindView(R.id.shop_logo) ImageView shopLogo;
    @BindView(R.id.delivery) TextView delivery;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView rating_count;
    @BindView(R.id.description) TextView description;
//        @BindView(R.id.list_item) ConstraintLayout list_item;


    @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
    @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;





    private Context context;
    private Order order;
    private Fragment fragment;




    public static ViewHolderOrderWithBillDeprecated create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_order_detail,parent,false);
        return new ViewHolderOrderWithBillDeprecated(view,context,fragment);
    }





    public ViewHolderOrderWithBillDeprecated(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }



    @OnClick(R.id.list_item_shop)
    void shopDetailsClick()
    {

        Shop shop = order.getShop();
        Intent intent = new Intent(context, ShopDetail.class);

        String shopJson = UtilityFunctions.provideGson().toJson(shop);
        intent.putExtra(ShopDetailFragment.TAG_JSON_STRING,shopJson);

        context.startActivity(intent);
    }






    public void setItem(Order order)
    {
        this.order = order;


        DeliveryAddress deliveryAddressLocal = order.getDeliveryAddress();
        Shop shop = order.getShop();

        orderID.setText("Order ID : " + order.getOrderID());
        dateTimePlaced.setText("" + order.getDateTimePlaced().toLocaleString());

        deliveryAddressName.setText(deliveryAddressLocal.getName());

        deliveryAddress.setText(deliveryAddressLocal.getDeliveryAddress() + ",\n"
                + deliveryAddressLocal.getCity() + " - " + deliveryAddressLocal.getPincode());

        deliveryAddressPhone.setText("Phone : " + deliveryAddressLocal.getPhoneNumber());

        numberOfItems.setText(order.getItemCount() + " Items");
        orderTotal.setText("| Total : " + PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getNetPayable()));


        String status = "";



        if(order.isPickFromShop())
        {
            status = OrderStatusPickFromShop.getStatusString(order.getStatusPickFromShop());

            isPickFromShop.setBackgroundColor(ContextCompat.getColor(context, R.color.orangeDark));
            isPickFromShop.setText("Pick from Shop");

            deliveryTypeDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.orangeDark));
            deliveryTypeDescription.setText(context.getString(R.string.delivery_type_description_pick_from_shop));
        }
        else
        {
            status = OrderStatusHomeDelivery.getStatusString(order.getStatusHomeDelivery());

            isPickFromShop.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
            isPickFromShop.setText("Home Delivery");

            deliveryTypeDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
            deliveryTypeDescription.setText(context.getString(R.string.delivery_type_description_home_delivery));
        }




        currentStatus.setText("Current Status : " + status);





        // bind billing details

        itemTotal.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getItemTotal()));
        deliveryCharge.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getDeliveryCharges()));
        appServiceCharge.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f", order.getAppServiceCharge()));
        netPayable.setText(PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f",order.getNetPayable()));




        if(shop!=null)
        {
            shopName.setText(shop.getShopName());

            if(shop.getShopAddress()!=null)
            {
                shopAddress.setText(shop.getShopAddress() + ", " +  shop.getCity() +" - " + shop.getPincode());
            }

//                String imagePath = UtilityGeneral.getImageEndpointURL(MyApplicationCoreNew.getAppContext())
//                        + shop.getLogoImagePath();

            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                    + shop.getLogoImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(shopLogo);

            delivery.setText("Delivery : " + PrefGeneral.getCurrencySymbol(context) + " " +  String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
            distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");



            if(shop.getRt_rating_count()==0)
            {
                rating.setText("N/A");
                rating_count.setText(" - ");

            }
            else
            {
                rating.setText(String.format("%.2f",shop.getRt_rating_avg()));
                rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
            }


            if(shop.getShortDescription()!=null)
            {
                description.setText(shop.getShortDescription());
            }


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

        }


    }





    @OnClick(R.id.list_item)
    void listItemClick()
    {
        ((ListItemClick)fragment).listItemClick(order,getAdapterPosition());
    }




    public interface ListItemClick
    {
        void listItemClick(Order order, int position);
        boolean listItemLongClick(View view, Order order, int position);
    }
}

