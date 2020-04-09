package org.nearbyshops.enduserappnew.adminModule.DashboardStaff;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Lists.OrderHistory.OrderHistory;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersList;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersListFragment;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.adminModule.ItemsDatabaseForAdmin.ItemsDatabaseAdmin;
import org.nearbyshops.enduserappnew.adminModule.ShopsList.ShopsDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StaffDashboardFragment extends Fragment {



    @BindView(R.id.service_name)
    TextView serviceName;


    public StaffDashboardFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_staff_dashboard, container, false);

        ButterKnife.bind(this,rootView);


        if(PrefServiceConfig.getServiceName(getActivity())!=null)
        {
            serviceName.setVisibility(View.VISIBLE);
            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
        }


        return rootView;
    }








    @OnClick(R.id.items_database)
    void optionItemCatApprovals()
    {
        startActivity(new Intent(getActivity(), ItemsDatabaseAdmin.class));
    }


    //    @OnClick(R.id.item_specifications)
//    void itemSpecNameClick()
    {
//        Intent intent = new Intent(getActivity(), ItemSpecName.class);
//        startActivity(intent);
    }







    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }






    @OnClick(R.id.shops_database)
    void optionAdminClick(View view)
    {
        Intent intent = new Intent(getActivity(), ShopsDatabase.class);
        startActivity(intent);
    }





    @OnClick(R.id.user_accounts)
    void optionUsersClick(View view)
    {
        Intent intent = new Intent(getActivity(), UsersList.class);
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY,UsersListFragment.MODE_ADMIN_USER_LIST);
        startActivity(intent);
    }



    @OnClick(R.id.orders_database)
    void ordersClick()
    {
        Intent intent = new Intent(getActivity(), OrderHistory.class);
        startActivity(intent);
    }



    @OnClick(R.id.header_tutorials)
    void headerTutorialsClick()
    {
        UtilityFunctions.openURL("https://blog.nearbyshops.org/tag/admin-tutorials/",getActivity());
    }
    


}
