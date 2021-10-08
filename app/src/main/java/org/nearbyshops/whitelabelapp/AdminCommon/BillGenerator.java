package org.nearbyshops.whitelabelapp.AdminCommon;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.nearbyshops.whitelabelapp.API.API_Admin.MarketSettingService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopUtilityService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Model.ModelUtility.PushNotificationData;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.PushFirebase.MessagingService;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.databinding.DialogBillGeneratorBinding;
import org.nearbyshops.whitelabelapp.databinding.DialogPushNoficationComposerBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BillGenerator extends DialogFragment {



    DialogBillGeneratorBinding binding;


    @Inject
    ShopUtilityService staffService;




    public BillGenerator() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DialogBillGeneratorBinding.inflate(inflater);
        return binding.getRoot();
    }




    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendButtonClick();
            }
        });
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }




    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }




    void sendButtonClick()
    {

        if(binding.billAmount.getText().toString().equals(""))
        {

            binding.billAmount.setError("Enter Bill Amount !");
            binding.billAmount.requestFocus();
            return;
        }


        if(binding.inputTitle.getText().toString().equals(""))
        {
            binding.inputTitle.setError("Please enter Title");
            binding.inputTitle.requestFocus();

            return;
        }
        else if(binding.inputDescription.getText().toString().equals(""))
        {
            binding.inputDescription.setError("Please enter Message");
            binding.inputDescription.requestFocus();

            return;
        }



        Call<ResponseBody> call = staffService.generateBills(
                PrefLogin.getAuthorizationHeader(getActivity()),
                Double.parseDouble(binding.billAmount.getText().toString()),
                binding.inputTitle.getText().toString(),
                binding.inputDescription.getText().toString()
        );



        binding.progressBar.setVisibility(View.VISIBLE);
        binding.sendButton.setVisibility(View.INVISIBLE);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.sendButton.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    showToastMessage("Successful !");
//                    dismiss();
                }
                else
                {
                    showToastMessage("Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.sendButton.setVisibility(View.VISIBLE);

            }
        });

    }


}
