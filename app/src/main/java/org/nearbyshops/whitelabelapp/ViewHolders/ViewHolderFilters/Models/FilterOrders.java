package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models;

import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.OrderEndPoint;

public class FilterOrders {



    boolean showDeliveryGuyFilter;
    boolean showShopFilter;
    boolean showDeliverySlotFilter;

    Integer selectedDeliverySlotID;
    Integer selectedShopID;

    OrderEndPoint orderEndPoint;


    public Integer getSelectedShopID() {
        return selectedShopID;
    }

    public void setSelectedShopID(Integer selectedShopID) {
        this.selectedShopID = selectedShopID;
    }

    public Integer getSelectedDeliverySlotID() {
        return selectedDeliverySlotID;
    }

    public void setSelectedDeliverySlotID(Integer selectedDeliverySlotID) {
        this.selectedDeliverySlotID = selectedDeliverySlotID;
    }

    public OrderEndPoint getOrderEndPoint() {
        return orderEndPoint;
    }

    public void setOrderEndPoint(OrderEndPoint orderEndPoint) {
        this.orderEndPoint = orderEndPoint;
    }

    public boolean isShowDeliveryGuyFilter() {
        return showDeliveryGuyFilter;
    }

    public void setShowDeliveryGuyFilter(boolean showDeliveryGuyFilter) {
        this.showDeliveryGuyFilter = showDeliveryGuyFilter;
    }

    public boolean isShowShopFilter() {
        return showShopFilter;
    }

    public void setShowShopFilter(boolean showShopFilter) {
        this.showShopFilter = showShopFilter;
    }

    public boolean isShowDeliverySlotFilter() {
        return showDeliverySlotFilter;
    }

    public void setShowDeliverySlotFilter(boolean showDeliverySlotFilter) {
        this.showDeliverySlotFilter = showDeliverySlotFilter;
    }
}
