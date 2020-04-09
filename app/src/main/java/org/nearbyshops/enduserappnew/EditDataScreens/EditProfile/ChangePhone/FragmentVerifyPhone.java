package org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.ChangePhone;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.API.API_SDS.UserServiceGlobal;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentVerifyPhone extends Fragment {


    /* Token renewal variables : BEGIN */

    boolean isDestroyed = false;

    // constants - request codes for token renewal
    public static final int REQUEST_CODE_UPDATE_EMAIL = 1;
    // housekeeping for token renewal
    int token_renewal_attempts = 0;  // variable to keep record of renewal attempts
    int token_renewal_request_code = -1; // variable to store the request code;

    /* Token renewal variables : END */



    @BindView(R.id.check_icon) ImageView checkIcon;
    @BindView(R.id.cross_icon) ImageView crossIcon;
    @BindView(R.id.message) TextView textAvailable;

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.verification_code)
    TextInputEditText verificationCode;
    @BindView(R.id.email_text) TextView emailText;


    @BindView(R.id.progress_bar_resend) ProgressBar progressBarResend;
    @BindView(R.id.message_resend) TextView messageResend;


    SmsVerifyCatcher smsVerifyCatcher;



    @BindView(R.id.progress_bar_update) ProgressBar progressBarUpdate;
    @BindView(R.id.update_phone) Button updatePhoneButton;



    @Inject
    UserService userService;

    User user;

    @Inject Gson gson;


//    boolean verificationCodeValid = false; // flag to keep record of verification code


    public FragmentVerifyPhone() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_verify_phone, container, false);
        ButterKnife.bind(this,rootView);

        user = PrefChangePhone.getUser(getActivity());


        emailText.setText("+" + user.getRt_phone_country_code() + "-" + user.getPhone());


        smsVerifyCatcher = new SmsVerifyCatcher(getActivity(), new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {

                String code = parseCode(message);//Parse verification code
                verificationCode.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });


        return rootView;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }


    @Override
    public void onResume() {
        super.onResume();

        isDestroyed = false;
    }






    private String parseCode(String message) {
        return message.replaceAll("[^0-9]", "");
    }



    @Override
    public void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }







    @OnTextChanged(R.id.verification_code)
    void verificationCodeChanged()
    {

//        verificationCodeValid = false; // reset the flag

        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);


        if(!validateVerificationCode(false))
        {
            return;
        }



