package org.nearbyshops.whitelabelapp.AdminDelivery.ButtonDashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.whitelabelapp.R;


public class DeliveryHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new DeliveryGuyHomeFragment())
                    .commitNow();
        }

    }
}
