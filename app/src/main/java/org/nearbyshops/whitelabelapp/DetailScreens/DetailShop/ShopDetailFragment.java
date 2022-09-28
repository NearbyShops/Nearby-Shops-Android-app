package org.nearbyshops.whitelabelapp.DetailScreens.DetailShop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import okhttp3.ResponseBody;

import org.nearbyshops.whitelabelapp.API.FavouriteShopService;
import org.nearbyshops.whitelabelapp.API.ShopImageService;
import org.nearbyshops.whitelabelapp.API.ShopReviewService;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopReviewEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelImages.ShopImage;
import org.nearbyshops.whitelabelapp.Model.ModelReviewShop.FavouriteShop;
import org.nearbyshops.whitelabelapp.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyReviewUpdate;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ImageScreens.ImageList.ImageListForShop.ShopImageList;
import org.nearbyshops.whitelabelapp.ShopReview.ShopReviews;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelShop;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;


public class ShopDetailFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, Target, RatingBar.OnRatingBarChangeListener, NotifyReviewUpdate {




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static final String TAG_JSON_STRING = "shop_json_string";


    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;


    @Inject
    ShopReviewService shopReviewService;

    @Inject
    FavouriteShopService favouriteShopService;

    @Inject
    ShopImageService shopImageService;



    @BindView(R.id.shop_profile_photo)
    ImageView shopProfilePhoto;
    @BindView(R.id.image_count)
    TextView imagesCount;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    @BindView(R.id.shop_name) TextView shopName;

    @BindView(R.id.shop_rating_numeric) TextView shopRatingNumeric;
    @BindView(R.id.shop_rating) RatingBar ratingBar;
    @BindView(R.id.rating_count) TextView ratingCount;

    @BindView(R.id.phone) TextView shopPhone;

    @BindView(R.id.shop_description) TextView shopDescription;
    @BindView(R.id.read_full_button) TextView readFullDescription;


    @BindView(R.id.shop_address) TextView shopAddress;
//    @BindView(R.id.get_directions) TextView getDirections;
//    @BindView(R.id.see_on_map) TextView seeOnMap;

    @BindView(R.id.phone_delivery) TextView phoneDelivery;

    @BindView(R.id.delivery_charge_text) TextView deliveryChargeText;
    @BindView(R.id.free_delivery_info) TextView freeDeliveryInfo;

    @BindView(R.id.shop_reviews)
    RecyclerView shopReviews;




