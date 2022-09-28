package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShopItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import okhttp3.ResponseBody;
import org.nearbyshops.whitelabelapp.API.CartItemService;
import org.nearbyshops.whitelabelapp.API.CartStatsService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.CartItem;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.CartStats;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import javax.inject.Inject;
import java.util.Map;


public class ViewHolderShopItemButton extends RecyclerView.ViewHolder{


    private Map<Integer, CartItem> cartItemMap;
//    private CartStats cartStatsMap;




    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;




    @BindView(R.id.add_to_cart_text) TextView addToCartText;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.item_title) TextView itemName;
    @BindView(R.id.item_image) ImageView itemImage;
    @BindView(R.id.item_price) TextView itemPrice;

    @BindView(R.id.available) TextView available;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView ratinCount;

    @BindView(R.id.increaseQuantity) ImageView increaseQuantity;
    @BindView(R.id.itemQuantity) EditText itemQuantity;
    @BindView(R.id.reduceQuantity) ImageView reduceQuantity;
    @BindView(R.id.total) TextView itemTotal;

//        @BindView(R.id.add_to_cart_text)
//        TextView addToCart;

    @BindView(R.id.out_of_stock_indicator) TextView outOfStockIndicator;
    @BindView(R.id.list_item) CardView shopItemListItem;



    private ShopItem shopItem;
    private CartItem cartItem;
    private CartStats cartStats;

    private Context context;
    private Fragment fragment;

    private RecyclerView.Adapter listAdapter;



    private int itemsInCart;
    private double cartTotal;




    public static ViewHolderShopItemButton create(ViewGroup parent, Context context, Fragment fragment, RecyclerView.Adapter adapter,
                                                  Map<Integer, CartItem> cartItemMap, CartStats cartStatsMap)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_item_by_shop,parent,false);
        return new ViewHolderShopItemButton(view,context,fragment,adapter,cartItemMap,cartStatsMap);
    }









    public ViewHolderShopItemButton(@NonNull View itemView, Context context, Fragment fragment, RecyclerView.Adapter listAdapter,
                                    Map<Integer, CartItem> cartItemMap, CartStats cartStatsMap) {

        super(itemView);
        ButterKnife.bind(this,itemView);

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        this.context = context;
        this.fragment = fragment;
        this.listAdapter = listAdapter;

        this.cartItemMap = cartItemMap;
        this.cartStats = cartStatsMap;




        itemQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                setFilter();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



                cartItem = cartItemMap.get(shopItem.getItemID());
                cartStats = cartStatsMap;


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



//                                if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//                                {
//                                    ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
//                                }



                                itemsInCart = cartStats.getItemsInCart();
                                if(fragment instanceof ListItemClick)
                                {
                                    ((ListItemClick) fragment).setItemsInCart(cartStats.getItemsInCart(),false);
                                }


                            }else
                            {

//                                if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//                                {
//                                    ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()-1) + " " + "Items in Cart");
////                                        addToCartText.setBackgroundColor(ContextCompat.getColor(context,R.color.deepOrange900));
//                                }


                                itemsInCart = cartStats.getItemsInCart()-1;
                                if(fragment instanceof ListItemClick)
                                {
                                    ((ListItemClick) fragment).setItemsInCart(cartStats.getItemsInCart() - 1,false);
                                    addToCartText.setText("Remove Item");
                                }

                            }

                        }else
                        {
                            if(cartItem==null)
                            {
                                // no shop exist


//                                if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//                                {
//                                    ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");
//                                }


                                itemsInCart = cartStats.getItemsInCart()+1;

                                if(fragment instanceof ListItemClick)
                                {
                                    ((ListItemClick) fragment).setItemsInCart(cartStats.getItemsInCart() + 1,false);
                                }

                            }else
                            {
                                // shop Exist


                                itemsInCart = cartStats.getItemsInCart();

                                if(fragment instanceof ListItemClick)
                                {
//                                    ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");

                                    ((ListItemClick) fragment).setItemsInCart(cartStats.getItemsInCart(),false);

                                }

                                addToCartText.setText("Update Cart");
                            }
                        }

                    }
                    catch (Exception ex)
                    {
                        //ex.printStackTrace();
                    }

                }



                itemTotal.setText("Total : "  + PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.2f", total));



//                if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//                {
//                    ((ItemsInShopByCatFragmentDeprecated)fragment).cartTotal.setText("Cart Total : " + PrefGeneral.getCurrencySymbol(context) + " " + String.valueOf(cartTotalNeutral() + total));
//                }






                cartTotal = cartTotalNeutral() + total;

                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick) fragment).setCartTotal(cartTotal,false);
                }




            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

