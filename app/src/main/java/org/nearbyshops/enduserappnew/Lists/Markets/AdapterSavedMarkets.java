package org.nearbyshops.enduserappnew.Lists.Markets;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.ViewHolderMarketSmall;

import java.util.List;

public class AdapterSavedMarkets extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<Market> dataset;
    private Context context;
    private Fragment subscriber;




    public AdapterSavedMarkets(List<Market> dataset, Context context, Fragment subscriber) {
        this.dataset = dataset;
        this.context = context;
        this.subscriber = subscriber;
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return ViewHolderMarketSmall.create(viewGroup,context,subscriber);
    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        if(viewHolder instanceof ViewHolderMarketSmall)
        {
            ((ViewHolderMarketSmall) viewHolder).setItem(dataset.get(i));
        }
    }






    @Override
    public int getItemCount() {
        return dataset.size();
    }




}
