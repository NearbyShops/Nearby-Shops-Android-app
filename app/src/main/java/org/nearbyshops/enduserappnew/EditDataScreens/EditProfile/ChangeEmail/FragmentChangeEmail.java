package org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.ChangeEmail;

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
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.multimarketfiles.API_SDS.UserServiceGlobal;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
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

public class FragmentChangeEmail extends Fragment {


    @BindView(R.id.progress_bar_button)
    ProgressBar progressBarButton;
    @BindView(R.id.next)
    TextView nextButton;


    @BindView(R.id.text_input_email)
    TextInputLayout emailLayout;

    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.password) TextInputEditText password;

    @BindView(R.id.check_icon) ImageView checkIcon;
    @BindView(R.id.cross_icon) ImageView crossIcon;
    @BindView(R.id.message) TextView textAvailable;

    @BindView(R.id.progress_bar) ProgressBar progressBar;


    boolean emailIsAvailable = false;

    User user;

    @Inject
    UserService userService;



    @Inject
    Gson gson;


    boolean isDestroyed = false;



    public FragmentChangeEmail() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_change_email, container, false);
        ButterKnife.bind(this, rootView);


        user = PrefChangeEmail.getUser(getActivity());

        if(user==null)
        {
            user = new User();
        }


        email.requestFocus();
        bindViews();
        textInputChanged();

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
        isDestroyed= false;
    }

    void bindViews()
    {
        email.setText(user.getEmail());
    }





    void saveDataFromViews()
    {
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }



    @OnTextChanged({R.id.email})
    void textInputChanged()
    {
        // reset flags
        emailIsAvailable = false;


        textAvailable.setVisibility(View.INVISIBLE);
        checkIcon.setVisibility(View.INVISIBLE);
        crossIcon.setVisibility(View.INVISIBLE);

        if(!isDataValid(false))
        {
            return;
        }

//        saveDataFromViews();


//        checkUsernameExist();


        progressBar.setVisibility(View.VISIBLE);
        countDownTimer.cancel();  // restart the timer
        countDownTimer.start();
    }



    boolean isDataValid(boolean showError)
    {
        boolean isValid = true;

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


        return isValid;
    }



    boolean validatePassword(boolean showError)
    {



        boolean isValid = true;

        if(password.getText().toString().length()==0)
        {
            if(showError)
            {
                password.requestFocus();
                password.setError("Password is empty. Please provide your password !");
            }

            isValid = false;

        }

        return isValid;
    }









    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {

        public void onTick(long millisUntilFinished) {

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
        String inputName = "";

        inputName = email.getText().toString();


        Call<ResponseBody> call;


        if(PrefGeneral.isMultiMarketEnabled(getActivity()))
        {
            // multi market mode enabled ... so use a global endpoint

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();


            call = retrofit.create(UserServiceGlobal.class).checkUsernameExists(inputName);

        }
        else
        {
            call = userService.checkUsernameExists(inputName);
        }





        progressBar.setVisibility(View.VISIBLE);

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


                    email.setError("An account already with that E-mail. Please use another e-mail !");

                    checkIcon.setVisibility(View.INVISIBLE);
                    crossIcon.setVisibility(View.VISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Not Available for Registration !");


                    emailIsAvailable = false;

                }
                else if(response.code()==204)
                {
                    // username is unique and available for registration
                    checkIcon.setVisibility(View.VISIBLE);
                    crossIcon.setVisibility(View.INVISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Available for Registration !");


                    emailIsAvailable = true;
                }
                else
                {
                    // username is unique and available for registration
                    checkIcon.setVisibility(View.INVISIBLE);
                    crossIcon.setVisibility(View.VISIBLE);

                    textAvailable.setVisibility(View.VISIBLE);
                    textAvailable.setText("Failed Code : " + response.code());

                    emailIsAvailable = false;
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




//        if(!validatePassword(true))
//        {
//            return;
//        }




        saveDataFromViews();

        PrefChangeEmail.saveUser(user,getActivity());



        // registering using email

        if(!emailIsAvailable)
        {
            // e-mail is not available for registration so return
            return;
        }





        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());


        dialog.setTitle("You will receive a verification code !")
                .setMessage("We will send you a verification code on your e-mail. You will be asked to " +
                        "enter the verification code !")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sendEmailVerificationCode();
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




    void sendEmailVerificationCode()
    {


        progressBarButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.INVISIBLE);



        Call<ResponseBody> call;

        if(PrefGeneral.isMultiMarketEnabled(getActivity()))
        {
            // multi market mode enabled ... so use a global endpoint

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();


            call = retrofit.create(UserServiceGlobal.class).sendVerificationEmail(
                    user.getEmail()
            );


        }
        else
        {
            call = userService.sendVerificationEmail(
                    user.getEmail()
            );
        }





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


                    if(getActivity() instanceof ShowFragmentChangeEmail)
                    {
                        ((ShowFragmentChangeEmail) getActivity()).showVerifyEmail();
                    }

                }
                else if(response.code()==304)
                {
                    showToastMessage("Failed to send verification code. Please try again !");
                }
                else
                {
                    showToastMessage("Failed to send verification code. Please try again !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                progressBarButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                showToastMessage("Failed to send verification code. Please try again !");

            }
        });


    }









    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

}
