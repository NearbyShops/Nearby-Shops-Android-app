package org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew.Model;

import org.nearbyshops.whitelabelapp.Model.Shop;

public class ShopAddress {

    Shop shop;


    public ShopAddress(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
