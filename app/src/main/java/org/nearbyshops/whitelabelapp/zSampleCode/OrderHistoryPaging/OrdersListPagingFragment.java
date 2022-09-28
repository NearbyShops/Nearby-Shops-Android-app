package org.nearbyshops.whitelabelapp.zSampleCode.OrderHistoryPaging;

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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderService;
import org.nearbyshops.whitelabelapp.zSampleCode.OrderHistoryPaging.ViewModel.ViewModelOrdersPaged;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.CartAndOrder.DetailOrder.OrderDetail;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Interfaces.RefreshFragment;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.SlidingLayerSortOrders;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderOrder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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





    private ViewModelOrdersPaged viewModelOrdersPaged;



    private boolean isDestroyed;



    @BindView(R.id.slidingLayer) SlidingLayer slidingLayer;
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
        View rootView = inflater.inflate(R.layout.fragment_orders_list_backup, container, false);
        ButterKnife.bind(this,rootView);


        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeContainer = rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
//            makeRefreshNetworkCall();


        }


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        toolbar.setTitle(getString(R.string.app_name));
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);





//        viewModelOrders = ViewModelProviders.of(this).get(ViewModelOrders.class);

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);


                viewModelOrdersPaged = new ViewModelOrdersPaged(MyApplication.application);
                setupRecyclerView();
            }
        });


        serviceName.setVisibility(View.GONE);

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



        viewModelOrdersPaged.getArticleLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<Object>>() {
            @Override
            public void onChanged(PagedList<Object> objects) {

                adapter.submitList(objects);

//                                    swipeContainer.setRefreshing(false);
            }
        });



//        adapter.addLoadStateListener(new PagedList.LoadStateListener() {
//            @Override
//            public void onLoadStateChanged(@NonNull PagedList.LoadType type, @NonNull PagedList.LoadState state, @Nullable Throwable error) {
//
////                System.out.println("Load Type : State " + type + " | " + state);
//
//                if(state.equals(PagedList.LoadState.DONE))
//                {
//                    swipeContainer.setRefreshing(false);
//                }
//            }
//        });







        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

//                System.out.println("Position Start " + positionStart + " | Item Count " + itemCount);

                if(itemCount>0)
                {
                    swipeContainer.setRefreshing(false);
                }
            }



        });









    }





    @Override
    public void onRefresh() {


        viewModelOrdersPaged.refresh();
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
//        PrefOrderDetail.saveOrder(order,getActivity());
//        getActivity().startActivity(new Intent(getActivity(), OrderDetail.class));

        startActivity(OrderDetail.getLaunchIntent(order.getOrderID(),getActivity()));
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
