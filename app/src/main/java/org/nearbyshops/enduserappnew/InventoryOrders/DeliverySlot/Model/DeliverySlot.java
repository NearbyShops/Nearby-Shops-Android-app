package org.nearbyshops.enduserappnew.InventoryOrders.DeliverySlot.Model;


import java.sql.Time;

/**
 * Created by sumeet on 10/6/16.
 */
public class DeliverySlot {



    // column Names
    public static final String SLOT_ID = "SLOT_ID";




    // instance variables
    private int slotID;
    private boolean isEnabled;
    private boolean isDeliverySlot;
    private boolean isPickupSlot;
    private String slotName;
    private int maxOrdersPerDay;
    private int shopID;

    private Time slotTime;
    private int durationInHours;
    private int closingHours;


    private int rt_order_count;









    // getter and setter methods


    public boolean isDeliverySlot() {
        return isDeliverySlot;
    }

    public void setDeliverySlot(boolean deliverySlot) {
        isDeliverySlot = deliverySlot;
    }

    public boolean isPickupSlot() {
        return isPickupSlot;
    }

    public void setPickupSlot(boolean pickupSlot) {
        isPickupSlot = pickupSlot;
    }

    public int getRt_order_count() {
        return rt_order_count;
    }

    public void setRt_order_count(int rt_order_count) {
        this.rt_order_count = rt_order_count;
    }

    public int getClosingHours() {
        return closingHours;
    }

    public void setClosingHours(int closingHours) {
        this.closingHours = closingHours;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
    }

    public Time getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(Time slotTime) {
        this.slotTime = slotTime;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }


    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public int getMaxOrdersPerDay() {
        return maxOrdersPerDay;
    }

    public void setMaxOrdersPerDay(int maxOrdersPerDay) {
        this.maxOrdersPerDay = maxOrdersPerDay;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
