package org.nearbyshops.enduserappnew.Login.SignUp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;


import org.nearbyshops.enduserappnew.Login.SignUp.Interfaces.ShowFragmentSignUp;
import org.nearbyshops.enduserappnew.R;


public class SignUp extends AppCompatActivity implements ShowFragmentSignUp {


    public static final String TAG_STEP_ONE = "tag_step_one";
    public static final String TAG_STEP_TWO = "tag_step_two";
    public static final String TAG_STEP_THREE = "tag_step_three";
    public static final String TAG_STEP_FOUR = "tag_step_four";

//    User signUpProfile = new User();

    SmsVerifyCatcher smsVerifyCatcher;











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

//        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setTitle("Sign Up");
        setSupportActionBar(toolbar);



//        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
//        {


        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new FragmentEnterName(),TAG_STEP_ONE)
                    .commitNow();
        }





        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {


            }
        });

//        }
    }







    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
    }



    @Override
    public void showEmailPhone() {

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.fragment_container,new FragmentEmailOrPhone(),TAG_STEP_TWO)
                    .addToBackStack("step_two")
                    .commit();

//
    }








    @Override
    public void showVerifyEmail() {

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_container,new FragmentVerify(),TAG_STEP_THREE)
                .addToBackStack("step_three")
                .commit();
    }




    @Override
    public void showEnterPassword() {

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_container,new FragmentEnterPassword(),TAG_STEP_FOUR)
                .addToBackStack("step_four")
                .commit();

//                        .addToBackStack("step_four")
    }







    @Override
    public void showResultSuccess() {

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_container,new FragmentResult())
                .commit();

    }




//    @Override
//    public User getSignUpProfile() {
//        return signUpProfile;
//    }
//
//    @Override
//    public void setSignUpProfile(User signUpProfile) {
//        this.signUpProfile = signUpProfile;
//    }
//


    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }


    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
