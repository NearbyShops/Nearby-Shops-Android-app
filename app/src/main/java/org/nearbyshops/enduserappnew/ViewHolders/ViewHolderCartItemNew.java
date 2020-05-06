package org.nearbyshops.enduserappnew.ViewHolders;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.API.CartItemService;
import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.CartItem;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.nearbyshops.enduserappnew.Utility.UtilityFunctions.refinedString;


public class ViewHolderCartItemNew extends RecyclerView.ViewHolder {


    @BindView(R.id.item_image) ImageView itemImage;
    @BindView(R.id.item_name) TextView itemName;
    @BindView(R.id.item_available_count) TextView itemsAvailable;
    @BindView(R.id.item_price) TextView itemPrice;
    @BindView(R.id.item_total) TextView itemTotal;
    @BindView(R.id.button_add) ImageView increaseQuantity;
    @BindView(R.id.button_reduce) ImageView reduceQuantity;
    @BindView(R.id.item_count) TextView itemQuantity;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    @BindView(R.id.discount_indicator) TextView discountIndicator;
    @BindView(R.id.list_price) TextView listPrice;



    private Context context;
    private Fragment fragment;
    private CartItem cartItem;

    private RecyclerView.Adapter adapter;
    private List<Object> dataset = null;


    private CartStats cartStats;
    private double cartTotal = 0;






    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;









    public static ViewHolderCartItemNew create(ViewGroup parent, Context context,
                                               RecyclerView.Adapter adapter, List<Object> dataset,
                                               Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart_item,parent,false);

