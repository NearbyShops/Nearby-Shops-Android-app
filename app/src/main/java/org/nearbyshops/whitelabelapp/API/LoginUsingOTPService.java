package org.nearbyshops.whitelabelapp.API;


import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 3/4/17.
 */

public interface LoginUsingOTPService {



    @GET ("/api/v1/User/LoginUsingOTP/LoginUsingPhoneOTP")
    Call<User> getProfileWithLogin(@Header("Authorization") String headers);



    @GET ("/api/v1/User/LoginUsingOTP/VerifyCredentialsUsingOTP")
    Call<User> verifyCredentialsUsingOTP(
            @Header("Authorization")String headerParam,
            @Query("RegistrationMode")int registrationMode // 1 for email and 2 for phone
    );




    @GET ("/api/v1/User/LoginUsingOTP/LoginUsingGlobalCredentials")
    Call<User> loginWithGlobalCredentials(
            @Header("Authorization") String headerParam,
            @Query("ServiceURLSDS") String serviceURLForSDS,
            @Query("MarketID") int marketID

    );






    @GET ("/api/v1/User/LoginUsingOTP/LoginUsingGlobalCredentials")
    Call<User> loginWithGlobalCredentials(
            @Header("Authorization") String headerParam,
            @Query("ServiceURLSDS") String serviceURLForSDS,
            @Query("MarketID") int marketID,
            @Query("IsPasswordAnOTP")boolean isPasswordAnOTP,
            @Query("VerifyUsingPassword")boolean verifyUsingPassword, // used when user is signing in using password
            @Query("RegistrationMode")int registrationMode, // 1 for email and 2 for phone
            @Query("GetServiceConfiguration") boolean getServiceConfig,
            @Query("GetUserProfileGlobal") boolean getUserProfileGlobal
    );



    @GET ("/api/v1/User/LoginUsingOTP/LoginUsingGlobalCredentials")
    Call<User> loginWithGlobalCredentials(
            @Header("Authorization") String headerParam,
            @Query("ServiceURLSDS") String serviceURLForSDS,
            @Query("MarketID") int marketID,
            @Query("GetServiceConfiguration") boolean getServiceConfig,
            @Query("GetUserProfileGlobal") boolean getUserProfileGlobal
    );


}
