package org.nearbyshops.enduserappnew.API;



import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.UserEndpoint;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface OrderServiceShopStaff {


    @PUT("/api/Order/ShopStaff/SetConfirmed/{OrderID}")
    Call<ResponseBody> confirmOrder(@Header("Authorization") String headers,
                                    @Path("OrderID") int orderID);


    @PUT("/api/Order/ShopStaff/SetOrderPacked/{OrderID}")
    Call<ResponseBody> setOrderPacked(@Header("Authorization") String headers,
                                      @Path("OrderID") int orderID);



    @PUT("/api/Order/ShopStaff/SetOutForDelivery/{OrderID}")
    Call<ResponseBody> setOutForDelivery(@Header("Authorization") String headers,
                                         @Path("OrderID") int orderID);



    @PUT("/api/Order/ShopStaff/DeliverOrder/{OrderID}")
    Call<ResponseBody> deliverOrder(@Header("Authorization") String headers,
                                     @Path("OrderID") int orderID);




    @PUT("/api/Order/ShopStaff/ReturnOrder/{OrderID}")
    Call<ResponseBody> returnOrder(@Header("Authorization") String headers,
                                     @Path("OrderID") int orderID);


//    @PUT("/api/Order/ShopStaffAccounts/HandoverToDelivery/{OrderID}")
//    Call<ResponseBody> handoverToDelivery(@Path("OrderID")int orderID,@Body Order order);


    @PUT("/api/Order/ShopStaff/HandoverToDelivery/{DeliveryGuySelfID}")
    Call<ResponseBody> handoverToDelivery(@Header("Authorization") String headers,
                                          @Path("DeliveryGuySelfID") int deliveryGuyID,
                                          @Body List<Order> ordersList);




    @PUT("/api/Order/ShopStaff/UndoHandover/{OrderID}")
    Call<ResponseBody> undoHandover(@Header("Authorization") String headers,
                                    @Path("OrderID") int orderID);




    @PUT("/api/Order/ShopStaff/AcceptReturn/{OrderID}")
    Call<ResponseBody> acceptReturn(@Header("Authorization") String headers,
                                    @Path("OrderID") int orderID);


    @PUT ("/api/Order/ShopStaff/UnpackOrder/{OrderID}")
    Call<ResponseBody> unpackOrder(
            @Header("Authorization") String headers,
            @Path("OrderID") int orderID
    );



    @PUT("/api/Order/ShopStaff/PaymentReceived/{OrderID}")
    Call<ResponseBody> paymentReceived(@Header("Authorization") String headers,
                                       @Path("OrderID") int orderID);


    @PUT("/api/Order/ShopStaff/PaymentReceived")
    Call<ResponseBody> paymentReceivedBulk(@Header("Authorization") String headers,
                                           @Body List<Order> ordersList);




//    @GET("/api/Order/ShopStaff")
//    Call<OrderEndPoint> getOrders(@Header("Authorization") String headers,
//                                  @Query("OrderID") Integer orderID,
//                                  @Query("EndUserID") Integer endUserID,
//                                  @Query("PickFromShop") Boolean pickFromShop,
//                                  @Query("StatusHomeDelivery") Integer homeDeliveryStatus,
//                                  @Query("StatusPickFromShopStatus") Integer pickFromShopStatus,
//                                  @Query("DeliveryGuyID") Integer deliveryGuyID,
//                                  @Query("PaymentsReceived") Boolean paymentsReceived,
//                                  @Query("DeliveryReceived") Boolean deliveryReceived,
//                                  @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
//                                  @Query("PendingOrders") Boolean pendingOrders,
//                                  @Query("SearchString") String searchString,
//                                  @Query("SortBy") String sortBy,
//                                  @Query("Limit") Integer limit, @Query("Offset") Integer offset,
//                                  @Query("metadata_only") Boolean metaonly);








    @GET ("/api/Order/ShopStaff/FetchDeliveryGuys")
    Call<UserEndpoint> fetchDeliveryGuys(
            @Header("Authorization") String headers,
            @Query("StatusHomeDelivery") Integer homeDeliveryStatus,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount") boolean getRowCount,
            @Query("MetadataOnly") boolean getOnlyMetaData

    );




    

    @PUT ("/api/Order/ShopStaff/SetConfirmedPFS/{OrderID}")
    Call<ResponseBody> confirmOrderPFS(
            @Header("Authorization") String headers,
            @Path("OrderID") int orderID
    );


    @PUT ("/api/Order/ShopStaff/SetOrderPackedPFS/{OrderID}")
    Call<ResponseBody> setOrderPackedPFS(
            @Header("Authorization") String headers,
            @Path("OrderID") int orderID
    );


    @PUT ("/api/Order/ShopStaff/SetOrderReadyForPickupPFS/{OrderID}")
    Call<ResponseBody> setOrderReadyForPickupPFS(
            @Header("Authorization") String headers,
            @Path("OrderID") int orderID
    );



    @PUT ("/api/Order/ShopStaff/PaymentReceivedPFS/{OrderID}")
    Call<ResponseBody> paymentReceivedPFS(
            @Header("Authorization") String headers,
            @Path("OrderID") int orderID
    );







    /*******************************************************************************/

    @PUT("/api/Order/ShopStaff/MarkDelivered/{OrderID}")
    Call<ResponseBody> markDelivered(@Header("Authorization") String headers,
                                     @Path("OrderID") int orderID);




    // previous methods

//    @GET("/api/Order/{id}")
//    Call<Order> getOrder(@Path("id") int orderID);

//    @PUT("/api/Order/{OrderID}")
//    Call<ResponseBody> putOrder(@Path("OrderID") int orderID, @Body Order order);

//    @PUT("/api/Order/ReturnOrder/{OrderID}")
//    Call<ResponseBody> returnOrder(@Path("OrderID") int orderID);

//    @PUT("/api/Order/CancelByShop/{OrderID}")
//    Call<ResponseBody> cancelOrderByShop(@Path("OrderID") int orderID);

}
