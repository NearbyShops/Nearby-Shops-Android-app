package org.nearbyshops.whitelabelapp.Model.ModelStaff;



public class ShopStaffPermissions {

    // Table Name for User
    public static final String TABLE_NAME = "SHOP_STAFF_PERMISSIONS";


    // Column names
    public static final String PERMISSION_ID = "PERMISSION_ID";
    public static final String STAFF_ID = "STAFF_ID";
    public static final String SHOP_ID = "SHOP_ID";

    public static final String DESIGNATION = "DESIGNATION";
    public static final String LAT_CURRENT = "LAT_CURRENT";
    public static final String LON_CURRENT = "LON_CURRENT";
    
    // permissions General
    public static final String ADD_REMOVE_ITEMS_FROM_SHOP = "ADD_REMOVE_ITEMS_FROM_SHOP";
    public static final String UPDATE_STOCK = "UPDATE_STOCK";

    // permissions : Home Delivery
    public static final String CANCEL_ORDERS = "CANCEL_ORDERS";

    public static final String CONFIRM_ORDERS = "CONFIRM_ORDERS";
    public static final String SET_ORDERS_PACKED = "SET_ORDERS_PACKED";
    public static final String HANDOVER_TO_DELIVERY = "HANDOVER_TO_DELIVERY";

    public static final String ACCEPT_PAYMENTS_FROM_DELIVERY = "ACCEPT_PAYMENTS_FROM_DELIVERY";
    public static final String ACCEPT_RETURNS = "ACCEPT_RETURNS";




    // instance variables
    private int permissionID;
    private int staffUserID;
    private int shopID;

    private String designation;

    private double latCurrent;
    private double lonCurrent;

    private boolean permitAddRemoveItems;
    private boolean permitUpdateItemsInShop;

    private boolean permitCancelOrders;
    private boolean permitConfirmOrders;
    private boolean permitSetOrdersPacked;
    private boolean permitHandoverToDelivery;
//    private boolean permitMarkOrdersDelivered;
    private boolean permitAcceptPaymentsFromDelivery;
    private boolean permitAcceptReturns;


    private double rt_distance;






    // getter and setters


    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public boolean isPermitAddRemoveItems() {
        return permitAddRemoveItems;
    }

    public void setPermitAddRemoveItems(boolean permitAddRemoveItems) {
        this.permitAddRemoveItems = permitAddRemoveItems;
    }

    public boolean isPermitUpdateItemsInShop() {
        return permitUpdateItemsInShop;
    }

    public void setPermitUpdateItemsInShop(boolean permitUpdateItemsInShop) {
        this.permitUpdateItemsInShop = permitUpdateItemsInShop;
    }

    public boolean isPermitCancelOrders() {
        return permitCancelOrders;
    }

    public void setPermitCancelOrders(boolean permitCancelOrders) {
        this.permitCancelOrders = permitCancelOrders;
    }

    public boolean isPermitConfirmOrders() {
        return permitConfirmOrders;
    }

    public void setPermitConfirmOrders(boolean permitConfirmOrders) {
        this.permitConfirmOrders = permitConfirmOrders;
    }

    public boolean isPermitSetOrdersPacked() {
        return permitSetOrdersPacked;
    }

    public void setPermitSetOrdersPacked(boolean permitSetOrdersPacked) {
        this.permitSetOrdersPacked = permitSetOrdersPacked;
    }

    public boolean isPermitHandoverToDelivery() {
        return permitHandoverToDelivery;
    }

    public void setPermitHandoverToDelivery(boolean permitHandoverToDelivery) {
        this.permitHandoverToDelivery = permitHandoverToDelivery;
    }




    public boolean isPermitAcceptPaymentsFromDelivery() {
        return permitAcceptPaymentsFromDelivery;
    }

    public void setPermitAcceptPaymentsFromDelivery(boolean permitAcceptPaymentsFromDelivery) {
        this.permitAcceptPaymentsFromDelivery = permitAcceptPaymentsFromDelivery;
    }

    public boolean isPermitAcceptReturns() {
        return permitAcceptReturns;
    }

    public void setPermitAcceptReturns(boolean permitAcceptReturns) {
        this.permitAcceptReturns = permitAcceptReturns;
    }

    public double getRt_distance() {
        return rt_distance;
    }

    public void setRt_distance(double rt_distance) {
        this.rt_distance = rt_distance;
    }

    public double getLatCurrent() {
        return latCurrent;
    }

    public void setLatCurrent(double latCurrent) {
        this.latCurrent = latCurrent;
    }

    public double getLonCurrent() {
        return lonCurrent;
    }

    public void setLonCurrent(double lonCurrent) {
        this.lonCurrent = lonCurrent;
    }

    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }

    public int getStaffUserID() {
        return staffUserID;
    }

    public void setStaffUserID(int staffUserID) {
        this.staffUserID = staffUserID;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
