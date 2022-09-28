package org.nearbyshops.whitelabelapp.Model.ModelItemSpecs;


import org.nearbyshops.whitelabelapp.Model.Item;

/**
 * Created by sumeet on 2/3/17.
 */
public class ItemSpecificationItem {


    // Join table : joins Item and ItemSpecificationValue



    // Table Name
    public static final String TABLE_NAME = "ITEM_SPECIFICATION_NAME";

    // column names
    public static final String ITEM_ID = "ID";
    public static final String ITEM_SPECIFICATION_VALUE_ID = "ITEM_SPECIFICATION_VALUE_ID";




    // create table statement
    public static final String createTableItemSpecificationItemPostgres = "CREATE TABLE IF NOT EXISTS "
            + ItemSpecificationItem.TABLE_NAME + "("
            + " " + ItemSpecificationItem.ITEM_ID + " int,"
            + " " + ItemSpecificationItem.ITEM_SPECIFICATION_VALUE_ID + " int,"
            + " FOREIGN KEY(" + ItemSpecificationItem.ITEM_ID +") REFERENCES " + Item.TABLE_NAME + "(" + Item.ITEM_ID + "),"
            + " FOREIGN KEY(" + ItemSpecificationItem.ITEM_SPECIFICATION_VALUE_ID +") REFERENCES " + ItemSpecificationValue.TABLE_NAME + "(" + ItemSpecificationValue.ID + ")"
            + ")";




    // instance variables

    private int itemID;
    private int itemSpecValueID;



    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getItemSpecValueID() {
        return itemSpecValueID;
    }

    public void setItemSpecValueID(int itemSpecValueID) {
        this.itemSpecValueID = itemSpecValueID;
    }
}
