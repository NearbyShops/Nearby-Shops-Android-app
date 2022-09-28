package org.nearbyshops.whitelabelapp.API.ShopAPI;

import org.nearbyshops.whitelabelapp.Model.Image;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.whitelabelapp.Model.Shop;

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
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ShopService {




    @PUT("/api/v1/Shop/UpdateByAdmin/{ShopID}")
    Call<ResponseBody> updateShopByAdmin(
            @Header("Authorization") String headers,
            @Body Shop shop,
            @Path("ShopID")int ShopID
    );




    @PUT("/api/v1/Shop/UpdateBySelf")
    Call<ResponseBody> updateBySelf(@Header("Authorization") String headers,
                                    @Body Shop shop);





    @DELETE ("/api/v1/Shop/{ShopID}")
    Call<ResponseBody> deleteShop(@Header("Authorization") String headers,
                                  @Path("ShopID")int shopID
    );





    @GET("/api/v1/Shop/QuerySimple")
    Call<ShopEndPoint> getShopListSimple(
            @Header("Authorization") String headers,
            @Query("RegistrationStatus") Integer registrationStatus,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") int limit, @Query("Offset") int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );





    @GET("/api/v1/Shop/FilterShopsByItemCat")
    Call<ShopEndPoint> filterShopsByItemCategory(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("MarketID") Integer marketID,
            @Query("MarketEnabled") boolean marketEnabled,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("proximity") Double proximity,
            @Query("GetSubcategories")boolean getSubcategories,
            @Query("GetBannerImages")boolean getBannerImages,
            @Query("GetCurrency") boolean getCurrency,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData);








    // Image Calls


//    @POST("/api/v1/Shop/Image")
//    Call<Image> uploadImage(@Header("Authorization") String headers,
//                            @Body RequestBody image);

    @Multipart
    @POST("/api/v1/Shop/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Part MultipartBody.Part img);


    @DELETE("/api/v1/Shop/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);



}



