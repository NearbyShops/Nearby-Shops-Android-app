package org.nearbyshops.whitelabelapp.EditDataScreens.EditShop;


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


import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopDetailService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopUtilityService;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.AdapterDeliverySlot;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.EditDeliverySlot.EditDeliverySlot;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.EditDeliverySlot.EditDeliverySlotFragment;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.ViewHolderDeliverySlot;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.ViewModelDeliverySlot;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.whitelabelapp.Lists.TransactionHistory.TransactionHistory;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.PlacePickers.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.whitelabelapp.Model.Image;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.ImageScreens.ImageList.ImageListForShop.ShopImageList;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderAddItem;
import org.nearbyshops.whitelabelapp.AdminShop.ButtonDashboard.DashboardShop.ShopDashboard;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.AdminCommon.AddCredit.AddCredit;

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
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;


public class EditShopFragment extends Fragment implements ViewHolderDeliverySlot.ListItemClick, ViewHolderAddItem.ListItemClick {

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


    @Inject
    ShopService shopService;

    @Inject
    ShopUtilityService shopUtilityService;

    @Inject
    ShopDetailService shopDetailService;


    // flag for knowing whether the image is changed or not
    private boolean isImageChanged = false;
    private boolean isImageRemoved = false;


    // bind views
    @BindView(R.id.uploadImage) ImageView resultView;


    // fields for admin options
    @BindView(R.id.shop_admin_name) TextView shopAdminName;
    @BindView(R.id.shop_admin_phone) TextView shopAdminPhone;
    @BindView(R.id.time_created) TextView timeOfRegistration;
    @BindView(R.id.extended_credit_limit) EditText extendedCreditLimit;
    @BindView(R.id.switch_permit_update_items) Switch permitUpdateItems;

    @BindView(R.id.account_balance) TextView accountBalance;


    @BindView(R.id.shop_open) CheckBox shopOpen;
//    @BindView(R.id.shop_id) EditText shopID;

    @BindView(R.id.enter_shop_id)
    EditText shopIDEnter;
    @BindView(R.id.shopName)
    EditText shopName;


    @BindView(R.id.shopAddress) EditText shopAddress;
    @BindView(R.id.shopCity) EditText city;
    @BindView(R.id.shopPincode) EditText pincode;
    @BindView(R.id.shopLandmark) EditText landmark;

    @BindView(R.id.customerHelplineNumber) EditText customerHelplineNumber;
    @BindView(R.id.deliveryHelplineNumber) EditText deliveryHelplineNumber;

    @BindView(R.id.shopShortDescription) EditText shopDescriptionShort;
    @BindView(R.id.shopLongDescription) EditText shopDescriptionLong;

    @BindView(R.id.latitude) EditText latitude;
    @BindView(R.id.longitude) EditText longitude;
    @BindView(R.id.pick_location_button) TextView pickLocationButton;
    @BindView(R.id.rangeOfDelivery) EditText rangeOfDelivery;

    @BindView(R.id.deliveryCharges) EditText deliveryCharge;
    @BindView(R.id.billAmountForFreeDelivery) EditText billAmountForFreeDelivery;

    @BindView(R.id.pick_from_shop_available) CheckBox pickFromShopAvailable;
    @BindView(R.id.home_delivery_available) CheckBox homeDeliveryAvailable;

    @BindView(R.id.error_delivery_option) TextView errorDeliveryOption;
    @BindView(R.id.error_delivery_option_top) TextView errorDeliveryOptionTop;


    @BindView(R.id.saveButton)
    TextView saveButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;




    @BindView(R.id.admin_options_block)
    LinearLayout adminOptionsBlock;


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

    public EditShopFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_shop, container, false);

        ButterKnife.bind(this, rootView);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        if (savedInstanceState == null) {

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY, MODE_ADD);
            shopID = getActivity().getIntent().getIntExtra("shop_id", 0 );
            shopAdminID = getActivity().getIntent().getIntExtra("shop_admin_id",0);

            getShopDetails();
        }


        updateFieldVisibility();



        setupViewModel();
        setupRecyclerView();
        getDeliverySlots();


        return rootView;
    }





    private void getShopDetails() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.loading_message));
        pd.show();



        Call<Shop> call = shopDetailService.getShopForEditScreen(
                PrefLogin.getAuthorizationHeader(getActivity()),
                shopID, shopAdminID
        );


        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
