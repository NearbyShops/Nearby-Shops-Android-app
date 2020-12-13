package org.nearbyshops.enduserappnew.ImageSlider.ImageSliderForShop;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.nearbyshops.enduserappnew.Model.ModelImages.ShopImage;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import java.util.List;


/**
 * Created by sumeet on 23/4/17.
 */

public class PagerAdapter extends FragmentPagerAdapter {



    private List<Object> imagesList;


    public PagerAdapter(FragmentManager fm, List<Object> imagesList) {
        super(fm);

        this.imagesList = imagesList;
    }

    @Override
    public Fragment getItem(int position) {

        FragmentShopImage fragment = new FragmentShopImage();

        Bundle bundle = new Bundle();
//        ItemImage taxiData = (ItemImage) imagesList.get(position);
//        String jsonString = UtilityFunctions.provideGson()
//                .toJson(taxiData);


//        bundle.putString("shop_image",jsonString);



        if(imagesList.get(position) instanceof ShopImage)
        {

            ShopImage taxiData = (ShopImage) imagesList.get(position);
            String jsonString = UtilityFunctions.provideGson()
                    .toJson(taxiData);


            bundle.putString("shop_image",jsonString);
        }
        else if(imagesList.get(position) instanceof Shop)
        {
            Shop item = (Shop) imagesList.get(position);
            String jsonString = UtilityFunctions.provideGson()
                    .toJson(item);

            bundle.putString("shop",jsonString);
        }


        fragment.setArguments(bundle);


//        showLogMessage("List Size : " + imagesList.size());

        return fragment;
    }



    void showLogMessage(String string)
    {
        Log.d("image_slider",string);
    }




    @Override
    public int getCount() {
        return imagesList.size();
    }




}
