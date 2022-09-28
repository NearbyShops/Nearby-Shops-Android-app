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

import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterItemsInMarket;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class ViewHolderFilterItems extends RecyclerView.ViewHolder {


    @BindView(R.id.show_hide_filters) TextView headerFilter;



    @BindView(R.id.clear_sort) TextView clearSort;

    @BindView(R.id.sort_by_bestseller) TextView sortByBestseller;
    @BindView(R.id.sort_by_discount) TextView sortByDiscount;
    @BindView(R.id.sort_by_new) TextView sortByNewest;

    @BindView(R.id.sort_by_price_lowest) TextView sortByPriceLowest;
    @BindView(R.id.sort_by_price_highest) TextView sortByPriceHighest;
    @BindView(R.id.sort_by_rating_highest) TextView sortByRating;

    @BindView(R.id.sort_by_available_highest) TextView sortByAvailableHighest;
    @BindView(R.id.sort_by_available_lowest) TextView sortByAvailableLowest;






    @BindView(R.id.filters_block) ConstraintLayout filtersBlock;



    public static String SORT_BY_BESTSELLER = " item_sold desc nulls last ";
    public static String SORT_BY_HIGHEST_DISCOUNT = " discount_percent desc ";
    public static String SORT_BY_NEW = Item.DATE_TIME_CREATED + " desc ";
    public static String SORT_BY_PRICE_LOWEST =  " avg_price asc ";
    public static String SORT_BY_PRICE_HIGHEST = " avg_price desc ";
    public static String SORT_BY_RATING_HIGHEST = " avg_rating desc nulls last ";
    public static String SORT_BY_AVAILABLE_HIGHEST = " shop_count desc nulls last ";
    public static String SORT_BY_AVAILABLE_LOWEST = " shop_count asc nulls last ";



    private Context context;
    private Fragment fragment;
    private FilterItemsInMarket filterItemsInShop;




    public static ViewHolderFilterItems create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_filter_items, parent, false);
        return new ViewHolderFilterItems(view, context, fragment);
    }


    public ViewHolderFilterItems(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;


        bindSort();
//        bindSortOrder();
    }




    public void setItem(FilterItemsInMarket filterShopsData)
    {
        this.filterItemsInShop = filterShopsData;

        headerFilter.setText(filterShopsData.getHeaderText());



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






    @OnClick({R.id.sort_by_bestseller,R.id.clear_sort})
    void bestsellerClick()
    {
        saveSortBy(context, SORT_BY_BESTSELLER);
        bindSort();

        notifyFiltersUpdated();
    }



    @OnClick({R.id.sort_by_discount})
    void discountClick()
    {
        saveSortBy(context, SORT_BY_HIGHEST_DISCOUNT);
        bindSort();

        notifyFiltersUpdated();
    }



    @OnClick({R.id.sort_by_new})
    void newClick()
    {
        saveSortBy(context, SORT_BY_NEW);
        bindSort();

        notifyFiltersUpdated();
    }




    @OnClick({R.id.sort_by_price_lowest})
    void priceLowestClick()
    {
        saveSortBy(context, SORT_BY_PRICE_LOWEST);
        bindSort();

        notifyFiltersUpdated();
    }



    @OnClick({R.id.sort_by_price_highest})
    void priceHighestClick()
    {
        saveSortBy(context, SORT_BY_PRICE_HIGHEST);
        bindSort();

        notifyFiltersUpdated();
    }




    @OnClick({R.id.sort_by_rating_highest})
    void ratingHighestClick()
    {
        saveSortBy(context, SORT_BY_RATING_HIGHEST);
        bindSort();

        notifyFiltersUpdated();
    }





    @OnClick({R.id.sort_by_available_highest})
    void availableHighestClick()
    {
        saveSortBy(context, SORT_BY_AVAILABLE_HIGHEST);
        bindSort();

        notifyFiltersUpdated();
    }




    @OnClick({R.id.sort_by_available_lowest})
    void availableLowestClick()
    {
        saveSortBy(context, SORT_BY_AVAILABLE_LOWEST);
        bindSort();

        notifyFiltersUpdated();
    }







    void bindSort()
    {




        clearSort();


//        clearSort.setVisibility(View.VISIBLE);


        if(getSortBy(context).equals(SORT_BY_BESTSELLER))
        {
            sortByBestseller.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByBestseller.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
//            clearSort.setVisibility(View.INVISIBLE);
        }
        else if(getSortBy(context).equals(SORT_BY_HIGHEST_DISCOUNT))
        {
            sortByDiscount.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByDiscount.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else if(getSortBy(context).equals(SORT_BY_NEW))
        {
            sortByNewest.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByNewest.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else if(getSortBy(context).equals(SORT_BY_PRICE_LOWEST))
        {
            sortByPriceLowest.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByPriceLowest.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else if(getSortBy(context).equals(SORT_BY_PRICE_HIGHEST))
        {
            sortByPriceHighest.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByPriceHighest.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else if(getSortBy(context).equals(SORT_BY_RATING_HIGHEST))
        {
            sortByRating.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByRating.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else if(getSortBy(context).equals(SORT_BY_AVAILABLE_HIGHEST))
        {
            sortByAvailableHighest.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByAvailableHighest.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
        else if(getSortBy(context).equals(SORT_BY_AVAILABLE_LOWEST))
        {
            sortByAvailableLowest.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortByAvailableLowest.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }


    }





    private void clearSort()
    {

        sortByBestseller.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByBestseller.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortByDiscount.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByDiscount.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortByNewest.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByNewest.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortByPriceLowest.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByPriceLowest.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortByPriceHighest.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByPriceHighest.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortByRating.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByRating.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));


        sortByAvailableLowest.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByAvailableLowest.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        sortByAvailableHighest.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        sortByAvailableHighest.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

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
        editor.putString("sort_items_in_market", sortBy);
        editor.apply();
    }



    public static String getSortBy(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString("sort_items_in_market", SORT_BY_BESTSELLER);
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

