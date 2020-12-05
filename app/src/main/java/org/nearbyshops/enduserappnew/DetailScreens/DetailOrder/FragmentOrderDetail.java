package org.nearbyshops.enduserappnew.DetailScreens.DetailOrder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.enduserappnew.API.OrderItemService;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders.ViewHolderOrderItem;
import org.nearbyshops.enduserappnew.DetailScreens.DetailItemNew.ItemDetail;
import org.nearbyshops.enduserappnew.DetailScreens.DetailShopNew.ShopDetail;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.OrderItemEndPoint;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.DetailScreens.DetailItem.ItemDetailFragment;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopSmall;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 15/11/16.
 */




public class FragmentOrderDetail extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderOrderItem.ListItemClick, ViewHolderShopSmall.ListItemClick
{






    private Order order;

    @Inject
    OrderItemService orderItemService;

    private RecyclerView recyclerView;
    private Adapter adapter;

    public List<Object> dataset = new ArrayList<>();


    private SwipeRefreshLayout swipeContainer;


    int orderID;




    private boolean isDestroyed;


    @BindView(R.id.service_name)
    TextView serviceName;





    public FragmentOrderDetail() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_order_detail_screen, container, false);


        ButterKnife.bind(this,rootView);


        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeContainer = rootView.findViewById(R.id.swipeContainer);

//        order = PrefOrderDetail.getOrder(getActivity());



        if(getActivity()!=null)
        {
            orderID = getActivity().getIntent().getIntExtra("order_id",0);
        }



        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();


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



    void bindOrder()
    {
        serviceName.setVisibility(View.VISIBLE);
        serviceName.setText("Order ID : " + order.getOrderID());
    }



    private void setupRecyclerView()
    {

        adapter = new Adapter(dataset,getActivity(),this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }



    @Override
    public void onRefresh() {

        makeNetworkCall();
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








    private void makeNetworkCall()
    {

        Call<OrderItemEndPoint> call = orderItemService.getOrderItem(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                orderID,
                null,
                PrefLocation.getLatitude(getActivity()),PrefLocation.getLongitude(getActivity()),
                null,null,
                null,null);





        call.enqueue(new Callback<OrderItemEndPoint>() {
            @Override
            public void onResponse(Call<OrderItemEndPoint> call, Response<OrderItemEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200 && response.body()!=null)
                {

                    dataset.clear();

                    // fetch extra order details
                    order = response.body().getOrderDetails();


                    bindOrder();



//                    if(extraDetails!=null)
//                    {
//                        order.setItemTotal(extraDetails.getItemTotal());
//                        order.setAppServiceCharge(extraDetails.getAppServiceCharge());
//                        order.setDeliveryCharges(extraDetails.getDeliveryCharges());
//                        order.setSavingsOverMRP(extraDetails.getSavingsOverMRP());
//                    }



                    dataset.add(order);



                    Shop shopProfile = response.body().getShopDetails();


                    if(shopProfile!=null)
                    {
                        dataset.add(shopProfile);
                    }


                    dataset.add(new HeaderTitle("Items in this Order : " + String.valueOf(order.getItemCount())));
                    dataset.addAll(response.body().getResults());





                    adapter.notifyDataSetChanged();


                }
                else
                {
                    showToastMessage("Failed : Code " + response.code());
                }




                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<OrderItemEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Request failed !");


                swipeContainer.setRefreshing(false);

            }
        });

    }







    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }





    private void showLog(String message)
    {
        Log.d("order_detail",message);
    }




    @Override
    public void listItemClick(Item item, int position) {

        Intent intent = new Intent(getActivity(), ItemDetail.class);

        intent.putExtra("item_id",item.getItemID());
        String itemJson = UtilityFunctions.provideGson().toJson(item);
        intent.putExtra(ItemDetailFragment.TAG_JSON_STRING,itemJson);

        startActivity(intent);
    }






        @Override
        public void listItemClick(Shop shop, int position) {

            Intent intent = new Intent(getActivity(), ShopDetail.class);
            intent.putExtra("shop_id",shop.getShopID());

//            String shopJson = UtilityFunctions.provideGson().toJson(shop);
//            intent.putExtra(ShopDetailFragment.TAG_JSON_STRING,shopJson);
            startActivity(intent);
        }



        @Override
        public void editClick(Shop shop, int position) {

            // ignore because its not required here
        }


}
