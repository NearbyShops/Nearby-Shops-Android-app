package org.nearbyshops.enduserappnew.adminModule.AddCredit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentAddCredit extends Fragment {

    @Inject
    ShopService shopService;



    @BindView(R.id.amount_to_add) EditText amount;
//    @BindView(R.id.radio_app_account) RadioButton appAccount;
//    @BindView(R.id.radio_tax_account) RadioButton taxAccount;
//    @BindView(R.id.radio_service_requests) RadioButton serviceRequestsAccount;

    @BindView(R.id.next) TextView addCreditButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;




    public FragmentAddCredit() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_add_credit, container, false);
        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        return rootView;
    }











    @OnClick(R.id.next)
    void nextClick()
    {
//        if(appAccount.isChecked())
//        {
//
//        }


        addCredit();

    }









    private void addCredit()
    {

        if(amount.getText().toString().length()==0)
        {
            amount.setError("Please enter amount");
            amount.requestFocus();
            return;
        }



        addCreditButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = shopService.addBalance(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                getActivity().getIntent().getIntExtra("tag_user_id",0),
                Double.parseDouble(amount.getText().toString())

        );



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                if(response.code()==200)
                {

                    showToast("Credit added Successfully !");
                    getActivity().finish();
                }
                else if(response.code()==401 || response.code()==403)
                {

                    showToast("Not Permitted !");
                }
                else
                {
                    showToast("Failed Response Code : " + response.code());
                }



                addCreditButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(!isVisible())
                {
                    return;
                }



                addCreditButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }





    private void showToast(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }





    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }



    private boolean isDestroyed = false;

}
