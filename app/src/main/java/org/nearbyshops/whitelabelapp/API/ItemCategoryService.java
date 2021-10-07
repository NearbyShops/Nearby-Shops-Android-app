package org.nearbyshops.whitelabelapp.API;

import org.nearbyshops.whitelabelapp.Model.Image;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ItemCategoryEndPoint;

import java.util.List;

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
 * Created by sumeet on 2/4/16.
 */

public interface ItemCategoryService {





    @GET("api/v1/ItemCategory/QuerySimple")
    Call<ItemCategoryEndPoint> getItemCategoriesQuerySimple(
            @Query("ParentID")Integer parentID,@Query("IsDetached")Boolean parentIsNull,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset
    );




    @GET("api/v1/ItemCategory")
    Call<ItemCategoryEndPoint> getItemCategoriesEndPoint(
            @Query("ShopID") Integer shopID,
            @Query("ParentID") Integer parentID, @Query("IsDetached") Boolean parentIsNull,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("ShopEnabled") Boolean shopEnabled,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );



    @GET("api/v1/ItemCategory")
    Call<ItemCategoryEndPoint> getItemCategories(
            @Query("ShopID") Integer shopID,
            @Query("ParentID") Integer parentID, @Query("IsDetached") Boolean parentIsNull,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );




    @GET ("/api/v1/ItemCategory/GetItemCategoryDetails")
    Call<ItemCategory> getItemCategoryDetails(
            @Query("ItemCategoryID")Integer itemCategoryID
    );




    @POST("/api/v1/ItemCategory")
    Call<ItemCategory> insertItemCategory(@Header("Authorization") String headers,
                                          @Body ItemCategory itemCategory);


    @PUT("/api/v1/ItemCategory/ChangeParent/{id}")
    Call<ResponseBody> changeParent(@Header("Authorization") String headers,
                                    @Body ItemCategory itemCategory,
                                    @Path("id") int id);


    @PUT ("/api/v1/ItemCategory/ChangeParent")
    Call<ResponseBody> changeParentBulk(@Header("Authorization") String headers,
                                        @Body List<ItemCategory> itemCategoryList);





//
//    @PUT("/api/v1/ItemCategory/{id}")
//    Call<ResponseBody> updateItemCategory(@Header("Authorization") String headers,
//                                          @Body ItemCategory itemCategory,
//                                          @Path("id") int id);



    @PUT("/api/v1/ItemCategory/{id}")
    Call<ResponseBody> updateItemCategory(@Header("Authorization") String headers,
                                          @Path("id") int id,
                                          @Body ItemCategory itemCategory
    );




    @PUT("/api/v1/ItemCategory/")
    Call<ResponseBody> updateItemCategoryBulk(@Header("Authorization") String headers,
                                              @Body List<ItemCategory> itemCategoryList);





    @DELETE("/api/v1/ItemCategory/{ItemCategoryID}")
    Call<ResponseBody> deleteItemCategory(@Header("Authorization") String headers,
                                          @Path("ItemCategoryID") int id);






    // Image Calls

//    @POST("/api/v1/ItemCategory/Image")
//    Call<Image> uploadImage(@Header("Authorization") String headers,
//                            @Body RequestBody image);



    @Multipart
    @POST("/api/v1/ItemCategory/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Part MultipartBody.Part img
    );



    @DELETE("/api/v1/ItemCategory/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);


}
