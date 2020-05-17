package org.nearbyshops.enduserappnew.aSuperAdminModule.MarketsList.ViewHolder;

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
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderMarketAdmin extends RecyclerView.ViewHolder{


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

    private Market configurationGlobal;
    private Fragment fragment;
    private Context context;




    public static ViewHolderMarketAdmin create(ViewGroup parent, Context context, Fragment subscriber)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market_new, parent, false);

        return new ViewHolderMarketAdmin(view,parent,context,subscriber);
    }







    public ViewHolderMarketAdmin(View itemView, ViewGroup parent, Context context, Fragment fragment)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);


        this.context = context;
        this.fragment = fragment;
    }








    public void setItem(Market configurationGlobal)
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







    @OnClick(R.id.list_item_market)
    void selectMarket()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemClick(configurationGlobal,getAdapterPosition());
        }
    }




    public interface ListItemClick
    {
        void listItemClick(Market configurationGlobal, int position);
    }





    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}



