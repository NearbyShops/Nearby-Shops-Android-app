package org.nearbyshops.enduserappnew.ViewHolders;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelShop;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewHolderShopSmall extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_address) TextView shopAddress;
    @BindView(R.id.shop_logo) ImageView shopLogo;
    @BindView(R.id.delivery) TextView delivery;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView rating_count;
    @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
    @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;


    @BindView(R.id.more_options) ImageView moreOptions;


    private Shop shop;
    private Context context;
    private Fragment fragment;

    private RecyclerView.Adapter adapter;


    private ViewModelShop viewModelShop;






    public static ViewHolderShopSmall create(ViewGroup parent, Context context, Fragment fragment,
                                             RecyclerView.Adapter adapter)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_new,parent,false);
        return new ViewHolderShopSmall(view,context,fragment, adapter);
    }






    public ViewHolderShopSmall(@NonNull View itemView, Context context, Fragment fragment, RecyclerView.Adapter adapter) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;

        viewModelShop = new ViewModelShop(MyApplication.application);



        viewModelShop.getEvent().observe(fragment.getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {


                if(integer == ViewModelShop.EVENT_SHOP_DELETED)
                {
                    ViewHolderShopSmall.this.adapter.notifyItemRemoved(getAdapterPosition());
                }

            }
        });




        viewModelShop.getMessage().observe(fragment. getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage(s);
            }
        });


    }







    @OnClick(R.id.list_item_shop)
    void listItemClick()
    {
//        Intent shopHomeIntent = new Intent(context, ShopDashboard.class);
//        context.startActivity(shopHomeIntent);

        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemClick(shop,getAdapterPosition());
        }
    }





    public void setItem(Shop shop, boolean moreOptionsEnabled)
    {

        this.shop = shop;


        if(moreOptionsEnabled)
        {
            moreOptions.setVisibility(View.VISIBLE);
        }



        if(shop!=null)
        {

            shopName.setText(shop.getShopName());


            if(shop.getPickFromShopAvailable())
            {
                pickFromShopIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                pickFromShopIndicator.setVisibility(View.GONE);
            }



            if(shop.getHomeDeliveryAvailable())
            {
                homeDeliveryIndicator.setVisibility(View.VISIBLE);

            }
            else
            {
                homeDeliveryIndicator.setVisibility(View.GONE);
            }


//            delivery.setVisibility(View.VISIBLE);
            shopAddress.setText(String.format( "%.2f", shop.getRt_distance()) + " Km - " + shop.getShopAddress());
//            delivery.setText(shop.getShopAddress());




            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                    + shop.getLogoImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                shopLogo.setClipToOutline(true);
            }



            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(shopLogo);



            if(shop.getRt_rating_count()==0)
            {
//                    holder.rating.setText("N/A");
                rating.setText(" New ");
                rating.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
                rating_count.setText("( Not Yet Rated )");
                rating_count.setVisibility(View.GONE);

            }
            else
            {
                rating_count.setVisibility(View.VISIBLE);
                rating.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));
                rating.setText(String.format("%.2f",shop.getRt_rating_avg()));
                rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
            }

        }


    }







    @OnClick(R.id.more_options)
    void optionsOverflowClick(View v)
    {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.list_item_shop_admin_options, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }







    @Override
    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.action_remove:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirm Delete Shop !")
                        .setMessage("Are you sure you want to delete this Shop ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                viewModelShop.deleteShop(shop.getShopID());

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                showToastMessage("Cancelled !");

                            }
                        })
                        .show();
                break;


            case R.id.action_edit:


                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick) fragment).editClick(shop,getAdapterPosition());
                }

                break;



            default:

                break;

        }




        return false;

    }










    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(context,message);
    }




    public interface ListItemClick
    {
        void listItemClick(Shop shop, int position);
        void editClick(Shop shop, int position);
    }

}
