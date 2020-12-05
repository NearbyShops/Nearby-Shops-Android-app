package org.nearbyshops.enduserappnew.API;


import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Model.ModelMarket.MarketSettings;
import org.nearbyshops.enduserappnew.Model.ModelUtility.PushNotificationData;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ServiceConfigurationService {




    @GET("/api/MarketSettings")
    Call<MarketSettings> getSettings();




    @PUT("/api/MarketSettings")
    Call<ResponseBody> updateSettings(
            @Header("Authorization") String headers,
            @Body MarketSettings settings
    );




    @POST ("/api/ServiceConfiguration/SendPushNotification")
    Call<ResponseBody> sendPushNotification(
            @Header("Authorization") String headers,
            @Body PushNotificationData pushNotificationData
    );








    @GET("/api/ServiceConfiguration")
    Call<Market> getServiceConfiguration(@Query("latCenter") Double latCenter,
                                         @Query("lonCenter") Double lonCenter);






    @PUT("/api/ServiceConfiguration")
    Call<ResponseBody> putServiceConfiguration(
            @Header("Authorization") String headers,
            @Body Market serviceConfiguration
    );






    // Image Calls

//    @POST("/api/ServiceConfiguration/Image")
//    Call<Image> uploadImage(@Header("Authorization") String headers,
//                            @Body RequestBody image);



    @Multipart
    @POST("/api/ServiceConfiguration/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Part MultipartBody.Part img);




    @DELETE("/api/ServiceConfiguration/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);
}
