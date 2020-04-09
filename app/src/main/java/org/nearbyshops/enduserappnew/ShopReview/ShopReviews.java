package org.nearbyshops.enduserappnew.ShopReview;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduserappnew.API.ShopReviewService;
import org.nearbyshops.enduserappnew.API.ShopReviewThanksService;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopReviewEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopReviewThanksEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.enduserappnew.Model.ModelReviewShop.ShopReviewThanks;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopReview.Interfaces.NotifyLoginByAdapter;
import org.nearbyshops.enduserappnew.ShopReview.SlidingLayerSort.SlidingLayerSortReview;
import org.nearbyshops.enduserappnew.ShopReview.SlidingLayerSort.UtilitySortShopReview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopReviews extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener , NotifySort,
        NotifyLoginByAdapter, NotifyAboutLogin {


//    @State
    ArrayList<ShopReview> dataset = new ArrayList<>();

//    @State
    ArrayList<ShopReviewThanks> datasetThanks = new ArrayList<>();

    Map<Integer, ShopReviewThanks> thanksMap = new HashMap<>();


    @BindView(R.id.recyclerView)
    RecyclerView reviewsList;

    ShopReviewAdapter adapter;

    GridLayoutManager layoutManager;

    @Inject
    ShopReviewService shopReviewService;

    @Inject
    ShopReviewThanksService thanksService;



//    @State
    Shop shop;

    boolean activityStopped = false;



    public static final String SHOP_INTENT_KEY = "shop_intent_key";


    // scroll variables
    private int limit = 30;
    int offset = 0;
    int item_count = 0;

    @BindView(R.id.slidingLayer)
    SlidingLayer slidingLayer;



//    HorizontalBarChart chart;





//    Unbinder unbinder;

    public ShopReviews() {

        // Inject the dependencies using Dependency Injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_reviews);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        chart = (HorizontalBarChart) findViewById(R.id.chart);


//        shop = getIntent().getParcelableExtra(SHOP_INTENT_KEY);


        String shopJson = getIntent().getStringExtra(SHOP_INTENT_KEY);
        shop = UtilityFunctions.provideGson().fromJson(shopJson, Shop.class);



        if(shop !=null)
        {
            getSupportActionBar().setTitle(shop.getShopName());
        }




        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(savedInstanceState==null)
        {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                    dataset.clear();
                    makeNetworkCall();
                }
            });


            makeNetworkCallThanks();

//            setStatFragment();
        }
        else
        {
            onRestoreInstanceState(savedInstanceState);
        }




        setupRecyclerView();
        setupSwipeContainer();
        setupSlidingLayer();


        // setup chart

//        setupChart();


    }





//
//
//    ShopReviewStats fragmentStats = new ShopReviewStats();
//
//    void setStatFragment()
//    {
//
//        Bundle args = new Bundle();
//        args.putParcelable("shopExtra",shop);
//        fragmentStats.setArguments(args);
//
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.review_stat_fragment,fragmentStats)
//                .commit();
//    }
//




    void setupChart()
    {



//        chart.setDescription("Shop Review Stats");


/*
        List<BarEntry> entries = new ArrayList<BarEntry>();

        entries.add(new BarEntry(1,909));
        entries.add(new BarEntry(2,50));
        entries.add(new BarEntry(3,342));
        entries.add(new BarEntry(4,130));
        entries.add(new BarEntry(5,250));


        BarDataSet dataSet = new BarDataSet(entries,"Ratings");

//        dataSet.setColors(new int[]{R.color.gplus_color_2,R.color.buttonColorDark,R.color.colorAccent,R.color.orangeDark,R.color.darkGreen},this);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
//        chart.setData(barData);


        XAxis axis = new XAxis();
        axis.setDrawGridLines(false);

*/

/*
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setDrawValueAboveBar(true);


        chart.getXAxis().setDrawGridLines(false);
        XAxis x_axis = chart.getXAxis();
        x_axis.setDrawGridLines(false);
*/



/*
        chart.getXAxis().setDrawGridLines(false);
//        chart.getXAxis().mAxisRange = 1;

        chart.getXAxis().setGranularity(1f);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);


        chart.invalidate();
*/

    }





    void setupRecyclerView()
    {

        adapter = new ShopReviewAdapter(dataset,thanksMap,this);
        reviewsList.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this,1);
        reviewsList.setLayoutManager(layoutManager);

        reviewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition() == dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall();
                    }

                }

            }
        });



        reviewsList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
