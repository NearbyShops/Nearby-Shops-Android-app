package org.nearbyshops.enduserappnew.EditDataScreens.EditShopStaffPermissions;

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

import org.nearbyshops.enduserappnew.API.ShopStaffService;
import org.nearbyshops.enduserappnew.Model.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditShopStaffPermissionsFragment extends Fragment {





    public static final String STAFF_ID_INTENT_KEY = "staff_id_intent_key";


    @Inject
    ShopStaffService shopStaffService;

    @BindView(R.id.saveButton) TextView updateDeliveryAddress;
    @BindView(R.id.progress_bar) ProgressBar progressBar;



    // address Fields
    @BindView(R.id.designation) EditText designation;

    @BindView(R.id.permit_add_remove_items) CheckBox permitAddRemoveItems;
    @BindView(R.id.permit_update_items_in_shop) CheckBox permitUpdateItemsInShop;
    @BindView(R.id.permit_cancel_orders) CheckBox permitCancelOrders;

    @BindView(R.id.permit_confirm_orders) CheckBox permitConfirmOrders;
    @BindView(R.id.permit_set_orders_packed) CheckBox permitSetOrdersPacked;
    @BindView(R.id.permit_handover_to_delivery) CheckBox permitHandoverToDelivery;
//    @BindView(R.id.permit_mark_orders_delivered) CheckBox permitMarkOrdersDelivered;
    @BindView(R.id.permit_accept_payments) CheckBox permitAcceptPayments;
    @BindView(R.id.permit_accept_returns) CheckBox permitAcceptReturns;




    private int staffID;
    private ShopStaffPermissions permissions;



    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;


    private int current_mode = MODE_ADD;



    public EditShopStaffPermissionsFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_shop_staff_permissions, container, false);
        ButterKnife.bind(this,rootView);

//        setContentView(R.layout.activity_edit_address);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);



        if(current_mode ==MODE_UPDATE)
        {
            staffID = getActivity().getIntent().getIntExtra(STAFF_ID_INTENT_KEY,0);
            getStaffPermissions();
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
                    actionBar.setTitle("Add Permissions");
                }
                else if(current_mode==MODE_UPDATE)
                {
                    actionBar.setTitle("Edit Permissions");
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





    private void getStaffPermissions()
    {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please with ... Getting user details !");
        pd.show();


        Call<ShopStaffPermissions> call = shopStaffService.getUserDetails(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                staffID
        );



        call.enqueue(new Callback<ShopStaffPermissions>() {
            @Override
            public void onResponse(Call<ShopStaffPermissions> call, Response<ShopStaffPermissions> response) {
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
            public void onFailure(Call<ShopStaffPermissions> call, Throwable t) {
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
            permissions = new ShopStaffPermissions();
        }


        permissions.setDesignation(designation.getText().toString());

        permissions.setPermitAddRemoveItems(permitAddRemoveItems.isChecked());
        permissions.setPermitUpdateItemsInShop(permitUpdateItemsInShop.isChecked());
        permissions.setPermitCancelOrders(permitCancelOrders.isChecked());

        permissions.setPermitConfirmOrders(permitConfirmOrders.isChecked());
        permissions.setPermitSetOrdersPacked(permitSetOrdersPacked.isChecked());
        permissions.setPermitHandoverToDelivery(permitHandoverToDelivery.isChecked());

//        permissions.setPermitMarkOrdersDelivered(permitMarkOrdersDelivered.isChecked());
        permissions.setPermitAcceptPaymentsFromDelivery(permitAcceptPayments.isChecked());
        permissions.setPermitAcceptReturns(permitAcceptReturns.isChecked());

        permissions.setDesignation(designation.getText().toString());

    }





    private void bindDataToViews()
    {
        if(permissions !=null)
        {
            designation.setText(permissions.getDesignation());

            permitAddRemoveItems.setChecked(permissions.isPermitAddRemoveItems());
            permitUpdateItemsInShop.setChecked(permissions.isPermitUpdateItemsInShop());
            permitCancelOrders.setChecked(permissions.isPermitCancelOrders());

            permitConfirmOrders.setChecked(permissions.isPermitConfirmOrders());
            permitSetOrdersPacked.setChecked(permissions.isPermitSetOrdersPacked());
            permitHandoverToDelivery.setChecked(permissions.isPermitHandoverToDelivery());

//            permitMarkOrdersDelivered.setChecked(permissions.isPermitMarkOrdersDelivered());
            permitAcceptPayments.setChecked(permissions.isPermitAcceptPaymentsFromDelivery());
            permitAcceptReturns.setChecked(permissions.isPermitAcceptReturns());
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
            addPermissions();
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





    private void addPermissions()
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

        Call<ResponseBody> call = shopStaffService.updateStaffPermissions(
                PrefLogin.getAuthorizationHeaders(getActivity()),
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
