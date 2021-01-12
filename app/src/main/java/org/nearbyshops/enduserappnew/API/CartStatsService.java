package org.nearbyshops.enduserappnew.API;


import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;





public interface CartStatsService {

    @GET("/api/CartStats/{EndUserID}")
    Call<List<CartStats>> getCartStatsList(
            @Path("EndUserID") int endUserID, @Query("CartID") Integer cartID,
            @Query("ShopID") Integer shopID, @Query("GetShopDetails") Boolean getShopDetails,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter
    );



    @GET ("/api/CartStats/{EndUserID}/{ShopID}")
    Call<CartStats> getCartStats(
            @Path("EndUserID")int endUserID, @Path("ShopID") int shopID,
            @Query("GetShopDetails") boolean getShopDetails,
            @Query("GetDeliveryConfig") boolean getDeliveryConfig
    );


}
