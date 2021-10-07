package org.nearbyshops.whitelabelapp.Model.ModelMarket;


/**
 * Created by sumeet on 19/6/16.
 */
public class MarketSettings {


    // Table Name
    public static final String TABLE_NAME = "MARKET_SETTINGS";

    // column Names
    public static final String SETTING_ID = "SETTING_ID";

    public static final String COD_ENABLED = "COD_ENABLED";
    public static final String POD_ENABLED = "POD_ENABLED";
    public static final String ONLINE_PAYMENT_ENABLED = "RAZOR_PAY_ENABLED";

    public static final String MARKET_FEE_FIXED = "MARKET_FEE_FIXED";
    public static final String MARKET_FEE_COMMISSION = "MARKET_FEE_COMMISSION";
    public static final String TAX_IN_PERCENT = "TAX_IN_PERCENT";
    public static final String ADD_TAXES_TO_BILL = "ADD_TAXES_TO_BILL"; // if enabled it takes market fee from the customer - Optional

//    public static final String USE_STANDARD_DELIVERY_FEE = "USE_STANDARD_DELIVERY_FEE"; // instead of using different delivery charge
//    set by each shop use a standard delivery charge set by the market

    public static final String BASE_DELIVERY_FEE_PER_ORDER = "BASE_DELIVERY_FEE_PER_ORDER";
    public static final String BASE_DELIVERY_FEE_MAX_DISTANCE = "BASE_DELIVERY_FEE_MAX_DISTANCE";
    public static final String EXTRA_DELIVERY_CHARGE_PER_KM = "EXTRA_DELIVERY_CHARGE_PER_KM";

    public static final String BILL_AMOUNT_FOR_FREE_DELIVERY = "BILL_AMOUNT_FOR_FREE_DELIVERY";
    public static final String DELIVERY_BY_SHOP_ENABLED = "DELIVERY_BY_SHOP_ENABLED";
    public static final String MINIMUM_AMOUNT_FOR_DELIVERY = "MINIMUM_AMOUNT_FOR_DELIVERY";

    public static final String STARTUP_MODE_ENABLED = "STARTUP_MODE_ENABLED";
    public static final String DEMO_MODE_ENABLED = "DEMO_MODE_ENABLED";
    public static final String HIDE_CANCEL_BUTTON_AFTER_ORDER_CONFIRMATION = "HIDE_CANCEL_BUTTON_AFTER_ORDER_CONFIRMATION";
    public static final String SHOW_CREATE_SHOP_IN_FRONT = "SHOW_CREATE_SHOP_IN_FRONT";

    private static final String MIN_ACCOUNT_BALANCE_FOR_SHOP_OWNER = "MIN_ACCOUNT_BALANCE_FOR_SHOP_OWNER";


    // Create Table Statement
    public static final String createTablePostgres
            = "CREATE TABLE IF NOT EXISTS " + MarketSettings.TABLE_NAME + "("
            + " " + MarketSettings.SETTING_ID + " SERIAL PRIMARY KEY,"

            + " " + MarketSettings.COD_ENABLED + " boolean not null default 't',"
            + " " + MarketSettings.POD_ENABLED + " boolean not null default 't',"
            + " " + MarketSettings.ONLINE_PAYMENT_ENABLED + " boolean not null default 'f',"

            + " " + MarketSettings.MARKET_FEE_FIXED + " FLOAT not null default 0,"
            + " " + MarketSettings.MARKET_FEE_COMMISSION + " FLOAT not null default 0,"
            + " " + MarketSettings.TAX_IN_PERCENT + " FLOAT not null default 0,"
            + " " + MarketSettings.ADD_TAXES_TO_BILL + " boolean not null default 'f',"

            + " " + MarketSettings.BASE_DELIVERY_FEE_PER_ORDER + " FLOAT not null default 0,"
            + " " + MarketSettings.BASE_DELIVERY_FEE_MAX_DISTANCE + " FLOAT not null default 0,"
            + " " + MarketSettings.EXTRA_DELIVERY_CHARGE_PER_KM + " FLOAT not null default 0,"

            + " " + MarketSettings.BILL_AMOUNT_FOR_FREE_DELIVERY + " FLOAT not null default 0,"
            + " " + MarketSettings.DELIVERY_BY_SHOP_ENABLED + " boolean not null default 'f',"
            + " " + MarketSettings.MINIMUM_AMOUNT_FOR_DELIVERY + " FLOAT not null default 0,"

            + " " + MarketSettings.STARTUP_MODE_ENABLED + " boolean not null default 'f',"
            + " " + MarketSettings.DEMO_MODE_ENABLED + " boolean not null default 'f', "
            + " " + MarketSettings.MIN_ACCOUNT_BALANCE_FOR_SHOP_OWNER + " FLOAT not null default 0 "

            + ")";



    int settingID;

    boolean codEnabled;
    boolean podEnabled;
    boolean onlinePaymentEnabled;

    float marketFeeFixed;
    float marketFeeCommission;
    float taxInPercent;
    boolean addTaxesToBill;

    float baseDeliveryChargePerOrder;
    float baseDeliveryFeeMaxDistance;
    float extraDeliveryChargePerKm;

