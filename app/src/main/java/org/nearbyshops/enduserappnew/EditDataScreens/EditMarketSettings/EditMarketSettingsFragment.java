package org.nearbyshops.enduserappnew.EditDataScreens.EditMarketSettings;

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

import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelMarket.MarketSettings;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
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

public class EditMarketSettingsFragment extends Fragment {



    private MarketSettings marketSettings;


    @Inject
    ServiceConfigurationService configurationService;




    @BindView(R.id.saveButton) TextView updateDeliveryAddress;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    // setting fields
    @BindView(R.id.email_sender_name) EditText emailSenderName;
    @BindView(R.id.service_name) EditText serviceNameForSMS;

    @BindView(R.id.cod_enabled) CheckBox cashOnDeliveryEnabled;
    @BindView(R.id.pod_enabled) CheckBox payOnDeliveryEnabled;
    @BindView(R.id.razorpay_enabled) CheckBox razorpayEnabled;

    @BindView(R.id.login_header) TextView loginHeader;
    @BindView(R.id.login_using_otp) CheckBox loginUsingOTPEnabled;

    @BindView(R.id.market_fee_pickup) EditText marketFeeForPickup;
    @BindView(R.id.market_fee_delivery) EditText marketFeeForDelivery;

    @BindView(R.id.add_market_fee_to_bill) CheckBox addMarketFeeToBill;
    @BindView(R.id.use_standard_delivery_fee) CheckBox useStandardDeliveryFee;

    @BindView(R.id.delivery_fee_per_order) EditText deliveryFeePerOrder;
    @BindView(R.id.min_account_balance_for_shop) EditText minAccountBalForShop;

    @BindView(R.id.bootstrap_mode_enabled) CheckBox bootstrapModeEnabled;
    @BindView(R.id.demo_mode_enabled) CheckBox demoModeEnabled;








    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;



    public EditMarketSettingsFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_settings, container, false);
        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);


        if(current_mode ==MODE_UPDATE)
        {
            getSettings();
        }



//        applyFiledVisibility();
        setActionBarTitle();

        return rootView;
    }





    void applyFiledVisibility()
    {
        if(PrefGeneral.isMultiMarketEnabled(getActivity()))
        {
            loginHeader.setVisibility(View.GONE);
            loginUsingOTPEnabled.setVisibility(View.GONE);
        }
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
                    actionBar.setTitle("Add Settings");
                }
                else if(current_mode==MODE_UPDATE)
                {
                    actionBar.setTitle("Edit Settings");
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





    private void getSettings() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please with ... Getting Item Category details !");
        pd.show();



        Call<MarketSettings> call = configurationService.getSettings();

        call.enqueue(new Callback<MarketSettings>() {
            @Override
            public void onResponse(Call<MarketSettings> call, Response<MarketSettings> response) {


                pd.dismiss();

                if (response.code() == 200) {
                    marketSettings = response.body();
                    bindDataToViews();

                } else {
                    showToastMessage("Failed : Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MarketSettings> call, Throwable t) {
                showToastMessage("Failed !");
            }
        });


    }






    private void getDataFromViews()
    {
        if(marketSettings ==null)
        {
            marketSettings = new MarketSettings();
        }


        marketSettings.setEmailSenderName(emailSenderName.getText().toString());
        marketSettings.setServiceNameForSMS(serviceNameForSMS.getText().toString());

        marketSettings.setCodEnabled(cashOnDeliveryEnabled.isChecked());
        marketSettings.setPodEnabled(payOnDeliveryEnabled.isChecked());
        marketSettings.setRazorPayEnabled(razorpayEnabled.isChecked());

        marketSettings.setLoginUsingOTPEnabled(loginUsingOTPEnabled.isChecked());

        marketSettings.setMarketFeePickupFromShop((float) Double.parseDouble(marketFeeForPickup.getText().toString()));
        marketSettings.setMarketFeeHomeDelivery((float) Double.parseDouble(marketFeeForDelivery.getText().toString()));

        marketSettings.setAddMarketFeeToBill(addMarketFeeToBill.isChecked());
        marketSettings.setUseStandardDeliveryFee(useStandardDeliveryFee.isChecked());

        marketSettings.setMarketDeliveryFeePerOrder((float) Double.parseDouble(deliveryFeePerOrder.getText().toString()));
        marketSettings.setMinAccountBalanceForShopOwner((float) Double.parseDouble(minAccountBalForShop.getText().toString()));

        marketSettings.setBootstrapModeEnabled(bootstrapModeEnabled.isChecked());
        marketSettings.setDemoModeEnabled(demoModeEnabled.isChecked());
    }


    private void bindDataToViews()
    {
        if(marketSettings !=null)
        {

            emailSenderName.setText(marketSettings.getEmailSenderName());
            serviceNameForSMS.setText(marketSettings.getServiceNameForSMS());

            cashOnDeliveryEnabled.setChecked(marketSettings.isCodEnabled());
            payOnDeliveryEnabled.setChecked(marketSettings.isPodEnabled());
            razorpayEnabled.setChecked(marketSettings.isRazorPayEnabled());

            loginUsingOTPEnabled.setChecked(marketSettings.isLoginUsingOTPEnabled());

            marketFeeForPickup.setText(String.valueOf(marketSettings.getMarketFeePickupFromShop()));
            marketFeeForDelivery.setText(String.valueOf(marketSettings.getMarketFeeHomeDelivery()));

            addMarketFeeToBill.setChecked(marketSettings.isAddMarketFeeToBill());
            useStandardDeliveryFee.setChecked(marketSettings.isUseStandardDeliveryFee());

            deliveryFeePerOrder.setText(String.valueOf(marketSettings.getMarketDeliveryFeePerOrder()));
            minAccountBalForShop.setText(String.valueOf(marketSettings.getMinAccountBalanceForShopOwner()));

            bootstrapModeEnabled.setChecked(marketSettings.isBootstrapModeEnabled());
            demoModeEnabled.setChecked(marketSettings.isUseStandardDeliveryFee());

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

        }
        else if(current_mode == MODE_UPDATE)
        {
            updateSettings();
        }

    }







    private boolean validateData()
    {
        boolean isValid = true;




        return isValid;
    }









    private void updateSettings()
    {
        getDataFromViews();

        Call<ResponseBody> call = configurationService.updateSettings(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                marketSettings
        );


        progressBar.setVisibility(View.VISIBLE);
        updateDeliveryAddress.setVisibility(View.INVISIBLE);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                }
                else
                {
                    showToastMessage("failed to update !");
                }



                progressBar.setVisibility(View.INVISIBLE);
                updateDeliveryAddress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
