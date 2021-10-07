package org.nearbyshops.whitelabelapp.Lists.UsersList;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.API.UserService;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditStaffPermissions.EditStaffPermissions;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditStaffPermissions.EditStaffPermissionsFragment;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Lists.UsersList.Dialogs.Deprecated.AddUserToShopStaffDialog;
import org.nearbyshops.whitelabelapp.Lists.UsersList.Dialogs.Deprecated.AddUserToStaffDialog;
import org.nearbyshops.whitelabelapp.Lists.UsersList.Dialogs.AddUserToStaffDialogNew;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.UserEndpoint;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShopStaffPermissions.EditShopStaffPermissions;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShopStaffPermissions.EditShopStaffPermissionsFragment;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterUsers;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.ViewHolderFilterUsers;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUserProfile.ViewHolderUserProfileItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 14/6/17.
 */

public class UsersListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderUserProfileItem.ListItemClick, ViewHolderFilterUsers.ListItemClick, NotifySearch
{



    public static final String USER_MODE_INTENT_KEY = "user_mode_key";


//    public static final int MODE_SUPER_ADMIN_USER_LIST = 50;

    public static final int MODE_ADMIN_USER_LIST = 51;
    public static final int MODE_ADMIN_STAFF_LIST = 52;
    public static final int MODE_ADMIN_DELIVERY_STAFF_LIST = 53; // for SM

//    public static final int MODE_MARKET_ADMIN_DELIVERY_STAFF_LIST = 53;
//    public static final int MODE_MARKET_ADMIN_STAFF_LIST = 54;

    public static final int MODE_SHOP_ADMIN_SHOP_STAFF_LIST = 55;
    public static final int MODE_SHOP_ADMIN_DELIVERY_STAFF_LIST = 56;
    public static final int MODE_SELECT_DELIVERY_PERSON = 57;



    private int current_mode;



    private boolean isDestroyed = false;


    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;


    @Inject
    UserService userService;


    @BindView(R.id.fab) FloatingActionButton fab;


    @Inject
    Gson gson;


    private GridLayoutManager layoutManager;
    private Adapter listAdapter;

    private ArrayList<Object> dataset = new ArrayList<>();


    // flags
    private boolean clearDataset = false;


    private int limit = 10;
    private int offset = 0;
    public int item_count = 0;



