package org.nearbyshops.enduserappnew.UtilityScreens.BannerSlider.Model;

import java.sql.Timestamp;

/**
 * Created by sumeet on 28/2/17.
 */
public class BannerImage {

    
    // Table Name
    public static final String TABLE_NAME = "BANNER_IMAGES";

    // column names
    public static final String BANNER_IMAGE_ID = "BANNER_IMAGE_ID";
    public static final String SHOP_ID = "SHOP_ID";
    public static final String IMAGE_FILENAME = "IMAGE_FILENAME";

    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";

    public static final String SORT_ORDER = "SORT_ORDER";
    public static final String SHOP_TO_OPEN = "SHOP_TO_OPEN";
    public static final String ITEM_TO_OPEN = "ITEM_TO_OPEN";

    public static final String SHOW_IN_FRONT_SCREEN = "SHOW_IN_FRONT_SCREEN";
    public static final String SHOW_IN_ITEMS_SCREEN = "SHOW_IN_ITEMS_SCREEN";
    public static final String SHOW_IN_SHOP_HOME = "SHOW_IN_SHOP_HOME";





    // instance variables
    private int bannerImageID;
    private int shopID;
    private String imageFilename;

    private Timestamp timestampCreated;

    private int sortOrder;
    private int shopIdToOpen;
    private int itemIDToOpen;

    private boolean showInFrontScreen;
    private boolean showInItemsScreen;
    private boolean showInShopHome;





    // getter and setters


    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public boolean isShowInFrontScreen() {
        return showInFrontScreen;
    }

    public void setShowInFrontScreen(boolean showInFrontScreen) {
        this.showInFrontScreen = showInFrontScreen;
    }

    public boolean isShowInItemsScreen() {
        return showInItemsScreen;
    }

    public void setShowInItemsScreen(boolean showInItemsScreen) {
        this.showInItemsScreen = showInItemsScreen;
    }

    public boolean isShowInShopHome() {
        return showInShopHome;
    }

    public void setShowInShopHome(boolean showInShopHome) {
        this.showInShopHome = showInShopHome;
    }

    public int getBannerImageID() {
        return bannerImageID;
    }

    public void setBannerImageID(int bannerImageID) {
        this.bannerImageID = bannerImageID;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getShopIdToOpen() {
        return shopIdToOpen;
    }

    public void setShopIdToOpen(int shopIdToOpen) {
        this.shopIdToOpen = shopIdToOpen;
    }

    public int getItemIDToOpen() {
        return itemIDToOpen;
    }

    public void setItemIDToOpen(int itemIDToOpen) {
        this.itemIDToOpen = itemIDToOpen;
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

}
