package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models;

import org.nearbyshops.enduserappnew.Model.Shop;

public class WhatsMessageData {

    Shop shop;

    public WhatsMessageData(Shop shop) {
        this.shop = shop;
    }



    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
