package org.nearbyshops.enduserappnew.mfiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderSwitchMarket extends RecyclerView.ViewHolder{



    @BindView(R.id.market_name) TextView marketName;

    private Context context;
    private Fragment fragment;




    public static ViewHolderSwitchMarket create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_switch_market,parent,false);
        return new ViewHolderSwitchMarket(view,context,fragment);
    }





    public ViewHolderSwitchMarket(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


        bindDashboard();
    }









    public void bindDashboard()
    {
        marketName.setText(PrefServiceConfig.getServiceName(context));
    }





    @OnClick(R.id.list_item)
    void changeLocation()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).changeMarketClick();
        }
    }







    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }







    public interface ListItemClick
    {
        void changeMarketClick();
    }



}