    @BindView(R.id.user_rating_review)
    LinearLayout user_review_ratings_block;
    @BindView(R.id.edit_review_text) TextView edit_review_text;
    @BindView(R.id.ratingBar_rate) RatingBar ratingBar_rate;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.delivery_block) LinearLayout deliveryBlock;


    @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
    @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;



    private Shop shop;



    private ShopReview reviewForUpdate;

    private ViewModelShop viewModelShop;
    private ProgressDialog progressDialog;










    public ShopDetailFragment() {
        // Required empty public constructor


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


            setRetainInstance(true);
            View rootView = inflater.inflate(R.layout.fragment_shop_detail, container, false);
            ButterKnife.bind(this,rootView);


            ratingBar_rate.setOnRatingBarChangeListener(this);

            setupSwipeContainer();


            String shopJson = getActivity().getIntent().getStringExtra(TAG_JSON_STRING);
            shop = UtilityFunctions.provideGson().fromJson(shopJson, Shop.class);



            toolbar.setTitle(shop.getShopName());


    //        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
    //
    //        if(actionBar!=null)
    //        {
    //            actionBar.setTitle(shop.getShopName());
    //        }




            bindViews();


            getShopImageCount();




            if (shop != null) {
                checkUserReview();
            }




            checkFavourite();



            viewModelShop = new ViewModelShop(MyApplication.application);


            viewModelShop.getShopLive().observe(getViewLifecycleOwner(), new Observer<Shop>() {
                @Override
                public void onChanged(Shop shop) {


                    ShopDetailFragment.this.shop = shop;
                    bindViews();
                }
            });


            viewModelShop.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {


                    if(integer == ViewModelShop.EVENT_SHOP_DETAILS_FETCHED || integer == ViewModelShop.EVENT_NETWORK_FAILED)
                    {
                        if(progressDialog!=null)
                        {
                            progressDialog.dismiss();
                        }

                        swipeContainer.setRefreshing(false);
                    }

                }
            });




            viewModelShop.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    showToastMessage(s);
                }
            });





        makeRefreshNetworkCall();


        return rootView;
    }












    @OnClick(R.id.see_reviews)
    void seeAllReviews()
    {

        Intent intent = new Intent(getActivity(), ShopReviews.class);
//        intent.putExtra(ShopReviews.SHOP_INTENT_KEY, shop);

        String shopJson = UtilityFunctions.provideGson().toJson(shop);
        intent.putExtra(ShopReviews.SHOP_INTENT_KEY,shopJson);

        startActivity(intent);
    }




    private void setupSwipeContainer()
    {
        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }






    @Override
    public void onRefresh() {

        viewModelShop.getShopDetailsForItemsInShopScreen(shop.getShopID());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait ... getting Shop Details !");
        progressDialog.show();
    }





    private void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                if(isDestroyed)
                {
                    swipeContainer.setRefreshing(false);
                    return;
                }

                onRefresh();
            }
        });
    }









    private void bindViews()
    {
        shopName.setText(shop.getShopName());



        if(shop.getRt_rating_count()==0)
        {
            shopRatingNumeric.setText(" New ");
            shopRatingNumeric.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.buttonColor));
            ratingBar.setVisibility(View.GONE);
            ratingCount.setVisibility(View.GONE);
        }
        else
        {
            shopRatingNumeric.setText(String.format("%.2f",shop.getRt_rating_avg()));
            ratingBar.setRating(shop.getRt_rating_avg());
            ratingCount.setText("(" + shop.getRt_rating_count() + " ratings )");
        }

        shopPhone.setText(shop.getCustomerHelplineNumber());
        shopDescription.setText(shop.getLongDescription());


        String shop_address = shop.getShopAddress()  + ", " + shop.getCity() + " - " + shop.getPincode() + "\n" + shop.getLandmark();


        shopAddress.setText(shop_address);
        phoneDelivery.setText(shop.getDeliveryHelplineNumber());


        deliveryChargeText.setText("Delivery Charge : " + PrefCurrency.getCurrencySymbol(getActivity()) + " " + shop.getDeliveryCharges() + " Per Delivery");
        freeDeliveryInfo.setText("Free Delivery for orders above " + PrefCurrency.getCurrencySymbol(getActivity()) + " " + shop.getBillAmountForFreeDelivery());



        String imagePath = PrefGeneral.getServerURL(getActivity()) + "/api/v1/Shop/Image/five_hundred_"
                + shop.getLogoImagePath() + ".jpg";

