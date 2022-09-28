package org.nearbyshops.whitelabelapp.API;



import org.nearbyshops.whitelabelapp.Model.ModelStaff.DeliveryGuyData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

/**
 * Created by sumeet on 30/8/17.
 */


public interface DeliveryGuyService {





    @PUT ("/api/v1/User/DeliveryGuy/SetOffline")
    Call<ResponseBody> setOffline(
            @Header("Authorization") String headers
    );



    @PUT("/api/v1/User/DeliveryGuy/SetOnline")
    Call<ResponseBody> setOnline(
            @Header("Authorization") String headers
    );





    @PUT ("/api/v1/User/DeliveryGuy/UpdateLocation")
    Call<ResponseBody> updateLocation(
            @Header("Authorization") String headers,
            @Body DeliveryGuyData deliveryGuyData
    );





    @GET("/api/v1/User/DeliveryGuy/DeliveryGuyDetails/ForDeliveryDashboard")
    Call<DeliveryGuyData> getDeliveryGuyDetails(
            @Header("Authorization") String headers
    );



    /*Functions given below are Deprecated*/


//
//
//    @PUT ("/api/v1/User/DeliveryGuy/UpdateProfileBySelf")
//    Call<ResponseBody> updateProfileBySelf(
//            @Header("Authorization") String headers,
//            @Body User user
//    );
//
//
//
//    @PUT ("/api/v1/User/DeliveryGuy/{UserID}")
//    Call<ResponseBody> updateDeliveryGuyByAdmin(
//            @Header("Authorization") String headers,
//            @Body User user,
//            @Path("UserID") int userID
//    );
//
//
//
//    @GET ("/api/v1/User/DeliveryGuy/GetDeliveryGuyForShopAdmin")
//    Call<UserEndpoint> getDeliveryGuyForShopAdmin(
//            @Header("Authorization") String headers,
//            @Query("latCurrent") Double latPickUp, @Query("lonCurrent") Double lonPickUp,
//            @Query("Gender") Boolean gender,
//            @Query("SortBy") String sortBy,
//            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
//            @Query("GetRowCount") boolean getRowCount,
//            @Query("MetadataOnly") boolean getOnlyMetaData
//    );


}
