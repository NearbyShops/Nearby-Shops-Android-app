//package org.nearbyshops.enduserappnew.MapboxVehicleTracker;
//
//import android.animation.ObjectAnimator;
//import android.animation.TypeEvaluator;
//import android.animation.ValueAnimator;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//import com.google.gson.Gson;
//import com.mapbox.mapboxsdk.annotations.Icon;
//import com.mapbox.mapboxsdk.annotations.IconFactory;
//import com.mapbox.mapboxsdk.annotations.MarkerOptions;
//import com.mapbox.mapboxsdk.annotations.MarkerView;
//import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
//import com.mapbox.mapboxsdk.geometry.LatLng;
//import com.mapbox.mapboxsdk.geometry.LatLngBounds;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
//import org.nearbyshops.enduserappnew.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//
//
//
//public class MapboxMapFragmentNew extends Fragment {
//
//    @BindView(R.id.stats)
//    TextView stats;
//
//    MapView mapView;
//    MarkerView markerView;
//    MapboxMap mapboxMapInstance;
////    SymbolLayer symbolLayer;
////    Marker marker;
//
//
//    public MapboxMapFragmentNew() {
//
//        DaggerComponentBuilder.getInstance()
//                .getNetComponent()
//                .Inject(this);
//    }
//
//
//
////    void showlog(String message)
////    {
////        Log.d("mqtt_log",message);
////    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        super.onCreateView(inflater, container, savedInstanceState);
//
//
////        setContentView(R.layout.activity_mapbox);
//
//        setRetainInstance(true);
//        View rootView = inflater.inflate(R.layout.activity_mapbox, container, false);
//        ButterKnife.bind(this,rootView);
//
//
//
//
////        Mapbox.getInstance(this, "pk.eyJ1Ijoic3VtZWV0");
//        mapView = (MapView) rootView.findViewById(R.id.mapview);
//        mapView.onCreate(savedInstanceState);
//
////        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
//
//
//
//
//
//
//        if(savedInstanceState==null)
//        {
//
//        }
//
//
//
//
//
////        getCurrentTrip();
//
//
//
//
//
//        mapView.setStyleUrl(APIKeys.styleURLBright);
//
//
//
//        // Add a MapboxMap
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(final MapboxMap mapboxMap) {
//
//
//                mapboxMap.getUiSettings().setAllGesturesEnabled(true);
//                mapboxMap.getUiSettings().setLogoEnabled(false);
//                mapboxMap.getUiSettings().setAttributionEnabled(false);
////                mapboxMap.getUiSettings().setAttributionGravity(GravityCompat.END| Gravity.BOTTOM);
//
//
//                mapboxMapInstance = mapboxMap;
//
//
//                String tripJson = getActivity().getIntent().getStringExtra("trip_json");
//                currentTrip = UtilityFunctions.provideGson().fromJson(tripJson, Trip.class);
//
//                bindCurrentTrip();
//
//
//
//
//                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng point) {
//
//
////                        showToastMessage(
////                                "Lat : " + String.valueOf(point.getLatitude())
////                                        + "\nLon : " + String.valueOf(point.getLongitude())
////                        );
////
//
//                        stats.setText("Current Zoom : " + String.valueOf(mapboxMap.getCameraPosition().zoom));
//
//                    }
//                });
//
//            }
//        });
//
//
//
//        return rootView;
//    }
//
//
//
//
//
//    @OnClick(R.id.close_button)
//    void closeButton()
//    {
//        getActivity().finish();
//    }
//
//
//
//
//
//
//
//    void updateLocation(LocationCurrentTrip locationCurrentTrip)
//    {
//        if(markerView ==null || mapboxMapInstance ==null)
//        {
//            return;
//        }
//
////        Layer layer = mapboxMapInstance.getLayer("layer_id");
//
//
//
//
//        LatLng point = new LatLng(locationCurrentTrip.getLatitude(),
//                locationCurrentTrip.getLongitude());
//
//
////        markerView.setRotation((float) computeHeading(markerView.getPosition(),point));
//
//
//        markerView.setRotation((float) locationCurrentTrip.getBearing());
//
//
//
//
//        ValueAnimator markerAnimator = ObjectAnimator.ofObject(markerView, "position",
//                new LatLngEvaluator(), markerView.getPosition(), point);
//        markerAnimator.setDuration(5000);
//        markerAnimator.start();
//
//
//
//
//
//        String lastUpdateTime = " - ";
//
//        if(locationCurrentTrip.getLastUpdated()!=null)
//        {
//            lastUpdateTime = locationCurrentTrip.getLastUpdated().toLocaleString();
//        }
//
//
//
//
//        stats.setText(
//                "Speed : " + String.format("%.2f",locationCurrentTrip.getSpeed()*3.6) + " Km / hour"
//            + "\nLat : " + String.format("%.5f",locationCurrentTrip.getLatitude())
//                + " | Lon : " + String.format("%.5f",locationCurrentTrip.getLongitude())
//                + "\nBearing : " + String.format("%.4f",locationCurrentTrip.getBearing())
//                + "\nLast Updated : " + lastUpdateTime
//        );
//
//
//
//
//
//        mapboxMapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(point,17),5000);
////        mapboxMapInstance.easeCamera(CameraUpdateFactory.newLatLngZoom(point,17));
//    }
//
//
//
//
//
//
//
//
//    void bindCurrentTrip()
//    {
//
//        if(mapboxMapInstance==null || currentTrip == null)
//        {
//            return;
//        }
//
//        Vehicle vehicle = currentTrip.getRt_vehicle();
//
//        if(vehicle==null)
//        {
//            return;
//        }
//
//
//
//        List<LatLng> boundsList = new ArrayList<>();
//
//        LatLng pickup = new LatLng(currentTrip.getLatPickUpLocation(), currentTrip.getLonPickUpLocation());
//
//
//        mapboxMapInstance.addMarker(
//                new MarkerOptions()
//                        .position(pickup)
//                        .title("Pickup")
//
//        );
//
//
//
//
//        boundsList.add(pickup);
//
//
//        Icon icon = IconFactory.getInstance(getActivity().getApplicationContext())
//                .fromResource(R.drawable.taxi_icon_medium);
//
////                        ContextCompat.getDrawable(MapboxActivity.this,R.drawable.ic_local_taxi_black_24px)
//
//
//
//
////        if(vehicle!=null)
////        {
//        LatLng currentLocation = new LatLng(vehicle.getLatCurrent(),vehicle.getLonCurrent());
//
//        markerView = mapboxMapInstance.addMarker(
//                new MarkerViewOptions()
//                        .icon(icon)
//                        .position(currentLocation)
//                        .title("Current")
//
//        );
//
//        boundsList.add(currentLocation);
//
//
//
//
//
//
//        if(currentTrip.getLatDestination()!=0 || currentTrip.getLonDestination()!=0)
//        {
//            LatLng destination = new LatLng(currentTrip.getLatDestination(),
//                    currentTrip.getLonDestination()
//            );
//
//
//            mapboxMapInstance.addMarker(
//                    new MarkerOptions()
//                            .position(destination)
//                            .title("Destination")
//
//            );
//
//
//            boundsList.add(destination);
//        }
//
//
//
////        .include(pickup)
////            .include(currentLocation)
////            .include(destination)
//
//
//        LatLngBounds latLngBounds = new LatLngBounds.Builder()
//                .includes(boundsList)
//                .build();
//
//
//
//        mapboxMapInstance.animateCamera(
//                    CameraUpdateFactory.newLatLngBounds(
//                            latLngBounds, 200
//                    ),5000
//            );
//
//
//    }
//
//
//
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if(mapView!=null)
//        {
//            mapView.onResume();
//        }
//
//
//        isDestroyed = false;
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        if(mapView!=null)
//        {
//            mapView.onPause();
//        }
//    }
//
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        if(mapView!=null)
//        {
//            mapView.onStop();
//        }
//
//        EventBus.getDefault().unregister(this);
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//
//        if(mapView!=null)
//        {
//            mapView.onDestroy();
//        }
//
//        isDestroyed = true;
//
//        LocalBroadcastManager.getInstance(getActivity())
//                .unregisterReceiver(broadcastReceiver);
//
//    }
//
//
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//
//
//
//    void showToastMessage(String message)
//    {
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//    }
//
//
//
//
//
//    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {
//        // Method is used to interpolate the marker animation.
//
//        private LatLng latLng = new LatLng();
//
//        @Override
//        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
//            latLng.setLatitude(startValue.getLatitude()
//                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
//            latLng.setLongitude(startValue.getLongitude()
//                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
//            return latLng;
//        }
//    }
//
//
//
//
//
////    public static double computeHeading(LatLng from, LatLng to) {
////         Compute bearing/heading using Turf and return the value.
////        return TurfMeasurement.bearing(
////                Position.fromCoordinates(from.getLongitude(), from.getLatitude()),
////                Position.fromCoordinates(to.getLongitude(), to.getLatitude())
////        );
////    }
//
//
//
////    Sources
//
////    https://github.com/mapbox/mapbox-android-demo/blob/master/MapboxAndroidDemo/src/main/java/com/mapbox/mapboxandroiddemo/labs/SpaceStationLocationActivity.java
////
////    https://stackoverflow.com/questions/39509614/marker-direction-in-mapbox-android
////
////    https://stackoverflow.com/questions/41461391/rotate-marker-car-icon-in-google-maps-android
//
//
//    public static double computeHeading(LatLng from, LatLng to) {
//        double fromLat = Math.toRadians(from.getLatitude());
//        double fromLng = Math.toRadians(from.getLongitude());
//        double toLat = Math.toRadians(to.getLatitude());
//        double toLng = Math.toRadians(to.getLongitude());
//        double dLng = toLng - fromLng;
//        double heading = Math.atan2(Math.sin(dLng) * Math.cos(toLat),
//                Math.cos(fromLat) * Math.sin(toLat) - Math.sin(fromLat) * Math.cos(toLat) * Math.cos(dLng));
//        return (Math.toDegrees(heading) >= -180 && Math.toDegrees(heading) < 180) ?
//                Math.toDegrees(heading) : ((((Math.toDegrees(heading) + 180) % 360) + 360) % 360 + -180);
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    // Define the callback for what to do when data is received
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            if(isDestroyed)
//            {
//                return;
//            }
//
//
//
//
//            if(intent.getAction().equals(MyApplication.ACTION_LOCATION_UPDATED))
//            {
//
//                String jsonString = intent.getStringExtra("location_current_trip");
//
//                showLogMessage(jsonString);
//
//                Gson gson = UtilityFunctions.provideGson();
//                LocationCurrentTrip locationCurrentTrip = gson.fromJson(jsonString, LocationCurrentTrip.class);
//
//
//                if(locationCurrentTrip!=null)
//                {
//                    showLogMessage("Inside LocationCurrentTrip!=null");
//                    updateLocation(locationCurrentTrip);
//                }
//
//            }
//        }
//    };
//
//
//
//
//
//
//    @Inject
//    TripService currentTripEndpoint;
//
//    @Inject
//    UserService userService;
//    boolean isDestroyed = false;
//    Trip currentTrip;
//
//    // housekeeping for token renewal
//    int token_renewal_attempts = 0;  // variable to keep record of renewal attempts
//    int token_renewal_request_code = -1; // variable to store the request code;
//
//    public static final int REQUEST_CODE_GET_CURRENT_TRIP = 1;
//    public static final int REQUEST_CODE_UPDATE_LOCATION = 2;
//
//
//
//
//
//
//
//
//
//
//    private void getCurrentTrip()
//    {
//        User endUser = PrefLogin.getUser(getActivity());
//
//        if(endUser==null)
//        {
//            return;
//        }
//
//
//
//
//        Call<Trip> call = currentTripEndpoint.getTripDetails(
//                PrefLogin.getAuthHeaderToken(getActivity()),
//                getActivity().getIntent().getIntExtra("trip_id",0)
//        );
//
//
//
//        call.enqueue(new Callback<Trip>() {
//            @Override
//            public void onResponse(Call<Trip> call, Response<Trip> response) {
//
//
//                if (isDestroyed) {
//                    return;
//                }
//
//
//
//                if (response.code() == 200) {
//
//
//                    currentTrip = response.body();
//
//                    bindCurrentTrip();
//
//
//                }
//                else
//                {
//                    showToastMessage("Failed : Code " + String.valueOf(response.code()));
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Trip> call, Throwable t) {
//
//                if(isDestroyed)
//                {
//                    return;
//                }
//
//            }
//        });
//
//
//    }
//
//
//
//
//
//    void refreshCurrentLocation()
//    {
//        User endUser = PrefLogin.getUser(getActivity());
//
//        if(endUser==null)
//        {
//            return;
//        }
//
//
//        refreshButton.setVisibility(View.INVISIBLE);
//        progressButton.setVisibility(View.VISIBLE);
//
//        Call<LocationCurrentTrip> call = currentTripEndpoint.getCurrentTripLocation(
//            getActivity().getIntent().getIntExtra("trip_id",0)
//        );
//
//
//
//
//
//        call.enqueue(new Callback<LocationCurrentTrip>() {
//            @Override
//            public void onResponse(Call<LocationCurrentTrip> call, Response<LocationCurrentTrip> response) {
//
//                if (isDestroyed) {
//                    return;
//                }
//
//
//                if(response.code()==200)
//                {
//                    updateLocation(response.body());
//                }
//                else
//                {
//                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
//                }
//
//
//
//                refreshButton.setVisibility(View.VISIBLE);
//                progressButton.setVisibility(View.INVISIBLE);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<LocationCurrentTrip> call, Throwable t) {
//
//                if (isDestroyed) {
//                    return;
//                }
//
//
//
//
//                refreshButton.setVisibility(View.VISIBLE);
//                progressButton.setVisibility(View.INVISIBLE);
//
//            }
//        });
//
//    }
//
//
//
//
//
//
//    @BindView(R.id.refresh)
//    TextView refreshButton;
//    @BindView(R.id.progress_button)
//    ProgressBar progressButton;
//
//
//
//
//
//
//
//    @OnClick(R.id.refresh)
//    void refreshClick()
//    {
//        refreshCurrentLocation();
//    }
//
//
//
//
//
//
//
////    void renewToken()
////    {
////
//////        if(UtilityLogin.getExpires(getActivity()).before(new Timestamp(System.currentTimeMillis())))
//////        {
////        // token has expired and needs to be renewed
////
////        Call<User> call = userService.getLogin(PrefLogin.getAuthHeaderPassword(getActivity()));
////
////
////        call.enqueue(new Callback<User>() {
////            @Override
////            public void onResponse(Call<User> call, Response<User> response) {
////
////                if(isDestroyed)
////                {
////                    return;
////                }
////
////                if(response.code()==200 && response.body()!=null)
////                {
////                    PrefLogin.saveToken(getActivity(),
////                            response.body().getToken(),
////                            response.body().getTimestampTokenExpires()
////                    );
////
////
////
////
//////                    logMessage("Current Trip Service : Token renewal successful !");
////
////
////
////
////                    if(token_renewal_request_code==REQUEST_CODE_GET_CURRENT_TRIP)
////                    {
////                        getCurrentTrip();
////                    }
////                    else if(token_renewal_request_code==REQUEST_CODE_UPDATE_LOCATION)
////                    {
////                        refreshCurrentLocation();
////                    }
////
////
////                    token_renewal_attempts = 0; // reset flag
////                    token_renewal_request_code = -1; // reset flag
////
////                }
////                else
////                {
////
//////                    logMessage("Current Trip Service : Token renewal failed code :" + String.valueOf(response.code()));
////
////
////                    if(token_renewal_attempts<3)
////                    {
////                        renewToken();
////                        token_renewal_attempts = token_renewal_attempts + 1;
////                    }
////
////                }
////
////            }
////
////            @Override
////            public void onFailure(Call<User> call, Throwable t) {
////
////
////                if(isDestroyed)
////                {
////                    return;
////                }
////
////
////                if(token_renewal_attempts<3)
////                {
////                    renewToken();
////                    token_renewal_attempts = token_renewal_attempts + 1;
////                }
////
////            }
////        });
////
//////        }
////    }
//
//
//
//
//
//
//
//
//////    public static final String BROKER_URL = "tcp://mqtt.nearbyshops.org:1883";
////    /* In a real application, you should get an Unique Client ID of the device and use this, see
////    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */
////    public static final String clientId = "android-client-end-user-map";
////
//////    public static final String TOPIC = "test";
////    private MqttClient mqttClient;
////
////
////
////    void setupMQTT()
////    {
////        ServiceConfiguration serviceConfiguration = PrefServiceConfig.getServiceConfigLocal(this);
////
////        if(serviceConfiguration==null)
////        {
////            return;
////        }
////
////
////
////        try {
////
////
////            String android_id = Settings.Secure.getString(getContentResolver(),
////                    Settings.Secure.ANDROID_ID);
////
////
////
////            showLogMessage("MQTT address : " + serviceConfiguration.getMqttServerAddress());
////            showLogMessage("android ID : " + android_id);
////
////            mqttClient = new MqttClient(serviceConfiguration.getMqttServerAddress(), android_id, new MemoryPersistence());
////
//////            mqttClient.connect();
////
////
////
////
////
////
////            if(currentTrip!=null)
////            {
////                String topic = "CurrentTrip:" + String.valueOf(currentTrip.getCurrentTripID());
////                showLogMessage("Topic : " + topic);
////                mqttClient.subscribe(topic);
////            }
////
////
////
////            mqttClient.setCallback(new MqttCallback() {
////                @Override
////                public void connectionLost(Throwable cause) {
////
////                    showLogMessage("Connection Lost !");
////
//////                    try {
//////                        mqttClient.connect();
//////                    } catch (MqttException e) {
//////                        e.printStackTrace();
//////                    }
//////
//////
//////                    if(currentTrip!=null)
//////                    {
//////                        String topic = "CurrentTrip:" + String.valueOf(currentTrip.getCurrentTripID());
//////                        showLogMessage("Topic : " + topic);
//////                        try {
//////                            mqttClient.subscribe(topic,0);
//////                        } catch (MqttException e) {
//////                            e.printStackTrace();
//////                        }
//////                    }
////                }
////
////
////
////                @Override
////                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
////
////
////                    runOnUiThread(new Runnable() {
////
////                        @Override
////                        public void run() {
////
////                            showLogMessage("MAP : Topic : " + topic + " | Message : " + message.toString());
////
////                            Gson gson = UtilityFunctions.provideGson();
////                            LocationCurrentTrip locationCurrentTrip = gson.fromJson(message.toString(), LocationCurrentTrip.class);
////
////
////                            if (locationCurrentTrip != null) {
////                                showlog("Inside LocationCurrentTrip!=null");
////                                updateLocation(locationCurrentTrip);
////                            } else {
////
////                                showlog("LocationCurrentTrip is null");
////                            }
////
////                        }
////
////
////                    });
////
////
////
////
////                }
////
////                @Override
////                public void deliveryComplete(IMqttDeliveryToken token) {
////
////                }
////            });
////
////
////
////
////
////
////
////
////
////
////
////
////
////        } catch (MqttException e) {
////
////            Toast.makeText(this, "Something went wrong ! " + e.getMessage(), Toast.LENGTH_LONG).show();
////            e.printStackTrace();
////        }
////
////    }
////
////
////
////
////    void subscribeTopic()
////    {
////
////        String topic = "CurrentTrip:" + String.valueOf(currentTrip.getCurrentTripID());
////
////        try {
////            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
////                @Override
////                public void onSuccess(IMqttToken asyncActionToken) {
////                    showlog("Subscribtion Success !");
////                }
////
////                @Override
////                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
////                    showlog("Failed to subscribe");
////                }
////            });
////
////
////
////
////        } catch (MqttException ex){
////            System.err.println("Exception whilst subscribing");
////            ex.printStackTrace();
////        }
////    }
//
//
//
//
//
//
////    public static final String BROKER_URL = "tcp://mqtt.nearbyshops.org:1883";
//    /* In a real application, you should get an Unique Client ID of the device and use this, see
//    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */
////    public static final String clientId = "android-client";
//
////    public static final String TOPIC = "test";
//
////    private MqttClient mqttClient;
//
//
////
////
////    void setupMQTT()
////    {
////
////
//////        ServiceConfiguration serviceConfiguration = PrefServiceConfig.getServiceConfig(this);
//////
//////        if(serviceConfiguration==null)
//////        {
//////            return;
//////        }
////
////
////
////
////        showLogMessage("Setup MQTT");
////
////
////
////
////
////        try {
////
////
////            String android_id = Settings.Secure.getString(getContentResolver(),
////                    Settings.Secure.ANDROID_ID) + ":EndUser";
////
////            showLogMessage("Android ID : " + android_id);
////
////            mqttClient = new MqttClient("tcp://139.59.14.114:1883", android_id, new MemoryPersistence());
////
////
////            mqttClient.setCallback(new MqttCallback() {
////                @Override
////                public void connectionLost(Throwable cause) {
////
////
////                    if(isDestroyed)
////                    {
////                        return;
////                    }
////
////
////                    try {
////
////                        mqttClient.connect();
////
////                        String topic = "CurrentTrip:" + String.valueOf(currentTrip.getCurrentTripID());
////                        mqttClient.subscribe(topic);
////
////                        showLogMessage("connectionLost : Reconnecting ... ");
////
////                    } catch (MqttException e) {
////                        e.printStackTrace();
////                    }
////
////
////
////
////
////                }
////
////                @Override
////                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
////
////                    showLogMessage("Message arrived : " + message.toString());
////
////
////
////                    runOnUiThread(new Runnable() {
////
////                        @Override
////                        public void run() {
////
////                            showLogMessage("MAP : Topic : " + topic + " | Message : " + message.toString());
////
////                            Gson gson = UtilityFunctions.provideGson();
////                            LocationCurrentTrip locationCurrentTrip = gson.fromJson(message.toString(), LocationCurrentTrip.class);
////
////
////                            if (locationCurrentTrip != null) {
////                                showLogMessage("Inside LocationCurrentTrip!=null");
////                                updateLocation(locationCurrentTrip);
////                            } else {
////
////                                showLogMessage("LocationCurrentTrip is null");
////                            }
////
////                        }
////
////
////                    });
////
////
////
////                }
////
////                @Override
////                public void deliveryComplete(IMqttDeliveryToken token) {
////
////                }
////            });
////
////
////
////
////            mqttClient.connect();
////
////
////            if(currentTrip!=null)
////            {
////                String topic = "CurrentTrip:" + String.valueOf(currentTrip.getCurrentTripID());
////                mqttClient.subscribe(topic);
////
////                showLogMessage("Setup MQTT Subscribed to : " + topic);
////            }
////
////
////
////        } catch (MqttException e) {
////
////            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
////            e.printStackTrace();
////        }
////    }
////
////
////
////
//
//
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//
////        mapView.onStart();
//
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(MyApplication.ACTION_LOCATION_UPDATED);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, filter);
//    }
//
//
//
//
//
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
//    public void onMessageEvent(OneSignalData data) {/* Do something */
//
//        showToastMessage("Location Received !");
//
//
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                LocationCurrentTrip location = new LocationCurrentTrip();
//                location.setLatitude(data.getLatCurrent());
//                location.setLongitude(data.getLonCurrent());
//                location.setBearing(data.getBearing());
//
//
//                showLogMessage(UtilityFunctions.provideGson().toJson(location));
//
//                updateLocation(location);
//            }
//        });
//
//
//    }
//
//
//
//
//
//
//
//
//
//
//
//    void showLogMessage(String message)
//    {
//        Log.d("one_signal",message);
//    }
//
//}
