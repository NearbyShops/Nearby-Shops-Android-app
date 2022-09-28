package org.nearbyshops.whitelabelapp.DetailScreens.DetailItemNew;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

import org.nearbyshops.whitelabelapp.Interfaces.RefreshFragment;
import org.nearbyshops.whitelabelapp.R;


public class ItemDetail extends AppCompatActivity {


    public static final String SHOP_DETAIL_INTENT_KEY = "item_detail";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);


        if (getSupportFragmentManager().findFragmentByTag("item_detail_fragment") == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ItemDetailFragmentNew(), "item_detail_fragment")
                    .commit();
        }

    }






//    @Override
    public void NotifyLogin() {


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(fragment instanceof RefreshFragment)
        {
            ((RefreshFragment) fragment).refreshFragment();
        }
    }


}
