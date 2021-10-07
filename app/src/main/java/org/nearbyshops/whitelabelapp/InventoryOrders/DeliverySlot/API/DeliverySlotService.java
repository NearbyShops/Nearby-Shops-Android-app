package org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.API;

import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.Model.DeliverySlotEndpoint;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.Model.DeliverySlot;

import java.sql.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 31/5/16.
 */
public interface DeliverySlotService {



    @POST ("/api/DeliverySlot")
    Call<DeliverySlot> createSlot(@Body DeliverySlot address);


    @PUT ("/api/DeliverySlot/{DeliverySlotID}")
    Call<ResponseBody> updateSlot(@Path("DeliverySlotID")int slotID, @Body DeliverySlot slot);




    @PUT ("/api/DeliverySlot/EnableSlot/{DeliverySlotID}/{Enabled}")
    Call<ResponseBody> enableSlot(@Path("DeliverySlotID")int slotID,
                               @Path("Enabled") boolean isEnabled);






    @DELETE ("/api/DeliverySlot/{SlotID}")
    Call<ResponseBody> deleteSlot(@Path("SlotID")int slotID);





    @GET ("/api/DeliverySlot")
    Call<DeliverySlotEndpoint> getItems(
            @Query("ShopID")Integer shopID,
            @Query("IsShopNull")boolean isShopNull,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")int offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData);






    @GET ("/api/DeliverySlot/GetAvailableSlots")
    Call<DeliverySlotEndpoint> getAvailableSlots(
            @Query("ShopID")Integer shopID,
            @Query("IsPickupSlot") boolean isPickupSlot,
            @Query("IsDeliverySlot") boolean isDeliverySlot,
            @Query("DeliveryDate") Date deliveryDate,
            @Query("SortBy") String sortBy,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );



}
