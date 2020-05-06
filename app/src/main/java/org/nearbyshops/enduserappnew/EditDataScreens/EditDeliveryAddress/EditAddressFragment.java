package org.nearbyshops.enduserappnew.EditDataScreens.EditDeliveryAddress;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

import org.nearbyshops.enduserappnew.API.DeliveryAddressService;
import org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerMapbox.PickLocation;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
//import org.nearbyshops.enduserappnew.LocationPicker.PickLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;



public class EditAddressFragment extends Fragment {



    private DeliveryAddress deliveryAddress;

    public static final String DELIVERY_ADDRESS_INTENT_KEY = "edit_delivery_address_intent_key";

    @Inject
    DeliveryAddressService deliveryAddressService;

    @BindView(R.id.saveButton) TextView updateDeliveryAddress;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    // address Fields
    @BindView(R.id.receiversName) EditText receiversName;
    @BindView(R.id.receiversPhoneNumber) EditText receiversPhoneNumber;
    @BindView(R.id.deliveryAddress) EditText deliveryAddressView;
    @BindView(R.id.addressCity) EditText city;
    @BindView(R.id.pincode) EditText pincode;
    @BindView(R.id.landmark) EditText landMark;
    @BindView(R.id.latitude) EditText latitude;
    @BindView(R.id.longitude) EditText longitude;



    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;



    public EditAddressFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_address, container, false);
        ButterKnife.bind(this,rootView);

