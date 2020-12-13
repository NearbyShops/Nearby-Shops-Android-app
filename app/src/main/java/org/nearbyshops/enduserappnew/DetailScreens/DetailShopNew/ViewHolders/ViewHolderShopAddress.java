package org.nearbyshops.enduserappnew.DetailScreens.DetailShopNew.ViewHolders;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.DetailScreens.DetailShopNew.Model.ShopAddress;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderShopAddress extends RecyclerView.ViewHolder{




    @BindView(R.id.shop_phone) TextView shopPhone;
    @BindView(R.id.shop_directions) TextView shopDirections;


    int vibrant;
    int vibrantDark;

    private Context context;
    private Shop shop;
    private Fragment fragment;


    public static ViewHolderShopAddress create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_address, parent, false);
        return new ViewHolderShopAddress(view, context, fragment);
    }





    public ViewHolderShopAddress(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


    }







    public void setShop(ShopAddress shopAddress) {
        this.shop = shopAddress.getShop();

        if(shop.getCustomerHelplineNumber()!=null)
        {
            shopPhone.setText(shop.getCustomerHelplineNumber());
        }

        shopDirections.setText(shop.getShopAddress());
    }





    public void setColors(int vibrant, int vibrantDark)
    {
        this.vibrant = vibrant;
        this.vibrantDark = vibrantDark;
    }







    @OnClick(R.id.shop_directions)
    void getDirectionsPickup()
    {
        getDirections(shop.getLatCenter(),shop.getLonCenter());
    }





    @OnClick(R.id.shop_phone)
    void shopPhoneClick()
    {
        dialPhoneNumber(shopPhone.getText().toString());
    }





    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }




    private void getDirections(double lat, double lon)
    {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }



    private void seeOnMap(double lat, double lon, String label)
    {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lon + "(" + label + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }




}