//
//                if (!isVisible()) {
//                    return;
//                }

                pd.dismiss();


                if (response.code() == 200) {
                    shop = response.body();
                    bindShopData();
                } else {
                    showToastMessage("Failed to get Shop Details : Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {


//                if (!isVisible()) {
//                    return;
//                }

                showToastMessage("Failed !");

            }
        });
    }




    private void updateFieldVisibility() {

        if (current_mode == MODE_ADD || current_mode==MODE_CREATE_SHOP_BY_ADMIN) {
            saveButton.setText("Add Shop");
            shopIDEnter.setVisibility(View.GONE);
            adminOptionsBlock.setVisibility(View.GONE);


            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Shop");
            }


        }
        else if (current_mode == MODE_UPDATE) {

            shopIDEnter.setVisibility(View.VISIBLE);
            saveButton.setText("Save");
            adminOptionsBlock.setVisibility(View.GONE);

            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Shop");
            }

        }
        else if (current_mode == MODE_UPDATE_BY_ADMIN) {

            shopIDEnter.setVisibility(View.VISIBLE);
            saveButton.setText("Save");
            adminOptionsBlock.setVisibility(VISIBLE);

            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Shop");
            }
        }
    }



    public static final String TAG_LOG = "TAG_LOG";

    private void showLogMessage(String message) {
        Log.i(TAG_LOG, message);
        System.out.println(message);
    }







    @OnClick(R.id.add_extra_photos)
    void imageClick() {
        Intent intent = new Intent(getActivity(), ShopImageList.class);
        intent.putExtra("shop_id", shop.getShopID());
        intent.putExtra("is_admin_mode",true);
        startActivity(intent);
    }





    @OnClick(R.id.saveButton)
    public void UpdateButtonClick() {

        if (!validateData()) {
//            showToastMessage("Please correct form data before save !");
            return;
        }




        if (current_mode == MODE_ADD || current_mode == MODE_CREATE_SHOP_BY_ADMIN) {
            addAccount();
        } else if (current_mode == MODE_UPDATE || current_mode == MODE_UPDATE_BY_ADMIN) {
            update();
        }
    }





    private boolean validateData() {
        boolean isValid = true;

        if (shopName.getText().toString().length() == 0) {
            shopName.setError("Please enter Shop Name");
            shopName.requestFocus();
            isValid = false;
        }


        if (!homeDeliveryAvailable.isChecked() && !pickFromShopAvailable.isChecked()) {
            homeDeliveryAvailable.setError("You must pick at least one delivery Option");
            pickFromShopAvailable.setError("You must pick at least one delivery Option");

            errorDeliveryOption.setVisibility(View.VISIBLE);
            errorDeliveryOptionTop.setVisibility(View.VISIBLE);

            homeDeliveryAvailable.requestFocus();
            pickFromShopAvailable.requestFocus();

            isValid = false;
        } else {

            errorDeliveryOption.setVisibility(View.GONE);
            errorDeliveryOptionTop.setVisibility(View.GONE);
        }


        if (latitude.getText().toString().length() == 0) {
            latitude.setError("Latitude cant be empty !");
            latitude.requestFocus();
            isValid = false;
        } else {
            double lat = Double.parseDouble(latitude.getText().toString());

            if (lat > 90 || lat < -90) {
                latitude.setError("Invalid Latitude !");
                latitude.requestFocus();
                isValid = false;
            }

            if(lat==0)
            {
                showToastMessage("Please Set Shop Location !");
                isValid = false;
            }
        }



        if (longitude.getText().toString().length() == 0) {
            longitude.setError("Longitude cant be empty !");
            longitude.requestFocus();
            isValid = false;
        } else {
            double lon = Double.parseDouble(longitude.getText().toString());

            if (lon > 180 || lon < -180) {
                longitude.setError("Invalid Longitude !");
                latitude.requestFocus();
                isValid = false;
            }

            if(lon==0)
            {
                showToastMessage("Please Set Shop Location !");
                isValid = false;
            }

        }

        if (rangeOfDelivery.getText().toString().length() == 0) {
            rangeOfDelivery.setError("Range of Delivery cant be empty !");
            rangeOfDelivery.requestFocus();
            isValid = false;
        }

        if (shopDescriptionShort.getText().toString().length() > 100) {
            shopDescriptionShort.setError("Should not be more than 100 characters !");
            shopDescriptionShort.requestFocus();
            isValid = false;
        }


        return isValid;
    }


    private void addAccount() {
        if (isImageChanged) {
            if (!isImageRemoved) {
                // upload image with add
                uploadPickedImage(false);
            }


            // reset the flags
            isImageChanged = false;
            isImageRemoved = false;

        } else {
            // post request
            retrofitPOSTRequest();
        }

    }


    private void update() {

        if (isImageChanged) {


            // delete previous Image from the Server
            deleteImage(shop.getLogoImagePath());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if (isImageRemoved) {

                shop.setLogoImagePath(null);
                retrofitPUTRequest();

            } else {

                uploadPickedImage(true);
            }


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        } else {

            retrofitPUTRequest();
        }
    }





    private void bindShopData() {

        if (shop == null) {
            return;
        }


        bindAdminOptions();


        shopOpen.setChecked(shop.isOpen());
        shopIDEnter.setText(String.valueOf(shop.getShopID()));
        shopName.setText(shop.getShopName());
        shopAddress.setText(shop.getShopAddress());

        city.setText(shop.getCity());
        pincode.setText(String.valueOf(shop.getPincode()));
        landmark.setText(shop.getLandmark());
        customerHelplineNumber.setText(shop.getCustomerHelplineNumber());

        deliveryHelplineNumber.setText(shop.getDeliveryHelplineNumber());
        shopDescriptionShort.setText(shop.getShortDescription());
        shopDescriptionLong.setText(shop.getLongDescription());
        latitude.setText(String.valueOf(shop.getLatCenter()));

        longitude.setText(String.valueOf(shop.getLonCenter()));
        rangeOfDelivery.setText(String.valueOf(shop.getDeliveryRange()));
        deliveryCharge.setText(String.valueOf(shop.getDeliveryCharges()));
        billAmountForFreeDelivery.setText(String.valueOf(shop.getBillAmountForFreeDelivery()));

        pickFromShopAvailable.setChecked(shop.getPickFromShopAvailable());
        homeDeliveryAvailable.setChecked(shop.getHomeDeliveryAvailable());


        String imagePath = PrefGeneral.getServerURL(getContext()) + "/api/v1/Shop/Image/five_hundred_" + shop.getLogoImagePath() + ".jpg";

        Picasso.get()
                .load(imagePath)
                .into(resultView);
    }



    private void bindAdminOptions()
    {
        User shopAdminProfile = shop.getShopAdminProfile();


        if(shopAdminProfile!=null)
        {
            shopAdminName.setText("Name : " + shopAdminProfile.getName());
            shopAdminPhone.setText("Phone : " + shopAdminProfile.getPhone());
        }


        if(shop.getTimestampCreated()!=null)
        {
            timeOfRegistration.setText("Created at : " + shop.getTimestampCreated().toLocaleString());
        }


//        aSwitch.setChecked(shop.getShopEnabled());
//        switchWaitlist.setChecked(shop.getShopWaitlisted());
        permitUpdateItems.setChecked(shop.isItemUpdatePermitted());

        extendedCreditLimit.setText(String.valueOf(shop.getExtendedCreditLimit()));
        accountBalance.setText("Balance : " + PrefCurrency.getCurrencySymbol(getActivity()) +  String.format(" %.2f",shop.getAccountBalance()));
    }






    private void getDataFromViews()
    {

        if(shop==null)
        {
            shop = new Shop();
        }




        shop.setOpen(shopOpen.isChecked());
        shop.setShopName(shopName.getText().toString());
        shop.setShopAddress(shopAddress.getText().toString());

        shop.setCity(city.getText().toString());



        if(!pincode.getText().toString().equals(""))
        {
            shop.setPincode(Long.parseLong(pincode.getText().toString()));
        }


        shop.setLandmark(landmark.getText().toString());
        shop.setCustomerHelplineNumber(customerHelplineNumber.getText().toString());

        shop.setDeliveryHelplineNumber(deliveryHelplineNumber.getText().toString());
        shop.setShortDescription(shopDescriptionShort.getText().toString());
        shop.setLongDescription(shopDescriptionLong.getText().toString());


        if(!latitude.getText().toString().equals("") && !longitude.getText().toString().equals("") && !rangeOfDelivery.getText().toString().equals(""))
        {
            shop.setLatCenter(Double.parseDouble(latitude.getText().toString()));
            shop.setLonCenter(Double.parseDouble(longitude.getText().toString()));
            shop.setDeliveryRange(Double.parseDouble(rangeOfDelivery.getText().toString()));
        }


        if(!deliveryCharge.getText().toString().equals(""))
        {
            shop.setDeliveryCharges(Double.parseDouble(deliveryCharge.getText().toString()));
        }

        if(!billAmountForFreeDelivery.getText().toString().equals(""))
        {
            shop.setBillAmountForFreeDelivery(Integer.parseInt(billAmountForFreeDelivery.getText().toString()));
        }


        shop.setPickFromShopAvailable(pickFromShopAvailable.isChecked());
        shop.setHomeDeliveryAvailable(homeDeliveryAvailable.isChecked());




        // get data from admin fields
//        shop.setShopEnabled(aSwitch.isChecked());
//        shop.setShopWaitlisted(switchWaitlist.isChecked());
        shop.setItemUpdatePermitted(permitUpdateItems.isChecked());

        if(extendedCreditLimit.getText().toString().length()>0)
        {
            shop.setExtendedCreditLimit(Double.parseDouble(extendedCreditLimit.getText().toString()));
        }

    }





    private void retrofitPUTRequest()
    {

        getDataFromViews();


        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = null;



        if(current_mode==MODE_UPDATE)
        {
            call = shopService.updateBySelf(
                    PrefLogin.getAuthorizationHeader(getContext()),
                    shop
            );


        }
        else if(current_mode==MODE_UPDATE_BY_ADMIN)
        {
            // update Item Call
            call = shopService.updateShopByAdmin(
                    PrefLogin.getAuthorizationHeader(getActivity()),
                    shop, shop.getShopID()
            );

        }
        else
        {


//            showToastMessage("Current Mode : " + current_mode);
            saveButton.setVisibility(VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                    PrefShopAdminHome.saveShop(shop,getContext());
                }
                else if(response.code()== 403 || response.code() ==401)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Update Failed Code : " + response.code());
                }



                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }








    private void retrofitPOSTRequest()
    {
        getDataFromViews();


        Call<Shop> call = null;


        if(current_mode==MODE_ADD)
        {
            call = shopUtilityService.createShop(
                    PrefLogin.getAuthorizationHeader(getContext()),
                    shop
            );

        }
        else if(current_mode==MODE_CREATE_SHOP_BY_ADMIN)
        {

            call = shopUtilityService.createShopByStaff(
                    PrefLogin.getAuthorizationHeader(getContext()),
                    shop, shopAdminID
            );

        }
        else
        {
            return;
        }





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



                    updateFieldVisibility();
                    shop = response.body();
                    bindShopData();

                    PrefShopAdminHome.saveShop(shop,getContext());


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




    @BindView(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);

        isImageChanged = true;
        isImageRemoved = true;
    }





    private static void clearCache(Context context)
    {
        File file = new File(context.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();
    }




    @OnClick({R.id.textChangePicture,R.id.uploadImage})
    void pickShopImage() {

//        ImageCropUtility.showFileChooser(()getActivity());



        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }



        ImagePicker.Companion.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(2024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1500, 1500)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();


    }






    File imagePickedFile;
    String imageFilePath;





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);



        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK

            resultView.setImageURI(result.getData());


            //You can get File object from intent
            imagePickedFile = ImagePicker.Companion.getFile(result);
            imageFilePath = ImagePicker.Companion.getFilePath(result);



            isImageChanged = true;
            isImageRemoved = false;



        } else if (resultCode == ImagePicker.RESULT_ERROR) {

            showToastMessage(ImagePicker.Companion.getError(result));

        }
        else if(requestCode==3 && resultCode==6)
        {
            latitude.setText(String.valueOf(result.getDoubleExtra("lat_dest",0.0)));
            longitude.setText(String.valueOf(result.getDoubleExtra("lon_dest",0.0)));
            rangeOfDelivery.setText(String.valueOf(result.getDoubleExtra("radius",10)));
        }
        else {
            showToastMessage("Task Cancelled !");
        }

    }





    /*

    // Code for Uploading Image

     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showToastMessage("Permission Granted !");
                    pickShopImage();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {


                    showToastMessage("Permission Denied for Read External Storage . ");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }





    public void uploadPickedImage(final boolean isModeEdit)
    {

        Log.d("applog", "onClickUploadImage");


        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }


//        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");

        File file;
        file = new File(imageFilePath);


        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("img", file.getName(), requestBody);

        // Marker

        RequestBody requestBodyBinary = null;

        InputStream in = null;

        try {
            in = new FileInputStream(file);

            byte[] buf;
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;

            requestBodyBinary = RequestBody.create(MediaType.parse("application/octet-stream"), buf);

        } catch (Exception e) {
            e.printStackTrace();
        }





        Call<Image> imageCall = shopService.uploadImage(PrefLogin.getAuthorizationHeader(getContext()),
                fileToUpload);



        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {

                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");

                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());


                    shop.setLogoImagePath(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    shop.setLogoImagePath(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    shop.setLogoImagePath(null);
                }



                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }



                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {

                showToastMessage("Image Upload failed !");
                shop.setLogoImagePath(null);


                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }



                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }





    private void deleteImage(String filename)
    {

        Call<ResponseBody> call = shopService.deleteImage(
                PrefLogin.getAuthorizationHeader(getContext()),
                filename);



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    if(response.code()==200)
                    {
                        showToastMessage("Image Removed !");
                    }
                    else
                    {
//                        showToastMessage("Image Delete failed");
                    }




            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                showToastMessage("Image Delete failed");


            }
        });
    }






    // code for picking up location
    @OnClick(R.id.pick_location_button)
    void pickLocation()
    {
//        Intent intent = new Intent(getActivity(), PickDeliveryRange.class);
//        intent.putExtra("lat_dest",Double.parseDouble(latitude.getText().toString()));
//        intent.putExtra("lon_dest",Double.parseDouble(longitude.getText().toString()));
//        intent.putExtra("radius",Double.parseDouble(rangeOfDelivery.getText().toString()));
//        startActivityForResult(intent,3);


        Intent intent = new Intent(getActivity(), GooglePlacePicker.class);
        intent.putExtra("lat_dest",Double.parseDouble(latitude.getText().toString()));
        intent.putExtra("lon_dest",Double.parseDouble(longitude.getText().toString()));
        intent.putExtra("radius",Double.parseDouble(rangeOfDelivery.getText().toString()));
        startActivityForResult(intent,3);
    }






    @OnClick(R.id.add_credit)
    void addCredit()
    {
//        showToastMessage("Add Credit !");
        Intent intent = new Intent(getActivity(), AddCredit.class);
        intent.putExtra("tag_user_id",shop.getShopAdminID());
        startActivity(intent);
    }





    @OnClick(R.id.shop_admin_profile)
    void shopAdminProfileClick()
    {
        User shopAdminProfile = shop.getShopAdminProfile();


        Gson gson = UtilityFunctions.provideGson();
        String jsonString = gson.toJson(shopAdminProfile);

        Intent intent = new Intent(getActivity(), EditProfile.class);
        intent.putExtra("user_profile",jsonString);
        intent.putExtra(FragmentEditProfile.EDIT_MODE_INTENT_KEY, FragmentEditProfile.MODE_UPDATE_BY_ADMIN);
        startActivity(intent);
    }





    @OnClick(R.id.transaction_button)
    void shopTransactionsClick()
    {
        Intent intent = new Intent(getActivity(), TransactionHistory.class);
        intent.putExtra("user_id",shop.getShopAdminID());
        startActivity(intent);
    }



    @OnClick(R.id.shop_dashboard)
    void shopDashboardClick()
    {
        PrefShopAdminHome.saveShop(shop,getActivity());
        Intent intent = new Intent(getActivity(), ShopDashboard.class);
        startActivity(intent);
    }








    ViewModelDeliverySlot viewModelDeliverySlot;


    private void setupViewModel()
    {

        viewModelDeliverySlot = new ViewModelDeliverySlot(MyApplication.application);


        viewModelDeliverySlot.getData().observe(getViewLifecycleOwner(), new Observer<List<Object>>() {
            @Override
            public void onChanged(List<Object> objects) {


                dataset.clear();

                dataset.add(0,new ViewHolderAddItem.AddItemData());
                dataset.addAll(objects);


                adapterDeliverySlot.notifyDataSetChanged();

            }
        });




        viewModelDeliverySlot.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

            }
        });





        viewModelDeliverySlot.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage(s);

            }
        });

    }







    ArrayList<Object> dataset = new ArrayList<>();
    @BindView(R.id.delivery_slot_list) RecyclerView itemImagesList;
    AdapterDeliverySlot adapterDeliverySlot;



    private void setupRecyclerView() {

        adapterDeliverySlot = new AdapterDeliverySlot(dataset,getActivity(),this,ViewHolderDeliverySlot.MODE_SHOP_ADMIN);
        itemImagesList.setAdapter(adapterDeliverySlot);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        itemImagesList.setLayoutManager(layoutManager);
    }







    void getDeliverySlots()
    {
        Integer shopID = shop.getShopID();

//        viewModelDeliverySlot.fetchDeliverySlots(shopID,false,
//                null,DeliverySlot.SLOT_ID
//        );
    }



    @OnClick(R.id.sync_refresh)
    void syncRefreshClick()
    {
        getDeliverySlots();
    }


    @Override
    public void listItemClick(int deliverySlotID) {

    }

    @Override
    public void editDeliverySlot(DeliverySlot deliverySlot, int position) {

        Intent intent = new Intent(getActivity(), EditDeliverySlot.class);
        intent.putExtra(EditDeliverySlotFragment.EDIT_MODE_INTENT_KEY,EditDeliverySlotFragment.MODE_UPDATE);
        intent.putExtra(EditDeliverySlotFragment.ACCESS_MODE_INTENT_KEY,EditDeliverySlotFragment.MODE_ACCESS_BY_SHOP_ADMIN);

        String jsonString = UtilityFunctions.provideGson().toJson(deliverySlot,DeliverySlot.class);
        intent.putExtra("delivery_slot_json",jsonString);

        startActivity(intent);
    }



    @Override
    public void removeDeliverySlot(DeliverySlot deliverySlot, int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Delete !")
                .setMessage("Are you sure you want to delete this Delivery Slot ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        viewModelDeliverySlot.deleteDeliverySlot(deliverySlot.getSlotID());

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        showToastMessage("Cancelled !");
                    }
                })
                .show();


    }




    @Override
    public void addItemClick() {

        Intent intent = new Intent(getActivity(), EditDeliverySlot.class);
        intent.putExtra(EditDeliverySlotFragment.EDIT_MODE_INTENT_KEY,EditDeliverySlotFragment.MODE_ADD);
        intent.putExtra(EditDeliverySlotFragment.ACCESS_MODE_INTENT_KEY,EditDeliverySlotFragment.MODE_ACCESS_BY_SHOP_ADMIN);

        startActivity(intent);
    }



}
