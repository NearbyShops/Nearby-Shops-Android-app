package org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;

import org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew.Model.ShopAddress;
import org.nearbyshops.whitelabelapp.ImageScreens.ImageList.ImageListForShop.ShopImageList;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelShopDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;




public class ShopDetailFragmentNew extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener ,Target{





    public static final String TAG_JSON_STRING = "shop_json_string";


    private ArrayList<Object> dataset = new ArrayList<>();



    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private Adapter adapter;




    @BindView(R.id.shop_profile_photo) ImageView shopProfilePhoto;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;



    private Shop shop;
    private int shopID;
    private ViewModelShopDetail viewModelShopDetail;




    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.fullscreen_progress_bar)
    ProgressBar fullScreenProgressBar;



    private ProgressDialog progressDialog;










    public ShopDetailFragmentNew() {
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
        View rootView = inflater.inflate(R.layout.fragment_shop_detail_new, container, false);
        ButterKnife.bind(this,rootView);


        setupSwipeContainer();



        if(getActivity()!=null)
        {
            shopID = getActivity().getIntent().getIntExtra("shop_id",0);
        }



        bindViews();


        setupViewModel();
        setupRecyclerView();


        fullScreenProgressBar.setVisibility(View.VISIBLE);
        viewModelShopDetail.getShopImages(shopID,true);
        makeRefreshNetworkCall();


        return rootView;
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



    private void setupRecyclerView() {

        if (recyclerView == null) {
            return;
        }


        adapter = new Adapter(dataset, getActivity(), this);
        recyclerView.setAdapter(adapter);


        final LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayoutManager);


//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                linearlayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
    }





    @Override
    public void onRefresh() {

//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Please wait ... getting Shop Details !");
//        progressDialog.show();



        viewModelShopDetail.getShopImages(shopID,true);
    }





    private void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                onRefresh();
            }
        });
    }









    private void bindViews()
    {
        if(shop==null)
        {
            return;
        }

        toolbar.setTitle(shop.getShopName());

        String imagePath = PrefGeneral.getServerURL(getActivity()) + "/api/v1/Shop/Image/five_hundred_"
                + shop.getLogoImagePath() + ".jpg";



        Picasso.get().load(imagePath)
                .into(shopProfilePhoto);


        Picasso.get()
                .load(imagePath)
                .into((Target) this);

    }









    int vibrant;
    int vibrantDark;

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
//          originalTitle.setTextColor(vibrantSwatch.getTitleTextColor());
        //}


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


            if(getActivity()!=null)
            {
//                getActivity().getWindow().setStatusBarColor(vibrantDark);
            }


        }


        if (fab != null && vibrantDark != 0) {

//            fab.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));

        }//fab.setBackgroundColor(vibrantDark);


//        bottomBar.setBackgroundColor(vibrant);



//        itemName.setBackgroundColor(vibrantDark);

//        itemName.setTextColor(vibrantDark);


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
                viewModelShopDetail.deleteFavourite(shop.getShopID());
            }
            else
            {
                viewModelShopDetail.insertFavourite(shop.getShopID());
            }
        }
    }





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
        }
    }







    void setupViewModel()
    {

        viewModelShopDetail = new ViewModelShopDetail(MyApplication.application);

        viewModelShopDetail.getEndpointLive().observe(getViewLifecycleOwner(), new Observer<ShopImageEndPoint>() {
            @Override
            public void onChanged(ShopImageEndPoint endpoint) {

                fullScreenProgressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);

                dataset.clear();
                dataset.add(endpoint.getShopDetails());
                dataset.add(endpoint);
                dataset.add(new ShopAddress(endpoint.getShopDetails()));

//                if(endpoint.getShopReview()!=null)
//                {
//                    dataset.add(endpoint.getShopReview());
//                }
//                else
//                {
//                    dataset.add(new ShopReview());
//                }

                adapter.notifyDataSetChanged();



//                Log.d("shop_detail",UtilityFunctions.provideGson().toJson(endpoint));


                ShopDetailFragmentNew.this.shop = endpoint.getShopDetails();
                bindViews();



                setFavouriteIcon(endpoint.isFavourite());
            }
        });



        viewModelShopDetail.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer event) {

                fullScreenProgressBar.setVisibility(View.GONE);


                if (event == ViewModelShopDetail.EVENT_ADD_FAVOURITE_SUCCESS) {

                    setFavouriteIcon(true);
                }
                else if (event == ViewModelShopDetail.EVENT_REMOVE_FAVOURITE_SUCCESS)
                {
                    setFavouriteIcon(false);

                }
                else if(event==ViewModelShopDetail.EVENT_NETWORK_FAILED)
                {
                    fullScreenProgressBar.setVisibility(View.GONE);
                }


            }
        });




        viewModelShopDetail.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                fullScreenProgressBar.setVisibility(View.GONE);

                showToastMessage(s);
            }
        });



    }




}
