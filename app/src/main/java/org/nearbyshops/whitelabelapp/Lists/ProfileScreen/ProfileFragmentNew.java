package org.nearbyshops.whitelabelapp.Lists.ProfileScreen;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory.OrderHistory;
import org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory.OrdersHistoryFragment;
import org.nearbyshops.whitelabelapp.Interfaces.LocationUpdated;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyAboutLogin;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.CartAndOrder.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.whitelabelapp.LaunchActivity;
import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings;
import org.nearbyshops.whitelabelapp.Services.GetAppSettings;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.FavouriteShopEndpoint;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Services.LocationService;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.PlacePickers.PlacePickerGoogleMaps.GooglePlacePicker;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUserProfile.Model.RoleDashboardMarker;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderCreateShop;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderMarketHelpline;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderPoweredBy;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderSignIn;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileFragmentNew extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        NotifySort, NotifySearch, LocationUpdated, ViewHolderSignIn.VHSignIn,
        ViewHolderEmptyScreenListItem.ListItemClick, ViewHolderSetLocationManually.ListItemClick ,
        NotifyAboutLogin, ViewHolderButton.ListItemClick{





    private Adapter adapter;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    public List<Object> dataset = new ArrayList<>();

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


            if(getActivity() instanceof SetToolbarText)
            {
                ((SetToolbarText) getActivity()).setToolbar(true,"Profile",false,null);
            }

        return rootView;
    }







    private void setupLocalBroadcastManager()
    {


        IntentFilter filter = new IntentFilter();

        filter.addAction(GetAppSettings.INTENT_ACTION_MARKET_CONFIG_FETCHED);
        filter.addAction(LocationService.INTENT_ACTION_LOCATION_UPDATED);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if(getActivity()!=null)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
    }





    void loadData()
    {

        dataset.clear();


        dataset.add(new ViewHolderMarketHelpline.MarketHelplineData());


        if(PrefGeneral.getServerURL(getActivity())!=null)
        {

            User userLocal = PrefLogin.getUser(getActivity());


            if(userLocal!=null)
            {

                if(userLocal.getRole()==User.ROLE_END_USER_CODE)
                {
                    if(!getResources().getBoolean(R.bool.single_vendor_mode_enabled)
                            && getResources().getBoolean(R.bool.show_create_shop_in_profile_screen))
                    {
                        dataset.add(new ViewHolderCreateShop.CreateShopData());
                    }
                }
                else
                {

                    if((requireActivity().getResources().getInteger(R.integer.app_type)
                            ==requireActivity().getResources().getInteger(R.integer.app_type_main_app)) &&
                            getResources().getBoolean(R.bool.role_switcher_enabled))
                    {
                        dataset.add(new RoleDashboardMarker());
                    }
                }


                dataset.add(userLocal);

//                if(getResources().getInteger(R.integer.app_type)==getResources().getInteger(R.integer.app_type_main_app))
//                {
//                    dataset.add(new FavouriteShopEndpoint());
//                }


            }
            else
            {
                dataset.add(new ViewHolderSignIn.SignInMarker());
            }
        }





        // do not show these options except when using the app as customer
        if(getResources().getInteger(R.integer.app_type)==getResources().getInteger(R.integer.app_type_main_app))
        {
            dataset.add(new ViewHolderSetLocationManually.SetLocationManually());

            if(PrefLogin.getUser(getActivity())!=null)
            {
                dataset.add(new ViewHolderButton.ButtonData("Delivery Addresses",R.drawable.ic_location,6));
                dataset.add(new ViewHolderButton.ButtonData("Order History",R.drawable.ic_receipt_black_24dp,15));
            }
        }




        dataset.add(new ViewHolderButton.ButtonData("Share App",R.drawable.ic_round_share_24,7));
        dataset.add(new ViewHolderButton.ButtonData("Rate " + UtilityFunctions.getAppName(requireContext()) + " app",R.drawable.ic_round_star_24,8));
        dataset.add(new ViewHolderButton.ButtonData("FAQs",R.drawable.ic_contact_support_24,4));
        dataset.add(new ViewHolderButton.ButtonData("Terms of Service",R.drawable.ic_account_box_black_24px,2));
        dataset.add(new ViewHolderButton.ButtonData("Privacy Policy",R.drawable.ic_supervisor_account_black_24px,3));



//        && launchScreen==PrefAppSettings.LAUNCH_SCREEN_MAIN
        if(PrefLogin.getUser(getActivity())!=null )
        {
            dataset.add(new ViewHolderButton.ButtonData("Log Out",R.drawable.ic_delete_black_48px,5));
        }



        if(getResources().getBoolean(R.bool.show_powered_by_nbs))
        {
            dataset.add(new ViewHolderPoweredBy.PoweredByData());
        }


        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }




    @Override
    public void buttonClick(ViewHolderButton.ButtonData data) {

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
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Please checkout "
                    + UtilityFunctions.getAppName(requireContext()) + " app download using this link " + UtilityFunctions.getAppLink(requireContext()));

            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Share " + UtilityFunctions.getAppName(requireContext()));
            startActivity(shareIntent);

        }
        else if(data.getRequestCode()==8)
        {
            rateTheApp();
        }
        else if(data.getRequestCode()==15)
        {
            startActivity(OrderHistory.getLaunchIntent(OrdersHistoryFragment.MODE_END_USER,getActivity()));
        }
    }






    void rateTheApp()
    {
        String url = UtilityFunctions.getAppLink(requireContext());

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }




    private void makeRefreshNetworkCall()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                if(getActivity()!=null)
                {
                    swipeContainer.setRefreshing(true);
                    onRefresh();
                }
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

//                        if(getResources().getInteger(R.integer.app_type)==getResources().getInteger(R.integer.app_type_market_admin_app))
//                        {
                            requireActivity().finish();
                            startActivity(new Intent(requireContext(),LaunchActivity.class));
//                        }

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
                PrefLocation.saveLatLonSelected(data.getDoubleExtra("lat_dest",0.0),data.getDoubleExtra("lon_dest",0.0),
                        getActivity());

                PrefLocation.setLocationSetByUser(true,getActivity());
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
    public void listItemButtonClick(String url) {
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
            intent.putExtra("lat_dest",PrefLocation.getLatitudeSelected(getActivity()));
            intent.putExtra("lon_dest",PrefLocation.getLongitudeSelected(getActivity()));
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

