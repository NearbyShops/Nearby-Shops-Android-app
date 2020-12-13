package org.nearbyshops.enduserappnew.InventoryOrders.OrderFilters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.AdapterDeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.ViewHolderDeliverySlot;
import org.nearbyshops.enduserappnew.InventoryOrders.OrderFilters.DeliveryGuyFilter.AdapterFilterDeliveryGuy;
import org.nearbyshops.enduserappnew.InventoryOrders.OrderFilters.ShopFilter.AdapterFilterShops;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.Models.FilterOrders;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class ViewHolderFilterOrders extends RecyclerView.ViewHolder {



    @BindView(R.id.clear_date) TextView clearDate;

    @BindView(R.id.delivery_asap) TextView dateASAP;
    @BindView(R.id.delivery_today) TextView dateToday;
    @BindView(R.id.delivery_tomorrow) TextView dateTomorrow;



    @BindView(R.id.clear_filter_slot) TextView clearDeliverySlot;
    @BindView(R.id.delivery_slot_list) RecyclerView deliverySlotList;
    AdapterDeliverySlot adapterDeliverySlot;


    @BindView(R.id.clear_filter_shop) TextView clearShop;
    @BindView(R.id.shop_list) RecyclerView shopListRecycler;
    AdapterFilterShops adapterFilterShops;


    @BindView(R.id.clear_filter_delivery) TextView clearDeliveryGuy;
    @BindView(R.id.delivery_guy_list) RecyclerView deliveryGuyList;
    AdapterFilterDeliveryGuy adapterFilterDeliveryGuy;


    @BindView(R.id.filters_block) ConstraintLayout filtersBlock;


    @BindView(R.id.filter_delivery_slot_block) LinearLayout filterDeliverySlotBlock;
    @BindView(R.id.filter_shop_block) LinearLayout filterShopBlock;
    @BindView(R.id.filter_delivery_guy_block) LinearLayout filterDeliveryGuyBlock;




    public static int FILTERS_CLEARED = 0;
    public static int FILTER_BY_DATE_ASAP = 1;
    public static int FILTER_BY_DATE_TODAY = 2;
    public static int FILTER_BY_DATE_TOMORROW = 3;


    private Context context;
    private Fragment fragment;
    private FilterOrders filterOrdersData;


    public static ViewHolderFilterOrders create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_filter_orders_by_date, parent, false);
        return new ViewHolderFilterOrders(view, context, fragment);
    }


    public ViewHolderFilterOrders(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;

        bindFilterByDate();
    }




    public void setItem(FilterOrders filterOrders)
    {
        this.filterOrdersData = filterOrders;


        if(filterOrdersData.isShowDeliverySlotFilter() && filterOrdersData.getOrderEndPoint().getDeliverySlotCount()>0)
        {
            filterDeliverySlotBlock.setVisibility(View.VISIBLE);
            setupDeliverySlotList();
        }
        else
        {
            // hide delivery slot list
            filterDeliverySlotBlock.setVisibility(View.GONE);
        }



        if(filterOrdersData.isShowShopFilter() && filterOrdersData.getOrderEndPoint().getShopCount()>0)
        {
            filterShopBlock.setVisibility(View.VISIBLE);
            setupShopList();

        }
        else
        {
            // hide shop list
            filterShopBlock.setVisibility(View.GONE);
        }

    }




    void setupDeliverySlotList()
    {
        List<Object> slotList = new ArrayList<>(filterOrdersData.getOrderEndPoint().getDeliverySlotList());

        adapterDeliverySlot = new AdapterDeliverySlot(slotList,context,fragment, ViewHolderDeliverySlot.MODE_END_USER);


        if(this.filterOrdersData.getSelectedDeliverySlotID()==null)
        {
            adapterDeliverySlot.setDeliverySlotID(-1);
        }
        else
        {
            adapterDeliverySlot.setDeliverySlotID(this.filterOrdersData.getSelectedDeliverySlotID());
        }



        deliverySlotList.setAdapter(adapterDeliverySlot);
        deliverySlotList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        deliverySlotList.setLayoutManager(layoutManager);





    }



    void setupShopList()
    {
        List<Object> shopArrayList = new ArrayList<>(filterOrdersData.getOrderEndPoint().getShopList());

        adapterFilterShops = new AdapterFilterShops(shopArrayList,context,fragment);

        if(this.filterOrdersData.getSelectedShopID()==null)
        {
            adapterFilterShops.setShopID(-1);
        }
        else
        {
            adapterFilterShops.setShopID(this.filterOrdersData.getSelectedShopID());
        }

        shopListRecycler.setAdapter(adapterFilterShops);
        shopListRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        shopListRecycler.setLayoutManager(layoutManager);
    }





    void setupDeliveryGuyList()
    {

    }




    @OnClick(R.id.show_hide_filters)
    void showHideClick()
    {
        if(filtersBlock.getVisibility()==View.GONE)
        {
            filtersBlock.setVisibility(View.VISIBLE);
        }
        else
        {
            filtersBlock.setVisibility(View.GONE);
        }

    }




    @OnClick(R.id.delivery_asap)
    void deliveryASAPClick()
    {
        saveFilterByDate(context, FILTER_BY_DATE_ASAP);
        bindFilterByDate();

        notifyFiltersUpdated();
    }




    @OnClick(R.id.delivery_today)
    void deliveryTodayClick()
    {

        saveFilterByDate(context, FILTER_BY_DATE_TODAY);
        bindFilterByDate();

        notifyFiltersUpdated();
    }



    @OnClick(R.id.delivery_tomorrow)
    void deliveryTomorrowClick()
    {
        saveFilterByDate(context, FILTER_BY_DATE_TOMORROW);
        bindFilterByDate();

        notifyFiltersUpdated();
    }




    void bindFilterByDate()
    {

        clearDate();

        if(getFilterByDate(context)==FILTER_BY_DATE_ASAP)
        {
            dateASAP.setTextColor(ContextCompat.getColor(context,R.color.white));
            dateASAP.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        else if(getFilterByDate(context)==FILTER_BY_DATE_TODAY)
        {
            dateToday.setTextColor(ContextCompat.getColor(context,R.color.white));
            dateToday.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        else if(getFilterByDate(context)==FILTER_BY_DATE_TOMORROW)
        {
            dateTomorrow.setTextColor(ContextCompat.getColor(context,R.color.white));
            dateTomorrow.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }

    }




    private void clearDate()
    {

        dateASAP.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        dateASAP.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        dateToday.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        dateToday.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        dateTomorrow.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        dateTomorrow.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
    }





    @OnClick(R.id.clear_date)
    void clearDateClick()
    {
        clearDateStatic(context);
        bindFilterByDate();

        notifyFiltersUpdated();
    }


    @OnClick(R.id.clear_filter_slot)
    void clearDeliverySlot()
    {
        adapterDeliverySlot.setDeliverySlotID(-1);
        adapterDeliverySlot.notifyDataSetChanged();

        notifyFiltersUpdated();
    }




    @OnClick(R.id.clear_filter_shop)
    void clearShop()
    {
        adapterFilterShops.setShopID(-1);
        adapterFilterShops.notifyDataSetChanged();

        notifyFiltersUpdated();
    }





    /*Preferences*/

    public static void clearDateStatic(Context context)
    {
        saveFilterByDate(context,FILTERS_CLEARED);
    }



    public static void saveFilterByDate(Context context, int dateConstant)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("delivery_date", dateConstant);
        editor.apply();
    }



    public static int getFilterByDate(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getInt("delivery_date", FILTER_BY_DATE_TODAY);
    }





    /* Interfaces */
    private void notifyFiltersUpdated()
    {
        if(fragment instanceof ListItemClick)
        {

            int deliverySlotID=-1;
            int shopID=-1;


            if(adapterDeliverySlot!=null)
            {
                deliverySlotID=adapterDeliverySlot.getDeliverySlotID();
            }

            if(adapterFilterShops!=null)
            {
                shopID=adapterFilterShops.getShopID();
            }


            ((ListItemClick)fragment).filtersUpdated(
                    deliverySlotID,
                    shopID,
                    -1,
                    getFilterByDate(context)
            );

        }
    }




    public interface ListItemClick
    {
        void filtersUpdated(int deliverySlotID,int shopID, int deliveryBoyID, int date);
    }

}

