package org.nearbyshops.whitelabelapp.Model.ModelBilling;



import java.sql.Timestamp;

/**
 * Created by sumeet on 4/8/17.
 */
public class Transaction {


    // constants
    public static final int TRANSACTION_TYPE_TAXI_REFERRAL_CHARGE = 1;
    public static final int TRANSACTION_TYPE_JOINING_CREDIT = 2;
    public static final int TRANSACTION_TYPE_USER_REFERRAL_CREDIT = 3;
    public static final int TRANSACTION_TYPE_PAYMENT_MADE = 4;

    public static final String TITLE_REFERRAL_CHARGE_FOR_TRIP = "Referral Charge";
    public static final String DESCRIPTION_REFERRAL_CHARGE_FOR_TRIP = "Referral Charges for Trip";

    public static final String TITLE_JOINING_CREDIT_FOR_DRIVER = "Joining Credit";
    public static final String DESCRIPTION_JOINING_CREDIT_FOR_DRIVEr = "Joining credit applied for ";

    public static final String TITLE_REFERRAL_CREDIT_APPLIED = "Referral Credit";
    public static final String DESCRIPTION_REFERRAL_CREDIT_APPLIED = "Referral credit applied";


    // Table Name for User
    public static final String TABLE_NAME = "TRANSACTION_HISTORY";




    // Column names
    public static final String TRANSACTION_ID = "TRANSACTION_ID";
    public static final String USER_ID = "USER_ID";

    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";

    public static final String TRANSACTION_TYPE = "TRANSACTION_TYPE";

    public static final String TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT";
    public static final String TAX_AMOUNT = "TAX_AMOUNT";

    public static final String IS_CREDIT = "IS_CREDIT"; // indicates whether transaction is credit or debit

    public static final String TIMESTAMP_OCCURRED = "TIMESTAMP_OCCURRED";

//    public static final String CURRENT_DUES_BEFORE_TRANSACTION = "CURRENT_DUES_BEFORE_TRANSACTION";
    public static final String SERVICE_BALANCE_AFTER_TRANSACTION = "SERVICE_BALANCE_AFTER_TRANSACTION";
    public static final String TAX_BALANCE_AFTER_TRANSACTION = "TAX_BALANCE_AFTER_TRANSACTION";







    // Instance Variables
    private int transactionID;
    private int userID;

    private String title;
    private String description;

    private int transactionType;

    private double taxAmount;
    private double transactionAmount;

    private Timestamp timestampOccurred;

//    private double currentDuesBeforeTransaction;
    private double serviceBalanceAfterTransaction;
    private double taxBalanceAfterTransaction;





    // getter and setters


    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTaxBalanceAfterTransaction() {
        return taxBalanceAfterTransaction;
    }

    public void setTaxBalanceAfterTransaction(double taxBalanceAfterTransaction) {
        this.taxBalanceAfterTransaction = taxBalanceAfterTransaction;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    public Timestamp getTimestampOccurred() {
        return timestampOccurred;
    }

    public void setTimestampOccurred(Timestamp timestampOccurred) {
        this.timestampOccurred = timestampOccurred;
    }

    public double getServiceBalanceAfterTransaction() {
        return serviceBalanceAfterTransaction;
    }

    public void setServiceBalanceAfterTransaction(double serviceBalanceAfterTransaction) {
        this.serviceBalanceAfterTransaction = serviceBalanceAfterTransaction;
    }
}
