package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderMarketHelpline extends RecyclerView.ViewHolder{





    private Context context;
    private Fragment fragment;

    MarketHelplineData data;

    @BindView(R.id.phone) TextView phoneText;
    @BindView(R.id.header) TextView headerText;

    public static int LAYOUT_TYPE_CLEAR_ALL = 1;
    public static int LAYOUT_TYPE_MENU_ITEM = 2;
    public static int LAYOUT_TYPE_ADD_NEW_LOCATION = 3;



    public static ViewHolderMarketHelpline create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = null;


        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_helpline,parent,false);

        return new ViewHolderMarketHelpline(view,context,fragment);
    }




    public ViewHolderMarketHelpline(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


        phoneText.setText(UtilityFunctions.getAppHelpline(context));
        headerText.setText(UtilityFunctions.getAppName(context) + " Helpline");
    }




    public void setItem(MarketHelplineData data)
    {
        this.data = data;
    }





    @OnClick({R.id.list_item})
    void changeLocation()
    {
        UtilityFunctions.dialPhoneNumber(UtilityFunctions.getAppHelpline(context),context);
    }

    public static class MarketHelplineData {


    }
}

