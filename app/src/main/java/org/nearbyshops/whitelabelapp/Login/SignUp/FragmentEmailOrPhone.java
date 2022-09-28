package org.nearbyshops.whitelabelapp.Login.SignUp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import okhttp3.ResponseBody;

import org.nearbyshops.whitelabelapp.API.UserService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Login.SignUp.Interfaces.ShowFragmentSignUp;
import org.nearbyshops.whitelabelapp.Login.SignUp.PrefSignUp.PrefrenceSignUp;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentEmailOrPhone extends Fragment {


    @BindView(R.id.select_email) TextView selectPhone;
    @BindView(R.id.select_phone) TextView selectEmail;

    @BindView(R.id.text_input_phone) TextInputLayout phoneLayout;
    @BindView(R.id.text_input_email) TextInputLayout emailLayout;


    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.phone)
    TextInputEditText phone;
    @BindView(R.id.email) TextInputEditText email;

    @BindView(R.id.check_icon) ImageView checkIcon;
    @BindView(R.id.cross_icon) ImageView crossIcon;
    @BindView(R.id.message) TextView textAvailable;

    @BindView(R.id.phone_registration_message) TextView phoneRegistrationMessage;


    @BindView(R.id.header) TextView header;


    @BindView(R.id.progress_bar) ProgressBar progressBar;


    boolean isDestroyed = false;


    @BindView(R.id.next) TextView nextButton;
    @BindView(R.id.progress_bar_next) ProgressBar progressBarNext;


    @Inject Gson gson;


//    int phoneOrEmail = 1; // flag for indicating the input mode 1 for phone and 2 for email


    boolean phoneIsAvailable = false;
    boolean emailIsAvailable = false;

    User user;


    @Inject
    UserService userService;



    public FragmentEmailOrPhone() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_email_or_phone, container, false);
        ButterKnife.bind(this, rootView);



        user = PrefrenceSignUp.getUser(getActivity());

        if (user.getRt_registration_mode() == User.REGISTRATION_MODE_EMAIL) {

            selectEmailClick();
        }
        else if (user.getRt_registration_mode() == User.REGISTRATION_MODE_PHONE) {

            selectPhoneClick();
        }
        else
        {
            selectEmailClick();
        }



        setupVisiblity();





//
//        ccp.setCustomMasterCountries();


        if(user.getRt_phone_country_code()!=null)
        {
            ccp.setCountryForPhoneCode(Integer.parseInt(user.getRt_phone_country_code()));
            ccp.setCcpClickable(true);

        }
        else
        {
            ccp.setCcpClickable(true);
//            if(PrefAppSettings.getAppConfig(getActivity())!=null)
//            {
//                ccp.setCountryForNameCode(PrefAppSettings.getAppConfig(getActivity()).getISOCountryCode());
//            }
        }





//        if(PrefServiceConfig.getServiceConfigLocal(getActivity())!=null)
//        {
//            ccp.setCountryForNameCode(PrefServiceConfig.getServiceConfigLocal(getActivity()).getISOCountryCode());
//        }





        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                textInputChanged();
            }
        });


        ccp.registerCarrierNumberEditText(phone);
        ccp.setNumberAutoFormattingEnabled(false);



        phone.requestFocus();
        bindViews();

        return rootView;
    }







    private void setupVisiblity()
    {
        boolean phoneEnabled = getResources().getBoolean(R.bool.login_using_phone_enabled);
        boolean emailEnabled = getResources().getBoolean(R.bool.login_using_email_enabled);

        if(phoneEnabled && emailEnabled)
        {
            selectEmail.setVisibility(View.VISIBLE);
            selectPhone.setVisibility(View.VISIBLE);
        }
        else
        {
            selectEmail.setVisibility(View.GONE);
            selectPhone.setVisibility(View.GONE);


            if(phoneEnabled)
            {
                user.setRt_registration_mode(User.REGISTRATION_MODE_PHONE);
                bindRegistrationMode();
                header.setText("Step 2 : Enter Phone");
            }
            else if(emailEnabled)
            {
                user.setRt_registration_mode(User.REGISTRATION_MODE_EMAIL);
                bindRegistrationMode();

                header.setText("Step 2 : Enter Email");
            }
        }
    }







    @OnClick(R.id.select_email)
    void selectEmailClick()
    {
        user.setRt_registration_mode(User.REGISTRATION_MODE_EMAIL);
        bindRegistrationMode();
    }





    private void bindRegistrationMode()
    {

        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
        {
            email.requestFocus();

            selectPhone.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));

            selectEmail.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
            selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));



            ccp.setVisibility(View.GONE);
            phoneLayout.setVisibility(View.INVISIBLE);
            emailLayout.setVisibility(View.VISIBLE);


        }
        else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
        {
            phone.requestFocus();


            ccp.setVisibility(View.VISIBLE);
            phoneLayout.setVisibility(View.VISIBLE);
            emailLayout.setVisibility(View.INVISIBLE);


            selectEmail.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));

            selectPhone.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
            selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        }








