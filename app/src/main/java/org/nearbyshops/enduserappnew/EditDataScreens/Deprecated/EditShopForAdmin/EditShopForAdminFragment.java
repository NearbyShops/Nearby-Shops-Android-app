package org.nearbyshops.enduserappnew.EditDataScreens.Deprecated.EditShopForAdmin;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.adminModule.AddCredit.AddCredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.Circle;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;


public class EditShopForAdminFragment extends Fragment {

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;

    boolean isDestroyed = false;

//    Validator validator;


//    @Inject
//    DeliveryGuySelfService deliveryService;

    @Inject
    ShopService shopService;


    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    // bind views
    @BindView(R.id.uploadImage)
    ImageView resultView;


    @BindView(R.id.shop_admin_name) TextView shopAdminName;
    @BindView(R.id.shop_admin_phone) TextView shopAdminPhone;
    @BindView(R.id.time_created) TextView timeOfRegistration;


    @BindView(R.id.shop_open) CheckBox shopOpen;
//    @BindView(R.id.shop_id) EditText shopID;

    @BindView(R.id.enter_shop_id) EditText shopIDEnter;
    @BindView(R.id.shopName) EditText shopName;

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


    @BindView(R.id.switch_enable) Switch aSwitch;
    @BindView(R.id.switch_waitlist) Switch switchWaitlist;

    @BindView(R.id.extended_credit_limit) EditText extendedCreditLimit;
    @BindView(R.id.account_balance) TextView accountBalance;


    @BindView(R.id.error_delivery_option) TextView errorDeliveryOption;
    @BindView(R.id.error_delivery_option_top) TextView errorDeliveryOptionTop;



//    @BindView(R.id.item_id) EditText item_id;
//    @BindView(R.id.name) EditText name;
//    @BindView(R.id.username) EditText username;
//    @BindView(R.id.password) EditText password;
//    @BindView(R.id.about) EditText about;

//    @BindView(R.id.phone_number) EditText phone;
//    @BindView(R.id.designation) EditText designation;
//    @BindView(R.id.switch_enable) Switch aSwitch;

    @BindView(R.id.saveButton) TextView buttonUpdateItem;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    public static final String SHOP_INTENT_KEY = "shop_intent_key";
    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;

//    DeliveryGuySelf deliveryGuySelf = new DeliveryGuySelf();
//    ShopAdmin shopAdmin = new ShopAdmin();
        Shop shop = new Shop();

    public EditShopForAdminFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_shop_for_admin_fragment, container, false);
        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(savedInstanceState==null)
        {
//            shopAdmin = getActivity().getIntent().getParcelableExtra(SHOP_ADMIN_INTENT_KEY);

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_UPDATE);

            if(current_mode == MODE_UPDATE)
            {
                shop = PrefShopForAdmin.getShop(getContext());

                if(shop!=null) {
                    bindDataToViews();
                }
            }

        }



//        if(validator==null)
//        {
//            validator = new Validator(this);
//            validator.setValidationListener(this);
//        }

        updateIDFieldVisibility();


        if(shop!=null) {
            loadImage(shop.getLogoImagePath());
            showLogMessage("Inside OnCreateView : DeliveryGUySelf : Not Null !");
        }


        showLogMessage("Inside On Create View !");
