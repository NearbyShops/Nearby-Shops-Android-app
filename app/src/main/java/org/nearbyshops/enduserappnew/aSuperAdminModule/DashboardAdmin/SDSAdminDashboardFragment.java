package org.nearbyshops.enduserappnew.aSuperAdminModule.DashboardAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersList;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersListFragment;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.aSuperAdminModule.MarketsList.Markets;
import org.nearbyshops.enduserappnew.aSuperAdminModule.MarketsListPagingLib.MarketsList;

import butterknife.ButterKnife;
import butterknife.OnClick;


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
        Intent intent = new Intent(getActivity(), Markets.class);
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
