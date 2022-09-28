package org.nearbyshops.whitelabelapp.API.API_Admin;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ShopServiceForAdmin {



    @PUT ("/api/v1/Shop/ForAdmin/EnableShop/{ShopID}")
    Call<ResponseBody> enableShop(
            @Header("Authorization") String headers,
            @Path("ShopID") int ShopID
    );




    @PUT ("/api/v1/Shop/ForAdmin/WaitlistShop/{ShopID}")
    Call<ResponseBody> setShopWaitlisted(
            @Header("Authorization") String headers,
            @Path("ShopID") int ShopID
    );




    @PUT ("/api/v1/Shop/ForAdmin/DisableShop/{ShopID}")
    Call<ResponseBody> disableShop(
            @Header("Authorization") String headers,
            @Path("ShopID") int ShopID
    );


}



