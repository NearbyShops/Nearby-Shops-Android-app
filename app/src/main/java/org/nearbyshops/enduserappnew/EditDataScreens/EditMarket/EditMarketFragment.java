package org.nearbyshops.enduserappnew.EditDataScreens.EditMarket;


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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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


import org.nearbyshops.enduserappnew.multimarketfiles.API_SDS.MarketService;
import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.AdapterDeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.EditDeliverySlot.EditDeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.EditDeliverySlot.EditDeliverySlotFragment;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.ViewHolderDeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.ViewModelDeliverySlot;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.AddItemData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderAddItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;


public class EditMarketFragment extends Fragment implements ViewHolderDeliverySlot.ListItemClick, ViewHolderAddItem.ListItemClick {




    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;



    @Inject
    ServiceConfigurationService configurationService;

    @Inject
    UserService userService;




    // flag for knowing whether the image is changed or not
    private boolean isImageChanged = false;
    private boolean isImageRemoved = false;


    // bind views
    @BindView(R.id.uploadImage) ImageView resultView;

    @BindView(R.id.item_id) EditText item_id;
    @BindView(R.id.service_name) EditText service_name;
    @BindView(R.id.helpline_number) EditText helpline_number;
    @BindView(R.id.address) EditText address;
    @BindView(R.id.city) EditText city;
    @BindView(R.id.pincode) EditText pincode;
    @BindView(R.id.landmark) EditText landmark;
    @BindView(R.id.state) EditText state;
    @BindView(R.id.auto_complete_language) AutoCompleteTextView autoComplete;
    @BindView(R.id.latitude) EditText latitude;
    @BindView(R.id.longitude) EditText longitude;
    @BindView(R.id.service_coverage) EditText serviceCoverage;
//    @BindView(R.id.getlatlon) Button getlatlon;
//    @BindView(R.id.style_url) EditText styleURL;
    @BindView(R.id.spinner_country) Spinner spinnerCountry;

//    @BindView(R.id.spinner_service_level) Spinner spinnerServiceLevel;
//    @BindView(R.id.spinner_service_type) Spinner spinnerServiceType;

    @BindView(R.id.description_short) EditText descriptionShort;
    @BindView(R.id.description_long) EditText descriptionLong;

    ArrayList<String> countryCodeList = new ArrayList<>();
    ArrayList<String> languageCodeList = new ArrayList<>();

//    @BindView(R.id.saveButton) TextView buttonUpdateItem;

    @BindView(R.id.saveButton) TextView buttonUpdateItem;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    @BindView(R.id.admin_options_block)
    LinearLayout adminOptionsBlock;


    @BindView(R.id.switch_enable)
    Switch marketENable;



    public static final String STAFF_INTENT_KEY = "staff_intent_key";
    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;
    public static final int MODE_UPDATE_BY_SUPER_ADMIN = 53;




    private int current_mode = MODE_UPDATE;


    private Market market = null;


    public EditMarketFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


//    Subscription editTextSub;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_market, container, false);

        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setupSpinners();






        if(savedInstanceState==null)
        {

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_UPDATE);

            if(current_mode == MODE_UPDATE)
            {
                market = PrefServiceConfig.getServiceConfigLocal(getActivity());
            }
            else if(current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
            {
                String jsonString = getActivity().getIntent().getStringExtra("market_profile");
                market = UtilityFunctions.provideGson().fromJson(jsonString, Market.class);
                bindDataToViews();

                getMarketDetailsNew();
            }





            if(market !=null) {

                bindDataToViews();
            }

        }





        setupViewModel();
        setupRecyclerView();
        getDeliverySlots();


        updateIDFieldVisibility();


        if(market !=null) {
            loadImage();
        }

