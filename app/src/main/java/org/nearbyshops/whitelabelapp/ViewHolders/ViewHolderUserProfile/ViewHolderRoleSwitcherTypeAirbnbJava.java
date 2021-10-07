package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUserProfile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Admin.AdminDashboardBottom;
import org.nearbyshops.whitelabelapp.LaunchActivity;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.databinding.ListItemRoleSwitcherBinding;


public class ViewHolderRoleSwitcherTypeAirbnbJava extends RecyclerView.ViewHolder{


    ListItemRoleSwitcherBinding binding = ListItemRoleSwitcherBinding.bind(itemView);


    private Context context;
    private Fragment fragment;





    public static ViewHolderRoleSwitcherTypeAirbnbJava create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_role_switcher,parent,false);
        return new ViewHolderRoleSwitcherTypeAirbnbJava(view,context,fragment);
    }





    public ViewHolderRoleSwitcherTypeAirbnbJava(View itemView, Context context, Fragment fragment) {
        super(itemView);

        this.context = context;
        this.fragment = fragment;

        bindDashboard();
    }





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
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE || user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE)
        {


            int launchScreen = PrefAppSettings.getLaunchScreen(context);

            if(launchScreen==PrefAppSettings.LAUNCH_SCREEN_MAIN)
            {
                PrefAppSettings.setLaunchScreen(PrefAppSettings.LAUNCH_SCREEN_DELIVERY,context);
                startLaunchScreen();


            }
            else
            {
                PrefAppSettings.setLaunchScreen(PrefAppSettings.LAUNCH_SCREEN_MAIN,context);
                startLaunchScreen();
            }


        }
        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE || user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {

            int launchScreen = PrefAppSettings.getLaunchScreen(context);

            if(launchScreen==PrefAppSettings.LAUNCH_SCREEN_MAIN)
            {
                PrefAppSettings.setLaunchScreen(PrefAppSettings.LAUNCH_SCREEN_SHOP_ADMIN,context);
                startLaunchScreen();


            }
            else
            {
                PrefAppSettings.setLaunchScreen(PrefAppSettings.LAUNCH_SCREEN_MAIN,context);
                startLaunchScreen();

            }
        }

    }

    private void startLaunchScreen() {
        Intent intent = new Intent(context, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (fragment.getActivity() != null) {
            fragment.getActivity().finish();
        }

        context.startActivity(intent);
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
            binding.dashboardName.setText(context.getString(R.string.admin_dashboard));
            binding.dashboardDescription.setText(context.getString(R.string.access_admin));
        }
        else if(user.getRole()==User.ROLE_STAFF_CODE)
        {
            binding.dashboardName.setText("Staff Dashboard");
            binding.dashboardDescription.setText("Press here to access the staff dashboard !");
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE || user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE)
        {

            binding.dashboardDescription.setText("Press here to access the delivery Dashboard !");

            int launchScreen = PrefAppSettings.getLaunchScreen(context);

            if(launchScreen==PrefAppSettings.LAUNCH_SCREEN_MAIN)
            {
                binding.dashboardName.setText("Switch to Delivery");
            }
            else
            {
                binding.dashboardName.setText("Switch to Customer");
            }

        }
        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE || user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {
            binding.dashboardDescription.setText(context.getString(R.string.access_shop));

            int launchScreen = PrefAppSettings.getLaunchScreen(context);

            if(launchScreen==PrefAppSettings.LAUNCH_SCREEN_MAIN)
            {
                binding.dashboardName.setText("Switch to Shop");
            }
            else
            {
                binding.dashboardName.setText("Switch to Customer");
            }
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

