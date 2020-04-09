package org.nearbyshops.enduserappnew.Lists.OrderHistory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wunderlist.slidinglayer.SlidingLayer;
import okhttp3.ResponseBody;

import org.nearbyshops.enduserappnew.API.OrderService;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.OrderEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.OrderDetail;
import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.PrefOrderDetail;
import org.nearbyshops.enduserappnew.SlidingLayerSort.PreferencesSort.PrefSortOrders;
import org.nearbyshops.enduserappnew.SlidingLayerSort.SlidingLayerSortOrders;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrder;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class OrdersHistoryFragment extends Fragment implements ViewHolderOrder.ListItemClick, SwipeRefreshLayout.OnRefreshListener,
        NotifySort, NotifySearch, RefreshFragment {



    

    public static final String TAG_SLIDING_LAYER = "sliding_layer_orders";
    public static final String IS_FILTER_BY_SHOP = "IS_FILTER_BY_SHOP";

    @Inject
    OrderService orderService;

    private RecyclerView recyclerView;
    private Adapter adapter;


    public List<Object> dataset = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;


    final private int limit = 10;
    private int offset = 0;
    int item_count = 0;


    private boolean isDestroyed;



    @BindView(R.id.slidingLayer) SlidingLayer slidingLayer;
    @BindView(R.id.shop_count_indicator) TextView orderCountIndicator;



    @BindView(R.id.empty_screen) LinearLayout emptyScreen;
    @BindView(R.id.service_name) TextView serviceName;

    public OrdersHistoryFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_orders_new, container, false);
        ButterKnife.bind(this,rootView);


        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeContainer = rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        toolbar.setTitle(getString(R.string.app_name));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);



//





        if(PrefServiceConfig.getServiceName(getActivity())!=null)
        {
            serviceName.setVisibility(View.VISIBLE);
            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
        }
        else
        {
            serviceName.setVisibility(View.GONE);
        }


        setupRecyclerView();
        setupSwipeContainer();

        setupSlidingLayer();


        return rootView;
    }



//    int AUTOCOMPLETE_REQUEST_CODE = 1;
//
//    @OnClick(R.id.toolbar)
//    void toolbarClick()
//    {
//
//        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
//
//// Start the autocomplete intent.
//        Intent intent = new Autocomplete.IntentBuilder(
//                AutocompleteActivityMode.FULLSCREEN, fields)
//                .build(getActivity());
//        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//
//    }




//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//
//
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i(TAG, status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
//    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


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







    private void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null) {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(10);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

//            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            if (getChildFragmentManager().findFragmentByTag(TAG_SLIDING_LAYER)==null)
            {
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.slidinglayerfragment,new SlidingLayerSortOrders(),TAG_SLIDING_LAYER)
                        .commit();
            }

        }
    }









    @OnClick({R.id.icon_sort, R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }






    private void setupRecyclerView()
    {

        adapter = new Adapter(dataset,this,getActivity());

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);



        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(offset + limit > layoutManager.findLastVisibleItemPosition() + 1 - 1)
                {
                    return;
                }



                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1 + 1)
                {
                    // trigger fetch next page

//                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
//                    {
//                        return;
//                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }



//                    previous_position = layoutManager.findLastVisibleItemPosition();

                }

            }
        });
    }



