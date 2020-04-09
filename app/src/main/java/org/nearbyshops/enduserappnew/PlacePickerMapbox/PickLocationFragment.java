package org.nearbyshops.enduserappnew.PlacePickerMapbox;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import org.nearbyshops.enduserappnew.R;

import java.util.ArrayList;


public class PickLocationFragment extends Fragment {

    MapView mapView;
    @BindView(R.id.seekbar_map) SeekBar seekbar;
    @BindView(R.id.label) TextView labelText;


    Polygon polygon;
    double radius;
    LatLng latLng;
    MapboxMap mapboxMapInstance;
    Marker center;


    @BindView(R.id.use_selected_button) TextView useSelectedButton;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mapView!=null)
        {
            mapView.onCreate(savedInstanceState);

        }
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_dest_filter_map, container, false);

        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //        Mapbox.getInstance(this, "pk.eyJ1Ijoic3VtZWV0");


        mapView = rootView.findViewById(R.id.mapview);

//        bindMap();

        return rootView;
    }




    void bindMap()
    {

        mapView.setStyleUrl("http://map-mumbai.triplogic.org/styles/osm-bright/style.json");



        // Add a MapboxMap
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                // Customize map with markers, polylines, etc.


                mapboxMapInstance = mapboxMap;



                mapboxMap.getUiSettings().setAllGesturesEnabled(true);

                mapboxMap.getUiSettings().setLogoEnabled(false);
                mapboxMap.getUiSettings().setLogoGravity(Gravity.TOP);
                mapboxMap.getUiSettings().setAttributionGravity(Gravity.TOP);
                mapboxMap.getUiSettings().setAttributionEnabled(false);

//                mapboxMap.setMyLocationEnabled(true);
//                mapboxMap.getMyLocation();
//                mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(new LatLng(17.4700,78.4521)));




//                mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(19.07,72.87)));


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


                radius = progress * 10;
                updateMap();

                labelText.setText("Radius : " + String.format("%.2f Meters",radius));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }





    private void updateMap()
    {
        if(latLng==null || mapboxMapInstance == null)
        {
            return;
        }

        if(polygon!=null)
        {
            mapboxMapInstance.removePolygon(polygon);
        }


        if(radius==0)
        {
            radius = 500;
        }


//        polygon = mapboxMapInstance.addPolygon(
//                new PolygonOptions()
//                        .addAll(polygonCircleForCoordinate(latLng, radius))
//                        .strokeColor(Color.parseColor("#000000"))
//                        .fillColor(Color.parseColor("#55121212"))
//        );






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
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }





    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(), message,Toast.LENGTH_SHORT).show();
    }

}
