package org.nearbyshops.enduserappnew.InventoryOrders.InventoryHomeDelivery;

import android.app.SearchManager;
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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserappnew.Interfaces.RefreshAdjacentFragment;
import org.nearbyshops.enduserappnew.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeDelivery extends AppCompatActivity implements NotifyTitleChanged, ViewPager.OnPageChangeListener, NotifySort,
        RefreshAdjacentFragment {


    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_delivery);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);

        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the ShopList/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }





    // methods for getting fragment reference

    /*public PlaceholderFragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return  getSupportFragmentManager().findFragmentByTag(name);
    }*/

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }



    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {

        mPagerAdapter.setTitle(title,tabPosition);
    }





    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }




    @Override
    public void onPageSelected(int position) {

        if(position==2||position==3||position==4 || position==5 || position==6)
        {
//            handoverButton.setVisibility(View.VISIBLE);

            if(position==3 || position==4  || position==5)
            {
                handoverButton.setText("Filter\nby Delivery");
            }
            else
            {
                handoverButton.setText("Handover\nto Delivery");
            }

        }
        else
        {
            handoverButton.setVisibility(View.GONE);
        }

        handoverButton.setVisibility(View.GONE);
    }





    @Override
    public void onPageScrollStateChanged(int state) {

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_history_hd, menu);


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


                Fragment fragment = getSupportFragmentManager()
                        .findFragmentByTag(
                                makeFragmentName(mViewPager.getId(),mViewPager.getCurrentItem())
                        );


                if(fragment instanceof NotifySearch)
                {
                    ((NotifySearch) fragment).endSearchMode();
                }

                return true;
            }
        });




        return true;
    }






    @Override
    public void refreshAdjacentFragment()
    {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(
                        makeFragmentName(mViewPager.getId(),mViewPager.getCurrentItem()+1)
                );


        if(fragment instanceof RefreshFragment)
        {
            ((RefreshFragment) fragment).refreshFragment();
        }

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

//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

            Fragment fragment = getSupportFragmentManager()
                    .findFragmentByTag(
                            makeFragmentName(mViewPager.getId(),mViewPager.getCurrentItem())
                    );


            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }



    @Override
    public void notifySortChanged() {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(
                        makeFragmentName(mViewPager.getId(),mViewPager.getCurrentItem())
                );

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }
    }




    public interface HandoverClicked
    {
        void handoverClicked();
    }






    @BindView(R.id.handover_button)
    TextView handoverButton;

    @OnClick(R.id.handover_button)
    void handoverClick()
    {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(makeFragmentName(mViewPager.getId(),mViewPager.getCurrentItem()));

        if(fragment instanceof HandoverClicked)
        {
            ((HandoverClicked)fragment).handoverClicked();
        }



//        FragmentManager fm = getSupportFragmentManager();
//        DeliveryGuyListDialog dialog = new DeliveryGuyListDialog();

//        dialog.show(fm, "rate");

    }


}
