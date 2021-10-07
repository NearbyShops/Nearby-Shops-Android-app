package org.nearbyshops.whitelabelapp.CartAndOrder.CartsList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.R;


public class CartsList extends AppCompatActivity implements SetToolbarText {

    public static final String TAG_FRAGMENT = "items_in_stock_simple";


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

        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new CartsListFragment(),TAG_FRAGMENT)
                    .commit();
        }
    }





    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }





    @Override
    public void setToolbar(boolean toolbarVisible, String header, boolean isheaderBold, String subtitle) {


        toolbarHeader.setText(header);

        if(isheaderBold)
        {
            toolbarHeader.setTypeface(null, Typeface.BOLD);
        }
        else
        {
            toolbarHeader.setTypeface(null, Typeface.NORMAL);
        }

        toolbarText.setVisibility(View.GONE);
    }


}
