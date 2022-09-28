package org.nearbyshops.whitelabelapp.Model.ModelCartOrder;

import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType;
import org.nearbyshops.whitelabelapp.Model.ModelBilling.RazorPayOrder;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.whitelabelapp.Model.Shop;

import java.sql.Timestamp;


/**
 * Created by sumeet on 29/5/16.
 */
public class Order extends ViewType {



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
    private int deliveryGuyID;

    private int deliveryAddressID;

    private int deliveryOTP;

    private int itemCount;
    private double itemTotal;
    private double deliveryCharges;
    private double netPayable;
    private double savingsOverMRP;

    private int statusCurrent;
    private int deliveryMode;
    private int paymentMode;
    private int orderSource;



    private int cancelledBy;
    private String reasonForCancellation;
    private Timestamp dateTimePlaced;




    private Shop shop;
    private DeliveryAddress deliveryAddress;
    private OrderStats orderStats;
    private User rt_delivery_guy_profile;


    private double rt_deliveryDistance;
    private double rt_pickupDistance;



    private RazorPayOrder razorPayOrder;
    private DeliverySlot deliverySlot;





    // getter and setter
    public int getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(int orderSource) {
        this.orderSource = orderSource;
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

    public int getDeliveryGuyID() {
        return deliveryGuyID;
    }

    public void setDeliveryGuyID(int deliveryGuyID) {
        this.deliveryGuyID = deliveryGuyID;
    }

    public int getDeliveryAddressID() {
        return deliveryAddressID;
    }

    public void setDeliveryAddressID(int deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
    }

    public int getDeliveryOTP() {
        return deliveryOTP;
    }

    public void setDeliveryOTP(int deliveryOTP) {
        this.deliveryOTP = deliveryOTP;
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

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public double getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(double netPayable) {
        this.netPayable = netPayable;
    }

    public double getSavingsOverMRP() {
        return savingsOverMRP;
    }

    public void setSavingsOverMRP(double savingsOverMRP) {
        this.savingsOverMRP = savingsOverMRP;
    }

    public int getStatusCurrent() {
        return statusCurrent;
    }

    public void setStatusCurrent(int statusCurrent) {
        this.statusCurrent = statusCurrent;
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

    public int getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(int cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getReasonForCancellation() {
        return reasonForCancellation;
    }

    public void setReasonForCancellation(String reasonForCancellation) {
        this.reasonForCancellation = reasonForCancellation;
    }

    public Timestamp getDateTimePlaced() {
        return dateTimePlaced;
    }

    public void setDateTimePlaced(Timestamp dateTimePlaced) {
        this.dateTimePlaced = dateTimePlaced;
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

    public RazorPayOrder getRazorPayOrder() {
        return razorPayOrder;
    }

    public void setRazorPayOrder(RazorPayOrder razorPayOrder) {
        this.razorPayOrder = razorPayOrder;
    }

    public DeliverySlot getDeliverySlot() {
        return deliverySlot;
    }

    public void setDeliverySlot(DeliverySlot deliverySlot) {
        this.deliverySlot = deliverySlot;
    }
}
