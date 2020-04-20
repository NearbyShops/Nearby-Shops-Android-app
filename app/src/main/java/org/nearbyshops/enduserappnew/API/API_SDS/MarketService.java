package org.nearbyshops.enduserappnew.API.API_SDS;


import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ServiceConfigurationEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationGlobal;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;


/**
 * Created by sumeet on 12/3/16.
 */
public interface MarketService {


    @GET("/api/v1/ServiceConfiguration/UpdateService")
    Call<ResponseBody> saveService(@Query("ServiceURL") String serviceURL);



    @PUT("/api/v1/ServiceConfiguration/UpdateByStaff/{ServiceID}")
    Call<ResponseBody> updateShop(@Header("Authorization") String headers,
                                  ServiceConfigurationGlobal serviceConfigurationGlobal,
                                  @Path("ServiceID") int serviceID);


//    @Query("IsOfficial") Boolean isOfficial, @Query("IsVerified") Boolean isVerified,
//    @Query("ServiceType") Integer serviceType,
//@Query("proximity") Double proximity,





    @GET("/api/v1/ServiceConfiguration")
    Call<ServiceConfigurationEndPoint> getShopListSimple(
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("ServiceURL") String serviceURL,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") int offset
    );



    @GET("/api/v1/ServiceConfiguration")
    Call<ServiceConfigurationEndPoint> getShopListSimple(
            @Header("Authorization") String headers,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("ServiceURL") String serviceURL,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") int offset
    );







    @GET ("/api/v1/ServiceConfiguration/MarketsList")
    Call<ServiceConfigurationEndPoint> getMarketsList(
            @Header("Authorization") String headers,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("ServiceURL") String serviceURL,
            @Query("SearchString") String searchString,
            @Query("IsOfficial") Boolean isOfficial,@Query("IsVerified")Boolean isVerified,
            @Query("ServiceType") Integer serviceType,
            @Query("SortBy") String sortBy,
            @Query("Limit") int limit, @Query("Offset") int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );






    // Image Calls
    @POST("/api/v1/ServiceConfiguration/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName


    @DELETE("/api/v1/ServiceConfiguration/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);

}