//    int previous_position = -1;



    @Override
    public void onRefresh() {

        offset = 0;
        makeNetworkCall(true);
    }







    private void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                if(!isVisible())
                {
                    return;
                }



                onRefresh();
            }
        });
    }







    public static OrdersHistoryFragment newInstance(boolean filterOrdersByUser, boolean filterOrdersByShop, boolean filterOrdersByDeliveryID) {
        OrdersHistoryFragment fragment = new OrdersHistoryFragment();
        Bundle args = new Bundle();

        args.putBoolean("filter_orders_by_user",filterOrdersByUser);
        args.putBoolean("filter_orders_by_shop",filterOrdersByShop);
        args.putBoolean("filter_orders_by_delivery",filterOrdersByDeliveryID);

        fragment.setArguments(args);
        return fragment;
    }




    private void makeNetworkCall(final boolean clearDataset)
    {

//            Shop currentShop = UtilityShopHome.getShopDetails(getContext());

            User endUser = null;



            if(getActivity()!=null)
            {
                endUser = PrefLogin.getUser(getActivity());
            }


            if(endUser==null)
            {
                showLoginDialog();

                swipeContainer.setRefreshing(false);
                return;
            }


            String current_sort = "";

            if(getActivity()!=null)
            {
                current_sort = PrefSortOrders.getSort(getActivity()) + " " + PrefSortOrders.getAscending(getActivity());
            }




            boolean filterOrdersByUser = getArguments().getBoolean("filter_orders_by_user",false);
            boolean filterOrdersByShop = getArguments().getBoolean("filter_orders_by_shop", false);
            boolean filterOrdersByDelivery = getArguments().getBoolean("filter_orders_by_delivery",false);



            Boolean pickFromShop = null;




            if(getActivity()!=null && PrefSortOrders.getFilterByDeliveryType(getActivity())==SlidingLayerSortOrders.FILTER_BY_PICK_FROM_SHOP)
            {
                pickFromShop=true;
            }
            else if(getActivity()!=null && PrefSortOrders.getFilterByDeliveryType(getActivity())==SlidingLayerSortOrders.FILTER_BY_HOME_DELIVERY)
            {
                pickFromShop=false;
            }



            Boolean ordersPendingStatus = null;

            if(getActivity()!=null && PrefSortOrders.getFilterByOrderStatus(getActivity())==SlidingLayerSortOrders.FILTER_BY_STATUS_PENDING)
            {
                ordersPendingStatus = true;
            }
            else if(getActivity()!=null && PrefSortOrders.getFilterByOrderStatus(getActivity())==SlidingLayerSortOrders.FILTER_BY_STATUS_COMPLETE)
            {
                ordersPendingStatus = false;
            }


            emptyScreen.setVisibility(View.GONE);



            Call<OrderEndPoint> call = orderService.getOrders(
                        PrefLogin.getAuthorizationHeaders(getActivity()),
                        filterOrdersByShop,
                        filterOrdersByUser,
                        null,
                        pickFromShop,
                        null,null,
                    null, null,
                        ordersPendingStatus,searchQuery,
                        current_sort,limit,offset,clearDataset,false);





            call.enqueue(new Callback<OrderEndPoint>() {
                @Override
                public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }


                    if(response.code()==200)
                    {
                        if(response.body()!= null)
                        {


                            if(clearDataset)
                            {
                                dataset.clear();
                                item_count = response.body().getItemCount();
                            }

                            if(response.body().getResults()!=null)
                            {
                                dataset.addAll(response.body().getResults());

                                if(response.body().getResults().size()==0)
                                {
                                    emptyScreen.setVisibility(View.VISIBLE);
                                }
                            }


                            adapter.notifyDataSetChanged();
//                            notifyTitleChanged();



                            orderCountIndicator.setText(dataset.size() + " out of " + item_count + " Orders");
                        }

                    }
                    else
                    {

                        showToastMessage("Failed Code : " + response.code());

                    }




                    if(offset+limit >= item_count)
                    {
                        adapter.setLoadMore(false);
                    }
                    else
                    {
                        adapter.setLoadMore(true);
                    }



                    swipeContainer.setRefreshing(false);
                }




                @Override
                public void onFailure(Call<OrderEndPoint> call, Throwable t) {

                    if(isDestroyed)
                    {
                        return;
                    }


                    emptyScreen.setVisibility(View.VISIBLE);



                    showToastMessage("Network Request failed !");
                    swipeContainer.setRefreshing(false);

                }
            });

    }






    @OnClick(R.id.button_try_again)
    void tryAgainClick()
    {
        makeRefreshNetworkCall();
    }





    @Override
    public void onResume() {
        super.onResume();
//        notifyTitleChanged();
        isDestroyed=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }





    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }



//
//    void notifyTitleChanged()
//    {
//
//        if(getActivity() instanceof NotifyTitleChanged)
//        {
//            ((NotifyTitleChanged)getActivity())
//                    .NotifyTitleChanged(
//                            "Pending (" + String.valueOf(dataset.size())
//                                    + "/" + String.valueOf(item_count) + ")",0);
//
//
//        }
//    }





    // Refresh the Confirmed PlaceholderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }



//    void refreshConfirmedFragment()
//    {
//        Fragment fragment = getActivity().getSupportFragmentManager()
//                .findFragmentByTag(makeFragmentName(R.id.container,1));
//
//        if(fragment instanceof RefreshFragment)
//        {
//            ((RefreshFragment)fragment).refreshFragment();
//        }
//    }






    @Override
    public void notifyOrderSelected(Order order) {
        PrefOrderDetail.saveOrder(order,getActivity());
        getActivity().startActivity(new Intent(getActivity(), OrderDetail.class));
    }





    @Override
    public void notifyCancelOrder(final Order order) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Cancel Order !")
                .setMessage("Are you sure you want to cancel this order !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        cancelOrder(order);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage(" Not Cancelled !");
                    }
                })
                .show();
    }





    private void cancelOrder(Order order) {

        showToastMessage("Cancel Order !");


//        Call<ResponseBody> call = orderService.cancelOrderByShop(order.getOrderID());

        Call<ResponseBody> call = orderService.cancelledByEndUser(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200 )
                {
                    showToastMessage("Successful");
                    makeRefreshNetworkCall();
                }
                else if(response.code() == 304)
                {
                    showToastMessage("Not Cancelled !");
                }
                else
                {
                    showToastMessage("Server Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Check your internet connection !");
            }
        });

    }







    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }





    private String searchQuery = null;

    @Override
    public void search(final String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }





    public static final String TAG_LOGIN_DIALOG = "tag_login_dialog";

    private void showLoginDialog()
    {

//        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG_LOGIN_DIALOG);
//
//        if(getActivity().getSupportFragmentManager().findFragmentByTag(TAG_LOGIN_DIALOG)==null)
//        {
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//            LoginDialog loginDialog = new LoginDialog();
//            loginDialog.show(fm,TAG_LOGIN_DIALOG);
//        }




        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }



    @Override
    public void refreshFragment() {
        makeRefreshNetworkCall();
    }

}
