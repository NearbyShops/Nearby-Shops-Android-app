package org.nearbyshops.enduserappnew.Model.ModelRoles;


import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;

import java.sql.Timestamp;

/**
 * Created by sumeet on 29/5/16.
 */
public class User {

    // constants
    public static final int REGISTRATION_MODE_EMAIL = 1;
    public static final int REGISTRATION_MODE_PHONE = 2;




//    // role codes
//    public static final int ROLE_ADMIN_CODE = 1;
//    public static final int ROLE_STAFF_CODE = 2;
//    public static final int ROLE_SHOP_ADMIN_CODE = 3;
//    public static final int ROLE_SHOP_STAFF_CODE = 4;
//    public static final int ROLE_DELIVERY_GUY_CODE = 5;
//    public static final int ROLE_END_USER_CODE = 6;


    // role codes
    public static final int ROLE_ADMIN_CODE = 1;
    public static final int ROLE_STAFF_CODE = 2;
    public static final int ROLE_DELIVERY_GUY_CODE = 3;
    public static final int ROLE_SHOP_ADMIN_CODE = 4;
    public static final int ROLE_SHOP_STAFF_CODE = 5;
    public static final int ROLE_DELIVERY_GUY_SELF_CODE = 6;
    public static final int ROLE_END_USER_CODE = 7;





    // Table Name for User
    public static final String TABLE_NAME = "USER_TABLE";

    // Column names
    public static final String USER_ID = "USER_ID";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    public static final String PASSWORD_RESET_CODE = "PASSWORD_RESET_CODE";
    public static final String RESET_CODE_EXPIRES = "RESET_CODE_EXPIRES";

    public static final String E_MAIL = "E_MAIL";
//    public static final String IS_EMAIL_VERIFIED = "IS_EMAIL_VERIFIED";
    public static final String PHONE = "PHONE";
//    public static final String IS_PHONE_VERIFIED = "IS_PHONE_VERIFIED";
    public static final String NAME = "NAME";

    public static final String GENDER = "GENDER";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";
    public static final String ROLE = "ROLE";

    public static final String IS_ACCOUNT_PRIVATE = "IS_ACCOUNT_PRIVATE";
    public static final String ABOUT = "ABOUT";
    public static final String ENABLED = "ENABLED";
    public static final String GOOGLE_ID = "GOOGLE_ID";
    public static final String FIREBASE_ID = "FIREBASE_ID";

    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";
    public static final String TIMESTAMP_UPDATED = "TIMESTAMP_UPDATED";

    public static final String TOKEN = "TOKEN";
    public static final String TIMESTAMP_TOKEN_EXPIRES = "TIMESTAMP_TOKEN_EXPIRES";


    // current_due = total_service_charges - total_credits - total_paid
    public static final String CURRENT_DUES = "CURRENT_DUES";
    public static final String TOTAL_SERVICE_CHARGES = "TOTAL_SERVICE_CHARGES";
    public static final String TOTAL_CREDITS = "TOTAL_CREDITS";
    public static final String TOTAL_PAID = "TOTAL_PAID";
    public static final String EXTENDED_CREDIT_LIMIT = "EXTENDED_CREDIT_LIMIT";

    public static final String REFERRED_BY = "REFERRED_BY";
    public static final String IS_REFERRER_CREDITED = "IS_REFERRER_CREDITED";

    // verified accounts are an indication that identity of the user is verified by a staff member
    public static final String IS_VERIFIED = "IS_VERIFIED";


    // for communicating with the local server on the client : this can be used to send push notifications
    public static final String IP_ADDRESS = "IP_ADDRESS";
    public static final String PORT = "PORT";







    // Instance Variables
    private int userID;
    private String username;
    private String password;

    private String passwordResetCode;
    private Timestamp resetCodeExpires;

    private String email;
    private String phone;
    private String name;
    private int secretCode;

    private boolean gender;
    private String profileImagePath;
    private int role;

    private boolean isAccountPrivate;
    private String about;
    private boolean enabled;
    private String googleID;
    private String firebaseID;

    private Timestamp timestampCreated;
    private Timestamp timestampUpdated;

    private String token;
    private Timestamp timestampTokenExpires;

    private double taxAccountBalance;
    private double serviceAccountBalance;
    private double serviceRequestsBalance;

    //    private double totalServiceCharges;
//    private double totalCredited;
//    private double totalPaid;
    private double extendedCreditLimit;

    private int referredBy;
    private boolean isReferrerCredited;
    private boolean isVerified;

    private String ipAddress;
    private int port;



