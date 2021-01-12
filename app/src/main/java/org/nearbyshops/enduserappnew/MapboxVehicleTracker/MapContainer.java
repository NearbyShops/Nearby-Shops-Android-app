//package org.nearbyshops.enduserappnew.MapboxVehicleTracker;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;
//
//public class MapContainer extends AppCompatActivity {
//
//
//
//    public final static String TAG_MAP_CONTAINER = "tag_map_container";
//
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//
//        setContentView(R.layout.activity_map_container);
//
//        if(getSupportFragmentManager().findFragmentByTag(TAG_MAP_CONTAINER)==null)
//        {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container,new MapboxMapFragmentNew(),TAG_MAP_CONTAINER)
//                    .commitNow();
//        }
//    }
//
//
//
//}
