package org.nearbyshops.whitelabelapp.Admin.ViewHolders;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.Admin.ViewModel.ViewModelShopForAdmin;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelShop;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewHolderShopForAdmin extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {



    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_address) TextView shopAddress;
    @BindView(R.id.shop_logo) ImageView shopLogo;
    @BindView(R.id.delivery) TextView delivery;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView rating_count;
    @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
    @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;


    @BindView(R.id.more_options) ImageView adminModeEnabled;



    @BindView(R.id.button_left) TextView buttonLeft;
    @BindView(R.id.progress_left) ProgressBar progressLeft;

    @BindView(R.id.button_right) TextView buttonRight;
    @BindView(R.id.progress_right) ProgressBar progressRight;


    private Shop shop;
    private Context context;
    private Fragment fragment;
    private int screenMode;

    private List<Object> dataset;

    private RecyclerView.Adapter adapter;


    private ViewModelShop viewModelShop;
    private ViewModelShopForAdmin viewModelShopForAdmin;


    public static int LAYOUT_TYPE_SIDE_BY_SIDE = 1;
    public static int LAYOUT_TYPE_UBER_EATS = 2;






    public static ViewHolderShopForAdmin create(ViewGroup parent, Context context, Fragment fragment, List<Object> dataset,
                                                RecyclerView.Adapter adapter, int screenMode)
    {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_for_admin,parent,false);

        return new ViewHolderShopForAdmin(view,context,fragment,dataset,adapter,screenMode);
    }






    public ViewHolderShopForAdmin(@NonNull View itemView, Context context, Fragment fragment, List<Object> dataset,
                                  RecyclerView.Adapter adapter, int screenMode) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
        this.screenMode = screenMode;
        this.dataset = dataset;

//        setupButtons();
        setupViewModel();
        setupViewModelShopForAdmin();
    }



    void setupViewModel()
    {
        viewModelShop = new ViewModelShop(MyApplication.application);

        viewModelShop.getEvent().observe(fragment.getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer eventCode) {

                if(eventCode == ViewModelShop.EVENT_SHOP_DELETED)
                {
                    dataset.remove(getLayoutPosition());
                    ViewHolderShopForAdmin.this.adapter.notifyItemRemoved(getLayoutPosition());
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


    void setupViewModelShopForAdmin()
    {

        viewModelShopForAdmin = new ViewModelShopForAdmin(MyApplication.application);

        viewModelShopForAdmin.getEvent().observe(fragment.getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer eventCode) {


                if(eventCode == ViewModelShopForAdmin.EVENT_SHOP_ADDED_TO_MARKET
                        || eventCode == ViewModelShopForAdmin.EVENT_SHOP_ADDED_TO_MARKET_BY_REQUEST
                        || eventCode == ViewModelShopForAdmin.EVENT_SHOP_REMOVED_FROM_MARKET)

                {
                    dataset.remove(getLayoutPosition());
                    ViewHolderShopForAdmin.this.adapter.notifyItemRemoved(getLayoutPosition());
                }


                buttonLeft.setVisibility(View.VISIBLE);
                progressLeft.setVisibility(View.INVISIBLE);

//                buttonRight.setVisibility(View.VISIBLE);
//                progressRight.setVisibility(View.INVISIBLE);

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
            ((ListItemClick) fragment).listItemClick(shop,getLayoutPosition());
        }
    }





    public void setItem(Shop shop)
    {
        this.shop = shop;


//        if(adminModeEnabled)
//        {
//            this.adminModeEnabled.setVisibility(View.VISIBLE);
//        }



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




            String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/Shop/Image/five_hundred_"
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


        setupButtons();

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
                    ((ListItemClick) fragment).editClick(shop,getLayoutPosition());
                }

                break;



            default:

                break;

        }




        return false;

    }



    void setupButtons()
    {
        String buttonLeftTitle = "";
        String buttonRightTitle = "";


        buttonLeft.setVisibility(View.GONE);
        buttonRight.setVisibility(View.GONE);



        if(shop.getRegistrationStatus()==Shop.STATUS_NEW_REGISTRATION)
        {

            buttonLeft.setText("Enable Shop");
            buttonLeft.setVisibility(View.VISIBLE);

            buttonRight.setText("Disable Shop");
            buttonRight.setVisibility(View.VISIBLE);

        }
        else if(shop.getRegistrationStatus()==Shop.STATUS_SHOP_ENABLED)
        {
            buttonLeft.setText("Disable Shop");
            buttonLeft.setVisibility(View.VISIBLE);

            buttonRight.setVisibility(View.GONE);
        }
        else if(shop.getRegistrationStatus()==Shop.STATUS_DISABLED)
        {
            buttonLeft.setText("Enable Shop");
            buttonLeft.setVisibility(View.VISIBLE);

            buttonRight.setVisibility(View.GONE);
        }

    }




    @OnClick(R.id.button_left)
    void leftButtonClick()
    {
        if(shop.getRegistrationStatus()==Shop.STATUS_NEW_REGISTRATION)
        {
            viewModelShopForAdmin.enableShop(shop.getShopID());
        }
        else if(shop.getRegistrationStatus()==Shop.STATUS_SHOP_ENABLED)
        {
            viewModelShopForAdmin.disableShop(shop.getShopID());
        }
        else if(shop.getRegistrationStatus()==Shop.STATUS_DISABLED)
        {
            viewModelShopForAdmin.enableShop(shop.getShopID());
        }



        buttonLeft.setVisibility(View.INVISIBLE);
        progressLeft.setVisibility(View.VISIBLE);
    }




    @OnClick(R.id.button_right)
    void rightButtonClick()
    {

        if(shop.getRegistrationStatus()==Shop.STATUS_NEW_REGISTRATION)
        {
            viewModelShopForAdmin.enableShop(shop.getShopID());
        }

        buttonRight.setVisibility(View.INVISIBLE);
        progressRight.setVisibility(View.VISIBLE);
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
