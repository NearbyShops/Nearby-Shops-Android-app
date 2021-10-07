package org.nearbyshops.whitelabelapp.API;

import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.CartItem;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by sumeet on 31/5/16.
 */
public interface CartItemService {


//    @Query("CartID") Integer cartID,
//    @Query("ItemID") Integer itemID,
//@Query("GetItems") Boolean getItems,



    @GET("/api/CartItem")
    Call<List<CartItem>> getCartItem(
            @Query("EndUserID") int endUserID,
            @Query("ShopID") int shopID,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")Integer offset
    );




    @GET ("/api/CartItem/GetAvailability/{ShopID}/{EndUserID}")
    Call<List<CartItem>> getCartItemAvailability(
            @Path("EndUserID") Integer endUserID,
            @Path("ShopID") Integer shopID
    );





    @GET ("/api/CartItem/GetAvailabilityByItem/{ItemID}/{EndUserID}")
    Call<List<CartItem>> getCartItemAvailabilityByItem(
            @Path("EndUserID") int endUserID,
            @Path("ItemID") int itemID
    );





    @DELETE("/api/CartItem")
    Call<ResponseBody> deleteCartItem(@Query("CartID") int cartID, @Query("ItemID") Integer itemID,
                                      @Query("EndUserID") int endUserID,
                                      @Query("ShopID") int shopID);


    @PUT("/api/CartItem")
    Call<ResponseBody> updateCartItem(@Body CartItem cartItem,
                                      @Query("EndUserID") int endUserID, @Query("ShopID") int shopID);

    @POST("/api/CartItem")
    Call<ResponseBody> createCartItem(@Body CartItem cartItem,
                                      @Query("EndUserID") int endUserID, @Query("ShopID") int shopID);


}
