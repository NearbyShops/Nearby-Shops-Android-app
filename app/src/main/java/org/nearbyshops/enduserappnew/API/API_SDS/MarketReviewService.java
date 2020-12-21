package org.nearbyshops.enduserappnew.API.API_SDS;

import org.nearbyshops.enduserappnew.Model.ModelEndPoints.MarketReviewEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelReviewMarket.MarketReview;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by sumeet on 2/4/16.
 */

public interface MarketReviewService {


    @GET("/api/v1/MarketReview")
    Call<MarketReviewEndPoint> getReviews(
            @Query("ItemID") Integer itemID,
            @Query("EndUserID") Integer endUserID,
            @Query("GetEndUser") Boolean getEndUser,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );


    @GET("/api/v1/MarketReview/{id}")
    Call<MarketReview> getItemReview(@Path("id") int itemReviewID);


    @POST("/api/v1/MarketReview")
    Call<MarketReview> insertItemReview(@Body MarketReview book);

    @PUT("/api/v1/MarketReview/{id}")
    Call<ResponseBody> updateItemReview(@Body MarketReview itemReview, @Path("id") int id);

    @PUT("/api/v1/MarketReview/")
    Call<ResponseBody> updateItemReviewBulk(@Body List<MarketReview> itemReviewList);

    @DELETE("/api/v1/MarketReview/{id}")
    Call<ResponseBody> deleteItemReview(@Path("id") int id);

}
