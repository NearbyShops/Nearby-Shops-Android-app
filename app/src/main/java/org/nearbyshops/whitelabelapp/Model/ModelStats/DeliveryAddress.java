package org.nearbyshops.whitelabelapp.Model.ModelStats;



/**
 * Created by sumeet on 10/6/16.
 */
public class DeliveryAddress{


    // Table Name
    public static final String TABLE_NAME = "DELIVERY_ADDRESS";

    // column Names
    public static final String ID = "ID";
    public static final String NAME = "DISTRIBUTOR_NAME";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";

    public static final String DELIVERY_ADDRESS = "DELIVERY_ADDRESS";
    public static final String CITY = "CITY";

    public static final String PINCODE = "PINCODE";
    public static final String LANDMARK = "LANDMARK";
    public static final String END_USER_ID = "END_USER_ID"; // Primary Key

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";






    // instance variables
    private int id;
    private String name;
    private long phoneNumber;
    private String deliveryAddress;
    private String city;
    private long pincode;
    private String landmark;
    private double latitude;
    private double longitude;
    private int endUserID;


    private Double rt_distance;




    // getter and setters


    public Double getRt_distance() {
        return rt_distance;
    }

    public void setRt_distance(Double rt_distance) {
        this.rt_distance = rt_distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(int endUserID) {
        this.endUserID = endUserID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
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
}
