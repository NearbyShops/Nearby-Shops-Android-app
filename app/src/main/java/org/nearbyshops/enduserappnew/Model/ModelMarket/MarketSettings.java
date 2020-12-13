package org.nearbyshops.enduserappnew.Model.ModelMarket;

import java.sql.Timestamp;

/**
 * Created by sumeet on 19/6/16.
 */
public class MarketSettings {


    // Table Name
    public static final String TABLE_NAME = "MARKET_SETTINGS";

    // column Names
    public static final String SETTING_ID = "SETTING_ID";

//    public static final String ADMIN_EMAIL = "ADMIN_EMAIL";
//    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";

    public static final String SMTP_SERVER_URL = "SMTP_SERVER_URL";
    public static final String SMTP_PORT = "SMTP_PORT";
    public static final String SMTP_USERNAME = "SMTP_USERNAME";
    public static final String SMTP_PASSWORD = "SMTP_PASSWORD";

    public static final String EMAIL_SENDER_NAME = "EMAIL_SENDER_NAME";
    public static final String EMAIL_ADDRESS_FOR_SENDER = "EMAIL_ADDRESS_FOR_SENDER";

    public static final String COD_ENABLED = "COD_ENABLED";
    public static final String POD_ENABLED = "POD_ENABLED";
    public static final String RAZOR_PAY_ENABLED = "RAZOR_PAY_ENABLED";
    public static final String RAZOR_PAY_KEY_ID = "RAZOR_PAY_KEY_ID";
    public static final String RAZOR_PAY_KEY_SECRET = "RAZOR_PAY_KEY_SECRET";

    public static final String MSG_91_API_KEY = "MSG_91_API_KEY";
    public static final String SENDER_ID_FOR_SMS = "SENDER_ID_FOR_SMS";
    public static final String SERVICE_NAME_FOR_SMS = "SERVICE_NAME_FOR_SMS";

    public static final String DEFAULT_COUNTRY_CODE = "DEFAULT_COUNTRY_CODE";
    public static final String LOGIN_USING_OTP_ENABLED = "LOGIN_USING_OTP_ENABLED";

    public static final String MARKET_FEE_PICKUP_FROM_SHOP = "MARKET_FEE_PICKUP_FROM_SHOP";
    public static final String MARKET_FEE_HOME_DELIVERY = "MARKET_FEE_HOME_DELIVERY";
    public static final String ADD_MARKET_FEE_TO_BILL = "ADD_MARKET_FEE_TO_BILL";
    public static final String USE_STANDARD_DELIVERY_FEE = "USE_STANDARD_DELIVERY_FEE";
    public static final String MARKET_DELIVERY_FEE_PER_ORDER = "MARKET_DELIVERY_FEE_PER_ORDER";

    public static final String BOOTSTRAP_MODE_ENABLED = "BOOTSTRAP_MODE_ENABLED";
    public static final String DEMO_MODE_ENABLED = "DEMO_MODE_ENABLED";

    public static final String MIN_ACCOUNT_BALANCE_FOR_SHOP_OWNER = "MIN_ACCOUNT_BALANCE_FOR_SHOP_OWNER";

    public static final String JOINING_CREDIT_FOR_SHOP_OWNER = "JOINING_CREDIT_FOR_SHOP_OWNER";
    public static final String JOINING_CREDIT_FOR_END_USER = "JOINING_CREDIT_FOR_END_USER";

    public static final String REFERRAL_CREDIT_FOR_SHOP_OWNER_REGISTRATION = "REFERRAL_CREDIT_FOR_SHOP_OWNER_REGISTRATION";
    public static final String REFERRAL_CREDIT_FOR_END_USER_REGISTRATION = "REFERRAL_CREDIT_FOR_END_USER_REGISTRATION";


    public static final String CREATED = "CREATED";
    public static final String UPDATED = "UPDATED";





    int settingID;

//    String smtpServerURL;
//    String smtpPort;
//    String smtpUsername;
//    String smtpPassword;

    String emailSenderName;
    String emailAddressForSender;

    boolean codEnabled;
    boolean podEnabled;
    boolean razorPayEnabled;
//    String razorPayKeyID;
//    String razorPayKeySecret;

//    String msg91APIKey;
    String senderIDForSMS;
    String serviceNameForSMS;

    int defaultCountryCode;
    boolean loginUsingOTPEnabled;

    float marketFeePickupFromShop;
    float marketFeeHomeDelivery;
    boolean addMarketFeeToBill;
    boolean useStandardDeliveryFee;
    float marketDeliveryFeePerOrder;

    boolean bootstrapModeEnabled;
    boolean demoModeEnabled;

    float minAccountBalanceForShopOwner;

    float joiningCreditForEndUser;
    float joiningCreditForShopOwner;

    float referralCreditForEndUserRegistration;
    float referralCreditForShopOwnerRegistration;

    private Timestamp created;
    private Timestamp updated;




    // getter and setter methods
    public float getJoiningCreditForEndUser() {
        return joiningCreditForEndUser;
    }