//        setupMap();

        return rootView;
    }



    void updateIDFieldVisibility()
    {

        if(current_mode==MODE_ADD)
        {
            buttonUpdateItem.setText("Add Shop");
            shopIDEnter.setVisibility(View.GONE);
        }
        else if(current_mode== MODE_UPDATE)
        {
            shopIDEnter.setVisibility(View.VISIBLE);
            buttonUpdateItem.setText("Save");
        }
    }


    public static final String TAG_LOG = "TAG_LOG";

    void showLogMessage(String message)
    {
        Log.i(TAG_LOG,message);
        System.out.println(message);
    }



    void loadImage(String imagePath) {

        String iamgepath = PrefGeneral.getServiceURL(getContext()) + "/api/v1/Shop/Image/" + imagePath;

        Picasso.get()
                .load(iamgepath)
                .into(resultView);
    }







    @OnClick(R.id.saveButton)
    public void UpdateButtonClick()
    {

        if(!validateData())
        {
//            showToastMessage("Please correct form data before save !");
            return;
        }

        if(current_mode == MODE_ADD)
        {
            shop = new Shop();
            addAccount();
        }
        else if(current_mode == MODE_UPDATE)
        {
            update();
        }
    }


    boolean validateData()
    {
        boolean isValid = true;

        if(shopName.getText().toString().length()==0)
        {
            shopName.setError("Please enter Shop Name");
            shopName.requestFocus();
            isValid= false;
        }


        if(!homeDeliveryAvailable.isChecked() && !pickFromShopAvailable.isChecked())
        {
            homeDeliveryAvailable.setError("You must pick at least one delivery Option");
            pickFromShopAvailable.setError("You must pick at least one delivery Option");
            homeDeliveryAvailable.requestFocus();
            pickFromShopAvailable.requestFocus();


            errorDeliveryOption.setVisibility(View.VISIBLE);
            errorDeliveryOptionTop.setVisibility(View.VISIBLE);

            isValid = false;
        }
        else
        {

            errorDeliveryOption.setVisibility(View.GONE);
            errorDeliveryOptionTop.setVisibility(View.GONE);
        }





        if(latitude.getText().toString().length()==0)
        {
            latitude.setError("Latitude cant be empty !");
            latitude.requestFocus();
            isValid = false;
        }
        else
        {
            double lat = Double.parseDouble(latitude.getText().toString());

            if(lat >90 || lat <- 90)
            {
                latitude.setError("Invalid Latitude !");
                isValid  = false;
            }
        }


        if(longitude.getText().toString().length()==0)
        {
            longitude.setError("Longitude cant be empty !");
            longitude.requestFocus();
            isValid= false;
        }
        else
        {
            double lon = Double.parseDouble(longitude.getText().toString());

            if(lon >180 || lon < -180)
            {
                longitude.setError("Invalid Longitude !");
                isValid = false;
            }

        }

        if(rangeOfDelivery.getText().toString().length()==0)
        {
            rangeOfDelivery.setError("Range of Delivery cant be empty !");
            rangeOfDelivery.requestFocus();
            isValid = false;
        }




        if(shopDescriptionShort.getText().toString().length()>100)
        {
            shopDescriptionShort.setError("Should not be more than 100 characters !");
            shopDescriptionShort.requestFocus();
            isValid = false;
        }



        return isValid;
    }




    void addAccount()
    {
        if(isImageChanged)
        {
            if(!isImageRemoved)
            {
                // upload image with add
                uploadPickedImage(false);
            }


            // reset the flags
            isImageChanged = false;
            isImageRemoved = false;

        }
        else
        {
            // post request
            retrofitPOSTRequest();
        }

    }


    void update()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            deleteImage(shop.getLogoImagePath());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {

                shop.setLogoImagePath(null);
                retrofitPUTRequest();

            }else
            {

                uploadPickedImage(true);
            }


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        }else {

            retrofitPUTRequest();
        }
    }



    void bindDataToViews()
    {
        if(shop!=null) {

            User shopAdminProfile = shop.getShopAdminProfile();


            if(shopAdminProfile!=null)
            {
                shopAdminName.setText("Name : " + shopAdminProfile.getName());
                shopAdminPhone.setText("Phone : " + shopAdminProfile.getPhone());


//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//                String dateTimeString = sdf.format(new Date(shopAdminProfile.getTimestampCreated().getTime()));

//                timeOfRegistration.setText("Time of Registration : " + shopAdminProfile.getTimestampCreated().toLocaleString());

//                timeOfRegistration.setText("Time of Registration : " + dateTimeString);


            }



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
            aSwitch.setChecked(shop.getShopEnabled());
            switchWaitlist.setChecked(shop.getShopWaitlisted());

            extendedCreditLimit.setText(String.valueOf(shop.getExtendedCreditLimit()));
            accountBalance.setText("Account Balance : " + String.format(" %.2f",shop.getAccountBalance()));

            timeOfRegistration.setText("Registered at : " + shop.getTimestampCreated().toLocaleString());


        }
    }





    void getDataFromViews()
    {
        if(shop==null)
        {
            if(current_mode == MODE_ADD)
            {
                shop = new Shop();
            }
            else
            {
                return;
            }
        }

//        if(current_mode == MODE_ADD)
//        {
//            deliveryGuySelf.setShopID(UtilityShopHome.getShopDetails(getContext()).getShopID());
//        }

        shop.setOpen(shopOpen.isChecked());
        shop.setShopName(shopName.getText().toString());
        shop.setShopAddress(shopAddress.getText().toString());

        shop.setCity(city.getText().toString());
        shop.setPincode(Long.parseLong(pincode.getText().toString()));
        shop.setLandmark(landmark.getText().toString());
        shop.setCustomerHelplineNumber(customerHelplineNumber.getText().toString());

        shop.setDeliveryHelplineNumber(deliveryHelplineNumber.getText().toString());
        shop.setShortDescription(shopDescriptionShort.getText().toString());
        shop.setLongDescription(shopDescriptionLong.getText().toString());
        shop.setLatCenter(Double.parseDouble(latitude.getText().toString()));

        shop.setLonCenter(Double.parseDouble(longitude.getText().toString()));
        shop.setDeliveryRange(Double.parseDouble(rangeOfDelivery.getText().toString()));
        shop.setDeliveryCharges(Double.parseDouble(deliveryCharge.getText().toString()));
        shop.setBillAmountForFreeDelivery(Integer.parseInt(billAmountForFreeDelivery.getText().toString()));

        shop.setPickFromShopAvailable(pickFromShopAvailable.isChecked());
        shop.setHomeDeliveryAvailable(homeDeliveryAvailable.isChecked());

        shop.setShopEnabled(aSwitch.isChecked());
        shop.setShopWaitlisted(switchWaitlist.isChecked());


        if(extendedCreditLimit.getText().toString().length()>0)
        {
            shop.setExtendedCreditLimit(Double.parseDouble(extendedCreditLimit.getText().toString()));
        }
    }





    public void retrofitPUTRequest()
    {

        getDataFromViews();


        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = shopService.updateShopByAdmin(
                PrefLogin.getAuthorizationHeaders(getContext()),
                shop,shop.getShopID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                    PrefShopForAdmin.saveShop(shop,getContext());
                }
                else
                {
                    showToastMessage("Update Failed Code : " + response.code());
                }



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


    void retrofitPOSTRequest()
    {
        getDataFromViews();



        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<Shop> call = shopService.createShop(PrefLogin.getAuthorizationHeaders(getContext()),shop);

        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code()==201)
                {
                    showToastMessage("Add successful !");

                    current_mode = MODE_UPDATE;
                    updateIDFieldVisibility();
                    shop = response.body();
                    bindDataToViews();

                    PrefShopForAdmin.saveShop(shop,getContext());

                }
                else
                {
                    showToastMessage("Add failed Code : " + response.code());
                }


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Add failed !");

                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }








    /*
        Utility Methods
     */




    void showToastMessage(String message)
    {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
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



    public static void clearCache(Context context)
    {
        File file = new File(context.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();
    }



    @OnClick(R.id.textChangePicture)
    void pickShopImage() {

//        ImageCropUtility.showFileChooser(()getActivity());



        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


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



        clearCache(getContext());

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);


        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_LAT_LON)
        {
            latitude.setText(String.valueOf(result.getDoubleExtra("latitude",0)));
            longitude.setText(String.valueOf(result.getDoubleExtra("longitude",0)));
        }


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {

                startCropActivity(result.getData(),getContext());
            }

        }


        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }
    }



    // upload image after being picked up
    void startCropActivity(Uri sourceUri, Context context) {



        final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";

        Uri destinationUri = Uri.fromFile(new File(getContext().getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));

        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);

