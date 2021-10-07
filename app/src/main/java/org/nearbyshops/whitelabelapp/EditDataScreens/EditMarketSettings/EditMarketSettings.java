package org.nearbyshops.whitelabelapp.EditDataScreens.EditMarketSettings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.aaMultimarketFiles.EditDataScreens.EditMarketSettingsMM.EditMarketSettingsKotlin;

public class EditMarketSettings extends AppCompatActivity {

    public static final String TAG_FRAGMENT_EDIT = "fragment_edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_EDIT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new EditMarketSettingsKotlin(),TAG_FRAGMENT_EDIT)
                    .commit();
        }

    }



}