//        reviewsList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));

/*
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutManager.setSpanCount(metrics.widthPixels/350);
*/

    }



    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(10);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.slidinglayerfragment,new SlidingLayerSortReview())
                    .commit();

        }

    }



    @OnClick({R.id.icon_sort, R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }






    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }






    private void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityStopped = true;
    }

    @Override
    public void onRefresh() {

        dataset.clear();
        offset = 0 ; // reset the offset
        makeNetworkCall();

        makeNetworkCallThanks();

//        Log.d("applog","refreshed");
    }



    void onRefreshSwipeIndicator()
    {


        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                onRefresh();

            }
        });
    }






    private void makeNetworkCall() {



        String current_sort = "";

        current_sort = UtilitySortShopReview.getSort(this)
                + " " + UtilitySortShopReview.getAscending(this);


        Call<ShopReviewEndPoint> call = shopReviewService.getReviews(shop.getShopID(),null,
                true,current_sort,limit,offset,false);


        call.enqueue(new Callback<ShopReviewEndPoint>() {

            @Override
            public void onResponse(Call<ShopReviewEndPoint> call, Response<ShopReviewEndPoint> response) {

                if(activityStopped)
                {
                    return;
                }

                if(response.body()!=null && response.body().getResults()!=null)
                {
                    item_count = response.body().getItemCount();
                    dataset.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();

                }

                stopRefreshing();

            }

            @Override
            public void onFailure(Call<ShopReviewEndPoint> call, Throwable t) {

                if(activityStopped)
                {
                    return;
                }

                showToastMessage("Network Not available !");

                stopRefreshing();

            }
        });
    }



    private void makeNetworkCallThanks()
    {

        User endUser = PrefLogin.getUser(this);


        if(endUser==null)
        {
            // end user not logged in
            return;
        }

        Call<ShopReviewThanksEndpoint> endpointCall = thanksService.getShopReviewThanks(null,endUser.getUserID(),
                shop.getShopID(),null,100,0,null);




        endpointCall.enqueue(new Callback<ShopReviewThanksEndpoint>() {
            @Override
            public void onResponse(Call<ShopReviewThanksEndpoint> call, Response<ShopReviewThanksEndpoint> response) {


                if(activityStopped)
                {
                    return;
                }

                if(response.body()!=null)
                {
                    datasetThanks.clear();
                    datasetThanks.addAll(response.body().getResults());


                    for(ShopReviewThanks thanks: datasetThanks)
                    {
                        thanksMap.put(thanks.getShopReviewID(),thanks);
                    }

                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ShopReviewThanksEndpoint> call, Throwable t) {


                if(activityStopped)
                {
                    return;
                }

            }
        });


    }








    @Override
    protected void onStop() {
        super.onStop();

    }

    void stopRefreshing()
    {
        if(swipeContainer!=null)
        {
            swipeContainer.setRefreshing(false);
        }
    }


//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        Icepick.saveInstanceState(this,outState);
////        outState.putParcelableArrayList("dataset",dataset);
//    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        Icepick.restoreInstanceState(this,savedInstanceState);

        thanksMap.clear();

        for(ShopReviewThanks thanks: datasetThanks)
        {
            thanksMap.put(thanks.getShopReviewID(),thanks);
        }

//        adapter.notifyDataSetChanged();



        /*if(savedInstanceState!=null)
        {
            ArrayList<ShopReview> tempCat = savedInstanceState.getParcelableArrayList("dataset");

            dataset.clear();
            dataset.addAll(tempCat);
            adapter.notifyDataSetChanged();
        }*/


    }




    private void showLoginDialog()
    {
//        FragmentManager fm = getSupportFragmentManager();
//        LoginDialog loginDialog = new LoginDialog();
//        loginDialog.show(fm,"serviceUrl");


        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }



    @Override
    public void notifySortChanged() {
        onRefreshSwipeIndicator();
    }


//
//    @Override
//    public void NotifyLogin() {
//        onRefreshSwipeIndicator();
//    }


    @Override
    public void NotifyLoginAdapter() {
        showLoginDialog();
    }



    @Override
    public void loginSuccess() {
        onRefreshSwipeIndicator();
    }





    @Override
    public void loggedOut() {

    }
}
