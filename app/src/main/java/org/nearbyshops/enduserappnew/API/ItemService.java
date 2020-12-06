package org.nearbyshops.enduserappnew.API;

import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ItemEndPoint;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by sumeet on 3/4/16.
 */
public interface ItemService
{



    @GET("/api/v1/Item")
    Call<ItemEndPoint> getItemsEndpoint(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("ShopID") Integer shopID,
            @Query("GetSubcategories")boolean getSubcategories,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("ItemSpecValues") String itemSpecValues,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount") boolean getRowCount,
            @Query("MetadataOnly") boolean getOnlyMetaData
    );





//
//    @GET("/api/v1/Item/OuterJoin")
//    Call<ItemEndPoint> getItemsOuterJoin(
//            @Query("ItemCategoryID")Integer itemCategoryID,
//            @Query("SortBy") String sortBy,
//            @Query("Limit")Integer limit, @Query("Offset")Integer offset,
//            @Query("metadata_only")Boolean metaonly
//    );





    @GET("/api/v1/Item/OuterJoin")
    Call<ItemEndPoint> getItemsOuterJoin(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("GetSubcategories")boolean getSubcategories,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );











    @PUT("/api/v1/Item/ChangeParent/{id}")
    Call<ResponseBody> changeParent(@Header("Authorization") String headers,
                                    @Body Item item,
                                    @Path("id") int id);


    @PUT ("/api/v1/Item/ChangeParent")
    Call<ResponseBody> changeParentBulk(@Header("Authorization") String headers,
                                        @Body List<Item> itemsList);





    @GET("/api/v1/Item/{id}")
    Call<Item> getItem(@Path("id") int ItemID);




    @POST("/api/v1/Item")
    Call<Item> insertItem(@Header("Authorization") String headers,
                          @Body Item item);



    @PUT("/api/v1/Item/{id}")
    Call<ResponseBody> updateItem(@Header("Authorization") String headers,
                                  @Body Item item,
                                  @Path("id") int id);




    @DELETE("/api/v1/Item/{id}")
    Call<ResponseBody> deleteItem(@Header("Authorization") String headers,
                                  @Path("id") int id);





    // Image Calls

    @POST("/api/v1/Item/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);


    @DELETE("/api/v1/Item/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);


}
