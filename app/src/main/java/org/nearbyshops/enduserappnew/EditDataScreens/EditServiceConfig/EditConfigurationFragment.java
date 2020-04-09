package org.nearbyshops.enduserappnew.EditDataScreens.EditServiceConfig;


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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import org.nearbyshops.enduserappnew.API.API_SDS.ServiceConfigService;
import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.PlacePickerMapbox.PlacePickerWithRadius.PickDeliveryRange;
import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


public class EditConfigurationFragment extends Fragment {




    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;



    @Inject
    ServiceConfigurationService configurationService;

    @Inject
    UserService userService;



    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


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


    public static final String STAFF_INTENT_KEY = "staff_intent_key";
    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    private int current_mode = MODE_UPDATE;


    private ServiceConfigurationLocal serviceConfiguration = null;


    public EditConfigurationFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


//    Subscription editTextSub;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_service, container, false);

        ButterKnife.bind(this,rootView);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setupSpinners();






        if(savedInstanceState==null)
        {

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_UPDATE);

            if(current_mode == MODE_UPDATE)
            {
                serviceConfiguration = PrefServiceConfig.getServiceConfigLocal(getActivity());
            }


            if(serviceConfiguration!=null) {

                bindDataToViews();
            }


            showLogMessage("Inside OnCreateView - Saved Instance State !");
        }



