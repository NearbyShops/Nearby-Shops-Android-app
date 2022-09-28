package org.nearbyshops.whitelabelapp.API.OrdersAPI;



import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.OrderItemEndPoint;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface OrderItemService {


    @GET("/api/OrderItem")
    Call<OrderItemEndPoint> getOrderItem(@Header("Authorization") String headers,
                                         @Query("OrderID") Integer orderID,
                                         @Query("ItemID") Integer itemID,
                                         @Query("latCenter")double latCenter, @Query("lonCenter")double lonCenter,
                                         @Query("SearchString") String searchString,
                                         @Query("SortBy") String sortBy,
                                         @Query("Limit") Integer limit, @Query("Offset") Integer offset);


//    @DELETE("/api/OrderItem")
//    Call<ResponseBody> deleteOrderItem(@Query("OrderID") int orderID, @Query("ItemID") int itemID);
//
//
//    @PUT("/api/OrderItem")
//    Call<ResponseBody> updateOrderItem(@Body OrderItem orderItem);
//
//
//    @POST("/api/OrderItem")
//    Call<ResponseBody> createOrderItem(@Body OrderItem orderItem);

}
