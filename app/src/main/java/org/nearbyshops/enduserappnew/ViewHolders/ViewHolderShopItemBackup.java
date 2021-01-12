package org.nearbyshops.enduserappnew.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.API.CartItemService;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.InputFilterMinMax;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewHolderShopItemBackup extends RecyclerView.ViewHolder{


    private Map<Integer, CartItem> cartItemMap;
//    private Map<Integer, CartStats> cartStatsMap;
    private CartStats cartStats;


    @Inject
    CartItemService cartItemService;



    @BindView(R.id.item_title) TextView itemName;
    @BindView(R.id.item_image) ImageView itemImage;
    @BindView(R.id.item_price) TextView itemPrice;

    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView ratinCount;

    @BindView(R.id.increaseQuantity) ImageView increaseQuantity;
    @BindView(R.id.itemQuantity) TextView itemQuantityText;
    @BindView(R.id.reduceQuantity) ImageView reduceQuantity;



    @BindView(R.id.out_of_stock_indicator) TextView outOfStockIndicator;
    @BindView(R.id.list_item) ConstraintLayout shopItemListItem;

    @BindView(R.id.add_label) TextView addLabel;


    private ShopItem shopItem;
    private CartItem cartItem;
//    private CartStats cartStats;

    private Context context;
    private Fragment fragment;

    private RecyclerView.Adapter listAdapter;

    @BindView(R.id.progress_bar) ProgressBar progressBar;



//    @BindView(R.id.label) TextView label;






    private int itemsInCart;
    private double cartTotal;




    public static ViewHolderShopItemBackup create(ViewGroup parent, Context context, Fragment fragment, RecyclerView.Adapter adapter,
                                                  Map<Integer, CartItem> cartItemMap, CartStats cartStats)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_item_full_width,parent,false);
        return new ViewHolderShopItemBackup(view,context,fragment,adapter,cartItemMap,cartStats);
    }






    private ViewHolderShopItemBackup(@NonNull View itemView, Context context, Fragment fragment, RecyclerView.Adapter listAdapter,
                                     Map<Integer, CartItem> cartItemMap, CartStats cartStats) {

        super(itemView);
        ButterKnife.bind(this,itemView);

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        this.context = context;
        this.fragment = fragment;
        this.listAdapter = listAdapter;

        this.cartItemMap = cartItemMap;
//        this.cartStatsMap = cartStatsMap;
        this.cartStats = cartStats;




        itemQuantityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                setFilter();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



                cartItem = cartItemMap.get(shopItem.getItemID());
//                cartStats = cartStatsMap.get(shopItem.getShopID());


                double total = 0;
                int availableItems = shopItem.getAvailableItemQuantity();



                if (!itemQuantityText.getText().toString().equals(""))
                {

                    try{

                        if(Integer.parseInt(itemQuantityText.getText().toString())>availableItems)
                        {

                            return;
                        }

                        total = shopItem.getItemPrice() * Integer.parseInt(itemQuantityText.getText().toString());


                        if(Integer.parseInt(itemQuantityText.getText().toString())==0)
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


        if (!itemQuantityText.getText().toString().equals("")){

            itemQuantityText.setText("0.25");
        }


//        itemQuantityText.setText(String.valueOf(Double.parseDouble(itemQuantityText.getText().toString()) + 0.25));
        itemQuantityText.setText("0.25");

        addToCartTimer();
    }




    @OnClick(R.id.quantity_half)
    void halfQuantityClick()
    {


        if (!itemQuantityText.getText().toString().equals("")){

            itemQuantityText.setText("0.5");
        }

//            itemQuantityText.setText(String.valueOf(Double.parseDouble(itemQuantityText.getText().toString()) + 0.5));

        itemQuantityText.setText("0.5");
        addToCartTimer();
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


                itemQuantityText.setText(String.valueOf(cartItem.getItemQuantity()));


//                label.setText(String.format("%.2f",cartItem.getItemQuantity()));



                double total = shopItem.getItemPrice() * cartItem.getItemQuantity();

//                itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));

                if(cartItem.getItemQuantity()==0)
                {
                    addLabel.setVisibility(View.VISIBLE);
                }
                else
                {
                    addLabel.setVisibility(View.GONE);
                }


            }else
            {

//                shopItemListItem.setBackgroundResource(R.color.colorWhite);
                itemQuantityText.setText(String.valueOf(0));
                addLabel.setVisibility(View.VISIBLE);
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
                String currency = "";
                currency = PrefGeneral.getCurrencySymbol(context);

                itemName.setText(item.getItemName());
//                itemPrice.setText(currency + " " + String.format("%.0f",shopItem.getItemPrice()) + " per " + item.getQuantityUnit());
                itemPrice.setText(currency + " " + String.format("%.0f",shopItem.getItemPrice()) + " / " + item.getQuantityUnit());

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



//                imagePath = UtilityGeneral.getImageEndpointURL(MyApplicationCoreNew.getAppContext())
//                        + item.getItemImageURL();

                imagePath = PrefGeneral.getServiceURL(context)
                        + "/api/v1/Item/Image/seven_hundred_" + item.getItemImageURL() + ".jpg";

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







    private void addToCartClick() {



        cartItem = cartItemMap.get(shopItem.getItemID());
//        cartStats = cartStatsMap.get(shopItem.getShopID());




        CartItem cartItem = new CartItem();
        cartItem.setItemID(shopItem.getItemID());

        if (!itemQuantityText.getText().toString().equals("")) {

            cartItem.setItemQuantity(Double.parseDouble(itemQuantityText.getText().toString()));
        }



        if (!cartItemMap.containsKey(shopItem.getItemID()))
        {

            // the item was not in the cart so add the item to the cart !


            if (itemQuantityText.getText().toString().equals("")){

                showToastMessage("Please select quantity !");
            }
            else if (Double.parseDouble(itemQuantityText.getText().toString()) == 0) {
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



                progressBar.setVisibility(View.VISIBLE);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                        if (response.code() == 201) {



                                showToastMessage("Add to cart successful !");

//                            getCartStats(true,getLayoutPosition(),false);


                            if(fragment instanceof ListItemClick)
                            {
                                ((ListItemClick) fragment).setCartTotal(cartTotal,true);
                                ((ListItemClick) fragment).setItemsInCart(itemsInCart,true);
                            }



                            cartItemMap.put(cartItem.getItemID(),cartItem);
//                            listAdapter.notifyItemChanged(getAdapterPosition());


                            bindShopItems(shopItem);


                        }



                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }


        }
        else
        {
            // the item is already available in the cart so we should either remove the item from cart if quantity is zero or
            // we should update the quantity if the quantity is not zero

            if(itemQuantityText.getText().toString().equals(""))
            {
                return;
            }

            double quantity = Double.parseDouble(itemQuantityText.getText().toString());

            if(quantity==0)
            {
                // Delete from cart

                //UtilityGeneral.getEndUserID(MyApplicationCoreNew.getAppContext())
                User endUser = PrefLogin.getUser(context);
                if(endUser==null)
                {
                    return;
                }

                Call<ResponseBody> callDelete = cartItemService.deleteCartItem(0,cartItem.getItemID(),
                        endUser.getUserID(),
                        shopItem.getShopID()
                );


                progressBar.setVisibility(View.VISIBLE);

                callDelete.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                        progressBar.setVisibility(View.INVISIBLE);


                        if(response.code()==200)
                        {

                            showToastMessage("Item Removed !");


                            if(fragment instanceof ListItemClick)
                            {
                                ((ListItemClick) fragment).setCartTotal(cartTotal,true);
                                ((ListItemClick) fragment).setItemsInCart(itemsInCart,true);
                            }



                            cartItemMap.remove(cartItem.getItemID());
//                            listAdapter.notifyItemChanged(getLayoutPosition());
                            bindShopItems(shopItem);





//                            getCartStats(true,getLayoutPosition(),false);

                            //makeNetworkCall();

//                                notifyFilledCart.notifyCartDataChanged();




                        }



                        progressBar.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {


                        progressBar.setVisibility(View.INVISIBLE);


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
                        cartItem,
                        endUser.getUserID(),
                        shopItem.getShopID()
                );




                progressBar.setVisibility(View.VISIBLE);

                callUpdate.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                        if (response.code() == 200) {

//                            Toast.makeText(context, "Update cart successful !", Toast.LENGTH_SHORT).show();
//                            getCartStats(false,getLayoutPosition(),false);

                            showToastMessage("Update cart successful !");


                            if(fragment instanceof ListItemClick)
                            {
                                ((ListItemClick) fragment).setCartTotal(cartTotal,true);
                                ((ListItemClick) fragment).setItemsInCart(itemsInCart,true);
                            }



                            cartItemMap.put(cartItem.getItemID(),cartItem);
//                            listAdapter.notifyItemChanged(getAdapterPosition());
                            bindShopItems(shopItem);

                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        }
    }







    private void setFilter() {


        if (shopItem != null) {
            int availableItems = shopItem.getAvailableItemQuantity();

            itemQuantityText.setFilters(new InputFilter[]{new InputFilterMinMax("0", String.valueOf(availableItems))});
        }
    }






    private double cartTotalNeutral(){

        double previousTotal = 0;

        if(cartItem!=null && shopItem!=null)
        {
            previousTotal = shopItem.getItemPrice() * cartItem.getItemQuantity();
        }

        double cartTotalValue = 0;

        Shop shop = PrefShopHome.getShop(context);

//        CartStats cartStats = cartStatsMap.get(shop.getShopID());


        if(cartStats !=null)
        {
            cartTotalValue = cartStats.getCart_Total();
        }

        return (cartTotalValue - previousTotal);
    }








    @OnClick(R.id.reduceQuantity)
    void reduceQuantityClick(View view)
    {

        if (!itemQuantityText.getText().toString().equals("")){


            try{

                if(Integer.parseInt(itemQuantityText.getText().toString())<=0) {


                    return;
                }



                itemQuantityText.setText(String.valueOf(Integer.parseInt(itemQuantityText.getText().toString()) - 1));
                addToCartTimer();

            }
            catch (Exception ex)
            {

            }




        }else
        {
            itemQuantityText.setText(String.valueOf(0));
            addToCartTimer();
        }
    }





    @OnClick(R.id.increaseQuantity)
    void increaseQuantityClick(View view)
    {



        int availableItems = shopItem.getAvailableItemQuantity();


        if (!itemQuantityText.getText().toString().equals("")) {




            try {

                if (Integer.parseInt(itemQuantityText.getText().toString()) >= availableItems) {
                    return;
                }


                itemQuantityText.setText(String.valueOf(Integer.parseInt(itemQuantityText.getText().toString()) + 1));
                addToCartTimer();


            }catch (Exception ex)
            {

            }



        }else
        {
            itemQuantityText.setText(String.valueOf(0));
            addToCartTimer();
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








    @OnClick(R.id.add_label)
    void addClick()
    {
        itemQuantityText.setText(String.valueOf(1));
        addToCartClick();
    }





    private void addToCartTimer()
    {
        progressBar.setVisibility(View.VISIBLE);

        countDownTimer.cancel();
        countDownTimer.start();
    }







    private CountDownTimer countDownTimer = new CountDownTimer(1000, 500) {

        public void onTick(long millisUntilFinished) {

//            logMessage("Timer onTick()");
        }


        public void onFinish() {

            addToCartClick();
        }
    };





    String refinedString(double number)
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
        void setCartTotal(double cartTotal, boolean save);
        void setItemsInCart(int itemsInCart, boolean save);

    }


}// View Holder Ends
