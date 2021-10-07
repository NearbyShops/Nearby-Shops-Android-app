package org.nearbyshops.whitelabelapp.Login.SignUp.ForgotPassword;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import okhttp3.ResponseBody;

import org.nearbyshops.whitelabelapp.API.UserService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Login.SignUp.PrefSignUp.PrefrenceForgotPassword;
import org.nearbyshops.whitelabelapp.Login.SignUp.Interfaces.ShowFragmentForgotPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentEnterCredentials extends Fragment {


    @BindView(R.id.select_email) TextView selectPhone;
    @BindView(R.id.select_phone) TextView selectEmail;

    @BindView(R.id.text_input_phone)
    TextInputLayout phoneLayout;
    @BindView(R.id.text_input_email) TextInputLayout emailLayout;

//    String phoneWithoutCountryCode;
//    @BindView(R2.id.ccp) CountryCodePicker ccp;
    @BindView(R.id.phone)
TextInputEditText phone;
    @BindView(R.id.email) TextInputEditText email;


    @BindView(R.id.check_icon) ImageView checkIcon;
    @BindView(R.id.cross_icon) ImageView crossIcon;
    @BindView(R.id.message) TextView textAvailable;

    @BindView(R.id.progress_bar) ProgressBar progressBar;


    @BindView(R.id.progress_bar_button) ProgressBar progressBarButton;
    @BindView(R.id.next) TextView nextButton;

    @BindView(R.id.phone_code_message) TextView phoneCodeMessage;



//    int phoneOrEmail = 1; // flag for indicating the input mode 1 for phone and 2 for email


    boolean phoneIsAvailable = false;
    boolean emailIsAvailable = false;

    User user;


    @Inject
    UserService userService;



    @Inject Gson gson;




    boolean isDestroyed = false;




    public FragmentEnterCredentials() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_enter_credentials, container, false);
        ButterKnife.bind(this,rootView);


        user = PrefrenceForgotPassword.getUser(getActivity());

        if(user == null)
        {
            user = new User();
        }



        if (user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
        {
            selectEmailClick();
        }
        else if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
        {
            selectPhoneClick();
        }
        else
        {
            selectPhoneClick();
        }



//
//        if(PrefServiceConfig.getServiceConfig(getActivity())!=null)
//        {
//            ccp.setCountryForNameCode(PrefServiceConfig.getServiceConfig(getActivity()).getISOCountryCode());
//        }
//
//
//
//
//        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected() {
//                textInputChanged();
//            }
//        });
//
//
//        ccp.registerCarrierNumberEditText(phone);
//        ccp.setNumberAutoFormattingEnabled(false);



//        phone.requestFocus();
        bindViews();

        return rootView;
    }





    @OnClick(R.id.select_email)
    void selectEmailClick()
    {
        phoneCodeMessage.setVisibility(View.GONE);

        user.setRt_registration_mode(User.REGISTRATION_MODE_EMAIL);

        email.requestFocus();

        selectPhone.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));

        selectEmail.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
        selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));

        phoneLayout.setVisibility(View.INVISIBLE);
//        ccp.setVisibility(View.INVISIBLE);
        emailLayout.setVisibility(View.VISIBLE);

        bindViews();

        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);

        textInputChanged();
    }



    @OnClick(R.id.select_phone)
    void selectPhoneClick()
    {

        phoneCodeMessage.setVisibility(View.VISIBLE);

        user.setRt_registration_mode(User.REGISTRATION_MODE_PHONE);

        phone.requestFocus();


        selectEmail.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        selectEmail.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.phonographyBlue));

        selectPhone.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueGrey800));
        selectPhone.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.light_grey));


        phoneLayout.setVisibility(View.VISIBLE);
