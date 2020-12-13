package org.nearbyshops.enduserappnew.Model.ModelBilling;



public class RazorPayOrder {


    // Table Name for User
    public static final String TABLE_NAME = "RAZOR_PAY_ORDER_INFO";


    // Column names
    public static final String RZP_INFO_ID = "RZP_INFO_ID";
    public static final String LOCAL_ORDER_ID = "LOCAL_ORDER_ID";
    public static final String RZP_PAYMENT_ID = "RZP_PAYMENT_ID";
    public static final String RZP_ORDER_ID = "RZP_ORDER_ID";
    public static final String PAID_AMOUNT = "PAID_AMOUNT";






    int rzpInfoID;
    int localOrderID;
    String rzpPaymentID;
    String rzpOrderID;
    double paidAmount;


    public int getRzpInfoID() {
        return rzpInfoID;
    }

    public void setRzpInfoID(int rzpInfoID) {
        this.rzpInfoID = rzpInfoID;
    }

    public int getLocalOrderID() {
        return localOrderID;
    }

    public void setLocalOrderID(int localOrderID) {
        this.localOrderID = localOrderID;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getRzpPaymentID() {
        return rzpPaymentID;
    }

    public void setRzpPaymentID(String rzpPaymentID) {
        this.rzpPaymentID = rzpPaymentID;
    }

    public String getRzpOrderID() {
        return rzpOrderID;
    }

    public void setRzpOrderID(String rzpOrderID) {
        this.rzpOrderID = rzpOrderID;
    }
}
