package org.nearbyshops.enduserappnew.API;





import org.nearbyshops.enduserappnew.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduserappnew.Model.ShopItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by sumeet on 14/3/16.
 */
public interface ShopItemService {



    @PUT("/api/v1/ShopItem")
    Call<ResponseBody> putShopItem(@Header("Authorization") String headers,
                                   @Body ShopItem shopItem);

    @DELETE("/api/v1/ShopItem")
    Call<ResponseBody> deleteShopItem(@Header("Authorization") String headers,
                                      @Query("ShopID") int ShopID, @Query("ItemID") int itemID);






    @GET("/api/v1/ShopItem")
    Call<ShopItemEndPoint> getShopItemEndpoint(
            @Query("ItemCategoryID") Integer ItemCategoryID,
            @Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("EndUserID") Integer endUserID, @Query("IsFilledCart") Boolean isFilledCart,
            @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero") Boolean priceEqualsZero,
            @Query("MinPrice") Integer minPrice, @Query("MaxPrice") Integer maxPrice,
            @Query("SearchString") String searchString,
            @Query("ShopEnabled") Boolean shopEnabled,
            @Query("SortBy") String sortBy,
            @Query("Limit") int limit, @Query("Offset") int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );





    @GET("/api/v1/ShopItem")
    Call<ShopItemEndPoint> getShopItemEndpoint(
            @Query("ItemCategoryID") Integer ItemCategoryID,
            @Query("GetSubcategories")boolean getSubcategories,
            @Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("EndUserID") Integer endUserID, @Query("IsFilledCart") Boolean isFilledCart,
            @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero") Boolean priceEqualsZero,
            @Query("MinPrice") Integer minPrice, @Query("MaxPrice") Integer maxPrice,
            @Query("SearchString") String searchString,
            @Query("ShopEnabled") Boolean shopEnabled,
            @Query("SortBy") String sortBy,
            @Query("Limit") int limit, @Query("Offset") int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );







    @GET("/api/v1/ShopItem")
    Call<ShopItemEndPoint> getShopItemEndpoint(
            @Query("ItemCategoryID") Integer ItemCategoryID,
            @Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("EndUserID") Integer endUserID, @Query("IsFilledCart") Boolean isFilledCart,
            @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero") Boolean priceEqualsZero,
            @Query("MinPrice") Integer minPrice, @Query("MaxPrice") Integer maxPrice,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") int limit, @Query("Offset") int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );








    @GET("/api/v1/ShopItem/ForShop")
    Call<ShopItemEndPoint> getShopItemsForShop(
            @Query("ItemCategoryID") Integer ItemCategoryID,
            @Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") int offset
    );




    // bulk update methods


    @POST("/api/v1/ShopItem/CreateBulk")
    Call<ResponseBody> createShopItemBulk(@Header("Authorization") String headers,
                                          @Body List<ShopItem> itemList);

    @POST("/api/v1/ShopItem/DeleteBulk")
    Call<ResponseBody> deleteShopItemBulk(@Header("Authorization") String headers,
                                          @Body List<ShopItem> itemList);





}
