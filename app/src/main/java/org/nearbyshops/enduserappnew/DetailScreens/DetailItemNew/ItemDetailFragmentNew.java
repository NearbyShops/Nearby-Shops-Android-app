package org.nearbyshops.enduserappnew.DetailScreens.DetailItemNew;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ImageList.ImageListForItem.ItemImageList;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ItemImageEndPoint;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelItemDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class ItemDetailFragmentNew extends Fragment implements Target, SwipeRefreshLayout.OnRefreshListener {


    public static final String TAG_JSON_STRING = "item_json_string";


    private ArrayList<Object> dataset = new ArrayList<>();

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private Adapter adapter;


    @BindView(R.id.item_image) ImageView itemImage;

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;


    private Item item;
    private int itemID;
    private ViewModelItemDetail viewModelItemDetail;


    @BindView(R.id.fullscreen_progress_bar)
    ProgressBar fullScreenProgressBar;



    public ItemDetailFragmentNew() {

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
        View rootView = inflater.inflate(R.layout.fragment_item_detail_new, container, false);
        ButterKnife.bind(this, rootView);



        if(getActivity()!=null)
        {

            itemID = getActivity().getIntent().getIntExtra("item_id",0);
        }


        setupRecyclerView();
        setupSwipeContainer();


//        bindViews();



        setupViewModel();


        fullScreenProgressBar.setVisibility(View.VISIBLE);
        viewModelItemDetail.getItemDetails(itemID,true);


//        viewModelItemDetail.checkFavourite(itemID);



        return rootView;
    }






    @OnClick(R.id.item_image)
    void profileImageClick() {
        Intent intent = new Intent(getActivity(), ItemImageList.class);
        intent.putExtra("item_id", itemID);
        startActivity(intent);
    }





    private void setupSwipeContainer() {
        if (swipeContainer != null) {

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





    private void bindViews() {


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






//    @OnClick(R.id.see_reviews)
    void seeAllReviews() {
//        showToastMessage("See all reviews click !");
    }


    private void showToastMessage(String message) {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }




    private void showLogin() {

        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent, 123);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {

            // login success

            viewModelItemDetail.checkFavourite(item.getItemID());
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


            if(getActivity()!=null)
            {
                getActivity().getWindow().setStatusBarColor(vibrantDark);
            }


        }


        if (fab != null && vibrantDark != 0) {

            fab.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));

        }//fab.setBackgroundColor(vibrantDark);


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


    private boolean isFavourite = false;


    @OnClick(R.id.fab)
    void fabClick() {

        if (PrefLogin.getUser(getActivity()) == null) {
            // User Not logged In.
            showToastMessage("Please Login to use this Feature !");
            showLogin();


        } else {

            if (isFavourite) {

                viewModelItemDetail.deleteFavourite(item.getItemID());
            }
            else {

                viewModelItemDetail.insertFavourite(item.getItemID());
            }
        }
    }





    private void setFavouriteIcon(boolean isFavourite) {

        this.isFavourite = isFavourite;


        if (fab == null) {
            return;
        }


        if (isFavourite) {

            fab.setImageResource(R.drawable.ic_favorite_white_24px);
        } else {
            fab.setImageResource(R.drawable.ic_favorite_border_white_24px);
        }
    }




    @Override
    public void onRefresh() {

//        fullScreenProgressBar.setVisibility(View.VISIBLE);
        viewModelItemDetail.getItemDetails(itemID,true);
    }





    void setupViewModel()
    {

        viewModelItemDetail = new ViewModelItemDetail(MyApplication.application);


        viewModelItemDetail.getImageEndpointLive().observe(getViewLifecycleOwner(), new Observer<ItemImageEndPoint>() {
            @Override
            public void onChanged(ItemImageEndPoint itemImageEndPoint) {

                fullScreenProgressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);

                dataset.clear();
                dataset.add(itemImageEndPoint.getItemDetails());
                dataset.add(itemImageEndPoint);
                adapter.notifyDataSetChanged();

                ItemDetailFragmentNew.this.item = itemImageEndPoint.getItemDetails();
                bindViews();



                setFavouriteIcon(itemImageEndPoint.isFavourite());
            }
        });


        viewModelItemDetail.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer event) {


                if (event == ViewModelItemDetail.EVENT_ADD_FAVOURITE_SUCCESS) {

                    setFavouriteIcon(true);
                }
                else if (event == ViewModelItemDetail.EVENT_REMOVE_FAVOURITE_SUCCESS)
                {
                    setFavouriteIcon(false);

                }
                else if(event==ViewModelItemDetail.EVENT_NETWORK_FAILED)
                {
                    fullScreenProgressBar.setVisibility(View.GONE);
                }


            }
        });




        viewModelItemDetail.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                showToastMessage(s);
            }
        });



    }




}


