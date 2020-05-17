package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class ViewHolderFilterMarkets extends RecyclerView.ViewHolder {


    @BindView(R.id.list_item)
    LinearLayout listItem;


    @BindView(R.id.sort_by_distance)
    TextView sortByDistance;

    @BindView(R.id.sort_by_date_created)
    TextView sortByDateCreated;


    @BindView(R.id.sort_ascending)
    TextView sortAscending;


    @BindView(R.id.sort_descending)
    TextView sortDescending;



    @BindView(R.id.ping_status_live)
    TextView filterPingStatusLive;

    @BindView(R.id.ping_status_dead)
    TextView filterPingStatusDead;

    @BindView(R.id.clear_ping_status)
    TextView resetPingStatus;


    @BindView(R.id.reset_sort) TextView resetSort;
    @BindView(R.id.reset_sort_order) TextView resetSortOrder;







    private Context context;
    //    private User user;
    private Fragment fragment;


    public static ViewHolderFilterMarkets create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_filter_markets, parent, false);
        return new ViewHolderFilterMarkets(view, context, fragment);
    }


    public ViewHolderFilterMarkets(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;


        bindFilterPingStatus(false);
        bindSortOrder(false);
        bindSort(false);
    }








    private void notifyFiltersUpdated()
    {
        ((ListItemClick)fragment).filtersUpdated();
    }




    public interface ListItemClick
    {
        void filtersUpdated();
    }




    @OnClick(R.id.sort_by_distance)
    void sortByTimeClick()
    {
        saveSortBy(context," distance ");
        bindSort(true);
    }



    @OnClick(R.id.sort_by_date_created)
    void sortByUserIDClick()
    {
        saveSortBy(context,Market.CREATED);
        bindSort(true);
    }




    private void bindSort(boolean notifyUpdate)
    {
        String sortBy = getSortBy(context);


        if(notifyUpdate)
        {
            notifyFiltersUpdated();
        }



        if(sortBy.equals(" distance "))
        {
            sortByDistance.setTextColor(ContextCompat.getColor(context, R.color.white));
            sortByDistance.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonColor));

            sortByDateCreated.setTextColor(ContextCompat.getColor(context, R.color.blueGrey800));
            sortByDateCreated.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));

            resetSort.setVisibility(View.GONE);
        }
        else if(sortBy.equals(Market.CREATED))
        {
            sortByDateCreated.setTextColor(ContextCompat.getColor(context, R.color.white));
            sortByDateCreated.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonColor));

            sortByDistance.setTextColor(ContextCompat.getColor(context, R.color.blueGrey800));
            sortByDistance.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));

            resetSort.setVisibility(View.VISIBLE);
        }
    }





    @OnClick(R.id.sort_ascending)
    void ascendingClick()
    {
        saveSortOrder(context,MyApplication.SORT_ASCENDING);
        bindSortOrder(true);
    }




    @OnClick(R.id.sort_descending)
    void descendingClick()
    {
        saveSortOrder(context,MyApplication.SORT_DESCENDING);
        bindSortOrder(true);
    }






    private void bindSortOrder(boolean notifyUpdate) {


        String sortOrder = getSortOrder(context);

        if(notifyUpdate)
        {
            notifyFiltersUpdated();
        }


        if (sortOrder.equals(MyApplication.SORT_ASCENDING)) {
            sortAscending.setTextColor(ContextCompat.getColor(context, R.color.white));
            sortAscending.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonColor));

            sortDescending.setTextColor(ContextCompat.getColor(context, R.color.blueGrey800));
            sortDescending.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
            resetSortOrder.setVisibility(View.GONE);
        }
        else if (sortOrder.equals(MyApplication.SORT_DESCENDING))
        {
            sortDescending.setTextColor(ContextCompat.getColor(context,R.color.white));
            sortDescending.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));

            sortAscending.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
            sortAscending.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
            resetSortOrder.setVisibility(View.VISIBLE);
        }

    }


    @OnClick(R.id.reset_sort)
    void resetSort()
    {
        saveSortBy(context,User.TIMESTAMP_CREATED);
        bindSort(true);
    }



    @OnClick(R.id.reset_sort_order)
    void resetSortOrder()
    {
        saveSortOrder(context,MyApplication.SORT_ASCENDING);
        bindSortOrder(true);
    }





    @OnClick(R.id.ping_status_live)
    void roleEndUserClick()
    {
        saveFilterByPingStatus(context,true);
        bindFilterPingStatus(true);
    }

    @OnClick(R.id.ping_status_dead)
    void roleShopAdminClick()
    {
        saveFilterByPingStatus(context,false);
        bindFilterPingStatus(true);
    }








    @OnClick(R.id.clear_ping_status)
    void clearFilterRoles()
    {
        saveFilterByPingStatus(context,true);
        bindFilterPingStatus(true);
    }




    private void bindFilterPingStatus(boolean notifyUpdate)
    {
        boolean filterByPingStatus = getPingStatusFilter(context);



        resetFilterPingStatus();


        if(notifyUpdate)
        {
            notifyFiltersUpdated();
        }



        if(filterByPingStatus)
        {
            resetPingStatus.setVisibility(View.VISIBLE);
        }




        if(filterByPingStatus)
        {
            filterPingStatusLive.setTextColor(ContextCompat.getColor(context,R.color.white));
            filterPingStatusLive.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
        }
        else
        {
            filterPingStatusDead.setTextColor(ContextCompat.getColor(context,R.color.white));
            filterPingStatusDead.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
        }

    }




    private void resetFilterPingStatus()
    {
        resetPingStatus.setVisibility(View.GONE);

        filterPingStatusLive.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        filterPingStatusLive.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        filterPingStatusDead.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        filterPingStatusDead.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
    }






    /*Preferences*/



    public static String getSortString(Context context)
    {
        return getSortBy(context) + " " +  getSortOrder(context);
    }





    public static void saveFilterByPingStatus(Context context, boolean pingStatus)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("filter_by_ping_status", pingStatus);
        editor.apply();
    }




    public static boolean getPingStatusFilter(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getBoolean("filter_by_ping_status", true);
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
        editor.putString("sort_for_markets_list", sortBy);
        editor.apply();
    }



    public static String getSortBy(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString("sort_for_markets_list", Market.CREATED);
    }



    public static void saveSortOrder(Context context, String sortOrder)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_order_for_markets_list", sortOrder);
        editor.apply();
    }


    public static String getSortOrder(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString("sort_order_for_markets_list", MyApplication.SORT_DESCENDING);
    }

}

