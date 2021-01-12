package org.nearbyshops.enduserappnew.Login;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.Interfaces.ShowFragmentSelectService;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.multimarketfiles.LoginGlobalUsingOTPFragment;


public class Login extends AppCompatActivity implements ShowFragmentSelectService, NotifyAboutLogin {


    public static final String TAG_STEP_ONE = "tag_step_one";
    public static final String TAG_STEP_TWO = "tag_step_two";
    public static final String TAG_STEP_THREE = "tag_step_three";
    public static final String TAG_STEP_FOUR = "tag_step_four";

    public static final String TAG_SELECT_SERVICE = "select_service";







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

        Market configurationLocal = PrefServiceConfig.getServiceConfigLocal(this);






        if(PrefGeneral.isMultiMarketEnabled(this))
        {
            if(savedInstanceState==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new LoginGlobalUsingOTPFragment(),TAG_STEP_ONE)
                        .commitNow();
            }

        }
        else
        {
            if(configurationLocal!=null)
            {
                if(configurationLocal.isRt_login_using_otp_enabled())
                {

                    if(savedInstanceState==null)
                    {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,new LoginLocalUsingOTPFragmentNew(),TAG_STEP_ONE)
                                .commitNow();
                    }

                }
                else
                {
                    if(savedInstanceState==null)
                    {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,new LoginLocalUsingPasswordFragment(),TAG_STEP_ONE)
                                .commitNow();
                    }
                }

            }
            else
            {

                if(savedInstanceState==null)
                {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,new LoginLocalUsingPasswordFragment(),TAG_STEP_ONE)
                            .commitNow();
                }
//                showToastMessage(" ... try again later !");
//                finish();
            }
        }


    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }







    @Override
    public void showSelectServiceFragment() {


//        if(getSupportFragmentManager().findFragmentByTag(TAG_SELECT_SERVICE)==null)
//        {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
//                    .replace(R.id.fragment_container,new SelectServiceFragment(),TAG_SELECT_SERVICE)
//                    .addToBackStack("select_service")
//                    .commit();
//        }

    }







    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
    }




    public static final int RESULT_CODE_LOGIN_SUCCESS  = 1;









    @Override
    public void loginSuccess() {


        setResult(RESULT_OK);
        finish();
    }




    @Override
    public void loggedOut() {

    }


}
