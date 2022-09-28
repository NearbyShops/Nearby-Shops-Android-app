package org.nearbyshops.whitelabelapp.Model.ModelImages;

import java.sql.Timestamp;

/**
 * Created by sumeet on 28/2/17.
 */
public class ShopImage {


    public static final String IMAGE_ORDER = "IMAGE_ORDER";


    // instance variables

    private int shopImageID;
    private int shopID;
    private String imageFilename;

    private Timestamp timestampCreated;
    private Timestamp timestampUpdated;

    private String captionTitle;
    private String caption;
    private String copyrights;
    private int imageOrder;

    // getter and setters


    public int getShopImageID() {
        return shopImageID;
    }

    public void setShopImageID(int shopImageID) {
        this.shopImageID = shopImageID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
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

    public String getCaptionTitle() {
        return captionTitle;
    }

    public void setCaptionTitle(String captionTitle) {
        this.captionTitle = captionTitle;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public int getImageOrder() {
        return imageOrder;
    }

    public void setImageOrder(int imageOrder) {
        this.imageOrder = imageOrder;
    }
}
