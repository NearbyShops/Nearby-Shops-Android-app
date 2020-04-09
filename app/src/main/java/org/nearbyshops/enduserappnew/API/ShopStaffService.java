package org.nearbyshops.enduserappnew.API;


import org.nearbyshops.enduserappnew.Model.ModelRoles.ShopStaffPermissions;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by sumeet on 30/8/17.
 */


public interface ShopStaffService {



    @PUT("/api/v1/User/ShopStaffLogin/UpdateStaffLocation")
    Call<ResponseBody> updateStaffLocation(
            @Header("Authorization") String headers,
            @Body ShopStaffPermissions permissions
    );




    @PUT ("/api/v1/User/ShopStaffLogin/UpgradeUser/{emailorphone}/{Role}/{SecretCode}")
    Call<ResponseBody> upgradeUserToShopStaff(
            @Header("Authorization") String headers,
            @Path("emailorphone")String emailorphone,
            @Path("Role")int role,
            @Path("SecretCode")int secretCode
    );



    @PUT ("/api/v1/User/ShopStaffLogin/UpdateStaffPermissions")
    Call<ResponseBody> updateStaffPermissions(
            @Header("Authorization") String headers,
            @Body ShopStaffPermissions permissions
    );



    @GET("/api/v1/User/ShopStaffLogin/GetStaffPermissions/{UserID}")
    Call<ShopStaffPermissions> getUserDetails(
            @Header("Authorization") String headers,
            @Path("UserID")int userID
    );


}
