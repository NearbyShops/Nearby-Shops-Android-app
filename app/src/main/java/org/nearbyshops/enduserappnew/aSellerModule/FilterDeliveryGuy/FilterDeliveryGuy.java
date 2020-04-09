package org.nearbyshops.enduserappnew.aSellerModule.FilterDeliveryGuy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import org.nearbyshops.enduserappnew.R;

import butterknife.ButterKnife;


public class FilterDeliveryGuy extends AppCompatActivity {


    public static final String TAG_STEP_ONE = "tag_step_one";
    public static final String TAG_STEP_TWO = "tag_step_two";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        setContentView(R.layout.activity_select_delivery);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
//        toolbar.setTitle("Forgot Password");
//        setSupportActionBar(toolbar);


        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new FilterDeliveryFragment(),TAG_STEP_ONE)
                    .commitNow();
        }


    }




}