//        phoneOrEmail = 2;  // set flag


//        bindViews();

        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);
        textInputChanged();



    }







    @OnClick(R.id.select_phone)
    void selectPhoneClick()
    {

        user.setRt_registration_mode(User.REGISTRATION_MODE_PHONE);
        bindRegistrationMode();


//
//        phone.requestFocus();
//
//
//        selectEmail.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//        selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));
//
//        selectPhone.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
//        selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
//
//
//        ccp.setVisibility(View.VISIBLE);
//        phoneLayout.setVisibility(View.VISIBLE);
//        emailLayout.setVisibility(View.INVISIBLE);
//
//
//        if(PrefGeneral.getMultiMarketMode(getActivity()))
//        {
//            phoneRegistrationMessage.setVisibility(View.VISIBLE);
//        }
//
//
////        phoneOrEmail = 1; // set flag
//
//        checkIcon.setVisibility(View.INVISIBLE);
//        crossIcon.setVisibility(View.INVISIBLE);
//        textAvailable.setVisibility(View.INVISIBLE);
//        textInputChanged();


    }





    private void bindViews()
    {
            phone.setText(user.getPhone());
            email.setText(user.getEmail());
    }






    private void saveDataFromViews()
    {

//            user.setPhone(ccp.getSelectedCountryCode()+phone.getText().toString());
//            user.setPhone(phone.getText().toString());

            user.setPhone(phone.getText().toString());
            user.setEmail(email.getText().toString());


            user.setRt_phone_country_code(ccp.getSelectedCountryCode());



//            showToastMessage("Phone : " + user.getRt_phone_country_code() + "-" + user.getPhone());

    }





    @OnTextChanged({R.id.email, R.id.phone})
    void textInputChanged()
    {
        // reset flags
        phoneIsAvailable = false;
        emailIsAvailable = false;


        textAvailable.setVisibility(View.INVISIBLE);
        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);

        if(!isDataValid(false))
        {
            return;
        }



        saveDataFromViews();



//        checkUsernameExist();