//        getCartStats(false,0,true);
    }








    public void bindShopItems(ShopItem shopItem)
    {

        this.shopItem = shopItem;
        Item item = shopItem.getItem();

//        holder.shopName.setText(dataset.get(position).getShopName());
//        holder.rating.setText(String.format( "%.2f", dataset.get(position).getRt_rating_avg()));
//        holder.distance.setText(String.format( "%.2f", dataset.get(position).getDistance() )+ " Km");





        cartItem = cartItemMap.get(shopItem.getItemID());
//        cartStats = cartStatsMap.get(shopItem.getShopID());


        CartItem cartItem = cartItemMap.get(shopItem.getItemID());

        if(cartItem!=null)
        {
            itemQuantity.setText(UtilityFunctions.refinedString(cartItem.getItemQuantity()));
            shopItemListItem.setBackgroundResource(R.color.gplus_color_2Alpha);

            double total = shopItem.getItemPrice() * cartItem.getItemQuantity();

            itemTotal.setText("Total : "  + PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
            addToCartText.setText("Update Cart");

        }else
        {

            shopItemListItem.setBackgroundResource(R.color.colorWhite);
            //holder.shopItemListItem.setBackgroundColor(22000000);
            itemQuantity.setText(String.valueOf(0));
            addToCartText.setText("Add to Cart");
        }




        if(shopItem.getAvailableItemQuantity()==0)
        {
            outOfStockIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            outOfStockIndicator.setVisibility(View.GONE);
        }



        available.setText("Available : " + shopItem.getAvailableItemQuantity() + " " + item.getQuantityUnit());



        String imagePath = null;

        if(item!=null)
        {
            String currency = "";
            currency = PrefCurrency.getCurrencySymbol(context);

            itemName.setText(item.getItemName());
            itemPrice.setText(currency + " " + String.format("%.2f",shopItem.getItemPrice()) + " per " + item.getQuantityUnit());

            if(item.getRt_rating_count()==0)
            {
                rating.setText(" - ");
                ratinCount.setText("(0 Ratings)");

            }
            else
            {
                rating.setText(String.format("%.1f",item.getRt_rating_avg()));
                ratinCount.setText("( " + (int) item.getRt_rating_count() +  " Ratings )");
            }



//                imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                        + item.getItemImageURL();

            imagePath = PrefGeneral.getServerURL(context)
                    + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";

        }


        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(itemImage);
    }






    @OnClick(R.id.item_image)
    void itemImageClick()
    {
        Item item = shopItem.getItem();

        if(item!=null && fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyItemImageClick(item);

        }
    }








    public ViewHolderShopItemButton(View itemView) {
        super(itemView);

        ButterKnife.bind(this,itemView);
    }




    @OnClick(R.id.add_to_cart_text)
    void addToCartClick(View view) {



        cartItem = cartItemMap.get(shopItem.getItemID());
//        cartStats = cartStatsMap.get(shopItem.getShopID());




        CartItem cartItem = new CartItem();
        cartItem.setItemID(shopItem.getItemID());

        if (!itemQuantity.getText().toString().equals("")) {

            cartItem.setItemQuantity(Double.parseDouble(itemQuantity.getText().toString()));
        }




        if (!cartItemMap.containsKey(shopItem.getItemID()))
        {


            if (itemQuantity.getText().toString().equals("")){

                showToastMessage("Please select quantity !");
            }
            else if (Double.parseDouble(itemQuantity.getText().toString()) == 0) {
                showToastMessage("Please select quantity greater than Zero !");

            } else {

                //showToastMessage("Add to cart! : " + dataset.get(getLayoutPosition()).getShopID());

                User endUser = PrefLogin.getUser(context);
                if(endUser==null)
                {

//                        Toast.makeText(context, "Please LoginUsingOTP to continue ...", Toast.LENGTH_SHORT).show();
                    showLoginDialog();

                    return;
                }


                Shop shop = PrefShopHome.getShop(context);

                Call<ResponseBody> call = cartItemService.createCartItem(
                        cartItem,
                        endUser.getUserID(),
                        shop.getShopID()
                );

                //dataset.get(getLayoutPosition()).getShopID()

                addToCartText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                        if (response.code() == 201) {

                            Toast.makeText(context, "Add to cart successful !", Toast.LENGTH_SHORT).show();

//                            getCartStats(true,getLayoutPosition(),false);


                            if(fragment instanceof ListItemClick)
                            {
                                ((ListItemClick) fragment).setCartTotal(cartTotal,true);
                                ((ListItemClick) fragment).setItemsInCart(itemsInCart,true);

                                ((ListItemClick) fragment).cartUpdated();
                            }



                            cartItemMap.put(cartItem.getItemID(),cartItem);
                            listAdapter.notifyItemChanged(getAdapterPosition());


                            addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                            itemTotal.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                        }


                        addToCartText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        addToCartText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }


        }
        else
        {

            if(itemQuantity.getText().toString().equals(""))
            {
                return;
            }

            double quantity = Double.parseDouble(itemQuantity.getText().toString());


            if(quantity==0)
            {
                // Delete from cart

                //UtilityGeneral.getEndUserID(MyApplication.getAppContext())
                User endUser = PrefLogin.getUser(context);
                if(endUser==null)
                {
                    return;
                }

                Call<ResponseBody> callDelete = cartItemService.deleteCartItem(0,cartItem.getItemID(),
                        endUser.getUserID(),
                        shopItem.getShopID()
                );

                addToCartText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                callDelete.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                        addToCartText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);


                        if(response.code()==200)
                        {

                            showToastMessage("Item Removed !");

                            addToCartText.setText("Add to Cart");




                            if(fragment instanceof ListItemClick)
                            {
                                ((ListItemClick) fragment).setCartTotal(cartTotal,true);
                                ((ListItemClick) fragment).setItemsInCart(itemsInCart,true);
                            }



                            cartItemMap.remove(cartItem.getItemID());
                            listAdapter.notifyItemChanged(getLayoutPosition());





//                            getCartStats(true,getLayoutPosition(),false);

                            //makeNetworkCall();

//                                notifyFilledCart.notifyCartDataChanged();

                            addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                            itemTotal.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));

                        }else
                        {

                        }

                        progressBar.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {


                        addToCartText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);


                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });


            }
            else
            {
                // Update from cart

                //UtilityGeneral.getEndUserID(MyApplication.getAppContext())
                User endUser = PrefLogin.getUser(context);

                if(endUser==null)
                {
                    return;
                }

//                if(getLayoutPosition() < dataset.size())
//                {




//                ShopItem shop = (ShopItem) dataset.get(getLayoutPosition());

                Call<ResponseBody> callUpdate = cartItemService.updateCartItem(
                        cartItem,
                        endUser.getUserID(),
                        shopItem.getShopID()
                );

                addToCartText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                callUpdate.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                        if (response.code() == 200) {

                            Toast.makeText(context, "Update cart successful !", Toast.LENGTH_SHORT).show();
//                            getCartStats(false,getLayoutPosition(),false);


                            if(fragment instanceof ListItemClick)
                            {
                                ((ListItemClick) fragment).setCartTotal(cartTotal,true);
                                ((ListItemClick) fragment).setItemsInCart(itemsInCart,true);
                            }



                            cartItemMap.put(cartItem.getItemID(),cartItem);
                            listAdapter.notifyItemChanged(getAdapterPosition());



                            addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                            itemTotal.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGrey800));
                        }

                        addToCartText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        addToCartText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

