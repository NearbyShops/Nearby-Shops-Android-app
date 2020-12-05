package org.nearbyshops.enduserappnew.aSellerModule.ViewHolders;

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

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.API.ShopItemService;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
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


public class ViewHolderShopItemSeller extends RecyclerView.ViewHolder{


    @Inject
    ShopItemService shopItemService;


    @BindView(R.id.itemName)
    TextView itemName;

//        @BindView(R.id.itemDescriptionShort)
//        TextView itemDescriptionShort;

    @BindView(R.id.itemImage)
    ImageView itemImage;

    @BindView(R.id.availableText)
    TextView availableText;

    @BindView(R.id.reduceQuantity)
    ImageView reduceQuantity;

    @BindView(R.id.itemQuantity)
    EditText itemQuantity;

    @BindView(R.id.increaseQuantity)
    ImageView increaseQuantity;


    @BindView(R.id.allow_quarter_quantity)
    TextView allowQuarterQuantity;

    @BindView(R.id.allow_half_quantity)
    TextView allowHalfQuantity;


    @BindView(R.id.priceText)
    TextView priceText;

    @BindView(R.id.reducePrice)
    ImageView reducePrice;

    @BindView(R.id.itemPrice)
    EditText itemPrice;

    @BindView(R.id.increasePrice)
    ImageView increasePrice;

    @BindView(R.id.removeButton) TextView removeButton;
    @BindView(R.id.progress_bar_remove) ProgressBar progressBarRemove;


    @BindView(R.id.updateButton) TextView updateButton;
    @BindView(R.id.progress_bar) ProgressBar progressBarUpdate;

    @BindView(R.id.discount_indicator) TextView discountIndicator;





    private Context context;
    private Item item;
    private ShopItem shopItem;
    private Fragment fragment;


    private RecyclerView.Adapter adapter;
    private List<Object> dataset = null;






    public static ViewHolderShopItemSeller create(ViewGroup parent, Context context, Fragment fragment,
                                                  RecyclerView.Adapter adapter, List<Object> dataset

    )
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_item_seller,parent,false);
        return new ViewHolderShopItemSeller(view,context,fragment,adapter,dataset);
    }







    public ViewHolderShopItemSeller(View itemView, Context context, Fragment fragment, RecyclerView.Adapter adapter, List<Object> dataset) {
        super(itemView);


        ButterKnife.bind(this,itemView);

        itemQuantity.addTextChangedListener(new QuantityTextWatcher());
        itemPrice.addTextChangedListener(new PriceTextWatcher());


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);


        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
        this.dataset = dataset;
    }









    class QuantityTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


            if(!itemQuantity.getText().toString().equals(""))
            {

                String availableLocal = String.valueOf(Integer.parseInt(itemQuantity.getText().toString()));


                if(item!=null)
                {
                    availableText.setText("Available : " + availableLocal + " " + item.getQuantityUnit());

                }else
                {
                    availableText.setText("Available : " + availableLocal + " Items");
                }

            }else
            {
                availableText.setText("Available : " + "0" + " Items");
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }






    @OnClick(R.id.reduceQuantity)
    void reduceQuantityClick(View view)
    {

        if(!itemQuantity.getText().toString().equals(""))
        {

            if(Integer.parseInt(itemQuantity.getText().toString())<=0)
            {
                return;
            }


            String availableLocal = String.valueOf(Integer.parseInt(itemQuantity.getText().toString())-1);

            itemQuantity.setText(availableLocal);


            if(item!=null)
            {
                availableText.setText("Available : " + availableLocal + " " + item.getQuantityUnit());

            }else
            {
                availableText.setText("Available : " + availableLocal + " Items");
            }



        }else
        {
            itemQuantity.setText(String.valueOf(0));
        }

    }







    @OnClick(R.id.increaseQuantity)
    void increaseQuantityClick(View view)
    {

        if(!itemQuantity.getText().toString().equals(""))
        {

            String availableLocal = String.valueOf(Integer.parseInt(itemQuantity.getText().toString())+1);

            itemQuantity.setText(availableLocal);


            if(item!=null)
            {
                availableText.setText("Available : " + availableLocal + " " + item.getQuantityUnit());

            }else
            {
                availableText.setText("Available : " + availableLocal + " Items");
            }


        }else
        {
            itemQuantity.setText(String.valueOf(0));
        }

    }



    // methods for setting item Price



    class PriceTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

