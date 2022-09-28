package org.nearbyshops.whitelabelapp.zDeprecatedScreens;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.API.UserService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShop;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.whitelabelapp.AdminDelivery.InventoryDeliveryPerson.DeliveryPersonInventory;
import org.nearbyshops.whitelabelapp.AdminDelivery.ButtonDashboard.FragmentDeprecated.DeliveryInventoryFragment;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyAboutLogin;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelShop;
import org.nearbyshops.whitelabelapp.AdminShop.ButtonDashboard.DashboardShopAdmin.ShopAdminHome;
import org.nearbyshops.whitelabelapp.AdminShop.ButtonDashboard.DashboardShopStaff.ShopDashboardForStaff;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

/**
 * Created by sumeet on 2/4/17.
 */


public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {



    private boolean isDestroyed = false;


    @BindView(R.id.label_login)TextView labelLogin;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;

    @BindView(R.id.user_profile) LinearLayout profileBlock;
    @BindView(R.id.profile_image) ImageView profileImage;

    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.phone) TextView phone;
    @BindView(R.id.user_id) TextView userID;

    @BindView(R.id.current_dues) TextView currentDues;
    @BindView(R.id.credit_limit) TextView creditLimit;

    @BindView(R.id.dashboard_name) TextView dashboardName;
    @BindView(R.id.dashboard_description) TextView dashboardDescription;
    @BindView(R.id.dashboard_by_role) LinearLayout dashboardByRole;


    @Inject
    UserService userService;



    private ViewModelShop viewModelShop;
    private ProgressDialog progressDialog;



    @BindView(R.id.service_name) TextView serviceName;




    public ProfileFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);