    float billAmountForFreeDelivery;
    boolean deliveryByShopEnabled;
    double minimumAmountForDelivery;

    boolean startupModeEnabled;
    boolean demoModeEnabled;
    int minAccountBalanceForShopOwner;

    int joiningCreditForShopOwner;
    int joiningCreditForEndUser;
    int referralCreditForEndUserRegistration;




    // getter and setter methods


    public int getReferralCreditForEndUserRegistration() {
        return referralCreditForEndUserRegistration;
    }

    public void setReferralCreditForEndUserRegistration(int referralCreditForEndUserRegistration) {
        this.referralCreditForEndUserRegistration = referralCreditForEndUserRegistration;
    }

    public int getJoiningCreditForShopOwner() {
        return joiningCreditForShopOwner;
    }

    public void setJoiningCreditForShopOwner(int joiningCreditForShopOwner) {
        this.joiningCreditForShopOwner = joiningCreditForShopOwner;
    }

    public int getJoiningCreditForEndUser() {
        return joiningCreditForEndUser;
    }

    public void setJoiningCreditForEndUser(int joiningCreditForEndUser) {
        this.joiningCreditForEndUser = joiningCreditForEndUser;
    }

    public int getMinAccountBalanceForShopOwner() {
        return minAccountBalanceForShopOwner;
    }

    public void setMinAccountBalanceForShopOwner(int minAccountBalanceForShopOwner) {
        this.minAccountBalanceForShopOwner = minAccountBalanceForShopOwner;
    }

    public float getTaxInPercent() {
        return taxInPercent;
    }

    public void setTaxInPercent(float taxInPercent) {
        this.taxInPercent = taxInPercent;
    }

    public boolean isDeliveryByShopEnabled() {
        return deliveryByShopEnabled;
    }

    public void setDeliveryByShopEnabled(boolean deliveryByShopEnabled) {
        this.deliveryByShopEnabled = deliveryByShopEnabled;
    }

    public double getMinimumAmountForDelivery() {
        return minimumAmountForDelivery;
    }

    public void setMinimumAmountForDelivery(double minimumAmountForDelivery) {
        this.minimumAmountForDelivery = minimumAmountForDelivery;
    }

    public float getBillAmountForFreeDelivery() {
        return billAmountForFreeDelivery;
    }

    public void setBillAmountForFreeDelivery(float billAmountForFreeDelivery) {
        this.billAmountForFreeDelivery = billAmountForFreeDelivery;
    }

    public int getSettingID() {
        return settingID;
    }

    public void setSettingID(int settingID) {
        this.settingID = settingID;
    }

    public boolean isCodEnabled() {
        return codEnabled;
    }

    public void setCodEnabled(boolean codEnabled) {
        this.codEnabled = codEnabled;
    }

    public boolean isPodEnabled() {
        return podEnabled;
    }

    public void setPodEnabled(boolean podEnabled) {
        this.podEnabled = podEnabled;
    }

    public boolean isOnlinePaymentEnabled() {
        return onlinePaymentEnabled;
    }

    public void setOnlinePaymentEnabled(boolean onlinePaymentEnabled) {
        this.onlinePaymentEnabled = onlinePaymentEnabled;
    }

    public float getMarketFeeFixed() {
        return marketFeeFixed;
    }

    public void setMarketFeeFixed(float marketFeeFixed) {
        this.marketFeeFixed = marketFeeFixed;
    }

    public float getMarketFeeCommission() {
        return marketFeeCommission;
    }

    public void setMarketFeeCommission(float marketFeeCommission) {
        this.marketFeeCommission = marketFeeCommission;
    }

    public boolean isAddTaxesToBill() {
        return addTaxesToBill;
    }

    public void setAddTaxesToBill(boolean addTaxesToBill) {
        this.addTaxesToBill = addTaxesToBill;
    }

    public float getBaseDeliveryChargePerOrder() {
        return baseDeliveryChargePerOrder;
    }

    public void setBaseDeliveryChargePerOrder(float baseDeliveryChargePerOrder) {
        this.baseDeliveryChargePerOrder = baseDeliveryChargePerOrder;
    }

    public float getBaseDeliveryFeeMaxDistance() {
        return baseDeliveryFeeMaxDistance;
    }

    public void setBaseDeliveryFeeMaxDistance(float baseDeliveryFeeMaxDistance) {
        this.baseDeliveryFeeMaxDistance = baseDeliveryFeeMaxDistance;
    }

    public float getExtraDeliveryChargePerKm() {
        return extraDeliveryChargePerKm;
    }

    public void setExtraDeliveryChargePerKm(float extraDeliveryChargePerKm) {
        this.extraDeliveryChargePerKm = extraDeliveryChargePerKm;
    }

    public boolean isStartupModeEnabled() {
        return startupModeEnabled;
    }

    public void setStartupModeEnabled(boolean startupModeEnabled) {
        this.startupModeEnabled = startupModeEnabled;
    }

    public boolean isDemoModeEnabled() {
        return demoModeEnabled;
    }

    public void setDemoModeEnabled(boolean demoModeEnabled) {
        this.demoModeEnabled = demoModeEnabled;
    }
}
