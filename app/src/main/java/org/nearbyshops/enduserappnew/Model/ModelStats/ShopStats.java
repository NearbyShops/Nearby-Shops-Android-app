package org.nearbyshops.enduserappnew.Model.ModelStats;

public class ShopStats {

    int ordersNotConfirmedPFS;
    int ordersNotConfirmedHD;

    int itemsInShopCount;
    int priceNotSetCount;
    int outOfStockCount;




    // getter and setter

    public int getOrdersNotConfirmedPFS() {
        return ordersNotConfirmedPFS;
    }

    public void setOrdersNotConfirmedPFS(int ordersNotConfirmedPFS) {
        this.ordersNotConfirmedPFS = ordersNotConfirmedPFS;
    }

    public int getOrdersNotConfirmedHD() {
        return ordersNotConfirmedHD;
    }

    public void setOrdersNotConfirmedHD(int ordersNotConfirmedHD) {
        this.ordersNotConfirmedHD = ordersNotConfirmedHD;
    }

    public int getItemsInShopCount() {
        return itemsInShopCount;
    }

    public void setItemsInShopCount(int itemsInShopCount) {
        this.itemsInShopCount = itemsInShopCount;
    }

    public int getPriceNotSetCount() {
        return priceNotSetCount;
    }

    public void setPriceNotSetCount(int priceNotSetCount) {
        this.priceNotSetCount = priceNotSetCount;
    }

    public int getOutOfStockCount() {
        return outOfStockCount;
    }

    public void setOutOfStockCount(int outOfStockCount) {
        this.outOfStockCount = outOfStockCount;
    }
}
