package org.nearbyshops.whitelabelapp.Login.SignUp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.textfield.TextInputEditText;


import org.nearbyshops.whitelabelapp.Login.SignUp.Interfaces.ShowFragmentSignUp;
import org.nearbyshops.whitelabelapp.Login.SignUp.PrefSignUp.PrefrenceSignUp;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.R;


/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentEnterName extends Fragment {


    @BindView(R.id.name) TextInputEditText name;
    @BindView(R.id.referrar_user_id) TextInputEditText referrerUserID;



    User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_enter_name, container, false);
        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R2.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        user = PrefrenceSignUp.getUser(getActivity());

        if(user == null)
        {
            user = new User();
        }

        bindViews();


        return rootView;
    }


//    void bindViews()
//    {
//
//        if(getActivity() instanceof ReadWriteUser)
//        {
//            User user = ((ReadWriteUser) getActivity()).getSignUpProfile();
//
//            name.setText(user.getName());
//            name.requestFocus();
//        }
//
//    }


//    void saveDataFromViews()
//    {
//        if(getActivity() instanceof ReadWriteUser)
//        {
//            User user = ((ReadWriteUser) getActivity()).getSignUpProfile();
//
//            user.setName(name.getText().toString());
//
//            ((ReadWriteUser) getActivity()).setSignUpProfile(user);
//
//        }
//    }



    private void bindViews()
    {
//        User user = PrefrenceSignUp.getUser(getActivity());
//
//        if(user == null)
//        {
//            return;
//        }

        name.setText(user.getName());
        name.requestFocus();

        referrerUserID.setText(String.valueOf(user.getReferredBy()));
    }




    private void saveDataFromViews()
    {
        user.setName(name.getText().toString());


        if(!referrerUserID.getText().toString().equals(""))
        {
            try{

                int referrerID = Integer.parseInt(referrerUserID.getText().toString());

                user.setReferredBy(referrerID);

            }
            catch (Exception ex)
            {

            }

        }
    }





    @OnClick(R.id.next)
    void nextClick()
    {
        if(name.getText().toString().equals(""))
        {
            name.requestFocus();
            name.setError("Please enter your name !");
            return;
        }


        saveDataFromViews();


        PrefrenceSignUp.saveUser(user,getActivity());

        if(getActivity() instanceof ShowFragmentSignUp)
        {
            ((ShowFragmentSignUp) getActivity()).showEmailPhone();
        }
    }




}
