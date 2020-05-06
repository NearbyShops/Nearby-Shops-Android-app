package org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerMapbox;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.location.*;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.nearbyshops.enduserappnew.AppConfig;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.R;


public class PickLocation extends AppCompatActivity {


    public static final int SEEKBAR_CONSTANT = 300;

    MapView mapView;
//    @BindView(R.id.seekbar_map) SeekBar seekbar;
//    @BindView(R.id.label) TextView labelText;

//

//    Polygon polygon;
    double radius;
    LatLng latLng;
    MapboxMap mapboxMapInstance;
    Marker center;


    @BindView(R.id.use_selected_button) TextView useSelectedButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dest_filter_map);
        ButterKnife.bind(this);


//        Mapbox.getInstance(this, "pk.eyJ1Ijoic3VtZWV0");
        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);







//        "https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png"


//        ServiceConfiguration configuration = PrefServiceConfig.getServiceConfig(this);



//        if(configuration!=null)
//        {
//            if(configuration.getStyleURL()!=null)
//            {
//                mapView.setStyleUrl(configuration.getStyleURL()+ "?key=" + APIKeys.openMapTilesMapsKey);
////                showlog(configuration.getStyleURL());
//            }
//        }







        mapView.setStyleUrl(AppConfig.MAPBOX_STYLE_URL);




//        mapView.setStyleUrl("asset://style-cdn-bright.json");

//        mapView.setStyleUrl("http://map-mumbai.triplogic.org/styles/osm-bright/style.json");



        // Add a MapboxMap
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                // Customize map with markers, polylines, etc.


                mapboxMapInstance = mapboxMap;



                mapboxMap.getUiSettings().setAllGesturesEnabled(true);


//                LatLng pointOne = new LatLng(16.4700,78.4520);
//                LatLng pointTwo = new LatLng(17.4500,78.4420);

//                LatLngBounds latLngBounds = new LatLngBounds.Builder()
//                        .include(pointOne)
//                        .include(pointTwo)
//                        .build();
//


                mapboxMap.getUiSettings().setLogoEnabled(false);
                mapboxMap.getUiSettings().setLogoGravity(Gravity.TOP);
                mapboxMap.getUiSettings().setAttributionGravity(Gravity.TOP);
                mapboxMap.getUiSettings().setAttributionEnabled(false);


//                mapboxMap.setMyLocationEnabled(true);
//                mapboxMap.getMyLocation();
//                mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(new LatLng(17.4700,78.4521)));




                showLogMessage("Lat Destination : " + getIntent().getDoubleExtra("lat_dest",0) +
                                "\nLon Destination : " + getIntent().getDoubleExtra("lon_dest",0)
                );






                if(getIntent().getDoubleExtra("lat_dest",0)==0
                        && getIntent().getDoubleExtra("lon_dest",0)==0)
                {



//                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(
//                            new LatLng(latCurrent, lonCurrent)
//                    ),2000);


//                    showLogMessage("Lat Lon Zero");
//                    requestLocationUpdates();

                    latLng = new LatLng(
                            PrefLocation.getLatitude(PickLocation.this),PrefLocation.getLongitude(PickLocation.this)
                    );


                }
                else
                {

                    latLng = new LatLng(
                            getIntent().getDoubleExtra("lat_dest",0),
                            getIntent().getDoubleExtra("lon_dest",0)
                    );


                    showLogMessage("Lat Lon not zero");

                }




                if(center!=null)
                {
                    mapboxMap.removeMarker(center);
                }


                center = mapboxMap.addMarker(new MarkerOptions().setPosition(latLng));


//                    updateMap();


                //
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17),
                        2000
                );





                mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng point) {


                        latLng = point;



                        if(center!=null)
                        {
                            mapboxMap.removeMarker(center);
                        }


                        center = mapboxMap.addMarker(new MarkerOptions().setPosition(point));


                        //
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,17),
                                5000
                        );
                    }
                });



            }
        });


//        requestLocationUpdates();
    }








