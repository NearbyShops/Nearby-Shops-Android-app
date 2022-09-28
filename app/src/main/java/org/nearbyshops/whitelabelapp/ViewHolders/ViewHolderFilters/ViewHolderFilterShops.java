package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterShopsData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class ViewHolderFilterShops extends RecyclerView.ViewHolder {



    @BindView(R.id.clear_sort) TextView clearSort;

    @BindView(R.id.sort_by_distance) TextView sortByDistance;
    @BindView(R.id.sort_by_popularity) TextView sortByPopularity;
    @BindView(R.id.sort_by_rating) TextView sortByRating;
    @BindView(R.id.sort_by_new) TextView sortByNewest;



    @BindView(R.id.clear_sort_order) TextView clearSortOrder;

    @BindView(R.id.sort_low_to_high) TextView sortLowToHigh;
    @BindView(R.id.sort_high_to_low) TextView sortHighToLow;


    @BindView(R.id.clear_delivery_or_pickup) TextView clearDeliveryOrPickup;

    @BindView(R.id.option_delivery) TextView optionDelivery;
    @BindView(R.id.option_pickup) TextView optionPickup;



    @BindView(R.id.filters_block) ConstraintLayout filtersBlock;



    public static String SORT_BY_DISTANCE = " distance asc ";
    public static String SORT_BY_POPULARITY = " popularity desc nulls last ";
    public static String SORT_BY_RATING = " avg_rating desc nulls last ";
    public static String SORT_BY_NEW = Shop.TIMESTAMP_CREATED + " desc ";


    public static String SORT_ASCENDING = " asc ";
    public static String SORT_DESCENDING = " desc ";



    private Context context;
    private Fragment fragment;
    private FilterShopsData filterShopsData;




    public static ViewHolderFilterShops create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_filter_shops, parent, false);
        return new ViewHolderFilterShops(view, context, fragment);
    }


    public ViewHolderFilterShops(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;

        bindSort();
//        bindSortOrder();
    }




    public void setItem(FilterShopsData filterShopsData)
    {
        this.filterShopsData = filterShopsData;

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






    @OnClick({R.id.sort_by_distance,R.id.clear_sort})
    void deliveryASAPClick()
    {
        saveSortBy(context, SORT_BY_DISTANCE);
        bindSort();

        notifyFiltersUpdated();
    }






    @OnClick(R.id.sort_by_popularity)
    void deliveryTodayClick()
    {

        saveSortBy(context, SORT_BY_POPULARITY);
        bindSort();

        notifyFiltersUpdated();
    }




    @OnClick(R.id.sort_by_rating)
    void deliveryTomorrowClick()
    {
        saveSortBy(context, SORT_BY_RATING);
        bindSort();

        notifyFiltersUpdated();
    }



    @OnClick(R.id.sort_by_new)
    void sortNewClick()
    {
        saveSortBy(context, SORT_BY_NEW);
        bindSort();

        notifyFiltersUpdated();
    }





    void bindSort()
    {

        clearSort();


//        clearSort.setVisibility(View.VISIBLE);


        if(getSortBy(context).equals(SORT_BY_DISTANCE))
        {
            sortByDistance.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByDistance.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
//            clearSort.setVisibility(View.INVISIBLE);
        }
        else if(getSortBy(context).equals(SORT_BY_POPULARITY))
        {
            sortByPopularity.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByPopularity.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else if(getSortBy(context).equals(SORT_BY_RATING))
        {
            sortByRating.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByRating.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else if(getSortBy(context).equals(SORT_BY_NEW))
        {
            sortByNewest.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByNewest.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }

    }





    private void clearSort()
    {

        sortByDistance.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByDistance.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortByPopularity.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByPopularity.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortByRating.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByRating.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortByNewest.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByNewest.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
    }






    @OnClick({R.id.sort_low_to_high,R.id.clear_sort_order})
    void setSortLowToHigh()
    {
        saveSortOrder(context, SORT_ASCENDING);
        bindSortOrder();

        notifyFiltersUpdated();
    }






    @OnClick(R.id.sort_high_to_low)
    void sortHighToLowClick()
    {

        saveSortOrder(context, SORT_DESCENDING);
        bindSortOrder();

        notifyFiltersUpdated();
    }



    void bindSortOrder()
    {

        clearSortOrder();
        clearSortOrder.setVisibility(View.VISIBLE);

        if(getSortOrder(context).equals(SORT_ASCENDING))
        {
            sortLowToHigh.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortLowToHigh.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            clearSortOrder.setVisibility(View.INVISIBLE);
        }
        else if(getSortOrder(context).equals(SORT_DESCENDING))
        {
            sortHighToLow.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortHighToLow.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }

    }





    private void clearSortOrder()
    {
        sortLowToHigh.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortLowToHigh.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortHighToLow.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortHighToLow.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
    }





    /*Preferences*/


    public static String getSortString(Context context)
    {

        if(context==null)
        {
            return " ";
        }

//        return getSortBy(context) + " " + getSortOrder(context) + " nulls last ";
        return getSortBy(context);
    }



    public static void saveSortBy(Context context, String sortBy)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_shops", sortBy);
        editor.apply();
    }



    public static String getSortBy(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString("sort_shops", SORT_BY_DISTANCE);
    }



    public static void saveSortOrder(Context context, String sortBy)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_shops_order", sortBy);
        editor.apply();
    }




    public static String getSortOrder(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString("sort_shops_order", SORT_ASCENDING);
    }



    public static void savePickup(Context context, boolean pickupAllowed)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("shop_filter_pickup", pickupAllowed);
        editor.apply();
    }




    public static boolean isPickupAllowed(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getBoolean("shop_filter_pickup", false);
    }




    public static void saveDelivery(Context context, boolean pickupAllowed)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("shop_filter_delivery", pickupAllowed);
        editor.apply();
    }




    public static boolean isDeliveryAllowed(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getBoolean("shop_filter_delivery", false);
    }






    private void notifyFiltersUpdated()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).filterShopUpdated();
        }
    }





    /* Interfaces */
    public interface ListItemClick
    {
        void filterShopUpdated();
    }

}