//
//                }
            }
        }
    }









    private void setFilter() {


        if (shopItem != null) {
            int availableItems = shopItem.getAvailableItemQuantity();

//            itemQuantity.setFilters(new InputFilter[]{new InputFilterMinMax("0", String.valueOf(availableItems))});
        }
    }




    private double cartTotalNeutral(){

        double previousTotal = 0;

        if(cartItem!=null && shopItem!=null)
        {
            previousTotal = shopItem.getItemPrice() * cartItem.getItemQuantity();
        }

        double cartTotalValue = 0;


//        Shop shop = PrefShopHome.getShopDetails(context);
//        CartStats cartStats = this.cartStats;

        if(cartStats!=null)
        {
            cartTotalValue = cartStats.getCart_Total();
        }

        return (cartTotalValue - previousTotal);
    }








    @OnClick(R.id.reduceQuantity)
    void reduceQuantityClick(View view)
    {
//        Shop shop = PrefShopHome.getShopDetails(context);


        cartItem = cartItemMap.get(shopItem.getItemID());
//        cartStats = cartStatsMap.get(shop.getShopID());

        addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_1));
//            itemTotal.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));


        double total = 0;


        if (!itemQuantity.getText().toString().equals("")){


            try{

                if(Double.parseDouble(itemQuantity.getText().toString())<=0) {

                    if (cartItem == null) {


                        if(cartStats==null)
                        {


//                            if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//                            {
//                                ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");
//                            }
                        }
                        else
                        {

//                            if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//                            {
//                                ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");;
//                            }


                        }


                    } else
                    {
//                        if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//                        {
//                            ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() - 1) + " " + "Items in Cart");
//                        }
                    }

                    return;
                }

                itemQuantity.setText(UtilityFunctions.refinedString(Double.parseDouble(itemQuantity.getText().toString()) - 1.0));

                total = shopItem.getItemPrice() * Double.parseDouble(itemQuantity.getText().toString());

            }
            catch (Exception ex)
            {

            }




//            if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//            {
//                ((ItemsInShopByCatFragmentDeprecated)fragment).cartTotal.setText("Cart Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.valueOf(cartTotalNeutral() + total));
//            }



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
        Shop shop = PrefShopHome.getShop(context);

//        shopItem = (ShopItem) dataset.get(getLayoutPosition());
        cartItem = cartItemMap.get(shopItem.getItemID());
//        cartStats = cartStatsMap.get(shop.getShopID());

        //dataset.get(getLayoutPosition()).getShopID()

        addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_1));
