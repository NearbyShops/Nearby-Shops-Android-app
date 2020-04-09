package org.nearbyshops.enduserappnew.Model.ModelCartOrder;

import org.nearbyshops.enduserappnew.Model.Item;

/**
 * Created by sumeet on 30/5/16.
 */
public class CartItem{

    private int cartID;
    private int itemID;

    private Cart cart;
    private Item item;

    private double itemQuantity;
    private int rt_availableItemQuantity;
    private double rt_itemPrice;
    private String rt_quantityUnit;







    public int getRt_availableItemQuantity() {
        return rt_availableItemQuantity;
    }

    public void setRt_availableItemQuantity(int rt_availableItemQuantity) {
        this.rt_availableItemQuantity = rt_availableItemQuantity;
    }

    public double getRt_itemPrice() {
        return rt_itemPrice;
    }

    public void setRt_itemPrice(double rt_itemPrice) {
        this.rt_itemPrice = rt_itemPrice;
    }

    public String getRt_quantityUnit() {
        return rt_quantityUnit;
    }

    public void setRt_quantityUnit(String rt_quantityUnit) {
        this.rt_quantityUnit = rt_quantityUnit;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }


    public double getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(double itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
