package org.nearbyshops.enduserappnew.DetailScreens.DetailShopItem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.enduserappnew.R;

import butterknife.ButterKnife;


public class ShopItemDetail extends AppCompatActivity {


    public static final String SHOP_DETAIL_INTENT_KEY = "item_detail";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);


        if (getSupportFragmentManager().findFragmentByTag("item_detail_fragment") == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ShopItemDetailFragment(), "item_detail_fragment")
                    .commit();
        }

    }

}