//            if (!shop.getBookCoverImageURL().equals("")) {

        Drawable placeholder = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_nature_people_white_48px, getActivity().getTheme());

        Picasso.get().load(imagePath)
                .placeholder(placeholder)
                .into(shopProfilePhoto);


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(this);






        if(shop.getPickFromShopAvailable())
        {
            pickFromShopIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            pickFromShopIndicator.setVisibility(View.GONE);
        }





        if(shop.getHomeDeliveryAvailable())
        {
            homeDeliveryIndicator.setVisibility(View.VISIBLE);
            deliveryBlock.setVisibility(View.VISIBLE);
        }
        else
        {
            homeDeliveryIndicator.setVisibility(View.GONE);
            deliveryBlock.setVisibility(View.GONE);
        }




    }








    private void getShopImageCount()
    {
        Call<ShopImageEndPoint> call = shopImageService.getShopImages(
                shop.getShopID(), ShopImage.IMAGE_ORDER,
                null,null,
                true,true
        );


        call.enqueue(new Callback<ShopImageEndPoint>() {
            @Override
            public void onResponse(Call<ShopImageEndPoint> call, Response<ShopImageEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }
//
//
//                if(!isVisible())
//                {
//                    return;
//                }

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
            public void onFailure(Call<ShopImageEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                if(!isVisible())
                {
                    return;
                }


                showToastMessage("Loading Images Failed !");
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
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }








    private void showToastMessage(String message) {
        UtilityFunctions.showToastMessage(getActivity(),message);
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
        //  originalTitle.setTextColor(vibrantSwatch.getTitleTextColor());
        //}


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(vibrantDark);

        }




        shopName.setTextColor(vibrant);


        if (fab != null && vibrantDark != 0) {

            fab.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));

        }//fab.setBackgroundColor(vibrantDark);

        //originalTitle.setBackgroundColor(vibrantDark);


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








    @OnClick(R.id.shop_profile_photo)
    void profileImageClick() {
        Intent intent = new Intent(getActivity(), ShopImageList.class);
        intent.putExtra("shop_id", shop.getShopID());
        startActivity(intent);
    }





    @OnClick(R.id.read_full_button)
    void readFullButtonClick() {
/*
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW,R.id.author_name);
        layoutParams.setMargins(0,10,0,0);
        bookDescription.setLayoutParams(layoutParams);
*/

        shopDescription.setMaxLines(Integer.MAX_VALUE);
        readFullDescription.setVisibility(View.GONE);
    }







    @OnClick(R.id.get_directions)
    void getDirectionsPickup()
    {
        getDirections(shop.getLatCenter(),shop.getLonCenter());
    }






    @OnClick(R.id.see_on_map)
    void seeOnMapDestination()
    {
        seeOnMap(shop.getLatCenter(), shop.getLonCenter(), shop.getShopAddress());
    }




    private void getDirections(double lat, double lon)
    {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }



    private void seeOnMap(double lat, double lon, String label)
    {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lon + "(" + label + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }









    @OnClick({R.id.phone_icon, R.id.phone})
    void phoneClick()
    {
        dialPhoneNumber(shop.getCustomerHelplineNumber());
    }




    @OnClick({R.id.phone_icon_delivery, R.id.phone_delivery})
    void phoneDeliveryClick()
    {
        dialPhoneNumber(shop.getDeliveryHelplineNumber());
    }




    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }










    private boolean isFavourite = false;



    @OnClick(R.id.fab)
    void fabClick() {

        if (PrefLogin.getUser(getActivity()) == null) {

//            showToastMessage("Please Login to use this Feature !");
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







//    void toggleFavourite() {
//
//        if (shop != null && PrefLogin.getUser(getActivity()) != null) {
//
//            Call<FavouriteShopEndpoint> call = favouriteShopService.getFavouriteShops(shop.getShopID(), PrefLogin.getUser(getActivity()).getUserID()
//                    , null, null, null, null);
//
//
//            call.enqueue(new Callback<FavouriteShopEndpoint>() {
//                @Override
//                public void onResponse(Call<FavouriteShopEndpoint> call, Response<FavouriteShopEndpoint> response) {
//
//
//                    if (response.body() != null) {
//                        if (response.body().getItemCount() >= 1) {
//                            deleteFavourite();
//
//                        } else if (response.body().getItemCount() == 0) {
//                            insertFavourite();
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<FavouriteShopEndpoint> call, Throwable t) {
//
//                    showToastMessage("Network Request failed. Check Network Connection !");
//                }
//            });
//        }
//    }








    private void setFavouriteIcon(boolean isFavourite) {

        if (fab == null) {
            return;
        }


        this.isFavourite = isFavourite;


        if (isFavourite) {

            fab.setImageResource(R.drawable.ic_favorite_white_24px);

        }
        else
        {


            fab.setImageResource(R.drawable.ic_favorite_border_white_24px);
        }
    }





    private void checkFavourite() {

        // make a network call to check the favourite

        if (shop != null && PrefLogin.getUser(getActivity()) != null) {

            Call<ResponseBody> call = favouriteShopService.checkFavourite(shop.getShopID(), PrefLogin.getUser(getActivity()).getUserID());


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(isDestroyed)
                    {
                        return;
                    }


                    if(response.code()==200)
                    {
                        setFavouriteIcon(true);
                    }
                    else
                    {
                        setFavouriteIcon(false);
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Network Request failed. Check Network Connection !");
                }
            });



        }
    }





    private void insertFavourite() {


        if (shop != null && PrefLogin.getUser(getActivity()) != null) {

            FavouriteShop favouriteBook = new FavouriteShop();
            favouriteBook.setShopID(shop.getShopID());
            favouriteBook.setEndUserID(PrefLogin.getUser(getActivity()).getUserID());

            Call<FavouriteShop> call = favouriteShopService.insertFavouriteShop(favouriteBook);

            call.enqueue(new Callback<FavouriteShop>() {
                @Override
                public void onResponse(Call<FavouriteShop> call, Response<FavouriteShop> response) {


                    if (response.code() == 201) {
                        // created successfully

                        setFavouriteIcon(true);
//                        isFavourite = true;
                    }
                }

                @Override
                public void onFailure(Call<FavouriteShop> call, Throwable t) {

//                    if(!isVisible())
//                    {
//                        return;
//                    }

                    showToastMessage("Network Request failed !");

                }
            });
        }


    }





    private void deleteFavourite() {

        if (shop != null && PrefLogin.getUser(getActivity()) != null) {
            Call<ResponseBody> call = favouriteShopService.deleteFavouriteShop(shop.getShopID(),
                    PrefLogin.getUser(getActivity()).getUserID());


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    if(!isVisible())
                    {
                        return;
                    }

                    if (response.code() == 200) {
                        setFavouriteIcon(false);
//                        isFavourite = false;
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {


                    if(!isVisible())
                    {
                        return;
                    }

                    showToastMessage("Network Request Failed !");
                }
            });
        }
    }






    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        write_review_click();
    }




    @OnClick({R.id.edit_icon, R.id.edit_review_label})
    void edit_review_Click() {

        if (reviewForUpdate != null) {
            FragmentManager fm = getChildFragmentManager();
            RateReviewDialog dialog = new RateReviewDialog();
            dialog.show(fm, "rate");
            dialog.setMode(reviewForUpdate, true, reviewForUpdate.getShopID());
        }

    }







    @OnClick({R.id.edit_review_text, R.id.ratingBar_rate})
    void write_review_click() {

        if(PrefLogin.getUser(getActivity())==null)
        {
            showToastMessage("Login to rate and review !");
            showLoginDialog();
            return;
        }


        FragmentManager fm = getChildFragmentManager();
        RateReviewDialog dialog = new RateReviewDialog();
        dialog.show(fm, "rate");

        if (shop != null) {
            dialog.setMode(null, false, shop.getShopID());
        }
    }




    @Override
    public void notifyReviewUpdated() {

        checkUserReview();
    }




    @Override
    public void notifyReviewDeleted() {

        shop.setRt_rating_count(shop.getRt_rating_count() - 1);
        checkUserReview();
    }





    @Override
    public void notifyReviewSubmitted() {
        shop.setRt_rating_count(shop.getRt_rating_count() + 1);
        checkUserReview();
    }






    @BindView(R.id.edit_review_block)
    RelativeLayout edit_review_block;

    @BindView(R.id.review_title)
    TextView review_title;

    @BindView(R.id.review_description)
    TextView review_description;

    @BindView(R.id.review_date)
    TextView review_date;


    @BindView(R.id.member_profile_image)
    ImageView member_profile_image;

    @BindView(R.id.member_name)
    TextView member_name;

    @BindView(R.id.member_rating)
    RatingBar member_rating_indicator;


