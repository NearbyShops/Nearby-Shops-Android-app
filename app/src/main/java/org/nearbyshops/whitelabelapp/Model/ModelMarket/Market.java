package org.nearbyshops.whitelabelapp.Model.ModelMarket;

import java.sql.Timestamp;

/**
 * Created by sumeet on 19/6/16.
 */
public class Market {


    // Market Types
    public static final int MARKET_TYPE_NONPROFIT = 1;
    public static final int MARKET_TYPE_COOPERATIVE = 2;
    public static final int MARKET_TYPE_GOVERNMENT = 3;
    public static final int MARKET_TYPE_COMMERCIAL = 4;

    public static final int REGISTRATION_STATUS_NEW = 1; // default registration status for new market
    public static final int REGISTRATION_STATUS_DASHBOARD_OPEN = 2;
    public static final int REGISTRATION_STATUS_ENABLED = 3;
    public static final int REGISTRATION_STATUS_DISABLED = 4;


    // Table Name
    public static final String TABLE_NAME = "MARKET";

    // column Names
    public static final String MARKET_ID = "MARKET_ID";

    public static final String TIME_CREATED = "TIME_CREATED";

    public static final String LOGO_IMAGE_PATH = "LOGO_IMAGE_PATH";
    public static final String BACKDROP_IMAGE_PATH = "BACKDROP_IMAGE_PATH";

    public static final String MARKET_NAME = "MARKET_NAME";
    public static final String HELPLINE_NUMBER = "HELPLINE_NUMBER";

    public static final String DESCRIPTION_SHORT = "DESCRIPTION_SHORT";
    public static final String DESCRIPTION_LONG = "DESCRIPTION_LONG";

    public static final String ADDRESS = "MARKET_ADDRESS";
    public static final String CITY = "MARKET_CITY";
    public static final String PINCODE = "MARKET_PINCODE";
    public static final String LANDMARK = "MARKET_LANDMARK";
    public static final String STATE = "MARKET_STATE";
    public static final String COUNTRY = "MARKET_COUNTRY";

    public static final String ISO_COUNTRY_CODE = "ISO_COUNTRY_CODE";
    public static final String ISO_LANGUAGE_CODE = "ISO_LANGUAGE_CODE";
    public static final String ISO_CURRENCY_CODE = "ISO_CURRENCY_CODE";

    public static final String MARKET_TYPE = "MARKET_TYPE";

    public static final String IS_DELIVERING = "IS_OPEN";
    public static final String ACCOUNT_BALANCE = "ACCOUNT_BALANCE";







    // Instance Variables
    private int marketID;

    private String logoImagePath;
    private String backdropImagePath;

    private String marketName;
    private String helplineNumber;

    private String descriptionShort;
    private String descriptionLong;

    private String address;
    private String city;
    private long pincode;
    private String landmark;
    private String state;
    private String country;

    private String ISOCountryCode;
    private String ISOLanguageCode;
    private String ISOCurrencyCode;

    private int marketType;

    private Timestamp created;

    private boolean isOpen;
    private double accountBalance;
    private boolean itemUpdatePermitted;


    // real time variables : the values of these variables are generated in real time.
    private Double rt_distance;




    //    Getter and Setters
    public int getMarketID() {
        return marketID;
    }

    public void setMarketID(int marketID) {
        this.marketID = marketID;
    }

    public String getLogoImagePath() {
        return logoImagePath;
    }

    public void setLogoImagePath(String logoImagePath) {
        this.logoImagePath = logoImagePath;
    }

    public String getBackdropImagePath() {
        return backdropImagePath;
    }

    public void setBackdropImagePath(String backdropImagePath) {
        this.backdropImagePath = backdropImagePath;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getHelplineNumber() {
        return helplineNumber;
    }

    public void setHelplineNumber(String helplineNumber) {
        this.helplineNumber = helplineNumber;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getISOCountryCode() {
        return ISOCountryCode;
    }

    public void setISOCountryCode(String ISOCountryCode) {
        this.ISOCountryCode = ISOCountryCode;
    }

    public String getISOLanguageCode() {
        return ISOLanguageCode;
    }

    public void setISOLanguageCode(String ISOLanguageCode) {
        this.ISOLanguageCode = ISOLanguageCode;
    }

    public String getISOCurrencyCode() {
        return ISOCurrencyCode;
    }

    public void setISOCurrencyCode(String ISOCurrencyCode) {
        this.ISOCurrencyCode = ISOCurrencyCode;
    }

    public int getMarketType() {
        return marketType;
    }

    public void setMarketType(int marketType) {
        this.marketType = marketType;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public boolean isItemUpdatePermitted() {
        return itemUpdatePermitted;
    }

    public void setItemUpdatePermitted(boolean itemUpdatePermitted) {
        this.itemUpdatePermitted = itemUpdatePermitted;
    }

    public Double getRt_distance() {
        return rt_distance;
    }

    public void setRt_distance(Double rt_distance) {
        this.rt_distance = rt_distance;
    }
}
