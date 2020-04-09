//package org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.Backups;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import org.nearbyshops.enduserappnew.API.OrderItemService;
//import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
//import org.nearbyshops.enduserappnew.DetailScreens.DetailItem.ItemDetail;
//import org.nearbyshops.enduserappnew.DetailScreens.DetailItem.ItemDetailFragment;
//import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.Adapter;
//import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.PrefOrderDetail;
//import org.nearbyshops.enduserappnew.DetailScreens.DetailShop.ShopDetail;
//import org.nearbyshops.enduserappnew.DetailScreens.DetailShop.ShopDetailFragment;
//import org.nearbyshops.enduserappnew.Model.Item;
//import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
//import org.nearbyshops.enduserappnew.Model.ModelEndPoints.OrderItemEndPoint;
//import org.nearbyshops.enduserappnew.Model.Shop;
//import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
//import org.nearbyshops.enduserappnew.R;
//import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
//import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopTypeTwo;
//import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
//import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderItem;
//import ViewHolderOrderWithBillDeprecated;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by sumeet on 15/11/16.
// */
//
//public class FragmentOrderDetail25Feb20 extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
//        ViewHolderOrderWithBillDeprecated.ListItemClick, ViewHolderOrderItem.ListItemClick, ViewHolderShopTypeTwo.ListItemClick
//        {
//
//    private Order order;
//
//    @Inject
//    OrderItemService orderItemService;
//
//    private RecyclerView recyclerView;
//    private Adapter adapter;
//
//    public List<Object> dataset = new ArrayList<>();
//
//    private GridLayoutManager layoutManager;
//    private SwipeRefreshLayout swipeContainer;
//
//
//
//    final private int limit = 5;
//    private int offset = 0;
//    private int item_count = 0;
//
//
//    private boolean isDestroyed;
//
//
//    @BindView(R.id.service_name)
//    TextView serviceName;
//
//
//
//
//
//    public FragmentOrderDetail25Feb20() {
//        DaggerComponentBuilder.getInstance()
//                .getNetComponent()
//                .Inject(this);
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//
//        setRetainInstance(true);
//        View rootView = inflater.inflate(R.layout.fragment_order_detail_screen, container, false);
//
//
//        ButterKnife.bind(this,rootView);
//
//
//        recyclerView = rootView.findViewById(R.id.recyclerView);
//        swipeContainer = rootView.findViewById(R.id.swipeContainer);
//
//        order = PrefOrderDetail.getOrder(getActivity());
//
//
//
//        if(savedInstanceState==null)
//        {
//            makeRefreshNetworkCall();
//        }
//
//
//
//
//        serviceName.setVisibility(View.VISIBLE);
//        serviceName.setText("Order ID : " + order.getOrderID());
//
//
//
//        setupRecyclerView();
//        setupSwipeContainer();
//
//
//        return rootView;
//    }
//
//
//
//
//    private void setupSwipeContainer()
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
//
//
//
//
//    private void setupRecyclerView()
//    {
//
//        adapter = new Adapter(dataset,getActivity(),this);
//        layoutManager = new GridLayoutManager(getActivity(),1);
//
//
///*
//        // add order to the dataset
//        if(!dataset.contains(order))
//        {
//            dataset.add(0,order);
//            adapter.notifyItemChanged(0);
//        }*/
//
//
////        layoutManager.setSpanCount(metrics.widthPixels/400);
//
//
//
//
//
////        if(spanCount==0){
////            spanCount = 1;
////        }
//
////        layoutManager.setSpanCount(spanCount);
//
////        final int finalSpanCount = spanCount;
//
//        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//
//                System.out.println("Position : " + position);
//
//
////                if(adapter.getItemViewType(position)==Adapter.TAG_VIEW_HOLDER_ORDER_ITEM)
////                {
////                    return 1;
////                }
////                else if(adapter.getItemViewType(position)==Adapter.TAG_VIEW_HOLDER_ORDER)
////                {
////
//
//
////                    DisplayMetrics metrics = new DisplayMetrics();
////                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
////                    int spanCount = (int) (metrics.widthPixels / (230 * metrics.density));
////
////                    if (spanCount == 0) {
////                        return 1;
////                    } else {
////                        return spanCount;
////                    }
//
////                    return 2;
////                }
//
//
//
//                if (dataset.get(position) instanceof OrderItem) {
//
//                    return 2;
//
//                }
//                else if (dataset.get(position) instanceof Order)
//                {
//
////                    DisplayMetrics metrics = new DisplayMetrics();
////                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
////                    int spanCount = (int) (metrics.widthPixels / (230 * metrics.density));
////
////                    if (spanCount == 0) {
////                        return 1;
////                    } else {
////                        return spanCount;
////                    }
//
//                    return 4;
//                }
//
//                return 4;
//            }
//        });
//*/
//
//
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
////        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
//
//
////
////        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
////            @Override
////            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
////                super.onScrollStateChanged(recyclerView, newState);
////
////                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
////                {
////                    // trigger fetch next page
////
//////                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
//////                    {
//////                        return;
//////                    }
////
////                    if(offset + limit > layoutManager.findLastVisibleItemPosition()+1-1)
////                    {
////                        return;
////                    }
////
////
////                    if((offset+limit)<=item_count)
////                    {
////                        offset = offset + limit;
////                        makeNetworkCall(false);
////                    }
////
//////                    previous_position = layoutManager.findLastVisibleItemPosition();
////
////                }
////
////            }
////        });
//    }
//
//
//
////    int previous_position = -1;
//
//
//
//    @Override
//    public void onRefresh() {
//
//        offset = 0;
//        makeNetworkCall(true);
////        makeNetworkCallShop();
//
//
//        System.out.println("Dataset Size onRefresh() : " + dataset.size());
//        showLog("Dataset Size onRefresh() : " + dataset.size());
//
//    }
//
//
//
//
//
//    private void makeRefreshNetworkCall()
//    {
//
//        swipeContainer.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeContainer.setRefreshing(true);
//
//                onRefresh();
//            }
//        });
//
//    }
//
//
//
//
//
//
//
//
//    private void makeNetworkCall(final boolean clearDataset)
//    {
//
////        Shop currentShop = PrefShopHome.getShopDetails(getContext());
//
//        Call<OrderItemEndPoint> call = orderItemService.getOrderItem(
//                PrefLogin.getAuthorizationHeaders(getActivity()),
//                order.getOrderID(),
//                null,
//                order.getShopID()
//                ,null,null,
//                null,null);
//
//
//
//
//
//        call.enqueue(new Callback<OrderItemEndPoint>() {
//            @Override
//            public void onResponse(Call<OrderItemEndPoint> call, Response<OrderItemEndPoint> response) {
//
//                if(isDestroyed)
//                {
//                    return;
//                }
//
//
//                if(response.code()==200)
//                {
//
//                    if(response.body()!= null)
//                    {
////                        item_count = response.body().getItemCount();
//
//                            dataset.clear();
//                            dataset.add(0,order);
//
////                            order.setShop(response.body().getShopDetails());
//
//                            dataset.add(response.body().getShopDetails());
//                            dataset.add(new HeaderTitle("Items in this Order"));
//                        }
//
//
//
//
//                        dataset.addAll(response.body().getResults());
//                        adapter.notifyDataSetChanged();
//
//
//                    }
//
//                }
//                else
//                {
//                    showToastMessage("Failed : Code " + response.code());
//                }
//
//
//
//
//                swipeContainer.setRefreshing(false);
//
//            }
//
//            @Override
//            public void onFailure(Call<OrderItemEndPoint> call, Throwable t) {
//                if(isDestroyed)
//                {
//                    return;
//                }
//
//                showToastMessage("Network Request failed !");
//                swipeContainer.setRefreshing(false);
//
//            }
//        });
//
//    }
//
//
//
//
//
//
//
//    private void showToastMessage(String message)
//    {
//        if(getActivity()!=null)
//        {
//            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        isDestroyed=true;
//    }
//
//
//
//
//
//    private void showLog(String message)
//    {
//        Log.d("order_detail",message);
//    }
//
//
//
//    @Override
//    public void listItemClick(Order order, int position) {
//
//    }
//
//    @Override
//    public boolean listItemLongClick(View view, Order order, int position) {
//        return false;
//    }
//
//
//
//    @Override
//    public void listItemClick(Item item, int position) {
//
//        Intent intent = new Intent(getActivity(), ItemDetail.class);
////        intent.putExtra(ItemDetail.ITEM_DETAIL_INTENT_KEY,item);
//        String itemJson = UtilityFunctions.provideGson().toJson(item);
//        intent.putExtra(ItemDetailFragment.TAG_JSON_STRING,itemJson);
//
//        startActivity(intent);
//    }
//
//
//
//
//
//
//    @Override
//    public void listItemClick(Shop shop, int position) {
//
//        Intent intent = new Intent(getActivity(), ShopDetail.class);
//
//        String shopJson = UtilityFunctions.provideGson().toJson(shop);
//        intent.putExtra(ShopDetailFragment.TAG_JSON_STRING,shopJson);
//
//        startActivity(intent);
//    }
//
//
//
//}
