package org.nearbyshops.enduserappnew.aSellerModule.DashboardShopAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShop.EditShop;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelShop;
import org.nearbyshops.enduserappnew.aSellerModule.DashboardShop.ShopDashboard;
import org.nearbyshops.enduserappnew.Lists.TransactionHistory.Transactions;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

public class ShopAdminHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {




    @Inject
    ShopService shopService;

    @BindView(R.id.notice) TextView notice;

    @BindView(R.id.shop_open_status) ImageView shopOpenStatus;
    @BindView(R.id.header) TextView headerText;
    @BindView(R.id.shop_open_switch) Switch shopOpenSwitch;
    @BindView(R.id.progress_switch) ProgressBar progressSwitch;

    @BindView(R.id.current_dues) TextView currentDues;
    @BindView(R.id.credit_limit) TextView creditLimit;
    @BindView(R.id.low_balance_message) TextView lowBalanceMessage;



    @BindView(R.id.shop_name) TextView shopName;


    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;



    public ShopAdminHomeFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_admin_home, container, false);
        ButterKnife.bind(this, rootView);

        setupSwipeContainer();


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//


        bindAllFields();

//        checkAccountActivation();

        if (savedInstanceState == null)
        {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {

                    onRefresh();
                }
            });
        }



        setupViewModel();



        return rootView;
    }








    private void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }





    @Override
    public void onRefresh() {
        refreshShop();
    }







    @OnClick(R.id.billing_info)
    void billingInfoClick()
    {
        Intent intent = new Intent(getActivity(), Transactions.class);
        startActivity(intent);
    }