//        setContentView(R.layout.activity_edit_address);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);




        if(current_mode ==MODE_UPDATE)
        {
            String jsonString = getActivity().getIntent().getStringExtra(DELIVERY_ADDRESS_INTENT_KEY);
            deliveryAddress = UtilityFunctions.provideGson().fromJson(jsonString,DeliveryAddress.class);

            bindDataToViews();
        }


        setActionBarTitle();

        return rootView;
    }











    private void setActionBarTitle()
    {
        if(getActivity() instanceof AppCompatActivity)
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

            if(actionBar!=null)
            {
                if(current_mode == MODE_ADD)
                {
                    actionBar.setTitle("Add Delivery Address");
                }
                else if(current_mode==MODE_UPDATE)
                {
                    actionBar.setTitle("Edit Delivery Address");
                }

            }
        }


        if(current_mode==MODE_ADD)
        {
            updateDeliveryAddress.setText("Add");
        }
        else if(current_mode==MODE_UPDATE)
        {
            updateDeliveryAddress.setText("Save");
        }


    }





    private void getDataFromViews()
    {
        if(deliveryAddress!=null)
        {
            deliveryAddress.setName(receiversName.getText().toString());
            deliveryAddress.setDeliveryAddress(deliveryAddressView.getText().toString());
            deliveryAddress.setCity(city.getText().toString());
            deliveryAddress.setPincode(Long.parseLong(pincode.getText().toString()));
            deliveryAddress.setLandmark(landMark.getText().toString());
            deliveryAddress.setPhoneNumber(Long.parseLong(receiversPhoneNumber.getText().toString()));


            deliveryAddress.setLatitude(Double.parseDouble(latitude.getText().toString()));
            deliveryAddress.setLongitude(Double.parseDouble(longitude.getText().toString()));
        }
    }





    private void bindDataToViews()
    {
        if(deliveryAddress!=null)
        {
            receiversName.setText(deliveryAddress.getName());
            deliveryAddressView.setText(deliveryAddress.getDeliveryAddress());
            city.setText(deliveryAddress.getCity());
            pincode.setText(String.valueOf(deliveryAddress.getPincode()));
            landMark.setText(deliveryAddress.getLandmark());
            receiversPhoneNumber.setText(String.valueOf(deliveryAddress.getPhoneNumber()));

            latitude.setText(String.valueOf(deliveryAddress.getLatitude()));
            longitude.setText(String.valueOf(deliveryAddress.getLongitude()));

        }
    }







    @OnClick(R.id.saveButton)
    void updateAddressClick(View view)
    {

        if(!validateData())
        {
            return;
        }

        if(current_mode == MODE_ADD)
        {
            addDeliveryAddress();
        }
        else if(current_mode == MODE_UPDATE)
        {
            updateAddress();
        }

    }







    private boolean validateData()
    {
        boolean isValid = true;



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




        if(pincode.getText().toString().length()==0)
        {
            pincode.setError("Pincode cannot be empty !");
            pincode.requestFocus();
            isValid = false;
        }

        if(receiversPhoneNumber.getText().toString().length()==0)
        {
            receiversPhoneNumber.setError("Phone number cannot be empty !");
            receiversPhoneNumber.requestFocus();
            isValid = false;
        }


        return isValid;
    }








    private void addDeliveryAddress()
    {
        if(PrefLogin.getUser(getActivity())==null)
        {
            showToastMessage("Please login to use this feature !");
            return;
        }


        if(deliveryAddress==null)
        {
            deliveryAddress = new DeliveryAddress();
        }

        getDataFromViews();
        deliveryAddress.setEndUserID(PrefLogin.getUser(getActivity()).getUserID());

        progressBar.setVisibility(View.VISIBLE);
        updateDeliveryAddress.setVisibility(View.INVISIBLE);



        Call<DeliveryAddress> call = deliveryAddressService.postAddress(deliveryAddress);
        call.enqueue(new Callback<DeliveryAddress>() {
            @Override
            public void onResponse(Call<DeliveryAddress> call, Response<DeliveryAddress> response) {


                if (response.code() == 201) {

                    showToastMessage("Added Successfully !");

                    current_mode = MODE_UPDATE;
//                    updateIDFieldVisibility();
                    deliveryAddress = response.body();
//                    bindDataToViews();

                    setActionBarTitle();



                    if(getActivity()!=null)
                    {
                        getActivity().finish();
                    }

                }
                else
                {
                    showToastMessage("Unsuccessful !");
                }



                progressBar.setVisibility(View.INVISIBLE);
                updateDeliveryAddress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<DeliveryAddress> call, Throwable t) {

                showToastMessage("Network Connection Failed !");


                progressBar.setVisibility(View.INVISIBLE);
                updateDeliveryAddress.setVisibility(View.VISIBLE);
            }
        });
    }







    private void updateAddress()
    {
        getDataFromViews();

        Call<ResponseBody> call = deliveryAddressService.putAddress(deliveryAddress,deliveryAddress.getId());


        progressBar.setVisibility(View.VISIBLE);
        updateDeliveryAddress.setVisibility(View.INVISIBLE);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                }
                else
                {
                    showToastMessage("failed to update !");
                }



                progressBar.setVisibility(View.INVISIBLE);
                updateDeliveryAddress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network connection failed !");



                progressBar.setVisibility(View.INVISIBLE);
                updateDeliveryAddress.setVisibility(View.VISIBLE);
            }
        });
    }


    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }







    private int REQUEST_CODE_PICK_LAT_LON = 23;


    @OnClick(R.id.pick_location_button)
    void pickLocationClick()
    {
//        Intent intent = new Intent(getActivity(),PickLocationActivity.class);
//        startActivityForResult(intent,REQUEST_CODE_PICK_LAT_LON);


        Intent intent = null;


        if(getResources().getBoolean(R.bool.use_google_maps))
        {
            intent = new Intent(getActivity(), GooglePlacePicker.class);
        }
        else
        {
            intent = new Intent(getActivity(), PickLocation.class);
        }



//        Intent intent = new Intent(getActivity(), PickLocation.class);


        intent.putExtra("lat_dest",Double.parseDouble(latitude.getText().toString()));
        intent.putExtra("lon_dest",Double.parseDouble(longitude.getText().toString()));
        startActivityForResult(intent,3);
    }




    @OnClick(R.id.navigate_button)
    void navigateButton()
    {
        String str_latitude = latitude.getText().toString();
        String str_longitude = longitude.getText().toString();

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + str_latitude +  "," + str_longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_LAT_LON)
        {
            latitude.setText(String.valueOf(data.getDoubleExtra("latitude",0)));
            longitude.setText(String.valueOf(data.getDoubleExtra("longitude",0)));
        }
        else if(requestCode==3 && resultCode==6)
        {
            latitude.setText(String.valueOf(data.getDoubleExtra("lat_dest",0.0)));
            longitude.setText(String.valueOf(data.getDoubleExtra("lon_dest",0.0)));
        }

    }




}
