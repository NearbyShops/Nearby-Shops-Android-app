package org.nearbyshops.whitelabelapp.Model.ModelItemSpecs;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 2/3/17.
 */
public class ItemSpecificationName {


    // Table Name
    public static final String TABLE_NAME = "ITEM_SPECIFICATION_NAME";

    // column names
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String IMAGE_FILENAME = "IMAGE_FILENAME";

    public static final String GIDB_ID = "GIDB_ID";
    public static final String GIDB_SERVICE_URL = "GIDB_SERVICE_URL";

    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";
    public static final String TIMESTAMP_UPDATED = "TIMESTAMP_UPDATED";


    // create table statement
    public static final String createTableItemSpecNamePostgres = "CREATE TABLE IF NOT EXISTS "
            + ItemSpecificationName.TABLE_NAME + "("

            + " " + ItemSpecificationName.ID + " SERIAL PRIMARY KEY,"
            + " " + ItemSpecificationName.TITLE + " text,"
            + " " + ItemSpecificationName.DESCRIPTION + " text,"
            + " " + ItemSpecificationName.IMAGE_FILENAME + " text,"

            + " " + ItemSpecificationName.GIDB_ID + " int,"
            + " " + ItemSpecificationName.GIDB_SERVICE_URL + " text,"

            + " " + ItemSpecificationName.TIMESTAMP_CREATED + " timestamp with time zone NOT NULL DEFAULT now(),"
            + " " + ItemSpecificationName.TIMESTAMP_UPDATED + " timestamp with time zone"
            + ")";




    // instance variables

    private int id;
    private String title;
    private String description;
    private String imageFilename;

    private int gidbID;
    private String gidbServiceURL;

    private Timestamp timestampCreated;
    private Timestamp timestampUpdated;

    private List<ItemSpecificationValue> rt_itemSpecificationValue = new ArrayList<>();


    // getter and setters


    public Timestamp getTimestampUpdated() {
        return timestampUpdated;
    }

    public void setTimestampUpdated(Timestamp timestampUpdated) {
        this.timestampUpdated = timestampUpdated;
    }

    public List<ItemSpecificationValue> getRt_itemSpecificationValue() {
        return rt_itemSpecificationValue;
    }

    public void setRt_itemSpecificationValue(List<ItemSpecificationValue> rt_itemSpecificationValue) {
        this.rt_itemSpecificationValue = rt_itemSpecificationValue;
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