//    @OnClick(R.id.image_edit_profile)
    void editProfileClick()
    {
        Intent intent = new Intent(getActivity(), EditProfile.class);
        intent.putExtra(FragmentEditProfile.EDIT_MODE_INTENT_KEY, FragmentEditProfile.MODE_UPDATE);
        startActivity(intent);
    }









    @OnClick(R.id.image_edit_shop)
    void editSHopClick()
    {
        if(PrefShopAdminHome.getShop(getActivity())==null)
        {
            // check online for shop exist or not
            // if shop exist save it in shop home and open it in edit mode
            // if shop does not exist then open edit shop fragment in ADD mode


            swipeContainer.post(new Runnable() {
                @Override
                public void run() {

                    swipeContainer.setRefreshing(true);
                }
            });



            viewModelShop.getShopForShopAdmin(false);
            requestCodeGetShop = 4150;
        }
        else
        {

            //     open edit shop in edit mode
            Intent intent = new Intent(getActivity(), EditShop.class);
            intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE);
            startActivity(intent);

            swipeContainer.setRefreshing(false);
        }



    }




    @OnClick(R.id.image_shop_dashboard)
    void shopDashboardClick()
    {



        if(PrefShopAdminHome.getShop(getActivity())==null)
        {

            viewModelShop.getShopForShopAdmin(false);
            requestCodeGetShop = 4130;
        }
        else
        {
            Intent intent = new Intent(getActivity(), ShopDashboard.class);
            startActivity(intent);

        }

    }






    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }











    private void bindAllFields()
    {
        bindToolbarHeader();
        bindBalance();
        bindShopOpenStatus();
        bindNotice();
    }



    private void bindToolbarHeader()
    {
        Shop shop = PrefShopAdminHome.getShop(getActivity());

        if(shop!=null)
        {
            shopName.setText(shop.getShopName());
        }
    }



    private void bindNotice()
    {

        Shop shop = PrefShopAdminHome.getShop(getActivity());

        if(shop==null)
        {
            return;
        }


        if(shop.getShopEnabled())
        {

            if(shop.getAccountBalance()<shop.getRt_min_balance())
            {
                notice.setText(getString(R.string.notice_low_balance));
                notice.setVisibility(View.VISIBLE);

            }
            else
            {
                notice.setVisibility(View.GONE);
            }

        }
        else
        {
            notice.setText(getString(R.string.notice_account_deactivated));
            notice.setVisibility(View.VISIBLE);
        }
    }





    private void bindShopOpenStatus()
    {

        Shop shop = PrefShopAdminHome.getShop(getActivity());


        if(shop==null)
        {
            return;
        }


        if(shop.getShopEnabled())
        {
            shopOpenSwitch.setEnabled(true);

            if(shop.isOpen())
            {

                shopOpenSwitch.setChecked(true);


                if(shop.getAccountBalance()<shop.getRt_min_balance())
                {
                    shopOpenStatus.setVisibility(View.GONE);
                }
                else
                {
                    shopOpenStatus.setVisibility(View.VISIBLE);
                    shopOpenStatus.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.open));
                }

                headerText.setText("Shop Open");
            }
            else
            {
//                shopOpenStatus.setVisibility(View.GONE);
                shopOpenStatus.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.shop_closed_small));

                shopOpenSwitch.setChecked(false);

                headerText.setText("Shop Closed");
            }
        }
        else
        {
            shopOpenStatus.setVisibility(View.GONE);
            shopOpenSwitch.setEnabled(false);

            headerText.setText("Shop Disabled");
        }
    }





    private void bindBalance()
    {
        Shop shop = PrefShopAdminHome.getShop(getActivity());


        if(shop==null)
        {
            return;
        }


        currentDues.setText("Balance : " + PrefGeneral.getCurrencySymbol(getActivity()) + String.format(" %.2f ",shop.getAccountBalance()));
        creditLimit.setText("Minimum balance required : " + PrefGeneral.getCurrencySymbol(getActivity()) + String.format(" %.2f ", shop.getRt_min_balance()));


        if(shop.getAccountBalance()<shop.getRt_min_balance())
        {
            lowBalanceMessage.setText(getString(R.string.notice_low_balance));
            lowBalanceMessage.setVisibility(View.VISIBLE);

        }
        else
        {
            lowBalanceMessage.setVisibility(View.GONE);
        }

    }






    private void refreshShop()
    {
        viewModelShop.getShopForShopAdmin(false);
        requestCodeGetShop = 4125;
    }




    @OnClick(R.id.header_tutorials)
    void headerTutorialsClick()
    {
        UtilityFunctions.openURL("https://blog.nearbyshops.org/tag/tutorials-for-shop-owners/",getActivity());
    }





    @OnClick(R.id.shop_open_switch)
    void shopSwitchClick()
    {
        if(shopOpenSwitch.isChecked())
        {
//            showToastMessage("Checked !");
            updateShopOpen();
        }
        else
        {
//            showToastMessage("Not checked !");
            updateShopClosed();
        }

    }



    private void updateShopOpen()
    {


        progressSwitch.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = shopService.updateShopOpen(
                PrefLogin.getAuthorizationHeaders(getActivity())
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }


                if(response.code()==200)
                {
//                    showToastMessage("Shop Open !");

                    Shop shop = PrefShopAdminHome.getShop(getActivity());
                    shop.setOpen(true);
                    PrefShopAdminHome.saveShop(shop,getActivity());

                    bindShopOpenStatus();
                }
                else
                {
                    showToastMessage("Failed Code : "  + response.code());
                    shopOpenSwitch.setChecked(!shopOpenSwitch.isChecked());
                }


                progressSwitch.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(!isVisible())
                {
                    return;
                }

                showToastMessage("Failed : Check you Network Connection !");
                shopOpenSwitch.setChecked(!shopOpenSwitch.isChecked());
                progressSwitch.setVisibility(View.GONE);
            }
        });

    }




    private void updateShopClosed()
    {


        progressSwitch.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = shopService.updateShopClosed(
                PrefLogin.getAuthorizationHeaders(getActivity())
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }


                if(response.code()==200)
                {
//                    showToastMessage("Shop Closed !");


                    Shop shop = PrefShopAdminHome.getShop(getActivity());
                    shop.setOpen(false);
                    PrefShopAdminHome.saveShop(shop,getActivity());

                    bindShopOpenStatus();
                }
                else
                {
                    showToastMessage("Failed Code : "  + response.code());
                    shopOpenSwitch.setChecked(!shopOpenSwitch.isChecked());
                }


                progressSwitch.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(!isVisible())
                {
                    return;
                }

                showToastMessage("Failed : Check you Network Connection !");
                shopOpenSwitch.setChecked(!shopOpenSwitch.isChecked());
                progressSwitch.setVisibility(View.GONE);
            }
        });

    }






    private ViewModelShop viewModelShop;
    private int requestCodeGetShop = 0;


    private void setupViewModel()
    {

        viewModelShop = new ViewModelShop(MyApplication.application);


        viewModelShop.getShopLive().observe(getViewLifecycleOwner(), new Observer<Shop>() {
            @Override
            public void onChanged(Shop shop) {


                swipeContainer.setRefreshing(false);


                PrefShopAdminHome.saveShop(shop,getActivity());

                if(requestCodeGetShop ==4125)
                {
                    bindAllFields();
                    UtilityFunctions.updateFirebaseSubscriptionsForShop();
                }
                else if(requestCodeGetShop==4130)
                {
                    Intent intent = new Intent(getActivity(), ShopDashboard.class);
                    startActivity(intent);
                }
                else if(requestCodeGetShop==4150)
                {
                    // Open Edit fragment in edit mode
                    Intent intent = new Intent(getActivity(), EditShop.class);
                    intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE);
                    startActivity(intent);
                }


            }
        });



        viewModelShop.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {


                swipeContainer.setRefreshing(false);

                if(integer==ViewModelShop.EVENT_SHOP_NOT_CREATED)
                {
                    if(requestCodeGetShop==4150)
                    {
                        Intent intent = new Intent(getActivity(), EditShop.class);
                        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_ADD);
                        startActivity(intent);
                    }
                }


            }
        });


        viewModelShop.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage(s);

            }
        });

    }


}
