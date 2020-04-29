package org.nearbyshops.enduserappnew.API;


import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ServiceConfigurationService {


    @GET("/api/ServiceConfiguration")
    Call<Market> getServiceConfiguration(@Query("latCenter") Double latCenter,
                                         @Query("lonCenter") Double lonCenter);




    @PUT("/api/ServiceConfiguration")
    Call<ResponseBody> putServiceConfiguration(@Header("Authorization") String headers,
                                               @Body Market serviceConfiguration);



    // Image Calls

    @POST("/api/ServiceConfiguration/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    @DELETE("/api/ServiceConfiguration/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);
}
