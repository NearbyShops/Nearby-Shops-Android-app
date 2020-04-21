package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewHolderCreateShop extends RecyclerView.ViewHolder{



    @BindView(R.id.header) TextView header;


    private Context context;
    private HeaderTitle item;



    public static ViewHolderCreateShop create(ViewGroup parent, Context context)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_create_shop, parent, false);

        return new ViewHolderCreateShop(view,context);
    }




    public ViewHolderCreateShop(View itemView, Context context) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
    }


    public void setItem(HeaderTitle data)
    {
        header.setText(data.getHeading());
    }


}

