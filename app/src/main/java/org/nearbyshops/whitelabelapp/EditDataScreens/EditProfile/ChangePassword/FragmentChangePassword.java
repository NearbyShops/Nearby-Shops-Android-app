package org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangePassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;

import okhttp3.ResponseBody;

import org.nearbyshops.whitelabelapp.API.UserService;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

/**
 * Created by sumeet on 15/4/17.
 */

public class FragmentChangePassword extends Fragment {



    @Inject
    Gson gson;

    @Inject
    UserService userService;


    @BindView(R.id.password) EditText password;
    @BindView(R.id.password_new) EditText passwordNew;
    @BindView(R.id.password_confirm) EditText passwordConfirm;
    @BindView(R.id.progress_bar) ProgressBar progressBar;



    private boolean isDestroyed = false;


    public FragmentChangePassword() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this,rootView);

        setActionBarTitle();


        // check if token expired and renew
//        checkTokenExpired(false);

        return rootView;
    }





    private void setActionBarTitle()
    {
        if(getActivity() instanceof AppCompatActivity)
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if(actionBar!=null)
            {
                actionBar.setTitle("Change Password");
            }
        }
    }






    private boolean validatePassword()
    {
        boolean isValid = true;


        if(passwordConfirm.getText().toString().length()==0)
        {
            passwordConfirm.requestFocus();
            passwordConfirm.setError("Cannot be empty !");
            isValid = false;
        }



        if(passwordNew.getText().toString().length()<6)
        {
            passwordNew.requestFocus();
            passwordNew.setError("Password should not be less than 6 characters.");
            isValid = false;
        }


        if(passwordNew.getText().toString().length()==0)
        {
            passwordNew.requestFocus();
            passwordNew.setError("Cannot be empty !");
            isValid = false;
        }





//        boolean isGlobalProfile = getActivity().getIntent().getBooleanExtra(EditProfile.TAG_IS_GLOBAL_PROFILE,false);


//        if(PrefGeneral.getMultiMarketMode(getActivity()))
//        {
//            if(!password.getText().toString().equals(PrefLoginGlobal.getPassword(getContext())))
//            {
//                password.requestFocus();
//                password.setError("Wrong Password !");
//                isValid = false;
//            }
//
//        }
//        else
//        {
//            if(!password.getText().toString().equals(PrefLogin.getPassword(getContext())))
//            {
//                password.requestFocus();
//                password.setError("Wrong Password !");
//                isValid = false;
//            }
//        }




        if(password.getText().toString().length()==0)
        {
            password.requestFocus();
            password.setError("Cannot be empty !");
            isValid = false;
        }






        if(!passwordConfirm.getText().toString().equals(passwordNew.getText().toString()))
        {
            passwordConfirm.requestFocus();
            passwordConfirm.setError("Passwords do not match !");
            isValid = false;
        }


        return isValid;

    }







    @OnClick(R.id.change_password)
    void updatePassword()
    {
        if(!validatePassword())
        {
            return;



        }

        User user = new User();

        user.setUsername(PrefLogin.getUsername(getContext()));
        user.setPhone(PrefLogin.getUsername(getContext()));
        user.setEmail(PrefLogin.getUsername(getContext()));

        // new password required here
        user.setPassword(passwordNew.getText().toString());

        progressBar.setVisibility(View.VISIBLE);


//        boolean isGlobalProfile = getActivity().getIntent().getBooleanExtra(EditProfile.TAG_IS_GLOBAL_PROFILE,false);

        Call<ResponseBody> call  = userService.changePassword(
                PrefLogin.getAuthorizationHeader(getContext()),
                user,password.getText().toString()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {
                    showToastMessage("Password changed Successfully !");

                    // update the new password so the tokens can be renewed without error
                    PrefLogin.savePassword(getActivity(),passwordNew.getText().toString());



                }
                else if(response.code()==304)
                {
                    showToastMessage("Password Not Changed !");
                }
                else
                {
                    showToastMessage("Failed with error code : " + response.code());
                }


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Network connection failure !");

                progressBar.setVisibility(View.GONE);
            }
        });


    }





    private void showToastMessage(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onResume() {
        super.onResume();
        isDestroyed= false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }




}
