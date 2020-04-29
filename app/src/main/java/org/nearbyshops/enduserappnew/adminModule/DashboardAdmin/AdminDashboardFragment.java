package org.nearbyshops.enduserappnew.adminModule.DashboardAdmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.Lists.OrderHistoryPaging.OrderHistoryPaging;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersListFragment;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.EditDataScreens.EditMarket.EditMarket;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersList;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.adminModule.ItemsDatabaseForAdmin.ItemsDatabaseAdmin;
import org.nearbyshops.enduserappnew.adminModule.ShopsList.ShopsDatabase;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.enduserappnew.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminDashboardFragment extends Fragment {





    @Inject
    ServiceConfigurationService configurationService;



    @BindView(R.id.service_name)
    TextView serviceName;





    public AdminDashboardFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

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



    @OnClick(R.id.service_config)
    void serviceCOnfigClick()
    {
//        startActivity(new Intent(getActivity(), EditServiceConfiguration.class));


        getServiceConfig(true);
    }









    private void getServiceConfig(final boolean launchEditConfig)
    {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");
        pd.show();

        Call<Market> call = configurationService.getServiceConfiguration(null,null);



        call.enqueue(new Callback<Market>() {
            @Override
            public void onResponse(Call<Market> call, Response<Market> response) {

                if(response.code()==200 && response.body()!=null)
                {

                    PrefServiceConfig.saveServiceConfigLocal(
                            response.body(),getActivity()
                    );


                    if(launchEditConfig)
                    {
                        startActivity(new Intent(getActivity(), EditMarket.class));
                    }
                }
                else
                {
                    System.out.println("Failed to get config " + response.code());
                }

                pd.dismiss();
            }



            @Override
            public void onFailure(Call<Market> call, Throwable t) {


                pd.dismiss();

                System.out.println("Check your network !");
            }
        });

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



    @OnClick(R.id.staff_accounts)
    void optionStaffClick(View view)
    {
        Intent intent = new Intent(getActivity(), UsersList.class);
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY,UsersListFragment.MODE_ADMIN_STAFF_LIST);
        startActivity(intent);
    }





    //    @OnClick(R.id.edit_profile)
    void editProfileClick()
    {
        Intent intent = new Intent(getActivity(), EditProfile.class);
        intent.putExtra(FragmentEditProfile.EDIT_MODE_INTENT_KEY, FragmentEditProfile.MODE_UPDATE);
        startActivity(intent);
    }







    @OnClick(R.id.orders_database)
    void ordersClick()
    {
        Intent intent = new Intent(getActivity(), OrderHistoryPaging.class);
        startActivity(intent);
    }



    @OnClick(R.id.header_tutorials)
    void headerTutorialsClick()
    {
        UtilityFunctions.openURL("https://blog.nearbyshops.org/tag/admin-tutorials/",getActivity());
    }
    


}
