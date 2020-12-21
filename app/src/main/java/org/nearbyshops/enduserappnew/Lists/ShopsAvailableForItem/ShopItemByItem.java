package org.nearbyshops.enduserappnew.Lists.ShopsAvailableForItem;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wunderlist.slidinglayer.SlidingLayer;
import org.nearbyshops.enduserappnew.Lists.CartsList.CartsList;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Lists.ShopsAvailableForItem.SlidingLayerSort.SlidingLayerSortShopItem;
import org.nearbyshops.enduserappnew.R;


public class ShopItemByItem extends AppCompatActivity implements NotifySort {


    public static final String TAG_FRAGMENT = "tag_shop_item_fragment";


    public static final String TAG_SLIDING_LAYER_FRAGMENT = "sliding_layer";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop_item_by_item_new);
        ButterKnife.bind(this);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

//        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);


        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setTitle("Available in Shops");
        setSupportActionBar(toolbar);



        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new ShopItemFragment(), TAG_FRAGMENT)
                    .commitNow();
        }

        setupSlidingLayer();
    }



    @BindView(R.id.slidingLayer)
    SlidingLayer slidingLayer;




    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(10);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);



            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            if(getSupportFragmentManager().findFragmentByTag(TAG_SLIDING_LAYER_FRAGMENT)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.slidinglayerfragment,new SlidingLayerSortShopItem(),TAG_SLIDING_LAYER_FRAGMENT)
                        .commit();
            }


        }

    }





    @OnClick({R.id.icon_checkout,R.id.text_checkout})
    void viewCartClick()
    {
        Intent intent = new Intent(this, CartsList.class);
        startActivity(intent);
    }





    @OnClick({R.id.icon_sort, R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @Override
    public void notifySortChanged() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }

    }
}
