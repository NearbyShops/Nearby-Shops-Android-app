package org.nearbyshops.whitelabelapp.UtilityScreens.PhotoSliderViewPager;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.nearbyshops.whitelabelapp.UtilityScreens.PhotoSliderViewPager.Model.PhotoSliderData;
import org.nearbyshops.whitelabelapp.R;

import java.util.List;

public class PhotosAdapter extends PagerAdapter {



    List<PhotoSliderData> photos;

    public PhotosAdapter(FragmentManager childFragmentManager, List<PhotoSliderData> photos) {
        this.photos = photos;
    }


    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return object==view;
    }





    @Override
    public Object instantiateItem(final ViewGroup container, int position) {


        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_pager_item_login_photo, container, false);

        ImageView imageView = layout.findViewById(R.id.image_view);
        TextView message = layout.findViewById(R.id.message);
        TextView mainMessage = layout.findViewById(R.id.main_message);
        LinearLayout textMessageBlock = layout.findViewById(R.id.text_message_block);


        if(photos.get(position).isHideTextBlock())
        {
            textMessageBlock.setVisibility(View.GONE);
        }
        else
        {
            textMessageBlock.setVisibility(View.VISIBLE);
        }


        message.setText(photos.get(position).getMessage());
        mainMessage.setText(photos.get(position).getMainMessage());

        Drawable placeholder = VectorDrawableCompat
                .create(container.getContext().getResources(),
                        R.drawable.ic_nature_people_white_48px, container.getContext().getTheme());

        Picasso.get()
                .load(photos.get(position).getPhotoURL())
                .placeholder(placeholder)
                .into(imageView);

        container.addView(layout);


        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



}
