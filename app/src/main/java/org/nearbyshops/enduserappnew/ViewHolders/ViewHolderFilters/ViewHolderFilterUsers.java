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

import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class ViewHolderFilterUsers extends RecyclerView.ViewHolder {


    @BindView(R.id.list_item)
    LinearLayout listItem;


    @BindView(R.id.sort_by_time)
    TextView sortByTime;
    @BindView(R.id.sort_by_user_id)
    TextView sortByUserID;
    @BindView(R.id.sort_ascending)
    TextView sortAscending;
    @BindView(R.id.sort_descending)
    TextView sortDescending;

    @BindView(R.id.role_end_user)
    TextView roleEndUser;
    @BindView(R.id.role_shop_admin)
    TextView roleShopAdmin;
    @BindView(R.id.role_shop_staff)
    TextView roleShopStaff;
    @BindView(R.id.role_delivery)
    TextView roleDelivery;
    @BindView(R.id.role_staff)
    TextView roleStaff;
    @BindView(R.id.role_admin)
    TextView roleAdmin;

    @BindView(R.id.clear_filter_roles)
    TextView clearFilterRoles;

    @BindView(R.id.reset_sort) TextView resetSort;
    @BindView(R.id.reset_sort_order) TextView resetSortOrder;


    @BindView(R.id.bottom_strip)
    LinearLayout bottomStrip;




    private Context context;
    //    private User user;
    private Fragment fragment;


    public static ViewHolderFilterUsers create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_filter_users, parent, false);
        return new ViewHolderFilterUsers(view, context, fragment);
    }


    public ViewHolderFilterUsers(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;


        bindFilterUserRoles(false);
        bindSortOrder(false);
        bindSort(false);

        hideBottomStrip();
    }







