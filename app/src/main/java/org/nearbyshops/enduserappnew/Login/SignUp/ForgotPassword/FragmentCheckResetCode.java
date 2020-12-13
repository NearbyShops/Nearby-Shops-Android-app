package org.nearbyshops.enduserappnew.Login.SignUp.ForgotPassword;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Login.SignUp.Interfaces.ShowFragmentForgotPassword;
import org.nearbyshops.enduserappnew.Login.SignUp.PrefSignUp.PrefrenceForgotPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentCheckResetCode extends Fragment {



    @BindView(R.id.check_icon) ImageView checkIcon;
    @BindView(R.id.cross_icon) ImageView crossIcon;
    @BindView(R.id.message) TextView textAvailable;

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.verification_code) TextInputEditText verificationCode;
    @BindView(R.id.email_text) TextView emailText;


    @BindView(R.id.progress_bar_resend) ProgressBar progressBarResend;
    @BindView(R.id.message_resend)
    TextView messageResend;



//    @BindView(R.id.enter_password) EditText enterPassword;
//    @BindView(R.id.confirm_password) EditText confirmPassword;


    @BindView(R.id.progress_bar_button) ProgressBar progressBarButton;
    @BindView(R.id.next) TextView nextButton;
    @BindView(R.id.header_title) TextView headerTitle;




    boolean isDestroyed = false;


    @Inject
    UserService userService;


    User user;

    SmsVerifyCatcher smsVerifyCatcher;



    @Inject Gson gson;

//    boolean verificationCodeValid = false; // flag to keep record of verification code


    public FragmentCheckResetCode() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_check_reset_code, container, false);
        ButterKnife.bind(this,rootView);

        user = PrefrenceForgotPassword.getUser(getActivity());

//        if(getActivity() instanceof ReadWriteUser)
//        {
//            User user = ((ReadWriteUser) getActivity()).getSignUpProfile();

