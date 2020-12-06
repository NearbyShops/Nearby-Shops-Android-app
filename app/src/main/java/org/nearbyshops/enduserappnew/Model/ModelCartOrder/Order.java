package org.nearbyshops.enduserappnew.Model.ModelCartOrder;

import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.Shop;

import java.sql.Timestamp;


/**
 * Created by sumeet on 29/5/16.
 */
public class Order {




    // Table Name for Distributor
    public static final String TABLE_NAME = "CUSTOMER_ORDER";

    // Column names for Distributor
    public static final String ORDER_ID = "ORDER_ID";

    public static final String END_USER_ID = "END_USER_ID"; // foreign Key
    public static final String SHOP_ID = "SHOP_ID"; // foreign Key
    public static final String DELIVERY_ADDRESS_ID = "DELIVERY_ADDRESS_ID";
    public static final String DELIVERY_GUY_SELF_ID = "DELIVERY_GUY_SELF_ID";

    public static final String DELIVERY_OTP = "DELIVERY_OTP";

    public static final String ITEM_COUNT = "ITEM_COUNT";

    public static final String ITEM_TOTAL = "ITEM_TOTAL";
    public static final String APP_SERVICE_CHARGE = "APP_SERVICE_CHARGE";
    public static final String DELIVERY_CHARGES = "DELIVERY_CHARGES";
    //    public static final String TAX_AMOUNT = "TAX_AMOUNT";
    public static final String NET_PAYABLE = "NET_PAYABLE";

    public static final String IS_CANCELLED_BY_END_USER = "CANCELLED_BY";   // true implies the order is cancelled by end-user and false implies the order is cancelled by shop
    public static final String REASON_FOR_CANCELLED_BY_USER = "REASON_FOR_CANCELLED_BY_USER";
    public static final String REASON_FOR_CANCELLED_BY_SHOP = "REASON_FOR_CANCELLED_BY_SHOP";
    public static final String REASON_FOR_ORDER_RETURNED = "REASON_FOR_ORDER_RETURNED";

    public static final String DATE_TIME_PLACED = "DATE_TIME_PLACED";
    public static final String TIMESTAMP_HD_CONFIRMED = "TIMESTAMP_HD_CONFIRMED";
    public static final String TIMESTAMP_HD_PACKED = "TIMESTAMP_HD_PACKED";
    public static final String TIMESTAMP_HD_OUT_FOR_DELIVERY = "TIMESTAMP_HD_OUT_FOR_DELIVERY";
    public static final String TIMESTAMP_HD_DELIVERED = "TIMESTAMP_HD_DELIVERED";

    public static final String PICK_FROM_SHOP = "PICK_FROM_SHOP";
    public static final String STATUS_HOME_DELIVERY = "STATUS_HOME_DELIVERY";
    public static final String STATUS_PICK_FROM_SHOP = "STATUS_PICK_FROM_SHOP";





    // Instance Variables

    private int orderID;

    private int endUserID;
    private int shopID;
    private int deliveryAddressID;
    private int deliveryGuySelfID;

    private int deliveryOTP;

    private int itemCount;

    private double itemTotal;
    private double appServiceCharge;
    private double deliveryCharges;
    private double netPayable;
    private double savingsOverMRP;

    private boolean isCancelledByEndUser;
    private String reasonCancelledByShop;
    private String reasonCancelledByUser;
    private String reasonForOrderReturned;

    private Timestamp dateTimePlaced;
    private Timestamp timestampHDConfirmed;
    private Timestamp timestampHDPacked;
    private Timestamp timestampHDOutForDelivery;
    private Timestamp timestampHDDelivered;


    private boolean isPickFromShop;
    private int statusHomeDelivery;
    private int statusPickFromShop;



    private Shop shop;
    private DeliveryAddress deliveryAddress;
    private OrderStats orderStats;
    private User rt_delivery_guy_profile;






    // getter and setter


    public double getSavingsOverMRP() {
        return savingsOverMRP;
    }

