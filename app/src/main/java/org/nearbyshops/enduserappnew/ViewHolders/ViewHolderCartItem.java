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

public class ViewHolderCartItem extends RecyclerView.ViewHolder implements TextWatcher, View.OnClickListener {


    private ImageView shopImage;
    private TextView rating;
    private TextView itemName;
    private TextView itemsAvailable;
    private TextView itemPrice;
    private TextView itemTotal;
    private ImageView increaseQuantity;
    private ImageView reduceQuantity;
    private EditText itemQuantity;
    private TextView updateButton;
    private TextView removeButton;
    private ProgressBar progressBarRemove;
    private ProgressBar progressBarUpdate;




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









    public static ViewHolderCartItem create(ViewGroup parent, Context context,
                                            RecyclerView.Adapter adapter,List<Object> dataset,
                                            Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart_item_new,parent,false);

        return new ViewHolderCartItem( view, context, adapter, dataset, fragment);
    }




    public ViewHolderCartItem(@NonNull View itemView, Context context,
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



        updateButton = itemView.findViewById(R.id.textUpdate);
        removeButton = itemView.findViewById(R.id.textRemove);
        reduceQuantity = itemView.findViewById(R.id.reduceQuantity);
        increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
        itemTotal = itemView.findViewById(R.id.itemTotal);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        itemsAvailable = itemView.findViewById(R.id.itemsAvailable);
        rating = itemView.findViewById(R.id.rating);
        itemName = itemView.findViewById(R.id.itemName);
        shopImage = itemView.findViewById(R.id.itemImage);

        itemQuantity = itemView.findViewById(R.id.itemQuantity);


        progressBarRemove = itemView.findViewById(R.id.progress_bar_remove);
        progressBarUpdate = itemView.findViewById(R.id.progress_bar_update);



        itemQuantity.addTextChangedListener(this);
        reduceQuantity.setOnClickListener(this);
        increaseQuantity.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
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

            //holder.itemTotal.setText(" x " + cartItem.getRt_availableItemQuantity() + " (Unit Price) = "
            //        + "Rs:" +  String.format( "%.2f", cartItem.getRt_itemPrice()*cartItem.getItemQuantity()));


            itemTotal.setText("Total : " + PrefGeneral.getCurrencySymbol(context) + " "
                    + String.format("%.2f", cartItem.getRt_itemPrice() * cartItem.getItemQuantity()));

            itemsAvailable.setText("Available : " + cartItem.getRt_availableItemQuantity() + " " + item.getQuantityUnit());


//            imagePath = UtilityGeneral.getImageEndpointURL(MyApplicationCoreNew.getAppContext())
//                    + item.getItemImageURL();

            String imagePath = PrefGeneral.getServiceURL(context)
                    + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_grey, context.getTheme());


            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(shopImage);

        }

    }







    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        setFilter();
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


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.textRemove:

                removeClick();

                break;

            case R.id.textUpdate:

                updateClick();

                break;

            case R.id.reduceQuantity:

                reduceQuantityClick();

                break;

            case R.id.increaseQuantity:

                increaseQuantityClick();

                break;


            default:
                break;
        }

    }








    private void removeClick()
    {

//            notifyCartItem.notifyRemove(dataset.get(getLayoutPosition()));


        progressBarRemove.setVisibility(View.VISIBLE);
        removeButton.setVisibility(View.INVISIBLE);



        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm Remove Item !")
                .setMessage("Are you sure you want to remove this item !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        removeItem();

                        progressBarRemove.setVisibility(View.INVISIBLE);
                        removeButton.setVisibility(View.VISIBLE);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage(" Not Removed !");

                        progressBarRemove.setVisibility(View.INVISIBLE);
                        removeButton.setVisibility(View.VISIBLE);

                    }
                })
                .show();

    }




    private void removeItem()
    {

        progressBarRemove.setVisibility(View.VISIBLE);
        removeButton.setVisibility(View.INVISIBLE);


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

                progressBarRemove.setVisibility(View.INVISIBLE);
                removeButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Remove failed. Please Try again !");

                progressBarRemove.setVisibility(View.INVISIBLE);
                removeButton.setVisibility(View.VISIBLE);
            }
        });
    }




    private void updateClick()
    {

        progressBarUpdate.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.INVISIBLE);



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


                    showToastMessage("Updated !");
                }


                progressBarUpdate.setVisibility(View.INVISIBLE);
                updateButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Update failed. Try again !");


                progressBarUpdate.setVisibility(View.INVISIBLE);
                updateButton.setVisibility(View.VISIBLE);
            }
        });

    }





    private void reduceQuantityClick()
    {
        setFilter();

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
        setFilter();


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












    private void setFilter() {

//            CartItem cartItem = null;
//
//            if (getLayoutPosition() != -1) {
//
//                cartItem = dataset.get(getLayoutPosition());
//            }
//
//            if (cartItem != null) {
//                int availableItems = cartItem.getRt_availableItemQuantity();
//
//                itemQuantity.setFilters(new InputFilter[]{new InputFilterMinMax("0", String.valueOf(availableItems))});
//            }

    }






    public interface ListItemClick{
        void notifyUpdate(CartItem cartItem);
        void notifyRemove(CartItem cartItem);
        void notifyTotal(double total);
    }




}
