package org.nearbyshops.enduserappnew.API;


import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ItemImageEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelImages.ItemImage;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 3/4/16.
 */
public interface ItemImageService
{



    @POST("/api/v1/ItemImage")
    Call<ItemImage> saveItemImage(@Header("Authorization") String headers,
                                  @Body ItemImage itemImage);




    @PUT("/api/v1/ItemImage/{ImageID}")
    Call<ResponseBody> updateItemImage(@Header("Authorization") String headers,
                                       @Body ItemImage itemImage, @Path("ImageID") int imageID);


    @DELETE("/api/v1/ItemImage/{ImageID}")
    Call<ResponseBody> deleteItemImageData(@Header("Authorization") String headers,
                                           @Path("ImageID") int imageID);




    @GET("/api/v1/ItemImage")
    Call<ItemImageEndPoint> getItemImages(
            @Query("ItemID") Integer itemID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly);




    // Image Calls

    @POST("/api/v1/ItemImage/Image")
    Call<Image> uploadItemImage(@Header("Authorization") String headers,
                                @Body RequestBody image);


    @DELETE("/api/v1/ItemImage/Image/{name}")
    Call<ResponseBody> deleteItemImage(@Header("Authorization") String headers,
                                       @Path("name") String fileName);

}
