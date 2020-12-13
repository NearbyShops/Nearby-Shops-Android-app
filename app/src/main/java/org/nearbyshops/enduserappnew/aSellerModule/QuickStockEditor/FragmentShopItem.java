package org.nearbyshops.enduserappnew.aSellerModule.QuickStockEditor;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.enduserappnew.API.ShopItemService;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.aSellerModule.ViewHolders.ViewHolderShopItemSeller;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;

import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 13/6/16.
 */


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentShopItem extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderShopItemSeller.ShopItemUpdates {


    @Inject
    ShopItemService shopItemService;
    private RecyclerView recyclerView;
    private Adapter adapter;


    public ArrayList<Object> dataset = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;

    private NotificationReceiverPager notificationReceiverPager;



    public static String ARG_MODE_KEY = "mode_key";

    public static int MODE_OUT_OF_STOCK = 1;
    public static int MODE_LOW_STOCK = 2;
    public static int MODE_RECENTLY_UPDATED = 3;
    public static int MODE_RECENTLY_ADDED = 4;
    public static int MODE_PRICE_NOT_SET = 5;




    private int limit = 10;
    private int offset = 0;
    private int item_count = 0;




    public FragmentShopItem() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }




    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentShopItem newInstance(int mode) {
        FragmentShopItem fragment = new FragmentShopItem();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE_KEY,mode);
        fragment.setArguments(args);
        return fragment;
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quick_stock_editor, container, false);
        setRetainInstance(true);

        swipeContainer = rootView.findViewById(R.id.swipeContainer);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        if(savedInstanceState == null)
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




    private void setupRecyclerView()
    {

        adapter = new Adapter(dataset,getActivity(),this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);

        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(offset + limit > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }

                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }

                }
            }
        });

    }



    @Override
    public void onRefresh() {
        makeNetworkCall(true);
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








    private void makeNetworkCall(final boolean clearDataset) {

        if(clearDataset) {
            offset = 0;
        }





        int mode = getArguments().getInt(ARG_MODE_KEY);


        Shop currentShop = PrefShopAdminHome.getShop(getContext());




        Call<ShopItemEndPoint> call = null;


        if (mode == MODE_OUT_OF_STOCK) {



            call = shopItemService.getShopItemsByShop(null,false,false,
                    currentShop.getShopID(),null,null,null,
                    null,null,
                    true,null,null,null,null,null,
                    "LAST_UPDATE_DATE_TIME",
                    limit,offset,clearDataset,
                    false
            );



        }
        else if (mode == MODE_LOW_STOCK)
        {

            call = shopItemService.getShopItemsByShop(
                    null,false,false,
                    currentShop.getShopID(),null,
                    null,null,null,
                    null,null,
                    null,
                    null,null,
                    null,false,"available_item_quantity",
                    limit,offset,
                    clearDataset, false
            );

        }
        else if (mode == MODE_RECENTLY_ADDED)
        {


            call = shopItemService.getShopItemsByShop(
                    null,false,false,
                    currentShop.getShopID(),null,
                    null,null,null,
                    null,null,
                    null,
                    null,null,
                    null,false,"date_time_added desc",
                    limit,offset,
                    clearDataset, false
            );



        }else if (mode == MODE_RECENTLY_UPDATED)
        {

            call = shopItemService.getShopItemsByShop(
                    null,false,false,
                    currentShop.getShopID(),null,
                    null,null,null,
                    null,null,
                    null,
                    null,null,
                    null,false," last_update_date_time desc ",
                    limit,offset,
                    clearDataset, false
            );


        }
        else if (mode == MODE_PRICE_NOT_SET)
        {

            call = shopItemService.getShopItemsByShop(
                    null,false,false,
                    currentShop.getShopID(),null,
                    null,null,null,
                    null,null,
                    true,
                    null,null,
                    null,false," LAST_UPDATE_DATE_TIME ",
                    limit,offset,
                    clearDataset, false
            );
        }





//        if (mode == MODE_OUT_OF_STOCK) {
//
////            call = shopItemService.getShopItems(currentShop.getShopID(), null, null, true, null);
//
//            call = shopItemService.getShopItemEndpoint(null,false,
//                    currentShop.getShopID(),null,null,null,
//                    null,null,null,null,null,true,null,null,null,null,
//                    "LAST_UPDATE_DATE_TIME",
//                    limit,offset,clearDataset,
//                    false
//            );
//
//
//
//
//
//
//        } else if (mode == MODE_LOW_STOCK)
//        {
//
////            call = shopItemService.getShopItems(currentShop.getShopID(),null,null,null,null,"available_item_quantity",null,null);
//
//            call = shopItemService.getShopItemEndpoint(null,false,
//                    currentShop.getShopID(),null,null,null,null,null,null,null,null,null,
//                    null,null,null,null,"available_item_quantity",
//                    limit,offset,
//                    clearDataset, false
//            );
//
//        }
//        else if (mode == MODE_RECENTLY_ADDED)
//        {
////            call = shopItemService.getShopItems(currentShop.getShopID(),null,null,null,null,"date_time_added desc",null,null);
//
//
//            call = shopItemService.getShopItemEndpoint(null,false,
//                    currentShop.getShopID(),null,null,null,null,null,null,null,null,null,
//                    null,null,null,
//                    null,"date_time_added desc",
//                    limit,offset,
//                    clearDataset, false
//            );
//
//
//        }else if (mode == MODE_RECENTLY_UPDATED)
//        {
////            call = shopItemService.getShopItems(currentShop.getShopID(),null,null,null,null,"last_update_date_time desc",null,null);
//
//            call = shopItemService.getShopItemEndpoint(null,false,
//                    currentShop.getShopID(),null,null,null,null,null,null,null,null,null,
//                    null,null,null,
//                    null,"last_update_date_time desc",
//                    limit,offset,
//                    clearDataset,false
//            );
//        }
//        else if (mode == MODE_PRICE_NOT_SET)
//        {
//            call = shopItemService.getShopItemEndpoint(null,false,
//                    currentShop.getShopID(),null,null,null,
//                    null,null,null,null,null,null,true,null,null,
//                    null,"LAST_UPDATE_DATE_TIME",
//                    limit,offset,
//                    clearDataset, false
//            );
//        }


        if(call == null)
        {
            return;
        }


        call.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {

                if(!isVisible())
                {
                    return;
                }


                if(response.body()!= null)
                {
                    if(clearDataset)
                    {
                        dataset.clear();
                        item_count = response.body().getItemCount();

                        if(item_count==0)
                        {
                            dataset.add(EmptyScreenDataFullScreen.emptyScreenQuickStockEditor());
                        }

                    }




                    dataset.addAll(response.body().getResults());





                    if(offset+limit >= item_count)
                    {
                        adapter.setLoadMore(false);
                    }
                    else
                    {
                        adapter.setLoadMore(true);
                    }

                }



                if(notificationReceiverPager!=null)
                {
                    notificationReceiverPager.OutOfStockChanged();
                }

                notifyTitleChanged();
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                if(!isVisible())
                {
                    return;
                }


//                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);


                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                adapter.notifyDataSetChanged();
            }
        });
    }






    private void showToastMessage(String message)
    {
//        if(getActivity()!=null)
//        {
//            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
//        }


        UtilityFunctions.showToastMessage(getActivity(),message);
    }




    @Override
    public void notifyShopItemUpdated(final ShopItem shopItem) {

//
//        Call<ResponseBody> call = shopItemService.putShopItem(
//                PrefLogin.getAuthorizationHeaders(getActivity()),
//                shopItem
//        );
//
//
//
//
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if(response.code()==200)
//                {
//
//
//                    if(shopItem.getItem()!=null)
//                    {
//                        showToastMessage(shopItem.getItem().getItemName() + " Updated !");
//
//                    }else
//                    {
//                        showToastMessage("Update Successful !");
//                    }
//                }
//                else if(response.code() == 403)
//                {
//                    showToastMessage("Not permitted !");
//                }
//                else if(response.code() == 401)
//                {
//                    showToastMessage("We are not able to identify you !");
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                showToastMessage("Network Request Failed. Try again !");
//
//            }
//        });



    }







    @Override
    public void notifyShopItemRemoved(final ShopItem shopItem) {

        offset = offset - 1;
        item_count = item_count -1;
        notifyTitleChanged();

//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//
//        dialog.setTitle("Confirm Remove Item !")
//                .setMessage("Do you want to remove this item from your shop !")
//                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                            removeShopItem(shopItem);
//
//                    }
//                })
//                .setNegativeButton("No",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        showToastMessage("Cancelled !");
//                    }
//                })
//                .show();

    }


    void removeShopItem(final ShopItem shopItem)
    {

        Call<ResponseBody> responseBodyCall = shopItemService.deleteShopItem(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                shopItem.getShopID(),
                shopItem.getItemID()
        );

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    if(shopItem.getItem()!=null)
                    {
                        showToastMessage(shopItem.getItem().getItemName() + " Removed !");

                    }else
                    {
                        showToastMessage("Successful !");
                    }


                    int removedPosition = dataset.indexOf(shopItem);
                    dataset.remove(shopItem);
                    adapter.notifyItemRemoved(removedPosition);

                    offset = offset - 1;
                    item_count = item_count -1;
                    notifyTitleChanged();
                }
                else if(response.code() == 304) {

                    showToastMessage("Not removed !");

                }
                else if(response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else if(response.code() == 401)
                {
                    showToastMessage("We are not able to identify you !");
                }
                else
                {
                    showToastMessage("Server Error !");
                }


//                makeRefreshNetworkCall();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network request failed !");

            }
        });


    }



    interface NotificationReceiverPager
    {
        void OutOfStockChanged();
    }



    // save and restore instance state



