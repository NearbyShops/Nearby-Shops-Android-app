package org.nearbyshops.enduserappnew.adminModule;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Model.ModelUtility.PushNotificationData;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.PushFirebase.MessagingService;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PushNotificationComposer extends DialogFragment {




    @BindView(R.id.input_title) EditText title;
    @BindView(R.id.input_description) EditText description;
    @BindView(R.id.image_url) EditText imageURL;

    @BindView(R.id.send_button) TextView sendButton;

    @BindView(R.id.check_end_user) CheckBox checkBoxEndUser;
    @BindView(R.id.check_delivery_staff) CheckBox checkDelivery;
    @BindView(R.id.check_vendor) CheckBox checkVendor;


    @Inject Gson gson;



    @Inject
    ServiceConfigurationService staffService;




    public PushNotificationComposer() {
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


        View view = inflater.inflate(R.layout.dialog_push_nofication_composer, container);
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




    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(getActivity(),message);
    }





    @BindView(R.id.progress_bar) ProgressBar progressBar;



    @OnClick(R.id.send_button)
    void addMemberClick()
    {



        if(title.getText().toString().equals(""))
        {
            title.setError("Please enter Title");
            title.requestFocus();

            return;
        }
        else if(description.getText().toString().equals(""))
        {
            description.setError("Please enter Message");
            description.requestFocus();

            return;
        }



        Market localConfig = PrefServiceConfig.getServiceConfigLocal(MyApplication.getAppContext());

        List<String> topicList = new ArrayList<>();


        if(checkBoxEndUser.isChecked())
        {
            topicList.add(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_END_USER);
        }

        if(checkDelivery.isChecked())
        {
            topicList.add(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_DELIVERY_STAFF);
        }


        if(checkVendor.isChecked())
        {
            topicList.add(localConfig.getRt_market_id_for_fcm()  + MessagingService.CHANNEL_SHOP_STAFF);
        }


        PushNotificationData data = new PushNotificationData();
        data.setTitle(title.getText().toString());
        data.setDescription(description.getText().toString());
        data.setImageURL(imageURL.getText().toString());
        data.setTopicList(topicList);



        Call<ResponseBody> call = staffService.sendPushNotification(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                data
        );




        progressBar.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.INVISIBLE);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBar.setVisibility(View.INVISIBLE);
                sendButton.setVisibility(View.VISIBLE);

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


                progressBar.setVisibility(View.INVISIBLE);
                sendButton.setVisibility(View.VISIBLE);

            }
        });

    }




}
