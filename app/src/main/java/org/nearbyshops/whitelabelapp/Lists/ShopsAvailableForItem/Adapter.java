package org.nearbyshops.whitelabelapp.Lists.ShopsAvailableForItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import okhttp3.ResponseBody;

import org.nearbyshops.whitelabelapp.API.CartItemService;
import org.nearbyshops.whitelabelapp.API.CartStatsService;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.CartItem;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.CartStats;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.InputFilterMinMax;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by sumeet on 27/5/16.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{


    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;





    private Map<Integer, CartItem> cartItemMap = new HashMap<>();
    private Map<Integer, CartStats> cartStatsMap = new HashMap<>();


    private Item item;
    private List<ShopItem> dataset;
    private Context context;
    private NotifyFilledCart notifyFilledCart;




    public Adapter(List<ShopItem> dataset, Context context, Item item, NotifyFilledCart notifyFilledCart) {
        this.dataset = dataset;
        this.context = context;
        this.item = item;
        this.notifyFilledCart = notifyFilledCart;

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        //makeNetworkCall();
    }








    void getCartStats()
    {

        cartItemMap.clear();
        cartStatsMap.clear();

        User endUser = PrefLogin.getUser(context);

        if(endUser == null)
        {
            return;
        }



        Call<List<CartItem>> cartItemCall = cartItemService.getCartItemAvailabilityByItem(
                endUser.getUserID(),
                item.getItemID()
        );



        cartItemCall.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {


                if(response.body()!=null)
                {

                    for(CartItem cartItem: response.body())
                    {
                        cartItemMap.put(cartItem.getCart().getShopID(),cartItem);
                    }

                    notifyDataSetChanged();


                }else
                {
                    cartItemMap.clear();
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {

                Toast.makeText(context," Unsuccessful !",Toast.LENGTH_SHORT).show();
            }
        });






        Call<List<CartStats>> listCall = cartStatsService
                .getCartStatsList(endUser.getUserID(),null, null, false,
                        null,null
                );



        listCall.enqueue(new Callback<List<CartStats>>() {

            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {


                if(response.body()!=null)
                {

                    for(CartStats cartStats: response.body())
                    {
                        cartStatsMap.put(cartStats.getShopID(),cartStats);
                    }

                }else
                {
                    cartStatsMap.clear();

                }


                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {

                Toast.makeText(context," Network Request Unsuccessful !",Toast.LENGTH_SHORT).show();
            }
        });
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shop_item_layout,parent,false);

        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ShopItem shopItem = dataset.get(position);

        Shop shop = null;


        //if(cartItemMap.size()>0)
        //{
            CartItem cartItem = cartItemMap.get(dataset.get(position).getShopID());

            if(cartItem!=null)
            {
                holder.itemQuantity.setText(UtilityFunctions.refinedString(cartItem.getItemQuantity()));
                holder.shopItemListItem.setBackgroundResource(R.color.backgroundTinted);

                double total = shopItem.getItemPrice() * cartItem.getItemQuantity();

                holder.itemTotal.setText("Total : " + PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.2f", total));

                holder.addToCartText.setText("Update Cart");

            }else
            {

                holder.shopItemListItem.setBackgroundResource(R.color.shopItemColor);
                //holder.shopItemListItem.setBackgroundColor(22000000);
            }




        //if(cartStatsMap.size()>0)
        //{
            CartStats cartStats = cartStatsMap.get(dataset.get(position).getShopID());

            if(cartStats!=null)
            {
                holder.itemsInCart.setText(cartStats.getItemsInCart() + " " + "Items in Cart");
                holder.cartTotal.setText("Cart Total : "  + PrefCurrency.getCurrencySymbol(context) + " " + UtilityFunctions.refinedStringWithDecimals(cartStats.getCart_Total()));
            }
        //}




        if(shopItem!=null)
        {
            shop = shopItem.getShop();
        }


        if(shop!=null)
        {

            if(shop.getRt_rating_count()>0)
            {
                holder.rating.setText(String.format("%.1f",shop.getRt_rating_avg()));
            }
            else
            {
                holder.rating.setText(" - ");
//                holder.rating.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
//                holder.rating.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey);
            }



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





            holder.ratingCount.setText("( " + String.format("%.0f",shop.getRt_rating_count()) + " Ratings )");



//            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplicationCoreNew.getAppContext())
//                    + shop.getLogoImagePath();


            String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/Shop/Image/"
                    + "five_hundred_" + shop.getLogoImagePath() + ".jpg";

//            System.out.println(imagePath);



            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.shopImage);



            holder.shopName.setText(shop.getShopName());

            holder.distance.setText(String.format( "%.2f", shop.getRt_distance()) + " Km");
            holder.deliveryCharge.setText("Delivery : " + PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.0f", shop.getDeliveryCharges()) + " Per Order");

        }


        if(shopItem !=null)
        {
            holder.itemsAvailable.setText("Available : " + shopItem.getAvailableItemQuantity() + " " + item.getQuantityUnit());
            holder.itemPrice.setText( PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.2f", shopItem.getItemPrice()) + " per " + item.getQuantityUnit());

            if(shopItem.getAvailableItemQuantity()==0)
            {
                holder.outOfStock.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.outOfStock.setVisibility(View.GONE);
            }
        }
    }






    @Override
    public int getItemCount() {
        return dataset.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.out_of_stock_indicator)
        TextView outOfStock;

        @BindView(R.id.rating)
        TextView rating;

        @BindView(R.id.ratings_count)
        TextView ratingCount;

        @BindView(R.id.textAddToCart)
        TextView addToCartText;

        @BindView(R.id.distance)
        TextView distance;

        @BindView(R.id.deliveryCharge)
        TextView deliveryCharge;

        @BindView(R.id.shopName)
        TextView shopName;

        @BindView(R.id.itemsAvailable)
        TextView itemsAvailable;

        @BindView(R.id.itemPrice)
        TextView itemPrice;

        @BindView(R.id.itemTotal)
        TextView itemTotal;

        @BindView(R.id.reduceQuantity)
        ImageView reduceQuantity;

        @BindView(R.id.increaseQuantity)
        ImageView increaseQuantity;

        @BindView(R.id.itemQuantity)
        TextView itemQuantity;

        @BindView(R.id.itemsInCart)
        TextView itemsInCart;

        @BindView(R.id.cartTotal)
        TextView cartTotal;

        @BindView(R.id.addToCart)
        LinearLayout addToCart;

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        @BindView(R.id.shopImage)
        ImageView shopImage;

        @BindView(R.id.shopItem_list_item)
        LinearLayout shopItemListItem;

        @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
        @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;

        ShopItem shopItem;
        CartItem cartItem;
        CartStats cartStats;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

//                    setFilter();

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    shopItem = dataset.get(getLayoutPosition());
                    cartItem = cartItemMap.get(dataset.get(getLayoutPosition()).getShopID());

                    cartStats = cartStatsMap.get(dataset.get(getLayoutPosition()).getShopID());


            /*

            if(cartItem==null)
            {

                if(Integer.parseInt(itemQuantity.getText().toString())==0)
                {
                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");

                }else
                {
                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");
                }


            }else
            {
                if(Integer.parseInt(itemQuantity.getText().toString())==0)
                {
                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()-1) + " " + "Items in Cart");

                }else
                {
                    itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
                }
            }

            */



                    double total = 0;
                    int availableItems = shopItem.getAvailableItemQuantity();

                    if (!itemQuantity.getText().toString().equals(""))
                    {

                        try{

                            if(Double.parseDouble(itemQuantity.getText().toString())>availableItems)
                            {

                                return;
                            }

                            total = shopItem.getItemPrice() * Double.parseDouble(itemQuantity.getText().toString());


                            if(Double.parseDouble(itemQuantity.getText().toString())==0)
                            {
                                if(cartItem==null)
                                {

                                    itemsInCart.setText(cartStats.getItemsInCart() + " " + "Items in Cart");

                                }else
                                {
                                    itemsInCart.setText((cartStats.getItemsInCart() - 1) + " " + "Items in Cart");

                                    addToCartText.setText("Remove Item");

                                }

                            }else
                            {
                                if(cartItem==null)
                                {
                                    // no shop exist

                                    itemsInCart.setText((cartStats.getItemsInCart() + 1) + " " + "Items in Cart");

                                }else
                                {
                                    // shop Exist

                                    itemsInCart.setText(cartStats.getItemsInCart() + " " + "Items in Cart");

                                    addToCartText.setText("Update Cart");
                                }
                            }

                        }
                        catch (Exception ex)
                        {
                            //ex.printStackTrace();
                        }

                    }


                    itemTotal.setText("Total : " + PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
                    cartTotal.setText("Cart Total : " + PrefCurrency.getCurrencySymbol(context) + " "+ UtilityFunctions.refinedStringWithDecimals(cartTotalNeutral() + total));


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }


        void setFilter() {

            if (getLayoutPosition() != -1) {

                shopItem = dataset.get(getLayoutPosition());
            }

            if (shopItem != null) {
                int availableItems = shopItem.getAvailableItemQuantity();

                itemQuantity.setFilters(new InputFilter[]{new InputFilterMinMax("0", String.valueOf(availableItems))});
            }
        }




        @OnClick(R.id.shopImage)
        void shopLogoClick()
        {
            Shop shop = dataset.get(getLayoutPosition()).getShop();

            if(shop !=null)
            {
                notifyFilledCart.notifyShopLogoClick(shop);
            }
        }






        @OnClick(R.id.addToCart)
        void addToCartClick(View view) {


            User endUser = PrefLogin.getUser(context);
            if(endUser==null)
            {

                showLoginDialog();
                return;
            }



            CartItem cartItem = new CartItem();
            cartItem.setItemID(dataset.get(getLayoutPosition()).getItemID());

            if (!itemQuantity.getText().toString().equals("")) {

                cartItem.setItemQuantity(Double.parseDouble(itemQuantity.getText().toString()));
            }

            if (!cartItemMap.containsKey(dataset.get(getLayoutPosition()).getShopID()))
            {

                if (Double.parseDouble(itemQuantity.getText().toString()) == 0) {
                    showToastMessage("Please select quantity greater than Zero !");

                } else {

                    //showToastMessage("Add to cart! : " + dataset.get(getLayoutPosition()).getShopID());

//                    User endUser = PrefLogin.getUser(context);
//                    if(endUser==null)
//                    {
//
//                        showLoginDialog();
//                        return;
//                    }


                    Call<ResponseBody> call = cartItemService.createCartItem(
                            cartItem,
                            endUser.getUserID(),
                            dataset.get(getLayoutPosition()).getShopID()
                    );



                    progressBar.setVisibility(View.VISIBLE);
                    addToCart.setVisibility(View.INVISIBLE);


                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                            progressBar.setVisibility(View.INVISIBLE);
                            addToCart.setVisibility(View.VISIBLE);


                            if (response.code() == 201) {
                                Toast.makeText(context, "Add to cart successful !", Toast.LENGTH_SHORT).show();

                                getCartStats();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {


                            progressBar.setVisibility(View.INVISIBLE);
                            addToCart.setVisibility(View.VISIBLE);

                        }
                    });
                }


            } else {


                double quantity = Double.parseDouble(itemQuantity.getText().toString());

                if(quantity==0)
                {
                    // Delete from cart

                    //UtilityGeneral.getEndUserID(MyApplicationCoreNew.getAppContext())
//                    User endUser = PrefLogin.getUser(context);
//                    if(endUser==null)
//                    {
//                        showLoginDialog();
//                        return;
//                    }

                    Call<ResponseBody> callDelete = cartItemService.deleteCartItem(0,cartItem.getItemID(),
                            endUser.getUserID(),
                            dataset.get(getLayoutPosition()).getShopID()
                    );



                    progressBar.setVisibility(View.VISIBLE);
                    addToCart.setVisibility(View.INVISIBLE);


                    callDelete.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                            progressBar.setVisibility(View.INVISIBLE);
                            addToCart.setVisibility(View.VISIBLE);


                            if(response.code()==200)
                            {

                                showToastMessage("Item Removed !");

                                addToCartText.setText("Add to Cart");

                                //makeNetworkCall();

//                                notifyFilledCart.notifyCartDataChanged();
                                cartDataChanged();

                            }else
                            {
                                getCartStats();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {



                            progressBar.setVisibility(View.INVISIBLE);
                            addToCart.setVisibility(View.VISIBLE);
                        }
                    });


                }
                else
                {
                    // Update from cart

                    //UtilityGeneral.getEndUserID(MyApplicationCoreNew.getAppContext())
//                    User endUser = PrefLogin.getUser(context);
//
//                    if(endUser==null)
//                    {
//                        showLoginDialog();
//                        return;
//                    }

                    if(getLayoutPosition() < dataset.size())
                    {
                        ShopItem shop = dataset.get(getLayoutPosition());

                        Call<ResponseBody> callUpdate = cartItemService.updateCartItem(
                                cartItem,
                                endUser.getUserID(),
                                shop.getShopID()
                        );


                        progressBar.setVisibility(View.VISIBLE);
                        addToCart.setVisibility(View.INVISIBLE);

                        callUpdate.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                                if (response.code() == 200) {
                                    Toast.makeText(context, "Update cart successful !", Toast.LENGTH_SHORT).show();

                                }



                                progressBar.setVisibility(View.INVISIBLE);
                                addToCart.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {


                                progressBar.setVisibility(View.INVISIBLE);
                                addToCart.setVisibility(View.VISIBLE);
                            }
                        });

                    }

                }
            }
        }



        double cartTotalNeutral(){

            double previousTotal = 0;

            if(cartItem!=null && shopItem!=null)
            {
                previousTotal = shopItem.getItemPrice() * cartItem.getItemQuantity();
            }

            double cartTotalValue = 0;

            CartStats cartStats = cartStatsMap.get(dataset.get(getLayoutPosition()).getShopID());

            if(cartStats!=null)
            {
                cartTotalValue = cartStats.getCart_Total();
            }

            return (cartTotalValue - previousTotal);
        }





        @OnClick(R.id.reduceQuantity)
        void reduceQuantityClick(View view)
        {
            shopItem = dataset.get(getLayoutPosition());
            cartItem = cartItemMap.get(dataset.get(getLayoutPosition()).getShopID());
            cartStats = cartStatsMap.get(dataset.get(getLayoutPosition()).getShopID());



            double total = 0;


            if (!itemQuantity.getText().toString().equals("")){


                try{

                    if(Double.parseDouble(itemQuantity.getText().toString())<=0) {

                        if(cartStats!=null) {


                            if (cartItem == null) {

                                itemsInCart.setText(cartStats.getItemsInCart() + " " + "Items in Cart");

                            } else {
                                itemsInCart.setText((cartStats.getItemsInCart() - 1) + " " + "Items in Cart");

                            }

                        }




                        itemQuantity.setText("0");
                        return;
                    }



                    if((Double.parseDouble(itemQuantity.getText().toString()) - 1) < 0)
                    {
                        itemQuantity.setText("0");
                    }
                    else
                    {
                        itemQuantity.setText(UtilityFunctions.refinedString(Double.parseDouble(itemQuantity.getText().toString()) - 1));
                    }





                    total = shopItem.getItemPrice() * Double.parseDouble(itemQuantity.getText().toString());

                }
                catch (Exception ex)
                {

                }

                cartTotal.setText("Cart Total : " + PrefCurrency.getCurrencySymbol(context) + " " + UtilityFunctions.refinedStringWithDecimals(cartTotalNeutral() + total));
                itemTotal.setText("Total : " + PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.2f", total));

            }else
            {

                itemQuantity.setText(String.valueOf(0));
                itemTotal.setText("Total : " + PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
            }

        }




        @OnClick(R.id.increaseQuantity)
        void increaseQuantityClick(View view)
        {
            shopItem = dataset.get(getLayoutPosition());
            cartItem = cartItemMap.get(dataset.get(getLayoutPosition()).getShopID());
            cartStats = cartStatsMap.get(dataset.get(getLayoutPosition()).getShopID());


            int availableItems = shopItem.getAvailableItemQuantity();

            double total = 0;



                if (!itemQuantity.getText().toString().equals("")) {


                    if (cartStats != null) {

                        if (cartItem == null) {
                            if (Double.parseDouble(itemQuantity.getText().toString()) > 0) {
                                itemsInCart.setText((cartStats.getItemsInCart() + 1) + " " + "Items in Cart");
                            }

                        } else {

                            itemsInCart.setText(cartStats.getItemsInCart() + " " + "Items in Cart");
                        }


                    }





                try {

                    if (Double.parseDouble(itemQuantity.getText().toString()) >= availableItems) {
                        return;
                    }


                        itemQuantity.setText(UtilityFunctions.refinedString(Double.parseDouble(itemQuantity.getText().toString()) + 1));


                    total = shopItem.getItemPrice() * Double.parseDouble(itemQuantity.getText().toString());

                }catch (Exception ex)
                {

                }

                itemTotal.setText("Total : "  + PrefCurrency.getCurrencySymbol(context) + " " + String.format("%.2f", total));

                cartTotal.setText("Cart Total : " + PrefCurrency.getCurrencySymbol(context) + " " + UtilityFunctions.refinedStringWithDecimals(cartTotalNeutral() + total));


            }
            else
            {
                itemQuantity.setText(String.valueOf(0));
                itemTotal.setText("Total : " + PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
            }
        }

    }// View Holder Ends








    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    private void cartDataChanged()
    {
        getCartStats();
    }



    public interface NotifyFilledCart
    {
        void notifyCartDataChanged();
        void notifyShopLogoClick(Shop shop);
        void showLoginScreen();
    }




    private void showLoginDialog()
    {
        notifyFilledCart.showLoginScreen();
    }



}
