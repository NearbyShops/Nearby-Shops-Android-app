package org.nearbyshops.enduserappnew.API;



import org.nearbyshops.enduserappnew.Model.ModelEndPoints.UserEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.DeliveryGuyData;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 30/8/17.
 */


public interface DeliveryGuyLoginService {





    @PUT ("/api/v1/User/DeliveryGuy/UpdateLocation")
    Call<ResponseBody> updateLocation(
            @Header("Authorization") String headers,
            @Body DeliveryGuyData deliveryGuyData
    );






    /*Functions given below are Deprecated*/




    @PUT ("/api/v1/User/DeliveryGuy/UpdateProfileBySelf")
    Call<ResponseBody> updateProfileBySelf(
            @Header("Authorization") String headers,
            @Body User user
    );



    @PUT ("/api/v1/User/DeliveryGuy/{UserID}")
    Call<ResponseBody> updateDeliveryGuyByAdmin(
            @Header("Authorization") String headers,
            @Body User user,
            @Path("UserID") int userID
    );



    @GET ("/api/v1/User/DeliveryGuy/GetDeliveryGuyForShopAdmin")
    Call<UserEndpoint> getDeliveryGuyForShopAdmin(
            @Header("Authorization") String headers,
            @Query("latCurrent") Double latPickUp, @Query("lonCurrent") Double lonPickUp,
            @Query("Gender") Boolean gender,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount") boolean getRowCount,
            @Query("MetadataOnly") boolean getOnlyMetaData
    );


}
