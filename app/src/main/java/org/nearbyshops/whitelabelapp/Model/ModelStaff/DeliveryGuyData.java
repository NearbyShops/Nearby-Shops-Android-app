package org.nearbyshops.whitelabelapp.Model.ModelStaff;


import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;

public class DeliveryGuyData {



    public static final int STATUS_OFFLINE = 1; // Offline
    public static final int STATUS_ONLINE = 2;  // Enabled

    // Table Name for User
    public static final String TABLE_NAME = "DELIVERY_GUY_DATA";

    // Column names
    public static final String DATA_ID = "DATA_ID";
    public static final String DELIVERY_USER_ID = "DELIVERY_USER_ID";
    public static final String LAT_CURRENT = "LAT_CURRENT";
    public static final String LON_CURRENT = "LON_CURRENT";


    public static final String SHOP_ID = "SHOP_ID";
    public static final String ONLINE_STATUS = "ONLINE_STATUS";
    public static final String DELIVERY_ACCOUNT_BALANCE = "CURRENT_BALANCE";


    public static final String LAT_CENTER_DELIVERY_AREA = "LAT_CENTER_DELIVERY_AREA";
    public static final String LON_CENTER_DELIVERY_AREA = "LON_CENTER_DELIVERY_AREA";
    public static final String RADIUS_DELIVERY_AREA = "RADIUS_DELIVERY_AREA";




    // instance variables
    private int dataID;
    private int userID;
    private double latCurrent;
    private double lonCurrent;
    private int shopID;
    private int onlineStatus;

    private double rt_distance;
    private User deliveryGuyProfile;



    // getter and setters


    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public int getDataID() {
        return dataID;
    }

    public void setDataID(int dataID) {
        this.dataID = dataID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getLatCurrent() {
        return latCurrent;
    }

    public void setLatCurrent(double latCurrent) {
        this.latCurrent = latCurrent;
    }

    public double getLonCurrent() {
        return lonCurrent;
    }

    public void setLonCurrent(double lonCurrent) {
        this.lonCurrent = lonCurrent;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public double getRt_distance() {
        return rt_distance;
    }

    public void setRt_distance(double rt_distance) {
        this.rt_distance = rt_distance;
    }

    public User getDeliveryGuyProfile() {
        return deliveryGuyProfile;
    }

    public void setDeliveryGuyProfile(User deliveryGuyProfile) {
        this.deliveryGuyProfile = deliveryGuyProfile;
    }
}
