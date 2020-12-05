package org.nearbyshops.enduserappnew.EditDataScreens.EditShop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.enduserappnew.R;


public class AddShop extends AppCompatActivity {

    public static final String TAG_FRAGMENT_EDIT = "fragment_add_shop";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);


        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_EDIT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new AddShopFragment(),TAG_FRAGMENT_EDIT)
                    .commit();
        }
    }


}
