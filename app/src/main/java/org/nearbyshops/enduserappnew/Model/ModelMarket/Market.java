package org.nearbyshops.enduserappnew.Model.ModelMarket;

import java.sql.Timestamp;

/**
 * Created by sumeet on 19/6/16.
 */
public class Market {


    public static final String CREATED = " CREATED ";


    // Instance Variables
    private int serviceID;

//    private String imagePath;
    private String logoImagePath;
    private String backdropImagePath;

    private String serviceName;
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

    private int serviceType;
    private int serviceLevel;

    private double latCenter;
    private double lonCenter;

    private double serviceRange;

    private boolean isOfficial;
    private boolean isVerified;
    private String serviceURL;

    private Timestamp created;
    private Timestamp updated;



    // real time variables : the values of these variables are generated in real time.
    private double rt_distance;

    private float rt_rating_avg;
    private float rt_rating_count;



    // real time variables : the values of these variables are generated in real time.
    private String rt_styleURL;

    private boolean rt_login_using_otp_enabled;
    private String rt_market_id_for_fcm;








    // getter and setters




    public String getRt_styleURL() {
        return rt_styleURL;
    }

    public void setRt_styleURL(String rt_styleURL) {
        this.rt_styleURL = rt_styleURL;
    }

    public boolean isRt_login_using_otp_enabled() {
        return rt_login_using_otp_enabled;
    }

    public void setRt_login_using_otp_enabled(boolean rt_login_using_otp_enabled) {
        this.rt_login_using_otp_enabled = rt_login_using_otp_enabled;
    }

    public String getRt_market_id_for_fcm() {
        return rt_market_id_for_fcm;
    }

    public void setRt_market_id_for_fcm(String rt_market_id_for_fcm) {
        this.rt_market_id_for_fcm = rt_market_id_for_fcm;
    }

    public float getRt_rating_avg() {
        return rt_rating_avg;
    }

    public void setRt_rating_avg(float rt_rating_avg) {
        this.rt_rating_avg = rt_rating_avg;
    }

    public float getRt_rating_count() {
        return rt_rating_count;
    }

    public void setRt_rating_count(float rt_rating_count) {
        this.rt_rating_count = rt_rating_count;
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



    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public String getISOCurrencyCode() {
        return ISOCurrencyCode;
    }

    public void setISOCurrencyCode(String ISOCurrencyCode) {
        this.ISOCurrencyCode = ISOCurrencyCode;
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

    public Double getRt_distance() {
        return rt_distance;
    }

    public void setRt_distance(Double rt_distance) {
        this.rt_distance = rt_distance;
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

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHelplineNumber() {
        return helplineNumber;
    }

    public void setHelplineNumber(String helplineNumber) {
        this.helplineNumber = helplineNumber;
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

    public Long getPincode() {
        return pincode;
    }

    public void setPincode(Long pincode) {
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


    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(Integer serviceLevel) {
        this.serviceLevel = serviceLevel;
    }


    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public void setServiceLevel(int serviceLevel) {
        this.serviceLevel = serviceLevel;
    }


    public double getLatCenter() {
        return latCenter;
    }

    public void setLatCenter(double latCenter) {
        this.latCenter = latCenter;
    }

    public double getLonCenter() {
        return lonCenter;
    }

    public void setLonCenter(double lonCenter) {
        this.lonCenter = lonCenter;
    }

    public double getServiceRange() {
        return serviceRange;
    }

    public void setServiceRange(double serviceRange) {
        this.serviceRange = serviceRange;
    }


    public Boolean getOfficial() {
        return isOfficial;
    }

    public void setOfficial(Boolean official) {
        isOfficial = official;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public void setRt_distance(double rt_distance) {
        this.rt_distance = rt_distance;
    }
}