//        ccp.setVisibility(View.VISIBLE);
        emailLayout.setVisibility(View.INVISIBLE);

        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);
        textInputChanged();

    }




    void bindViews()
    {
//            phone.setText(user.getRt_phone_witout_country_code());


            phone.setText(user.getPhone());
            email.setText(user.getEmail());
    }



    void saveDataFromViews()
    {

//            user.setRt_phone_witout_country_code(phone.getText().toString());
            user.setPhone(phone.getText().toString());
//        user.setPhone(ccp.getSelectedCountryCode() + phone.getText().toString());
            user.setEmail(email.getText().toString());


//            showToastMessage("Phone : " + user.getPhone());
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


        progressBar.setVisibility(View.VISIBLE);
        countDownTimer.cancel();  // restart the timer
        countDownTimer.start();
    }



    boolean isDataValid(boolean showError)
    {
        boolean isValid = true;

        if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
        {
            // validate phone

            if(phone.getText().toString().equals(""))
            {
                if(showError)
                {
                    phone.setError("Phone cannot be empty !");
                }

                isValid= false;
            }

//            else if(phone.getText().toString().length()!=10)
//            {
//                if(showError)
//                {
//                    phone.setError("Phone should have 10 digits");
//                }
//
//                isValid=false;
//            }

        }
        else if (user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
        {
            if(email.getText().toString().equals(""))
            {
                if(showError)
                {
                    email.setError("E-mail cannot be empty !");
                }

                isValid = false;
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
            {
                if(showError)
                {
                    email.setError("Not a valid e-mail !");
                }

                isValid = false;
            }

        }


        return isValid;
    }





    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {

        public void onTick(long millisUntilFinished) {


            if(isDestroyed)
            {
                return;
            }


            textAvailable.setVisibility(View.INVISIBLE);
            checkIcon.setVisibility(View.INVISIBLE);
            crossIcon.setVisibility(View.INVISIBLE);
        }

        public void onFinish() {

            if(isDestroyed)
            {
                return;
            }


            checkUsernameExist();
        }
    };






    void checkUsernameExist()
    {
        String username = "";

        if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
        {
            // check for phone
//            user.setPhone(phone.getText().toString());
            username = user.getPhone();
        }
        else if(user.getRt_registration_mode() == User.REGISTRATION_MODE_EMAIL)
        {
            // check for email
//            user.setEmail( email.getText().toString());
            username = user.getEmail();
        }


        Call<ResponseBody> call = userService.checkUsernameExists(username);


        progressBar.setVisibility(View.VISIBLE);
        textAvailable.setVisibility(View.INVISIBLE);
        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }


                progressBar.setVisibility(View.INVISIBLE);

                if(response.code()==200)
                {
                    // username is not unique and already exist


                    // username is unique and available for registration
                    checkIcon.setVisibility(View.VISIBLE);
                    crossIcon.setVisibility(View.INVISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Account Exists !");


                    if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
                    {
                        phoneIsAvailable = true;
                    }
                    else if(user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
                    {
                        emailIsAvailable = true;
                    }

                }
                else if(response.code()==204)
                {


//                    if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
//                    {
//                        phone.setError("Somebody has already registered using that phone !");
//                    }
//                    else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
//                    {
//                        email.setError("Somebody has already registered using that E-mail !");
//                    }

                    checkIcon.setVisibility(View.INVISIBLE);
                    crossIcon.setVisibility(View.VISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);



                    if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
                    {
                        phoneIsAvailable = false;
                        textAvailable.setText("No account exist with this Phone Number !");
                    }
                    else if(user.getRt_registration_mode()== User.REGISTRATION_MODE_EMAIL)
                    {
                        emailIsAvailable = false;
                        textAvailable.setText("No account exist with this Email !");
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
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

        PrefrenceForgotPassword.saveUser(user,getActivity());

        if(user.getRt_registration_mode()== User.REGISTRATION_MODE_PHONE)
        {
            // registering using phone

            if(!phoneIsAvailable)
            {
                // phone is not available for registration so return
                return;
            }


            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            dialog.setTitle("You will receive a verification code !")
                    .setMessage("We will send you an SMS having a verification code. You will be asked to " +
                            "enter the verification code !")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                            showToastMessage("Next Initiated");
                            sendPasswordResetCode();


                        }
                    })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            showToastMessage("Cancelled !");
                        }
                    })
                    .show();

        }
        else if(user.getRt_registration_mode() == User.REGISTRATION_MODE_EMAIL)
        {
            // registering using email

            if(!emailIsAvailable)
            {
                // e-mail is not available for registration so return
                return;
            }


            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());


            dialog.setTitle("You will receive a reset code !")
                    .setMessage("We will send you a password reset code on your e-mail. You will be asked to " +
                            "enter the password reset code !")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sendPasswordResetCode();

                        }
                    })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            showToastMessage("Cancelled !");
                        }
                    })
                    .show();

        }

    }







    void sendPasswordResetCode()
    {
        progressBarButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.INVISIBLE);


        Call<ResponseBody> call = userService.generateResetCode(user);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }



                progressBarButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    if(getActivity() instanceof ShowFragmentForgotPassword)
                    {
                        ((ShowFragmentForgotPassword) getActivity()).showCheckResetCode();
                    }
                }
                else
                {
                    showToastMessage("Failed with code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {


                if(isDestroyed)
                {
                    return;
                }


                progressBarButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                showToastMessage("Network failed !");

            }
        });

    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }




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
}
