package org.nearbyshops.whitelabelapp.Lists.ItemsInShopByCategory;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;

import butterknife.ButterKnife;

import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.R;

import static android.content.ContentValues.TAG;


public class ItemsInShopByCat extends AppCompatActivity{

    public static final String TAG_FRAGMENT = "items_in_stock_simple";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,ItemsInShopByCatFragment.newInstance(false,
                            ItemsInShopByCatFragment.MODE_ITEMS_IN_SHOP_MULTI_VENDOR),
                            TAG_FRAGMENT)
                    .commit();
        }

//        handleFirebaseDynamicLink();
    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        //notifyBackPressed !=null

        if(fragment instanceof NotifyBackPressed)
        {
            if(((NotifyBackPressed) fragment).backPressed())
            {
                super.onBackPressed();
            }
        }
        else
        {
            super.onBackPressed();
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_by_cat_simple, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setQueryHint("Enter Item Name ...");


        MenuItem item = menu.findItem(R.id.action_search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if (fragment instanceof NotifySearch) {
                    ((NotifySearch) fragment).endSearchMode();
                }

//                Toast.makeText(Home.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();

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


            if (fragment instanceof NotifySearch) {
                ((NotifySearch) fragment).search(query);
            }
        }



        
//        if(intent.getData()!=null)
//        {
//
//            String shopID = intent.getData().getQueryParameter("shop_id");
//
//            showToastMessage("Shop ID : "  + shopID);
//
//            System.out.println("Shop ID : "  + shopID);
//        }


    }






}
