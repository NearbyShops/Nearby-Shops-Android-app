package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderDeprecated;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Lists.Markets.AdapterSavedMarkets;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.Model.MarketsList;
import org.nearbyshops.enduserappnew.R;

import java.util.List;

public class ViewHolderSavedMarketList extends RecyclerView.ViewHolder {


    @BindView(R.id.recycler_view) RecyclerView savedMarketList;



//    private List<ServiceConfigurationGlobal> configurationGlobal;
    private Context context;
    private Fragment subscriber;


    @BindView(R.id.list_title) TextView listTitle;





    public static ViewHolderSavedMarketList create(ViewGroup parent, Context context, Fragment subscriber)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market_saved_list,parent,false);

        return new ViewHolderSavedMarketList(view,context,subscriber);
    }




    public ViewHolderSavedMarketList(@NonNull View itemView, Context context, Fragment subscriber) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.subscriber = subscriber;
    }







    public void setItem(List<Market> item)
    {

//        this.configurationGlobal = item;


        AdapterSavedMarkets adapter=  new AdapterSavedMarkets(item,context, subscriber);
        savedMarketList.setAdapter(adapter);
        savedMarketList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        savedMarketList.setLayoutManager(layoutManager);

    }







    public void setItem(MarketsList list)
    {

//        this.configurationGlobal = item;


        listTitle.setText(list.getListTitle());

        AdapterSavedMarkets adapter=  new AdapterSavedMarkets(list.getDataset(),context, subscriber);
        savedMarketList.setAdapter(adapter);
        savedMarketList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        savedMarketList.setLayoutManager(layoutManager);

    }






}
