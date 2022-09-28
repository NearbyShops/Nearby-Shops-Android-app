package org.nearbyshops.whitelabelapp.EditDataScreens.EditStaffPermissions;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.nearbyshops.whitelabelapp.API.StaffService;
import org.nearbyshops.whitelabelapp.Model.ModelStaff.StaffPermissions;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditStaffPermissionsFragment extends Fragment {




    public static final String STAFF_ID_INTENT_KEY = "staff_id_intent_key";


    @Inject
    StaffService staffService;



    @BindView(R.id.saveButton) TextView updateDeliveryAddress;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    // address Fields
    @BindView(R.id.designation) EditText designation;

    @BindView(R.id.permit_create_update_item_cat) CheckBox permitCreateUpdateItemCat;
    @BindView(R.id.permit_create_update_items) CheckBox permitCreateUpdateItems;
    @BindView(R.id.permit_enable_shops) CheckBox permitEnableShops;



    private int staffID;
    private StaffPermissions permissions;




    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;



    public EditStaffPermissionsFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_staff_permissions, container, false);
        ButterKnife.bind(this,rootView);

//        setContentView(R.layout.activity_edit_address);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);




        if(current_mode ==MODE_UPDATE)
        {

            staffID = getActivity().getIntent().getIntExtra(STAFF_ID_INTENT_KEY,0);
            getPermissions();
        }


        setActionBarTitle();

        return rootView;
    }











    private void setActionBarTitle()
    {
        if(getActivity() instanceof AppCompatActivity)
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

            if(actionBar!=null)
            {
                if(current_mode == MODE_ADD)
                {
                    actionBar.setTitle("Add Staff Permissions");
                }
                else if(current_mode==MODE_UPDATE)
                {
                    actionBar.setTitle("Edit Staff Permissions");
                }

            }
        }


        if(current_mode==MODE_ADD)
        {
            updateDeliveryAddress.setText("Add");
        }
        else if(current_mode==MODE_UPDATE)
        {
            updateDeliveryAddress.setText("Save");
        }


    }




    private void getPermissions()
    {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.loading_message));
        pd.show();


        Call<StaffPermissions> call = staffService.getPermissionDetails(
                PrefLogin.getAuthorizationHeader(getActivity()),
                staffID
        );


        call.enqueue(new Callback<StaffPermissions>() {
            @Override
            public void onResponse(Call<StaffPermissions> call, Response<StaffPermissions> response) {
                if(!isVisible())
                {
                    return;
                }

                pd.dismiss();



                if(response.code()==200)
                {
                    permissions = response.body();

                    bindDataToViews();
                }
                else
                {
                    showToastMessage("Failed to get User Details : Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<StaffPermissions> call, Throwable t) {
                if(!isVisible())
                {
                    return;
                }

                showToastMessage("Failed !");
            }
        });



    }







    private void getDataFromViews()
    {
        if(permissions ==null)
        {
            permissions = new StaffPermissions();
        }


        permissions.setDesignation(designation.getText().toString());
        permissions.setPermitCreateUpdateItemCat(permitCreateUpdateItemCat.isChecked());
        permissions.setPermitCreateUpdateItems(permitCreateUpdateItems.isChecked());
        permissions.setPermitApproveShops(permitEnableShops.isChecked());
    }





    private void bindDataToViews()
    {
        if(permissions!=null)
        {
            designation.setText(permissions.getDesignation());

            permitCreateUpdateItemCat.setChecked(permissions.isPermitCreateUpdateItemCat());
            permitCreateUpdateItems.setChecked(permissions.isPermitCreateUpdateItems());
            permitEnableShops.setChecked(permissions.isPermitApproveShops());
        }
    }







    @OnClick(R.id.saveButton)
    void updateAddressClick(View view)
    {

        if(!validateData())
        {
            return;
        }

        if(current_mode == MODE_ADD)
        {
            addDeliveryAddress();
        }
        else if(current_mode == MODE_UPDATE)
        {
            updatePermissions();
        }

    }







    private boolean validateData()
    {
        boolean isValid = true;



        return isValid;
    }








    private void addDeliveryAddress()
    {
        if(PrefLogin.getUser(getActivity())==null)
        {
            showToastMessage("Please login to use this feature !");
            return;
        }

        getDataFromViews();

        progressBar.setVisibility(View.VISIBLE);
        updateDeliveryAddress.setVisibility(View.INVISIBLE);
    }







    private void updatePermissions()
    {
        getDataFromViews();


        permissions.setStaffUserID(staffID);


        Call<ResponseBody> call = staffService.updateStaffPermissions(
                PrefLogin.getAuthorizationHeader(getActivity()),
                permissions
        );



        progressBar.setVisibility(View.VISIBLE);
        updateDeliveryAddress.setVisibility(View.INVISIBLE);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(!isVisible())
                {
                    return;
                }


                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                }
                else
                {
                    showToastMessage("failed Code : " + response.code());
                }



                progressBar.setVisibility(View.INVISIBLE);
                updateDeliveryAddress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(!isVisible())
                {
                    return;
                }

                showToastMessage("Network connection failed !");



                progressBar.setVisibility(View.INVISIBLE);
                updateDeliveryAddress.setVisibility(View.VISIBLE);
            }
        });
    }





    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }



}
