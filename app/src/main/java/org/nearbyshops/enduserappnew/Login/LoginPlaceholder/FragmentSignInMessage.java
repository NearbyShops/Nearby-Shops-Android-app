package org.nearbyshops.enduserappnew.Login.LoginPlaceholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentSignInMessage extends Fragment {




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_sign_in_message, container, false);
        ButterKnife.bind(this,rootView);


        return rootView;
    }




    @OnClick(R.id.sign_in_button)
    void signInClick()
    {

        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent,1);

//        showLogin();
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1)
        {
            if(getActivity() instanceof NotifyAboutLogin)
            {
//                showToastMessage("Notify about login !");
                ((NotifyAboutLogin) getActivity()).loginSuccess();
            }
        }
    }






    void showLogin()
    {
        if(getActivity()==null)
        {
            return;
        }

        Market configurationLocal = PrefServiceConfig.getServiceConfigLocal(getActivity());



        if(PrefGeneral.isMultiMarketEnabled(getActivity()))
        {
            NavHostFragment.findNavController(this).navigate(R.id.loginGlobalUsingOTPFragment);
        }
        else
        {
            if(configurationLocal!=null)
            {
                if(configurationLocal.isRt_login_using_otp_enabled())
                {
                    NavHostFragment.findNavController(this).navigate(R.id.loginLocalUsingOTPFragmentNew);
                }
                else
                {
                    NavHostFragment.findNavController(this).navigate(R.id.loginLocalUsingPasswordFragment);
                }

            }
            else
            {
                NavHostFragment.findNavController(this).navigate(R.id.loginLocalUsingPasswordFragment);
            }
        }

    }





    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

}
