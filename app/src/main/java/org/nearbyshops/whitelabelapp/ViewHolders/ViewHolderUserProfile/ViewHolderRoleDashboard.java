package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUserProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Admin.AdminDashboardBottom;
import org.nearbyshops.whitelabelapp.AdminShop.ButtonDashboard.DashboardShopAdmin.ShopAdminHome;
import org.nearbyshops.whitelabelapp.AdminShop.ButtonDashboard.DashboardShopStaff.ShopDashboardForStaff;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShop;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.whitelabelapp.AdminDelivery.InventoryDeliveryPerson.DeliveryPersonInventory;
import org.nearbyshops.whitelabelapp.AdminDelivery.ButtonDashboard.FragmentDeprecated.DeliveryInventoryFragment;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelShop;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class
ViewHolderRoleDashboard extends RecyclerView.ViewHolder{




    @BindView(R.id.dashboard_name) TextView dashboardName;
    @BindView(R.id.market_name) TextView marketName;
    @BindView(R.id.dashboard_description) TextView dashboardDescription;




    private Context context;
    private Fragment fragment;
    private ViewModelShop viewModelShop;


    private ProgressDialog progressDialog;





    public static ViewHolderRoleDashboard create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_role_dashboard,parent,false);
        return new ViewHolderRoleDashboard(view,context,fragment);
    }





    public ViewHolderRoleDashboard(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


        bindDashboard();



//        viewModelShop = ViewModelProviders.of(fragment).get(ViewModelShop.class);

        viewModelShop = new ViewModelShop(MyApplication.application);


        viewModelShop.getShopLive().observe(fragment, new Observer<Shop>() {
            @Override
            public void onChanged(Shop shop) {

                PrefShopAdminHome.saveShop(shop,context);

                Intent intent = new Intent(context, ShopDashboardForStaff.class);
                context.startActivity(intent);
            }
        });



        viewModelShop.getEvent().observe(fragment, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }



                if(integer == ViewModelShop.EVENT_BECOME_A_SELLER_SUCCESSFUL)
                {
                    bindDashboard();
                }

            }
        });




        viewModelShop.getMessage().observe(fragment, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage(s);

                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }
            }
        });


    }






    @OnClick(R.id.dashboard_by_role)
    public void dashboardClick()
    {

        User user = PrefLogin.getUser(context);

        if(user==null)
        {
            return;
        }


        if(user.getRole()==User.ROLE_ADMIN_CODE)
        {
            Intent intent = new Intent(context, AdminDashboardBottom.class);
            context.startActivity(intent);
        }
        else if(user.getRole()==User.ROLE_STAFF_CODE)
        {
//            Intent intent = new Intent(context, MarketStaffDashboard.class);
//            context.startActivity(intent);

            showToastMessage("Dashboard under development !");

        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE)
        {
            Intent intent = new Intent(context, DeliveryPersonInventory.class);
            intent.putExtra(DeliveryInventoryFragment.SCREEN_MODE_INTENT_KEY,DeliveryInventoryFragment.SCREEN_MODE_DELIVERY_PERSON_MARKET);
            context.startActivity(intent);
        }
        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
        {

            Intent intent = new Intent(context, ShopAdminHome.class);
            context.startActivity(intent);
        }
        else if(user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {

            viewModelShop.getShopForShopDashboard();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.get_shop_details));
            progressDialog.show();

        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE)
        {
            Intent intent = new Intent(context, DeliveryPersonInventory.class);
            intent.putExtra(DeliveryInventoryFragment.SCREEN_MODE_INTENT_KEY,DeliveryInventoryFragment.SCREEN_MODE_DELIVERY_PERSON_VENDOR);
            context.startActivity(intent);
//            DeliveryPersonInventory.start(DeliveryInventoryFragment.SCREEN_MODE_DELIVERY_PERSON_VENDOR,context);

        }
        else if(user.getRole()==User.ROLE_END_USER_CODE)
        {

            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            dialog.setTitle(context.getString(R.string.create_shop))
                    .setMessage(context.getString(R.string.become_shop_seller))
                    .setPositiveButton(context.getString(R.string.yes),new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                            viewModelShop.becomeASeller();
//                            progressDialog = new ProgressDialog(context);
//                            progressDialog.setMessage("Please wait ... converting you to a seller !");
//                            progressDialog.show();




                            //     open edit shop in edit mode
                            Intent intent = new Intent(context, EditShop.class);
                            intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_ADD);
                            fragment.startActivityForResult(intent,890);

                        }
                    })
                    .setNegativeButton(context.getString(R.string.no),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            showToastMessage(context.getString(R.string.cancelled) +" !");
                        }
                    })
                    .show();

        }

    }







    public void bindDashboard()
    {
        User user = PrefLogin.getUser(context);


        if(user==null)
        {
            return;
        }


        String marketNameString = "";


        if(user.getRole()==User.ROLE_ADMIN_CODE)
        {
            marketName.setText(marketNameString);
            dashboardName.setText(context.getString(R.string.admin_dashboard));
            dashboardDescription.setText(context.getString(R.string.access_admin));
        }
        else if(user.getRole()==User.ROLE_STAFF_CODE)
        {
            marketName.setText(marketNameString);
            dashboardName.setText("Staff Dashboard");
            dashboardDescription.setText("Press here to access the staff dashboard !");
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE)
        {
            marketName.setText(marketNameString);
            dashboardName.setText("Delivery Dashboard");
            dashboardDescription.setText("Press here to access the delivery Dashboard !");

        }
        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
        {
            marketName.setText(marketNameString);
            dashboardName.setText(context.getString(R.string.shop_dashboard));
            dashboardDescription.setText(context.getString(R.string.access_shop));
        }
        else if(user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {
            marketName.setText(marketNameString);
            dashboardName.setText(context.getString(R.string.staff_dashboard));
            dashboardDescription.setText(context.getString(R.string.access_staff));
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE)
        {
            marketName.setText(marketNameString);
            dashboardName.setText(context.getString(R.string.delivery_dashboard));
            dashboardDescription.setText(context.getString(R.string.access_delivery));

        }
        else if(user.getRole()==User.ROLE_END_USER_CODE)
        {
            marketName.setText(marketNameString);

            dashboardName.setText("Create your Shop in " + marketNameString);
            dashboardDescription.setText(" ... Press here to create your shop !");
        }

    }




    void listItemClick()
    {
        ((ListItemClick)fragment).listItemClick();
    }




    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }







    public interface ListItemClick
    {
        void listItemClick();
    }



}