    private String rt_email_verification_code;
    private String rt_phone_verification_code;
    private String rt_phone_country_code;
    private int rt_registration_mode; // 1 for registration by email 2 for registration by phone
//    private StaffPermissions rt_staff_permissions;

    private User userProfileGlobal;

    private ShopStaffPermissions rt_shop_staff_permissions;
    private DeliveryGuyData rt_delivery_guy_data;




    private Market serviceConfigurationLocal;






    // utility functions



    public String getPhoneWithCountryCode()
    {
        return (rt_phone_country_code + phone);
    }








    // Getters and Setters


    public int getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(int secretCode) {
        this.secretCode = secretCode;
    }

    public ShopStaffPermissions getRt_shop_staff_permissions() {
        return rt_shop_staff_permissions;
    }

    public void setRt_shop_staff_permissions(ShopStaffPermissions rt_shop_staff_permissions) {
        this.rt_shop_staff_permissions = rt_shop_staff_permissions;
    }

    public DeliveryGuyData getRt_delivery_guy_data() {
        return rt_delivery_guy_data;
    }

    public void setRt_delivery_guy_data(DeliveryGuyData rt_delivery_guy_data) {
        this.rt_delivery_guy_data = rt_delivery_guy_data;
    }

    public String getRt_phone_country_code() {
        return rt_phone_country_code;
    }

    public void setRt_phone_country_code(String rt_phone_country_code) {
        this.rt_phone_country_code = rt_phone_country_code;
    }

    public User getUserProfileGlobal() {
        return userProfileGlobal;
    }

    public void setUserProfileGlobal(User userProfileGlobal) {
        this.userProfileGlobal = userProfileGlobal;
    }


    public Market getServiceConfigurationLocal() {
        return serviceConfigurationLocal;
    }

    public void setServiceConfigurationLocal(Market serviceConfigurationLocal) {
        this.serviceConfigurationLocal = serviceConfigurationLocal;
    }

    public double getServiceRequestsBalance() {
        return serviceRequestsBalance;
    }

    public void setServiceRequestsBalance(double serviceRequestsBalance) {
        this.serviceRequestsBalance = serviceRequestsBalance;
    }

    public double getTaxAccountBalance() {
        return taxAccountBalance;
    }

    public void setTaxAccountBalance(double taxAccountBalance) {
        this.taxAccountBalance = taxAccountBalance;
    }

    public double getServiceAccountBalance() {
        return serviceAccountBalance;
    }

    public void setServiceAccountBalance(double serviceAccountBalance) {
        this.serviceAccountBalance = serviceAccountBalance;
    }


    public Timestamp getResetCodeExpires() {
        return resetCodeExpires;
    }

    public void setResetCodeExpires(Timestamp resetCodeExpires) {
        this.resetCodeExpires = resetCodeExpires;
    }

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public double getExtendedCreditLimit() {
        return extendedCreditLimit;
    }

    public void setExtendedCreditLimit(double extendedCreditLimit) {
        this.extendedCreditLimit = extendedCreditLimit;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(int referredBy) {
        this.referredBy = referredBy;
    }

    public boolean isReferrerCredited() {
        return isReferrerCredited;
    }

    public void setReferrerCredited(boolean referrerCredited) {
        isReferrerCredited = referrerCredited;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }


    public int getRt_registration_mode() {
        return rt_registration_mode;
    }

    public void setRt_registration_mode(int rt_registration_mode) {
        this.rt_registration_mode = rt_registration_mode;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getRt_email_verification_code() {
        return rt_email_verification_code;
    }

    public void setRt_email_verification_code(String rt_email_verification_code) {
        this.rt_email_verification_code = rt_email_verification_code;
    }

    public String getRt_phone_verification_code() {
        return rt_phone_verification_code;
    }

    public void setRt_phone_verification_code(String rt_phone_verification_code) {
        this.rt_phone_verification_code = rt_phone_verification_code;
    }


    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public boolean isAccountPrivate() {
        return isAccountPrivate;
    }

    public void setAccountPrivate(boolean accountPrivate) {
        isAccountPrivate = accountPrivate;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public Timestamp getTimestampUpdated() {
        return timestampUpdated;
    }

    public void setTimestampUpdated(Timestamp timestampUpdated) {
        this.timestampUpdated = timestampUpdated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Timestamp getTimestampTokenExpires() {
        return timestampTokenExpires;
    }

    public void setTimestampTokenExpires(Timestamp timestampTokenExpires) {
        this.timestampTokenExpires = timestampTokenExpires;
    }


}
