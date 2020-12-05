package org.nearbyshops.enduserappnew.Checkout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.razorpay.PaymentResultListener;

import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import butterknife.ButterKnife;


public class PlaceOrder extends AppCompatActivity implements PaymentResultListener {

    public static final String TAG_FRAGMENT = "place_order_fragment";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);



        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new PlaceOrderFragment(),TAG_FRAGMENT)
                    .commit();
        }
    }





    void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(this,message);
    }







    public static Intent getLaunchIntent(int shopID, Context context)
    {

//        Intent intent = new Intent(getActivity(), PlaceOrder.class);
//        String cartStatsJson = UtilityFunctions.provideGson().toJson(cartStats);
//        intent.putExtra(PlaceOrderFragment.CART_STATS_INTENT_KEY,cartStatsJson);


        Intent intent = new Intent(context,PlaceOrder.class);
        intent.putExtra("shop_id",shopID);

        return intent;
    }









    @Override
    public void onPaymentSuccess(String s) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(fragment instanceof PaymentResultListener)
        {
            ((PaymentResultListener) fragment).onPaymentSuccess(s);
        }
    }




    @Override
    public void onPaymentError(int i, String s) {


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(fragment instanceof PaymentResultListener)
        {
            ((PaymentResultListener) fragment).onPaymentError(i,s);
        }

    }
}
