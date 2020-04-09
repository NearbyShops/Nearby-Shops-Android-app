package org.nearbyshops.enduserappnew.Model.ModelCartOrder;

/**
 * Created by sumeet on 30/5/16.
 */
public class Cart {

    private int cartID;
    private int endUserID;
    private int shopID;

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
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
}
