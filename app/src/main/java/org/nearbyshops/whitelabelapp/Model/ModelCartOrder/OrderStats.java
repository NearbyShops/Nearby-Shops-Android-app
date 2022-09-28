package org.nearbyshops.whitelabelapp.Model.ModelCartOrder;

/**
 * Created by sumeet on 13/6/16.
 */
public class OrderStats {

    private int orderID;
    private int itemCount;
    private int itemTotal;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(int itemTotal) {
        this.itemTotal = itemTotal;
    }
}
