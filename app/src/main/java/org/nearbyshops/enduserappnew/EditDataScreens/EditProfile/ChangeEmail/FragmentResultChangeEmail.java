package org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.ChangeEmail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.R;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentResultChangeEmail extends Fragment {


    @BindView(R.id.account_credentials)
    TextView accountCredentials;

    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_result_change_email, container, false);
        ButterKnife.bind(this,rootView);

        user = PrefChangeEmail.getUser(getActivity());


        accountCredentials.setText("E-mail : " + user.getEmail()
        + "\nPassword : XXXXX (Password is hidden)");

        return rootView;
    }




    @OnClick(R.id.finish_button)
    void finishButton()
    {

        // reset preferences
        PrefChangeEmail.saveUser(null,getActivity());

        getActivity().setResult(10);
        getActivity().finish();
    }


}
