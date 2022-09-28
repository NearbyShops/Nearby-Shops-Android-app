package org.nearbyshops.whitelabelapp.Model.ModelUtility;

public class DeliveryConfig {


    private double billAmountForFreeDelivery;
    private double minimumAmountForDelivery;

    private double baseDeliveryCharge;
    private double distanceFee;
    private double totalDeliveryFee;


    @Override
    public String toString() {
        return "DeliveryConfig{" +
                "deliveryCharge=" + baseDeliveryCharge +
                ", billAmountForFreeDelivery=" + billAmountForFreeDelivery +
                ", minimumAmountForDelivery=" + minimumAmountForDelivery +
                ", distanceFee=" + distanceFee +
                '}';
    }


    public double getTotalDeliveryFee() {
        return totalDeliveryFee;
    }

    public void setTotalDeliveryFee(double totalDeliveryFee) {
        this.totalDeliveryFee = totalDeliveryFee;
    }

    public double getDistanceFee() {
        return distanceFee;
    }

    public void setDistanceFee(double distanceFee) {
        this.distanceFee = distanceFee;
    }

    public double getBaseDeliveryCharge() {
        return baseDeliveryCharge;
    }

    public void setBaseDeliveryCharge(double baseDeliveryCharge) {
        this.baseDeliveryCharge = baseDeliveryCharge;
    }

    public double getBillAmountForFreeDelivery() {
        return billAmountForFreeDelivery;
    }

    public void setBillAmountForFreeDelivery(double billAmountForFreeDelivery) {
        this.billAmountForFreeDelivery = billAmountForFreeDelivery;
    }

    public double getMinimumAmountForDelivery() {
        return minimumAmountForDelivery;
    }

    public void setMinimumAmountForDelivery(double minimumAmountForDelivery) {
        this.minimumAmountForDelivery = minimumAmountForDelivery;
    }
}