    public UsersListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_users_list, container, false);
        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        toolbar.setTitle("Users List");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        current_mode = getActivity().getIntent().getIntExtra(USER_MODE_INTENT_KEY,MODE_ADMIN_USER_LIST);



        if(current_mode==MODE_SELECT_DELIVERY_PERSON)
        {
            ViewHolderFilterUsers.saveFilterByRole(getActivity(),User.ROLE_DELIVERY_GUY_SHOP_CODE);
            fab.setVisibility(View.GONE);
        }
        else if(current_mode==MODE_ADMIN_USER_LIST)
        {
            // clear filter
            ViewHolderFilterUsers.saveFilterByRole(getActivity(),0);
            fab.setVisibility(View.GONE);
        }
        else if(current_mode==MODE_ADMIN_STAFF_LIST)
        {
            ViewHolderFilterUsers.saveFilterByRole(getActivity(),User.ROLE_STAFF_CODE);
        }
        else if(current_mode==MODE_ADMIN_DELIVERY_STAFF_LIST)
        {
            ViewHolderFilterUsers.saveFilterByRole(getActivity(),User.ROLE_DELIVERY_GUY_MARKET_CODE);
        }
        else if(current_mode==MODE_SHOP_ADMIN_SHOP_STAFF_LIST)
        {
            ViewHolderFilterUsers.saveFilterByRole(getActivity(),User.ROLE_SHOP_STAFF_CODE);
        }
        else if(current_mode==MODE_SHOP_ADMIN_DELIVERY_STAFF_LIST)
        {
            ViewHolderFilterUsers.saveFilterByRole(getActivity(),User.ROLE_DELIVERY_GUY_SHOP_CODE);
        }





        setupSwipeContainer();
        setupRecyclerView();

        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }



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








    private void setupRecyclerView()
    {

        listAdapter = new Adapter(dataset,getActivity(),this,current_mode);
        recyclerView.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    if(offset + limit > layoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }


                    // trigger fetch next page

                    if((offset + limit)<= item_count)
                    {
                        offset = offset + limit;

                        getUsers();
                    }


                }
            }
        });

    }








    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
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







    @Override
    public void onRefresh() {

        clearDataset = true;
        getUsers();
    }






    private void getUsers()
    {

        if(clearDataset)
        {
            offset = 0;
        }






        Integer userRole = null;

        int role = ViewHolderFilterUsers.getFilterByRole(getActivity());

        if(role!=0)
        {
            userRole = role;
        }






        Call<UserEndpoint> call = null;


        User user = PrefLogin.getUser(getActivity());


        if(user ==null)
        {
            swipeContainer.setRefreshing(false);
            showToastMessage("User null !");
            return;
        }



        call = userService.getUsers(
                PrefLogin.getAuthorizationHeader(getActivity()),
                userRole,
                null,
                searchQuery,
                ViewHolderFilterUsers.getSortString(getActivity()),
                limit, offset,
                clearDataset,false
        );



        call.enqueue(new Callback<UserEndpoint>() {
            @Override
            public void onResponse(Call<UserEndpoint> call, Response<UserEndpoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200 && response.body()!=null) {


                    if (clearDataset) {
                        dataset.clear();
                        clearDataset = false;

                        item_count = response.body().getItemCount();



                        if(current_mode==MODE_ADMIN_USER_LIST)
                        {
                            dataset.add(new FilterUsers());
                        }



                        if(item_count>0)
                        {
                            dataset.add(new ViewHolderHeader.HeaderTitle("Users List : " + item_count + " ( Total )"));
                        }
                    }



                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
                    }



                    if(offset + limit >= item_count)
                    {
                        listAdapter.setLoadMore(false);
                    }
                    else
                    {
                        listAdapter.setLoadMore(true);
                    }
                }
                else
                {
                    showToastMessage("Failed Code : " + response.code());
                }



//                showToastMessage("Item Count : " + item_count);

                if(item_count==0)
                {
                    dataset.add(getEmptyScreen());
                }



                listAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UserEndpoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                swipeContainer.setRefreshing(false);


                dataset.clear();
                dataset.add(ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.getOffline());
                listAdapter.notifyDataSetChanged();

            }
        });


    }






    private ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen getEmptyScreen()
    {

        if(current_mode==MODE_ADMIN_USER_LIST)
        {
            return ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyUsersList();
        }
        else if (current_mode==MODE_ADMIN_STAFF_LIST)
        {
            return ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenStaffList();
        }
        else if(current_mode==MODE_ADMIN_DELIVERY_STAFF_LIST)
        {
            return ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenDeliveryStaff();
        }
        else if(current_mode==MODE_SHOP_ADMIN_SHOP_STAFF_LIST)
        {
            return ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenShopStaffList();
        }
        else if(current_mode==MODE_SHOP_ADMIN_DELIVERY_STAFF_LIST)
        {
            return ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenDeliveryStaff();
        }
        else if(current_mode==MODE_SELECT_DELIVERY_PERSON)
        {
            return ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyScreenDeliveryStaff();
        }



        return ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen.emptyUsersList();
    }






    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }






    @Override
    public void userDeleted(User user, int position) {

        listAdapter.notifyItemRemoved(position);
    }


    @Override
    public void listItemClick(User user, int position) {



        if(current_mode==MODE_SELECT_DELIVERY_PERSON)
        {
            Intent data = new Intent();
            data.putExtra("delivery_guy_id",user.getUserID());
            getActivity().setResult(456,data);

            getActivity().finish();
        }
        else if(current_mode==MODE_ADMIN_USER_LIST)
        {

            Gson gson = UtilityFunctions.provideGson();
            String jsonString = gson.toJson(user);


            Intent intent = new Intent(getActivity(), EditProfile.class);
            intent.putExtra("user_profile",jsonString);
            intent.putExtra(FragmentEditProfile.EDIT_MODE_INTENT_KEY, FragmentEditProfile.MODE_UPDATE_BY_ADMIN);
            startActivity(intent);

        }
        else if(current_mode==MODE_ADMIN_STAFF_LIST)
        {
            if(user.getRole()==User.ROLE_STAFF_CODE)
            {
                Intent intent = new Intent(getActivity(), EditStaffPermissions.class);
                intent.putExtra(EditStaffPermissionsFragment.EDIT_MODE_INTENT_KEY, EditStaffPermissionsFragment.MODE_UPDATE);
                intent.putExtra(EditStaffPermissionsFragment.STAFF_ID_INTENT_KEY,user.getUserID());
                startActivity(intent);
            }

        }
        else if(current_mode==MODE_SHOP_ADMIN_SHOP_STAFF_LIST)
        {

            if(user.getRole()==User.ROLE_SHOP_STAFF_CODE)
            {
                Intent intent = new Intent(getActivity(), EditShopStaffPermissions.class);
                intent.putExtra(EditShopStaffPermissionsFragment.EDIT_MODE_INTENT_KEY, EditShopStaffPermissionsFragment.MODE_UPDATE);
                intent.putExtra(EditShopStaffPermissionsFragment.STAFF_ID_INTENT_KEY,user.getUserID());
                startActivity(intent);
            }
        }

    }



    @OnClick(R.id.fab)
    void fabClick()
    {
        FragmentManager fm = getChildFragmentManager();
        AddUserToStaffDialogNew dialog = new AddUserToStaffDialogNew();

        if(current_mode==MODE_ADMIN_STAFF_LIST)
        {
            dialog.setRole(User.ROLE_STAFF_CODE);
            dialog.setScreenMode(AddUserToStaffDialogNew.MODE_ADMIN);
        }
        else if(current_mode==MODE_ADMIN_DELIVERY_STAFF_LIST)
        {
            dialog.setRole(User.ROLE_DELIVERY_GUY_MARKET_CODE);
            dialog.setScreenMode(AddUserToStaffDialogNew.MODE_ADMIN);
        }
        else if(current_mode==MODE_SHOP_ADMIN_SHOP_STAFF_LIST)
        {
            dialog.setRole(User.ROLE_SHOP_STAFF_CODE);
            dialog.setScreenMode(AddUserToStaffDialogNew.MODE_SHOP_ADMIN);
        }
        else if(current_mode==MODE_SHOP_ADMIN_DELIVERY_STAFF_LIST)
        {
            dialog.setRole(User.ROLE_DELIVERY_GUY_SHOP_CODE);
            dialog.setScreenMode(AddUserToStaffDialogNew.MODE_SHOP_ADMIN);
        }


        dialog.show(fm, "add_user_to_shop_staff");
    }



    void fabClickBackup()
    {

        if(current_mode==MODE_SHOP_ADMIN_SHOP_STAFF_LIST)
        {
            FragmentManager fm = getChildFragmentManager();
            AddUserToShopStaffDialog dialog = new AddUserToShopStaffDialog();

            dialog.setSelectedRole(User.ROLE_SHOP_STAFF_CODE);
            dialog.show(fm, "add_user_to_shop_staff");
        }
        else if(current_mode==MODE_SHOP_ADMIN_DELIVERY_STAFF_LIST)
        {

            FragmentManager fm = getChildFragmentManager();
            AddUserToShopStaffDialog dialog = new AddUserToShopStaffDialog();

            dialog.setSelectedRole(User.ROLE_DELIVERY_GUY_SHOP_CODE);
            dialog.show(fm, "add_user_to_shop_staff");
        }
        else if(current_mode==MODE_ADMIN_STAFF_LIST)
        {
            FragmentManager fm = getChildFragmentManager();
            AddUserToStaffDialog dialog = new AddUserToStaffDialog();

            dialog.show(fm, "add_user_to_staff");
            dialog.setRole(User.ROLE_STAFF_CODE);
        }
        else if(current_mode==MODE_ADMIN_DELIVERY_STAFF_LIST)
        {
            FragmentManager fm = getChildFragmentManager();
            AddUserToStaffDialog dialog = new AddUserToStaffDialog();

            dialog.show(fm, "add_user_to_staff");
            dialog.setRole(User.ROLE_DELIVERY_GUY_MARKET_CODE);
        }
    }





    @Override
    public void filtersUpdated() {
        makeRefreshNetworkCall();
    }



    private String searchQuery = null;
    @Override
    public void search(String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }
}