//        progressBar.setVisibility(View.VISIBLE);
//        countDownTimer.cancel();  // restart the timer
//        countDownTimer.start();


    }



    boolean isDataValid(boolean showError){
        boolean isValid = true;

        if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
        {
            // validate phone


            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = null;

            try {
                phoneNumber = phoneUtil.parse(phone.getText().toString(),ccp.getSelectedCountryNameCode());
            } catch (NumberParseException e) {
                e.printStackTrace();
            }


            if(phone.getText().toString().equals(""))
            {
                if(showError)
                {
                    phone.requestFocus();
                    phone.setError("Phone cannot be empty !");
                }

                isValid= false;

            }
            else if(phoneNumber!=null && !phoneUtil.isValidNumberForRegion(phoneNumber,ccp.getSelectedCountryNameCode()))
            {
//                isValidMobile(phone.getText().toString())
//                phone.getText().toString().length()!=10


                if(showError)
                {
                    phone.requestFocus();
                    phone.setError("Phone is not valid !");
                }

                isValid=false;
            }
            else if(phoneNumber==null)
            {

                if(showError)
                {
                    phone.requestFocus();
                    phone.setError("Phone invalid !");
                }

                isValid=false;
            }

        }
        else if (user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
        {
            if(email.getText().toString().equals(""))
            {
                if(showError)
                {
                    email.requestFocus();
                    email.setError("E-mail cannot be empty !");
                }

                isValid = false;
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
            {
                if(showError)
                {
                    email.requestFocus();
                    email.setError("Not a valid e-mail !");
                }

                isValid = false;
            }

        }


        return isValid;
    }




    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            // if(phone.length() != 10) {
            check = phone.length() >= 6 && phone.length() <= 13;
        } else {
            check=false;
        }
        return check;
    }






    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {

        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {


            if(isDestroyed)
            {
                return;
            }

//           checkUsernameExist();
        }
    };









    void checkUsernameExist(boolean initiateNext, boolean showNextButtonProgress)
    {
        String inputName = "";

        if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
        {
            // check for phone

//            inputName =  phone.getText().toString();
//            inputName = ccp.getSelectedCountryCode() + phone.getText().toString();


            inputName = ccp.getFullNumber();

//            showToastMessage("Phone : " + inputName);

        }
        else if(user.getRt_registration_mode() == User.REGISTRATION_MODE_EMAIL)
        {
            // check for email

            inputName = email.getText().toString();
        }




        Call<ResponseBody> call = userService.checkUsernameExists(inputName);


        if(showNextButtonProgress)
        {
            progressBarNext.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
        }


        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }



                if(showNextButtonProgress)
                {
                    progressBarNext.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                }



                progressBar.setVisibility(View.INVISIBLE);

                if(response.code()==200)
                {
                    // username is not unique and already exist

                    if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
                    {
                        phone.setError("An account already exist with that phone. Please use another phone or reset the password for that phone !");

//                        Somebody has already registered using that phone !
                    }
                    else if(user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
                    {
                        email.setError("An account already exist with that e-mail. Please use another email or reset the password for that e-mail !");
                    }


                    checkIcon.setVisibility(View.INVISIBLE);
                    crossIcon.setVisibility(View.VISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Not Available for Registration !");


                    if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
                    {
                        phoneIsAvailable = false;
                    }
                    else if(user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
                    {
                        emailIsAvailable = false;
                    }

                }
                else if(response.code()==204)
                {
                    // username is unique and available for registration
                    checkIcon.setVisibility(View.VISIBLE);
                    crossIcon.setVisibility(View.INVISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Available for Registration !");


                    if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
                    {
                        phoneIsAvailable = true;
                    }
                    else if(user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
                    {
                        emailIsAvailable = true;
                    }



                    if(initiateNext)
                    {
                        initiateNext();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }



                if(showNextButtonProgress)
                {
                    progressBarNext.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                }

                progressBar.setVisibility(View.INVISIBLE);


            }
        });

    }








    @OnClick(R.id.next)
    void nextClick()
    {


        if(!isDataValid(true))
        {
            return;
        }

        checkUsernameExist(true,true);
    }






    void initiateNext()
    {
        if(!isDataValid(true))
        {
            return;
        }


        PrefrenceSignUp.saveUser(user,getActivity());



        if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
        {
            // registering using phone

            if(!phoneIsAvailable)
            {
                // phone is not available for registration so return
                return;
            }


//            showToastMessage("Next Initiated");


            if(getActivity() instanceof ShowFragmentSignUp)
            {
                ((ShowFragmentSignUp) getActivity()).showEnterPassword();
            }

        }
        else if(user.getRt_registration_mode() == User.REGISTRATION_MODE_EMAIL)
        {
            // registering using email

            if(!emailIsAvailable)
            {
                // e-mail is not available for registration so return
                return;
            }


            if(getActivity() instanceof ShowFragmentSignUp)
            {
                ((ShowFragmentSignUp) getActivity()).showEnterPassword();
            }
        }

    }







    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
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
}
