package org.nearbyshops.enduserappnew.DetailScreens.DetailItem;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import okhttp3.ResponseBody;

import org.nearbyshops.enduserappnew.API.FavouriteItemService;
import org.nearbyshops.enduserappnew.API.ItemImageService;
import org.nearbyshops.enduserappnew.API.ItemReviewService;
import org.nearbyshops.enduserappnew.API.ItemSpecNameService;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.FavouriteItemEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ItemImageEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelImages.ItemImage;
import org.nearbyshops.enduserappnew.Model.ModelItemSpecs.ItemSpecificationName;
import org.nearbyshops.enduserappnew.Model.ModelReviewItem.FavouriteItem;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ImageList.ImageListForItem.ItemImageList;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ItemDetailFragment extends Fragment implements Target {



    public static final String TAG_JSON_STRING = "item_json_string";



    @BindView(R.id.item_image) ImageView itemImage;
    @BindView(R.id.image_count) TextView imagesCount;

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;


    @BindView(R.id.item_name) TextView itemName;

    @BindView(R.id.item_rating_numeric) TextView itemRatingNumeric;
    @BindView(R.id.item_rating) RatingBar itemRating;
    @BindView(R.id.rating_count) TextView ratingCount;


    @BindView(R.id.item_description) TextView itemDescription;
    @BindView(R.id.read_full_button) TextView readFullButton;


    private Item item;






    @Inject ItemReviewService itemReviewService;
    @Inject FavouriteItemService favouriteItemService;
    @Inject ItemImageService itemImageService;
    @Inject ItemSpecNameService itemSpecNameService;





    public ItemDetailFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        ButterKnife.bind(this,rootView);



        String shopJson = getActivity().getIntent().getStringExtra(TAG_JSON_STRING);
        item = UtilityFunctions.provideGson().fromJson(shopJson, Item.class);


        bindViews();
        setupRecyclerViewSpecs();
        getItemImageCount();
        checkFavourite();



        return rootView;
    }




    @OnClick(R.id.item_image)
    void profileImageClick()
    {
        Intent intent = new Intent(getActivity(), ItemImageList.class);
        intent.putExtra("item_id",item.getItemID());
        startActivity(intent);
    }








    private void bindViews()
    {
        itemName.setText(item.getItemName());

//        itemRatingNumeric.setText(String.format("%.2f",item.getRt_rating_avg()));
//        itemRating.setRating(item.getRt_rating_avg());
//        ratingCount.setText("(" + item.getRt_rating_count() + " ratings )");

        itemDescription.setText(item.getItemDescriptionLong());



        if(item.getRt_rating_count()==0)
        {
            itemRatingNumeric.setText(" New ");
            itemRatingNumeric.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));

            ratingCount.setVisibility(View.GONE);
            itemRating.setVisibility(View.GONE);
        }
        else
        {
            itemRatingNumeric.setText(String.format("%.2f",item.getRt_rating_avg()));
            itemRatingNumeric.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gplus_color_2));

            ratingCount.setText("( " + (int) item.getRt_rating_count() + " Ratings )");

            ratingCount.setVisibility(View.VISIBLE);
            itemRating.setVisibility(View.VISIBLE);
        }





        String imagePath = PrefGeneral.getServiceURL(getActivity()) + "/api/v1/Item/Image/seven_hundred_"
                + item.getItemImageURL() + ".jpg";


        Drawable placeholder = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_nature_people_white_48px, getActivity().getTheme());


        Picasso.get().load(imagePath)
                .placeholder(placeholder)
                .into(itemImage);


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(this);

    }






    @OnClick(R.id.see_reviews)
    void seeAllReviews()
    {
//        showToastMessage("See all reviews click !");
    }




    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }







    @OnClick(R.id.read_full_button)
    void readFullButtonClick() {

        itemDescription.setMaxLines(Integer.MAX_VALUE);
        readFullButton.setVisibility(View.GONE);
    }







    private void showLoginDialog() {

        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent,123);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123 && resultCode == RESULT_OK)
        {
            // login success
            checkFavourite();
//            checkUserReview();

        }
    }











    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        Palette palette = Palette.from(bitmap).generate();

        int color = getResources().getColor(R.color.colorPrimaryDark);
        int colorLight = getResources().getColor(R.color.colorPrimary);
        int vibrant = palette.getVibrantColor(color);
        int vibrantLight = palette.getLightVibrantColor(color);
        int vibrantDark = palette.getDarkVibrantColor(colorLight);
        int muted = palette.getMutedColor(color);
        int mutedLight = palette.getLightMutedColor(color);
        int mutedDark = palette.getDarkMutedColor(color);

        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

        //if(vibrantSwatch!=null) {
