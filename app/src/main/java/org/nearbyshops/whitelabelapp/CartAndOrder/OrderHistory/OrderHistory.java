package org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.R;


public class OrderHistory extends AppCompatActivity  implements SetToolbarText {





    public static final String IS_FILTER_BY_SHOP = "IS_FILTER_BY_SHOP";


    Toolbar toolbar;
    TextView toolbarHeader;
    TextView toolbarText;



    void bindViews()
    {
        toolbarHeader = findViewById(R.id.toolbar_header);
        toolbarText = findViewById(R.id.toolbar_text);
        toolbar = findViewById(R.id.toolbar);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container_with_toolbar);
        ButterKnife.bind(this);

        bindViews();


        int shopID = 0;

        if(PrefShopAdminHome.getShop(this)!=null)
        {
            shopID = PrefShopAdminHome.getShop(this).getShopID();
        }


        int currentMode = getIntent().getIntExtra(OrdersHistoryFragment.USER_MODE_INTENT_KEY,OrdersHistoryFragment.MODE_END_USER);


        if (getSupportFragmentManager().findFragmentByTag("order_history_fragment") == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,
                            OrdersHistoryFragment.newInstance(0,shopID,currentMode),
                            "order_history_fragment")
                    .commit();
        }


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_history_new, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));



        MenuItem item = menu.findItem(R.id.action_search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {


                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


                if(fragment instanceof NotifySearch)
                {
                    ((NotifySearch) fragment).endSearchMode();
                }

//                Toast.makeText(OrderHistoryHD.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();

                return true;
            }
        });



        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

//            Toast.makeText(this,query,Toast.LENGTH_SHORT).show();

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }




    public static Intent getLaunchIntent(int screenMode, Context context)
    {
        Intent intent = new Intent(context, OrderHistory.class);
        intent.putExtra(OrdersHistoryFragment.USER_MODE_INTENT_KEY,screenMode);
        return intent;
    }



    @Override
    public void setToolbar(boolean toolbarVisible, String header, boolean isheaderBold, String subtitle) {

        toolbarHeader.setText(header);

        if(subtitle==null)
        {
            toolbarText.setVisibility(View.GONE);
        }
    }
}
