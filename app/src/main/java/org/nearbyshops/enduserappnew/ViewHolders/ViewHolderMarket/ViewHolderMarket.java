package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.ViewModels.MarketViewModel;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

import javax.inject.Inject;



public class ViewHolderMarket extends RecyclerView.ViewHolder{


    @BindView(R.id.service_name) TextView serviceName;
    @BindView(R.id.address) TextView serviceAddress;
//    @BindView(R.id.indicator_category) TextView indicatorCategory;
//    @BindView(R.id.indicator_verified) TextView indicatorVerified;
//    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView ratingCount;
    @BindView(R.id.short_description) TextView description;
    @BindView(R.id.logo) ImageView serviceLogo;

    @BindView(R.id.distance) TextView distance;

    @BindView(R.id.progress_bar_select) ProgressBar progressBarSelect;
//    @BindView(R.id.select_market) TextView selectMarket;


    private ServiceConfigurationGlobal configurationGlobal;
    private Fragment fragment;
    private Context context;

    private MarketViewModel viewModel;


    @Inject Gson gson;





    public static ViewHolderMarket create(ViewGroup parent, Context context, Fragment subscriber)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market_new, parent, false);

        return new ViewHolderMarket(view,parent,context,subscriber);
    }







    public ViewHolderMarket(View itemView, ViewGroup parent, Context context, Fragment fragment)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);



        this.context = context;
        this.fragment = fragment;

//        itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_market,parent,false);


//        viewModel  = ViewModelProviders.of(fragment).get(MarketViewModel.class);
        viewModel = new MarketViewModel(MyApplication.application);

        viewModel.getEvent().observe(fragment, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {


//                showToastMessage("Event : " + integer);



                if(integer==MarketViewModel.EVENT_LOCAL_CONFIG_FETCHED || integer==MarketViewModel.EVENT_LOGGED_IN_TO_LOCAL_SUCCESS)
                {

//                    selectMarket.setVisibility(View.VISIBLE);
                    serviceLogo.setVisibility(View.VISIBLE);
                    progressBarSelect.setVisibility(View.INVISIBLE);


                    if(fragment instanceof ListItemClick)
                    {
                        ((ListItemClick) fragment).selectMarketSuccessful(configurationGlobal,getAdapterPosition());
                    }

                }
                else if(integer==MarketViewModel.EVENT_NETWORK_FAILED)
                {
//                    selectMarket.setVisibility(View.VISIBLE);
                    serviceLogo.setVisibility(View.VISIBLE);
                    progressBarSelect.setVisibility(View.INVISIBLE);
                }

            }
        });





        viewModel.getMessage().observe(fragment, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick) fragment).showMessage(s);
                }

            }
        });




//        itemView.setOnClickListener(this);



        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }








    public void setItem(ServiceConfigurationGlobal configurationGlobal)
    {
        this.configurationGlobal = configurationGlobal;

        serviceName.setText(configurationGlobal.getServiceName());
        serviceAddress.setText(configurationGlobal.getCity());


        distance.setText(String.format("%.2f Km", configurationGlobal.getRt_distance()));

        description.setText(configurationGlobal.getDescriptionShort());

//                service.getAddress() + ", " +



//                if(service.getVerified())
//                {
//                    holder.indicatorVerified.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    holder.indicatorVerified.setVisibility(View.GONE);
//                }



//                holder.indicatorVerified.setVisibility(View.VISIBLE);





//        distance.setText("Distance : " + String.format("%.2f",configurationGlobal.getRt_distance()));
//        description.setText(configurationGlobal.getDescriptionShort());


        if(configurationGlobal.getRt_rating_count()==0)
        {
            rating.setText(" New ");
            rating.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
            ratingCount.setVisibility(View.GONE);
        }
        else
        {
            rating.setText(String.format("%.2f",configurationGlobal.getRt_rating_avg()));
            ratingCount.setText("( " + (int) configurationGlobal.getRt_rating_count() + " Ratings )");

            rating.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));
            ratingCount.setVisibility(View.VISIBLE);

        }





        String imagePath = PrefServiceConfig.getServiceURL_SDS(context)
                + "/api/v1/ServiceConfiguration/Image/three_hundred_" + configurationGlobal.getLogoImagePath() + ".jpg";

//                System.out.println("Service LOGO : " + imagePath);

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(serviceLogo);

    }






//        @OnClick(R.id.description)
//        void copyURLClick()
//        {
//            ClipboardManager clipboard = (ClipboardManager) fragment.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("URL", serviceURL.getText().toString());
//            clipboard.setPrimaryClip(clip);
//
//            showToastMessage("Copied !");
//        }




    @OnClick(R.id.list_item_market)
    void selectMarket()
    {

        ServiceConfigurationGlobal configurationGlobal = this.configurationGlobal;

//        selectMarket.setVisibility(View.INVISIBLE);
        serviceLogo.setVisibility(View.INVISIBLE);
        progressBarSelect.setVisibility(View.VISIBLE);

        if(PrefLoginGlobal.getUser(context)==null)
        {
            // user not logged in so just fetch configuration
            viewModel.fetchLocalConfiguration(configurationGlobal);
        }
        else
        {
            viewModel.loginToLocalEndpoint(configurationGlobal);
        }

    }





    @OnLongClick(R.id.list_item_market)
    void showDetailsClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemClick(configurationGlobal,getLayoutPosition());
        }
    }





    public interface ListItemClick
    {
        void listItemClick(ServiceConfigurationGlobal configurationGlobal, int position);
        void selectMarketSuccessful(ServiceConfigurationGlobal configurationGlobal, int position);
        void showMessage(String message);
    }





    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}



