package org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerMapbox.PlacePickerWithRadius;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;


import org.nearbyshops.enduserappnew.AppConfig;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PickDeliveryRange extends AppCompatActivity {


    public static final int SEEKBAR_CONSTANT = 300; // 100 for 10 km, 200 for 20 km and 300 for 30 km and so on ...

    MapView mapView;
    @BindView(R.id.seekbar_map) SeekBar seekbar;
    @BindView(R.id.label) TextView labelText;



    Polygon polygon;
    double radius;
    LatLng latLng;
    MapboxMap mapboxMapInstance;
    Marker center;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_pick_delivery_radius);
        ButterKnife.bind(this);


//        Mapbox.getInstance(this, "pk.eyJ1Ijoic3VtZWV0");
        mapView = findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);





//        "https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png"


//        ServiceConfiguration configuration = PrefServiceConfig.getServiceConfig(this);
//
//
//
//        if(configuration!=null)
//        {
//            if(configuration.getStyleURL()!=null)
//            {
//                mapView.setStyleUrl(configuration.getStyleURL()+ "?key=" + AppConfig.openMapTilesMapsKey);
////                showlog(configuration.getStyleURL());
//            }
//        }



        mapView.setStyleUrl(AppConfig.styleURLBright);







//        mapView.setStyleUrl("asset://style-cdn-bright.json");

//        mapView.setStyleUrl("http://map-mumbai.triplogic.org/styles/osm-bright/style.json");



        // Add a MapboxMap
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                // Customize map with markers, polylines, etc.


                mapboxMapInstance = mapboxMap;


//                mapboxMapInstance.setStyle(new Style.Builder().fromUrl(AppConfig.styleURLBright));


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




                radius = getIntent().getDoubleExtra("radius",0) * 1000;

                if(radius==0)
                {
                    radius = 500;
                }

                seekbar.setProgress((int) (radius / SEEKBAR_CONSTANT));




                if(getIntent().getDoubleExtra("lat_dest",0)==0
                        && getIntent().getDoubleExtra("lon_dest",0)==0)
                {

//                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(
//                            new LatLng(latCurrent, lonCurrent)
//                    ),2000);


//                    showLogMessage("Lat Lon Zero");



                    latLng = new LatLng(
                            PrefLocation.getLatitude(PickDeliveryRange.this),
                            PrefLocation.getLongitude(PickDeliveryRange.this)
                    );


                }
                else
                {

                    latLng = new LatLng(
                            getIntent().getDoubleExtra("lat_dest",0),
                            getIntent().getDoubleExtra("lon_dest",0)
                    );

//                    showLogMessage("Lat Lon not zero");
                }


                if(center!=null)
                {
                    mapboxMap.removeMarker(center);
                }


                center = mapboxMap.addMarker(new MarkerOptions().setPosition(latLng));


                updateMap();


                //
//                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(),
//                            2000
//                    );


//                mapboxMap.setOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
//                    @Override
//                    public void onMapLongClick(@NonNull LatLng point) {
//
//                        latLng = point;
//
//                        updateMap();
//
//
//
////                        showToastMessage(
////                                "Lat : " + String.valueOf(point.getLatitude())
////                                        + "\nLon : " + String.valueOf(point.getLongitude())
////                        );
////
//
//                        if(center!=null)
//                        {
//                            mapboxMap.removeMarker(center);
//                        }
//
//
//                        center = mapboxMap.addMarker(new MarkerOptions().setPosition(point));
//
//
//                    }
//                });



                mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng point) {

                        latLng = point;

                        updateMap();



//                        showToastMessage(
//                                "Lat : " + String.valueOf(point.getLatitude())
//                                        + "\nLon : " + String.valueOf(point.getLongitude())
//                        );
//

                        if(center!=null)
                        {
                            mapboxMap.removeMarker(center);
                        }


                        center = mapboxMap.addMarker(new MarkerOptions().setPosition(point));

                    }
                });



            }
        });




        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                radius = progress * SEEKBAR_CONSTANT;
                updateMap();

                labelText.setText("Radius : " + String.format("%.2f Km",radius/1000));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        requestLocationUpdates();
    }





    void updateMap()
    {
        if(latLng==null || mapboxMapInstance == null)
        {
            return;
        }

        if(polygon!=null)
        {
            mapboxMapInstance.removePolygon(polygon);
        }



        polygon = mapboxMapInstance.addPolygon(
                new PolygonOptions()
                        .addAll(polygonCircleForCoordinate(latLng, radius))
                        .strokeColor(Color.parseColor("#000000"))
                        .fillColor(Color.parseColor("#55121212"))
        );






        showLogMessage("UpdateMap : latLng : " + latLng.toString());


        // adjust zoom
        mapboxMapInstance.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng,getZoomLevel()),
                2000
        );





    }




    public int getZoomLevel()
    {
        int zoomLevel = 11;

        double scale = radius / 500;
        zoomLevel = (int) Math.floor((15 - Math.log(scale) / Math.log(2)));
//        zoomLevel = (int) Math.floor((16 - Math.log(scale) / Math.log(2)));

        return zoomLevel ;
    }





    private ArrayList<LatLng> polygonCircleForCoordinate(LatLng location, double radius){
        int degreesBetweenPoints = 8; //45 sides
        int numberOfPoints = (int) Math.floor(360 / degreesBetweenPoints);
        double distRadians = radius / 6371000.0; // earth radius in meters
        double centerLatRadians = location.getLatitude() * Math.PI / 180;
        double centerLonRadians = location.getLongitude() * Math.PI / 180;
        ArrayList<LatLng> polygons = new ArrayList<>(); //array to hold all the points
        for (int index = 0; index < numberOfPoints; index++) {
            double degrees = index * degreesBetweenPoints;
            double degreeRadians = degrees * Math.PI / 180;
            double pointLatRadians = Math.asin(Math.sin(centerLatRadians) * Math.cos(distRadians) + Math.cos(centerLatRadians) * Math.sin(distRadians) * Math.cos(degreeRadians));
            double pointLonRadians = centerLonRadians + Math.atan2(Math.sin(degreeRadians) * Math.sin(distRadians) * Math.cos(centerLatRadians),
                    Math.cos(distRadians) - Math.sin(centerLatRadians) * Math.sin(pointLatRadians));
            double pointLat = pointLatRadians * 180 / Math.PI;
            double pointLon = pointLonRadians * 180 / Math.PI;
            LatLng point = new LatLng(pointLat, pointLon);
            polygons.add(point);
        }
        return polygons;
    }



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
        Toast.makeText(PickDeliveryRange.this, message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {

        if(latLng!=null && radius>0)
        {
            Intent intent = new Intent();
            intent.putExtra("lat_dest",latLng.getLatitude());
            intent.putExtra("lon_dest",latLng.getLongitude());
            intent.putExtra("radius",radius/1000);
            setResult(3,intent);
        }


        super.onBackPressed();
    }









}