//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Icepick.saveInstanceState(this, outState);
//        outState.putParcelableArrayList("dataset",dataset);
//    }


//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);


//        Icepick.restoreInstanceState(this, savedInstanceState);
//
//        if (savedInstanceState != null) {
//
//            ArrayList<ShopItem> tempList = savedInstanceState.getParcelableArrayList("dataset");
//
//            dataset.clear();
//            dataset.addAll(tempList);
//            adapter.notifyDataSetChanged();
//        }
//
//    }








    private void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChanged)
        {

            int mode = getArguments().getInt(ARG_MODE_KEY);


            if (mode == MODE_OUT_OF_STOCK) {

//            call = shopItemService.getShopItems(currentShop.getShopID(), null, null, true, null);

                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Out of Stock (" + dataset.size()
                                        + "/" + item_count + ")",1);




            } else if (mode == MODE_LOW_STOCK)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Low Stock (" + dataset.size()
                                        + "/" + item_count + ")",0);


            }
            else if (mode == MODE_RECENTLY_ADDED)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Recently Added (" + dataset.size()
                                        + "/" + item_count + ")",3);



            }else if (mode == MODE_RECENTLY_UPDATED)
            {


                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Recently Updated (" + dataset.size()
                                        + "/" + item_count + ")",4);

            }
            else if (mode == MODE_PRICE_NOT_SET)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Price not Set (" + dataset.size()
                                        + "/" + item_count + ")",2);

            }



        }
    }



}
