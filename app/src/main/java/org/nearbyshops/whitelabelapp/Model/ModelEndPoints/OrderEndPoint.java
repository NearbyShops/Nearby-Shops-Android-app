package org.nearbyshops.whitelabelapp.Model.ModelEndPoints;


import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.Model.DeliverySlot;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Model.Shop;

import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class OrderEndPoint {

    Integer itemCount;
    Integer offset;
    Integer limit;
    Integer max_limit;
    List<Order> results;


    private List<DeliverySlot> deliverySlotList;
    private List<Shop> shopList;
    private List<User> deliveryPersonList;

    private int deliverySlotCount;
    private int shopCount;
    private int deliveryPersonCount;





    // getter and setter methods

    public List<DeliverySlot> getDeliverySlotList() {
        return deliverySlotList;
    }

    public void setDeliverySlotList(List<DeliverySlot> deliverySlotList) {
        this.deliverySlotList = deliverySlotList;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public List<User> getDeliveryPersonList() {
        return deliveryPersonList;
    }

    public void setDeliveryPersonList(List<User> deliveryPersonList) {
        this.deliveryPersonList = deliveryPersonList;
    }

    public int getDeliverySlotCount() {
        return deliverySlotCount;
    }

    public void setDeliverySlotCount(int deliverySlotCount) {
        this.deliverySlotCount = deliverySlotCount;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public int getDeliveryPersonCount() {
        return deliveryPersonCount;
    }

    public void setDeliveryPersonCount(int deliveryPersonCount) {
        this.deliveryPersonCount = deliveryPersonCount;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<Order> getResults() {
        return results;
    }

    public void setResults(List<Order> results) {
        this.results = results;
    }

    public Integer getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(Integer max_limit) {
        this.max_limit = max_limit;
    }
}