//
//
//    public int getZoomLevel()
//    {
//        int zoomLevel = 11;
//
//        double scale = radius / 500;
//        zoomLevel = (int) Math.floor((15 - Math.log(scale) / Math.log(2)));
////        zoomLevel = (int) Math.floor((16 - Math.log(scale) / Math.log(2)));
//
//        return zoomLevel ;
//    }
//
//
//
//
//
//    private ArrayList<LatLng> polygonCircleForCoordinate(LatLng location, double radius){
//        int degreesBetweenPoints = 8; //45 sides
//        int numberOfPoints = (int) Math.floor(360 / degreesBetweenPoints);
//        double distRadians = radius / 6371000.0; // earth radius in meters
//        double centerLatRadians = location.getLatitude() * Math.PI / 180;
//        double centerLonRadians = location.getLongitude() * Math.PI / 180;
//        ArrayList<LatLng> polygons = new ArrayList<>(); //array to hold all the points
//        for (int index = 0; index < numberOfPoints; index++) {
//            double degrees = index * degreesBetweenPoints;
//            double degreeRadians = degrees * Math.PI / 180;
//            double pointLatRadians = Math.asin(Math.sin(centerLatRadians) * Math.cos(distRadians) + Math.cos(centerLatRadians) * Math.sin(distRadians) * Math.cos(degreeRadians));
//            double pointLonRadians = centerLonRadians + Math.atan2(Math.sin(degreeRadians) * Math.sin(distRadians) * Math.cos(centerLatRadians),
//                    Math.cos(distRadians) - Math.sin(centerLatRadians) * Math.sin(pointLatRadians));
//            double pointLat = pointLatRadians * 180 / Math.PI;
//            double pointLon = pointLonRadians * 180 / Math.PI;
//            LatLng point = new LatLng(pointLat, pointLon);
//            polygons.add(point);
//        }
//        return polygons;
//    }



    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if(locationCallback!=null)
        {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }



    void showLogMessage(String message)
    {
        Log.d("dest_filter",message);
    }


    void showToastMessage(String message)
    {
        Toast.makeText(PickLocation.this, message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {

        if(latLng!=null)
        {
            Intent intent = new Intent();
            intent.putExtra("lat_dest",latLng.getLatitude());
            intent.putExtra("lon_dest",latLng.getLongitude());
//            intent.putExtra("radius",radius/1000);
            setResult(6,intent);
        }


        super.onBackPressed();
    }







    @OnClick(R.id.use_selected_button)
    void useSelectedClick()
    {

        if(latLng!=null)
        {
            Intent intent = new Intent();
            intent.putExtra("lat_dest",latLng.getLatitude());
            intent.putExtra("lon_dest",latLng.getLongitude());
//            intent.putExtra("radius",radius/1000);
            setResult(3,intent);
        }

        finish();
    }








    LocationCallback locationCallback;
    LocationRequest mLocationRequest;
    double latCurrent;
    double lonCurrent;


    void requestLocationUpdates()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }




        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setSmallestDisplacement(100);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


//        locationCallback = new MyLocationCallback();

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                latCurrent = locationResult.getLastLocation().getLatitude();
                lonCurrent = locationResult.getLastLocation().getLongitude();


                latLng = new LatLng(latCurrent,lonCurrent);

//                radius = getIntent().getDoubleExtra("radius",0) * 1000;
//                seekbar.setProgress((int) (radius / 30));


                if(mapboxMapInstance!=null)
                {
                    if(center!=null)
                    {
                        mapboxMapInstance.removeMarker(center);
                    }


                    center = mapboxMapInstance.addMarker(new MarkerOptions().setPosition(latLng));
                }



//                updateMap();




                if(locationCallback!=null)
                {
                    LocationServices.getFusedLocationProviderClient(PickLocation.this)
                            .removeLocationUpdates(locationCallback);
                }





                if(mapboxMapInstance!=null)
                {

                    mapboxMapInstance.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(new LatLng(
                                    locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude()),17)
                    ,5000);

                }


//                if(getIntent().getDoubleExtra("lat_dest",0)==0
//                        && getIntent().getDoubleExtra("lon_dest",0)==0)
//
//                {
//                    if(mapboxMapInstance!=null)
//                    {
//
//                        mapboxMapInstance.animateCamera(
//                                CameraUpdateFactory.newLatLngZoom(new LatLng(
//                                        locationResult.getLastLocation().getLatitude(),
//                                        locationResult.getLastLocation().getLongitude()),17)
//                        );
//
//                    }
//
//                }
//                else
//                {
//                    if(mapboxMapInstance!=null)
//                    {
//
//                        mapboxMapInstance.animateCamera(
//                                CameraUpdateFactory.newLatLngZoom(new LatLng(
//                                        getIntent().getDoubleExtra("lat_dest",0),
//                                        getIntent().getDoubleExtra("lat_dest",0)),17)
//                        );
//
//                    }
//                }


            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };


        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(mLocationRequest,locationCallback, null);
    }




}
