package org.nearbyshops.enduserappnew.multimarketfiles.ViewHolderMarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import org.nearbyshops.enduserappnew.R;

public class ViewHolderHeaderMarket extends RecyclerView.ViewHolder {





    public static ViewHolderHeaderMarket create(ViewGroup parent, Context context)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_header_markets,parent,false);

        return new ViewHolderHeaderMarket(view);
    }




    public ViewHolderHeaderMarket(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }






}
