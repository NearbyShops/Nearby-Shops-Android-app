package org.nearbyshops.whitelabelapp.Model.ModelCartOrder;

import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin;
import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType;
import org.nearbyshops.whitelabelapp.Model.Item;

/**
 * Created by sumeet on 29/5/16.
 */
public class OrderItem extends ViewType {


    public OrderItem() {
        setViewType(AdapterKotlin.VIEW_TYPE_ORDER_ITEM);
    }



    // Table Name
    public static final String TABLE_NAME = "ORDER_ITEM";

    // Column names

    public static final String ITEM_ID = "ITEM_ID";     // FOREIGN KEY
    public static final String ORDER_ID = "ORDER_ID";   // Foreign KEY
    public static final String ITEM_QUANTITY = "ITEM_QUANTITY";
    public static final String ITEM_PRICE_AT_ORDER = "ITEM_PRICE_AT_ORDER";




    // instance variables
    private int itemID;
    private int orderID;
    private double itemQuantity;
    private double itemPriceAtOrder;
    private double listPriceAtOrder;

    private Item item;





    public double getListPriceAtOrder() {
        return listPriceAtOrder;
    }

    public void setListPriceAtOrder(double listPriceAtOrder) {
        this.listPriceAtOrder = listPriceAtOrder;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public double getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(double itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getItemPriceAtOrder() {
        return itemPriceAtOrder;
    }

    public void setItemPriceAtOrder(double itemPriceAtOrder) {
        this.itemPriceAtOrder = itemPriceAtOrder;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
