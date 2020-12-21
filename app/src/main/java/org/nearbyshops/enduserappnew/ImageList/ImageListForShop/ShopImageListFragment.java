package org.nearbyshops.enduserappnew.ImageList.ImageListForShop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

import org.nearbyshops.enduserappnew.API.ShopImageService;
import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShopImage.EditShopImage;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShopImage.EditShopImageFragment;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShopImage.UtilityShopImage;
import org.nearbyshops.enduserappnew.ImageList.ImageListForShop.ViewHolder.ViewHolderShopImage;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelImages.ShopImage;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ImageSlider.ImageSliderForShop.ImageSliderShop;
import org.nearbyshops.enduserappnew.Interfaces.OnFilterChanged;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 14/6/17.
 */

public class ShopImageListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderShopImage.ListItemClick, OnFilterChanged {

    boolean isDestroyed = false;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ConstraintLayout progressBar;




    @Inject
    UserService userService;

    @Inject
    ShopImageService service;

    private GridLayoutManager layoutManager;
    private Adapter listAdapter;

    private List<Object> dataset = new ArrayList<>();
//    List<ShopImage> shopListData;


    // flags
    private boolean clearDataset = false;

//    boolean getRowCountVehicle = false;
//    boolean resetOffsetVehicle = false;


    private int limit = 30;
    private int offset = 0;
    public int item_count = 0;




    @BindView(R.id.empty_screen) LinearLayout emptyScreen;


    private boolean isAdminMode = false;

    @BindView(R.id.fab) FloatingActionButton fab;



//    @BindView(R.id.drivers_count) TextView driversCount;
//    int i = 1;

    public ShopImageListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }








    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_images, container, false);
        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//        toolbar.setTitle("Trip History");
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        checkAdminMode();

        setupSwipeContainer();
        setupRecyclerView();

        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }


//        driversCount.setText("Drivers COunt : " + String.valueOf(++i));

        return rootView;
    }





    private void checkAdminMode()
    {

        isAdminMode = getActivity().getIntent().getBooleanExtra("is_admin_mode",false);

        if(isAdminMode)
        {
            fab.setVisibility(View.VISIBLE);
        }
        else
        {
            fab.setVisibility(View.GONE);
        }
    }








    private void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }





    private void setupRecyclerView()
    {


        listAdapter = new Adapter(dataset,getActivity(),this, isAdminMode);
        recyclerView.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),1, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    if(offset + limit > layoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }


                    // trigger fetch next page

                    if((offset + limit)<= item_count)
                    {
                        offset = offset + limit;

                        getShopImages();
                    }


                }
            }
        });

    }


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





    private void makeRefreshNetworkCall()
    {
        progressBar.setVisibility(View.VISIBLE);

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });
    }




    @Override
    public void onRefresh() {

        clearDataset = true;
//        getRowCountVehicle = true;
//        resetOffsetVehicle = true;

        getShopImages();
    }




    private void getShopImages()
    {

//        if(resetOffsetVehicle)
//        {
//            offset = 0;
//            resetOffsetVehicle = false;
//        }


        if(clearDataset)
        {
            offset=0;
        }



        int shopID = getActivity().getIntent().getIntExtra("shop_id",0);



        Call<ShopImageEndPoint> call = service.getShopImages(
                shopID,
                ShopImage.IMAGE_ORDER,
                limit, offset,
                clearDataset,false
        );



        call.enqueue(new Callback<ShopImageEndPoint>() {
            @Override
            public void onResponse(Call<ShopImageEndPoint> call, Response<ShopImageEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.INVISIBLE);

                if(response.code() == 200 && response.body()!=null) {

                    if (clearDataset) {
                        dataset.clear();
                        clearDataset = false;

                        item_count = response.body().getItemCount();

//                        listAdapter.setItemCount(item_count);

                        if(item_count>0)
                        {
                            dataset.add(new HeaderTitle("Shop Images"));
                        }
                    }


                    if(item_count==0)
                    {
//                        emptyScreen.setVisibility(View.VISIBLE);

                        dataset.add(EmptyScreenDataFullScreen.emptyScreenShopImages());
                    }
                    else
                    {
//                        emptyScreen.setVisibility(View.GONE);
                    }






                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
//                        shopListData = response.body().getResults();
                    }

                    listAdapter.notifyDataSetChanged();
                }

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ShopImageEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.INVISIBLE);

                showToastMessage("Network Connection Failed !");

                swipeContainer.setRefreshing(false);
            }
        });


    }






    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }





    @Override
    public void taxiFiltersChanged() {
        makeRefreshNetworkCall();
    }







    @Override
    public void listItemClick(ShopImage taxiImage, int position) {


        Intent intent = new Intent(getActivity(), ImageSliderShop.class);
        List<Object> list = new ArrayList<>();
        list.addAll(dataset);
        list.remove(0);

        String json = UtilityFunctions.provideGson().toJson(list);
        intent.putExtra("images_list",json);
        intent.putExtra("position",position-1);


        startActivity(intent);
    }





    @Override
    public boolean listItemLongClick(View view, ShopImage tripRequest, int position) {
        return true;
    }




    private void deleteClickMain(final ShopImage shopImage, final int position)
    {


        String filename = " ";
        if(shopImage.getImageFilename()!=null)
        {
            filename = shopImage.getImageFilename();
        }



        Call<ResponseBody> call = service.deleteShopImage(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                shopImage.getShopImageID()
        );




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                if (response.code()==200)
                {
                    showToastMessage("Deleted !");
                    listAdapter.notifyItemRemoved(position);
                    dataset.remove(shopImage);

                }
                else
                {
                    showToastMessage("Failed code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Delete Failed !");

            }
        });


    }


    @Override
    public void deleteClick(ShopImage shopImage, int position) {


        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Confirm Delete Image !")
                .setMessage("Are you sure you want to delete this Image !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteClickMain(shopImage,position);

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }





    @Override
    public void editClick(ShopImage shopImage, int position) {


//        int itemID = getActivity().getIntent().getIntExtra("item_id",0);
        int shopId = getActivity().getIntent().getIntExtra("shop_id",0);

        Intent intent = new Intent(getActivity(), EditShopImage.class);
        intent.putExtra(EditShopImageFragment.EDIT_MODE_INTENT_KEY, EditShopImageFragment.MODE_UPDATE);
        intent.putExtra(EditShopImageFragment.SHOP_ID_INTENT_KEY,shopId);

        UtilityShopImage.saveItemImage(shopImage,getActivity());
        startActivity(intent);
    }





    @OnClick(R.id.fab)
    void fabClick()
    {

        int shopId = getActivity().getIntent().getIntExtra("shop_id",0);

        Intent intent = new Intent(getActivity(), EditShopImage.class);
        intent.putExtra(EditShopImageFragment.EDIT_MODE_INTENT_KEY, EditShopImageFragment.MODE_ADD);
        intent.putExtra(EditShopImageFragment.SHOP_ID_INTENT_KEY,shopId);
        startActivity(intent);
    }

}
