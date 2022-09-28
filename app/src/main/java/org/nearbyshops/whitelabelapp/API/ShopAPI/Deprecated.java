package org.nearbyshops.whitelabelapp.API.ShopAPI;

import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.whitelabelapp.Model.Shop;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface Deprecated {



    @PUT("/api/v1/Shop/BecomeASeller")
    Call<ResponseBody> becomeASeller(@Header("Authorization") String headers);






    @GET("/api/v1/Shop/ForShopFilters")
    Call<ShopEndPoint> getShopForFilters(
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("metadata_only") Boolean metaonly
    );






    @GET ("/api/v1/Shop")
    Call<ShopEndPoint> getShops(
            @Query("LeafNodeItemCategoryID") Integer itemCategoryID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );



    @GET("/api/v1/Shop/GetShopForShopAdmin")
    Call<Shop> getShopForShopAdmin(@Header("Authorization") String headers,
                                   @Query("GetStats")boolean getStats
    );



    @GET ("/api/v1/Shop/GetShopIDForShopAdmin/{ShopAdminID}")
    Call<Shop> getShopIDForShopAdmin(@Header("Authorization") String headers,
                                     @Path("ShopAdminID") int shopAdminID
    );




    @GET ("/api/v1/Shop/GetShopForShopStaff")
    Call<Shop> getShopForShopStaff(@Header("Authorization") String headers);





}



