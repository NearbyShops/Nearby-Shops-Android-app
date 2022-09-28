package org.nearbyshops.whitelabelapp.API;


import org.nearbyshops.whitelabelapp.Model.Image;
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.Model.BannerImage;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 3/4/16.
 */
public interface BannerImageService
{


    @POST("/api/v1/BannerImages")
    Call<BannerImage> createBannerImage(@Header("Authorization") String headers,
                                        @Body BannerImage bannerImage);




    @PUT ("/api/v1/BannerImages/{ImageID}")
    Call<ResponseBody> updateBannerImage(@Header("Authorization") String headers,
                                        @Body BannerImage bannerImage,
                                        @Path("ImageID")int imageID);



    @DELETE ("/api/v1/BannerImages/{BannerID}")
    Call<ResponseBody> deleteBannerImage(@Header("Authorization") String headers,
                                        @Path("BannerID")int bannerID);




    @GET ("/api/v1/BannerImages/GetBannerImageDetails")
    Call<BannerImage> getBannerImageDetails(
            @Query("BannerImageID")int bannerImageID
    );



    /*Image Upload Methods*/


    @Multipart
    @POST("/api/v1/BannerImages/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Part MultipartBody.Part img
    );




    @DELETE("/api/v1/BannerImages/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);




}
