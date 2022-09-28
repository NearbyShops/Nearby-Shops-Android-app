package org.nearbyshops.whitelabelapp.Model.ModelReviewShop;


import org.nearbyshops.whitelabelapp.Model.Shop;

/**
 * Created by sumeet on 8/8/16.
 */
public class FavouriteShop {




    // instance Variables

    private Integer endUserID;
    private Integer shopID;


    private Shop shopProfile;

    // Getter and Setter


    public Shop getShopProfile() {
        return shopProfile;
    }

    public void setShopProfile(Shop shopProfile) {
        this.shopProfile = shopProfile;
    }

    public Integer getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(Integer endUserID) {
        this.endUserID = endUserID;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }
}
