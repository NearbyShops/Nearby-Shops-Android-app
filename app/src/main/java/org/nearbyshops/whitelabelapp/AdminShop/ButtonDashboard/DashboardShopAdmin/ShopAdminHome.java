package org.nearbyshops.whitelabelapp.AdminShop.ButtonDashboard.DashboardShopAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.nearbyshops.whitelabelapp.R;


public class ShopAdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new ShopAdminHomeFragment())
                    .commitNow();
        }

    }
}
