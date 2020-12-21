package org.nearbyshops.enduserappnew.DetailScreens.DetailShop;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import org.nearbyshops.enduserappnew.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.R;


public class ShopDetail extends AppCompatActivity {


    public static final String SHOP_DETAIL_INTENT_KEY = "shop_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);



        if (getSupportFragmentManager().findFragmentByTag("shop_detail_fragment") == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ShopDetailFragment(), "shop_detail_fragment")
                    .commit();
        }



//        String shopJson = getIntent().getStringExtra(TAG_JSON_STRING);
//        Shop shop = UtilityFunctions.provideGson().fromJson(shopJson, Shop.class);


//        getSupportActionBar().setTitle(shop.getShopName());




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
