package org.nearbyshops.whitelabelapp.API.ShopAPI;

import org.nearbyshops.whitelabelapp.Model.Shop;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ShopUtilityService {



    @POST("/api/v1/ShopUtility")
    Call<Shop> createShop(@Header("Authorization") String headers,
                          @Body Shop shop);



    @POST ("/api/v1/ShopUtility/CreateShopByAdmin/{UserID}")
    Call<Shop> createShopByStaff(@Header("Authorization") String headers,
                                 @Body Shop shop, @Path("UserID")int userID
    );



    @PUT ("/api/v1/ShopUtility/AddBalance/{ShopAdminID}/{AmountToAdd}")
    Call<ResponseBody> addBalance(
            @Header("Authorization") String headers,
            @Path("ShopAdminID") int shopAdminID,
            @Path("AmountToAdd") double amountToAdd
    );




    @PUT("/api/v1/ShopUtility/SetShopOpen")
    Call<ResponseBody> updateShopOpen(@Header("Authorization") String headers);



    @PUT ("/api/v1/ShopUtility/SetShopClosed")
    Call<ResponseBody> setShopClosed(@Header("Authorization") String headers);


}