//            itemTotal.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));


        int availableItems = shopItem.getAvailableItemQuantity();
        double total = 0;


        if (!itemQuantity.getText().toString().equals("")) {


            if(cartItem==null)
            {
                if(Double.parseDouble(itemQuantity.getText().toString())>0 )
                {

                    if(cartStats==null)
                    {
//                        if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//                        {
//                            ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(1) + " " + "Items in Cart");
//                        }
                    }
                    else
                    {

//                        if(fragment instanceof ItemsInShopByCatFragmentDeprecated) {
//
//                            ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart() + 1) + " " + "Items in Cart");
//                        }



                    }

                }

            }
            else
            {


//                if(fragment instanceof ItemsInShopByCatFragmentDeprecated) {
//
//                    ((ItemsInShopByCatFragmentDeprecated)fragment).itemsInCart.setText(String.valueOf(cartStats.getItemsInCart()) + " " + "Items in Cart");
//                }
            }


            try {

                if (Double.parseDouble(itemQuantity.getText().toString()) >= availableItems) {
                    return;
                }


                itemQuantity.setText(UtilityFunctions.refinedString(Double.parseDouble(itemQuantity.getText().toString()) + 1.0));
                total = shopItem.getItemPrice() * Double.parseDouble(itemQuantity.getText().toString());

            }catch (Exception ex)
            {

            }

            itemTotal.setText("Total : "  + PrefCurrency.getCurrencySymbol(context) + " " + String.format("%.2f", total));


//            if(fragment instanceof ItemsInShopByCatFragmentDeprecated)
//            {
//                ((ItemsInShopByCatFragmentDeprecated)fragment).cartTotal.setText("Cart Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.valueOf(cartTotalNeutral() + total));
//            }



        }else
        {
            itemQuantity.setText(String.valueOf(0));
            itemTotal.setText("Total : "  + PrefCurrency.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
        }
    }







    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }





    private void showLoginDialog()
    {

//        if(context instanceof AppCompatActivity)
//        {
//            FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
//            LoginDialog loginDialog = new LoginDialog();
//            loginDialog.show(fm,"serviceUrl");
//        }



        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).showLogin();
        }

    }







    public interface ListItemClick{
        void notifyItemImageClick(Item item);
        void showLogin();

        void cartUpdated();

        void setCartTotal(double cartTotal, boolean save);
        void setItemsInCart(int itemsInCart, boolean save);

    }


}// View Holder Ends
