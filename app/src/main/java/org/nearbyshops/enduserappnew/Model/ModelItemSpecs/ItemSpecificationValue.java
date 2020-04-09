package org.nearbyshops.enduserappnew.Model.ModelItemSpecs;


import java.sql.Timestamp;

/**
 * Created by sumeet on 2/3/17.
 */
public class ItemSpecificationValue {


    // Table Name
    public static final String TABLE_NAME = "ITEM_SPECIFICATION_VALUE";

    // column names
    public static final String ID = "ID";
    public static final String ITEM_SPECIFICATION_NAME_ID = "ITEM_SPECIFICATION_NAME_ID";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String IMAGE_FILENAME = "IMAGE_FILENAME";

    public static final String GIDB_ID = "GIDB_ID";
    public static final String GIDB_SERVICE_URL = "GIDB_SERVICE_URL";

    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";
    public static final String TIMESTAMP_UPDATED = "TIMESTAMP_UPDATED";



    // create table statement
    public static final String createTableItemSpecificationValuePostgres = "CREATE TABLE IF NOT EXISTS "
            + ItemSpecificationValue.TABLE_NAME + "("

            + " " + ItemSpecificationValue.ID + " SERIAL PRIMARY KEY,"
            + " " + ItemSpecificationValue.ITEM_SPECIFICATION_NAME_ID + " int,"
            + " " + ItemSpecificationValue.TITLE + " text,"
            + " " + ItemSpecificationValue.DESCRIPTION + " text,"
            + " " + ItemSpecificationValue.IMAGE_FILENAME + " text,"

            + " " + ItemSpecificationValue.GIDB_ID + " int,"
            + " " + ItemSpecificationValue.GIDB_SERVICE_URL + " text,"

            + " " + ItemSpecificationValue.TIMESTAMP_CREATED + " timestamp with time zone NOT NULL DEFAULT now(),"
            + " " + ItemSpecificationValue.TIMESTAMP_UPDATED + " timestamp with time zone ,"

            + " FOREIGN KEY(" + ItemSpecificationValue.ITEM_SPECIFICATION_NAME_ID +") REFERENCES " + ItemSpecificationName.TABLE_NAME + "(" + ItemSpecificationName.ID + ")"
            + ")";



    // instance variables


    private int id;
    private int itemSpecNameID;
    private String title;
    private String description;
    private String imageFilename;

    private int gidbID;
    private String gidbServiceURL;

    private Timestamp timestampCreated;
    private Timestamp timestampUpdated;

    private int rt_item_count;




    // getter and setters


    public Timestamp getTimestampUpdated() {
        return timestampUpdated;
    }

    public void setTimestampUpdated(Timestamp timestampUpdated) {
        this.timestampUpdated = timestampUpdated;
    }

    public int getRt_item_count() {
        return rt_item_count;
    }

    public void setRt_item_count(int rt_item_count) {
        this.rt_item_count = rt_item_count;
    }

    public int getItemSpecNameID() {
        return itemSpecNameID;
    }

    public void setItemSpecNameID(int itemSpecNameID) {
        this.itemSpecNameID = itemSpecNameID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public int getGidbID() {
        return gidbID;
    }

    public void setGidbID(int gidbID) {
        this.gidbID = gidbID;
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
}