//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        options.setCompressionQuality(100);

        options.setToolbarColor(ContextCompat.getColor(getContext(),R.color.blueGrey800));
        options.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);


        // this function takes the file from the source URI and saves in into the destination URI location.
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(context,this);

        //.withMaxResultSize(400,300)
        //.withMaxResultSize(500, 400)
        //.withAspectRatio(16, 9)
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
                != PackageManager.PERMISSION_GRANTED) {


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


        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");


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




        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<Image> imageCall = shopService.uploadImage(PrefLogin.getAuthorizationHeaders(getContext()),
                requestBodyBinary);


        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {


                if(isDestroyed)
                {
                    return;
                }


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


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


            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);




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
            }
        });

    }



    void deleteImage(String filename)
    {


        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<ResponseBody> call = shopService.deleteImage(
                PrefLogin.getAuthorizationHeaders(getContext()),
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



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                showToastMessage("Image Delete failed");



                if(isDestroyed)
                {
                    return;
                }


//                showToastMessage("Image Delete failed");


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }



    // code for picking up location



    private int REQUEST_CODE_PICK_LAT_LON = 23;

    @OnClick(R.id.pick_location_button)
    void pickLocationClick()
    {

//        Intent intent = new Intent(getActivity(),PickLocationActivity.class);
//        startActivityForResult(intent,REQUEST_CODE_PICK_LAT_LON);
    }





    // Map Setup



//    private GoogleMap mMap;
//    Marker currentMarker;



//    void setupMap()
//    {
//        SupportMapFragment mapFragment1 = new SupportMapFragment();
//
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .add(R.id.map,mapFragment1).commit();
//
//        mapFragment1.getMapAsync(this);
//    }



//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        mMap = googleMap;
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
//
//            return;
//        }
//
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMapToolbarEnabled(true);
////        mMap.getUiSettings().setAllGesturesEnabled(true);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//
//
////        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(,14));
//
////        Location currentLocation = UtilityLocationOld.getCurrentLocation(this);
//
//        if(shop!=null)
//        {
//            LatLng latLng = new LatLng(shop.getLatCenter(),shop.getLonCenter());
//
//            Circle circle = mMap
//                    .addCircle(
//                            new CircleOptions()
//                                    .center(latLng)
//                                    .radius(shop.getDeliveryRange()*1000)
//                                    .fillColor(0x11000000)
//                                    .strokeWidth(1)
//                                    .strokeColor(R.color.buttonColorDark)
//                    );
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,getZoomLevel(circle)));
//            //
//
//            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(shop.getShopName()));
////                mMap.moveCamera();
////            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//            currentMarker.showInfoWindow();
//
//        }
//    }



//    public int getZoomLevel(Circle circle)
//    {
//        int zoomLevel = 11;
//        if (circle != null)
//        {
//            double radius = circle.getRadius();
//            double scale = radius / 500;
//            zoomLevel = (int) Math.floor((16 - Math.log(scale) / Math.log(2)));
//        }
//        return zoomLevel ;
//    }



    @OnClick(R.id.get_directions_button)
    void getDirections()
    {
        String str_latitude = String.valueOf(shop.getLatCenter());
        String str_longitude = String.valueOf(shop.getLonCenter());

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + str_latitude +  "," + str_longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }








    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }







    @OnClick(R.id.add_credit)
    void addCredit()
    {
//        showToastMessage("Add Credit !");
        Intent intent = new Intent(getActivity(), AddCredit.class);
        intent.putExtra("tag_user_id",shop.getShopAdminID());
        startActivity(intent);
    }


}
