package org.nearbyshops.enduserappnew.aSDSAdminModule.DashboardAdmin;

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
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.enduserappnew.EditDataScreens.EditServiceConfig.EditConfiguration;
import org.nearbyshops.enduserappnew.Lists.OrderHistory.OrderHistory;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersList;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersListFragment;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.adminModule.ItemsDatabaseForAdmin.ItemsDatabaseAdmin;
import org.nearbyshops.enduserappnew.adminModule.ShopsList.ShopsDatabase;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SDSAdminDashboardFragment extends Fragment {





    public SDSAdminDashboardFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_admin_dashboard_sds, container, false);

        ButterKnife.bind(this,rootView);


        return rootView;
    }








    @OnClick(R.id.markets_list)
    void optionAdminClick(View view)
    {
        Intent intent = new Intent(getActivity(), ShopsDatabase.class);
        startActivity(intent);
    }



    @OnClick(R.id.users_list)
    void optionUsersClick(View view)
    {
        Intent intent = new Intent(getActivity(), UsersList.class);
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY,UsersListFragment.MODE_SUPER_ADMIN_USER_LIST);
        startActivity(intent);
    }






    @OnClick(R.id.header_tutorials)
    void headerTutorialsClick()
    {
        UtilityFunctions.openURL("https://blog.nearbyshops.org/tag/admin-tutorials/",getActivity());
    }

}
