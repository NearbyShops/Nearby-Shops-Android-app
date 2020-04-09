package org.nearbyshops.enduserappnew.Login;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import okhttp3.OkHttpClient;

import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;

/**
 * Created by sumeet on 19/4/17.
 */

public class ServiceIndicatorFragment extends Fragment {

    boolean isDestroyed = false;

//    @BindView(R.id.service_url) TextView serviceURL;
//    @BindView(R.id.service_name)
//    TextView serviceName;
//    @BindView(R.id.city)
//    TextView city;
//    @BindView(R.id.address)
//    TextView addressText;

    @BindView(R.id.service_info_block) LinearLayout serviceInfoBlock;
//    @BindView(R.id.no_service_block) LinearLayout noServiceBlock;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @BindView(R.id.indicator_light) TextView indicatorLight;
    @BindView(R.id.status) TextView status;
    @BindView(R.id.reconnect) ImageView refresh;




    @BindView(R.id.market_photo) ImageView marketPhoto;
    @BindView(R.id.market_name) TextView marketName;
    @BindView(R.id.market_city) TextView marketCity;



    @Inject
    Gson gson;

    @Inject
    ServiceConfigurationService service;


    public ServiceIndicatorFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

//        View rootView = inflater.inflate(R.layout.fragment_service_indicator, container, false);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_service_indicator, container, false);
        ButterKnife.bind(this,rootView);


        bindViews();

        return rootView;
    }



    public void refresh()
    {
        bindViews();
    }





    private void bindViews()
    {



//        PrefGeneral.getServiceURL(getActivity());
////            noServiceBlock.setVisibility(View.GONE);
//        serviceInfoBlock.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.GONE);


        if(PrefServiceConfig.getServiceConfigLocal(getActivity())==null)
        {
            // fetch local service config from local service
            getLocalConfig();
        }
        else
        {


            ServiceConfigurationLocal serviceConfig = PrefServiceConfig.getServiceConfigLocal(getActivity());


//                serviceName.setText(serviceConfig.getServiceName());
//                serviceURL.setText(PrefGeneral.getServiceURL(getActivity()));

            String address = serviceConfig.getState() + ", " + serviceConfig.getCountry() + " - "
                    + serviceConfig.getPincode();

//                addressText.setText(address);
//                city.setText(serviceConfig.getCity());



            marketCity.setText(serviceConfig.getCity());
            marketName.setText(serviceConfig.getServiceName());




            String imagePath = PrefGeneral.getServiceURL(getActivity())
                    + "/api/ServiceConfiguration/Image/three_hundred_" + serviceConfig.getLogoImagePath() + ".jpg";


//                System.out.println("Service LOGO : " + imagePath);

            Drawable placeholder = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_nature_people_white_48px, getActivity().getTheme());


            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(marketPhoto);



            indicatorLight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gplus_color_1));
            status.setText("Available");

        }

    }






    @OnClick(R.id.reconnect)
    void getLocalConfig()
    {
        progressBar.setVisibility(View.VISIBLE);
        serviceInfoBlock.setVisibility(View.GONE);
//        noServiceBlock.setVisibility(View.GONE);




        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();



        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(0.0,0.0);





        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {

                if(isDestroyed)
                {
                    return;
                }



                progressBar.setVisibility(View.GONE);
                serviceInfoBlock.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    PrefServiceConfig.saveServiceConfigLocal(response.body(),getActivity());
                    bindViews();
                }
                else
                {
                    PrefServiceConfig.saveServiceConfigLocal(null,getActivity());

                    // no service
//                    serviceName.setText("Failed to get service into please try again");
//                    serviceURL.setText(PrefGeneral.getServiceURL(getActivity()));

                    indicatorLight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gplus_color_4));
                    status.setText("Not available");



                    showToastMessage("Failed Code : " + response.code());



//                    noServiceBlock.setVisibility(View.GONE);


                }
            }

            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                PrefServiceConfig.saveServiceConfigLocal(null,getActivity());

//                serviceName.setText("Failed to get service info please try again");

                indicatorLight.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gplus_color_4));
                status.setText("Not available");




                progressBar.setVisibility(View.GONE);
                serviceInfoBlock.setVisibility(View.VISIBLE);
//                noServiceBlock.setVisibility(View.GONE);

            }
        });





    }







    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();

        isDestroyed = true;
    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }
}