//        viewModelShop = ViewModelProviders.of(this).get(ViewModelShop.class);

        viewModelShop = new ViewModelShop(MyApplication.application);


        viewModelShop.getShopLive().observe(getViewLifecycleOwner(), new Observer<Shop>() {
            @Override
            public void onChanged(Shop shop) {

                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }



                PrefShopAdminHome.saveShop(shop,getActivity());

                Intent intent = new Intent(getActivity(), ShopDashboardForStaff.class);
                startActivity(intent);
            }
        });





        viewModelShop.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if(integer == ViewModelShop.EVENT_BECOME_A_SELLER_SUCCESSFUL)
                {
                    if(progressDialog!=null)
                    {
                        progressDialog.dismiss();
                    }


                    onRefresh();
                }

            }
        });





        viewModelShop.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage(s);
            }
        });




        setupSwipeContainer();


        if(savedInstanceState==null)
        {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {

                    swipeContainer.setRefreshing(true);
                    onRefresh();
                }
            });

            bindUserProfile();

        }


        bindDashboard();


        return rootView;
    }








    private void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }







    private void bindDashboard()
    {
        User user = PrefLogin.getUser(getActivity());


        if(user.getRole()==User.ROLE_ADMIN_CODE)
        {
            dashboardName.setText("Admin Dashboard");
            dashboardDescription.setText("Press here to access the admin dashboard !");
        }
        else if(user.getRole()==User.ROLE_STAFF_CODE)
        {
            dashboardName.setText("Staff Dashboard");
            dashboardDescription.setText("Press here to access the staff dashboard !");
        }
        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
        {
            dashboardName.setText("Shop Dashboard");
            dashboardDescription.setText("Press here to access the shop dashboard !");
        }
        else if(user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {
            dashboardName.setText("Shop Staff Dashboard");
            dashboardDescription.setText("Press here to access the staff dashboard !");
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE || user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE)
        {
            dashboardName.setText("Delivery Dashboard");
            dashboardDescription.setText("Press here to access the Delivery dashboard !");
        }
        else if(user.getRole()==User.ROLE_END_USER_CODE)
        {
            if(getResources().getBoolean(R.bool.single_vendor_mode_enabled))
            {
                dashboardByRole.setVisibility(View.GONE);
            }
            else
            {
                dashboardByRole.setVisibility(View.VISIBLE);
                dashboardName.setText("Become a Seller");
                dashboardDescription.setText("Press here to create a shop and become a seller !");
            }

        }

    }





    @OnClick(R.id.dashboard_by_role)
    void dashboardClick()
    {
        User user = PrefLogin.getUser(getActivity());

        if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
        {
            Intent intent = new Intent(getActivity(), ShopAdminHome.class);
            startActivity(intent);
        }
        else if(user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {

            viewModelShop.getShopForShopDashboard();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait ... getting shop details !");
            progressDialog.show();

        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE)
        {

            Intent intent = new Intent(getActivity(), DeliveryPersonInventory.class);
            intent.putExtra(DeliveryInventoryFragment.SCREEN_MODE_INTENT_KEY,DeliveryInventoryFragment.SCREEN_MODE_DELIVERY_PERSON_VENDOR);
            startActivity(intent);

        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE)
        {
            Intent intent = new Intent(getActivity(), DeliveryPersonInventory.class);
            intent.putExtra(DeliveryInventoryFragment.SCREEN_MODE_INTENT_KEY,DeliveryInventoryFragment.SCREEN_MODE_DELIVERY_PERSON_MARKET);
            startActivity(intent);

        }
        else if(user.getRole()==User.ROLE_END_USER_CODE)
        {

//            viewModelShop.becomeASeller();
//
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("Please wait ... converting you to a seller !");
//            progressDialog.show();



            //     open edit shop in edit mode
            Intent intent = new Intent(getActivity(), EditShop.class);
            intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_ADD);
            startActivityForResult(intent,2323);

        }

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2323)
        {
//            Intent intent = new Intent(getActivity(), ShopAdminHome.class);
//            startActivity(intent);
        }


    }




    @OnClick({R.id.profile_image, R.id.user_profile})
    void editProfileClick()
    {
        Intent intent = new Intent(getActivity(), EditProfile.class);
        intent.putExtra(FragmentEditProfile.EDIT_MODE_INTENT_KEY, FragmentEditProfile.MODE_UPDATE);
        startActivity(intent);

    }






    @OnClick(R.id.billing_info)
    void billingInfoClick()
    {
//        Intent intent = new Intent(getActivity(), Transactions.class);
//        startActivity(intent);
    }





    @OnClick(R.id.faqs_block)
    void faqsBlock()
    {

        String url = getString(R.string.faqs_link);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }




    @OnClick(R.id.privacy_policy_block)
    void privacyPolicyClick()
    {
        String url = getString(R.string.privacy_policy_link);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }




    @OnClick(R.id.tos_block)
    void termsOfServiceClick()
    {
        String url = getString(R.string.tos_link);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }








    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }







    @OnClick({R.id.login_block})
    void loginClick()
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Confirm Logout !")
                .setMessage("Do you want to log out !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }







    private void logout()
    {


        UtilityFunctions.logout(getActivity());

        if(getActivity() instanceof NotifyAboutLogin)
        {
            ((NotifyAboutLogin) getActivity()).loggedOut();
        }

    }




    @Override
    public void onRefresh() {
//        countDownTimer.start();
        getUserProfile();
    }







    private void getUserProfile()
    {

        if(getActivity()==null)
        {
            return;
        }


        User endUser = PrefLogin.getUser(getActivity());

        if(endUser==null)
        {
            return;
        }





        Call<User> call = userService.getProfile(
                PrefLogin.getAuthorizationHeader(getActivity())
        );





        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {

                    PrefLogin.saveUserProfile(response.body(),getActivity());

                }
                else if(response.code()==204)
                {
                    // Vehicle not registered so remove the saved vehicle
//                    PrefVehicle.saveVehicle(null,getContext());

                }
                else
                {
                    showToastMessage("Server error code : " + response.code());
                }


                bindUserProfile();
                bindDashboard();

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                bindUserProfile();
                bindDashboard();

                swipeContainer.setRefreshing(false);

            }
        });

    }




    private void bindUserProfile()
    {

        User user = PrefLogin.getUser(getActivity());

        if(user==null)
        {
            profileBlock.setVisibility(View.GONE);
            profileImage.setVisibility(View.GONE);
            return;
        }




        userID.setText("User ID : " + user.getUserID());

        profileBlock.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);



        Drawable placeholder = ContextCompat.getDrawable(getActivity(), R.drawable.ic_nature_people_white_48px);
        String imagePath = PrefGeneral.getServerURL(getActivity()) + "/api/v1/User/Image/" + "five_hundred_"+ user.getProfileImagePath() + ".jpg";




        showLogMessage("Profile Screen : User Image Path : " + imagePath);



        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(profileImage);


        phone.setText(user.getPhone());
        userName.setText(user.getName());


//        if(user.getCurrentDues()>=0)
//        {
//            currentDues.setText("You Owe : "
//                    + getString(R.string.rupee_symbol)
//                    + " " + String.format("%.2f",user.getCurrentDues())
//            );
//        }
//        else
//        {
//            currentDues.setText("You have a surplus of "
//                    + getString(R.string.rupee_symbol)
//                    + " " + String.format("%.2f",-user.getCurrentDues())
//                    + " in your account."
//            );
//        }

        creditLimit.setText("Your Credit Limit : "
                + getString(R.string.rupee_symbol) + " "
                + String.format("%.2f",user.getExtendedCreditLimit() + 1000)
        );

    }






    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false; // reset flag
    }


    @Override
    public void onStop() {
        super.onStop();
        isDestroyed = true;
    }


    private void showLogMessage(String message)
    {
        Log.d("location_service",message);
    }

}
