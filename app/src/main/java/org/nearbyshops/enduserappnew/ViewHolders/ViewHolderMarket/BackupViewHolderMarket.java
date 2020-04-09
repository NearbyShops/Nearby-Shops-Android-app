package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.API.LoginUsingOTPService;
import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BackupViewHolderMarket extends RecyclerView.ViewHolder implements View.OnClickListener {


    @BindView(R.id.service_name) TextView serviceName;
    @BindView(R.id.address) TextView serviceAddress;
//    @BindView(R.id.indicator_category) TextView indicatorCategory;
//    @BindView(R.id.indicator_verified) TextView indicatorVerified;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView ratingCount;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.logo) ImageView serviceLogo;

    @BindView(R.id.progress_bar_select) ProgressBar progressBarSelect;
    @BindView(R.id.select_market) TextView selectMarket;


    private ServiceConfigurationGlobal configurationGlobal;
    private Fragment subscriber;
    private Context context;


    @Inject Gson gson;





    public static BackupViewHolderMarket create(ViewGroup parent, Context context, Fragment subscriber)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market, parent, false);

        return new BackupViewHolderMarket(view,parent,context,subscriber);
    }







    public BackupViewHolderMarket(View itemView, ViewGroup parent, Context context, Fragment subscriber)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);



        this.context = context;
        this.subscriber = subscriber;

//        itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_market,parent,false);


        itemView.setOnClickListener(this);



        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }







    public void setItem(ServiceConfigurationGlobal configurationGlobal)
    {
        this.configurationGlobal = configurationGlobal;


        serviceName.setText(configurationGlobal.getServiceName());
        serviceAddress.setText(configurationGlobal.getCity());

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





        distance.setText("Distance : " + String.format("%.2f",configurationGlobal.getRt_distance()));
//                holder.rating.setText(String.format("%.2f",));

        description.setText(configurationGlobal.getDescriptionShort());


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




    @OnClick(R.id.select_market)
    void selectMarket()
    {


        ServiceConfigurationGlobal configurationGlobal = this.configurationGlobal;



        if(PrefLoginGlobal.getUser(context)==null)
        {
            // user not logged in so just fetch configuration
            fetchConfiguration(configurationGlobal);
        }
        else
        {
            // user logged in so make an attempt to login to local service
            loginToLocalEndpoint(configurationGlobal);
        }



    }



    @Override
    public void onClick(View v) {

        if(subscriber instanceof ListItemClick)
        {
            ((ListItemClick) subscriber).listItemClick(configurationGlobal,getLayoutPosition());
        }
    }





    private void fetchConfiguration(final ServiceConfigurationGlobal configurationGlobal)
    {

//            PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),getApplicationContext());
//            PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());



//            PrefGeneral.getServiceURL(MyApplicationCoreNew.getAppContext())


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(configurationGlobal.getServiceURL())
                .client(new OkHttpClient().newBuilder().build())
                .build();





        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(0.0,0.0);



        selectMarket.setVisibility(View.INVISIBLE);
        progressBarSelect.setVisibility(View.VISIBLE);


        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {


                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {

                    PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),context);
                    PrefServiceConfig.saveServiceConfigLocal(response.body(),context);


                    ServiceConfigurationLocal config = response.body();

                    if(config!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",config.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),context);
                    }



                    if(subscriber instanceof ListItemClick)
                    {

                        ((ListItemClick) subscriber).selectMarketSuccessful(configurationGlobal,getLayoutPosition());
                    }

                }
                else
                {
//                        PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
//                        PrefGeneral.saveServiceURL(null,getApplicationContext());



                    if(subscriber instanceof  ListItemClick)
                    {
                        ((ListItemClick) subscriber).showMessage("Failed Code : " + response.code());
                    }
                }


            }



            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {


                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);


                if(subscriber instanceof ListItemClick)
                {
                    ((ListItemClick) subscriber).showMessage("Failed ... Please check your network ! ");
                }
            }
        });
    }



    private void loginToLocalEndpoint(final ServiceConfigurationGlobal configurationGlobal)
    {

//        final String phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();




        selectMarket.setVisibility(View.INVISIBLE);
        progressBarSelect.setVisibility(View.VISIBLE);



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(configurationGlobal.getServiceURL())
                .client(new OkHttpClient().newBuilder().build())
                .build();





        Call<User> call = retrofit.create(LoginUsingOTPService.class).loginWithGlobalCredentials(
                PrefLoginGlobal.getAuthorizationHeaders(context),
                PrefServiceConfig.getServiceURL_SDS(context),
                123,

                true,false
        );






        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {




                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    // save username and password




                    PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),context);



                    User user = response.body();


//                    PrefLogin.saveCredentials(
//                            context,
//                            user.getPhone(),
//                            user.getPassword()
//                    );

                    String username = "";

                    if(user.getPhone()!=null)
                    {
                        username = user.getPhone();
                    }
                    else if(user.getEmail()!=null)
                    {
                        username = user.getEmail();
                    }
                    else if(user.getUsername()!=null)
                    {
                        username = user.getUsername();
                    }
                    else if(user.getUserID()!=0)
                    {
                        username = String.valueOf(user.getUserID());
                    }

                    // local username can be different from the supplied username

                    PrefLogin.saveCredentialsPassword(
                            context,
                            username,
                            user.getPassword()
                    );



//                    PrefLogin.saveCredentials(
//                            context,
//                            PrefLoginGlobal.getUsername(context),
//                            PrefLoginGlobal.getPassword(context)
//                    );


                    PrefLogin.saveUserProfile(
                            user,
                            context
                    );



                    ServiceConfigurationLocal configurationLocal = user.getServiceConfigurationLocal();

                    PrefServiceConfig.saveServiceConfigLocal(configurationLocal,context);



                    if(configurationLocal!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",configurationLocal.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),context);
                    }



                    UtilityFunctions.updateFirebaseSubscriptions();

                    if(subscriber instanceof ListItemClick)
                    {
                        ((ListItemClick) subscriber).selectMarketSuccessful(configurationGlobal,getLayoutPosition());
                    }



                }
                else
                {

                    if(subscriber instanceof ListItemClick)
                    {
                        ((ListItemClick) subscriber).showMessage("Login Failed : Username or password is incorrect !");
                    }


                    System.out.println("Login Failed : Code " + response.code());
                }

            }




            @Override
            public void onFailure(Call<User> call, Throwable t) {


                if(subscriber instanceof ListItemClick)
                {
                    ((ListItemClick) subscriber).showMessage("Failed ... Please check your network connection !");
                }

                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);


            }
        });
    }




    public interface ListItemClick
    {
        void listItemClick(ServiceConfigurationGlobal configurationGlobal, int position);
        void selectMarketSuccessful(ServiceConfigurationGlobal configurationGlobal, int position);
        void showMessage(String message);
    }

}



