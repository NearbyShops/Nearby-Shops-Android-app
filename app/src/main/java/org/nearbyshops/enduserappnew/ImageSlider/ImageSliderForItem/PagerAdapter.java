package org.nearbyshops.enduserappnew.ImageSlider.ImageSliderForItem;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.nearbyshops.enduserappnew.Model.ModelImages.ItemImage;
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

        FragmentImage fragment = new FragmentImage();

        Bundle bundle = new Bundle();
        ItemImage taxiData = (ItemImage) imagesList.get(position);
        String jsonString = UtilityFunctions.provideGson()
                .toJson(taxiData);


        bundle.putString("item_image",jsonString);
        fragment.setArguments(bundle);



        showLogMessage("List Size : " + imagesList.size());

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