//        }


        if(user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
        {
            headerTitle.setText("We have sent you a reset code on your e-mail ID : ");
            emailText.setText(user.getEmail());
        }
        else if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
        {
            headerTitle.setText("We have sent you a SMS containing a reset code on your Phone : ");
            emailText.setText(user.getPhone());
        }



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




    //
//    boolean validatePasswords(boolean showError)
//    {
//
//        boolean isValid = true;
//
//        if(enterPassword.getText().toString().equals(""))
//        {
//            if(showError)
//            {
//                enterPassword.setError("Password cannot be empty !");
//                enterPassword.requestFocus();
//            }
//
//            isValid = false;
//        }
//        else if(!enterPassword.getText().toString().equals(confirmPassword.getText().toString()))
//        {
//            if(showError)
//            {
//                confirmPassword.setError("Passwords do not match !");
//                confirmPassword.requestFocus();
//            }
//
//            isValid = false;
//        }
//
//
//        return isValid;
//    }
//
//
//







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
                verificationCode.setError("Verification code cannot be empty !");
                verificationCode.requestFocus();
            }

            isValid = false;
        }
        else if(verificationCode.getText().toString().length() < 3)
        {
            if(showError)
            {
                verificationCode.setError("Verification code not valid !");
                verificationCode.requestFocus();
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

            verifyResetCode(false);
        }
    };






        @Override
        public void onResume() {
            super.onResume();
            isDestroyed = false;
        }


        @Override
        public void onDestroyView() {
            super.onDestroyView();
            isDestroyed = true;
        }



    void verifyResetCode(final boolean initiateNext)
    {


        String emailOrPhone = "";

        if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
        {
            emailOrPhone = user.getPhone();
        }
        else if(user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
        {
            emailOrPhone = user.getEmail();
        }


        user.setPasswordResetCode(verificationCode.getText().toString());



        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        if(initiateNext)
        {
            nextButton.setVisibility(View.INVISIBLE);
            progressBarButton.setVisibility(View.VISIBLE);
        }



        Call<ResponseBody> call;


        if(PrefGeneral.isMultiMarketEnabled(getActivity()))
        {
            // multi market mode enabled ... so use a global endpoint

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();


            call = retrofit.create(UserServiceGlobal.class).checkPasswordResetCode(
                    emailOrPhone,user.getPasswordResetCode()
            );
        }
        else
        {
            call = userService.checkPasswordResetCode(
                    emailOrPhone,user.getPasswordResetCode()
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

                if(initiateNext)
                {
                    nextButton.setVisibility(View.VISIBLE);
                    progressBarButton.setVisibility(View.INVISIBLE);
                }


//                verificationCodeValid = false;

                if(response.code()==200)
                {
                    checkIcon.setVisibility(View.VISIBLE);
                    crossIcon.setVisibility(View.INVISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Reset code is valid !");

//                    verificationCodeValid = true;

//                    if(initiateNext) {

//                        showToastMessage("Next Initiated !");


                        // save Verification code
//                        if(getActivity() instanceof ReadWriteUser)
//                        {
//                            User user = ((ReadWriteUser) getActivity()).getSignUpProfile();
//                            user.setRt_email_verification_code(verificationCode.getText().toString());
//                            ((ReadWriteUser) getActivity()).setSignUpProfile(user);
//                        }




//                        user.setRt_email_verification_code(verificationCode.getText().toString());
//
//                        if (getActivity() instanceof ShowFragmentSignUp)
//                        {
//                            ((ShowFragmentSignUp) getActivity()).showEnterPassword();
//                        }


//                    }


                    if(initiateNext)
                    {

//                        nextButton.setVisibility(View.INVISIBLE);
//                        progressBarButton.setVisibility(View.VISIBLE);

                        PrefrenceForgotPassword.saveUser(user,getActivity());

                        if(getActivity() instanceof ShowFragmentForgotPassword)
                        {
                            ((ShowFragmentForgotPassword) getActivity()).showResetPassword();
                        }
                    }

                }
                else if(response.code() == 204)
                {

                    checkIcon.setVisibility(View.INVISIBLE);
                    crossIcon.setVisibility(View.VISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Reset code invalid or expired !");
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                if(isDestroyed)
                {
                    return;
                }



                if(initiateNext)
                {
                    nextButton.setVisibility(View.VISIBLE);
                    progressBarButton.setVisibility(View.INVISIBLE);
                }

                progressBar.setVisibility(View.INVISIBLE);

                checkIcon.setVisibility(View.INVISIBLE);
                crossIcon.setVisibility(View.VISIBLE);

                textAvailable.setVisibility(View.VISIBLE);
                textAvailable.setText("Unable to check validity !");

            }
        });

    }







//    @OnClick(R.id.next)
    void nextClick()
    {

//        if(!verificationCodeValid)
//        {
//            return;
//        }
    }


    @OnClick(R.id.next)
    void createAccountClick()
    {


        if(!validateVerificationCode(true))
        {
            return;
        }


        //        if(!validatePasswords(true))
//        {
//            return;
//        }



        verifyResetCode(true);
    }



//
//    void resetPassword()
//    {
//
//
//            user.setPasswordResetCode(verificationCode.getText().toString());
////            user.setPassword(enterPassword.getText().toString());
//
//
//            Gson gson = new Gson();
//            logMessage(gson.toJson(user));
//
//
//            Call<ResponseBody> call = userService.resetPassword(user);
//
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//
//                    if(response.code()==200)
//                    {
//                        if(getActivity() instanceof ShowFragmentForgotPassword)
//                        {
//                            ((ShowFragmentSignUp) getActivity()).showEnterPassword();
//                        }
//
//                    }
//                    else if(response.code()==304)
//                    {
//
//                        showToastMessage("Failed to create account");
//                    }
//                    else
//                    {
//                        showToastMessage("Failed code : " + String.valueOf(response.code()));
//                    }
//
//
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//
//                    showToastMessage("Network failure !");
//                }
//            });
//    }
//



    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }




    @BindView(R.id.resend_code)
    TextView resendCode;


    @OnClick(R.id.resend_code)
    void resendVerificationCode()
    {



        resendCode.setVisibility(View.INVISIBLE);
        progressBarResend.setVisibility(View.VISIBLE);
        messageResend.setVisibility(View.VISIBLE);
        messageResend.setText("Sending reset code ... ");


        Call<ResponseBody> call;


        if(PrefGeneral.isMultiMarketEnabled(getActivity()))
        {
            // multi market mode enabled ... so use a global endpoint

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();


            call = retrofit.create(UserServiceGlobal.class).generateResetCode(user);
        }
        else
        {
            call = userService.generateResetCode(user);
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
                resendCode.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    messageResend.setText("Reset Code Sent !");
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
                resendCode.setVisibility(View.VISIBLE);
                messageResend.setText("Network failure please check your internet connection!");
            }
        });
    }

}
