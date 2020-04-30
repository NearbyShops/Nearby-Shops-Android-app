package org.nearbyshops.enduserappnew.ViewHolders;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ViewHolderCartItemNew extends RecyclerView.ViewHolder implements TextWatcher{


    ImageView itemImage;
    TextView itemName;
    TextView itemsAvailable;
    TextView itemPrice;
    TextView itemTotal;
    ImageView increaseQuantity;
    ImageView reduceQuantity;
    EditText itemQuantity;
    ProgressBar progressBar;



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
                .inflate(R.layout.list_item_cart_item_new,parent,false);

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

            itemQuantity.setText(UtilityFunctions.refinedString(cartItem.getItemQuantity()));
            itemPrice.setText("Price : " + String.format("%.2f", cartItem.getRt_itemPrice()) + " per " + item.getQuantityUnit());


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



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

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


        itemTotal.setText("Total "  + PrefGeneral.getCurrencySymbol(context) + " " + String.format( "%.2f", total));



        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyTotal(cartTotal);
        }

        //cartTotal.setText("Cart Total : Rs " + String.format( "%.2f", total));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }











    private void removeItem()
    {

        Call<ResponseBody> call = cartItemService.deleteCartItem(cartItem.getCartID(),cartItem.getItemID(),0,0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

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




    private void updateClick()
    {


        Call<ResponseBody> call = cartItemService.updateCartItem(cartItem,0,0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    if(fragment instanceof ListItemClick)
                    {
                        ((ListItemClick) fragment).notifyUpdate(cartItem);
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Update failed. Try again !");

            }
        });

    }





    private void reduceQuantityClick()
    {

        double total = 0;

        if (!itemQuantity.getText().toString().equals("")){

            try{

                if(Double.parseDouble(itemQuantity.getText().toString())<=0) {

                    return;
                }

                itemQuantity.setText(UtilityFunctions.refinedString(Double.parseDouble(itemQuantity.getText().toString()) - 1));

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






    private void increaseQuantityClick()
    {

        int availableItems = cartItem.getRt_availableItemQuantity();

        double total = 0;

        if (!itemQuantity.getText().toString().equals("")) {

            try {


                if (Double.parseDouble(itemQuantity.getText().toString()) >= availableItems) {
                    return;
                }

                itemQuantity.setText(UtilityFunctions.refinedString(Double.parseDouble(itemQuantity.getText().toString()) + 1));

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





    public interface ListItemClick{
        void notifyUpdate(CartItem cartItem);
        void notifyRemove(CartItem cartItem);
        void notifyTotal(double total);
    }




}
