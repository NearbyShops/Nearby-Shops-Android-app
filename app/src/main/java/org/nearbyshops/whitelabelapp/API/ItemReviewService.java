package org.nearbyshops.whitelabelapp.API;

import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ItemReviewEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelReviewItem.ItemReview;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by sumeet on 2/4/16.
 */

public interface ItemReviewService {

    @GET("/api/v1/ItemReview")
    Call<ItemReviewEndPoint> getReviews(
            @Query("ItemID") Integer itemID,
            @Query("EndUserID") Integer endUserID,
            @Query("GetEndUser") Boolean getEndUser,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @GET("/api/v1/ItemReview/{id}")
    Call<ItemReview> getItemReview(@Path("id") int itemReviewID);

    @POST("/api/v1/ItemReview")
    Call<ItemReview> insertItemReview(@Body ItemReview book);

    @PUT("/api/v1/ItemReview/{id}")
    Call<ResponseBody> updateItemReview(@Body ItemReview itemReview, @Path("id") int id);

    @PUT("/api/v1/ItemReview/")
    Call<ResponseBody> updateItemReviewBulk(@Body List<ItemReview> itemReviewList);

    @DELETE("/api/v1/ItemReview/{id}")
    Call<ResponseBody> deleteItemReview(@Path("id") int id);

}
