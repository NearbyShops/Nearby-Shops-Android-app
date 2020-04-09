package org.nearbyshops.enduserappnew.DetailScreens.DetailMarket;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import org.nearbyshops.enduserappnew.API.API_SDS.FavouriteMarketService;
import org.nearbyshops.enduserappnew.API.API_SDS.MarketReviewService;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.FavouriteMarketEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.MarketReviewEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelReviewMarket.FavouriteMarket;
import org.nearbyshops.enduserappnew.Model.ModelReviewMarket.MarketReview;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifyReviewUpdate;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;


public class MarketDetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener ,
        Target, RatingBar.OnRatingBarChangeListener, NotifyReviewUpdate {



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static final String TAG_JSON_STRING = "market_json_string";


    @Inject Gson gson;


//    @Inject
//    ShopReviewService shopReviewService;

//    @Inject
//    FavouriteShopService favouriteShopService;




    @BindView(R.id.profile_photo) ImageView shopProfilePhoto;
    @BindView(R.id.image_count) TextView imagesCount;

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;


    @BindView(R.id.market_name) TextView shopName;

    @BindView(R.id.rating_avg) TextView shopRatingNumeric;
    @BindView(R.id.market_rating) RatingBar ratingBar;
    @BindView(R.id.rating_count) TextView ratingCount;

    @BindView(R.id.phone) TextView shopPhone;

    @BindView(R.id.market_description) TextView shopDescription;
    @BindView(R.id.read_full_button) TextView readFullDescription;


    @BindView(R.id.market_address) TextView shopAddress;
//    @BindView(R.id.get_directions) TextView getDirections;
//    @BindView(R.id.see_on_map) TextView seeOnMap;





//    @BindView(R.id.delivery_block) LinearLayout deliveryBlock;
//    @BindView(R.id.phone_delivery) TextView phoneDelivery;
//    @BindView(R.id.delivery_charge_text) TextView deliveryChargeText;
//    @BindView(R.id.free_delivery_info) TextView freeDeliveryInfo;
    @BindView(R.id.shop_reviews) RecyclerView shopReviews;




    @BindView(R.id.user_rating_review) LinearLayout user_review_ratings_block;
    @BindView(R.id.edit_review_text) TextView edit_review_text;
    @BindView(R.id.ratingBar_rate) RatingBar ratingBar_rate;


    @BindView(R.id.toolbar)
    Toolbar toolbar;




//    @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
//    @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;




    private ServiceConfigurationGlobal market;
    private MarketReview reviewForUpdate;



    public MarketDetailFragment() {
        // Required empty public constructor


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }



//    public static MarketDetailFragment newInstance(String param1, String param2) {
//        MarketDetailFragment fragment = new MarketDetailFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }






//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_market_detail, container, false);
        ButterKnife.bind(this,rootView);


        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


        ratingBar_rate.setOnRatingBarChangeListener(this);

//        setupSwipeContainer();




        String json = getActivity().getIntent().getStringExtra(TAG_JSON_STRING);
        market = UtilityFunctions.provideGson().fromJson(json, ServiceConfigurationGlobal.class);


        toolbar.setTitle(market.getServiceName());
        bindViews();




//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//
//        if(actionBar!=null)
//        {
//            actionBar.setTitle(market.getShopName());
//        }







//        getShopImageCount();



        if (market != null) {
            checkUserReview();
        }


        checkFavourite();







        return rootView;
    }







    @OnClick(R.id.see_reviews)
    void seeAllReviews()
    {
//        Intent intent = new Intent(getActivity(), ShopReviews.class);
//        String shopJson = UtilityFunctions.provideGson().toJson(market);
//        intent.putExtra(ShopReviews.SHOP_INTENT_KEY,shopJson);
//
//        startActivity(intent);
    }






//    void setupSwipeContainer()
//    {
//        if(swipeContainer!=null) {
//
//            swipeContainer.setOnRefreshListener(this);
//            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                    android.R.color.holo_green_light,
//                    android.R.color.holo_orange_light,
//                    android.R.color.holo_red_light);
//        }
//
//    }


    @Override
    public void onRefresh() {
//        swipeContainer.setRefreshing(false);
    }







    private void bindViews()
    {
        shopName.setText(market.getServiceName());


        if(market.getRt_rating_count()==0)
        {
            shopRatingNumeric.setText(" New ");
            shopRatingNumeric.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));

            ratingCount.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);

        }
        else
        {
            shopRatingNumeric.setText(String.format("%.2f",market.getRt_rating_avg()));
            ratingCount.setText("( " + (int) market.getRt_rating_count() + " Ratings )");

            ratingBar.setVisibility(View.VISIBLE);
            ratingBar.setRating(market.getRt_rating_avg());

            shopRatingNumeric.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gplus_color_2));
            ratingCount.setVisibility(View.VISIBLE);
        }



