package org.nearbyshops.enduserappnew.aSellerModule.DashboardDeliveryGuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.aSellerModule.InventoryDeliveryPerson.DeliveryGuyDashboard;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliveryGuyHomeFragment extends Fragment {





    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.activity_delivery_guy_home, container, false);
        ButterKnife.bind(this,rootView);


//        if(getChildFragmentManager().findFragmentByTag(TAG_SERVICE_INDICATOR)==null)
//        {
//            getChildFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.service_indicator,new ServiceIndicatorFragment(),TAG_SERVICE_INDICATOR)
//                    .commit();
//        }
//


        return rootView;
    }










    @OnClick(R.id.image_dashboard)
    void dashboardClick()
    {
        User user = PrefLogin.getUser(getActivity());

        Intent deliveryGuyDashboard = new Intent(getActivity(), DeliveryGuyDashboard.class);
        deliveryGuyDashboard.putExtra("delivery_guy_id",user.getUserID());

        startActivity(deliveryGuyDashboard);

    }



    @OnClick(R.id.image_edit_profile)
    void editProfileClick()
    {
//        Intent intent = new Intent(this, EditDeliverySelf.class);
////        intent.putExtra(EditDeliverySelfFragment.DELIVERY_GUY_INTENT_KEY,UtilityLogin.getDeliveryGuySelf(this));
//        intent.putExtra(EditDeliverySelfFragment.EDIT_MODE_INTENT_KEY,EditDeliverySelfFragment.MODE_UPDATE);
//        startActivity(intent);




        Intent intent = new Intent(getActivity(), EditProfile.class);
        intent.putExtra(FragmentEditProfile.EDIT_MODE_INTENT_KEY, FragmentEditProfile.MODE_UPDATE);
//        intent.putExtra("user_role", User.ROLE_SHOP_STAFF_CODE);
        startActivity(intent);

    }













    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }



}