//        logMessage("Validated !");


        progressBar.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
        countDownTimer.start();
    }





    boolean validateVerificationCode(boolean showError)
    {

        boolean isValid = true;

        if(verificationCode.getText().toString().length() == 0)
        {
            if(showError)
            {
                verificationCode.requestFocus();
                verificationCode.setError("Verification code cannot be empty !");
            }

            isValid = false;
        }
        else if(verificationCode.getText().toString().length() < 3)
        {
            if(showError)
            {
                verificationCode.requestFocus();
                verificationCode.setError("Verification code not valid !");
            }

            isValid = false;
        }


        return isValid;
    }





    void logMessage(String message)
    {
        Log.d("verify_email",message);
    }



    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {

        public void onTick(long millisUntilFinished) {

            if(isDestroyed)
            {
                return;
            }

            logMessage("Timer onTick()");
        }

        public void onFinish() {

            if(isDestroyed)
            {
                return;
            }

            logMessage("Timer onFinish() ");

            verifyEmailCode(false);
        }
    };






    void verifyEmailCode(final boolean initiateNext)
    {


        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


//        boolean isGlobalProfile = getActivity().getIntent().getBooleanExtra(ChangePhone.TAG_IS_GLOBAL_PROFILE,false);


        Call<ResponseBody> call;

        if(PrefGeneral.getMultiMarketMode(getActivity())) {

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();

            call = retrofit.create(UserServiceGlobal.class).checkPhoneVerificationCode(
                    user.getPhoneWithCountryCode(),verificationCode.getText().toString()
            );

        }
        else
        {

            call = userService.checkPhoneVerificationCode(
                    user.getPhoneWithCountryCode(),verificationCode.getText().toString()
            );
        }





        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                progressBar.setVisibility(View.INVISIBLE);

//                verificationCodeValid = false;

                if(response.code()==200)
                {
                    checkIcon.setVisibility(View.VISIBLE);
                    crossIcon.setVisibility(View.INVISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("OTP code is valid !");



                }
                else if(response.code() == 204)
                {
                    checkIcon.setVisibility(View.INVISIBLE);
                    crossIcon.setVisibility(View.VISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("OTP code invalid or expired !");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                progressBar.setVisibility(View.INVISIBLE);

                checkIcon.setVisibility(View.INVISIBLE);
                crossIcon.setVisibility(View.VISIBLE);

                textAvailable.setVisibility(View.VISIBLE);
                textAvailable.setText("Unable to check validity !");

            }
        });

    }







    @OnClick(R.id.update_phone)
    void createAccountClick()
    {

        if(!validateVerificationCode(true))
        {
            return;
        }


//        checkIcon.setVisibility(View.INVISIBLE);
//        crossIcon.setVisibility(View.INVISIBLE);
//        textAvailable.setVisibility(View.INVISIBLE);

        verifyEmailCode(true);



        changePhone();
    }


    void changePhone()
    {


            user.setRt_phone_verification_code(verificationCode.getText().toString());


//            Gson gson = new Gson();
//            logMessage(gson.toJson(user));













//            boolean isGlobalProfile = getActivity().getIntent().getBooleanExtra(ChangePhone.TAG_IS_GLOBAL_PROFILE,false);

            Call<ResponseBody> call;

            if(PrefGeneral.getMultiMarketMode(getActivity())) {

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                        .client(new OkHttpClient().newBuilder().build())
                        .build();

                user.setPassword(PrefLoginGlobal.getPassword(getActivity()));

                call = retrofit.create(UserServiceGlobal.class).updatePhone(
                        PrefLoginGlobal.getAuthorizationHeaders(getActivity()),
                        user
                );


            }
            else
            {
                user.setPassword(PrefLogin.getPassword(getActivity()));



                call = userService.updatePhone(
                        PrefLogin.getAuthorizationHeaders(getActivity()) ,
                        user
                );
            }


            progressBarUpdate.setVisibility(View.VISIBLE);
            updatePhoneButton.setVisibility(View.INVISIBLE);




        call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(isDestroyed)
                    {
                        return;
                    }


                    progressBarUpdate.setVisibility(View.INVISIBLE);
                    updatePhoneButton.setVisibility(View.VISIBLE);


                    if(response.code()==200)
                    {

                        if(PrefGeneral.getMultiMarketMode(getActivity()))
                        {
                            User userDetails = PrefLoginGlobal.getUser(getActivity());
                            userDetails.setPhone(user.getPhoneWithCountryCode());
                            PrefLoginGlobal.saveUserProfile(userDetails,getActivity());
                            PrefLoginGlobal.saveUsername(getActivity(),user.getPhoneWithCountryCode());
                        }
                        else
                        {
                            User userDetails = PrefLogin.getUser(getActivity());
                            userDetails.setPhone(user.getPhoneWithCountryCode());
                            PrefLogin.saveUserProfile(userDetails,getActivity());
                            PrefLogin.saveUsername(getActivity(),user.getPhoneWithCountryCode());
                        }


                        if(getActivity() instanceof ShowFragmentChangePhone)
                        {
                            ((ShowFragmentChangePhone) getActivity()).showResultSuccess();
                        }




                    }
                    else if(response.code()==304)
                    {

                        showToastMessage("Failed to change Phone");
                    }
                    else
                    {
                        showToastMessage("Failed code : " + response.code());
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    if(isDestroyed)
                    {
                        return;
                    }


                    progressBarUpdate.setVisibility(View.INVISIBLE);
                    updatePhoneButton.setVisibility(View.VISIBLE);


                    showToastMessage("Network failure !");
                }
            });


    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }





    @OnClick(R.id.resend_code)
    void resendVerificationCode()
    {

        progressBarResend.setVisibility(View.VISIBLE);
        messageResend.setVisibility(View.VISIBLE);
        messageResend.setText("Sending verification code ... ");



//        boolean isGlobalProfile = getActivity().getIntent().getBooleanExtra(ChangePhone.TAG_IS_GLOBAL_PROFILE,false);

        Call<ResponseBody> call;



        if(PrefGeneral.getMultiMarketMode(getActivity())) {

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();


            call = retrofit.create(UserServiceGlobal.class).sendVerificationPhone(
                    user.getPhoneWithCountryCode()
            );


        }
        else
        {
            call = userService.sendVerificationPhone(user.getPhoneWithCountryCode());
        }




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                progressBarResend.setVisibility(View.INVISIBLE);
                messageResend.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    messageResend.setText("Verification Code Sent !");
                }
                else
                {
                    messageResend.setText("Failed to resend code !");
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                progressBarResend.setVisibility(View.INVISIBLE);
                messageResend.setText("Network failure please check your internet connection!");
            }
        });
    }






}
