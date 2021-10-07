package org.nearbyshops.whitelabelapp.AdminCommon.AddCredit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.whitelabelapp.R;


public class AddCredit extends AppCompatActivity {


    public static final String TAG_USER_ID = "tag_user_id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
//        toolbar.setTitle("Add Credit");
//        setSupportActionBar(toolbar);

        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new FragmentAddCredit(),"tag_fragment_one")
                    .commitNow();
        }


    }

}
