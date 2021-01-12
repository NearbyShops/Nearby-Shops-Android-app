package org.nearbyshops.enduserappnew.API;

import org.nearbyshops.enduserappnew.Model.ModelEndPoints.FavouriteShopEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelReviewShop.FavouriteShop;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 2/4/16.
 */

public interface FavouriteShopService {



    @GET("/api/v1/FavouriteShop")
    Call<FavouriteShopEndpoint> getFavouriteShops(
            @Query("EndUserID") Integer userID,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset
    );




    @GET ("/api/v1/FavouriteShop/CheckFavourite")
    Call<ResponseBody> checkFavourite(
            @Query("ShopID")Integer shopID,
            @Query("EndUserID")Integer endUserID
    );



    @POST("/api/v1/FavouriteShop")
    Call<FavouriteShop> insertFavouriteShop(@Body FavouriteShop book);

    @DELETE("/api/v1/FavouriteShop")
    Call<ResponseBody> deleteFavouriteShop(@Query("ShopID") Integer bookID,
                                           @Query("EndUserID") Integer memberID);



}
