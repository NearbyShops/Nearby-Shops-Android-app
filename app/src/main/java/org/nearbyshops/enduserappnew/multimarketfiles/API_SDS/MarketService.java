package org.nearbyshops.enduserappnew.multimarketfiles.API_SDS;


import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ServiceConfigurationEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;


/**
 * Created by sumeet on 12/3/16.
 */
public interface MarketService {


    @GET("/api/v1/Markets/UpdateService")
    Call<ResponseBody> saveService(@Query("ServiceURL") String serviceURL);



    @PUT("/api/v1/Markets/UpdateByStaff/{ServiceID}")
    Call<ResponseBody> updateMarket(@Header("Authorization") String headers,
                                      @Body Market market,
                                      @Path("ServiceID") int serviceID);




    @GET("/api/v1/Markets/NearbyMarkets")
    Call<ServiceConfigurationEndPoint> getMarketsList(
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("ServiceURL") String serviceURL,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") int offset
    );



    @GET("/api/v1/Markets/NearbyMarkets")
    Call<ServiceConfigurationEndPoint> getMarketsList(
            @Header("Authorization") String headers,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("ServiceURL") String serviceURL,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") int offset
    );







    @GET ("/api/v1/Markets/MarketsListForAdmin")
    Call<ServiceConfigurationEndPoint> getMarketsList(
            @Header("Authorization") String headers,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("ServiceURL") String serviceURL,
            @Query("SearchString") String searchString,
            @Query("IsOfficial") Boolean isOfficial,@Query("IsVerified")Boolean isVerified,
            @Query("ServiceType") Integer serviceType,
            @Query("SortBy") String sortBy,
            @Query("IsLiveMarkets") boolean isLiveMarkets,
            @Query("Limit") int limit, @Query("Offset") int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );






    @GET ("/api/v1/Markets/GetMarketDetails/{MarketID}")
    Call<Market> getMarketDetails(@Path("MarketID")int marketID,
                                     @Query("latCenter")double latCenter, @Query("lonCenter")double lonCenter);






    // Image Calls
    @POST("/api/v1/Markets/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName


    @DELETE("/api/v1/Markets/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);

}
