package org.nearbyshops.whitelabelapp.Model.ModelEndPoints;


import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.OrderItem;
import org.nearbyshops.whitelabelapp.Model.ModelStaff.DeliveryGuyData;
import org.nearbyshops.whitelabelapp.Model.Shop;

import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class OrderItemEndPoint extends ViewType {



    Integer itemCount;
    Integer offset;
    Integer limit;
    Integer max_limit;
    List<OrderItem> results;
    private Shop shopDetails;
    private Order orderDetails;
    private DeliveryGuyData deliveryData;




    public DeliveryGuyData getDeliveryData() {
        return deliveryData;
    }

    public void setDeliveryData(DeliveryGuyData deliveryData) {
        this.deliveryData = deliveryData;
    }

    public Order getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Order orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Shop getShopDetails() {
        return shopDetails;
    }

    public void setShopDetails(Shop shopDetails) {
        this.shopDetails = shopDetails;
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

    public List<OrderItem> getResults() {
        return results;
    }

    public void setResults(List<OrderItem> results) {
        this.results = results;
    }

    public Integer getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(Integer max_limit) {
        this.max_limit = max_limit;
    }
}
