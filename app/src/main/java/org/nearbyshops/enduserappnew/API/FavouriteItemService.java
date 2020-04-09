package org.nearbyshops.enduserappnew.API;

import org.nearbyshops.enduserappnew.Model.ModelEndPoints.FavouriteItemEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelReviewItem.FavouriteItem;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 2/4/16.
 */

public interface FavouriteItemService {

    @GET("/api/v1/FavouriteItem")
    Call<FavouriteItemEndpoint> getFavouriteBooks(
            @Query("ItemID") Integer itemID,
            @Query("EndUserID") Integer endUserID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @POST("/api/v1/FavouriteItem")
    Call<FavouriteItem> insertFavouriteItem(@Body FavouriteItem item);

    @DELETE("/api/v1/FavouriteItem")
    Call<ResponseBody> deleteFavouriteItem(@Query("ItemID") Integer itemID,
                                           @Query("EndUserID") Integer endUserID);

}
