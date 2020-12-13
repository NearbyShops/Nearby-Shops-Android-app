package org.nearbyshops.enduserappnew.Lists.ShopsList;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.enduserappnew.R;

import butterknife.ButterKnife;


public class ShopsList extends AppCompatActivity{

    public static final String TAG_FRAGMENT = "fragment_shops_list";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new FragmentShopsList(),TAG_FRAGMENT)
                    .commit();
        }
    }





    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



}
