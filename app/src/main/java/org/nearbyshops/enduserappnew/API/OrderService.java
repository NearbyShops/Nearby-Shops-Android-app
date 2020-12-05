package org.nearbyshops.enduserappnew.API;

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.OrderEndPoint;

import java.sql.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 31/5/16.
 */
public interface OrderService {




    @POST("/api/Order")
    Call<ResponseBody> postOrder(@Body Order order, @Query("CartID") int cartID);





    @GET("/api/Order")
    Call<OrderEndPoint> getOrders(
            @Header("Authorization") String headers,
            @Query("ShopID")Integer shopID,
            @Query("EndUserID") Integer endUserID,
            @Query("DeliveryMode") Integer deliveryMode,
            @Query("StatusHomeDelivery")Integer homeDeliveryStatus,
            @Query("StatusPickFromShopStatus")Integer pickFromShopStatus,
            @Query("PendingOrders") Boolean pendingOrders,

            @Query("GetFilterDeliveryPerson") boolean getFilterDeliveryPerson,
            @Query("GetFilterShops") boolean getFilterShops,
            @Query("GetFilterDeliverySlots") boolean getFilterDeliverySlots,

            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit")int limit, @Query("Offset")int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );



    @GET ("/api/Order/OrdersListForEndUser")
    Call<OrderEndPoint> getOrdersForEndUser(
            @Header("Authorization") String headers,
            @Query("ShopID")Integer shopID,
            @Query("EndUserID") Integer endUserID,
            @Query("DeliveryMode") Integer deliveryMode,
            @Query("DeliveryDate") Date deliveryDate,
            @Query("DeliverySlotID")Integer deliverySlotID,
            @Query("StatusHomeDelivery")Integer homeDeliveryStatus,
            @Query("StatusPickFromShopStatus")Integer pickFromShopStatus,
            @Query("PendingOrders") Boolean pendingOrders,

            @Query("GetFilterDeliveryPerson") boolean getFilterDeliveryPerson,
            @Query("GetFilterShops") boolean getFilterShops,
            @Query("GetFilterDeliverySlots") boolean getFilterDeliverySlots,

            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit")int limit, @Query("Offset")int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );



    @GET ("/api/Order/OrdersListForDelivery")
    Call<OrderEndPoint> getOrdersForDelivery(
            @Header("Authorization") String headers,
            @Query("DeliveryDate") Date deliveryDate,
            @Query("isASAPDelivery") boolean isASAPDelivery,
            @Query("DeliverySlotID")Integer deliverySlotID,
            @Query("ShopID")Integer shopID,

            @Query("DeliveryGuyNull")boolean deliveryGuyNull,
            @Query("DeliveryGuyID")Integer deliveryGuyID,

            @Query("DeliveryMode") Integer deliveryMode,

            @Query("StatusHDLessThan")Integer statusHDLessThan,
            @Query("StatusHomeDelivery")Integer homeDeliveryStatus,

            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,

            @Query("GetFilterDeliveryPerson") boolean getFilterDeliveryPerson,
            @Query("GetFilterShops") boolean getFilterShops,
            @Query("GetFilterDeliverySlots") boolean getFilterDeliverySlots,

            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit")int limit, @Query("Offset")int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );




    @PUT ("/api/Order/CancelByUser/{OrderID}/{EndUserID}")
    Call<ResponseBody> cancelOrderByEndUser(@Header("Authorization") String headers,
                                            @Path("OrderID")int orderID,
                                            @Path("EndUserID")int endUserID);



    @PUT ("/api/Order/CancelByShop/{OrderID}/{ShopID}")
    Call<ResponseBody> cancelOrderByShop(@Header("Authorization") String headers,
                                         @Path("OrderID")int orderID,
                                         @Path("ShopID")int shopID);


}
