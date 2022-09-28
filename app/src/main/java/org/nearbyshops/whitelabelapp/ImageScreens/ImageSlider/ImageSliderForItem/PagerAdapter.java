package org.nearbyshops.whitelabelapp.ImageScreens.ImageSlider.ImageSliderForItem;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ModelImages.ItemImage;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

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

        FragmentImage fragment = new FragmentImage();

        Bundle bundle = new Bundle();

        if(imagesList.get(position) instanceof ItemImage)
        {

            ItemImage taxiData = (ItemImage) imagesList.get(position);
            String jsonString = UtilityFunctions.provideGson()
                    .toJson(taxiData);


            bundle.putString("item_image",jsonString);
        }
        else if(imagesList.get(position) instanceof Item)
        {
            Item item = (Item) imagesList.get(position);
            String jsonString = UtilityFunctions.provideGson()
                    .toJson(item);

            bundle.putString("item",jsonString);
        }


        fragment.setArguments(bundle);



//        showLogMessage("List Size : " + imagesList.size());

        return fragment;
    }






    private void showLogMessage(String string)
    {
        Log.d("image_slider",string);
    }




    @Override
    public int getCount() {
        return imagesList.size();
    }




}