//    ShopReview reviewForUpdate;





    // method to check whether the user has written the review or not if the user is currently logged in.
    private void checkUserReview() {



        if (PrefLogin.getUser(getActivity()) == null) {

            user_review_ratings_block.setVisibility(View.VISIBLE);


        } else
            {

            // Unhide review dialog







            if (shop.getRt_rating_count() == 0) {

                user_review_ratings_block.setVisibility(View.VISIBLE);
                edit_review_block.setVisibility(View.GONE);

                edit_review_text.setText(R.string.shop_review_be_the_first_to_review);
            } else if (shop.getRt_rating_count() > 0) {


                Call<ShopReviewEndPoint> call = shopReviewService.getReviews(shop.getShopID(),
                        PrefLogin.getUser(getActivity()).getUserID(), true, "REVIEW_DATE", null, null, null);

//                Log.d("review_check",String.valueOf(UtilityGeneral.getUserID(this)) + " : " + String.valueOf(shop.getBookID()));

                call.enqueue(new Callback<ShopReviewEndPoint>() {
                    @Override
                    public void onResponse(Call<ShopReviewEndPoint> call, Response<ShopReviewEndPoint> response) {


                        if(isDestroyed)
                        {
                            return;
                        }


                        if (response.body() != null) {
                            if (response.body().getItemCount() > 0) {

//                                edit_review_text.setText("Edit your review and Rating !");


                                if (edit_review_block == null) {
                                    // If the views are not bound then return. This can happen in delayed response. When this call is executed
                                    // after the activity have gone out of scope.
                                    return;
                                }




                                edit_review_block.setVisibility(View.VISIBLE);
                                user_review_ratings_block.setVisibility(View.GONE);

                                reviewForUpdate = response.body().getResults().get(0);

                                review_title.setText(response.body().getResults().get(0).getReviewTitle());
                                review_description.setText(response.body().getResults().get(0).getReviewText());

                                review_date.setText(response.body().getResults().get(0).getReviewDate().toLocaleString());

                                member_rating_indicator.setRating(response.body().getResults().get(0).getRating());


//                                user_review.setText(response.body().getResults().get(0).getReviewText());
//                                ratingBar_rate.setRating(response.body().getResults().get(0).getRating());

                                User member = response.body().getResults().get(0).getRt_end_user_profile();
                                member_name.setText(member.getName());

//                                String imagePath = PrefGeneral.getImageEndpointURL(getActivity())
//                                        + member.getProfileImagePath();

                                String imagepath = PrefGeneral.getServerURL(getContext()) + "/api/v1/User/Image/five_hundred_"
                                        + member.getProfileImagePath() + ".jpg";


                                Drawable placeholder = VectorDrawableCompat
                                        .create(getResources(),
                                                R.drawable.ic_nature_people_white_48px,null);


                                Picasso.get()
                                        .load(imagepath)
                                        .placeholder(placeholder)
                                        .into(member_profile_image);


                            } else if (response.body().getItemCount() == 0) {
                                edit_review_text.setText("Rate this shop !");
                                edit_review_block.setVisibility(View.GONE);
                                user_review_ratings_block.setVisibility(View.VISIBLE);

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ShopReviewEndPoint> call, Throwable t) {


                        if(!isVisible())
                        {
                            return;
                        }


//                        showToastMessage("Network Request Failed. Check your internet connection !");

                    }
                });


            }

            // check shop ratings count
            // If ratings count is 0 then set message : Be the first to review


            // If ratings count is >0 then
            // check if user has written the review or not
            // if Yes
            // Write messsage : Edit your review and rating
            // If NO
            // Write message : Rate and Review this shop

        }

    }






    private void showLoginDialog() {

//        Intent intent = new Intent(getActivity(), Login.class);
//        startActivity(intent);


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
            checkUserReview();
        }
    }







}