//            if(!itemPrice.getText().toString().equals(""))
//            {
//
//                String priceLocal = String.valueOf(Double.parseDouble(itemPrice.getText().toString()));
//
//                if(item!=null)
//                {
//                    priceText.setText("Price : " + priceLocal + " per " + item.getQuantityUnit());
//
//                }else
//                {
//                    priceText.setText("Price : " + priceLocal + " per Item ");
//                }
//
//
//            }else
//            {
//
//                if(item!=null)
//                {
//                    priceText.setText("Price : " + "0" + " per " + item.getQuantityUnit());
//
//                }else
//                {
//                    priceText.setText("Price : " + "0" + " per Item ");
//                }
//
//            }



        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }



    @OnClick(R.id.reducePrice)
    void reducePriceClick(View view)
    {

        if(!itemPrice.getText().toString().equals(""))
        {

            if(Double.parseDouble(itemPrice.getText().toString())<=0)
            {
                return;
            }


            String priceLocal = String.valueOf(Double.parseDouble(itemPrice.getText().toString())-1);

            itemPrice.setText(priceLocal);

            bindDiscount();


//            if(item!=null)
//            {
//                priceText.setText("Price : " + priceLocal + " per " + item.getQuantityUnit());
//
//            }else
//            {
//                priceText.setText("Price : " + priceLocal + " per Item ");
//            }




        }else
        {
            itemPrice.setText(String.valueOf(0));
        }

    }







    @OnClick(R.id.increasePrice)
    void increasePriceClick(View view)
    {

        if(!itemPrice.getText().toString().equals(""))
        {

            String priceLocal = String.valueOf(Double.parseDouble(itemPrice.getText().toString())+1);

            if(Double.parseDouble(priceLocal)>item.getListPrice()  && item.getListPrice()>0)
            {
                showToastMessage("Price cannot be greater then MRP");
                return;
            }

            itemPrice.setText(priceLocal);

            bindDiscount();



//            if(item!=null)
//            {
//                priceText.setText("Price : " + priceLocal + " per " + item.getQuantityUnit());
//
//            }else
//            {
//                priceText.setText("Price : " + priceLocal + " per Item ");
//            }


        }else
        {
            itemPrice.setText(String.valueOf(0));
        }

    }






    // Update and remove methods

    @OnClick(R.id.updateButton)
    void updateClick(View view)
    {

        if(!itemQuantity.getText().toString().equals("") && !itemPrice.getText().toString().equals(""))
        {

            int quantityLocal = Integer.parseInt(itemQuantity.getText().toString());
            double priceLocal = Double.parseDouble(itemPrice.getText().toString());

                /*
                if(quantityLocal <= 0 || priceLocal<=0)
                {
                    showToastMessage("Quantity or Price not set !");

                    return;
                }
                */

            shopItem.setAvailableItemQuantity(quantityLocal);
            shopItem.setItemPrice(priceLocal);

            bindDiscount();



            int shopID = PrefShopAdminHome.getShop(context).getShopID();





            Call<ResponseBody> call = shopItemService.putShopItem(
                    PrefLogin.getAuthorizationHeaders(context),
                    shopID,
                    shopItem
            );



            updateButton.setVisibility(View.INVISIBLE);
            progressBarUpdate.setVisibility(View.VISIBLE);


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {


                        if(fragment instanceof ShopItemUpdates)
                        {
                            ((ShopItemUpdates) fragment).notifyShopItemUpdated(shopItem);
                        }




                        if(shopItem.getItem()!=null)
                        {
                            showToastMessage(shopItem.getItem().getItemName() + " Updated !");

                        }else
                        {
                            showToastMessage("Update Successful !");
                        }
                    }
                    else if(response.code() == 403)
                    {
                        showToastMessage("Not permitted !");
                    }
                    else if(response.code() == 401)
                    {
                        showToastMessage("We are not able to identify you !");
                    }
                    else
                    {
                        showToastMessage("Failed Code : " + response.code());
                    }



                    updateButton.setVisibility(View.VISIBLE);
                    progressBarUpdate.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Network Request Failed. Try again !");

                    updateButton.setVisibility(View.VISIBLE);
                    progressBarUpdate.setVisibility(View.INVISIBLE);
                }
            });

        }
        else
        {
            showToastMessage("Quantity or Price not set !");
        }

    }






    @OnClick(R.id.removeButton)
    void removeClick(View view)
    {



        removeButton.setVisibility(View.INVISIBLE);
        progressBarRemove.setVisibility(View.VISIBLE);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("Confirm Remove Item !")
                .setMessage("Do you want to remove this item from your shop !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        removeButton.setVisibility(View.VISIBLE);
                        progressBarRemove.setVisibility(View.INVISIBLE);


                        removeShopItem(shopItem);

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        removeButton.setVisibility(View.VISIBLE);
                        progressBarRemove.setVisibility(View.INVISIBLE);

                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }








    private void removeShopItem(final ShopItem shopItem)
    {

        Call<ResponseBody> responseBodyCall = shopItemService.deleteShopItem(
                PrefLogin.getAuthorizationHeaders(context),
                shopItem.getShopID(),
                shopItem.getItemID()
        );


        removeButton.setVisibility(View.INVISIBLE);
        progressBarRemove.setVisibility(View.VISIBLE);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    if(shopItem.getItem()!=null)
                    {
                        showToastMessage(shopItem.getItem().getItemName() + " Removed !");

                    }else
                    {
                        showToastMessage("Successful !");
                    }


//                    int removedPosition = dataset.indexOf(shopItem);


                    if(fragment instanceof ShopItemUpdates)
                    {
                        ((ShopItemUpdates) fragment).notifyShopItemRemoved(shopItem);
                        adapter.notifyItemRemoved(getAdapterPosition());
                        dataset.remove(shopItem);
                    }




//                    dataset.remove(shopItem);
//                    notifyItemRemoved(removedPosition);



//                        offset = offset - 1;
//                        item_count = item_count -1;
//                        notifyTitleChanged();
                }
                else if(response.code() == 304) {

                    showToastMessage("Not removed !");

                }
                else if(response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else if(response.code() == 401)
                {
                    showToastMessage("We are not able to identify you !");
                }
                else
                {
                    showToastMessage("Server Error !");
                }



                removeButton.setVisibility(View.VISIBLE);
                progressBarRemove.setVisibility(View.INVISIBLE);

//                makeRefreshNetworkCall();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network request failed !");


                removeButton.setVisibility(View.VISIBLE);
                progressBarRemove.setVisibility(View.INVISIBLE);

            }
        });
    }





    public void setShopItem(ShopItem shopItem)
    {

            this.shopItem = shopItem;
            this.item  = shopItem.getItem();


            itemName.setText(item.getItemName());

//            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext()) + item.getItemImageURL();

            String imagePath = PrefGeneral.getServiceURL(context)
                    + "/api/v1/Item/Image/five_hundred_" + item.getItemImageURL() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(itemImage);


            //holder.availableText.setText("Available : " + shopItem.getAvailableItemQuantity() + " Items");
            //holder.priceText.setText("Price : " + shopItem.getItemPrice());


            itemPrice.setText(String.valueOf(shopItem.getItemPrice()));
            itemQuantity.setText(String.valueOf(shopItem.getAvailableItemQuantity()));

            //holder.itemPrice.setText(String.format( "%.0f", shopItem.getItemPrice()));
            //holder.itemQuantity.setText(String.format( "%.0f", shopItem.getAvailableItemQuantity()));



            if(item!=null)
            {
                availableText.setText("Available : " + shopItem.getAvailableItemQuantity() + " " + item.getQuantityUnit());
//                priceText.setText("Price : " + shopItem.getItemPrice() + " per " + item.getQuantityUnit());

                if(item.getListPrice()>0)
                {
                    priceText.setText("MRP : " + UtilityFunctions.refinedStringWithDecimals(item.getListPrice()) + " per " + item.getQuantityUnit());
                }
                else
                {
                    priceText.setText("MRP not set");
                }


            }else
            {
                availableText.setText("Available : " + shopItem.getAvailableItemQuantity() + " Items");
//                priceText.setText("Price : " + shopItem.getItemPrice() + " per Item");
                priceText.setText("MRP : " + UtilityFunctions.refinedStringWithDecimals(item.getListPrice()) + " per " + item.getQuantityUnit());
            }







            bindDiscount();
        bindQuarterQuantity();
            bindHalfQuantity();
    }




    void bindDiscount()
    {

        double priceLocal = Double.parseDouble(itemPrice.getText().toString());
        shopItem.setItemPrice(priceLocal);


        if(item.getListPrice()>0.0 && item.getListPrice()>shopItem.getItemPrice())
        {
            double discountPercent = ((item.getListPrice() - shopItem.getItemPrice())/item.getListPrice())*100;
            discountIndicator.setText(String.format("%.0f ",discountPercent) + " %\nOff");

            discountIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            discountIndicator.setVisibility(View.GONE);
        }
    }





    private void bindQuarterQuantity()
    {

        if(shopItem.isAllowQuarterQuantity())
        {
            allowQuarterQuantity.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
            allowQuarterQuantity.setTextColor(ContextCompat.getColor(context,R.color.white));
        }
        else
        {
            allowQuarterQuantity.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
            allowQuarterQuantity.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        }

    }





    private void bindHalfQuantity()
    {

        if(shopItem.isAllowHalfQuantity())
        {
            allowHalfQuantity.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
            allowHalfQuantity.setTextColor(ContextCompat.getColor(context,R.color.white));
        }
        else
        {
            allowHalfQuantity.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
            allowHalfQuantity.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        }

    }






    @OnClick(R.id.allow_half_quantity)
    void halfQuantityClick()
    {
        shopItem.setAllowHalfQuantity(!shopItem.isAllowHalfQuantity());

        bindHalfQuantity();
    }



    @OnClick(R.id.allow_quarter_quantity)
    void quarterQuantityClick()
    {
        shopItem.setAllowQuarterQuantity(!shopItem.isAllowQuarterQuantity());

        bindQuarterQuantity();
    }








    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(context,message);
    }




    public interface ShopItemUpdates{

        void notifyShopItemUpdated(ShopItem shopItem);
        void notifyShopItemRemoved(ShopItem shopItem);

    }


}


