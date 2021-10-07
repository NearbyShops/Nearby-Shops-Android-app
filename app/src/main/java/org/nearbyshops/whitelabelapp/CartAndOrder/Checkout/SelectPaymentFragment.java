package org.nearbyshops.whitelabelapp.CartAndOrder.Checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.nearbyshops.whitelabelapp.API.RazorPayService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelUtility.PaymentConfig;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectPaymentFragment extends Fragment{



    @BindView(R.id.service_name) TextView serviceName;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    @Inject
    RazorPayService razorPayService;

    PaymentConfig paymentConfig;


    public SelectPaymentFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_select_payment, container, false);
        ButterKnife.bind(this, rootView);



//        if(PrefServiceConfig.getServiceName(getActivity())!=null) {
//            serviceName.setVisibility(View.VISIBLE);
//            serviceName.setText(PrefServiceConfig.getServiceName(getActivity()));
//        }


        getPaymentConfig();


        return rootView;
    }



    void getPaymentConfig()
    {
        Call<PaymentConfig> call = razorPayService.getPaymentConfig(
                PrefLogin.getAuthorizationHeader(getActivity())
        );

        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<PaymentConfig>() {
            @Override
            public void onResponse(Call<PaymentConfig> call, Response<PaymentConfig> response) {

                progressBar.setVisibility(View.GONE);

                if(response.code()==200)
                {
                    SelectPaymentFragment.this.paymentConfig = response.body();
                    bindPaymentConfig();

                }
            }

            @Override
            public void onFailure(Call<PaymentConfig> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
            }
        });
    }


    void bindPaymentConfig()
    {
        if(paymentConfig==null)
        {
            return;
        }


        if(paymentConfig.isCashOnDeliveryEnabled())
        {
            cashOnDelivery.setVisibility(View.VISIBLE);
        }
        else
        {
            cashOnDelivery.setVisibility(View.GONE);
        }

        if(paymentConfig.isPayOnlineOnDeliveryEnabled())
        {
            payOnlineOnDelivery.setVisibility(View.VISIBLE);
        }
        else
        {
            payOnlineOnDelivery.setVisibility(View.GONE);
        }


        if(paymentConfig.isRazorPayEnabled())
        {
            payUsingRazorPay.setVisibility(View.VISIBLE);
        }
        else
        {
            payUsingRazorPay.setVisibility(View.GONE);
        }


    }


        @BindView(R.id.cash_on_delivery) TextView cashOnDelivery;
        @BindView(R.id.pay_online_on_delivery) TextView payOnlineOnDelivery;
        @BindView(R.id.payment_razorpay) TextView payUsingRazorPay;


        @OnClick(R.id.cash_on_delivery)
        void codClick()
        {
            finishSelection(Order.PAYMENT_MODE_CASH_ON_DELIVERY);
        }


        @OnClick(R.id.pay_online_on_delivery)
        void payOnlineOnDelivery()
        {
            finishSelection(Order.PAYMENT_MODE_PAY_ONLINE_ON_DELIVERY);
        }


        @OnClick(R.id.payment_razorpay)
        void setPayUsingRazorPay()
        {
            finishSelection(Order.PAYMENT_MODE_RAZORPAY);
        }



        void finishSelection(int paymentMode)
        {
            Intent output = new Intent();
            output.putExtra("output",paymentMode);

            if(paymentConfig!=null)
            {
                output.putExtra("razor_pay_key_id",paymentConfig.getRazorPayKey());
            }

            getActivity().setResult(2,output);
            getActivity().finish();
        }



    void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }



}
