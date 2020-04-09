package org.nearbyshops.enduserappnew.API;


import org.nearbyshops.enduserappnew.Model.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.enduserappnew.Model.ModelRoles.StaffPermissions;

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




    @PUT("/api/v1/User/StaffLogin/UpdateStaffLocation")
    Call<ResponseBody> updateStaffLocation(
            @Header("Authorization") String headers,
            @Body ShopStaffPermissions permissions
    );



    @PUT ("/api/v1/User/StaffLogin/UpgradeUser/{emailorphone}/{SecretCode}")
    Call<ResponseBody> upgradeUserToStaff(
            @Header("Authorization") String headers,
            @Path("emailorphone") String emailorphone,
            @Path("SecretCode") int secretCode
    );




    @PUT ("/api/v1/User/StaffLogin/UpdateStaffPermissions")
    Call<ResponseBody> updateStaffPermissions(
            @Header("Authorization") String headers,
            @Body StaffPermissions permissions
    );




    @GET("/api/v1/User/StaffLogin/GetStaffPermissions/{UserID}")
    Call<StaffPermissions> getPermissionDetails(
            @Header("Authorization") String headers,
            @Path("UserID") int userID
    );


}
