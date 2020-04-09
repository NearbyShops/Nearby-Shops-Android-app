package org.nearbyshops.enduserappnew.Lists.CartItemList.Backups;

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

import androidx.appcompat.app.AlertDialog;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 6/6/16.
 */
public class AdapterBackup extends RecyclerView.Adapter<AdapterBackup.ViewHolder>{


    private List<CartItem> dataset = null;
    private Context context;
    private NotifyCartItem notifyCartItem;

    private CartStats cartStats;
    private double cartTotal = 0;



    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;








    public AdapterBackup(List<CartItem> dataset, Context context, NotifyCartItem notifyCartItem) {

        this.dataset = dataset;
        this.context = context;
        this.notifyCartItem = notifyCartItem;
//        this.cartStats = cartStats;


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


    void setCartStats(CartStats cartStats)
    {
        this.cartStats = cartStats;
        cartTotal = cartStats.getCart_Total();
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart_item_new,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CartItem cartItem = dataset.get(position);

//        String imagePath = null;

        Item item = null;

        if(cartItem!=null)
        {
            item = cartItem.getItem();
        }

        if(item != null)
        {
            holder.itemName.setText(item.getItemName());

            holder.itemQuantity.setText(UtilityFunctions.refinedString(cartItem.getItemQuantity()));
            holder.itemPrice.setText("Price : " + String.format( "%.2f", cartItem.getRt_itemPrice()) + " per " + item.getQuantityUnit());

            //holder.itemTotal.setText(" x " + cartItem.getRt_availableItemQuantity() + " (Unit Price) = "
            //        + "Rs:" +  String.format( "%.2f", cartItem.getRt_itemPrice()*cartItem.getItemQuantity()));


            holder.itemTotal.setText("Total : "  + PrefGeneral.getCurrencySymbol(context) + " "
                    + String.format( "%.2f", cartItem.getRt_itemPrice()*cartItem.getItemQuantity()));

            holder.itemsAvailable.setText("Available : " + cartItem.getRt_availableItemQuantity() + " " + item.getQuantityUnit());



//            imagePath = UtilityGeneral.getImageEndpointURL(MyApplicationCoreNew.getAppContext())
//                    + item.getItemImageURL();

            String imagePath = PrefGeneral.getServiceURL(context)
                    + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());



            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.shopImage);
        }
    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {

        ImageView shopImage;
        TextView rating;
        TextView itemName;
        TextView itemsAvailable;
        TextView itemPrice;
        TextView itemTotal;
        ImageView increaseQuantity;
        ImageView reduceQuantity;
        EditText itemQuantity;
        TextView updateButton;
        TextView removeButton;
        ProgressBar progressBarRemove;
        ProgressBar progressBarUpdate;


        public ViewHolder(View itemView) {
            super(itemView);

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

            itemQuantity.addTextChangedListener(this);
            reduceQuantity.setOnClickListener(this);
            increaseQuantity.setOnClickListener(this);
            updateButton.setOnClickListener(this);
            removeButton.setOnClickListener(this);

            progressBarRemove = itemView.findViewById(R.id.progress_bar_remove);
            progressBarUpdate = itemView.findViewById(R.id.progress_bar_update);

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



        public void removeClick()
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




        public void removeItem()
        {

            progressBarRemove.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.INVISIBLE);

            final CartItem cartItem = dataset.get(getLayoutPosition());

            Call<ResponseBody> call = cartItemService.deleteCartItem(cartItem.getCartID(),cartItem.getItemID(),0,0);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code() == 200)
                    {
                        showToastMessage("Item Removed");

                        // refresh the list
//                        makeNetworkCall();
                        notifyCartItem.notifyRemove(dataset.get(getLayoutPosition()));
                    }


                    notifyItemRemoved(getLayoutPosition());
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




        public void updateClick()
        {

//            notifyCartItem.notifyUpdate(dataset.get(getLayoutPosition()));

            progressBarUpdate.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.INVISIBLE);

            final CartItem cartItem = dataset.get(getLayoutPosition());

            Call<ResponseBody> call = cartItemService.updateCartItem(cartItem,0,0);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code() == 200)
                    {
                        notifyCartItem.notifyUpdate(cartItem);
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



        public void reduceQuantityClick()
        {
            setFilter();

            CartItem cartItem = dataset.get(getLayoutPosition());

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








        public void increaseQuantityClick()
        {
            setFilter();

            CartItem cartItem = dataset.get(getLayoutPosition());

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





        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            setFilter();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            CartItem cartItem;

            cartItem = dataset.get(getLayoutPosition());

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




            notifyCartItem.notifyTotal(cartTotal);

                    //cartTotal.setText("Cart Total : Rs " + String.format( "%.2f", total));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }







        void setFilter() {

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

    }






    public interface NotifyCartItem{
        void notifyUpdate(CartItem cartItem);
        void notifyRemove(CartItem cartItem);
        void notifyTotal(double total);
    }





    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

}
