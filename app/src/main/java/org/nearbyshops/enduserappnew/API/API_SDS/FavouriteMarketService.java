package org.nearbyshops.enduserappnew.API.API_SDS;


import org.nearbyshops.enduserappnew.Model.ModelEndPoints.FavouriteMarketEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelReviewMarket.FavouriteMarket;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 2/4/16.
 */



public interface FavouriteMarketService {



    @GET("/api/v1/FavouriteMarket")
    Call<FavouriteMarketEndpoint> getFavouriteMarkets(
            @Query("ItemID") Integer itemID,
            @Query("EndUserID") Integer endUserID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @POST("/api/v1/FavouriteMarket")
    Call<FavouriteMarket> insertFavouriteItem(
            @Header("Authorization") String headers,
            @Body FavouriteMarket item
    );

    @DELETE("/api/v1/FavouriteMarket")
    Call<ResponseBody> deleteFavouriteItem(@Header("Authorization") String headers,
                                           @Query("ItemID") Integer itemID,
                                           @Query("EndUserID") Integer endUserID);

}