//        if(validator==null)
//        {
//            validator = new Validator(this);
//            validator.setValidationListener(this);
//        }

        updateIDFieldVisibility();


        if(serviceConfiguration!=null) {
            loadImage(serviceConfiguration.getLogoImagePath());
            showLogMessage("Inside OnCreateView : DeliveryGUySelf : Not Null !");
        }


        showLogMessage("Inside On Create View !");

        return rootView;
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
        }
        else if(current_mode== MODE_UPDATE)
        {
            item_id.setVisibility(View.VISIBLE);
            buttonUpdateItem.setText("Save");
        }
    }





    public static final String TAG_LOG = "TAG_LOG";


    private void showLogMessage(String message)
    {
        Log.i(TAG_LOG,message);
        System.out.println(message);
    }






    private void loadImage(String imagePath) {

        String iamgepath = PrefGeneral.getServiceURL(getContext()) + "/api/ServiceConfiguration/Image/" + imagePath;

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
            serviceConfiguration = new ServiceConfigurationLocal();
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
//            retrofitPOSTRequest();
        }

    }









    private void update()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            deleteImage(serviceConfiguration.getLogoImagePath());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {
                serviceConfiguration.setLogoImagePath(null);
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
        if(serviceConfiguration!=null) {

            item_id.setText(String.valueOf(serviceConfiguration.getServiceID()));
            service_name.setText(serviceConfiguration.getServiceName());
            helpline_number.setText(serviceConfiguration.getHelplineNumber());
//            spinnerServiceType.setSelection(serviceConfiguration.getServiceType()-1);
//            spinnerServiceLevel.setSelection(serviceConfiguration.getServiceLevel()-1);
            address.setText(serviceConfiguration.getAddress());
            city.setText(serviceConfiguration.getCity());
            pincode.setText(String.valueOf(serviceConfiguration.getPincode()));
            landmark.setText(serviceConfiguration.getLandmark());
            state.setText(serviceConfiguration.getState());
            spinnerCountry.setSelection(countryCodeList.indexOf(serviceConfiguration.getISOCountryCode()));
            autoComplete.setText(serviceConfiguration.getISOLanguageCode());
            latitude.setText(String.valueOf(serviceConfiguration.getLatCenter()));
            longitude.setText(String.valueOf(serviceConfiguration.getLonCenter()));
            serviceCoverage.setText(String.valueOf(serviceConfiguration.getServiceRange()));

//            latitude.setText(String.format("%.2d",serviceConfiguration.getLatCenter()));
//            longitude.setText(String.format("%.2d",serviceConfiguration.getLonCenter()));
//            serviceCoverage.setText(String.format("%.2d",serviceConfiguration.getServiceRange()));


            descriptionShort.setText(serviceConfiguration.getDescriptionShort());
            descriptionLong.setText(serviceConfiguration.getDescriptionLong());
        }
    }



    private void getDataFromViews()
    {
        if(serviceConfiguration==null)
        {
            if(current_mode == MODE_ADD)
            {
                serviceConfiguration = new ServiceConfigurationLocal();
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



//            serviceConfigurationForEdit.setConfigurationNickname(nickname.getText().toString());
        serviceConfiguration.setServiceName(service_name.getText().toString());
//            serviceConfigurationForEdit.setServiceURL(service_url.getText().toString());
        serviceConfiguration.setHelplineNumber(helpline_number.getText().toString());
//        serviceConfiguration.setServiceType(spinnerServiceType.getSelectedItemPosition() + 1);
//        serviceConfiguration.setServiceLevel(spinnerServiceLevel.getSelectedItemPosition() + 1);
        serviceConfiguration.setAddress(address.getText().toString());
        serviceConfiguration.setCity(city.getText().toString());

        if(!pincode.getText().toString().equals(""))
        {
            serviceConfiguration.setPincode(Long.parseLong(pincode.getText().toString()));
        }


        serviceConfiguration.setLandmark(landmark.getText().toString());
        serviceConfiguration.setState(state.getText().toString());
        serviceConfiguration.setISOCountryCode(countryCodeList.get(spinnerCountry.getSelectedItemPosition()));

        Locale locale = new Locale("", serviceConfiguration.getISOCountryCode());
        serviceConfiguration.setCountry(locale.getDisplayCountry());

        serviceConfiguration.setISOLanguageCode(autoComplete.getText().toString());

        if(!latitude.getText().toString().equals("")&&!longitude.getText().toString().equals(""))
        {
            serviceConfiguration.setLatCenter(Double.parseDouble(latitude.getText().toString()));
            serviceConfiguration.setLonCenter(Double.parseDouble(longitude.getText().toString()));
        }

        if(!serviceCoverage.getText().toString().equals(""))
        {
            serviceConfiguration.setServiceRange(Double.parseDouble(serviceCoverage.getText().toString()));
        }


        serviceConfiguration.setDescriptionShort(descriptionShort.getText().toString());
        serviceConfiguration.setDescriptionLong(descriptionLong.getText().toString());


//        serviceConfiguration.setStyleURL(styleURL.getText().toString());
    }



    public void retrofitPUTRequest()
    {

        getDataFromViews();


        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



//        final Staff staff = UtilityStaff.getStaff(getContext());
        Call<ResponseBody> call = configurationService.putServiceConfiguration(
                PrefLogin.getAuthorizationHeaders(getContext()),
                serviceConfiguration
        );



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");

                    PrefServiceConfig.saveServiceConfigLocal(serviceConfiguration,getContext());
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


        Call<ResponseBody> call = retrofit.create(ServiceConfigService.class).saveService(PrefGeneral.getServiceURL(getActivity()));


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
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
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
            serviceCoverage.setText(String.valueOf((int)result.getDoubleExtra("delivery_range_kms",0)));
        }
        else if(requestCode==3 && resultCode==3)
        {
            latitude.setText(String.valueOf(result.getDoubleExtra("lat_dest",0.0)));
            longitude.setText(String.valueOf(result.getDoubleExtra("lon_dest",0.0)));
            serviceCoverage.setText(String.valueOf(result.getDoubleExtra("radius",0.0)));
        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {

                startCropActivity(result.getData(),getContext());
            }

        }
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(null);
            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;


        }
        else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }



    }





    // upload image after being picked up
    private void startCropActivity(Uri sourceUri, Context context) {



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
                .withMaxResultSize(1200, 1200)
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



        Call<Image> imageCall = configurationService.uploadImage(
                PrefLogin.getAuthorizationHeaders(getContext()),
                requestBodyBinary
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


                    serviceConfiguration.setLogoImagePath(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    serviceConfiguration.setLogoImagePath(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    serviceConfiguration.setLogoImagePath(null);

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
                serviceConfiguration.setLogoImagePath(null);

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

        Intent intent = new Intent(getActivity(), PickDeliveryRange.class);
        intent.putExtra("lat_dest",Double.parseDouble(latitude.getText().toString()));
        intent.putExtra("lon_dest",Double.parseDouble(longitude.getText().toString()));
        intent.putExtra("radius",Double.parseDouble(serviceCoverage.getText().toString()));
        startActivityForResult(intent,3);
    }




    boolean isDestroyed = false;


}
