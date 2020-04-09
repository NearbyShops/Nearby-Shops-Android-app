package org.nearbyshops.enduserappnew.Model.ModelReviewItem;


import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;

/**
 * Created by sumeet on 8/8/16.
 */
public class FavouriteItem {

    // Table Name
    public static final String TABLE_NAME = "FAVOURITE_ITEM";

    // column Names
    public static final String END_USER_ID = "END_USER_ID"; // foreign Key
    public static final String ITEM_ID = "ITEM_ID"; // foreign Key
    public static final String IS_FAVOURITE = "IS_FAVOURITE";


    // Create Table Statement
    public static final String createTableFavouriteItemPostgres = "CREATE TABLE IF NOT EXISTS "
            + FavouriteItem.TABLE_NAME + "("

            + " " + FavouriteItem.END_USER_ID + " INT,"
            + " " + FavouriteItem.ITEM_ID + " INT,"
            + " " + FavouriteItem.IS_FAVOURITE + " boolean,"

            + " FOREIGN KEY(" + FavouriteItem.END_USER_ID +") REFERENCES " + User.TABLE_NAME + "(" + User.USER_ID + "),"
            + " FOREIGN KEY(" + FavouriteItem.ITEM_ID +") REFERENCES " + Item.TABLE_NAME + "(" + Item.ITEM_ID + "),"
            + " PRIMARY KEY (" + FavouriteItem.END_USER_ID + ", " + FavouriteItem.ITEM_ID + ")"
            + ")";


    // instance Variables

    private Integer endUserID;
    private Integer itemID;

    // Getter and Setter


    public Integer getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(Integer endUserID) {
        this.endUserID = endUserID;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }
}
