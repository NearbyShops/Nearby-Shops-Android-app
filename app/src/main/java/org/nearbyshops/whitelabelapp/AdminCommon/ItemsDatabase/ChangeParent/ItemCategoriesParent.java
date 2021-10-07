package org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ChangeParent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;


import org.nearbyshops.whitelabelapp.API.ItemCategoryService;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ItemCategoryEndPoint;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemCategoriesParent extends AppCompatActivity
        implements  SwipeRefreshLayout.OnRefreshListener, Adapter.requestSubCategory, Adapter.NotificationReceiver {


    // data
    public static Map<Integer, ItemCategory> excludeList = new HashMap<>();
    ArrayList<ItemCategory> dataset = new ArrayList<>();

    boolean menuVisible;
    boolean instructionsVisible = false;

    @Inject
    ItemCategoryService itemCategoryService;


    ItemCategory currentCategory = null;


//    int currentCategoryID = 1; // the ID of root category is always supposed to be 1



//    private boolean isRootCategory = true;
//    private ArrayList<String> categoryTree = new ArrayList<>();




    // views
    RecyclerView itemCategoriesList;
    Adapter listAdapter;
    GridLayoutManager layoutManager;

    @BindView(R.id.show_hide_instructions)
    TextView showHideInstructions;

    @BindView(R.id.usage_instructions)
    TextView usageInstructions;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.assign_parent)
    TextView assignParent;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;


    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;



    public ItemCategoriesParent() {
        super();

        // Inject the dependencies using Dependency Injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setParentCategoryID(-1);
        currentCategory.setCategoryName("ROOT");
        currentCategory.setCategoryDescription("The root category.");
        menuVisible = true;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_categories_parent);

        ButterKnife.bind(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemCategoriesList = findViewById(R.id.recyclerViewItemCategories);
        setupRecyclerView();
        setupSwipeContainer();


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


    }




    private void setupSwipeContainer()
    {
        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }


    @OnClick(R.id.show_hide_instructions)
    void clickShowHideInstructions()
    {
        if(instructionsVisible)
        {
            usageInstructions.setVisibility(View.GONE);

            instructionsVisible = false;

        }else
        {
            usageInstructions.setVisibility(View.VISIBLE);

            instructionsVisible = true;
        }

    }


    void setupRecyclerView()
    {
        listAdapter = new Adapter(dataset,this,this,this);

        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(this,1);

        itemCategoriesList.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutManager.setSpanCount(2);
    }






    private void makeRefreshNetworkCall() {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {


                    onRefresh();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }
            }
        });

    }




    @Override
    public void onRefresh() {

        makeRequestRetrofit();
    }





    public void makeRequestRetrofit()
    {


        Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesQuerySimple(
                currentCategory.getItemCategoryID(),null,ItemCategory.CATEGORY_ORDER,
                null,null
        );



        endPointCall.enqueue(new Callback<ItemCategoryEndPoint>() {
            @Override
            public void onResponse(Call<ItemCategoryEndPoint> call, Response<ItemCategoryEndPoint> response) {

                if(isDestroyed())
                {
                    return;
                }

                swipeContainer.setRefreshing(false);

                if(response.code()==200 && response.body()!=null)
                {
                    dataset.clear();

                    if(currentCategory.getItemCategoryID()==1)
                    {
                        dataset.add(0,currentCategory);
                    }



//                    item_count = response.body().getItemCount();

                    // the entities in the exclude list should not be added into the dataset
                    for(ItemCategory itemCategory : response.body().getResults())
                    {
                        // if item does not exist in the exclude list then only add it.
                        if(!excludeList.containsKey(itemCategory.getItemCategoryID()))
                        {
                            dataset.add(itemCategory);
                        }
                    }



                    listAdapter.notifyDataSetChanged();


                }
                else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }


            }

            @Override
            public void onFailure(Call<ItemCategoryEndPoint> call, Throwable t) {

                if(isDestroyed())
                {
                    return;
                }

                swipeContainer.setRefreshing(false);

                showToastMessage("Network request failed. Please check your connection !");
            }
        });

    }




    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(this,message);
    }







    private void insertTab(String categoryName)
    {
        if(tabLayout.getVisibility()==View.GONE)
        {
            tabLayout.setVisibility(View.VISIBLE);
        }

        tabLayout.addTab(tabLayout.newTab().setText(" : : " + categoryName + " : : "));
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);

    }

    private void removeLastTab()
    {
        if(tabLayout.getTabCount()>0)
        {
            tabLayout.removeTabAt(tabLayout.getTabCount()-1);
            tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);
        }


        if(tabLayout.getTabCount()==0)
        {
            tabLayout.setVisibility(View.GONE);
        }
    }





    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {


        if(itemCategory.getItemCategoryID()==1)
        {
            return;
        }

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

//        categoryTree.add(currentCategory.getCategoryName());

        insertTab(currentCategory.getCategoryName());

//        currentCategoryID = itemCategory.getItemCategoryID();

//        if(isRootCategory) {
//
//            isRootCategory = false;
//
//        }else
//        {
//            boolean isFirst = true;
//        }

//        offset = 0; // reset the offset
//        dataset.clear();
        makeRefreshNetworkCall();


        appBarLayout.setVisibility(View.VISIBLE);
        assignParent.setVisibility(View.VISIBLE);

    }


    @Override
    public void onBackPressed() {


        Integer currentCategoryID = null;

        if(currentCategory!=null)
        {

//            if(categoryTree.size()>0) {
//
//                categoryTree.remove(categoryTree.size() - 1);
//
//            }

            removeLastTab();

            if(currentCategory.getParentCategory()!= null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();
            }
            else
            {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if(currentCategoryID!=-1)
            {


                makeRefreshNetworkCall();



                appBarLayout.setVisibility(View.VISIBLE);
                assignParent.setVisibility(View.VISIBLE);

                listAdapter.clearSelection();
            }
        }

        if(currentCategoryID == -1)
        {
            super.onBackPressed();
        }

    }



    @Override
    public void notifyItemSelected() {

        assignParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyItemDeleted() {

        makeRefreshNetworkCall();
    }




    @OnClick(R.id.assign_parent)
    void assignParentClick()
    {
        if(listAdapter.getSelection()==null)
        {
            showToastMessage("No item selected. Please make a selection !");
            return;
        }



        Intent returnIntent = new Intent();
        String jsonString = UtilityFunctions.provideGson().toJson(listAdapter.getSelection());
        returnIntent.putExtra("result", jsonString);
        setResult(Activity.RESULT_OK,returnIntent);

        // reset the static variable to null so that the data could be garbage collected.
        clearExcludeList();

        finish();

    }




    static public void clearExcludeList()
    {
        ItemCategoriesParent.excludeList.clear();
    }




}
