package org.nearbyshops.enduserappnew.multimarketfiles;

import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import org.nearbyshops.enduserappnew.multimarketfiles.API_SDS.UserServiceGlobal;
import org.nearbyshops.enduserappnew.API.LoginUsingOTPService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 19/4/17.
 */

public class LoginGlobalUsingOTPFragment extends Fragment {



    public static final String TAG_SERVICE_INDICATOR = "service_indicator";
    private boolean isDestroyed = false;




    @Inject Gson gson;
    @BindView(R.id.ccp) CountryCodePicker ccp;
    @BindView(R.id.username) TextInputEditText username;
    @BindView(R.id.password) TextInputEditText password;
    @BindView(R.id.progress_bar_login) ProgressBar progressBar;

//    @BindView(R.id.clear) TextView clear;
//    @BindView(R.id.select_service) TextView selectAutomatic;


    @BindView(R.id.login) Button loginButton;
    @BindView(R.id.text_input_password) TextInputLayout textInputPassword;

    @BindView(R.id.login_using_password) TextView loginUsingPassword;

    private int registrationMode = User.REGISTRATION_MODE_PHONE;



    public LoginGlobalUsingOTPFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_login_using_otp_global, container, false);
        ButterKnife.bind(this,rootView);



        bindRegistrationMode();

        if(getResources().getBoolean(R.bool.show_login_using_password))
        {
            loginUsingPassword.setVisibility(View.VISIBLE);
        }

        return rootView;
    }






    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }






    private boolean validateData()
    {
        boolean isValid = true;
//        boolean phoneValidity = false;
//        boolean emailValidity = false;
//
//
//        emailValidity = EmailValidator.getInstance().isValid(username.getText().toString());
//        phoneValidity = android.util.Patterns.PHONE.matcher(username.getText().toString()).matches();



        if(password.getText().toString().isEmpty())
        {
            password.requestFocus();
            password.setError("Please Enter OTP !");
            isValid = false;
        }



//        if(!phoneValidity)
//        {
//            username.setError("Not a valid phone !");
//            username.requestFocus();
//
//            isValid = false;
//        }
//
//        if(username.getText().toString().isEmpty())
//        {
//            password.requestFocus();
//            username.setError("Please enter phone !");
//            username.requestFocus();
//
//            isValid = false;
//        }
//        else if(username.getText().toString().length()!=10)
//        {
//            username.setError("Enter a valid phone number !");
//            username.requestFocus();
//
//            isValid = false;
//        }






        if(registrationMode==User.REGISTRATION_MODE_EMAIL)
        {

        }
        else if(registrationMode==User.REGISTRATION_MODE_PHONE)
        {
            // phone and password both needs to be valid
            isValid = validatePhone() && isValid;
        }




        return isValid;
    }






    private boolean validatePhone()
    {
        boolean isValid = true;
        boolean phoneValidity = false;
//        boolean emailValidity = false;
//
//
//        emailValidity = EmailValidator.getInstance().isValid(username.getText().toString());
        phoneValidity = android.util.Patterns.PHONE.matcher(username.getText().toString()).matches();


        if(username.getText().toString().isEmpty())
        {
//            password.requestFocus();
            username.setError("Please enter phone !");
            username.requestFocus();

            isValid = false;
        }
        else if(username.getText().toString().length()!=10)
        {
            username.setError("Enter a valid phone number !");
            username.requestFocus();

            isValid = false;
        }


        if(!phoneValidity)
        {
            username.setError("Invalid phone number !");
            isValid = false;
        }



        return isValid;
    }


    private boolean validateEmail()
    {
        boolean isValid = true;
        boolean emailValidity = false;
//
//
//        emailValidity = EmailValidator.getInstance().isValid(username.getText().toString());
        emailValidity = Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches();


        if(username.getText().toString().isEmpty())
        {
//            password.requestFocus();
            username.setError("Please enter email !");
            username.requestFocus();

            isValid = false;
        }


        if(!emailValidity)
        {
            username.setError("Invalid Email !");
            isValid = false;
        }



        return isValid;
    }





    @Override
    public void onStart() {
        super.onStart();
        isDestroyed= false;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isDestroyed= true;
    }





    @OnClick(R.id.login)
    void loginButtonClick()
    {
        if(textInputPassword.getVisibility()==View.GONE)
        {
            textInputPassword.requestFocus();
            sendOTP(registrationMode);
        }
        else if (textInputPassword.getVisibility()==View.VISIBLE)
        {

            if(PrefGeneral.getServiceURL(MyApplication.getAppContext())==null)
            {

                loginToGlobalEndpoint();
            }
            else
            {

                loginToLocalEndpoint();
            }

        }

    }






    private void loginToGlobalEndpoint()
    {

        if(!validateData())
        {
            // validation failed return
            return;
        }




//        final String phoneWithCode = username.getText().toString();

        String phoneWithCode = "";

        if(registrationMode==User.REGISTRATION_MODE_PHONE)
        {
            phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();
        }
        else if(registrationMode==User.REGISTRATION_MODE_EMAIL)
        {
            phoneWithCode = username.getText().toString();
        }





        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();


        Call<User> call = retrofit.create(UserServiceGlobal.class).verifyCredentialsUsingOTP(
                PrefLogin.baseEncoding(phoneWithCode, password.getText().toString()),
                registrationMode
        );


        String finalPhoneWithCode = phoneWithCode;
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    // save username and password



                    User user = response.body();



                    if(user!=null)
                    {
                        PrefLoginGlobal.saveToken(
                                getActivity(),
                                finalPhoneWithCode,
                                user.getToken()
                        );



                        // save user profile information
                        PrefLoginGlobal.saveUserProfile(
                                user,
                                getActivity()
                        );


                        UtilityFunctions.updateFirebaseSubscriptions();

                    }







                    if(getActivity() instanceof NotifyAboutLogin)
                    {
                        ((NotifyAboutLogin) getActivity()).loginSuccess();
                    }



                }
                else
                {
                    showToastMessage("Login Failed : Username or password is incorrect !");
                    System.out.println("Login Failed : Code " + response.code());
                }

            }




            @Override
            public void onFailure(Call<User> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }
                showToastMessage("Network connection problem !");
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });



    }





    private void loginToLocalEndpoint()
    {
        if(!validateData())
        {
            // validation failed return
            return;
        }




//        final String phoneWithCode = username.getText().toString();

        String phoneWithCode = "";

        if(registrationMode==User.REGISTRATION_MODE_PHONE)
        {
            phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();
        }
        else if(registrationMode==User.REGISTRATION_MODE_EMAIL)
        {
            phoneWithCode = username.getText().toString();
        }



        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);




        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



        Call<User> call = retrofit.create(LoginUsingOTPService.class).loginWithGlobalCredentials(
                PrefLogin.baseEncoding(phoneWithCode,password.getText().toString()),
                PrefServiceConfig.getServiceURL_SDS(getActivity()),
                123,
                true,false,
                registrationMode,
                false,true
        );




        String finalPhoneWithCode = phoneWithCode;
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    // save username and password


                    User user = response.body();


                    if (user != null) {

                        PrefLoginGlobal.saveToken(
                                getActivity(),
                                finalPhoneWithCode,
                                user.getUserProfileGlobal().getToken()

                        );
                    }





                    String username = "";

                    if (user != null) {


                        if(user.getPhone()!=null)
                        {
                            username = user.getPhone();
                        }
                        else if(user.getEmail()!=null)
                        {
                            username = user.getEmail();
                        }
                        else if(user.getUsername()!=null)
                        {
                            username = user.getUsername();
                        }
                        else if(user.getUserID()!=0)
                        {
                            username = String.valueOf(user.getUserID());
                        }
                    }


                    // local username can be different from the supplied username

                    if (user != null) {


                        PrefLogin.saveToken(
                                getActivity(),
                                username,
                                user.getToken()
                        );
                    }


                    // save user profile information
                    PrefLogin.saveUserProfile(
                            user,
                            getActivity()
                    );




                    UtilityFunctions.updateFirebaseSubscriptions();




                    if(PrefLoginGlobal.getUser(getActivity())==null)
                    {

                        if (response.body() != null) {


                            PrefLoginGlobal.saveUserProfile(
                                    response.body().getUserProfileGlobal(),
                                    getActivity()
                            );

                        }
                    }







                    if(getActivity() instanceof NotifyAboutLogin)
                    {
                        ((NotifyAboutLogin) getActivity()).loginSuccess();
                    }


                }
                else
                {
                    showToastMessage("Login Failed : Username or password is incorrect !");
                    System.out.println("Login Failed : Code " + response.code());
                }

            }




            @Override
            public void onFailure(Call<User> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }
                showToastMessage("Network connection problem !");
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }






    private void sendOTP(int registrationMode)
    {


        if(registrationMode==User.REGISTRATION_MODE_PHONE && !validatePhone())
        {
            // validation failed return
            return;
        }
        else if(registrationMode==User.REGISTRATION_MODE_EMAIL && !validateEmail())
        {
            return;
        }



//        final String phoneWithCode = username.getText().toString();

        String phoneWithCode = "";

        if(registrationMode==User.REGISTRATION_MODE_EMAIL)
        {
            phoneWithCode = username.getText().toString();
        }
        else if(registrationMode==User.REGISTRATION_MODE_PHONE)
        {
            phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();
        }



        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();







        Call<ResponseBody> call = null;


        if(registrationMode==User.REGISTRATION_MODE_EMAIL)
        {
            call = retrofit.create(UserServiceGlobal.class).sendVerificationEmail(
                    phoneWithCode
            );
        }
        else if(registrationMode==User.REGISTRATION_MODE_PHONE)
        {
            call = retrofit.create(UserServiceGlobal.class).sendVerificationPhone(
                    phoneWithCode
            );
        }



        textInputPassword.setVisibility(View.VISIBLE);
        loginButton.setText("Login");


            if (call == null) {
                return;
            }




            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    if(isDestroyed)
                    {
                        return;
                    }

                    progressBar.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);



                    if(response.code()==200) {
                        // save username and password

    //                    showToastMessage("OTP Sent !");

                        if(registrationMode==User.REGISTRATION_MODE_EMAIL)
                        {
                            showToastMessage("OTP sent on your Email !");
                        }
                        else if(registrationMode==User.REGISTRATION_MODE_PHONE)
                        {
                            showToastMessage("OTP sent on your Phone !");
                        }

                    }
                    else
                    {
                        showToastMessage("Failed to send OTP ... failed Code : " + response.code());
                    }


                    }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {


                    if(isDestroyed)
                    {
                        return;
                    }

                    showToastMessage("Failed ... Please check your network !");

                    progressBar.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);



                }
            });



    }




    @OnClick(R.id.select_phone)
    void selectPhoneClick()
    {
        registrationMode = User.REGISTRATION_MODE_PHONE;
        bindRegistrationMode();
    }



    @OnClick(R.id.select_email)
    void selectEmailClick()
    {
        registrationMode= User.REGISTRATION_MODE_EMAIL;
        bindRegistrationMode();
    }





    @BindView(R.id.select_phone) TextView selectPhone;
    @BindView(R.id.select_email) TextView selectEmail;


    private void bindRegistrationMode()
    {
        if(registrationMode==User.REGISTRATION_MODE_EMAIL)
        {
            selectEmail.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.buttonColor));

            selectPhone.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
            selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

            ccp.setVisibility(View.GONE);

            username.setInputType(InputType.TYPE_CLASS_TEXT);
            username.setHint("Enter Email");

        }
        else if(registrationMode==User.REGISTRATION_MODE_PHONE)
        {
            selectEmail.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
            selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

            selectPhone.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.buttonColor));

            ccp.setVisibility(View.VISIBLE);


            ccp.setCountryForNameCode("IN");
            ccp.setCcpClickable(false);


            username.setInputType(InputType.TYPE_CLASS_PHONE);
            username.setHint("Enter Phone");
        }

    }





    @OnClick(R.id.login_using_password)
    void loginUsingPasswordClick()
    {
        if(getActivity()!=null)
        {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new LoginGlobalUsingPasswordFragment())
                    .commitNow();
        }



//        NavHostFragment.findNavController(this).navigate(R.id.loginGlobalUsingPasswordFragment);
    }

}
