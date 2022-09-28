package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShopItem;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.API.CartItemService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.CartItem;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.CartStats;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Model.ShopItem;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopHome;
import org.nearbyshops.whitelabelapp.R;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class ViewHolderShopItemInstacart extends RecyclerView.ViewHolder{


    private Map<Integer, CartItem> cartItemMap;
    private CartStats cartStats;


    @Inject
    CartItemService cartItemService;


    @BindView(R.id.quantity_half) TextView quantityHalf;
    @BindView(R.id.quantity_quarter) TextView quantityQuarter;
    @BindView(R.id.dummy_text_for_margin) TextView dummyTextForMargin;


    @BindView(R.id.item_title) TextView itemName;
    @BindView(R.id.item_image) ImageView itemImage;
    @BindView(R.id.item_price) TextView itemPrice;

//    @BindView(R.id.rating) TextView rating;
//    @BindView(R.id.rating_count) TextView ratinCount;

    @BindView(R.id.plus_button) ImageView increaseQuantity;
    @BindView(R.id.item_quantity) TextView itemQuantity;
    @BindView(R.id.minus_button) ImageView reduceQuantity;

    @BindView(R.id.add_button_block) CardView addButtonBlock;

    @BindView(R.id.out_of_stock_indicator) TextView outOfStockIndicator;

    @BindView(R.id.item_quantity_indicator) TextView itemQuantityIndicator;


    @BindView(R.id.list_item) CardView shopItemListItem;


    @BindView(R.id.discount_indicator) TextView discountIndicator;
    @BindView(R.id.list_price) TextView listPrice;



    @BindView(R.id.add_button) ImageView addLabel;

    @BindView(R.id.image_loading) ProgressBar imageProgress;
    @BindView(R.id.progress_quantity_update) ProgressBar progressQuantityUpdate;

    String currency = "";



    private ShopItem shopItem;
    private CartItem cartItem;



    private Context context;
    private Fragment fragment;

    private RecyclerView.Adapter listAdapter;



    public static int LAYOUT_TYPE_FULL_WIDTH = 1;
    public static int LAYOUT_TYPE_GRID = 2;




    private int itemsInCart;
    private double cartTotal;




    public static ViewHolderShopItemInstacart create(ViewGroup parent, Context context, Fragment fragment, RecyclerView.Adapter adapter,
                                                     Map<Integer, CartItem> cartItemMap, CartStats cartStats)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_item_instacart,parent,false);
        return new ViewHolderShopItemInstacart(view,context,fragment,adapter,cartItemMap,cartStats);
    }






    public static ViewHolderShopItemInstacart create(ViewGroup parent, Context context, Fragment fragment, RecyclerView.Adapter adapter,
                                                     Map<Integer, CartItem> cartItemMap, CartStats cartStats, int layoutType)
    {

        View view = null;

        if(layoutType==LAYOUT_TYPE_FULL_WIDTH)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_item_instacart_full_width_new,parent,false);
        }
        else if(layoutType==LAYOUT_TYPE_GRID)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_item_instacart,parent,false);
        }


        return new ViewHolderShopItemInstacart(view,context,fragment,adapter,cartItemMap,cartStats);
    }






    private ViewHolderShopItemInstacart(@NonNull View itemView, Context context, Fragment fragment, RecyclerView.Adapter listAdapter,
                                        Map<Integer, CartItem> cartItemMap, CartStats cartStats) {

        super(itemView);
        ButterKnife.bind(this,itemView);

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        this.context = context;
        this.fragment = fragment;
        this.listAdapter = listAdapter;

        this.cartItemMap = cartItemMap;
        this.cartStats = cartStats;


                currency = PrefCurrency.getCurrencySymbol(context);
//        this.currency = PrefShopHome.getCurrencySymbolForShop(context);




        itemQuantityIndicator.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                setFilter();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



                cartItem = cartItemMap.get(shopItem.getItemID());


                double total = 0;
                int availableItems = shopItem.getAvailableItemQuantity();



                if (!itemQuantityIndicator.getText().toString().equals(""))
                {

                    try{

                        if(Double.parseDouble(itemQuantityIndicator.getText().toString())>availableItems)
                        {
                            return;
                        }



                        total = shopItem.getItemPrice() * Double.parseDouble(itemQuantityIndicator.getText().toString());


                        if(Double.parseDouble(itemQuantityIndicator.getText().toString())==0)
                        {
                            if(cartItem==null)
                            {

                                itemsInCart = cartStats.getItemsInCart();

                                if(fragment instanceof ListItemClick)
                                {
                                    ((ListItemClick) fragment).setItemsInCart(cartStats.getItemsInCart(),false);
                                }

                            }
                            else
                            {

                                itemsInCart = cartStats.getItemsInCart()-1;

                                if(fragment instanceof ListItemClick)
                                {
                                    ((ListItemClick) fragment).setItemsInCart(cartStats.getItemsInCart() - 1,false);
                                }

                            }

                        }
                        else
                        {

                            if(cartItem==null)
                            {
                                // no shop exist
                                itemsInCart = cartStats.getItemsInCart()+1;

                                if(fragment instanceof ListItemClick)
                                {
                                    ((ListItemClick) fragment).setItemsInCart(cartStats.getItemsInCart() + 1,false);
                                }

                            }
                            else
                            {

                                // shop Exist
                                itemsInCart = cartStats.getItemsInCart();

                                if(fragment instanceof ListItemClick)
                                {
                                    ((ListItemClick) fragment).setItemsInCart(cartStats.getItemsInCart(),false);
                                }
                            }
                        }

                    }
                    catch (Exception ex)
                    {
                        //ex.printStackTrace();
                    }

                }



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






    @OnClick(R.id.quantity_quarter)
    void quarterQuantityClick()
    {

        if (!itemQuantity.getText().toString().equals("")){

            itemQuantityIndicator.setText(String.valueOf(Double.parseDouble(itemQuantityIndicator.getText().toString()) + 0.25));
            itemQuantity.setText(String.valueOf(Double.parseDouble(itemQuantity.getText().toString()) + 0.25));
        }

        addToCartTimer();
    }






    @OnClick(R.id.quantity_half)
    void halfQuantityClick()
    {
        if (!itemQuantity.getText().toString().equals("")){

            itemQuantityIndicator.setText(String.valueOf(Double.parseDouble(itemQuantityIndicator.getText().toString()) + 0.50));
            itemQuantity.setText(String.valueOf(Double.parseDouble(itemQuantity.getText().toString()) + 0.50));
        }

        addToCartTimer();
    }







    public void bindShopItems(ShopItem shopItem)
    {

        this.shopItem = shopItem;
        Item item = shopItem.getItem();




        if(shopItem.isAllowQuarterQuantity())
        {
            quantityQuarter.setVisibility(View.VISIBLE);
        }
        else
        {
            quantityQuarter.setVisibility(View.GONE);
        }



        if(shopItem.isAllowHalfQuantity())
        {
            quantityHalf.setVisibility(View.VISIBLE);
        }
        else
        {
            quantityHalf.setVisibility(View.GONE);
        }


        if(shopItem.isAllowHalfQuantity() && shopItem.isAllowQuarterQuantity())
        {
//            dummyTextForMargin.setVisibility(View.VISIBLE);
        }
        else
        {
            dummyTextForMargin.setVisibility(View.GONE);
        }





        cartItem = cartItemMap.get(shopItem.getItemID());



            if(cartItem!=null)
            {

                itemQuantityIndicator.setText(refinedString(cartItem.getItemQuantity()));
                itemQuantity.setText(refinedString(cartItem.getItemQuantity()));


                if(cartItem.getItemQuantity()==0)
                {
                    addLabel.setBackgroundResource(R.drawable.empty_circle);
                    itemQuantityIndicator.setVisibility(View.GONE);
                }
                else
                {

                    itemQuantityIndicator.setVisibility(View.VISIBLE);
                    addLabel.setBackgroundResource(R.drawable.empty_circle_filled);
                }


            }else
            {

                itemQuantityIndicator.setText(String.valueOf(0));
                itemQuantity.setText(String.valueOf(0));

                itemQuantityIndicator.setVisibility(View.GONE);
                addLabel.setBackgroundResource(R.drawable.empty_circle);
            }



            if(shopItem.getAvailableItemQuantity()==0)
            {
                outOfStockIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                outOfStockIndicator.setVisibility(View.GONE);
            }





            String imagePath = null;

            if(item!=null)
            {

                itemName.setText(item.getItemName());
                itemPrice.setText(currency + " " + refinedString(shopItem.getItemPrice()) + " / " + item.getQuantityUnit());



                if(item.getListPrice()>0.0 && item.getListPrice()>shopItem.getItemPrice())
                {
                    listPrice.setText(currency + " " + refinedString(item.getListPrice()));
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


                imagePath = PrefGeneral.getServerURL(context)
                        + "/api/v1/Item/Image/seven_hundred_" + item.getItemImageURL() + ".jpg";

            }


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            imageProgress.setVisibility(View.VISIBLE);

            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(itemImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            imageProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            imageProgress.setVisibility(View.GONE);
                        }
                    });
    }









    @OnClick(R.id.list_item)
    void itemImageClick()
    {
        Item item = shopItem.getItem();

        if(item!=null && fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyItemImageClick(item);
        }
    }






