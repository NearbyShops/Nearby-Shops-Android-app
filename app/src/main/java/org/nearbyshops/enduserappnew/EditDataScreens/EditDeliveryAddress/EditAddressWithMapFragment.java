package org.nearbyshops.enduserappnew.EditDataScreens.EditDeliveryAddress;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.nearbyshops.enduserappnew.API.DeliveryAddressService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class EditAddressWithMapFragment extends Fragment {



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
//    @BindView(R.id.addressCity) EditText city;
//    @BindView(R.id.pincode) EditText pincode;
//    @BindView(R.id.landmark) EditText landMark;
//    @BindView(R.id.latitude) EditText latitude;
//    @BindView(R.id.longitude) EditText longitude;



    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;





    private GoogleMap googleMapInstance;
    private LatLng cameraPositionCurrent;

    String selectedAddress;
    double selectedLat;
    double selectedLon;





    public EditAddressWithMapFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_address_with_map, container, false);
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



        initializeMapGoogle();



        setActionBarTitle();

        return rootView;
    }









        private void initializeMapGoogle()
        {
            SupportMapFragment mapFragment = (SupportMapFragment)
                    getChildFragmentManager().findFragmentById(R.id.mapview);


            if (mapFragment != null) {



                mapFragment.getMapAsync(new com.google.android.gms.maps.OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        googleMapInstance = googleMap;


                        if(current_mode==MODE_ADD)
                        {
                            double lat = 0;
                            double lon = 0;



                            lat = PrefLocation.getLatitudeCurrent(getActivity());
                            lon = PrefLocation.getLongitudeCurrent(getActivity());



                            if(lat!=0 && lon!=0)
                            {


                                googleMap.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(lat,lon),17
                                        )
                                );

                            }

                        }
                        else
                        {

                            updateMapPosition(deliveryAddress.getLatitude(),deliveryAddress.getLongitude());

                        }








                        googleMapInstance.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                            @Override
                            public void onCameraMove() {


                                cameraPositionCurrent = googleMapInstance.getCameraPosition().target;



                                selectedLat = cameraPositionCurrent.latitude;
                                selectedLon = cameraPositionCurrent.longitude;
                            }
                        });


                    }
                });
            }

        }







        @OnClick(R.id.fab_current)
        void myLocation()
        {

            if(googleMapInstance!=null)
            {



                LatLng latLng = new LatLng(PrefLocation.getLatitudeCurrent(getActivity()),
                        PrefLocation.getLongitudeCurrent(getActivity()));



                googleMapInstance.animateCamera(
                        CameraUpdateFactory.newLatLng(
                                latLng)
                );
            }
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
            deliveryAddress.setPhoneNumber(Long.parseLong(receiversPhoneNumber.getText().toString()));



            if(selectedLat!=0 && selectedLon!=0)
            {
                deliveryAddress.setLatitude(selectedLat);
                deliveryAddress.setLongitude(selectedLon);
            }
        }
    }








    private void bindDataToViews()
    {
        if(deliveryAddress!=null)
        {
            receiversName.setText(deliveryAddress.getName());
            deliveryAddressView.setText(deliveryAddress.getDeliveryAddress());
            receiversPhoneNumber.setText(String.valueOf(deliveryAddress.getPhoneNumber()));

//            latitude.setText(String.valueOf(deliveryAddress.getLatitude()));
//            longitude.setText(String.valueOf(deliveryAddress.getLongitude()));



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


        if(selectedLat==0 && selectedLon==0)
        {
            showToastMessage("Failed ... Please try again !");
            return;
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

                    getActivity().finish();

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

                    getActivity().finish();
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
        UtilityFunctions.showToastMessage(getActivity(),message);
    }







    private int AUTOCOMPLETE_REQUEST_CODE = 1;



    @OnClick(R.id.search_text_wrapper)
    void searchBoxClick()
    {



        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME,Place.Field.LAT_LNG);

// Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(getActivity());


        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);




//        .setCountry("IN")
//            .setLocationBias(RectangularBounds.newInstance(
//                    new com.google.android.gms.maps.model.LatLng(PrefLocation.getLatitude(getActivity()),PrefLocation.getLongitude(getActivity())),
//                    new com.google.android.gms.maps.model.LatLng(PrefLocation.getLatitude(getActivity()),PrefLocation.getLongitude(getActivity()))
//            ))




    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==AUTOCOMPLETE_REQUEST_CODE)
        {

            // place picker pickup address
            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);


                selectedAddress = place.getName().toString() + place.getAddress().toString();
                selectedLat = place.getLatLng().latitude;
                selectedLon = place.getLatLng().longitude;


                updateMapPosition(selectedLat,selectedLon);


            }
        }
    }






    private void updateMapPosition(double lat, double lon)
    {

        LatLng point = new LatLng(
                lat,
                lon
        );

        googleMapInstance.animateCamera(
                CameraUpdateFactory.newLatLngZoom(point,17)
        );

    }






}
