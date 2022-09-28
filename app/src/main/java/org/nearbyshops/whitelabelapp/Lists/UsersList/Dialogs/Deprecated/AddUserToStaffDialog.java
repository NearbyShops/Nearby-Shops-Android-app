package org.nearbyshops.whitelabelapp.Lists.UsersList.Dialogs.Deprecated;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.google.gson.Gson;

import org.nearbyshops.whitelabelapp.API.StaffService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddUserToStaffDialog extends DialogFragment {




    @BindView(R.id.input_user_id) EditText inputUserID;
    @BindView(R.id.input_secret_code) EditText secretCode;

    @BindView(R.id.add_member) TextView addMember;
    @BindView(R.id.dismiss) TextView dismiss;


    @Inject Gson gson;



    private int role=User.ROLE_DELIVERY_GUY_MARKET_CODE;



    private String userID;
    private int secretCodeInteger;




    @Inject
    StaffService staffService;


    public AddUserToStaffDialog() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }




//    Unbinder unbinder;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.dialog_add_staff, container);
        ButterKnife.bind(this,view);

        return view;
    }





    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }



    public void setRole(int role)
    {
        this.role= role;
    }




    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }









    @OnTextChanged(R.id.input_user_id)
    void editUserID()
    {

        userID = inputUserID.getText().toString();
    }



    @OnTextChanged(R.id.input_secret_code)
    void editSecretCode()
    {
        if(secretCode.getText().toString().length()>0)
        {
            secretCodeInteger = Integer.parseInt(secretCode.getText().toString());
        }
    }







    @BindView(R.id.progress_bar_add_member) ProgressBar progressAddMember;



    @OnClick(R.id.add_member)
    void addMemberClick()
    {

        if(inputUserID.getText().toString().equals(""))
        {
            inputUserID.setError("Please enter User ID");
            inputUserID.requestFocus();

            return;
        }
        else if(secretCode.getText().toString().equals(""))
        {
            secretCode.setError("Please enter Secret Code");
            secretCode.requestFocus();

            return;
        }





        Call<ResponseBody> call = staffService.upgradeUserToStaff(
                PrefLogin.getAuthorizationHeader(getActivity()),
                userID, role,
                secretCodeInteger
        );




        progressAddMember.setVisibility(View.VISIBLE);
        addMember.setVisibility(View.INVISIBLE);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressAddMember.setVisibility(View.INVISIBLE);
                addMember.setVisibility(View.VISIBLE);


                if(!isVisible())
                {
                    return;
                }

                if(response.code()==200)
                {
                    showToastMessage("Successful !");
                }
                else
                {
                    showToastMessage("Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                progressAddMember.setVisibility(View.INVISIBLE);
                addMember.setVisibility(View.VISIBLE);



                if(!isVisible())
                {
                    return;
                }


                showToastMessage("Failed !");
            }
        });


    }




    @OnClick(R.id.dismiss)
    void dismissClick()
    {
        dismiss();
    }


}
