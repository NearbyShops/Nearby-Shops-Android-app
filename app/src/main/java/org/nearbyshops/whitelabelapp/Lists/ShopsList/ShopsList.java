package org.nearbyshops.whitelabelapp.Lists.ShopsList;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import butterknife.ButterKnife;


public class ShopsList extends AppCompatActivity implements SetToolbarText {

    public static final String TAG_FRAGMENT = "fragment_shops_list";


    TextView toolbarHeader;
    TextView toolbarText;



    void bindViews()
    {
        toolbarHeader = findViewById(R.id.toolbar_header);
        toolbarText = findViewById(R.id.toolbar_text);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container_with_toolbar);
        bindViews();

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
        UtilityFunctions.showToastMessage(this,message);
    }



    @Override
    public void setToolbar(boolean toolbarVisible, String header, boolean isheaderBold, String subtitle) {


//        showToastMessage(header + " " + subtitle);
        toolbarHeader.setText(header);

        if(subtitle==null)
        {
            toolbarText.setVisibility(View.GONE);
        }
        else
        {
            toolbarText.setVisibility(View.VISIBLE);
            toolbarText.setText(subtitle);
        }

    }
}
