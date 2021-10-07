package org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForShop;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wunderlist.slidinglayer.SlidingLayer;


import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch;
import org.nearbyshops.whitelabelapp.Interfaces.NotifySort;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyFABClick;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyIndicatorChanged;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.SortFilterSlidingLayer.SlidingLayerSortItems;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ItemsDatabase extends AppCompatActivity implements NotifyIndicatorChanged, NotifySort {

    public static final String TAG_FRAGMENT = "item_categories_simple";
    public static final String TAG_SLIDING = "sort_items_sliding";

//    @BindView(R.id.fab_menu) FloatingActionMenu fab_menu;
//    @BindView(R.id.fab_remove_selected_from_shop) FloatingActionButton fab_remove_selected;
//    @BindView(R.id.fab_add_selected_to_shop) FloatingActionButton fab_add_selected;


    @BindView(R.id.fab)
    FloatingActionButton fab;


    @BindView(R.id.text_sub) TextView itemHeader;
    @BindView(R.id.slidingLayer) SlidingLayer slidingLayer;



//    @BindView(R.id.service_name) TextView serviceName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_by_category_simple);
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
                    .add(R.id.fragment_container,new ItemsDatabaseFragment(),TAG_FRAGMENT)
                    .commit();
        }


        setupSlidingLayer();
//        setFabBackground();
    }






    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
            slidingLayer.setPreviewOffsetDistance(20);
            slidingLayer.setOffsetDistance(10);
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
                        .replace(R.id.slidinglayerfragment,new SlidingLayerSortItems(),TAG_SLIDING)
                        .commit();
            }
        }

    }




//
//    void setMarketName()
//    {
//
//        if(PrefAppSettings.getServiceName(this)!=null)
//        {
////            serviceName.setText(PrefAppSettings.getServiceName(this));
//        }
//    }




//
//    private void setFabBackground() {
//        // assign background to the FAB's
//
//        Drawable removeDrawable = VectorDrawableCompat
//                .create(getResources(),
//                        R.drawable.ic_remove_white_24px, getTheme());
//
//
//        Drawable drawableAdd = VectorDrawableCompat
//                .create(getResources(), R.drawable.ic_add_white_24px, getTheme());
//
////        ContextCompat.getDrawable(this,R.drawable.ic_remove_white_24px)
////        ContextCompat.getDrawable(this,R.drawable.ic_add_white_24px)
//        fab_remove_selected.setImageDrawable(removeDrawable);
//        fab_add_selected.setImageDrawable(drawableAdd);
//
////        fab_add_item.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_add_white_24px));
//    }
//
//
//
//
//
//    @Override
//    public void showFab() {
//        fab_menu.animate().translationY(0);
//    }
//
//    @Override
//    public void hideFab() {
//        fab_menu.animate().translationY(fab_menu.getHeight());
//    }
//
//





//    Fragment fragment = null;


    @Override
    public void onBackPressed() {

//        Fragment fragment = getSupportFragmentManager()
//                .findFragmentByTag(TAG_FRAGMENT);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
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
    public void notifyItemIndicatorChanged(String header) {
        itemHeader.setText(header);
    }



    @OnClick({R.id.icon_sort, R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @Override
    public void notifySortChanged() {

//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }
    }













    // fab click buttons


//    @OnClick(R.id.fab_remove_selected_from_shop)
//    void fabRemoveSelectedClick()
//    {
////        Fragment fragment = getSupportFragmentManager()
////                .findFragmentByTag(TAG_FRAGMENT);
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//
//        if(fragment instanceof NotifyFABClick)
//        {
//            ((NotifyFABClick) fragment).removeSelectedFromShop();
//        }
//    }
//
//
//    @OnClick(R.id.fab_add_selected_to_shop)
//    void fabAddSelectedClick()
//    {
////        Fragment fragment = getSupportFragmentManager()
////                .findFragmentByTag(TAG_FRAGMENT);
//
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//
//        if(fragment instanceof NotifyFABClick)
//        {
//            ((NotifyFABClick) fragment).addSelectedToShop();
//        }
//    }






//    @OnClick(R.id.fab_add_item)
//    void fabAddItemClick()
//    {
//        Fragment fragment = getSupportFragmentManager()
//                .findFragmentByTag(TAG_FRAGMENT);
//
//        if(fragment instanceof NotifyFABClick)
//        {
//            ((NotifyFABClick) fragment).addItem();
//        }
//    }
//






    // Add Search Feature to the activity

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_by_cat_simple, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));





//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//
//                showToast("Search Dismissed");
//
//                return false;
//            }
//        });





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

//                Toast.makeText(ItemsDatabase.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();

                return true;
            }
        });





//        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//
//                return true;
//            }
//        });



        return true;
    }





    void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
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

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }







    @OnClick(R.id.fab)
    void fabClick()
    {
//        showToast("Fab Clicked !");

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifyFABClick)
        {
            ((NotifyFABClick) fragment).addItem();
        }

    }



}
