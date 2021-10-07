package org.nearbyshops.whitelabelapp.Login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import org.nearbyshops.whitelabelapp.API.LoginUsingOTPService;
import org.nearbyshops.whitelabelapp.API.UserService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyAboutLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency;
import org.nearbyshops.whitelabelapp.UtilityScreens.PhotoSliderViewPager.Model.PhotoSliderData;
import org.nearbyshops.whitelabelapp.UtilityScreens.PhotoSliderViewPager.PhotosAdapter;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFCMTopicSubscriptions;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

public class LoginUsingOTPFragment extends Fragment {


    public static final String TAG_SERVICE_INDICATOR = "service_indicator";
    private boolean isDestroyed = false;



    @Inject Gson gson;
    @BindView(R.id.ccp) CountryCodePicker ccp;
    @BindView(R.id.username) TextInputEditText username;
    @BindView(R.id.password) TextInputEditText password;
    @BindView(R.id.progress_bar_login) ProgressBar progressBar;

//    @BindView(R.id.login_message) TextView loginMessage;



    @BindView(R.id.login) TextView loginButton;
    @BindView(R.id.text_input_password) TextInputLayout textInputPassword;


    TabLayout tabLayout;
    ViewPager viewPager;






    private int registrationMode = User.REGISTRATION_MODE_PHONE;



    public LoginUsingOTPFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }



    void bindViews(View rootView)
    {
//        myViewFlipper = rootView.findViewById(R.id.view_flipper);

        tabLayout = (TabLayout)rootView.findViewById(R.id.tab_layout);
        viewPager = rootView.findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager,true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);



        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_login_using_otp_global, container, false);
        ButterKnife.bind(this,rootView);

        bindViews(rootView);
        setupViewPager();


//        selectEmailClick();
        bindRegistrationMode();
        setupCountryCodePicker();


//        setupViewFlipper();

        return rootView;
    }



    int currentPage = 0;
    List<PhotoSliderData> photoSliderData;


    void setupViewPager()
    {
        photoSliderData = PhotoSliderData.getCustomerSlides();

        if(getResources().getInteger(R.integer.app_type) == getResources().getInteger(R.integer.app_type_main_app))
        {
            photoSliderData = PhotoSliderData.getCustomerSlides();
        }
        else if(getResources().getInteger(R.integer.app_type) == getResources().getInteger(R.integer.app_type_market_admin_app))
        {
            photoSliderData = PhotoSliderData.getMarketAdminSlides();
        }
        else if(getResources().getInteger(R.integer.app_type) == getResources().getInteger(R.integer.app_type_vendor_app))
        {
            photoSliderData = PhotoSliderData.getVendorAppSlides();
        }




        tabLayout.setupWithViewPager(viewPager,true);
        PagerAdapter adapter = new PhotosAdapter(getChildFragmentManager(), photoSliderData);
        viewPager.setAdapter(adapter);



        Timer timer;
        final long DELAY_MS = 00;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.


        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {

            public void run() {
                if (currentPage == photoSliderData.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }






    private void setupCountryCodePicker()
    {

        if(getResources().getBoolean(R.bool.restrict_phone_login_country))
        {
            ccp.setCcpClickable(false);
        }
        else
        {
            ccp.setCcpClickable(true);
        }

//        Market serviceConfig = PrefAppSettings.getAppConfig(getActivity());

//        if(serviceConfig!=null)
//        {
//            ccp.setCountryForNameCode(serviceConfig.getISOCountryCode());
//        }

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
        phoneValidity = Patterns.PHONE.matcher(username.getText().toString()).matches();


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
            sendOTP(registrationMode);
        }
        else if (textInputPassword.getVisibility()==View.VISIBLE)
        {
            loginToEndpoint();
        }

    }






    private void loginToEndpoint()
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
                .baseUrl(PrefGeneral.getServerURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();




        Call<User> call = retrofit.create(LoginUsingOTPService.class).verifyCredentialsUsingOTP(
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
                        PrefLogin.saveToken(
                                getActivity(),
                                finalPhoneWithCode,
                                user.getToken()
                        );



                        // save user profile information
                        PrefLogin.saveUserProfile(
                                user,
                                getActivity()
                        );


                        UtilityFCMTopicSubscriptions.subscribeToAllTopics();
                    }







                    if(getActivity() instanceof NotifyAboutLogin)
                    {
                        ((NotifyAboutLogin) getActivity()).loginSuccess();
                    }



                }
                else
                {
                    showToastMessage("Login Failed : Username or OTP is incorrect !");
//                    showToastMessage("Login Failed : Code " + response.code());
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
                .baseUrl(PrefGeneral.getServerURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();







        Call<ResponseBody> call = null;


        if(registrationMode==User.REGISTRATION_MODE_EMAIL)
        {
            call = retrofit.create(UserService.class).sendVerificationEmail(
                    phoneWithCode
            );
        }
        else if(registrationMode==User.REGISTRATION_MODE_PHONE)
        {
            call = retrofit.create(UserService.class).sendVerificationPhone(
                    phoneWithCode
            );
        }



        textInputPassword.setVisibility(View.VISIBLE);
        textInputPassword.requestFocus();

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




    private void sendOTPSignUp(int registrationMode)
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
                .baseUrl(PrefGeneral.getServerURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();







        Call<ResponseBody> call = retrofit.create(UserService.class).checkUsernameAndSignUp(
                phoneWithCode, registrationMode
        );







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


                    textInputPassword.setVisibility(View.VISIBLE);
                    textInputPassword.requestFocus();



                    loginButton.setText("Login");



                }
                else if(response.code()== 404)
                {
                    showToastMessage("Sign Up Required");


                    textInputPassword.setVisibility(View.VISIBLE);
                    textInputPassword.requestFocus();

                }
                else if(response.code()== 204)
                {
                    showToastMessage("Failed to send OTP ... failed Code : " + response.code());
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
        boolean emailEnabled = getResources().getBoolean(R.bool.login_using_email_enabled);
        boolean phoneEnabled = getResources().getBoolean(R.bool.login_using_phone_enabled);



        if(emailEnabled && phoneEnabled)
        {
            selectPhone.setVisibility(View.VISIBLE);
            selectEmail.setVisibility(View.VISIBLE);
        }
        else
        {
            selectPhone.setVisibility(View.GONE);
            selectEmail.setVisibility(View.GONE);

            if(phoneEnabled)
            {
                registrationMode=User.REGISTRATION_MODE_PHONE;

            }
            else if(emailEnabled)
            {
//                loginMessage.setText("Enter your E-mail to Login");
                registrationMode=User.REGISTRATION_MODE_EMAIL;
            }
        }




        if(registrationMode==User.REGISTRATION_MODE_EMAIL)
        {
            selectEmail.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//            selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.mapbox_blue));
            selectEmail.setBackgroundResource(R.drawable.button_orange);

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
//            selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.mapbox_blue));
            selectPhone.setBackgroundResource(R.drawable.button_orange);

            ccp.setVisibility(View.VISIBLE);

            username.setInputType(InputType.TYPE_CLASS_PHONE);
            username.setHint("Enter Phone");
        }

    }

}
