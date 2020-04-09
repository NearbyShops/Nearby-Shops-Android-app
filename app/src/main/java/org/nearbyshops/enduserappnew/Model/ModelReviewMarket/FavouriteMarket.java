package org.nearbyshops.enduserappnew.Model.ModelReviewMarket;


/**
 * Created by sumeet on 8/8/16.
 */
public class FavouriteMarket {


    // Table Name
    public static final String TABLE_NAME = "FAVOURITE_MARKETS";

    // column Names
    public static final String END_USER_ID = "END_USER_ID"; // foreign Key
    public static final String ITEM_ID = "ITEM_ID"; // foreign Key




    // instance Variables

    private int endUserID;
    private int itemID;





    // Getter and Setter

    public int getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(int endUserID) {
        this.endUserID = endUserID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}
