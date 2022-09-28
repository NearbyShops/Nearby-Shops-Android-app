package org.nearbyshops.whitelabelapp.API;




import org.nearbyshops.whitelabelapp.Model.ModelBilling.TransactionEndpoint;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 13/8/17.
 */

public interface TransactionService {


    @GET("/api/v1/Transaction")
    Call<TransactionEndpoint> getTransactions(
            @Header("Authorization") String headers,
            @Query("IsCredit") Boolean isCredit,
            @Query("TransactionType") Integer transactionType,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("GetRowCount") boolean getRowCount,
            @Query("MetadataOnly") boolean getOnlyMetaData);





    @GET ("/api/v1/Transaction/GetUserTransactions/{UserID}")
    Call<TransactionEndpoint> getUserTransactions(
            @Header("Authorization") String headers,
            @Path("UserID") int userID,
            @Query("IsCredit") Boolean isCredit,
            @Query("TransactionType") Integer transactionType,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")Integer offset,
            @Query("GetRowCount")boolean getRowCount,
            @Query("MetadataOnly")boolean getOnlyMetaData
    );



}
