package org.nearbyshops.whitelabelapp.ShopReview.SlidingLayerSort;

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

import org.nearbyshops.whitelabelapp.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.R;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerSortReview extends Fragment {

    @BindView(R.id.sort_by_date)
    TextView sort_by_date;

//    @BindView(R.id.sort_by_username)
//    TextView sort_by_username;

    @BindView(R.id.sort_by_thanks_count)
    TextView sort_by_thanks_count;

//    @Bind(R.id.sort_by_updated)
//    TextView sort_by_updated;

//    @Bind(R.id.price_average)
//    TextView sort_by_price_avg;

//    @Bind(R.id.shop_count)
//    TextView sort_by_shop_count;

    @BindView(R.id.sort_ascending)
    TextView sort_ascending;

    @BindView(R.id.sort_descending)
    TextView sort_descending;

    String currentSort = SORT_BY_DATE;
    String currentAscending = SORT_ASCENDING;

    int colorSelected = R.color.blueGrey800;
    int colorSelectedAscending = R.color.gplus_color_2;


    public static String SORT_BY_DATE = ShopReview.REVIEW_DATE;
    public static String SORT_BY_THANKS_COUNT = "thanks_count";
//    public static String SORT_BY_SHOP_COUNT = "shop_count";
//    public static String SORT_BY_AVG_PRICE = "avg_price";

    public static String SORT_DESCENDING = "DESC";
    public static String SORT_ASCENDING = "ASC";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_sort_review,container,false);
        ButterKnife.bind(this,view);

        loadDefaultSort();
        return view;
    }





    void loadDefaultSort() {
//        String[] sort_options = UtilitySortShopReview.getSort(getActivity());

        currentSort = UtilitySortShopReview.getSort(getActivity());
        currentAscending = UtilitySortShopReview.getAscending(getActivity());

        clearSelectionSort();
        clearSelectionAscending();

        if (currentSort.equals(SORT_BY_DATE))
        {
            sort_by_date.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_date.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_THANKS_COUNT))
        {
            sort_by_thanks_count.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_thanks_count.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
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



    @OnClick(R.id.sort_by_date)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_date.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_by_date.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortShopReview.saveSort(getActivity(),SORT_BY_DATE);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_by_thanks_count)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_thanks_count.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_by_thanks_count.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortShopReview.saveSort(getActivity(),SORT_BY_THANKS_COUNT);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_ascending)
    void ascendingClick(View view)
    {
        clearSelectionAscending();
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        UtilitySortShopReview.saveAscending(getActivity(),SORT_ASCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }

    }


    @OnClick(R.id.sort_descending)
    void descendingClick(View view)
    {
        clearSelectionAscending();
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        UtilitySortShopReview.saveAscending(getActivity(),SORT_DESCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    void clearSelectionSort()
    {
        sort_by_date.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
        sort_by_thanks_count.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
//        sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
//        sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_by_date.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        sort_by_thanks_count.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
//        sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
//        sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
    }

}
