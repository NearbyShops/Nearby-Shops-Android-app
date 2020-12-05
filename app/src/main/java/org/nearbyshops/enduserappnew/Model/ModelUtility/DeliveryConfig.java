package org.nearbyshops.enduserappnew.Model.ModelUtility;

public class DeliveryConfig {

    private double marketDeliveryCharge;
    private double marketFeeForPickup;
    private double marketFeeForDelivery;
    private boolean marketFeeAddedToBill;

    private boolean useStandardDeliveryCharge;






    public boolean isUseStandardDeliveryCharge() {
        return useStandardDeliveryCharge;
    }

    public void setUseStandardDeliveryCharge(boolean useStandardDeliveryCharge) {
        this.useStandardDeliveryCharge = useStandardDeliveryCharge;
    }

    public double getMarketDeliveryCharge() {
        return marketDeliveryCharge;
    }

    public void setMarketDeliveryCharge(double marketDeliveryCharge) {
        this.marketDeliveryCharge = marketDeliveryCharge;
    }

    public double getMarketFeeForPickup() {
        return marketFeeForPickup;
    }

    public void setMarketFeeForPickup(double marketFeeForPickup) {
        this.marketFeeForPickup = marketFeeForPickup;
    }

    public double getMarketFeeForDelivery() {
        return marketFeeForDelivery;
    }

    public void setMarketFeeForDelivery(double marketFeeForDelivery) {
        this.marketFeeForDelivery = marketFeeForDelivery;
    }

    public boolean isMarketFeeAddedToBill() {
        return marketFeeAddedToBill;
    }

    public void setMarketFeeAddedToBill(boolean marketFeeAddedToBill) {
        this.marketFeeAddedToBill = marketFeeAddedToBill;
    }
}
