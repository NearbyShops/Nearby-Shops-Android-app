package org.nearbyshops.enduserappnew.adminModule.Preferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerSortItems extends Fragment {

    @BindView(R.id.sort_rating)
    TextView sort_by_rating;

    @BindView(R.id.sort_popularity)
    TextView sort_by_popularity;

    @BindView(R.id.shop_count)
    TextView sort_by_shop_count;

    @BindView(R.id.item_price)
    TextView sort_by_price_avg;




    @BindView(R.id.sort_ascending)
    TextView sort_ascending;

    @BindView(R.id.sort_descending)
    TextView sort_descending;

    String currentSort = SORT_BY_ITEM_RATING;
    String currentAscending = SORT_DESCENDING;

    int colorSelected = R.color.blueGrey800;
    int colorSelectedAscending = R.color.colorAccent;


    public static String SORT_BY_ITEM_RATING = "avg_rating";
    public static String SORT_BY_POPULARITY = "popularity";
    public static String SORT_BY_SHOP_COUNT = "shop_count";
    public static String SORT_BY_AVG_PRICE = "avg_price";

    public static String SORT_DESCENDING = "DESC NULLS LAST";
    public static String SORT_ASCENDING = "ASC NULLS LAST";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_sort_items_by_category,container,false);
        ButterKnife.bind(this,view);

        loadDefaultSort();
        return view;
    }



    void loadDefaultSort() {
//        String[] sort_options = UtilitySortShopAdmin.getSort(getActivity());

        currentSort = PrefSortItemsByCategory.getSort(getActivity());
        currentAscending = PrefSortItemsByCategory.getAscending(getActivity());

        clearSelectionSort();
        clearSelectionAscending();

        if (currentSort.equals(SORT_BY_ITEM_RATING))
        {
            sort_by_rating.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_rating.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_POPULARITY))
        {
            sort_by_popularity.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_popularity.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }
        else if(currentSort.equals(SORT_BY_AVG_PRICE))
        {
            sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }
        else if(currentSort.equals(SORT_BY_SHOP_COUNT))
        {
            sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }



        if(currentAscending.equals(SORT_ASCENDING))
        {
            sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
        else if(currentAscending.equals(SORT_DESCENDING))
        {
            sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
    }



    @OnClick(R.id.sort_rating)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_rating.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_rating.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        PrefSortItemsByCategory.saveSort(getActivity(),SORT_BY_ITEM_RATING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_popularity)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_popularity.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_popularity.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        PrefSortItemsByCategory.saveSort(getActivity(),SORT_BY_POPULARITY);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.shop_count)
    void sortByShopCount(View view)
    {
        clearSelectionSort();
        sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        PrefSortItemsByCategory.saveSort(getActivity(),SORT_BY_SHOP_COUNT);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    @OnClick(R.id.item_price)
    void sortByPriceAvg(View view)
    {
        clearSelectionSort();
        sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        PrefSortItemsByCategory.saveSort(getActivity(),SORT_BY_AVG_PRICE);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }






    @OnClick(R.id.sort_ascending)
    void ascendingClick(View view)
    {
        clearSelectionAscending();
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        PrefSortItemsByCategory.saveAscending(getActivity(),SORT_ASCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }

    }


    @OnClick(R.id.sort_descending)
    void descendingClick(View view)
    {
        clearSelectionAscending();
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        PrefSortItemsByCategory.saveAscending(getActivity(),SORT_DESCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    void clearSelectionSort()
    {
        sort_by_rating.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_popularity.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_by_rating.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_popularity.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }

}
