package org.nearbyshops.whitelabelapp.API.ShopAPI;

import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.whitelabelapp.Model.Shop;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ShopDetailService {




    @GET ("/api/v1/ShopDetails/ForEditScreen")
    Call<Shop> getShopForEditScreen(
            @Header("Authorization")String headerParam,
            @Query("ShopID") int shopID,
            @Query("ShopAdminID") int shopAdminID
    );




    @GET ("/api/v1/ShopDetails/ForShopDashboard")
    Call<Shop> getShopForShopDashboard(
            @Header("Authorization")String headerParam
    );



    @GET ("/api/v1/ShopDetails/ForDetailScreen")
    Call<ShopImageEndPoint> getShopDetailsForDetailScreen(
            @Header("Authorization")String headerParam,
            @Query("GetShopDetails")boolean getShopDetails,
            @Query("ShopID")Integer shopID,
            @Query("latCenter") Double latCenter,
            @Query("lonCenter") Double lonCenter,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")int offset
    );


    @GET("/api/v1/ShopDetails/ForItemsInShopScreen/{ShopID}")
    Call<Shop> getShopDetailsForItemsInShopScreen(
            @Path("ShopID") int shopId,
            @Query("latCenter") Double latCenter,
            @Query("lonCenter") Double lonCenter
    );



}