//          originalTitle.setTextColor(vibrantSwatch.getTitleTextColor());
        //}



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(vibrantDark);

        }


        if (fab != null && vibrantDark != 0) {

            fab.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));

        }//fab.setBackgroundColor(vibrantDark);


//        itemName.setBackgroundColor(vibrantDark);
        itemName.setTextColor(vibrantDark);



        if (collapsingToolbarLayout != null) {

            collapsingToolbarLayout.setContentScrimColor(vibrant);
        }
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }



    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }






    ArrayList<ItemSpecificationName> datasetSpecs = new ArrayList<>();


    @BindView(R.id.recyclerview_item_specifications) RecyclerView itemSpecsList;

    AdapterItemSpecifications adapterItemSpecs;
    GridLayoutManager layoutManagerItemSpecs;

    @BindView(R.id.item_specifications_header) TextView itemSpecificationHeader;




    private void setupRecyclerViewSpecs()
    {
        adapterItemSpecs = new AdapterItemSpecifications(datasetSpecs,getActivity());
        itemSpecsList.setAdapter(adapterItemSpecs);
        layoutManagerItemSpecs = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
        itemSpecsList.setLayoutManager(layoutManagerItemSpecs);

        makeNetworkCallSpecs(true);
    }






    private void makeNetworkCallSpecs(final boolean clearDataset)
    {
        Call<List<ItemSpecificationName>> call = itemSpecNameService.getItemSpecName(
                item.getItemID(),null
        );


        call.enqueue(new Callback<List<ItemSpecificationName>>() {
            @Override
            public void onResponse(Call<List<ItemSpecificationName>> call, Response<List<ItemSpecificationName>> response) {

                if(response.code()==200)
                {
                    if(clearDataset)
                    {
                        datasetSpecs.clear();
                    }

                    datasetSpecs.addAll(response.body());


                    if(datasetSpecs.size()==0)
                    {
                        itemSpecificationHeader.setVisibility(View.GONE);
                    }
                    else
                    {
                        itemSpecificationHeader.setVisibility(View.VISIBLE);
                    }




                    adapterItemSpecs.notifyDataSetChanged();
                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not Permitted");
                }
                else
                {
                    showToastMessage("Failed to load specs : code " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<ItemSpecificationName>> call, Throwable t) {

                showToastMessage("Failed !");
            }
        });
    }









    private boolean isDestroyed = false;

    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }








    private boolean isFavourite = false;


    @OnClick(R.id.fab)
    void fabClick()
    {

        if(PrefLogin.getUser(getActivity())==null)
        {
            // User Not logged In.
            showToastMessage("Please Login to use this Feature !");
            showLoginDialog();


        }
        else
        {
            if(isFavourite)
            {
                deleteFavourite();
            }
            else
            {
                insertFavourite();
            }
        }
    }




//    private void showLoginDialog()
//    {
//        Intent intent = new Intent(getActivity(),Login.class);
//        startActivity(intent);
//    }








    private void insertFavourite()
    {


        if(item !=null && PrefLogin.getUser(getActivity())!=null)
        {

            FavouriteItem favouriteItem = new FavouriteItem();
            favouriteItem.setItemID(item.getItemID());
            favouriteItem.setEndUserID(PrefLogin.getUser(getActivity()).getUserID());

            Call<FavouriteItem> call = favouriteItemService.insertFavouriteItem(favouriteItem);

            call.enqueue(new Callback<FavouriteItem>() {
                @Override
                public void onResponse(Call<FavouriteItem> call, Response<FavouriteItem> response) {

                    if(response.code() == 201)
                    {
                        // created successfully

                        setFavouriteIcon(true);
//                        isFavourite = true;
                    }
                }

                @Override
                public void onFailure(Call<FavouriteItem> call, Throwable t) {

                    showToastMessage("Network Request failed !");

                }
            });
        }
    }






    private void deleteFavourite()
    {

        if(item !=null && PrefLogin.getUser(getActivity())!=null)
        {
            Call<ResponseBody> call = favouriteItemService.deleteFavouriteItem(item.getItemID(),
                    PrefLogin.getUser(getActivity()).getUserID());


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {
                        setFavouriteIcon(false);
//                        isFavourite = false;
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Network Request Failed !");
                }
            });
        }



    }






    private void setFavouriteIcon(boolean isFavourite)
    {

        this.isFavourite = isFavourite;



        if(fab==null)
        {
            return;
        }



        if (isFavourite) {

            fab.setImageResource(R.drawable.ic_favorite_white_24px);
        }
        else
        {
            fab.setImageResource(R.drawable.ic_favorite_border_white_24px);
        }
    }






    private void checkFavourite()
    {

        // make a network call to check the favourite

//        Log.d("Before Check Favourite", "Item ID : EndUser ID" + String.valueOf(item.getItemID()) + " : " + String.valueOf(UtilityLogin.getEndUser(this).getEndUserID()));

        if(PrefLogin.getUser(getActivity())==null)
        {
            return;
        }


        if(item != null && PrefLogin.getUser(getActivity()) != null)
        {


            Log.d("After Favourite", "Item ID : EndUser ID" + item.getItemID() + " : " + PrefLogin.getUser(getActivity()).getUserID());

            Call<FavouriteItemEndpoint> call = favouriteItemService.getFavouriteBooks(item.getItemID(),
                    PrefLogin.getUser(getActivity()).getUserID()
                    ,null,null,null,null);


            call.enqueue(new Callback<FavouriteItemEndpoint>() {
                @Override
                public void onResponse(Call<FavouriteItemEndpoint> call, Response<FavouriteItemEndpoint> response) {


                    if(response.body()!=null)
                    {

                        Log.d("After Favourite", "Item Count : " + response.body().getItemCount());


                        if(response.body().getItemCount()>=1)
                        {
                            setFavouriteIcon(true);


                        }
                        else if(response.body().getItemCount()==0)
                        {
                            setFavouriteIcon(false);

                        }
                    }

                }

                @Override
                public void onFailure(Call<FavouriteItemEndpoint> call, Throwable t) {

                    showToastMessage("Network Request failed. Check Network Connection !");
                }
            });

        }
    }







    private void getItemImageCount()
    {

        Call<ItemImageEndPoint> call = itemImageService.getItemImages(
                item.getItemID(), ItemImage.IMAGE_ORDER,
                null,null,
                true
        );


        call.enqueue(new Callback<ItemImageEndPoint>() {
            @Override
            public void onResponse(Call<ItemImageEndPoint> call, Response<ItemImageEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!=null)
                {
                    int count = response.body().getItemCount();


                    if(count==0)
                    {
                        imagesCount.setVisibility(View.GONE);
                    }
                    else
                    {
                        imagesCount.setText(count + " Photos");
                    }

                }
            }

            @Override
            public void onFailure(Call<ItemImageEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Loading Images Failed !");
            }
        });
    }

}


