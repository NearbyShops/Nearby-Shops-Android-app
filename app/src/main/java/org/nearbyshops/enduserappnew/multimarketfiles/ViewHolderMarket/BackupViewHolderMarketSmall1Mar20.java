package org.nearbyshops.enduserappnew.multimarketfiles.ViewHolderMarket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.multimarketfiles.Markets.ViewModelMarkets;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BackupViewHolderMarketSmall1Mar20 extends RecyclerView.ViewHolder {


    @BindView(R.id.market_photo) ImageView marketPhoto;
    @BindView(R.id.market_name) TextView marketName;
    @BindView(R.id.market_city) TextView marketCity;

    @BindView(R.id.progress_bar_select) ProgressBar progressBarSelect;
    @BindView(R.id.select_market) TextView selectMarket;



    private Market configurationGlobal;
    private Context context;
    private Fragment fragment;



    private ViewModelMarkets viewModel;


    @Inject Gson gson;




    public static BackupViewHolderMarketSmall1Mar20 create(ViewGroup parent, Context context, Fragment subscriber)
    {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market_type_small,parent,false);

        return new BackupViewHolderMarketSmall1Mar20(view,context,subscriber);
    }




    public BackupViewHolderMarketSmall1Mar20(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
        this.fragment = fragment;


//        viewModel  = ViewModelProviders.of(fragment).get(MarketViewModel.class);
        viewModel  = new ViewModelMarkets(MyApplication.application);


        viewModel.getEvent().observe(fragment, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {


                if(integer== ViewModelMarkets.EVENT_LOCAL_CONFIG_FETCHED || integer== ViewModelMarkets.EVENT_LOGGED_IN_TO_LOCAL_SUCCESS)
                {

                    selectMarket.setVisibility(View.VISIBLE);
                    progressBarSelect.setVisibility(View.INVISIBLE);


                    if(fragment instanceof ViewHolderMarket.ListItemClick)
                    {
                        ((ViewHolderMarket.ListItemClick) fragment).selectMarketSuccessful(configurationGlobal,getAdapterPosition());
                    }

                }
                else if(integer== ViewModelMarkets.EVENT_NETWORK_FAILED)
                {
                    selectMarket.setVisibility(View.VISIBLE);
                    progressBarSelect.setVisibility(View.INVISIBLE);
                }

            }
        });



        viewModel.getMessage().observe(fragment, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if(fragment instanceof ViewHolderMarket.ListItemClick)
                {
                    ((ViewHolderMarket.ListItemClick) fragment).showMessage(s);
                }

            }
        });



        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }




    public void setItem(Market item)
    {

        this.configurationGlobal = item;


        marketName.setText(configurationGlobal.getServiceName());
        marketCity.setText(configurationGlobal.getCity());


        String imagePath = PrefServiceConfig.getServiceURL_SDS(context)
                + "/api/v1/ServiceConfiguration/Image/three_hundred_" + configurationGlobal.getLogoImagePath() + ".jpg";


//                System.out.println("Service LOGO : " + imagePath);

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(marketPhoto);

    }





    @OnClick(R.id.list_item)
    void listItemClick()
    {


        if(fragment instanceof ViewHolderMarket.ListItemClick)
        {
            ((ViewHolderMarket.ListItemClick) fragment).listItemClick(configurationGlobal,getLayoutPosition());
        }
    }







    @OnClick(R.id.select_market)
    void selectMarket()
    {
        Market configurationGlobal = this.configurationGlobal;


        selectMarket.setVisibility(View.INVISIBLE);
        progressBarSelect.setVisibility(View.VISIBLE);


        if(PrefLoginGlobal.getUser(context)==null)
        {
            // user not logged in so just fetch configuration
            viewModel.fetchLocalConfiguration(configurationGlobal);
        }
        else
        {
            // user logged in so make an attempt to login to local service
            viewModel.loginToLocalEndpoint(configurationGlobal);
        }
    }




    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}
