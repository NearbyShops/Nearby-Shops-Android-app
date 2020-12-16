package org.nearbyshops.enduserappnew.Lists.ProfileScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.enduserappnew.Interfaces.LocationUpdated;
import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Lists.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.MarketHelplineData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.PoweredByData;
import org.nearbyshops.enduserappnew.mfiles.Markets.MarketsList;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.FavouriteShopEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Services.LocationService;
import org.nearbyshops.enduserappnew.Services.UpdateServiceConfiguration;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile.Model.RoleDashboardMarker;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.CreateShopData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.ButtonData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SetLocationManually;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SignInMarker;
import org.nearbyshops.enduserappnew.mfiles.SwitchMarketData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderButton;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSignIn;
import org.nearbyshops.enduserappnew.mfiles.ViewHolderSwitchMarket;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileFragmentNew extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        NotifySort, NotifySearch, LocationUpdated, ViewHolderSignIn.VHSignIn,
        ViewHolderEmptyScreenListItem.ListItemClick, ViewHolderSetLocationManually.ListItemClick ,
        NotifyAboutLogin, ViewHolderButton.ListItemClick, ViewHolderSwitchMarket.ListItemClick {





    private Adapter adapter;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    public List<Object> dataset = new ArrayList<>();



    @BindView(R.id.service_name) TextView serviceName;



    private ViewModelUser viewModelUser;




    public static ProfileFragmentNew newInstance() {
        ProfileFragmentNew fragment = new ProfileFragmentNew();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


            setRetainInstance(true);
            View rootView = inflater.inflate(R.layout.fragment_profile_new, container, false);
            ButterKnife.bind(this,rootView);



            if(savedInstanceState==null)
            {
                makeRefreshNetworkCall();
            }


            setupRecyclerView();
            setupSwipeContainer();
            setupViewModel();

            setupLocalBroadcastManager();






//            if(!PrefLocation.isLocationSetByUser(getActivity()) && getActivity()!=null)
//            {
//                getActivity().startService(new Intent(getActivity(), LocationService.class));
//            }





            if(PrefServiceConfig.getServiceName(getActivity())!=null) {

                serviceName.setVisibility(View.VISIBLE);
                serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
            }



        return rootView;
    }







    private void setupLocalBroadcastManager()
    {


        IntentFilter filter = new IntentFilter();

        filter.addAction(UpdateServiceConfiguration.INTENT_ACTION_MARKET_CONFIG_FETCHED);
        filter.addAction(LocationService.INTENT_ACTION_LOCATION_UPDATED);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if(getActivity()!=null)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            serviceName.setVisibility(View.VISIBLE);
                            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));

                            makeRefreshNetworkCall();

                        }
                    });
                }


            }
        },filter);
    }






    private void setupViewModel()
    {

        if(viewModelUser==null)
        {
            viewModelUser = new ViewModelUser(MyApplication.application);
        }



        viewModelUser.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if(integer == ViewModelUser.EVENT_profile_fetched)
                {
                    adapter.notifyDataSetChanged();
                }

            }
        });




        viewModelUser.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                showToastMessage(s);
            }
        });



    }



    private void setupSwipeContainer()
    {
        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }





    private void setupRecyclerView()
    {

        adapter = new Adapter(dataset,this);
        recyclerView.setAdapter(adapter);

        adapter.setLoadMore(false);



        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }




    @Override
    public void onRefresh() {

        loadData();

        viewModelUser.getUserProfile();


//        if(!PrefGeneral.isMultiMarketEnabled(getActivity()))
//        {
//
//        }
    }





    void loadData()
    {

        dataset.clear();


        if(PrefGeneral.isMultiMarketEnabled(getActivity()))
        {
            dataset.add(new SwitchMarketData());
        }


        dataset.add(new MarketHelplineData());


        if(PrefGeneral.getServiceURL(getActivity())!=null)
        {

            User userLocal = PrefLogin.getUser(getActivity());


            if(userLocal!=null)
            {

                if(userLocal.getRole()==User.ROLE_END_USER_CODE)
                {
                    if(!getResources().getBoolean(R.bool.single_vendor_mode_enabled)
                            && getResources().getBoolean(R.bool.show_create_shop_in_profile_screen))
                    {
                        dataset.add(new CreateShopData());
                    }
                }
                else
                {
                    dataset.add(new RoleDashboardMarker());
                }


                dataset.add(userLocal);

            }
            else
            {
                dataset.add(new SignInMarker());
            }
        }




        if(PrefLogin.getUser(getActivity())!=null && !getResources().getBoolean(R.bool.single_vendor_mode_enabled)
                && !PrefGeneral.isMultiMarketEnabled(getActivity()))
        {
            dataset.add(new FavouriteShopEndpoint());

        }



        dataset.add(new SetLocationManually());



        if(PrefLogin.getUser(getActivity())!=null)
        {
            dataset.add(new ButtonData("Delivery Addresses",R.drawable.ic_location,6));
        }


        dataset.add(new ButtonData("Share App",R.drawable.ic_round_share_24,7));
        dataset.add(new ButtonData("Rate " + getString(R.string.app_name) + " app",R.drawable.ic_round_star_24,8));
        dataset.add(new ButtonData("FAQs",R.drawable.ic_contact_support_24,4));
        dataset.add(new ButtonData("Terms of Service",R.drawable.ic_account_box_black_24px,2));
        dataset.add(new ButtonData("Privacy Policy",R.drawable.ic_supervisor_account_black_24px,3));


        if(PrefLogin.getUser(getActivity())!=null)
        {
            dataset.add(new ButtonData("Log Out",R.drawable.ic_delete_black_48px,5));
        }



        if(getResources().getBoolean(R.bool.show_powered_by_nbs))
        {
            dataset.add(new PoweredByData());
        }


        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }




    @Override
    public void buttonClick(ButtonData data) {

        if(data.getRequestCode()==5)
        {
            logOut();
        }
        else if(data.getRequestCode()==2)
        {
            termsOfServiceClick();
        }
        else if(data.getRequestCode()==3)
        {
            privacyPolicyClick();
        }
        else if(data.getRequestCode()==4)
        {
            faqsClick();
        }
        else if(data.getRequestCode()==6)
        {
            Intent intent = new Intent(getActivity(), DeliveryAddressActivity.class);
            startActivity(intent);
        }
        else if(data.getRequestCode()==7)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Please checkout " + getString(R.string.app_name) + " app download using this link " +
                    getString(R.string.app_download_link));

            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Share " + getString(R.string.app_name));
            startActivity(shareIntent);


        }
        else if(data.getRequestCode()==8)
        {
            rateTheApp();
        }
    }





    void rateTheApp()
    {
        String url = getString(R.string.app_download_link);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }



    private void makeRefreshNetworkCall()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                onRefresh();
            }
        });

    }






    void logOut()
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Confirm Logout !")
                .setMessage("Do you want to log out !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        UtilityFunctions.logout(getActivity());
                        loadData();

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





    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }








    // Refresh the Confirmed PlaceholderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }




    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }






    private String searchQuery = null;



    @Override
    public void search(final String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }








    @Override
    public void permissionGranted() {

    }





    @Override
    public void locationUpdated() {
        makeRefreshNetworkCall();
    }





    @Override
    public void signInClick() {

        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent,123);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123)
        {
            makeRefreshNetworkCall();
        }
        else if(resultCode==405)
        {
            makeRefreshNetworkCall();
        }
        else if(requestCode==3 && resultCode==6)
        {
            if(data!=null)
            {
                PrefLocation.saveLatLon(data.getDoubleExtra("lat_dest",0.0),data.getDoubleExtra("lon_dest",0.0),
                        getActivity());

                PrefLocation.locationSetByUser(true,getActivity());
                makeRefreshNetworkCall();
            }

        }
        else if(requestCode==3 && resultCode==2)
        {

            makeRefreshNetworkCall();

        }
        else if(requestCode==890)
        {
            makeRefreshNetworkCall();
        }
        else if(requestCode==3262 && resultCode ==3121)
        {
            makeRefreshNetworkCall();
        }
    }






    @Override
    public void changeMarketClick() {

        Intent intent = new Intent(getActivity(), MarketsList.class);
        intent.putExtra("is_selection_mode",true);
        startActivityForResult(intent,3262);
    }







    @Override
    public void buttonClick(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }





    @Override
    public void changeLocationClick() {
//        Intent intent = new Intent(getActivity(), PickLocation.class);


        Intent intent = null;

        User user = PrefLogin.getUser(getActivity());

        if(user==null)
        {
//            if(getResources().getBoolean(R.bool.use_google_maps))
//            {
//                intent = new Intent(getActivity(), GooglePlacePicker.class);
//            }
//            else
//            {
//                intent = new Intent(getActivity(), PickLocation.class);
//            }


            intent = new Intent(getActivity(), GooglePlacePicker.class);
            intent.putExtra("lat_dest",PrefLocation.getLatitude(getActivity()));
            intent.putExtra("lon_dest",PrefLocation.getLongitude(getActivity()));
            startActivityForResult(intent,3);

        }
        else
        {
            intent = new Intent(getActivity(), DeliveryAddressActivity.class);
            startActivityForResult(intent,3);
        }

    }






    @Override
    public void loginSuccess() {

    }



    @Override
    public void loggedOut() {

        makeRefreshNetworkCall();
    }




    void faqsClick()
    {

        String url = getString(R.string.faqs_link);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }





    void privacyPolicyClick()
    {
        String url = getString(R.string.privacy_policy_link);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }





    void termsOfServiceClick()
    {
        String url = getString(R.string.tos_link);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }





}

