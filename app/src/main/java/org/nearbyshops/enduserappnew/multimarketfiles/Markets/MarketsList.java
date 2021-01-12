package org.nearbyshops.enduserappnew.multimarketfiles.Markets;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.enduserappnew.HomePleaseSelectMarket;
import org.nearbyshops.enduserappnew.Interfaces.MarketSelected;
import org.nearbyshops.enduserappnew.R;

import butterknife.ButterKnife;


public class MarketsList extends AppCompatActivity implements MarketSelected {



    public static final String TAG_FRAGMENT = "markets_list";




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
                    .add(R.id.fragment_container,MarketsFragmentNew.newInstance(),TAG_FRAGMENT)
                    .commit();
        }
    }









    @Override
    public void marketSelected() {

        boolean isSelectionMode = getIntent().getBooleanExtra("is_selection_mode",false);


        if(isSelectionMode)
        {
            setResult(3121);
            finish();
        }
        else
        {
            startActivity(new Intent(this, HomePleaseSelectMarket.class));
        }

    }
}
