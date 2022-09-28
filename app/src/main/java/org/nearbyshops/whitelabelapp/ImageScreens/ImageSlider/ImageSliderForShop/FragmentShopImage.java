package org.nearbyshops.whitelabelapp.ImageScreens.ImageSlider.ImageSliderForShop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.Model.ModelImages.ShopImage;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.R;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentShopImage extends Fragment {



    ShopImage shopImageData;
    Shop shopData;

    @BindView(R.id.taxi_image) ImageView taxiImage;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @BindView(R.id.title) TextView titleText;
    @BindView(R.id.description) TextView descriptionText;
    @BindView(R.id.copyrights) TextView copyrightText;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_item_image_slider, container, false);
        ButterKnife.bind(this,rootView);




        // decoding the object passed to the activity
        String jsonString = getArguments().getString("shop_image");
        Gson gson = UtilityFunctions.provideGson();
        shopImageData = gson.fromJson(jsonString, ShopImage.class);



        String jsonStringItem = getArguments().getString("shop");
        shopData = UtilityFunctions.provideGson().fromJson(jsonStringItem, Shop.class);


        String imagePathFullSize = null;


        if(shopImageData!=null)
        {
            imagePathFullSize = PrefGeneral.getServerURL(getActivity()) + "/api/v1/ShopImage/Image/"
                    + shopImageData.getImageFilename();


            titleText.setText(shopImageData.getCaptionTitle());
            descriptionText.setText(shopImageData.getCaption());
            copyrightText.setText(shopImageData.getCopyrights());
        }
        else if(shopData!=null)
        {

            imagePathFullSize = PrefGeneral.getServerURL(getActivity()) + "/api/v1/Shop/Image/"
                + shopData.getLogoImagePath();

            titleText.setText(shopData.getShopName());
            descriptionText.setVisibility(View.GONE);
            copyrightText.setVisibility(View.GONE);
        }



        progressBar.setVisibility(View.VISIBLE);


        if(imagePathFullSize!=null)
        {
            Picasso.get()
                    .load(imagePathFullSize)
                    .into(taxiImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(Exception e) {

                        }

                    });



        }



        return rootView;
    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }
}
