package org.nearbyshops.enduserappnew.DI.DaggerModules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import org.nearbyshops.enduserappnew.API.CartItemService;
import org.nearbyshops.enduserappnew.API.CartService;
import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.API.DeliveryAddressService;
import org.nearbyshops.enduserappnew.API.DeliveryGuyLoginService;
import org.nearbyshops.enduserappnew.API.FavouriteItemService;
import org.nearbyshops.enduserappnew.API.FavouriteShopService;
import org.nearbyshops.enduserappnew.API.ItemCategoryService;
import org.nearbyshops.enduserappnew.API.ItemImageService;
import org.nearbyshops.enduserappnew.API.ItemReviewService;
import org.nearbyshops.enduserappnew.API.ItemService;
import org.nearbyshops.enduserappnew.API.ItemSpecNameService;
import org.nearbyshops.enduserappnew.API.OrderItemService;
import org.nearbyshops.enduserappnew.API.OrderService;
import org.nearbyshops.enduserappnew.API.OrderServiceDeliveryPersonSelf;
import org.nearbyshops.enduserappnew.API.OrderServiceShopStaff;
import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.API.ShopImageService;
import org.nearbyshops.enduserappnew.API.ShopItemService;
import org.nearbyshops.enduserappnew.API.ShopReviewService;
import org.nearbyshops.enduserappnew.API.ShopReviewThanksService;
import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.API.ShopStaffService;
import org.nearbyshops.enduserappnew.API.StaffService;
import org.nearbyshops.enduserappnew.API.TransactionService;
import org.nearbyshops.enduserappnew.API.UserService;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;

/**
 * Created by sumeet on 14/5/16.
 */

        /*
        retrofit = new Retrofit.Builder()
                .baseUrl(UtilityGeneral.getServiceURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        */

@Module
public class NetModule {

    String serviceURL;

    // Constructor needs one parameter to instantiate.
    public NetModule() {

    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    /*
    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    */



    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        // cache is commented out ... you can add cache by putting it back in the builder options
        //.cache(cache)

        //Cache cache

        return new OkHttpClient()
                .newBuilder()
                .build();
    }





    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

//        Log.d("applog","Retrofit: " + PrefGeneral.getServiceURL(MyApplicationCoreNew.getAppContext()));


        if(PrefGeneral.getServiceURL(MyApplication.getAppContext())!=null)
        {
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                    .client(okHttpClient)
                    .build();

        }
        else
        {
            // a dummy method place here only to prevent returning null
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("http://example.com")
                    .client(okHttpClient)
                    .build();
        }
    }







    @Provides
    ShopItemService provideShopItemService(Retrofit retrofit)
    {
        return retrofit.create(ShopItemService.class);
    }


    @Provides
    CartService provideCartService(Retrofit retrofit)
    {

//        Log.d("applog","CartService : " + PrefGeneral.getServiceURL(MyApplicationCoreNew.getAppContext()));

        return retrofit.create(CartService.class);
    }


    @Provides
    CartItemService provideCartItemService(Retrofit retrofit)
    {

//        Log.d("applog","CartItemService : " + PrefGeneral.getServiceURL(MyApplicationCoreNew.getAppContext()));

        return retrofit.create(CartItemService.class);
    }


    @Provides
    CartStatsService provideCartStatsService(Retrofit retrofit)
    {
//        Log.d("applog","CartStatsService : " + PrefGeneral.getServiceURL(MyApplicationCoreNew.getAppContext()));
        return retrofit.create(CartStatsService.class);
    }

    @Provides
    DeliveryAddressService provideDeliveryAddressService(Retrofit retrofit)
    {
        //        Log.d("applog","DeliveryAddressService : " + PrefGeneral.getServiceURL(MyApplicationCoreNew.getAppContext()));
        return retrofit.create(DeliveryAddressService.class);
    }


    @Provides
    OrderService provideOrderService(Retrofit retrofit)
    {
//        Log.d("applog","OrderServicePFS : " + PrefGeneral.getServiceURL(MyApplicationCoreNew.getAppContext()));
        return retrofit.create(OrderService.class);
    }


    @Provides
    OrderItemService orderItemService(Retrofit retrofit)
    {
        return retrofit.create(OrderItemService.class);
    }



    @Provides
    ItemCategoryService itemCategoryService(Retrofit retrofit)
    {
        ItemCategoryService service = retrofit.create(ItemCategoryService.class);

//        Log.d("applog","ItemCategoryService : " + PrefGeneral.getServiceURL(MyApplicationCoreNew.getAppContext()));

        return service;
    }





    @Provides
    ItemService itemService(Retrofit retrofit)
    {

        return retrofit.create(ItemService.class);
    }



    @Provides
    ShopService shopService(Retrofit retrofit)
    {
        return retrofit.create(ShopService.class);
    }





    @Provides
    ItemImageService itemImageService(Retrofit retrofit)
    {
        return retrofit.create(ItemImageService.class);
    }



    @Provides
    ShopReviewService shopReviewService(Retrofit retrofit)
    {

//        EndUserService endUserService = ;
        return retrofit.create(ShopReviewService.class);
    }


    @Provides
    ItemReviewService itemReviewService(Retrofit retrofit)
    {
        return retrofit.create(ItemReviewService.class);
    }



    @Provides
    FavouriteShopService favouriteShopService(Retrofit retrofit)
    {

//        EndUserService endUserService = ;
        return retrofit.create(FavouriteShopService.class);
    }


    @Provides
    FavouriteItemService favouriteItemService(Retrofit retrofit)
    {
        return retrofit.create(FavouriteItemService.class);
    }



    @Provides
    ShopReviewThanksService shopReviewThanksService(Retrofit retrofit)
    {
        return retrofit.create(ShopReviewThanksService.class);
    }


    @Provides
    UserService provideUserService(Retrofit retrofit)
    {
        return retrofit.create(UserService.class);
    }



    @Provides
    ShopImageService provideShopImage(Retrofit retrofit)
    {
        return retrofit.create(ShopImageService.class);
    }


    @Provides
    ItemSpecNameService itemSpecNameService(Retrofit retrofit)
    {
        return retrofit.create(ItemSpecNameService.class);
    }





    @Provides
    ServiceConfigurationService configurationService(Retrofit retrofit)
    {
        return retrofit.create(ServiceConfigurationService.class);
    }





    @Provides
    OrderServiceShopStaff orderServiceShopStaff(Retrofit retrofit)
    {
        return retrofit.create(OrderServiceShopStaff.class);
    }




    @Provides
    DeliveryGuyLoginService deliveryGuyLoginService(Retrofit retrofit)
    {
        return retrofit.create(DeliveryGuyLoginService.class);
    }






    @Provides
    TransactionService transactionService(Retrofit retrofit)
    {
        return retrofit.create(TransactionService.class);
    }




    @Provides
    ShopStaffService getShopStaffService(Retrofit retrofit)
    {
        return retrofit.create(ShopStaffService.class);
    }




    @Provides
    StaffService getStaffService(Retrofit retrofit)
    {
        return retrofit.create(StaffService.class);
    }




    @Provides
    OrderServiceDeliveryPersonSelf getOrderDeliveryService(Retrofit retrofit)
    {
        return retrofit.create(OrderServiceDeliveryPersonSelf.class);
    }

}
