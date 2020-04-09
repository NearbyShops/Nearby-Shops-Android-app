package org.nearbyshops.enduserappnew.API;

import org.nearbyshops.enduserappnew.Model.ModelCartOrder.CartItem;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by sumeet on 31/5/16.
 */
public interface CartItemService {


    @GET("/api/CartItem")
    Call<List<CartItem>> getCartItem(@Query("CartID") Integer cartID,
                                     @Query("ItemID") Integer itemID,
                                     @Query("EndUserID") Integer endUserID,
                                     @Query("ShopID") Integer shopID,
                                     @Query("GetItems") Boolean getItems);



    @DELETE("/api/CartItem")
    Call<ResponseBody> deleteCartItem(@Query("CartID") int cartID, @Query("ItemID") int itemID,
                                      @Query("EndUserID") int endUserID,
                                      @Query("ShopID") int shopID);


    @PUT("/api/CartItem")
    Call<ResponseBody> updateCartItem(@Body CartItem cartItem,
                                      @Query("EndUserID") int endUserID, @Query("ShopID") int shopID);

    @POST("/api/CartItem")
    Call<ResponseBody> createCartItem(@Body CartItem cartItem,
                                      @Query("EndUserID") int endUserID, @Query("ShopID") int shopID);


}
