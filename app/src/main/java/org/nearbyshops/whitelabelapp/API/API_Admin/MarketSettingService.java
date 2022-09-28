package org.nearbyshops.whitelabelapp.API.API_Admin;


import org.nearbyshops.whitelabelapp.Model.Image;
import org.nearbyshops.whitelabelapp.Model.ModelMarket.Market;
import org.nearbyshops.whitelabelapp.Model.ModelMarket.MarketSettings;
import org.nearbyshops.whitelabelapp.Model.ModelUtility.PushNotificationData;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by sumeet on 13/8/17.
 */

public interface MarketSettingService {




    @POST ("/api/Market/SendPushNotification")
    Call<ResponseBody> sendPushNotification(
            @Header("Authorization") String headers,
            @Body PushNotificationData pushNotificationData
    );




    @PUT ("/api/Market/SetMarketOpen/{IsOpen}")
    Call<ResponseBody> setMarketOpen(
            @Header("Authorization") String headers,
            @Path("IsOpen")boolean isOpen
    );





    @GET ("/api/Market/GetMarketForDashboard")
    Call<Market> getMarketForDashboard(
            @Header("Authorization") String headers
    );




    @GET ("/api/Market/GetMarketForEditScreen")
    Call<Market> getMarketDetails(
            @Header("Authorization") String headers
    );



    @PUT ("/api/Market/UpdateMarket")
    Call<ResponseBody> updateMarket(
            @Header("Authorization") String headers,
            @Body Market market
    );









    @GET("/api/MarketSettings")
    Call<MarketSettings> getSettings(
            @Header("Authorization") String headers
    );




    @PUT("/api/MarketSettings")
    Call<ResponseBody> updateSettings(
            @Header("Authorization") String headers,
            @Body MarketSettings settings
    );







    // Image Calls

//    @POST("/api/ServiceConfiguration/Image")
//    Call<Image> uploadImage(@Header("Authorization") String headers,
//                            @Body RequestBody image);

    @Multipart
    @POST("/api/Market/Image")
    Call<Image> uploadImage(
            @Header("Authorization") String headers,
            @Part MultipartBody.Part img
    );



    @DELETE("/api/Market/Image/{name}")
    Call<ResponseBody> deleteImage(
            @Header("Authorization") String headers,
            @Path("name") String fileName
    );


}
