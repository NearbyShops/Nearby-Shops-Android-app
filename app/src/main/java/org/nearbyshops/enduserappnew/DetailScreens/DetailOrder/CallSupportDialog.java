package org.nearbyshops.enduserappnew.DetailScreens.DetailOrder;

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

import org.nearbyshops.enduserappnew.API.StaffService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CallSupportDialog extends DialogFragment {



    @Inject Gson gson;


    String deliveryBoyPhone;
    String marketPhone;


    @BindView(R.id.delivery_boy_phone) TextView deliveryBoyPhoneText;
    @BindView(R.id.market_phone) TextView marketPhoneText;


    @Inject
    StaffService staffService;


    public CallSupportDialog() {
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


        View view = inflater.inflate(R.layout.dialog_call_support, container);
        ButterKnife.bind(this,view);

        if(deliveryBoyPhone==null || deliveryBoyPhone.equals(""))
        {
            deliveryBoyPhoneText.setText(" - ");
        }
        else
        {
            deliveryBoyPhoneText.setText(deliveryBoyPhone);
        }


        marketPhoneText.setText(marketPhone);

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



    public void setDeliveryBoyPhone(String deliveryBoyPhone)
    {
        this.deliveryBoyPhone = deliveryBoyPhone;

    }




    public void setMarketPhone(String marketPhone)
    {
        this.marketPhone = marketPhone;
    }



    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }



}
