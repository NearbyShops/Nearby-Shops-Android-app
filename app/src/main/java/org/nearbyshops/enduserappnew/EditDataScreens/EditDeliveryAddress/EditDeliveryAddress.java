package org.nearbyshops.enduserappnew.EditDataScreens.EditDeliveryAddress;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.nearbyshops.enduserappnew.R;

public class EditDeliveryAddress extends AppCompatActivity {

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
                    .add(R.id.fragment_container,new EditAddressFragment(),TAG_FRAGMENT_EDIT)
                    .commit();
        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
