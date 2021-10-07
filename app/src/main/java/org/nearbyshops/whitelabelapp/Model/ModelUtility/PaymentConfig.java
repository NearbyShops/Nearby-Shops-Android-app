package org.nearbyshops.whitelabelapp.Model.ModelUtility;

public class PaymentConfig {

    boolean cashOnDeliveryEnabled;
    boolean payOnlineOnDeliveryEnabled;
    boolean razorPayEnabled;
    String razorPayKey;


    public String getRazorPayKey() {
        return razorPayKey;
    }

    public void setRazorPayKey(String razorPayKey) {
        this.razorPayKey = razorPayKey;
    }

    public boolean isCashOnDeliveryEnabled() {
        return cashOnDeliveryEnabled;
    }

    public void setCashOnDeliveryEnabled(boolean cashOnDeliveryEnabled) {
        this.cashOnDeliveryEnabled = cashOnDeliveryEnabled;
    }

    public boolean isPayOnlineOnDeliveryEnabled() {
        return payOnlineOnDeliveryEnabled;
    }

    public void setPayOnlineOnDeliveryEnabled(boolean payOnlineOnDeliveryEnabled) {
        this.payOnlineOnDeliveryEnabled = payOnlineOnDeliveryEnabled;
    }

    public boolean isRazorPayEnabled() {
        return razorPayEnabled;
    }

    public void setRazorPayEnabled(boolean razorPayEnabled) {
        this.razorPayEnabled = razorPayEnabled;
    }
}
