package org.nearbyshops.enduserappnew.mfiles.ViewHolderMarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.mfiles.Markets.AdapterSavedMarkets;
import org.nearbyshops.enduserappnew.mfiles.Markets.ViewModelMarkets;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.mfiles.ViewHolderMarket.Model.MarketsListData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewHolderMarketsList extends RecyclerView.ViewHolder {


    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.list_title) TextView listTitle;


    private Context context;
    private Fragment fragment;


    MarketsListData marketsList;
    AdapterSavedMarkets adapter;




    public static ViewHolderMarketsList create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_horizontal_list,parent,false);

        return new ViewHolderMarketsList(view,context,fragment);
    }





    public ViewHolderMarketsList(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;

        setupViewModel();
        viewModel.getNearbyMarketsHorizontal();
    }



    public void refreshList()
    {
        viewModel.getNearbyMarketsHorizontal();
    }






    void setupRecyclerView()
    {


        if(marketsList==null)
        {
            return;
        }


        listTitle.setVisibility(View.VISIBLE);
        listTitle.setText("Markets in your area");


        adapter = new AdapterSavedMarkets(marketsList.getDataset(),fragment.getActivity(),fragment, ViewHolderMarketSmall.LAYOUT_TYPE_COVERED);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }




    public void setTextSize(float size)
    {
        listTitle.setTextSize(size);
    }





    private ViewModelMarkets viewModel;



    private void setupViewModel()
    {


        viewModel = new ViewModelMarkets(MyApplication.application);


        viewModel.getData().observe(fragment, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> objects) {


                if (objects != null) {

                    for(Object object : objects)
                    {

                        if(object instanceof MarketsListData)
                        {
                            marketsList = (MarketsListData) object;


                            System.out.println(UtilityFunctions.provideGson().toJson(marketsList));

                            setupRecyclerView();
                        }
                    }
                }


//                adapter.notifyDataSetChanged();

            }
        });





        viewModel.getMessage().observe(fragment, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                showToastMessage(s);
            }
        });

    }



    private void showToastMessage(String message)
    {
        Toast.makeText(MyApplication.getAppContext(),message,Toast.LENGTH_SHORT).show();
    }




}
