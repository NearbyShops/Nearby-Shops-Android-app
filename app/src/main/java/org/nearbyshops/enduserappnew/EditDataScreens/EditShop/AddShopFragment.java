package org.nearbyshops.enduserappnew.EditDataScreens.EditShop;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.enduserappnew.ImageList.ImageListForShop.ShopImageList;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.AdapterDeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.EditDeliverySlot.EditDeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.EditDeliverySlot.EditDeliverySlotFragment;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.ViewHolderDeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.ViewModelDeliverySlot;
import org.nearbyshops.enduserappnew.Lists.TransactionHistory.TransactionHistory;
import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.AddItemData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderAddItem;
import org.nearbyshops.enduserappnew.aSellerModule.DashboardShop.ShopDashboard;
import org.nearbyshops.enduserappnew.adminModule.AddCredit.AddCredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;




public class AddShopFragment extends Fragment{

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


    @Inject
    ShopService shopService;




    @BindView(R.id.shopName) EditText shopName;

    @BindView(R.id.shopAddress) EditText shopAddress;
    @BindView(R.id.shopCity) EditText city;
    @BindView(R.id.customerHelplineNumber) EditText customerHelplineNumber;

    @BindView(R.id.saveButton) TextView saveButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;



    public static final String SHOP_INTENT_KEY = "shop_intent_key";
    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";


    public static final int MODE_CREATE_SHOP_BY_ADMIN = 56;
    public static final int MODE_UPDATE_BY_ADMIN = 55;
    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;


    private int current_mode = MODE_ADD;

    int shopID;
    int shopAdminID;


    private Shop shop = new Shop();

    public AddShopFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_add_shop, container, false);

        ButterKnife.bind(this, rootView);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        return rootView;
    }







    public static final String TAG_LOG = "TAG_LOG";

    private void showLogMessage(String message) {
        Log.i(TAG_LOG, message);
        System.out.println(message);
    }








    @OnClick(R.id.saveButton)
    public void UpdateButtonClick() {

        if (!validateData()) {
//            showToastMessage("Please correct form data before save !");
            return;
        }

        addAccount();
    }





    private boolean validateData() {
        boolean isValid = true;

        if (shopName.getText().toString().length() == 0) {
            shopName.setError("Please enter Shop Name");
            shopName.requestFocus();
            isValid = false;
        }


        if (customerHelplineNumber.getText().toString().length() == 0) {
            customerHelplineNumber.setError("Please enter Phone Number");
            customerHelplineNumber.requestFocus();
            isValid = false;
        }

        return isValid;
    }







    private void addAccount() {

        retrofitPOSTRequest();
    }




    private void bindShopData() {

        if (shop == null) {
            return;
        }

        shopName.setText(shop.getShopName());
        shopAddress.setText(shop.getShopAddress());

        city.setText(shop.getCity());
        customerHelplineNumber.setText(shop.getCustomerHelplineNumber());

    }





    private void getDataFromViews()
    {

        if(shop==null)
        {
            shop = new Shop();
        }



        shop.setShopName(shopName.getText().toString());
        shop.setShopAddress(shopAddress.getText().toString());

        shop.setCity(city.getText().toString());
        shop.setCustomerHelplineNumber(customerHelplineNumber.getText().toString());
    }





    private void retrofitPOSTRequest()
    {
        getDataFromViews();


        Call<Shop> call = null;


        call = shopService.createShop(
                PrefLogin.getAuthorizationHeaders(getContext()),
                shop
        );



        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {

                if(response.code()==201)
                {
                    showToastMessage("Add successful !");

                    if(current_mode==MODE_ADD)
                    {
                        current_mode = MODE_UPDATE;


                        // save user profile
                        User user  = PrefLogin.getUser(getActivity());
                        user.setRole(User.ROLE_SHOP_ADMIN_CODE);
                        PrefLogin.saveUserProfile(user,getActivity());

                        getActivity().setResult(405);

                    }
                    else if(current_mode==MODE_CREATE_SHOP_BY_ADMIN)
                    {
                        current_mode=MODE_UPDATE_BY_ADMIN;
                    }



                    shop = response.body();
                    bindShopData();

                    PrefShopAdminHome.saveShop(shop,getContext());


                    getActivity().finish();

                }
                else
                {
                    showToastMessage("Add failed Code : " + response.code());
                }



                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                showToastMessage("Add failed !");



                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }






    /*
        Utility Methods
     */


    private void showToastMessage(String message)
    {
//        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        UtilityFunctions.showToastMessage(getActivity(),message);
    }






}
