package org.nearbyshops.whitelabelapp.AdminDelivery.ButtonDashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import org.nearbyshops.whitelabelapp.AdminDelivery.ButtonDashboard.FragmentDeprecated.DeliveryInventoryFragment;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyAboutLogin;
import org.nearbyshops.whitelabelapp.AdminDelivery.InventoryDeliveryPerson.DeliveryPersonInventory;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliveryGuyHomeFragment extends Fragment {





    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.activity_delivery_guy_home, container, false);
        ButterKnife.bind(this,rootView);



        if(getActivity()!=null && getActivity().getActionBar()!=null )
        {
            getActivity().getActionBar().setTitle("Delivery Dashboard");
        }



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

//        Intent deliveryGuyDashboard = new Intent(getActivity(), DeliveryPersonInventory.class);
//        deliveryGuyDashboard.putExtra("delivery_guy_id",user.getUserID());

//        startActivity(deliveryGuyDashboard);


        if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE)
        {
            Intent intent = new Intent(getActivity(), DeliveryPersonInventory.class);
            intent.putExtra(DeliveryInventoryFragment.SCREEN_MODE_INTENT_KEY,DeliveryInventoryFragment.SCREEN_MODE_DELIVERY_PERSON_MARKET);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getActivity(), DeliveryPersonInventory.class);
            intent.putExtra(DeliveryInventoryFragment.SCREEN_MODE_INTENT_KEY,DeliveryInventoryFragment.SCREEN_MODE_DELIVERY_PERSON_VENDOR);
            startActivity(intent);

        }


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








    @OnClick({R.id.log_out_button})
    void loginClick()
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Confirm Logout !")
                .setMessage("Do you want to log out !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }







    private void logout()
    {


        UtilityFunctions.logout(getActivity());

        if(getActivity() instanceof NotifyAboutLogin)
        {
            ((NotifyAboutLogin) getActivity()).loggedOut();
        }

    }







    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }



}