    public void setSavingsOverMRP(double savingsOverMRP) {
        this.savingsOverMRP = savingsOverMRP;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(int endUserID) {
        this.endUserID = endUserID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getDeliveryAddressID() {
        return deliveryAddressID;
    }

    public void setDeliveryAddressID(int deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
    }



    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public double getAppServiceCharge() {
        return appServiceCharge;
    }

    public void setAppServiceCharge(double appServiceCharge) {
        this.appServiceCharge = appServiceCharge;
    }

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public double getNetPayable() {
        return netPayable;
    }



    public Timestamp getDateTimePlaced() {
        return dateTimePlaced;
    }


    public boolean isPickFromShop() {
        return isPickFromShop;
    }

    public void setPickFromShop(boolean pickFromShop) {
        isPickFromShop = pickFromShop;
    }

    public int getStatusHomeDelivery() {
        return statusHomeDelivery;
    }

    public void setStatusHomeDelivery(int statusHomeDelivery) {
        this.statusHomeDelivery = statusHomeDelivery;
    }

    public int getStatusPickFromShop() {
        return statusPickFromShop;
    }

    public void setStatusPickFromShop(int statusPickFromShop) {
        this.statusPickFromShop = statusPickFromShop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public int getDeliveryGuySelfID() {
        return deliveryGuySelfID;
    }

    public void setDeliveryGuySelfID(int deliveryGuySelfID) {
        this.deliveryGuySelfID = deliveryGuySelfID;
    }

    public int getDeliveryOTP() {
        return deliveryOTP;
    }

    public void setDeliveryOTP(int deliveryOTP) {
        this.deliveryOTP = deliveryOTP;
    }

    public void setNetPayable(double netPayable) {
        this.netPayable = netPayable;
    }

    public boolean isCancelledByEndUser() {
        return isCancelledByEndUser;
    }

    public void setCancelledByEndUser(boolean cancelledByEndUser) {
        isCancelledByEndUser = cancelledByEndUser;
    }

    public String getReasonCancelledByShop() {
        return reasonCancelledByShop;
    }

    public void setReasonCancelledByShop(String reasonCancelledByShop) {
        this.reasonCancelledByShop = reasonCancelledByShop;
    }

    public String getReasonCancelledByUser() {
        return reasonCancelledByUser;
    }

    public void setReasonCancelledByUser(String reasonCancelledByUser) {
        this.reasonCancelledByUser = reasonCancelledByUser;
    }

    public String getReasonForOrderReturned() {
        return reasonForOrderReturned;
    }

    public void setReasonForOrderReturned(String reasonForOrderReturned) {
        this.reasonForOrderReturned = reasonForOrderReturned;
    }

    public void setDateTimePlaced(Timestamp dateTimePlaced) {
        this.dateTimePlaced = dateTimePlaced;
    }

    public Timestamp getTimestampHDConfirmed() {
        return timestampHDConfirmed;
    }

    public void setTimestampHDConfirmed(Timestamp timestampHDConfirmed) {
        this.timestampHDConfirmed = timestampHDConfirmed;
    }

    public Timestamp getTimestampHDPacked() {
        return timestampHDPacked;
    }

    public void setTimestampHDPacked(Timestamp timestampHDPacked) {
        this.timestampHDPacked = timestampHDPacked;
    }

    public Timestamp getTimestampHDOutForDelivery() {
        return timestampHDOutForDelivery;
    }

    public void setTimestampHDOutForDelivery(Timestamp timestampHDOutForDelivery) {
        this.timestampHDOutForDelivery = timestampHDOutForDelivery;
    }

    public Timestamp getTimestampHDDelivered() {
        return timestampHDDelivered;
    }

    public void setTimestampHDDelivered(Timestamp timestampHDDelivered) {
        this.timestampHDDelivered = timestampHDDelivered;
    }

    public OrderStats getOrderStats() {
        return orderStats;
    }

    public void setOrderStats(OrderStats orderStats) {
        this.orderStats = orderStats;
    }

    public User getRt_delivery_guy_profile() {
        return rt_delivery_guy_profile;
    }

    public void setRt_delivery_guy_profile(User rt_delivery_guy_profile) {
        this.rt_delivery_guy_profile = rt_delivery_guy_profile;
    }
}
