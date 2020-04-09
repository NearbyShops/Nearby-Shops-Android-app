package org.nearbyshops.enduserappnew.API;

import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.enduserappnew.Model.Shop;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ShopService {


    @POST("/api/v1/Shop")
    Call<Shop> createShop(@Header("Authorization") String headers,
                          @Body Shop shop);




    @DELETE ("/api/v1/Shop/{ShopID}")
    Call<ResponseBody> deleteShop(@Header("Authorization") String headers,
                                  @Path("ShopID")int shopID
    );




    @PUT("/api/v1/Shop/UpdateBySelf")
    Call<ResponseBody> updateBySelf(@Header("Authorization") String headers,
                                    @Body Shop shop);






    @PUT("/api/v1/Shop/UpdateByAdmin/{ShopID}")
    Call<ResponseBody> updateShopByAdmin(
            @Header("Authorization") String headers,
            @Body Shop shop,
            @Path("ShopID")int ShopID
    );







    @GET("/api/v1/Shop/GetShopDetails/{id}")
    Call<Shop> getShopDetails(
            @Path("id") int id,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter
    );



    @GET("/api/v1/Shop/GetShopForShopAdmin")
    Call<Shop> getShopForShopAdmin(@Header("Authorization") String headers);




    @GET ("/api/v1/Shop/GetShopForShopStaff")
    Call<Shop> getShopForShopStaff(@Header("Authorization") String headers);





    @PUT ("/api/v1/Shop/AddBalance/{ShopAdminID}/{AmountToAdd}")
    Call<ResponseBody> addBalance(
            @Header("Authorization") String headers,
            @Path("ShopAdminID") int shopAdminID,
            @Path("AmountToAdd") double amountToAdd
    );







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
            @Query("DistributorID") Integer distributorID,
            @Query("LeafNodeItemCategoryID") Integer itemCategoryID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );





    @GET("/api/v1/Shop/QuerySimple")
    Call<ShopEndPoint> getShopListSimple(
            @Query("UnderReview")Boolean underReview,
            @Query("Enabled")Boolean enabled, @Query("Waitlisted") Boolean waitlisted,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") int limit, @Query("Offset") int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );




    @GET("/api/v1/Shop/FilterByItemCat/{ItemCategoryID}")
    Call<ShopEndPoint> filterShopsByItemCategory(
            @Path("ItemCategoryID") Integer itemCategoryID,
            @Query("DistributorID") Integer distributorID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly);







    @PUT("/api/v1/Shop/BecomeASeller")
    Call<ResponseBody> becomeASeller(@Header("Authorization") String headers);







    @PUT("/api/v1/Shop/SetShopOpen")
    Call<ResponseBody> updateShopOpen(@Header("Authorization") String headers);



    @PUT ("/api/v1/Shop/SetShopClosed")
    Call<ResponseBody> updateShopClosed(@Header("Authorization") String headers);






    // Image Calls

    @POST("/api/v1/Shop/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);


    @DELETE("/api/v1/Shop/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);



//    @GET("/api/v1/Shop/QuerySimple")
//    Call<ShopEndPoint> getShopListSimple(
//            @Query("Enabled")Boolean enabled,
//            @Query("Waitlisted") Boolean waitlisted,
//            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
//            @Query("deliveryRangeMax")Double deliveryRangeMax,
//            @Query("deliveryRangeMin")Double deliveryRangeMin,
//            @Query("proximity")Double proximity,
//            @Query("SearchString") String searchString,
//            @Query("SortBy") String sortBy,
//            @Query("Limit") Integer limit, @Query("Offset") int offset
//    );


}



