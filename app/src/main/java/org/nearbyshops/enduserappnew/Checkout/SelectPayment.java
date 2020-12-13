package org.nearbyshops.enduserappnew.Checkout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.enduserappnew.R;

import butterknife.ButterKnife;


public class SelectPayment extends AppCompatActivity{



    public static final String TAG_FRAGMENT = "fragment_select_payment";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);



        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new SelectPaymentFragment(),TAG_FRAGMENT)
                    .commit();
        }
    }





    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }








    public static Intent getLaunchIntent(int shopID, Context context)
    {
        Intent intent = new Intent(context, SelectPayment.class);
        intent.putExtra("shop_id",shopID);

        return intent;
    }



}
