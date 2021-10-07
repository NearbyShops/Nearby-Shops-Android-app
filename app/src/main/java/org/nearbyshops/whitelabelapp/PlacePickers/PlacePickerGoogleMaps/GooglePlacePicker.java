package org.nearbyshops.whitelabelapp.PlacePickers.PlacePickerGoogleMaps;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.nearbyshops.whitelabelapp.R;


public class GooglePlacePicker extends AppCompatActivity {


    public static final String TAG_PICKUP = "tag_pickup";
    public static final String TAG_DESTINATION = "tag_destination";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        setContentView(R.layout.activity_select_location);


        if(savedInstanceState==null)
        {

            // show pickup
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new AddressPickerFragment(),TAG_PICKUP)
                    .commitNow();
        }

    }

}
