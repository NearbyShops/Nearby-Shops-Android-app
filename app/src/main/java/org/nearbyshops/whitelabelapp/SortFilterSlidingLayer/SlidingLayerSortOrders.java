package org.nearbyshops.whitelabelapp.SortFilterSlidingLayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.PreferencesSort.PrefSortOrders;
import org.nearbyshops.whitelabelapp.R;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerSortOrders extends Fragment {

    @BindView(R.id.sort_distance) TextView sort_by_distance;
    @BindView(R.id.sort_date_time) TextView sort_by_date_time;
    @BindView(R.id.order_status) TextView sort_by_status;
    @BindView(R.id.pincode) TextView sort_by_pincode;


    @BindView(R.id.sort_ascending) TextView sort_ascending;
    @BindView(R.id.sort_descending) TextView sort_descending;

    String currentSort = SORT_BY_DISTANCE;
    String currentAscending = SORT_DESCENDING;

    int colorSelected = R.color.blueGrey800;
    int colorSelectedAscending = R.color.gplus_color_2;


    public static String SORT_BY_DISTANCE = "distance";
    public static String SORT_BY_DATE_TIME = Order.DATE_TIME_PLACED;
    public static String SORT_BY_STATUS = Order.STATUS_HOME_DELIVERY;
    public static String SORT_BY_PINCODE = DeliveryAddress.PINCODE;

    public static String SORT_DESCENDING = "DESC NULLS LAST";
    public static String SORT_ASCENDING = "ASC NULLS LAST";





    public static int CLEAR_FILTERS_ORDER_STATUS = -1;
    public static int FILTER_BY_STATUS_PENDING = 1;
    public static int FILTER_BY_STATUS_COMPLETE = 2;
    public static int FILTER_BY_STATUS_CANCELLED = 3;


    public static int CLEAR_FILTERS_DELIVERY_TYPE = -1;
    public static int FILTER_BY_HOME_DELIVERY = 1;
    public static int FILTER_BY_PICK_FROM_SHOP = 2;



    @BindView(R.id.clear_filter_order_status) TextView clearFilterOrderStatus;
    @BindView(R.id.filter_pending) TextView filterPending;
    @BindView(R.id.filter_complete) TextView filterComplete;
    @BindView(R.id.filter_cancelled) TextView filterCancelled;


    @BindView(R.id.filter_home_delivery) TextView filterHomeDelivery;
    @BindView(R.id.filter_pick_from_shop) TextView filterPickFromShop;
    @BindView(R.id.clear_filter_delivery_type) TextView clearFilterDeliveryType;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_sort_orders_cl,container,false);
        ButterKnife.bind(this,view);


        loadDefaultSort();
        bindOrderStatus();
        bindDeliveryType();

        return view;
    }




    void loadDefaultSort() {
//        String[] sort_options = PrefSortShops.getSort(getActivity());

        currentSort = PrefSortOrders.getSort(getActivity());
        currentAscending = PrefSortOrders.getAscending(getActivity());

        clearSelectionSort();
        clearSelectionAscending();

        if (currentSort.equals(SORT_BY_DISTANCE))
        {
            sort_by_distance.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_distance.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_DATE_TIME))
        {
            sort_by_date_time.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_date_time.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }
        else if(currentSort.equals(SORT_BY_PINCODE))
        {
            sort_by_pincode.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_pincode.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }
        else if(currentSort.equals(SORT_BY_STATUS))
        {
            sort_by_status.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_status.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }



        if(currentAscending.equals(SORT_ASCENDING))
        {
            sort_ascending.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
        else if(currentAscending.equals(SORT_DESCENDING))
        {
            sort_descending.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
    }



    @OnClick(R.id.sort_distance)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_distance.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_by_distance.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        PrefSortOrders.saveSort(getActivity(), SORT_BY_DISTANCE);

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_date_time)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_date_time.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_by_date_time.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        PrefSortOrders.saveSort(getActivity(), SORT_BY_DATE_TIME);

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }




    @OnClick(R.id.order_status)
    void sortByShopCount(View view)
    {
        clearSelectionSort();
        sort_by_status.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_by_status.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        PrefSortOrders.saveSort(getActivity(), SORT_BY_STATUS);

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }



    @OnClick(R.id.pincode)
    void sortByPriceAvg(View view)
    {
        clearSelectionSort();
        sort_by_pincode.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_by_pincode.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        PrefSortOrders.saveSort(getActivity(), SORT_BY_PINCODE);

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }






    @OnClick(R.id.sort_ascending)
    void ascendingClick(View view)
    {
        clearSelectionAscending();
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        PrefSortOrders.saveAscending(getActivity(),SORT_ASCENDING);

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }

    }


    @OnClick(R.id.sort_descending)
    void descendingClick(View view)
    {
        clearSelectionAscending();
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        PrefSortOrders.saveAscending(getActivity(),SORT_DESCENDING);

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }








    void clearSelectionSort()
    {
        sort_by_distance.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
        sort_by_date_time.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
        sort_by_status.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
        sort_by_pincode.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

        sort_by_distance.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        sort_by_date_time.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        sort_by_status.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        sort_by_pincode.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));

    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
    }







    @OnClick(R.id.clear_filter_order_status)
    void clearOrderStatusClick()
    {
        PrefSortOrders.saveFilterByOrderStatus(getActivity(),CLEAR_FILTERS_ORDER_STATUS);
        bindOrderStatus();

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }



    @OnClick(R.id.filter_pending)
    void filterByStatusPending()
    {
        PrefSortOrders.saveFilterByOrderStatus(getActivity(),FILTER_BY_STATUS_PENDING);
        bindOrderStatus();

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }



    @OnClick(R.id.filter_complete)
    void filterByStatusComplete()
    {
        PrefSortOrders.saveFilterByOrderStatus(getActivity(),FILTER_BY_STATUS_COMPLETE);
        bindOrderStatus();

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }




    @OnClick(R.id.filter_cancelled)
    void filterByStatusCancelled()
    {
        PrefSortOrders.saveFilterByOrderStatus(getActivity(),FILTER_BY_STATUS_CANCELLED);
        bindOrderStatus();

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }






    void bindOrderStatus()
    {
        int orderStatus = PrefSortOrders.getFilterByOrderStatus(getActivity());

        if(orderStatus == CLEAR_FILTERS_ORDER_STATUS)
        {
            clearFilterOrderStatus.setVisibility(View.GONE);

            filterPending.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterPending.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

            filterCancelled.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterCancelled.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

            filterComplete.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterComplete.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));


        }
        else if(orderStatus == FILTER_BY_STATUS_PENDING)
        {
            clearFilterOrderStatus.setVisibility(View.VISIBLE);


            filterPending.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));
            filterPending.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

            filterCancelled.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterCancelled.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

            filterComplete.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterComplete.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));


        }
        else if(orderStatus == FILTER_BY_STATUS_COMPLETE)
        {
            clearFilterOrderStatus.setVisibility(View.VISIBLE);


            filterPending.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterPending.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

            filterCancelled.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterCancelled.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

            filterComplete.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));
            filterComplete.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        }
        else if(orderStatus == FILTER_BY_STATUS_CANCELLED)
        {
            clearFilterOrderStatus.setVisibility(View.VISIBLE);


            filterPending.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterPending.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

            filterCancelled.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));
            filterCancelled.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

            filterComplete.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterComplete.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

        }

    }




    @OnClick(R.id.clear_filter_delivery_type)
    void clearDeliveryType()
    {
        PrefSortOrders.saveFilterByDeliveryType(getActivity(),CLEAR_FILTERS_DELIVERY_TYPE);
        bindDeliveryType();

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }



    @OnClick(R.id.filter_pick_from_shop)
    void filterByPickFromShop()
    {
        PrefSortOrders.saveFilterByDeliveryType(getActivity(),FILTER_BY_PICK_FROM_SHOP);
        bindDeliveryType();

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }


    @OnClick(R.id.filter_home_delivery)
    void filterByHomeDelivery()
    {
        PrefSortOrders.saveFilterByDeliveryType(getActivity(),FILTER_BY_HOME_DELIVERY);
        bindDeliveryType();

        if(getParentFragment() instanceof NotifySort)
        {
            ((NotifySort)getParentFragment()).notifySortChanged();
        }
    }



    void bindDeliveryType()
    {
        int deliveryType = PrefSortOrders.getFilterByDeliveryType(getActivity());


        if(deliveryType==CLEAR_FILTERS_DELIVERY_TYPE)
        {
            clearFilterDeliveryType.setVisibility(View.GONE);

            filterPickFromShop.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterPickFromShop.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

            filterHomeDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterHomeDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

        }
        else if(deliveryType==FILTER_BY_PICK_FROM_SHOP)
        {

            clearFilterDeliveryType.setVisibility(View.VISIBLE);

            filterPickFromShop.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.buttonColor));
            filterPickFromShop.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

            filterHomeDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterHomeDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
        }
        else if(deliveryType==FILTER_BY_HOME_DELIVERY)
        {
            clearFilterDeliveryType.setVisibility(View.VISIBLE);

            filterPickFromShop.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
            filterPickFromShop.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

            filterHomeDelivery.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.buttonColor));
            filterHomeDelivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        }
    }


}
