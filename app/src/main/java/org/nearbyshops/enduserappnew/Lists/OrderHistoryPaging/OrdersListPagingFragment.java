package org.nearbyshops.enduserappnew.Lists.OrderHistoryPaging;

import android.app.Application;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduserappnew.API.OrderService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.OrderDetail;
import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.PrefOrderDetail;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.SlidingLayerSort.SlidingLayerSortOrders;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersListPagingFragment extends Fragment implements ViewHolderOrder.ListItemClick, SwipeRefreshLayout.OnRefreshListener,
        NotifySort, NotifySearch, RefreshFragment {






    public static final String TAG_SLIDING_LAYER = "sliding_layer_orders";
    public static final String IS_FILTER_BY_SHOP = "IS_FILTER_BY_SHOP";

    @Inject
    OrderService orderService;

    private RecyclerView recyclerView;
    private AdapterPaged adapter;


    public List<Object> dataset = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;





    private ViewModelOrders viewModelOrders;



    private boolean isDestroyed;


    @BindView(R.id.slidingLayer)
    SlidingLayer slidingLayer;
    @BindView(R.id.shop_count_indicator) TextView orderCountIndicator;



    @BindView(R.id.empty_screen) LinearLayout emptyScreen;
    @BindView(R.id.service_name) TextView serviceName;

    public OrdersListPagingFragment() {

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





//        viewModelOrders = ViewModelProviders.of(this).get(ViewModelOrders.class);
        viewModelOrders = new ViewModelOrders(MyApplication.application);




        if(PrefGeneral.getMultiMarketMode(getActivity()) && PrefServiceConfig.getServiceName(getActivity())!=null)
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

        adapter = new AdapterPaged(getActivity(),this);

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



        viewModelOrders.getArticleLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<Object>>() {
            @Override
            public void onChanged(PagedList<Object> objects) {

                adapter.submitList(objects);


                swipeContainer.setRefreshing(false);
            }
        });

    }





    @Override
    public void onRefresh() {


        viewModelOrders.refresh();
    }







//    private void makeRefreshNetworkCall()
//    {
//        swipeContainer.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeContainer.setRefreshing(true);
//
//                if(!isVisible())
//                {
//                    return;
//                }
//
//
//
//                onRefresh();
//            }
//        });
//    }


    private void makeRefreshNetworkCall() {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {


                    onRefresh();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }
            }
        });

    }






    public static OrdersListPagingFragment newInstance(boolean filterOrdersByUser, boolean filterOrdersByShop, boolean filterOrdersByDeliveryID) {
        OrdersListPagingFragment fragment = new OrdersListPagingFragment();
        Bundle args = new Bundle();

        args.putBoolean("filter_orders_by_user",filterOrdersByUser);
        args.putBoolean("filter_orders_by_shop",filterOrdersByShop);
        args.putBoolean("filter_orders_by_delivery",filterOrdersByDeliveryID);

        fragment.setArguments(args);
        return fragment;
    }









    @OnClick(R.id.button_try_again)
    void tryAgainClick()
    {
        makeRefreshNetworkCall();
    }






    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }








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

        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }



    @Override
    public void refreshFragment() {
        makeRefreshNetworkCall();
    }

}
