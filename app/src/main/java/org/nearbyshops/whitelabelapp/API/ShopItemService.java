package org.nearbyshops.whitelabelapp.API;





import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ItemImageEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.whitelabelapp.Model.ShopItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 14/3/16.
 */
public interface ShopItemService {



    @GET ("/api/v1/ShopItem/GetShopItemDetails")
    Call<ItemImageEndPoint> getShopItemDetails(
            @Header("Authorization")String headerParam,
            @Query("GetShopItemDetails")boolean getShopItemDetails,
            @Query("ItemID")Integer itemID,
            @Query("ShopID")Integer shopID,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")int offset
    );




    @PUT("/api/v1/ShopItem/Update/{ShopID}")
    Call<ResponseBody> putShopItem(@Header("Authorization") String headers,
                                   @Path("ShopID")int shopID,
                                   @Body ShopItem shopItem);



    @DELETE("/api/v1/ShopItem/Delete")
    Call<ResponseBody> deleteShopItem(@Header("Authorization") String headers,
                                      @Query("ShopID") int shopID, @Query("ItemID") int itemID);




//    @GET("/api/v1/ShopItem")
//    Call<ShopItemEndPoint> getShopItemEndpoint(
//            @Query("ItemCategoryID") Integer ItemCategoryID,
//            @Query("FilterCategoriesRecursively")boolean filterCategoriesRecursively,
//            @Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
//            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
//            @Query("deliveryRangeMax") Double deliveryRangeMax,
//            @Query("deliveryRangeMin") Double deliveryRangeMin,
//            @Query("proximity") Double proximity,
//            @Query("EndUserID") Integer endUserID, @Query("IsFilledCart") Boolean isFilledCart,
//            @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero") Boolean priceEqualsZero,
//            @Query("MinPrice") Integer minPrice, @Query("MaxPrice") Integer maxPrice,
//            @Query("SearchString") String searchString,
//            @Query("ShopEnabled") Boolean shopEnabled,
//            @Query("SortBy") String sortBy,
//            @Query("Limit") int limit, @Query("Offset") int offset,
//            @Query("GetRowCount")boolean getRowCount,
//            @Query("MetadataOnly")boolean getOnlyMetaData
//    );
//
//
//
//
//
//    @GET("/api/v1/ShopItem")
//    Call<ShopItemEndPoint> getShopItemEndpoint(
//            @Query("ItemCategoryID") Integer ItemCategoryID,
//            @Query("FilterCategoriesRecursively")boolean filterCategoriesRecursively,
//            @Query("GetSubcategories")boolean getSubcategories,
//            @Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
//            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
//            @Query("deliveryRangeMax") Double deliveryRangeMax,
//            @Query("deliveryRangeMin") Double deliveryRangeMin,
//            @Query("proximity") Double proximity,
//            @Query("EndUserID") Integer endUserID, @Query("IsFilledCart") Boolean isFilledCart,
//            @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero") Boolean priceEqualsZero,
//            @Query("MinPrice") Integer minPrice, @Query("MaxPrice") Integer maxPrice,
//            @Query("SearchString") String searchString,
//            @Query("ShopEnabled") Boolean shopEnabled,
//            @Query("SortBy") String sortBy,
//            @Query("Limit") int limit, @Query("Offset") int offset,
//            @Query("GetRowCount")boolean getRowCount,
//            @Query("MetadataOnly")boolean getOnlyMetaData
//    );
//
//
//
//
//
//
//
//    @GET("/api/v1/ShopItem")
//    Call<ShopItemEndPoint> getShopItemEndpoint(
//            @Query("ItemCategoryID") Integer ItemCategoryID,
//            @Query("FilterCategoriesRecursively")boolean filterCategoriesRecursively,
//            @Query("ShopID") Integer ShopID, @Query("ItemID") Integer itemID,
//            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
//            @Query("deliveryRangeMax") Double deliveryRangeMax,
//            @Query("deliveryRangeMin") Double deliveryRangeMin,
//            @Query("proximity") Double proximity,
//            @Query("EndUserID") Integer endUserID, @Query("IsFilledCart") Boolean isFilledCart,
//            @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero") Boolean priceEqualsZero,
//            @Query("MinPrice") Integer minPrice, @Query("MaxPrice") Integer maxPrice,
//            @Query("SearchString") String searchString,
//            @Query("SortBy") String sortBy,
//            @Query("Limit") int limit, @Query("Offset") int offset,
//            @Query("GetRowCount")boolean getRowCount,
//            @Query("MetadataOnly")boolean getOnlyMetaData
//    );





    @GET ("/api/v1/ShopItem/ShopItemsByShop")
    Call<ShopItemEndPoint> getShopItemsByShop(
            @Query("ItemCategoryID")Integer ItemCategoryID,
            @Query("FilterCategoriesRecursively")boolean filterByCategoryRecursively,
            @Query("GetSubcategories")boolean getSubcategories,
            @Query("ShopID")Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("EndUserID") Integer endUserID, @Query("IsFilledCart") Boolean isFilledCart,
            @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero")Boolean priceEqualsZero,
            @Query("MinPrice")Integer minPrice, @Query("MaxPrice")Integer maxPrice,
            @Query("SearchString") String searchString,
            @Query("ShopEnabled")Boolean shopEnabled,
            @Query("PriceGreaterThanZero") Boolean priceGreaterThanZero,
            @Query("SortBy") String sortBy,
            @Query("Limit") int limit, @Query("Offset") int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );



    @GET ("/api/v1/ShopItem/ShopItemsByItem")
    Call<ShopItemEndPoint> getShopItemsByItem(
            @Query("ItemCategoryID")Integer ItemCategoryID,
            @Query("GetSubcategories")boolean getSubcategories,
            @Query("ShopID")Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("EndUserID") Integer endUserID, @Query("IsFilledCart") Boolean isFilledCart,
            @Query("IsOutOfStock") Boolean isOutOfStock, @Query("PriceEqualsZero")Boolean priceEqualsZero,
            @Query("MinPrice")Integer minPrice, @Query("MaxPrice")Integer maxPrice,
            @Query("SearchString") String searchString,
            @Query("ShopEnabled")Boolean shopEnabled,
            @Query("SortBy") String sortBy,
            @Query("Limit") int limit, @Query("Offset") int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );





    @GET ("/api/v1/ShopItem/GetAvailableShops")
    Call<ShopItemEndPoint> getAvailableShops(
            @Query("ItemID") Integer itemID,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("EndUserID") Integer endUserID, @Query("GetFilledCart") boolean GetFilledCart,
            @Query("SearchString") String searchString,
            @Query("ShopEnabled")Boolean shopEnabled,
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


    @POST("/api/v1/ShopItem/CreateBulk/{ShopID}")
    Call<ResponseBody> createShopItemBulk(@Header("Authorization") String headers,
                                          @Path("ShopID")int shopID,
                                          @Body List<ShopItem> itemList);


    @POST("/api/v1/ShopItem/DeleteBulk/{ShopID}")
    Call<ResponseBody> deleteShopItemBulk(@Header("Authorization") String headers,
                                          @Path("ShopID")int shopID,
                                          @Body List<ShopItem> itemList);





}
