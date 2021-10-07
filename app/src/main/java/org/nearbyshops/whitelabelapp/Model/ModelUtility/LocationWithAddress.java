package org.nearbyshops.whitelabelapp.Model.ModelUtility;

import android.location.Location;

/**
 * Created by sumeet on 8/6/17.
 */
public class LocationWithAddress {

    // class for storing latitude and longitude


    private double latitude;
    private double longitude;
    private String locationName;
    private String locationAddress;




    public double distanceFrom(double lat,double lon)
    {
        Location pointOne = new Location("point_one");
        pointOne.setLatitude(lat);
        pointOne.setLongitude(lon);

        Location pointTwo = new Location("point_two");
        pointTwo.setLatitude(latitude);
        pointTwo.setLongitude(longitude);

        return pointOne.distanceTo(pointTwo);
    }





    public Location getLocation()
    {
        Location location = new Location("location");

        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }

    public LocationWithAddress(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationWithAddress(double latitude, double longitude, String locationName, String locationAddress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
    }

    public LocationWithAddress() {
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
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
}
