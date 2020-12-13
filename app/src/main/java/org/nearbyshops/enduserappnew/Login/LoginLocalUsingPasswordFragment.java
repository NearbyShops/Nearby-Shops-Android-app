package org.nearbyshops.enduserappnew.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;

import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Login.SignUp.ForgotPassword.ForgotPassword;
import org.nearbyshops.enduserappnew.Login.SignUp.PrefSignUp.PrefrenceForgotPassword;
import org.nearbyshops.enduserappnew.Login.SignUp.PrefSignUp.PrefrenceSignUp;
import org.nearbyshops.enduserappnew.Login.SignUp.SignUp;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;

/**
 * Created by sumeet on 19/4/17.
 */

public class LoginLocalUsingPasswordFragment extends Fragment {

    public static final String TAG_SERVICE_INDICATOR = "service_indicator";

    boolean isDestroyed = false;


    @Inject Gson gson;
//    @BindView(R.id.ccp) CountryCodePicker ccp;
    @BindView(R.id.username) TextInputEditText username;
    @BindView(R.id.password) TextInputEditText password;
    @BindView(R.id.progress_bar_login) ProgressBar progressBar;

//    @BindView(R.id.clear)TextView clear;
//    @BindView(R.id.select_service) TextView selectAutomatic;


    public LoginLocalUsingPasswordFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,rootView);


        if(getChildFragmentManager().findFragmentByTag(TAG_SERVICE_INDICATOR)==null)
        {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.service_indicator,new ServiceIndicatorFragment(),TAG_SERVICE_INDICATOR)
                    .commit();
        }



        return rootView;
    }







    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }









    @OnClick(R.id.sign_up)
    void signUp()
    {

        PrefrenceSignUp.saveUser(null,getActivity());
        Intent intent = new Intent(getActivity(), SignUp.class);
        startActivity(intent);
    }






    @OnClick(R.id.forgot_password)
    void forgotPasswordClick()
    {
        PrefrenceForgotPassword.saveUser(null,getActivity());
        Intent intent = new Intent(getActivity(), ForgotPassword.class);
        startActivity(intent);
    }





    @OnTextChanged(R.id.username)
    void usernameChanged()
    {
//        UtilityLogin.saveUsername(getActivity(),username.getText().toString());
    }



    @OnTextChanged(R.id.password)
    void passwordChanged()
    {
//        UtilityLogin.savePassword(getActivity(),password.getText().toString());
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
            password.setError("Password cannot be empty !");
            isValid = false;
        }


//        if(!emailValidity && !phoneValidity)
//        {
//            username.setError("Not a valid email or phone !");
//            username.requestFocus();
//
//            isValid = false;
//        }

        if(username.getText().toString().isEmpty())
        {
            password.requestFocus();
            username.setError("username cannot be empty !");
            username.requestFocus();

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




    @BindView(R.id.login)
    Button loginButton;





    @OnClick(R.id.login)
    void makeRequestLogin()
    {

        if(!validateData())
        {
            // validation failed return
            return;
        }





        final String phoneWithCode = username.getText().toString();
//        final String phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



        Call<User> call = retrofit.create(UserService.class).getToken(
                PrefLogin.baseEncoding(phoneWithCode,password.getText().toString())
        );





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


//                    PrefLogin.saveCredentialsPassword(
//                            getActivity(),
//                            phoneWithCode,
//                            password.getText().toString()
//
//                    );




                    PrefLogin.saveToken(
                            getActivity(),
                            phoneWithCode,
                            response.body().getToken()
                    );





                    // save user profile information
                    PrefLogin.saveUserProfile(
                            response.body(),
                            getActivity()
                    );



                    UtilityFunctions.updateFirebaseSubscriptions();





//                    PrefOneSignal.saveToken(getActivity(),PrefOneSignal.getLastToken(getActivity()));
//
//                    if(PrefOneSignal.getToken(getActivity())!=null)
//                    {
//                        // update one signal id if its not updated
//                        getActivity().startService(new Intent(getActivity(), UpdateOneSignalID.class));
//                    }










                    if(getActivity() instanceof NotifyAboutLogin)
                    {
//                        showToastMessage("Notify about login !");
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





}
