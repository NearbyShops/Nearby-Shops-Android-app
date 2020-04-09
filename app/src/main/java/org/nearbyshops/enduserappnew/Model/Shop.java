package org.nearbyshops.enduserappnew.Model;



import org.nearbyshops.enduserappnew.Model.ModelRoles.User;

import java.sql.Timestamp;

public class Shop{





	// normal variables
	private int shopID;
	
	private String shopName;

	// the radius of the circle considering shop location as its center.
	//This is the distance upto which shop can deliver its items
	private double deliveryRange;

	// latitude and longitude for storing the location of the shop
	private double latCenter;
	private double lonCenter;

	// delivery charger per order
	private double deliveryCharges;
	private int billAmountForFreeDelivery;
	private boolean pickFromShopAvailable;
	private boolean homeDeliveryAvailable;

	private boolean shopEnabled;
	private boolean shopWaitlisted;


	
	private String logoImagePath;


	// added recently
	private String shopAddress;
	private String city;
	private long pincode;
	private String landmark;

	private String customerHelplineNumber;
	private String deliveryHelplineNumber;

	private String shortDescription;
	private String longDescription;

	private Timestamp dateTimeStarted;
	private Timestamp timestampCreated;
	private boolean isOpen;
	private int shopAdminID;


	private double accountBalance;



	// real time variables
	private double rt_distance;
	private float rt_rating_avg;
	private float rt_rating_count;
	private double rt_min_balance;


	private User shopAdminProfile;
	private double extendedCreditLimit;








	public Timestamp getTimestampCreated() {
		return timestampCreated;
	}

	public void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public int getShopAdminID() {
		return shopAdminID;
	}

	public void setShopAdminID(int shopAdminID) {
		this.shopAdminID = shopAdminID;
	}

	public double getExtendedCreditLimit() {
		return extendedCreditLimit;
	}

	public void setExtendedCreditLimit(double extendedCreditLimit) {
		this.extendedCreditLimit = extendedCreditLimit;
	}

	public User getShopAdminProfile() {
		return shopAdminProfile;
	}

	public void setShopAdminProfile(User shopAdminProfile) {
		this.shopAdminProfile = shopAdminProfile;
	}

	public boolean isPickFromShopAvailable() {
		return pickFromShopAvailable;
	}

	public void setPickFromShopAvailable(boolean pickFromShopAvailable) {
		this.pickFromShopAvailable = pickFromShopAvailable;
	}

	public boolean isHomeDeliveryAvailable() {
		return homeDeliveryAvailable;
	}

	public void setHomeDeliveryAvailable(boolean homeDeliveryAvailable) {
		this.homeDeliveryAvailable = homeDeliveryAvailable;
	}

	public boolean isShopEnabled() {
		return shopEnabled;
	}

	public void setShopEnabled(boolean shopEnabled) {
		this.shopEnabled = shopEnabled;
	}

	public boolean isShopWaitlisted() {
		return shopWaitlisted;
	}

	public void setShopWaitlisted(boolean shopWaitlisted) {
		this.shopWaitlisted = shopWaitlisted;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public double getRt_min_balance() {
		return rt_min_balance;
	}

	public void setRt_min_balance(double rt_min_balance) {
		this.rt_min_balance = rt_min_balance;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getDeliveryRange() {
		return deliveryRange;
	}

	public void setDeliveryRange(double deliveryRange) {
		this.deliveryRange = deliveryRange;
	}

	public double getLatCenter() {
		return latCenter;
	}

	public void setLatCenter(double latCenter) {
		this.latCenter = latCenter;
	}

	public double getLonCenter() {
		return lonCenter;
	}

	public void setLonCenter(double lonCenter) {
		this.lonCenter = lonCenter;
	}

	public double getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(double deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public int getBillAmountForFreeDelivery() {
		return billAmountForFreeDelivery;
	}

	public void setBillAmountForFreeDelivery(int billAmountForFreeDelivery) {
		this.billAmountForFreeDelivery = billAmountForFreeDelivery;
	}

	public Boolean getPickFromShopAvailable() {
		return pickFromShopAvailable;
	}

	public void setPickFromShopAvailable(Boolean pickFromShopAvailable) {
		this.pickFromShopAvailable = pickFromShopAvailable;
	}

	public Boolean getHomeDeliveryAvailable() {
		return homeDeliveryAvailable;
	}

	public void setHomeDeliveryAvailable(Boolean homeDeliveryAvailable) {
		this.homeDeliveryAvailable = homeDeliveryAvailable;
	}

	public Boolean getShopEnabled() {
		return shopEnabled;
	}

	public void setShopEnabled(Boolean shopEnabled) {
		this.shopEnabled = shopEnabled;
	}

	public Boolean getShopWaitlisted() {
		return shopWaitlisted;
	}

	public void setShopWaitlisted(Boolean shopWaitlisted) {
		this.shopWaitlisted = shopWaitlisted;
	}

	public String getLogoImagePath() {
		return logoImagePath;
	}

	public void setLogoImagePath(String logoImagePath) {
		this.logoImagePath = logoImagePath;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getPincode() {
		return pincode;
	}

	public void setPincode(long pincode) {
		this.pincode = pincode;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCustomerHelplineNumber() {
		return customerHelplineNumber;
	}

	public void setCustomerHelplineNumber(String customerHelplineNumber) {
		this.customerHelplineNumber = customerHelplineNumber;
	}

	public String getDeliveryHelplineNumber() {
		return deliveryHelplineNumber;
	}

	public void setDeliveryHelplineNumber(String deliveryHelplineNumber) {
		this.deliveryHelplineNumber = deliveryHelplineNumber;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public Timestamp getDateTimeStarted() {
		return dateTimeStarted;
	}

	public void setDateTimeStarted(Timestamp dateTimeStarted) {
		this.dateTimeStarted = dateTimeStarted;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean open) {
		isOpen = open;
	}

	public double getRt_distance() {
		return rt_distance;
	}

	public void setRt_distance(double rt_distance) {
		this.rt_distance = rt_distance;
	}

	public float getRt_rating_avg() {
		return rt_rating_avg;
	}

	public void setRt_rating_avg(float rt_rating_avg) {
		this.rt_rating_avg = rt_rating_avg;
	}

	public float getRt_rating_count() {
		return rt_rating_count;
	}

	public void setRt_rating_count(float rt_rating_count) {
		this.rt_rating_count = rt_rating_count;
	}
}
