package org.nearbyshops.whitelabelapp.Login.LoginPlaceholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyAboutLogin;
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.Login.Login;
import org.nearbyshops.whitelabelapp.R;

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

        if(getActivity() instanceof SetToolbarText)
        {
            ((SetToolbarText) getActivity()).setToolbar(false,null,false,null);
        }

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








    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

}
