package org.nearbyshops.enduserappnew.Model.ModelCartOrder;

import org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.enduserappnew.Model.ModelBilling.RazorPayOrder;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.Shop;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * Created by sumeet on 29/5/16.
 */
public class Order {



    public static final String DATE_TIME_PLACED = "DATE_TIME_PLACED";
    public static final String STATUS_HOME_DELIVERY = "STATUS_HOME_DELIVERY";



    // Constants
    public static final int DELIVERY_MODE_HOME_DELIVERY = 1;
    public static final int DELIVERY_MODE_PICKUP_FROM_SHOP = 2;

    public static final int PAYMENT_MODE_CASH_ON_DELIVERY = 1;
    public static final int PAYMENT_MODE_PAY_ONLINE_ON_DELIVERY = 2;
    public static final int PAYMENT_MODE_RAZORPAY = 3;





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



    private Date deliveryDate;
    private int deliverySlotID;
    private int deliveryMode;
    private int paymentMode;





    private Shop shop;
    private DeliveryAddress deliveryAddress;
    private OrderStats orderStats;
    private User rt_delivery_guy_profile;


    private double rt_deliveryDistance;
    private double rt_pickupDistance;



    private RazorPayOrder razorPayOrder;
    private DeliverySlot deliverySlot;









    // getter and setter


    public RazorPayOrder getRazorPayOrder() {
        return razorPayOrder;
    }

    public void setRazorPayOrder(RazorPayOrder razorPayOrder) {
        this.razorPayOrder = razorPayOrder;
    }

    public double getRt_deliveryDistance() {
        return rt_deliveryDistance;
    }

    public void setRt_deliveryDistance(double rt_deliveryDistance) {
        this.rt_deliveryDistance = rt_deliveryDistance;
    }

    public double getRt_pickupDistance() {
        return rt_pickupDistance;
    }

    public void setRt_pickupDistance(double rt_pickupDistance) {
        this.rt_pickupDistance = rt_pickupDistance;
    }

    public DeliverySlot getDeliverySlot() {
        return deliverySlot;
    }

    public void setDeliverySlot(DeliverySlot deliverySlot) {
        this.deliverySlot = deliverySlot;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getDeliverySlotID() {
        return deliverySlotID;
    }

    public void setDeliverySlotID(int deliverySlotID) {
        this.deliverySlotID = deliverySlotID;
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public int getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

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
