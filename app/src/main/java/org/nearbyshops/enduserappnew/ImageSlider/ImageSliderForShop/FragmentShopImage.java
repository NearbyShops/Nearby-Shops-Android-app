package org.nearbyshops.enduserappnew.ImageSlider.ImageSliderForShop;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.ModelImages.ShopImage;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentShopImage extends Fragment {



    ShopImage shopImageData;
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


        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_nature_people_white_48px);

//        String imagePath = PrefGeneral.getServiceURL(getActivity()) + "/api/v1/TaxiImages/Image/" + "nine_hundred_"+ shopImageData.getImageFilename() + ".jpg";
//        String image_url = PrefGeneral.getServiceURL(getActivity()) + "/api/v1/TaxiImages/Image/" + shopImageData.getImageFilename();

        String imagePathSmall = PrefGeneral.getServiceURL(getActivity()) + "/api/v1/ShopImage/Image/five_hundred_"
                + shopImageData.getImageFilename() + ".jpg";


        String imagePathFullSize = PrefGeneral.getServiceURL(getActivity()) + "/api/v1/ShopImage/Image/"
                + shopImageData.getImageFilename();


        titleText.setText(shopImageData.getCaptionTitle());
        descriptionText.setText(shopImageData.getCaption());
        copyrightText.setText(shopImageData.getCopyrights());


        progressBar.setVisibility(View.VISIBLE);



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



        return rootView;
    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }
}
