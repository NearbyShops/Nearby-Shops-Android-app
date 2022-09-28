package org.nearbyshops.whitelabelapp.ImageScreens.ImageList.ImageListForItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.nearbyshops.whitelabelapp.API.ItemImageService;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderImages.ViewHolderItemImage;
import org.nearbyshops.whitelabelapp.ImageScreens.ImageSlider.ImageSliderForItem.ImageSlider;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ItemImageEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelImages.ItemImage;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.OnFilterChanged;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 14/6/17.
 */


public class ItemImageListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderItemImage.ListItemClick, OnFilterChanged {




    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ConstraintLayout progressBar;




    @Inject
    ItemImageService service;

    private GridLayoutManager layoutManager;
    private Adapter listAdapter;
    private List<Object> dataset = new ArrayList<>();




    private boolean isDestroyed = false;

    // flags
    private boolean clearDataset = false;



    private int limit = 30;
    private int offset = 0;
    public int item_count = 0;





    public ItemImageListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_item_images, container, false);
        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//        toolbar.setTitle("Trip History");
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        setupSwipeContainer();
        setupRecyclerView();

        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }


//        driversCount.setText("Drivers COunt : " + String.valueOf(++i));

        return rootView;
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

        listAdapter = new Adapter(dataset,getActivity(),this);
        recyclerView.setAdapter(listAdapter);


        layoutManager = new GridLayoutManager(getActivity(),1, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


//        final DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


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

                        getItemImages();
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
        getItemImages();
    }






    private void getItemImages()
    {


        if(clearDataset)
        {
            offset=0;
        }


        int itemID = getActivity().getIntent().getIntExtra("item_id",0);




        Call<ItemImageEndPoint> call = service.getItemImagesForEnduser(
                itemID,
                ItemImage.IMAGE_ORDER,
                limit, offset
        );




        call.enqueue(new Callback<ItemImageEndPoint>() {

            @Override
            public void onResponse(Call<ItemImageEndPoint> call, Response<ItemImageEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.INVISIBLE);

                if(response.code() == 200 && response.body()!=null) {

                    if (clearDataset) {

                        clearDataset = false;
                        dataset.clear();

                        if(response.body().getResults().size()>0 || response.body().getItemDetails()!=null)
                        {
                            dataset.add(new ViewHolderHeader.HeaderTitle("Images"));
                        }
                    }



                    itemImageResponse = response.body();



                    if(response.body().getResults().size()==0 && response.body().getItemDetails()==null)
                    {
//                        emptyScreen.setVisibility(View.VISIBLE);
                        dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenItemImages());
                    }


                    if(response.body().getItemDetails()!=null)
                    {
                        dataset.add(response.body().getItemDetails());
                    }


                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
                    }

                    listAdapter.notifyDataSetChanged();
                }

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ItemImageEndPoint> call, Throwable t) {


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







    ItemImageEndPoint itemImageResponse;


    @Override
    public void listItemClick(ItemImage taxiImage, int position) {


        Intent intent = new Intent(getActivity(), ImageSlider.class);

//        List<Object> list = new ArrayList<>();
//        list.addAll(dataset);
//        list.remove(0);

//        String json = UtilityFunctions.provideGson().toJson(list);
//        intent.putExtra("images_list",json);
//        intent.putExtra("position",position-1);



        if(itemImageResponse!=null)
        {
            String json = UtilityFunctions.provideGson().toJson(itemImageResponse);
            intent.putExtra("images_list",json);
            intent.putExtra("position",position-1);
            startActivity(intent);
        }

    }







    @Override
    public boolean listItemLongClick(View view, ItemImage tripRequest, int position) {
        return true;
    }


}
