package org.nearbyshops.whitelabelapp.PlacePickers.PlacePickerGoogleMaps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelUtility.LocationWithAddress;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.R;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sumeet on 4/1/18.
 */


public class AddressPickerFragment extends Fragment {

    @Inject Gson gson;

    String searchText;


    private GoogleMap googleMapInstance;


    private LocationRequest mLocationRequest;
    private LocationWithAddress selectedLocation = new LocationWithAddress();
    private LocationWithAddress selectedFromGeocoder = new LocationWithAddress();





    @BindView(R.id.fab_current)
    FloatingActionButton fabCurrent;
//    @BindView(R.id.progress_bar_selected) ProgressBar progressBarSelected;

    @BindView(R.id.instructions) TextView instructionsText;
    @BindView(R.id.instructions_box) LinearLayout instructionsBox;



    private boolean isDestroyed;


    @BindView(R.id.pickup) TextView pickupAddress;

//    @BindView(R.id.progress_bar_pickup) ProgressBar progressPickup;
//    @BindView(R.id.distance_from_pickup) TextView distanceFromPickup;

    @BindView(R.id.done_button) TextView doneButton;


    private LatLng cameraPositionCurrent;



    public AddressPickerFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_place_picker_google, container, false);
        ButterKnife.bind(this,rootView);

        initializeMapGoogle();

//        selectedFromGeocoder.setLocationAddress();
//        selectedFromGeocoder.setLocationName("Current Location");

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


                    double lat = 0;
                    double lon = 0;


                    lat = getActivity().getIntent().getDoubleExtra("lat_dest",0);
                    lon = getActivity().getIntent().getDoubleExtra("lon_dest",0);



                    if(lat==0 && lon == 0)
                    {
                        lat = PrefLocation.getLatitudeSelected(getActivity());
                        lon = PrefLocation.getLongitudeSelected(getActivity());
                    }
                    else
                    {
                        selectedFromGeocoder.setLatitude(lat);
                        selectedFromGeocoder.setLongitude(lon);
                        selectedFromGeocoder.setLocationName(getActivity().getIntent().getStringExtra("name"));
                        selectedFromGeocoder.setLocationAddress(getActivity().getIntent().getStringExtra("address"));

//                        if(selectedFromGeocoder.getLocationName()==null)
//                        {
//                            selectedFromGeocoder.setLocationName("Selected Location");
//                        }
//
//                        if(selectedFromGeocoder.getLocationAddress()==null)
//                        {
//                            selectedFromGeocoder.setLocationAddress("Current Location");
//                        }



                    }









                    if(lat!=0 && lon!=0)
                    {


                        googleMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lat,lon),17
                                )
                        );

                    }




                    googleMapInstance.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                        @Override
                        public void onCameraMove() {


                            cameraPositionCurrent = googleMapInstance.getCameraPosition().target;



                            selectedLocation.setLatitude(cameraPositionCurrent.latitude);
                            selectedLocation.setLongitude(cameraPositionCurrent.longitude);



                            selectedLocation.setLocationName("Selected Location : ");

                            if(selectedLocation.distanceFrom(selectedFromGeocoder.getLatitude(),selectedFromGeocoder.getLongitude())<300
                                    && selectedFromGeocoder.getLocationAddress()!=null)
                            {

                                selectedLocation.setLocationAddress(selectedFromGeocoder.getLocationAddress());
                            }
                            else
                            {
//                                selectedLocation.setLocationAddress("" +
//                                        "Lat : " + String.format("%.4f",cameraPositionCurrent.latitude)
//                                        + " | Lon : " + String.format("%.4f",cameraPositionCurrent.longitude)
//                                );

                                selectedLocation.setLocationName("");
                                selectedLocation.setLocationAddress(" - ");
                            }


                            pickupAddress.setText(selectedLocation.getLocationName() + "\n" + selectedLocation.getLocationAddress());

                        }
                    });


                }
            });
        }

    }








    @OnClick(R.id.done_button)
    void finishButton()
    {


        Intent intentData = new Intent();
        intentData.putExtra("lat_dest",selectedLocation.getLatitude());
        intentData.putExtra("lon_dest",selectedLocation.getLongitude());
        intentData.putExtra("locationName",selectedLocation.getLocationName());
        intentData.putExtra("address",selectedLocation.getLocationAddress());
        intentData.putExtra("radius",10);


        if(getActivity()!=null)
        {
            getActivity().setResult(6,intentData);
            getActivity().finish();
        }

    }








    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(), message,Toast.LENGTH_SHORT).show();
    }



    void showlog(String message)
    {
        Log.d("place_picker_map",message);
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









    private CountDownTimer countDownTimerPickup = new CountDownTimer(1000, 1000) {

        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {

//            reverseGeocodeMapzenPickup();

        }
    };













    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }





    @Override
    public void onPause() {
        super.onPause();

        if(getActivity()!=null)
        {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }










    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }







    private Marker selectedPoint;




    void showLogMessage(String logMessage)
    {
        Log.d("place_picker",logMessage);
    }







    private void updateMapPosition(double lat, double lon)
    {


        if(selectedPoint !=null)
        {
            selectedPoint.remove();
            selectedPoint=null;
        }



        LatLng point = new LatLng(
                lat,
                lon
        );



        selectedPoint = googleMapInstance.addMarker(
                new MarkerOptions()
                        .position(point)
                        .title("Pickup")

        );

        googleMapInstance.animateCamera(
                CameraUpdateFactory.newLatLngZoom(point,17)
        );

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
//                Place place = PlacePicker.getPlace(getActivity(), data);
//                String toastMsg = String.format("Place: %s", place.getName());
//                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();


//                selectedLocation = new LocationWithAddress();
                selectedFromGeocoder.setLocationName(place.getName().toString());
                selectedFromGeocoder.setLocationAddress(place.getAddress().toString());
                selectedFromGeocoder.setLatitude(place.getLatLng().latitude);
                selectedFromGeocoder.setLongitude(place.getLatLng().longitude);

                selectedLocation.setLocationName(place.getName().toString());
                selectedLocation.setLocationAddress(place.getAddress().toString());
                selectedLocation.setLatitude(place.getLatLng().latitude);
                selectedLocation.setLongitude(place.getLatLng().longitude);



//                PreferenceTaxiFiltersLocal.savePickUpLocation(pickupAddressLocal,getActivity());
//                PreferenceTaxiFiltersLocal.savePickupSetByUser(getActivity(),true);

                displayPickupLocation();




                updateMapPosition(selectedFromGeocoder.getLatitude(),selectedFromGeocoder.getLongitude());


            }
        }
    }










    private void displayPickupLocation()
    {
        if(selectedLocation!=null)
        {
//            pickupAddress.setText(selectedLocation.getLocationName());
            pickupAddress.setText(selectedLocation.getLocationAddress());
//
        }
        else
        {
            pickupAddress.setText("Location not Selected");
        }

    }









}
