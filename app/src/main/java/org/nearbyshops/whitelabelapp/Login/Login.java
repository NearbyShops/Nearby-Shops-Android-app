package org.nearbyshops.whitelabelapp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.nearbyshops.whitelabelapp.LaunchActivity;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyAboutLogin;
import org.nearbyshops.whitelabelapp.R;


public class Login extends AppCompatActivity implements NotifyAboutLogin {


    public static final String TAG_STEP_ONE = "tag_step_one";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

//        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);

        setContentView(R.layout.activity_fragment_container);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
//        toolbar.setTitle("LoginUsingOTP");
//        setSupportActionBar(toolbar);


        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new LoginUsingOTPFragment(),TAG_STEP_ONE)
                    .commitNow();
        }

    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }






    @Override
    public void loginSuccess() {


//        if(getResources().getInteger(R.integer.app_type)==getResources().getInteger(R.integer.app_type_market_admin_app))
//        {
////            Intent intent = new Intent(this, MarketDashboardBottom.class);
//
//            User user = PrefLogin.getUser(this);
//
//            if(user.getRole()==User.ROLE_MARKET_ADMIN_CODE || user.getRole()==User.ROLE_MARKET_STAFF_CODE)
//            {
//            }
//            else if(user.getRole()==User.ROLE_END_USER_CODE)
//            {
//
//            }
//            else
//            {
//                // only new users or existing market admin or staff are permitted to login !
////                UtilityFunctions.showToastMessage(this,"Login Not Permitted !");
////                UtilityFunctions.logout(this);
//
//            }
//
//
//        }
//        else if(getResources().getInteger(R.integer.app_type)==getResources().getInteger(R.integer.app_type_vendor_app))
//        {
////            Intent intent = new Intent(this, MarketDashboardBottom.class);
//
//            User user = PrefLogin.getUser(this);
//
//            if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE || user.getRole()==User.ROLE_SHOP_STAFF_CODE)
//            {
//            }
//            else if(user.getRole()==User.ROLE_END_USER_CODE)
//            {
//
//            }
//            else
//            {
//                // only new users or existing market admin or staff are permitted to login !
////                UtilityFunctions.showToastMessage(this,"Login Not Permitted !");
////                UtilityFunctions.logout(this);
//            }
//
//
//
//        }





        if(getResources().getInteger(R.integer.app_type)!=getResources().getInteger(R.integer.app_type_main_app))
        {
            Intent intent = new Intent(this, LaunchActivity.class);
            startActivity(intent);
        }


        setResult(RESULT_OK);
        finish();

    }




    @Override
    public void loggedOut() {

    }


}