//    public void setItem(User user)
//    {
//
//        this.user = user;
//
//
//    }








    private void hideBottomStrip()
    {

        User user = PrefLogin.getUser(context);

        if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
        {
            bottomStrip.setVisibility(View.GONE);
            roleEndUser.setVisibility(View.GONE);
//            clearFilterRoles.setVisibility(View.GONE);
        }
        else
        {
            bottomStrip.setVisibility(View.VISIBLE);
            roleEndUser.setVisibility(View.VISIBLE);
//            clearFilterRoles.setVisibility(View.VISIBLE);
        }
    }






    private void notifyFiltersUpdated()
    {
        ((ListItemClick)fragment).filtersUpdated();
    }




    public interface ListItemClick
    {
        void filtersUpdated();
    }




    @OnClick(R.id.sort_by_time)
    void sortByTimeClick()
    {
        saveSortBy(context,User.TIMESTAMP_CREATED);
        bindSort(true);
    }



    @OnClick(R.id.sort_by_user_id)
    void sortByUserIDClick()
    {
        saveSortBy(context,User.USER_ID);
        bindSort(true);
    }




    private void bindSort(boolean notifyUpdate)
    {
        String sortBy = getSortBy(context);


        if(notifyUpdate)
        {
            notifyFiltersUpdated();
        }


        if(sortBy.equals(User.TIMESTAMP_CREATED))
        {
            sortByTime.setTextColor(ContextCompat.getColor(context, R.color.white));
            sortByTime.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonColor));

            sortByUserID.setTextColor(ContextCompat.getColor(context, R.color.blueGrey800));
            sortByUserID.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));

            resetSort.setVisibility(View.GONE);
        }
        else if(sortBy.equals(User.USER_ID))
        {
            sortByUserID.setTextColor(ContextCompat.getColor(context, R.color.white));
            sortByUserID.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonColor));

            sortByTime.setTextColor(ContextCompat.getColor(context, R.color.blueGrey800));
            sortByTime.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));

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





    @OnClick(R.id.role_end_user)
    void roleEndUserClick()
    {
        saveFilterByRole(context,User.ROLE_END_USER_CODE);
        bindFilterUserRoles(true);
    }

    @OnClick(R.id.role_shop_admin)
    void roleShopAdminClick()
    {
        saveFilterByRole(context,User.ROLE_SHOP_ADMIN_CODE);
        bindFilterUserRoles(true);
    }

    @OnClick(R.id.role_shop_staff)
    void roleShopStaffClick()
    {
        saveFilterByRole(context,User.ROLE_SHOP_STAFF_CODE);
        bindFilterUserRoles(true);
    }



    @OnClick(R.id.role_delivery)
    void roleDeliveryClick()
    {
        saveFilterByRole(context,User.ROLE_DELIVERY_GUY_SELF_CODE);
        bindFilterUserRoles(true);
    }


    @OnClick(R.id.role_admin)
    void roleAdminClick()
    {
        saveFilterByRole(context,User.ROLE_ADMIN_CODE);
        bindFilterUserRoles(true);
    }



    @OnClick(R.id.role_staff)
    void roleStaffClick()
    {
        saveFilterByRole(context,User.ROLE_STAFF_CODE);
        bindFilterUserRoles(true);
    }






    @OnClick(R.id.clear_filter_roles)
    void clearFilterRoles()
    {
        saveFilterByRole(context,0);
        bindFilterUserRoles(true);
    }




    private void bindFilterUserRoles(boolean notifyUpdate)
    {
        int userRole = getFilterByRole(context);

        clearFilterUserRole();


        if(notifyUpdate)
        {
            notifyFiltersUpdated();
        }



        if(userRole!=0)
        {
            clearFilterRoles.setVisibility(View.VISIBLE);
        }


        if(userRole==User.ROLE_END_USER_CODE)
        {
            roleEndUser.setTextColor(ContextCompat.getColor(context,R.color.white));
            roleEndUser.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
        }
        else if(userRole==User.ROLE_DELIVERY_GUY_CODE||userRole==User.ROLE_DELIVERY_GUY_SELF_CODE)
        {
            roleDelivery.setTextColor(ContextCompat.getColor(context,R.color.white));
            roleDelivery.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));

        }
        else if(userRole==User.ROLE_SHOP_ADMIN_CODE)
        {
            roleShopAdmin.setTextColor(ContextCompat.getColor(context,R.color.white));
            roleShopAdmin.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
        }
        else if(userRole==User.ROLE_SHOP_STAFF_CODE)
        {
            roleShopStaff.setTextColor(ContextCompat.getColor(context,R.color.white));
            roleShopStaff.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
        }
        else if(userRole==User.ROLE_STAFF_CODE)
        {
            roleStaff.setTextColor(ContextCompat.getColor(context,R.color.white));
            roleStaff.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
        }
        else if(userRole==User.ROLE_ADMIN_CODE)
        {
            roleAdmin.setTextColor(ContextCompat.getColor(context,R.color.white));
            roleAdmin.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonColor));
        }

    }




    private void clearFilterUserRole()
    {
        clearFilterRoles.setVisibility(View.GONE);

        roleEndUser.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        roleEndUser.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        roleShopAdmin.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        roleShopAdmin.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        roleShopStaff.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        roleShopStaff.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        roleDelivery.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        roleDelivery.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        roleAdmin.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        roleAdmin.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

        roleStaff.setTextColor(ContextCompat.getColor(context,R.color.blueGrey800));
        roleStaff.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
    }






    /*Preferences*/

    public static void resetFilters(Context context)
    {
        saveFilterByRole(context,0);
    }



    public static String getSortString(Context context)
    {
        return getSortBy(context) + getSortOrder(context);
    }




    public static void saveFilterByRole(Context context, int userRole)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("filter_by_user_role", userRole);
        editor.apply();
    }




    public static int getFilterByRole(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getInt("filter_by_user_role", 0);
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
        editor.putString("sort_for_user_list", sortBy);
        editor.apply();
    }



    public static String getSortBy(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString("sort_for_user_list", User.TIMESTAMP_CREATED);
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
        editor.putString("sort_order_for_user_list", sortOrder);
        editor.apply();
    }


    public static String getSortOrder(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString("sort_order_for_user_list", MyApplication.SORT_DESCENDING);
    }











}

