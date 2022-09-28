package org.nearbyshops.whitelabelapp.Login.InvalidLogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.whitelabelapp.R;

import butterknife.ButterKnife;


public class InvalidLogin extends AppCompatActivity{



    public static final String TAG_FRAGMENT = "fragment_invalid_login";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);



        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new InvalidLoginMessageFragment(),TAG_FRAGMENT)
                    .commit();
        }
    }





    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }








    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, InvalidLogin.class);
    }



}
