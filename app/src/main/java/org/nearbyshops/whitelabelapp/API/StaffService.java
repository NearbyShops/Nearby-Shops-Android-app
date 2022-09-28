package org.nearbyshops.whitelabelapp.API;


import org.nearbyshops.whitelabelapp.Model.ModelStaff.ShopStaffPermissions;
import org.nearbyshops.whitelabelapp.Model.ModelStaff.StaffPermissions;

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


public interface StaffService {




    @PUT("/api/v1/User/Staff/UpdateStaffLocation")
    Call<ResponseBody> updateStaffLocation(
            @Header("Authorization") String headers,
            @Body ShopStaffPermissions permissions
    );



    @PUT ("/api/v1/User/Staff/UpgradeUser/{emailorphone}/{Role}/{SecretCode}")
    Call<ResponseBody> upgradeUserToStaff(
            @Header("Authorization") String headers,
            @Path("emailorphone") String emailorphone,
            @Path("Role")int role,
            @Path("SecretCode") int secretCode
    );




    @PUT ("/api/v1/User/Staff/UpdateStaffPermissions")
    Call<ResponseBody> updateStaffPermissions(
            @Header("Authorization") String headers,
            @Body StaffPermissions permissions
    );




    @GET("/api/v1/User/Staff/GetStaffPermissions/{UserID}")
    Call<StaffPermissions> getPermissionDetails(
            @Header("Authorization") String headers,
            @Path("UserID") int userID
    );


}