    public void setJoiningCreditForEndUser(float joiningCreditForEndUser) {
        this.joiningCreditForEndUser = joiningCreditForEndUser;
    }

    public float getJoiningCreditForShopOwner() {
        return joiningCreditForShopOwner;
    }

    public void setJoiningCreditForShopOwner(float joiningCreditForShopOwner) {
        this.joiningCreditForShopOwner = joiningCreditForShopOwner;
    }

    public float getReferralCreditForEndUserRegistration() {
        return referralCreditForEndUserRegistration;
    }

    public void setReferralCreditForEndUserRegistration(float referralCreditForEndUserRegistration) {
        this.referralCreditForEndUserRegistration = referralCreditForEndUserRegistration;
    }

    public float getReferralCreditForShopOwnerRegistration() {
        return referralCreditForShopOwnerRegistration;
    }

    public void setReferralCreditForShopOwnerRegistration(float referralCreditForShopOwnerRegistration) {
        this.referralCreditForShopOwnerRegistration = referralCreditForShopOwnerRegistration;
    }

    public int getSettingID() {
        return settingID;
    }

    public void setSettingID(int settingID) {
        this.settingID = settingID;
    }


    public String getEmailSenderName() {
        return emailSenderName;
    }

    public void setEmailSenderName(String emailSenderName) {
        this.emailSenderName = emailSenderName;
    }

    public String getEmailAddressForSender() {
        return emailAddressForSender;
    }

    public void setEmailAddressForSender(String emailAddressForSender) {
        this.emailAddressForSender = emailAddressForSender;
    }

    public boolean isCodEnabled() {
        return codEnabled;
    }

    public void setCodEnabled(boolean codEnabled) {
        this.codEnabled = codEnabled;
    }

    public boolean isPodEnabled() {
        return podEnabled;
    }

    public void setPodEnabled(boolean podEnabled) {
        this.podEnabled = podEnabled;
    }

    public boolean isRazorPayEnabled() {
        return razorPayEnabled;
    }

    public void setRazorPayEnabled(boolean razorPayEnabled) {
        this.razorPayEnabled = razorPayEnabled;
    }

    public String getSenderIDForSMS() {
        return senderIDForSMS;
    }

    public void setSenderIDForSMS(String senderIDForSMS) {
        this.senderIDForSMS = senderIDForSMS;
    }

    public String getServiceNameForSMS() {
        return serviceNameForSMS;
    }

    public void setServiceNameForSMS(String serviceNameForSMS) {
        this.serviceNameForSMS = serviceNameForSMS;
    }

    public int getDefaultCountryCode() {
        return defaultCountryCode;
    }

    public void setDefaultCountryCode(int defaultCountryCode) {
        this.defaultCountryCode = defaultCountryCode;
    }

    public boolean isLoginUsingOTPEnabled() {
        return loginUsingOTPEnabled;
    }

    public void setLoginUsingOTPEnabled(boolean loginUsingOTPEnabled) {
        this.loginUsingOTPEnabled = loginUsingOTPEnabled;
    }

    public float getMarketFeePickupFromShop() {
        return marketFeePickupFromShop;
    }

    public void setMarketFeePickupFromShop(float marketFeePickupFromShop) {
        this.marketFeePickupFromShop = marketFeePickupFromShop;
    }

    public float getMarketFeeHomeDelivery() {
        return marketFeeHomeDelivery;
    }

    public void setMarketFeeHomeDelivery(float marketFeeHomeDelivery) {
        this.marketFeeHomeDelivery = marketFeeHomeDelivery;
    }

    public boolean isAddMarketFeeToBill() {
        return addMarketFeeToBill;
    }

    public void setAddMarketFeeToBill(boolean addMarketFeeToBill) {
        this.addMarketFeeToBill = addMarketFeeToBill;
    }

    public boolean isUseStandardDeliveryFee() {
        return useStandardDeliveryFee;
    }

    public void setUseStandardDeliveryFee(boolean useStandardDeliveryFee) {
        this.useStandardDeliveryFee = useStandardDeliveryFee;
    }


    public float getMarketDeliveryFeePerOrder() {
        return marketDeliveryFeePerOrder;
    }

    public void setMarketDeliveryFeePerOrder(float marketDeliveryFeePerOrder) {
        this.marketDeliveryFeePerOrder = marketDeliveryFeePerOrder;
    }

    public boolean isBootstrapModeEnabled() {
        return bootstrapModeEnabled;
    }

    public void setBootstrapModeEnabled(boolean bootstrapModeEnabled) {
        this.bootstrapModeEnabled = bootstrapModeEnabled;
    }

    public boolean isDemoModeEnabled() {
        return demoModeEnabled;
    }

    public void setDemoModeEnabled(boolean demoModeEnabled) {
        this.demoModeEnabled = demoModeEnabled;
    }

    public float getMinAccountBalanceForShopOwner() {
        return minAccountBalanceForShopOwner;
    }

    public void setMinAccountBalanceForShopOwner(float minAccountBalanceForShopOwner) {
        this.minAccountBalanceForShopOwner = minAccountBalanceForShopOwner;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}
