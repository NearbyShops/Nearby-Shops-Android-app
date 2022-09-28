package org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForAdmin;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyFABClickAdmin;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyHeaderChanged;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Interfaces.ToggleFab;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.SlidingLayerSortItemsAdmin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ItemsDatabaseAdmin extends AppCompatActivity implements NotifyHeaderChanged, NotifySort, ToggleFab {

    public static final String TAG_FRAGMENT = "items_database_admin";
    public static final String TAG_SLIDING = "sort_items_sliding";





    // Fab Variables
    @BindView(R.id.fab_menu) FloatingActionMenu fab_menu;
//    @BindView(R.id.fab_detach) FloatingActionButton fab_detach;
//    @BindView(R.id.fab_change_parent) FloatingActionButton fab_change_parent;
    @BindView(R.id.fab_add_item) FloatingActionButton fab_add_item;
    @BindView(R.id.fab_add) FloatingActionButton fab_add;
//    @BindView(R.id.fab_add_from_global_item) FloatingActionButton fab_add_from_global_item;
//    @BindView(R.id.fab_add_from_global) FloatingActionButton fab_add_from_global;
    @BindView(R.id.text_sub) TextView itemHeader;
    @BindView(R.id.slidingLayer)
    SlidingLayer slidingLayer;




    @BindView(R.id.service_name) TextView serviceName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_categories_simple);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

     */

//     getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new ItemsDatabaseForAdminFragment(),TAG_FRAGMENT)
                    .commit();
        }




        serviceName.setVisibility(View.GONE);




        setFabBackground();
        setupSlidingLayer();
    }


    private void setFabBackground() {
        // assign background to the FAB's
        fab_add.setImageResource(R.drawable.fab_add);
        fab_add_item.setImageResource(R.drawable.fab_add);


//        Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_low_priority_black_24px, getTheme());
//        fab_change_parent.setImageDrawable(drawable);

//        Drawable drawable_add = VectorDrawableCompat.create(getResources(), R.drawable.ic_playlist_add_from_global, getTheme());
//        fab_add_from_global.setImageDrawable(drawable_add);

//        Drawable drawable_detach = VectorDrawableCompat.create(getResources(), R.drawable.ic_detach, getTheme());
//        fab_detach.setImageDrawable(drawable_detach);

//        fab_add_item.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_add_black_24dp));
//        fab_add_from_global.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_playlist_add_from_global));
    }



    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
//            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(30);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            if(getSupportFragmentManager().findFragmentByTag(TAG_SLIDING)==null)
            {
                System.out.println("Item Cat Simple : New Sliding Layer Loaded !");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.slidinglayerfragment,new SlidingLayerSortItemsAdmin(),TAG_SLIDING)
                        .commit();
            }
        }

    }





    @Override
    public void showFab() {
//        fab_menu.animate().translationY(0);
    }

    @Override
    public void hideFab() {
//        fab_menu.animate().translationY(fab_menu.getHeight());
    }





//    Fragment fragment = null;


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
    public void notifyItemHeaderChanged(String header) {
        itemHeader.setText(header);
    }



    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @Override
    public void notifySortChanged() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }
    }




//    @OnClick(R.id.fab_detach)
//    void fabDetachClick()
//    {
//        Fragment fragment = getSupportFragmentManager()
//                .findFragmentByTag(TAG_FRAGMENT);
//
//        if(fragment instanceof NotifyFABClick)
//        {
//            ((NotifyFABClick) fragment).detachSelectedClick();
//        }
//    }


//    @OnClick(R.id.fab_change_parent)
    void changeParentClick()
    {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifyFABClickAdmin)
        {
            ((NotifyFABClickAdmin) fragment).changeParentForSelected();
        }
    }


    @OnClick(R.id.fab_add_item)
    void fabAddItemClick()
    {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifyFABClickAdmin)
        {
            ((NotifyFABClickAdmin) fragment).addItem();
        }
    }



    @OnClick(R.id.fab_add)
    void fabAddItemCatClick()
    {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifyFABClickAdmin)
        {
            ((NotifyFABClickAdmin) fragment).addItemCategory();
        }
    }









    

    // Add Search Feature to the activity

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_by_cat_simple, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }


        MenuItem item = menu.findItem(R.id.action_search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }






    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }


}