//        shopRatingNumeric.setText(String.format("%.2f", market.getRt_rating_avg()));
//        ratingBar.setRating(market.getRt_rating_avg());
//        ratingCount.setText("(" + market.getRt_rating_count() + " ratings )");




        shopPhone.setText(market.getHelplineNumber());
        shopDescription.setText(market.getDescriptionLong());



        String market_address = market.getAddress()  + ", " + market.getCity() + " - " + market.getPincode() + "\n" + market.getLandmark();


        shopAddress.setText(market_address);
//        phoneDelivery.setText(market.getHelplineNumber());



//        String imagePath = PrefServiceConfig.getServiceURL_SDS(getActivity()) + "/api/v1/ServiceConfiguration/Image/five_hundred_"
//                + market.getLogoImagePath() + ".jpg";

        String imagePath = PrefServiceConfig.getServiceURL_SDS(getActivity())
                + "/api/v1/ServiceConfiguration/Image/five_hundred_" + market.getLogoImagePath() + ".jpg";


        Drawable placeholder = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_nature_people_white_48px, getActivity().getTheme());

        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(shopProfilePhoto);

        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(this);



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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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








    @OnClick(R.id.profile_photo)
    void profileImageClick() {
//        Intent intent = new Intent(getActivity(), ShopImageList.class);
//        intent.putExtra("shop_id", market.getShopID());
//        startActivity(intent);
    }








    @OnClick(R.id.read_full_button)
    void readFullButtonClick() {

        shopDescription.setMaxLines(Integer.MAX_VALUE);
        readFullDescription.setVisibility(View.GONE);
    }







    @OnClick(R.id.get_directions)
    void getDirectionsPickup()
    {
        getDirections(market.getLatCenter(), market.getLonCenter());
    }






    @OnClick(R.id.see_on_map)
    void seeOnMapDestination()
    {
        seeOnMap(market.getLatCenter(), market.getLonCenter(), market.getAddress());
    }




    void getDirections(double lat,double lon)
    {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }



    void seeOnMap(double lat,double lon,String label)
    {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lon + "(" + label + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }









    @OnClick({R.id.phone_icon, R.id.phone})
    void phoneClick()
    {
        dialPhoneNumber(market.getHelplineNumber());
    }




