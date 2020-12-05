package org.nearbyshops.enduserappnew.DetailScreens.DetailShopItem;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelImages.ItemImage;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends
        SliderViewAdapter<SliderViewAdapter.ViewHolder> {



    public static final int VIEW_TYPE_MAIN_IMAGE = 1;
    public static final int VIEW_TYPE_EXTRA_IMAGE = 2;


    private Context context;
    private Fragment fragment;
    private List<Object> mSliderItems = new ArrayList<>();



    public SliderAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public void renewItems(List<Object> sliderItems) {

//        this.mSliderItems = sliderItems;

        this.mSliderItems.clear();
        this.mSliderItems.addAll(sliderItems);

        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(ItemImage sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }



    @Override
    public SliderViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {


        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image_slider, null);

        return new VHSliderExtraImages(inflate);
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if(mSliderItems.get(position) instanceof ItemImage)
        {
            ItemImage sliderItem = (ItemImage) mSliderItems.get(position);



//        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Item/Image/seven_hundred_"
//                + sliderItem.getImageFilename() + ".jpg";

            String imagePathSmall = PrefGeneral.getServiceURL(context) + "/api/v1/ItemImage/Image/nine_hundred_" + sliderItem.getImageFilename() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            VHSliderExtraImages viewHolderEI = (VHSliderExtraImages) viewHolder;

            Picasso.get().load(imagePathSmall)
                    .placeholder(placeholder)
                    .into(viewHolderEI.imageViewBackground);

        }
        else if(mSliderItems.get(position) instanceof Item)
        {
            Item item = (Item) mSliderItems.get(position);



            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/Item/Image/seven_hundred_"
                    + item.getItemImageURL() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            VHSliderExtraImages viewHolderEI = (VHSliderExtraImages) viewHolder;

            Picasso.get().load(imagePath)
                    .placeholder(placeholder)
                    .into(viewHolderEI.imageViewBackground);



        }




        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UtilityFunctions.showToastMessage(context,"This is item in position " + position);

                if(fragment instanceof SliderListItemClick)
                {
                    ((SliderListItemClick) fragment).sliderItemClick();
                }

            }
        });
    }




    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }










    class VHSliderExtraImages extends SliderViewAdapter.ViewHolder {


        public ImageView imageViewBackground;


        public VHSliderExtraImages(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.image_view);
        }
    }




    public interface SliderListItemClick
    {
        void sliderItemClick();
    }

}