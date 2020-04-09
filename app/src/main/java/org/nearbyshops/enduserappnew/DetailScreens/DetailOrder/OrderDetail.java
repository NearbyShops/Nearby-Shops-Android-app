package org.nearbyshops.enduserappnew.DetailScreens.DetailOrder;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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


}
