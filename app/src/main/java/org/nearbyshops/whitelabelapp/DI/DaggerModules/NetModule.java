package org.nearbyshops.whitelabelapp.DI.DaggerModules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import org.nearbyshops.whitelabelapp.API.API_Admin.MarketSettingService;
import org.nearbyshops.whitelabelapp.API.API_Admin.ShopServiceForAdmin;
import org.nearbyshops.whitelabelapp.API.BannerImageService;
import org.nearbyshops.whitelabelapp.API.CartItemService;
import org.nearbyshops.whitelabelapp.API.CartStatsService;
import org.nearbyshops.whitelabelapp.API.DeliveryAddressService;
import org.nearbyshops.whitelabelapp.API.DeliveryGuyService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopDetailService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopUtilityService;
import org.nearbyshops.whitelabelapp.API.FavouriteItemService;
import org.nearbyshops.whitelabelapp.API.FavouriteShopService;
import org.nearbyshops.whitelabelapp.API.ItemCategoryService;
import org.nearbyshops.whitelabelapp.API.ItemImageService;
import org.nearbyshops.whitelabelapp.API.ItemReviewService;
import org.nearbyshops.whitelabelapp.API.ItemService;
import org.nearbyshops.whitelabelapp.API.ItemSpecNameService;
import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderItemService;
import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderService;
import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderServiceDeliveryPersonSelf;
import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderServiceShopStaff;
import org.nearbyshops.whitelabelapp.API.RazorPayService;
import org.nearbyshops.whitelabelapp.API.ShopImageService;
import org.nearbyshops.whitelabelapp.API.ShopItemService;
import org.nearbyshops.whitelabelapp.API.ShopReviewService;
import org.nearbyshops.whitelabelapp.API.ShopReviewThanksService;
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopService;
import org.nearbyshops.whitelabelapp.API.StaffShopService;
import org.nearbyshops.whitelabelapp.API.StaffService;
import org.nearbyshops.whitelabelapp.API.TransactionService;
import org.nearbyshops.whitelabelapp.API.UserService;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.API.DeliverySlotService;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
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

//        RuntimeTypeAdapterFactory<WrapperForGSON> typeAdapterFactory = RuntimeTypeAdapterFactory
//                .of(WrapperForGSON.class)
//                .registerSubtype(Item.class)
//                .registerSubtype(Shop.class);


//        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")


        return new GsonBuilder()
//                .registerTypeAdapterFactory(typeAdapterFactory)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
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


//        .addConverterFactory(GsonConverterFactory.create(gson))

        if(PrefGeneral.getServerURL(MyApplication.getAppContext())!=null)
        {
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefGeneral.getServerURL(MyApplication.getAppContext()))
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
    ShopUtilityService provideShopUtilityService(Retrofit retrofit)
    {
        return retrofit.create(ShopUtilityService.class);
    }



    @Provides
    ShopDetailService provideShopDetailService(Retrofit retrofit)
    {
        return retrofit.create(ShopDetailService.class);
    }



    @Provides
    ShopServiceForAdmin provideShopServiceForAdmin(Retrofit retrofit)
    {
        return retrofit.create(ShopServiceForAdmin.class);
    }



    @Provides
    ShopService shopService(Retrofit retrofit)
    {
        return retrofit.create(ShopService.class);
    }





    @Provides
    MarketSettingService provideMarkets(Retrofit retrofit)
    {
        return retrofit.create(MarketSettingService.class);
    }




    @Provides
    ShopItemService provideShopItemService(Retrofit retrofit)
    {
        return retrofit.create(ShopItemService.class);
    }



    @Provides
    CartItemService provideCartItemService(Retrofit retrofit)
    {
        return retrofit.create(CartItemService.class);
    }


    @Provides
    CartStatsService provideCartStatsService(Retrofit retrofit)
    {
        return retrofit.create(CartStatsService.class);
    }

    @Provides
    DeliveryAddressService provideDeliveryAddressService(Retrofit retrofit)
    {
        return retrofit.create(DeliveryAddressService.class);
    }


    @Provides
    OrderService provideOrderService(Retrofit retrofit)
    {
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
        return retrofit.create(ItemCategoryService.class);
    }





    @Provides
    ItemService itemService(Retrofit retrofit)
    {

        return retrofit.create(ItemService.class);
    }




    @Provides
    ItemImageService itemImageService(Retrofit retrofit)
    {
        return retrofit.create(ItemImageService.class);
    }



    @Provides
    BannerImageService provideBannerImage(Retrofit retrofit)
    {
        return retrofit.create(BannerImageService.class);
    }



    @Provides
    ShopImageService provideShopImage(Retrofit retrofit)
    {
        return retrofit.create(ShopImageService.class);
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
    ItemSpecNameService itemSpecNameService(Retrofit retrofit)
    {
        return retrofit.create(ItemSpecNameService.class);
    }





    @Provides
    OrderServiceShopStaff orderServiceShopStaff(Retrofit retrofit)
    {
        return retrofit.create(OrderServiceShopStaff.class);
    }




    @Provides
    DeliveryGuyService deliveryGuyLoginService(Retrofit retrofit)
    {
        return retrofit.create(DeliveryGuyService.class);
    }






    @Provides
    TransactionService transactionService(Retrofit retrofit)
    {
        return retrofit.create(TransactionService.class);
    }




    @Provides
    StaffShopService getShopStaffService(Retrofit retrofit)
    {
        return retrofit.create(StaffShopService.class);
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




    @Provides
    DeliverySlotService provideDeliverySlot(Retrofit retrofit)
    {
        return retrofit.create(DeliverySlotService.class);
    }



    @Provides
    RazorPayService provideRazorPay(Retrofit retrofit)
    {
        return retrofit.create(RazorPayService.class);
    }


}
