package org.nearbyshops.whitelabelapp.EditDataScreens.EditShop;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopUtilityService;
import org.nearbyshops.whitelabelapp.AdminShop.ShopDashboardBottom;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.PlacePickers.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddShopFragment extends Fragment{

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


    @Inject
    ShopUtilityService shopService;




    @BindView(R.id.shopName) EditText shopName;

    @BindView(R.id.shopAddress) EditText shopAddress;
    @BindView(R.id.shopCity) EditText city;
    @BindView(R.id.customerHelplineNumber) EditText customerHelplineNumber;
    @BindView(R.id.latitude) EditText latitude;
    @BindView(R.id.longitude) EditText longitude;
    @BindView(R.id.pick_location_button) TextView pickLocationButton;

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



        if (latitude.getText().toString().length() == 0) {
            latitude.setError("Latitude cant be empty !");
            latitude.requestFocus();
            isValid = false;
        } else {
            double lat = Double.parseDouble(latitude.getText().toString());

            if (lat > 90 || lat < -90 || lat ==0) {
                latitude.setError("Please Set Location !");
                latitude.requestFocus();
                isValid = false;
            }

            if(lat==0)
            {
                showToastMessage("Please Set Shop Location !");
                return false;
            }
        }


        if (longitude.getText().toString().length() == 0) {
            longitude.setError("Longitude cant be empty !");
            longitude.requestFocus();
            isValid = false;
        } else {
            double lon = Double.parseDouble(longitude.getText().toString());

            if (lon > 180 || lon < -180 || lon ==0) {
                longitude.setError("Please Set Location !");
                longitude.requestFocus();
                isValid = false;
            }

            if(lon==0)
            {
                showToastMessage("Please Set Shop Location !");
                return false;
            }

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
        shop.setCustomerHelplineNumber(customerHelplineNumber.getText().toString());

        shop.setShopAddress(shopAddress.getText().toString());
        shop.setCity(city.getText().toString());



        if(!latitude.getText().toString().equals("") && !longitude.getText().toString().equals(""))
        {
            shop.setLatCenter(Double.parseDouble(latitude.getText().toString()));
            shop.setLonCenter(Double.parseDouble(longitude.getText().toString()));
        }
    }






    private void retrofitPOSTRequest()
    {
        getDataFromViews();


        Call<Shop> call = null;


        call = shopService.createShop(
                PrefLogin.getAuthorizationHeader(getContext()),
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

                    if(getResources().getInteger(R.integer.app_type)==getResources().getInteger(R.integer.app_type_vendor_app))
                    {
                        startActivity(new Intent(requireActivity(), ShopDashboardBottom.class));
                    }

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
        UtilityFunctions.showToastMessage(getActivity(),message);
    }






    // code for picking up location
    @OnClick(R.id.pick_location_button)
    void pickLocation()
    {
        Intent intent = new Intent(getActivity(), GooglePlacePicker.class);
        intent.putExtra("lat_dest",Double.parseDouble(latitude.getText().toString()));
        intent.putExtra("lon_dest",Double.parseDouble(longitude.getText().toString()));
        startActivityForResult(intent,3);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3 && resultCode==6)
        {
            latitude.setText(String.valueOf(data.getDoubleExtra("lat_dest",0.0)));
            longitude.setText(String.valueOf(data.getDoubleExtra("lon_dest",0.0)));
        }
    }


}