//    @OnTouch({R.id.list_item,R.id.item_image})
    void listItemTouch()
    {
//        addButtonBlock.setVisibility(View.GONE);
    }





    private void addToCartClick() {



        cartItem = cartItemMap.get(shopItem.getItemID());




        CartItem cartItemLocal = new CartItem();
        cartItemLocal.setItemID(shopItem.getItemID());



        if (!itemQuantityIndicator.getText().toString().equals("")) {

            cartItemLocal.setItemQuantity(Double.parseDouble(itemQuantityIndicator.getText().toString()));
        }



        if (!cartItemMap.containsKey(shopItem.getItemID()))
        {

            // the item was not in the cart so add the item to the cart !


            if (itemQuantityIndicator.getText().toString().equals("")){

                showToastMessage("Please select quantity !");
            }
            else if (Double.parseDouble(itemQuantityIndicator.getText().toString()) == 0) {
                showToastMessage("Please select quantity greater than Zero !");

            } else {


                User endUser = PrefLogin.getUser(context);
                if(endUser==null)
                {

                    showLoginDialog();

                    return;
                }


                Shop shop = PrefShopHome.getShop(context);

                Call<ResponseBody> call = cartItemService.createCartItem(
                        cartItemLocal,
                        endUser.getUserID(),
                        shop.getShopID()
                );


                progressQuantityUpdate.setVisibility(View.VISIBLE);


                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                        if (response.code() == 201) {


                            addButtonBlock.setVisibility(View.GONE);

                                showToastMessage("Add to cart successful !");

//                            getCartStats(true,getLayoutPosition(),false);


                            if(fragment instanceof ListItemClick)
                            {
                                ((ListItemClick) fragment).setCartTotal(cartTotal,true);
                                ((ListItemClick) fragment).setItemsInCart(itemsInCart,true);


                                ((ListItemClick) fragment).cartUpdated();
                            }



                            cartItemMap.put(cartItemLocal.getItemID(),cartItemLocal);
//                            listAdapter.notifyItemChanged(getAdapterPosition());


                            bindShopItems(shopItem);


                        }



//                        progressBar.setVisibility(View.INVISIBLE);
                        progressQuantityUpdate.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

//                        progressBar.setVisibility(View.INVISIBLE);


                        progressQuantityUpdate.setVisibility(View.GONE);
                    }
                });
            }


        }
        else
        {
            // the item is already available in the cart so we should either remove the item from cart if quantity is zero or
            // we should update the quantity if the quantity is not zero

            if(itemQuantityIndicator.getText().toString().equals(""))
            {
                return;
            }

            double quantity = Double.parseDouble(itemQuantityIndicator.getText().toString());

            if(quantity==0)
            {
                // Delete from cart

                //UtilityGeneral.getEndUserID(MyApplicationCoreNew.getAppContext())
                User endUser = PrefLogin.getUser(context);
                if(endUser==null)
                {
                    return;
                }

                Call<ResponseBody> callDelete = cartItemService.deleteCartItem(0,cartItemLocal.getItemID(),
                        endUser.getUserID(),
                        shopItem.getShopID()
                );


//                progressBar.setVisibility(View.VISIBLE);

                callDelete.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



//                        progressBar.setVisibility(View.INVISIBLE);


                        if(response.code()==200)
                        {
                            addButtonBlock.setVisibility(View.GONE);

                            showToastMessage("Item Removed !");


                            if(fragment instanceof ListItemClick)
                            {
                                ((ListItemClick) fragment).setCartTotal(cartTotal,true);
                                ((ListItemClick) fragment).setItemsInCart(itemsInCart,true);

                                ((ListItemClick) fragment).cartUpdated();
                            }



                            cartItemMap.remove(cartItemLocal.getItemID());
                            bindShopItems(shopItem);



                        }



//                        progressBar.setVisibility(View.INVISIBLE);

                        progressQuantityUpdate.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {


//                        progressBar.setVisibility(View.INVISIBLE);

                        progressQuantityUpdate.setVisibility(View.GONE);

                    }
                });


            }
            else
            {
                // Update from cart

                //UtilityGeneral.getEndUserID(MyApplicationCoreNew.getAppContext())
                User endUser = PrefLogin.getUser(context);

                if(endUser==null)
                {
                    return;
                }

//                if(getLayoutPosition() < dataset.size())
//                {




//                ShopItem shop = (ShopItem) dataset.get(getLayoutPosition());

                Call<ResponseBody> callUpdate = cartItemService.updateCartItem(
                        cartItemLocal,
                        endUser.getUserID(),
                        shopItem.getShopID()
                );




//                progressBar.setVisibility(View.VISIBLE);

                callUpdate.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                        if (response.code() == 200) {


                            showToastMessage("Update cart successful !");


                            addButtonBlock.setVisibility(View.GONE);

                            if(fragment instanceof ListItemClick)
                            {
                                ((ListItemClick) fragment).setCartTotal(cartTotal,true);
                                ((ListItemClick) fragment).setItemsInCart(itemsInCart,true);

                                ((ListItemClick) fragment).cartUpdated();
                            }



                            cartItemMap.put(cartItemLocal.getItemID(),cartItemLocal);
//                            listAdapter.notifyItemChanged(getAdapterPosition());
                            bindShopItems(shopItem);

                        }

//                        progressBar.setVisibility(View.INVISIBLE);


                        progressQuantityUpdate.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {


                        progressQuantityUpdate.setVisibility(View.GONE);
                    }
                });

            }
        }
    }










    private double cartTotalNeutral(){

        double previousTotal = 0;

        if(cartItem!=null && shopItem!=null)
        {
            previousTotal = shopItem.getItemPrice() * cartItem.getItemQuantity();
        }

        double cartTotalValue = 0;


        if(cartStats !=null)
        {
            cartTotalValue = cartStats.getCart_Total();
        }

        return (cartTotalValue - previousTotal);
    }








    @OnClick(R.id.minus_button_cover)
    void reduceQuantityClick(View view)
    {

        if (!itemQuantityIndicator.getText().toString().equals("")){


            try{

                if(Double.parseDouble(itemQuantityIndicator.getText().toString())<=0) {

                    addButtonBlock.setVisibility(View.GONE);
                    return;
                }



                if((Double.parseDouble(itemQuantityIndicator.getText().toString()) - 1)<=0)
                {
                    itemQuantityIndicator.setText(refinedString(0));
                    itemQuantity.setText(refinedString(0));
                    addButtonBlock.setVisibility(View.GONE);
                }
                else
                {
                    itemQuantityIndicator.setText(refinedString(Double.parseDouble(itemQuantityIndicator.getText().toString()) - 1));
                    itemQuantity.setText(refinedString(Double.parseDouble(itemQuantity.getText().toString()) - 1));
                }



                addToCartTimer();

            }
            catch (Exception ex)
            {

            }




        }else
        {
            itemQuantityIndicator.setText(String.valueOf(0));
            itemQuantity.setText(String.valueOf(0));
            addToCartTimer();
        }
    }




    @OnClick(R.id.plus_button_cover)
    void increaseQuantityClick(View view)
    {
        User endUser = PrefLogin.getUser(context);

        if(endUser==null)
        {
            showLoginDialog();
            return;
        }




        int availableItems = shopItem.getAvailableItemQuantity();


        if (availableItems==0) {
            // item out of stock
            showMessage("Item Out of Stock !");
            return;
        }


        if (!itemQuantityIndicator.getText().toString().equals("")) {




            try {

                if (Double.parseDouble(itemQuantityIndicator.getText().toString()) >= availableItems) {
                    return;
                }


                itemQuantityIndicator.setText(refinedString(Double.parseDouble(itemQuantityIndicator.getText().toString()) + 1));
                itemQuantity.setText(refinedString(Double.parseDouble(itemQuantity.getText().toString()) + 1));


                addToCartTimer();


            }catch (Exception ex)
            {

            }



        }else
        {
            itemQuantityIndicator.setText(String.valueOf(0));
            itemQuantity.setText(String.valueOf(0));

            addToCartTimer();
        }
    }







    @OnClick(R.id.add_button)
    void addClick()
    {
        User endUser = PrefLogin.getUser(context);

        if(endUser==null)
        {
            showLoginDialog();
            return;
        }



        addButtonBlock.setVisibility(View.VISIBLE);


        int availableItems = shopItem.getAvailableItemQuantity();


        if (availableItems==0) {
            // item out of stock
            showMessage("Item Out of Stock !");
            return;
        }



        if(shopItem.isAllowHalfQuantity() || shopItem.isAllowQuarterQuantity())
        {

        }
        else
        {

            if(Double.parseDouble(itemQuantityIndicator.getText().toString())==0)
            {
                itemQuantityIndicator.setText(String.valueOf(1));
                itemQuantity.setText(String.valueOf(1));
                addToCartTimer();
            }

        }



    }







    private void showToastMessage(String message)
    {
//        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }




    private void showMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }





    private void showLoginDialog()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).showLogin();
        }
    }











    private void addToCartTimer()
    {

        progressQuantityUpdate.setVisibility(View.VISIBLE);

//        addLabel.setVisibility(View.VISIBLE);

//        progressBar.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
        countDownTimer.start();
    }







    private CountDownTimer countDownTimer = new CountDownTimer(1400, 500) {

        public void onTick(long millisUntilFinished) {

//            logMessage("Timer onTick()");
        }


        public void onFinish() {

            addToCartClick();
        }
    };





    private String refinedString(double number)
    {
        if(number % 1 !=0)
        {
            // contains decimal numbers

            return String.format("%.2f",number);
        }
        else
        {
            return String.format("%.0f",number);
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
