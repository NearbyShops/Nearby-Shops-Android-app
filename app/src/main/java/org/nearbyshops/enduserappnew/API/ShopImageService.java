package org.nearbyshops.enduserappnew.API;


import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelImages.ShopImage;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ShopImageService {



    @POST("/api/v1/ShopImage")
    Call<ShopImage> saveShopImage(
            @Header("Authorization") String headers,
            @Body ShopImage shopImage
    );





    @PUT("/api/v1/ShopImage/{ImageID}")
    Call<ResponseBody> updateShopImage(
            @Header("Authorization") String headers,
            @Body ShopImage shopImage,
            @Path("ImageID") int imageID
    );





    @DELETE("/api/v1/ShopImage/{ImageID}")
    Call<ResponseBody> deleteShopImage(
            @Header("Authorization") String headers,
            @Path("ImageID") int imageID
    );




    @GET("/api/v1/ShopImage")
    Call<ShopImageEndPoint> getShopImages(
            @Query("ShopID") Integer shopID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount") boolean getRowCount,
            @Query("MetadataOnly") boolean getOnlyMetaData);





    // Image Calls

    @POST("/api/v1/ShopImage/Image")
    Call<Image> uploadShopImage(
            @Header("Authorization") String headers,
            @Body RequestBody image
    );



    @DELETE("/api/v1/ShopImage/Image/{name}")
    Call<ResponseBody> deleteShopImage(
            @Header("Authorization") String headers,
            @Path("name") String fileName
    );



}