        return new ViewHolderCartItemNew( view, context, adapter, dataset, fragment);
    }




    public ViewHolderCartItemNew(@NonNull View itemView, Context context,
                                 RecyclerView.Adapter adapter, List<Object> dataset,
                                 Fragment fragment) {
        super(itemView);

        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
        this.dataset = dataset;



        ButterKnife.bind(this,itemView);



        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }







    public void setItem(CartItem cartItem)
    {
        this.cartItem =  cartItem;

        Item item = null;

        if(cartItem!=null)
        {
            item = cartItem.getItem();
        }

        if(item != null) {

            itemName.setText(item.getItemName());


            itemQuantity.setText(refinedString(cartItem.getItemQuantity()));
            itemPrice.setText(PrefGeneral.getCurrencySymbol(context)  + String.format(" %.2f", cartItem.getRt_itemPrice()) + " per " + item.getQuantityUnit());



            String currency = "";
            currency = PrefGeneral.getCurrencySymbol(context);


            if(item.getListPrice()>0.0 && item.getListPrice()>cartItem.getRt_itemPrice())
            {
                listPrice.setText(currency + " " + refinedString(item.getListPrice()));
                listPrice.setPaintFlags(listPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                listPrice.setVisibility(View.VISIBLE);


                double discountPercent = ((item.getListPrice() - cartItem.getRt_itemPrice())/item.getListPrice())*100;
                discountIndicator.setText(String.format("%.0f ",discountPercent) + " %\nOff");

                discountIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                discountIndicator.setVisibility(View.GONE);
                listPrice.setVisibility(View.GONE);
            }


            itemTotal.setText("Total : " + PrefGeneral.getCurrencySymbol(context) + " "
                    + String.format("%.2f", cartItem.getRt_itemPrice() * cartItem.getItemQuantity()));

            itemsAvailable.setText("Available : " + cartItem.getRt_availableItemQuantity() + " " + item.getQuantityUnit());


            String imagePath = PrefGeneral.getServiceURL(context)
                    + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());



            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(itemImage);

        }

    }












    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }








    private void removeItem()
    {
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = cartItemService.deleteCartItem(cartItem.getCartID(),cartItem.getItemID(),0,0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBar.setVisibility(View.GONE);


                if(response.code() == 200)
                {
                    showToastMessage("Item Removed");

                    // refresh the list
//                        makeNetworkCall();

                    if(fragment instanceof ListItemClick)
                    {
                        ((ListItemClick) fragment).notifyRemove(cartItem);

                    }
                }




                adapter.notifyItemRemoved(getLayoutPosition());
                dataset.remove(cartItem);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Remove failed. Please Try again !");

            }
        });
    }




    private void updateQuantity()
    {
        progressBar.setVisibility(View.VISIBLE);


        updateTotal();


        Call<ResponseBody> call = cartItemService.updateCartItem(cartItem,0,0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                progressBar.setVisibility(View.GONE);

                if(response.code() == 200)
                {
                    if(fragment instanceof ListItemClick)
                    {
                        ((ListItemClick) fragment).notifyUpdate(cartItem);
                    }
                }
                else
                {
//                    showToastMessage("Failed Code : " + response.code());
                }

//                showToastMessage("Updated : " + response.code());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Update failed. Try again !");

            }
        });

    }





    @OnClick(R.id.button_reduce)
    void reduceQuantityClick()
    {

        double total = 0;

        if (!itemQuantity.getText().toString().equals("")){

            try{

                if(Double.parseDouble(itemQuantity.getText().toString())<=0) {

                    removeItem();

                    return;
                }

                itemQuantity.setText(refinedString(Double.parseDouble(itemQuantity.getText().toString()) - 1));



                if(Double.parseDouble(itemQuantity.getText().toString())<=0)
                {
                    removeItem();
                }
                else
                {
                    updateQuantityTimer();
                }



                total = cartItem.getRt_itemPrice() * Double.parseDouble(itemQuantity.getText().toString());

            }
            catch (Exception ex)
            {

            }

            itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));

        }else
        {
            itemQuantity.setText(String.valueOf(0));
            itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
        }

    }






    @OnClick(R.id.button_add)
    void increaseQuantityClick()
    {

        int availableItems = cartItem.getRt_availableItemQuantity();

        double total = 0;

        if (!itemQuantity.getText().toString().equals("")) {

            try {


                if (Double.parseDouble(itemQuantity.getText().toString()) >= availableItems) {
                    return;
                }

                itemQuantity.setText(refinedString(Double.parseDouble(itemQuantity.getText().toString()) + 1));

                updateQuantityTimer();

                total = cartItem.getRt_itemPrice() * Double.parseDouble(itemQuantity.getText().toString());


            }catch (Exception ex)
            {

            }

            itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format("%.2f", total));

        }else
        {
            itemQuantity.setText(String.valueOf(0));
            itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));
        }

    }





    private void updateQuantityTimer()
    {
        progressBar.setVisibility(View.VISIBLE);

        countDownTimer.cancel();
        countDownTimer.start();
    }












    private CountDownTimer countDownTimer = new CountDownTimer(700, 500) {

        public void onTick(long millisUntilFinished) {

//            logMessage("Timer onTick()");
        }


        public void onFinish() {

            updateQuantity();
        }
    };







    private void updateTotal()
    {
        double previousTotal = cartItem.getRt_itemPrice()*cartItem.getItemQuantity();

        double total = 0;

        if (!itemQuantity.getText().toString().equals(""))
        {

            try{


                total = cartItem.getRt_itemPrice() * Double.parseDouble(itemQuantity.getText().toString());

                cartItem.setItemQuantity(Double.parseDouble(itemQuantity.getText().toString()));


                if(Double.parseDouble(itemQuantity.getText().toString())==0)
                {



                    //itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");

                }else
                {

                    //itemsInCart.setText(String.valueOf(1) + " " + "Items in Cart");
                }

            }
            catch (Exception ex)
            {
                //ex.printStackTrace();
            }

            cartTotal = cartTotal - previousTotal + total;


        }else
        {
            //itemsInCart.setText(String.valueOf(0) + " " + "Items in Cart");
        }


        //itemTotal.setText("Total : " + String.format( "%.2f", total));

        //itemTotal.setText(" x " + cartItem.getRt_itemPrice() + " (Unit Price) = "
        //        + "Rs:" + String.format( "%.2f", total));


//        itemTotal.setText("Total "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));



        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyTotal(cartTotal);
        }
    }



    public interface ListItemClick{
        void notifyUpdate(CartItem cartItem);
        void notifyRemove(CartItem cartItem);
        void notifyTotal(double total);
    }




}
