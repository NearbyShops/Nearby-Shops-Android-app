package org.nearbyshops.whitelabelapp.Model.ModelImages;

import org.nearbyshops.whitelabelapp.Model.Item;

import java.io.InputStream;
import java.sql.Timestamp;

/**
 * Created by sumeet on 28/2/17.
 */
public class ItemImage {

    // Table Name
    public static final String TABLE_NAME = "ITEM_IMAGES";

    // column names
    public static final String IMAGE_ID = "IMAGE_ID";
    public static final String ITEM_ID = "ITEM_ID";
    public static final String IMAGE_FILENAME = "IMAGE_FILENAME";
    public static final String GIDB_IMAGE_ID = "GIDB_IMAGE_ID";
    public static final String GIDB_SERVICE_URL = "GIDB_SERVICE_URL";

    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";
    public static final String TIMESTAMP_UPDATED = "TIMESTAMP_UPDATED";

    public static final String CAPTION_TITLE = "CAPTION_TITLE";
    public static final String CAPTION = "CAPTION";
    public static final String IMAGE_COPYRIGHTS = "IMAGE_COPYRIGHTS";
    public static final String IMAGE_ORDER = "IMAGE_ORDER";



    // create table statement
    public static final String createTableItemImagesPostgres = "CREATE TABLE IF NOT EXISTS "

            + ItemImage.TABLE_NAME + "("

            + " " + ItemImage.IMAGE_ID + " SERIAL PRIMARY KEY,"
            + " " + ItemImage.ITEM_ID + " int,"
            + " " + ItemImage.IMAGE_FILENAME + " text,"
            + " " + ItemImage.GIDB_IMAGE_ID + " int,"
            + " " + ItemImage.GIDB_SERVICE_URL + " text,"

            + " " + ItemImage.TIMESTAMP_CREATED + " timestamp with time zone NOT NULL DEFAULT now(),"
            + " " + ItemImage.TIMESTAMP_UPDATED + " timestamp with time zone,"

            + " " + ItemImage.CAPTION_TITLE + " text,"
            + " " + ItemImage.CAPTION + " text,"
            + " " + ItemImage.IMAGE_COPYRIGHTS + " text,"
            + " " + ItemImage.IMAGE_ORDER + " int,"

            + " FOREIGN KEY(" + ItemImage.ITEM_ID +") REFERENCES " + Item.TABLE_NAME + "(" + Item.ITEM_ID + ") ON DELETE CASCADE "
            + ")";






    // instance variables

    private int imageID;
    private int itemID;
    private String imageFilename;
    private int gidbImageID;
    private String gidbServiceURL;

    private Timestamp timestampCreated;
    private Timestamp timestampUpdated;

    private String captionTitle;
    private String caption;
    private String imageCopyrights;
    private int imageOrder;


    private InputStream inputStream;

    private byte[] picBytes;





    public byte[] getPicBytes() {
        return picBytes;
    }

    public void setPicBytes(byte[] picBytes) {
        this.picBytes = picBytes;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Timestamp getTimestampUpdated() {
        return timestampUpdated;
    }

    public void setTimestampUpdated(Timestamp timestampUpdated) {
        this.timestampUpdated = timestampUpdated;
    }

    public int getImageOrder() {
        return imageOrder;
    }

    public void setImageOrder(int imageOrder) {
        this.imageOrder = imageOrder;
    }

    public String getImageCopyrights() {
        return imageCopyrights;
    }

    public void setImageCopyrights(String imageCopyrights) {
        this.imageCopyrights = imageCopyrights;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public int getGidbImageID() {
        return gidbImageID;
    }

    public void setGidbImageID(int gidbImageID) {
        this.gidbImageID = gidbImageID;
    }

    public String getGidbServiceURL() {
        return gidbServiceURL;
    }

    public void setGidbServiceURL(String gidbServiceURL) {
        this.gidbServiceURL = gidbServiceURL;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
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
}