//    @OnClick({R.id.phone_icon_delivery,R.id.phone_delivery})
//    void phoneDeliveryClick()
//    {
//        dialPhoneNumber(market.getHelplineNumber());
//    }




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

        if (PrefLoginGlobal.getUser(getActivity()) == null) {

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
//            toggleFavourite();
        }
    }











    private void setFavouriteIcon(boolean isFavourite) {

        if (fab == null) {
            return;
        }


        this.isFavourite = isFavourite;


        if (isFavourite) {


//            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_favorite_white_24px, getActivity().getTheme());
//            fab.setImageDrawable(drawable);


            fab.setImageResource(R.drawable.ic_favorite_white_24px);

        } else {
//            Drawable drawable2 = VectorDrawableCompat.create(getResources(), R.drawable.ic_favorite_border_white_24px, getActivity().getTheme());
//            fab.setImageDrawable(drawable2);


            fab.setImageResource(R.drawable.ic_favorite_border_white_24px);
        }
    }







    private void checkFavourite() {

        // make a network call to check the favourite

        Log.d("app_log","Inside Check Favourite : " );


        if (market != null && PrefLoginGlobal.getUser(getActivity()) != null) {




            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(getActivity()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();



            Call<FavouriteMarketEndpoint> call = retrofit.create(FavouriteMarketService.class)
                    .getFavouriteMarkets(market.getServiceID(), PrefLoginGlobal.getUser(getActivity()).getUserID()
                    , null, null, null, null);


            call.enqueue(new Callback<FavouriteMarketEndpoint>() {
                @Override
                public void onResponse(Call<FavouriteMarketEndpoint> call, Response<FavouriteMarketEndpoint> response) {



                    if (response.body() != null) {


                        Log.d("app_log","Item Count Favourite : " + response.body().getItemCount());

                        if (response.body().getItemCount() >= 1) {

                            setFavouriteIcon(true);
//                            isFavourite = true;

                        } else if (response.body().getItemCount() == 0) {

                            setFavouriteIcon(false);
//                            isFavourite = false;
                        }
                    }

                }

                @Override
                public void onFailure(Call<FavouriteMarketEndpoint> call, Throwable t) {

                    showToastMessage("Network Request failed. Check Network Connection !");
                }
            });



        }
    }








    private void insertFavourite() {


        if (market != null && PrefLoginGlobal.getUser(getActivity()) != null) {

            FavouriteMarket favouriteBook = new FavouriteMarket();
            favouriteBook.setItemID(market.getServiceID());
            favouriteBook.setEndUserID(PrefLoginGlobal.getUser(getActivity()).getUserID());




            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(getActivity()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();


            Call<FavouriteMarket> call = retrofit.create(FavouriteMarketService.class).insertFavouriteItem(
                    PrefLoginGlobal.getAuthorizationHeaders(getActivity()),
                    favouriteBook
            );


            call.enqueue(new Callback<FavouriteMarket>() {
                @Override
                public void onResponse(Call<FavouriteMarket> call, Response<FavouriteMarket> response) {

                    if (response.code() == 201) {
                        // created successfully

                        setFavouriteIcon(true);
//                        isFavourite = true;
                    }
                }

                @Override
                public void onFailure(Call<FavouriteMarket> call, Throwable t) {

                    showToastMessage("Network Request failed !");
                }
            });


        }


    }







    private void deleteFavourite() {

        if (market != null && PrefLoginGlobal.getUser(getActivity()) != null) {




            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(getActivity()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();




            Call<ResponseBody> call = retrofit.create(FavouriteMarketService.class).deleteFavouriteItem(
                    PrefLoginGlobal.getAuthorizationHeaders(getActivity()),
                    market.getServiceID(),
                    PrefLoginGlobal.getUser(getActivity()).getUserID());


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {
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






    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        write_review_click();
    }




    @OnClick({R.id.edit_icon, R.id.edit_review_label})
    void edit_review_Click() {

        if (reviewForUpdate != null) {
            FragmentManager fm = getChildFragmentManager();
            RateReviewDialogMarket dialog = new RateReviewDialogMarket();
            dialog.show(fm, "rate");
            dialog.setMode(reviewForUpdate, true, reviewForUpdate.getItemID());
        }

    }







    @OnClick({R.id.edit_review_text, R.id.ratingBar_rate})
    void write_review_click() {

        if(PrefLoginGlobal.getUser(getActivity())==null)
        {
            showToastMessage("Login to rate and review !");
            showLoginDialog();
            return;
        }


        FragmentManager fm = getChildFragmentManager();
        RateReviewDialogMarket dialog = new RateReviewDialogMarket();
        dialog.show(fm, "rate");

        if (market != null) {
            dialog.setMode(null, false, market.getServiceID());
        }
    }




    @Override
    public void notifyReviewUpdated() {

        checkUserReview();
    }




    @Override
    public void notifyReviewDeleted() {

        market.setRt_rating_count(market.getRt_rating_count() - 1);
        checkUserReview();
    }





    @Override
    public void notifyReviewSubmitted() {
        market.setRt_rating_count(market.getRt_rating_count() + 1);
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



        if (PrefLoginGlobal.getUser(getActivity()) == null) {

            user_review_ratings_block.setVisibility(View.VISIBLE);


        } else
            {

            // Unhide review dialog







            if (market.getRt_rating_count() == 0) {

                user_review_ratings_block.setVisibility(View.VISIBLE);
                edit_review_block.setVisibility(View.GONE);

                edit_review_text.setText(R.string.market_review_be_the_first_to_review);
            } else if (market.getRt_rating_count() > 0) {




                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .baseUrl(PrefServiceConfig.getServiceURL_SDS(getActivity()))
                        .client(new OkHttpClient().newBuilder().build())
                        .build();


                Call<MarketReviewEndPoint> call = retrofit.create(MarketReviewService.class)
                        .getReviews(market.getServiceID(),
                        PrefLoginGlobal.getUser(getActivity()).getUserID(), true, "REVIEW_DATE", null, null, null);




                call.enqueue(new Callback<MarketReviewEndPoint>() {
                    @Override
                    public void onResponse(Call<MarketReviewEndPoint> call, Response<MarketReviewEndPoint> response) {


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


                                String imagepath = PrefServiceConfig.getServiceURL_SDS(getContext()) + "/api/v1/User/Image/five_hundred_"
                                        + member.getProfileImagePath() + ".jpg";


                                Drawable placeholder = VectorDrawableCompat
                                        .create(getResources(),
                                                R.drawable.ic_nature_people_white_48px,null);


                                Picasso.get()
                                        .load(imagepath)
                                        .placeholder(placeholder)
                                        .into(member_profile_image);


                            } else if (response.body().getItemCount() == 0) {
                                edit_review_text.setText("Rate this market !");
                                edit_review_block.setVisibility(View.GONE);
                                user_review_ratings_block.setVisibility(View.VISIBLE);

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<MarketReviewEndPoint> call, Throwable t) {

                        if(isDestroyed)
                        {
                            return;
                        }


                        //                        showToastMessage("Network Request Failed. Check your internet connection !");
                    }
                });


            }

            // check market ratings count
            // If ratings count is 0 then set message : Be the first to review


            // If ratings count is >0 then
            // check if user has written the review or not
            // if Yes
            // Write messsage : Edit your review and rating
            // If NO
            // Write message : Rate and Review this market

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
