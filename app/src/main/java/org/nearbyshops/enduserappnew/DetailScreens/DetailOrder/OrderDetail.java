package org.nearbyshops.enduserappnew.DetailScreens.DetailOrder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.enduserappnew.R;


public class OrderDetail extends AppCompatActivity {




    public static final String FRAGMENT_ORDER_DETAIL = "FRAGMENT_ORDER_DETAIL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);



        if(getSupportFragmentManager().findFragmentByTag(FRAGMENT_ORDER_DETAIL)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new FragmentOrderDetail(),FRAGMENT_ORDER_DETAIL)
                    .commit();
        }
    }




    public static Intent getLaunchIntent(int orderID, Context context)
    {
        Intent intent = new Intent(context,OrderDetail.class);
        intent.putExtra("order_id",orderID);

        return intent;
    }


}
