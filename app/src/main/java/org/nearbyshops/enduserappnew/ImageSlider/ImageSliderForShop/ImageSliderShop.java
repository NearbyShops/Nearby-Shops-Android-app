package org.nearbyshops.enduserappnew.ImageSlider.ImageSliderForShop;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 23/4/17.
 */

public class ImageSliderShop extends AppCompatActivity {


    @BindView(R.id.view_pager)
    ViewPager viewPager;
    FragmentPagerAdapter pagerAdapter;

    List<Object> listImages = new ArrayList<>();
    ShopImageEndPoint endPoint;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.image_slider);
        ButterKnife.bind(this);



        Gson gson = UtilityFunctions.provideGson();
        String itemJson = getIntent().getStringExtra("images_list");
//        Type listType = new TypeToken<ArrayList<ItemImage>>(){}.getType();
//        listImages = gson.fromJson(itemJson, listType);


        endPoint = gson.fromJson(itemJson, ShopImageEndPoint.class);



        int position = getIntent().getIntExtra("position",0);



        listImages.add(endPoint.getShopDetails());
        listImages.addAll(endPoint.getResults());


//        showLogMessage(itemJson);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),listImages);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setCurrentItem(position);
    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



    void showLogMessage(String string)
    {
        Log.d("image_slider",string);
    }

}