        return rootView;
    }








    private void getMarketDetailsNew()
    {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait ... getting market Details !");
        pd.show();


        Call<Market> call = null;


        if(current_mode==MODE_UPDATE)
        {
            call = configurationService.getServiceConfiguration(null,null);


        }
        else if(current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();



            call = retrofit.create(MarketService.class).getMarketDetails(
                    market.getServiceID(),
                    PrefLocation.getLatitude(MyApplication.getAppContext()), PrefLocation.getLongitude(MyApplication.getAppContext())
            );


        }
        else
        {
            return;
        }



        call.enqueue(new Callback<Market>() {
            @Override
            public void onResponse(Call<Market> call, Response<Market> response) {

                if (!isVisible()) {
                    return;
                }

                pd.dismiss();


                if (response.code() == 200) {
                    market = response.body();

                    bindDataToViews();

                } else {
                    showToastMessage("Failed to get Details : Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Market> call, Throwable t) {


                pd.dismiss();

                System.out.println("Check your network !");
            }
        });

    }





    private void setupSpinners()
    {
        // setup spinners

        ArrayList<String> spinnerList = new ArrayList<>();
        ArrayList<String> spinnerListLanguages = new ArrayList<>();


        String[] locales = Locale.getISOCountries();



        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

//            System.out.println("Country Code = " + obj.getCountry()
//                    + ", Country Name = " + obj.getDisplayCountry());

            spinnerList.add(obj.getCountry() + " : " + obj.getDisplayCountry());

            countryCodeList.add(obj.getCountry());
        }


        for(String string: Locale.getISOLanguages())
        {
            Locale locale = new Locale(string,"");

            spinnerListLanguages.add(locale.getDisplayLanguage());

            languageCodeList.add(locale.getDisplayLanguage());
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, spinnerList);

        spinnerCountry.setAdapter(adapter);

        ArrayAdapter<String> adapterLanguages = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,spinnerListLanguages);

        autoComplete.setAdapter(adapterLanguages);
//        autoComplete.setValidator(new Validator());


        // setup spinner ends
    }



    private void updateIDFieldVisibility()
    {
        if(current_mode==MODE_ADD)
        {
            buttonUpdateItem.setText("Create Account");
            item_id.setVisibility(View.GONE);
            adminOptionsBlock.setVisibility(View.GONE);
        }
        else if(current_mode== MODE_UPDATE)
        {
            item_id.setVisibility(View.VISIBLE);
            buttonUpdateItem.setText("Save");
            adminOptionsBlock.setVisibility(View.GONE);

        }
        else if(current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
        {
            adminOptionsBlock.setVisibility(VISIBLE);
        }
    }





    public static final String TAG_LOG = "TAG_LOG";


    private void showLogMessage(String message)
    {
        Log.i(TAG_LOG,message);
        System.out.println(message);
    }






    private void loadImage() {

        String iamgepath = PrefGeneral.getServiceURL(getContext()) + "/api/ServiceConfiguration/Image/" + market.getLogoImagePath();



        if(current_mode==MODE_UPDATE)
        {
            iamgepath = PrefGeneral.getServiceURL(getContext()) + "/api/ServiceConfiguration/Image/" + market.getLogoImagePath();
        }
        else if(current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
        {

            iamgepath = PrefServiceConfig.getServiceURL_SDS(getActivity())
                    + "/api/v1/Markets/Image/three_hundred_" + market.getLogoImagePath() + ".jpg";
        }





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
            market = new Market();
            addAccount();
        }
        else if(current_mode == MODE_UPDATE || current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
        {
            update();
        }
    }






    private boolean validateData()
    {
        boolean isValid = true;
/*
        if(phone.getText().toString().length()==0)
        {
            phone.setError("Please enter Phone Number");
            phone.requestFocus();
            isValid= false;
        }


        if(password.getText().toString().length()==0)
        {
            password.requestFocus();
            password.setError("Password cannot be empty");
            isValid = false;
        }


        if(username.getText().toString().length()==0)
        {
            username.requestFocus();
            username.setError("Username cannot be empty");
            isValid= false;
        }


        if(name.getText().toString().length()==0)
        {

//            Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_close_black_24dp);
            name.requestFocus();
            name.setError("Name cannot be empty");
            isValid = false;
        }
*/


        return isValid;
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();

    }





    private void addAccount()
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
//            retrofitPOSTRequest();
        }

    }









    private void update()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            deleteImage(market.getLogoImagePath());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {
                market.setLogoImagePath(null);
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



    private void bindDataToViews()
    {
        if(market !=null) {

            item_id.setText(String.valueOf(market.getServiceID()));
            service_name.setText(market.getServiceName());
            helpline_number.setText(market.getHelplineNumber());
//            spinnerServiceType.setSelection(serviceConfiguration.getServiceType()-1);
//            spinnerServiceLevel.setSelection(serviceConfiguration.getServiceLevel()-1);
            address.setText(market.getAddress());
            city.setText(market.getCity());
            pincode.setText(String.valueOf(market.getPincode()));
            landmark.setText(market.getLandmark());
            state.setText(market.getState());
            spinnerCountry.setSelection(countryCodeList.indexOf(market.getISOCountryCode()));
            autoComplete.setText(market.getISOLanguageCode());
            latitude.setText(String.valueOf(market.getLatCenter()));
            longitude.setText(String.valueOf(market.getLonCenter()));
            serviceCoverage.setText(String.valueOf(market.getServiceRange()));

//            latitude.setText(String.format("%.2d",serviceConfiguration.getLatCenter()));
//            longitude.setText(String.format("%.2d",serviceConfiguration.getLonCenter()));
//            serviceCoverage.setText(String.format("%.2d",serviceConfiguration.getServiceRange()));


            descriptionShort.setText(market.getDescriptionShort());
            descriptionLong.setText(market.getDescriptionLong());

            marketENable.setChecked(market.getVerified());
        }
    }





    private void getDataFromViews()
    {
        if(market ==null)
        {
            if(current_mode == MODE_ADD)
            {
                market = new Market();
            }
            else
            {
                return;
            }
        }



        market.setVerified(marketENable.isChecked());
        market.setOfficial(marketENable.isChecked());


        System.out.println("Is Enabled : " + market.getVerified());



//            serviceConfigurationForEdit.setConfigurationNickname(nickname.getText().toString());
        market.setServiceName(service_name.getText().toString());
//            serviceConfigurationForEdit.setServiceURL(service_url.getText().toString());
        market.setHelplineNumber(helpline_number.getText().toString());
//        serviceConfiguration.setServiceType(spinnerServiceType.getSelectedItemPosition() + 1);
//        serviceConfiguration.setServiceLevel(spinnerServiceLevel.getSelectedItemPosition() + 1);
        market.setAddress(address.getText().toString());
        market.setCity(city.getText().toString());

        if(!pincode.getText().toString().equals(""))
        {
            market.setPincode(Long.parseLong(pincode.getText().toString()));
        }


        market.setLandmark(landmark.getText().toString());
        market.setState(state.getText().toString());
        market.setISOCountryCode(countryCodeList.get(spinnerCountry.getSelectedItemPosition()));

        Locale locale = new Locale("", market.getISOCountryCode());
        market.setCountry(locale.getDisplayCountry());

        market.setISOLanguageCode(autoComplete.getText().toString());

        if(!latitude.getText().toString().equals("")&&!longitude.getText().toString().equals(""))
        {
            market.setLatCenter(Double.parseDouble(latitude.getText().toString()));
            market.setLonCenter(Double.parseDouble(longitude.getText().toString()));
        }

        if(!serviceCoverage.getText().toString().equals(""))
        {
            market.setServiceRange(Double.parseDouble(serviceCoverage.getText().toString()));
        }


        market.setDescriptionShort(descriptionShort.getText().toString());
        market.setDescriptionLong(descriptionLong.getText().toString());


//        serviceConfiguration.setStyleURL(styleURL.getText().toString());
    }



    public void retrofitPUTRequest()
    {

        getDataFromViews();


        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



//        final Staff staff = UtilityStaff.getStaff(getContext());
        Call<ResponseBody> call = null;


        if(current_mode==MODE_UPDATE)
        {
            call = configurationService.putServiceConfiguration(
                    PrefLogin.getAuthorizationHeaders(getContext()),
                    market
            );
        }
        else if(current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
        {

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();


            System.out.println(UtilityFunctions.provideGson().toJson(market));

            call = retrofit.create(MarketService.class).updateMarket(
                    PrefLoginGlobal.getAuthorizationHeaders(getActivity()),
                    market,market.getServiceID()
            );


        }
        else
        {


            showToastMessage("Current Mode : " + current_mode);
            buttonUpdateItem.setVisibility(VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }





        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");


                    if(current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
                    {

                    }
                    else
                    {
                        PrefServiceConfig.saveServiceConfigLocal(market,getContext());
                    }


//                    updateMarketEntry();

                }
                else
                {
                    showToastMessage("Update Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                showToastMessage("Update Failed !");
            }
        });
    }








    @Inject
    Gson gson;



    private void updateMarketEntry()
    {



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();


        Call<ResponseBody> call = retrofit.create(MarketService.class).saveService(PrefGeneral.getServiceURL(getActivity()));


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(!isVisible())
                {
                    return;
                }

                if(response.code()==200)
                {
                    showToastMessage("SDS Entry Updated Successfully !");
                }
                else if(response.code()==201)
                {
                    showToastMessage("SDS Entry Added Successfully !");
                }
                else
                {
                    showToastMessage("SDS Update Failed ... Error Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(!isVisible())
                {
                    return;
                }

                showToastMessage("SDS Update Failed !");
            }
        });
    }




//    void retrofitPOSTRequest()
//    {
//        getDataFromViews();
//
////        final Staff staffTemp = UtilityStaff.getStaff(getContext());
//        Call<Staff> call = serviceConfiguration.post(UtilityLogin.getAuthorizationHeaders(getContext()),staff);
//
//        call.enqueue(new Callback<Staff>() {
//            @Override
//            public void onResponse(Call<Staff> call, Response<Staff> response) {
//
//                if(response.code()==201)
//                {
//                    showToastMessage("Add successful !");
//
//                    current_mode = MODE_UPDATE;
//                    updateIDFieldVisibility();
//                    staff = response.body();
//                    bindDataToViews();
//
//                    UtilityStaff.saveStaff(staff,getContext());
//
//                }
//                else
//                {
//                    showToastMessage("Add failed !");
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Staff> call, Throwable t) {
//
//                showToastMessage("Add failed !");
//
//            }
//        });
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }







    /*
        Utility Methods
     */

    private void showToastMessage(String message)
    {
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



        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_LAT_LON)
        {
            latitude.setText(String.valueOf(result.getDoubleExtra("latitude",0)));
            longitude.setText(String.valueOf(result.getDoubleExtra("longitude",0)));
            serviceCoverage.setText(String.valueOf((int)result.getDoubleExtra("delivery_range_kms",0)));
        }
        else if(requestCode==3 && resultCode==6)
        {
//            else if(requestCode==3 && resultCode==3)
            latitude.setText(String.valueOf(result.getDoubleExtra("lat_dest",0.0)));
            longitude.setText(String.valueOf(result.getDoubleExtra("lon_dest",0.0)));
            serviceCoverage.setText(String.valueOf(result.getDoubleExtra("radius",10)));


        }
        else if (resultCode == Activity.RESULT_OK) {
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

//                    showToastMessage("Permission Granted !");
                    pickShopImage();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {


                    showToastMessage("Permission Denied for Reading External Storage ! ");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }







    private void uploadPickedImage(final boolean isModeEdit)
    {

        Log.d("applog", "onClickUploadImage");


        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



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



        Call<Image> imageCall = configurationService.uploadImage(
                PrefLogin.getAuthorizationHeaders(getContext()),
                fileToUpload
        );



        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {

                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");

                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());


                    market.setLogoImagePath(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    market.setLogoImagePath(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    market.setLogoImagePath(null);

                }

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
//                    retrofitPOSTRequest();
                }


            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                showToastMessage("Image Upload failed !");
                market.setLogoImagePath(null);

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
//                    retrofitPOSTRequest();
                }
            }
        });

    }



    private void deleteImage(String filename)
    {

//        buttonUpdateItem.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = configurationService.deleteImage(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                filename
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


//                buttonUpdateItem.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);


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

//                buttonUpdateItem.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);


//                showToastMessage("Image Delete failed");
            }
        });
    }



    // Pick location code
    private int REQUEST_CODE_PICK_LAT_LON = 23;




    @OnClick(R.id.pick_location_button)
    void pickLocationClick()
    {

//        Intent intent = new Intent(getActivity(), PickDeliveryRange.class);
//        intent.putExtra("lat_dest",Double.parseDouble(latitude.getText().toString()));
//        intent.putExtra("lon_dest",Double.parseDouble(longitude.getText().toString()));
//        intent.putExtra("radius",Double.parseDouble(serviceCoverage.getText().toString()));
//        startActivityForResult(intent,3);


        Intent intent = new Intent(getActivity(), GooglePlacePicker.class);
        intent.putExtra("lat_dest",Double.parseDouble(latitude.getText().toString()));
        intent.putExtra("lon_dest",Double.parseDouble(longitude.getText().toString()));
        intent.putExtra("radius",Double.parseDouble(serviceCoverage.getText().toString()));

        startActivityForResult(intent,3);
    }




    boolean isDestroyed = false;










    ViewModelDeliverySlot viewModelDeliverySlot;


    private void setupViewModel()
    {

        viewModelDeliverySlot = new ViewModelDeliverySlot(MyApplication.application);


        viewModelDeliverySlot.getData().observe(getViewLifecycleOwner(), new Observer<List<Object>>() {
            @Override
            public void onChanged(List<Object> objects) {


                dataset.clear();

                dataset.add(0,new AddItemData());
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
    @BindView(R.id.delivery_slot_list)
    RecyclerView itemImagesList;
    AdapterDeliverySlot adapterDeliverySlot;



    private void setupRecyclerView() {

        adapterDeliverySlot = new AdapterDeliverySlot(dataset,getActivity(),this, ViewHolderDeliverySlot.MODE_SHOP_ADMIN);
        itemImagesList.setAdapter(adapterDeliverySlot);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        itemImagesList.setLayoutManager(layoutManager);
    }







    void getDeliverySlots()
    {
//        viewModelDeliverySlot.fetchDeliverySlots(null,true,
//                null, DeliverySlot.SLOT_ID
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
        intent.putExtra(EditDeliverySlotFragment.ACCESS_MODE_INTENT_KEY,EditDeliverySlotFragment.MODE_ACCESS_BY_MARKET_ADMIN);

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
        intent.putExtra(EditDeliverySlotFragment.ACCESS_MODE_INTENT_KEY,EditDeliverySlotFragment.MODE_ACCESS_BY_MARKET_ADMIN);

        startActivity(intent);
    }




}
