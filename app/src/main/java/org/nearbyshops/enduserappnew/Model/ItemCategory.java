package org.nearbyshops.enduserappnew.Model;


public class ItemCategory{

	// contract class describing the Globals schema for the ItemCategory

	// Table Name
	public static final String TABLE_NAME = "ITEM_CATEGORY";

	// Column Names
	public static final String ITEM_CATEGORY_ID = "ID";
	public static final String ITEM_CATEGORY_NAME = "ITEM_CATEGORY_NAME";
	public static final String ITEM_CATEGORY_DESCRIPTION = "ITEM_CATEGORY_DESC";
	public static final String PARENT_CATEGORY_ID = "PARENT_CATEGORY_ID";
	public static final String IS_LEAF_NODE = "IS_LEAF";
	public static final String IMAGE_PATH = "IMAGE_PATH";
	public static final String CATEGORY_ORDER = "CATEGORY_ORDER";

	// to be implemented
	public static final String ITEM_CATEGORY_DESCRIPTION_SHORT = "ITEM_CATEGORY_DESCRIPTION_SHORT";
	public static final String IS_ABSTRACT = "IS_ABSTRACT";


	// to be Implemented
	public static final String IS_ENABLED = "IS_ENABLED";
	public static final String IS_WAITLISTED = "IS_WAITLISTED";







	// Instance Variables


	private int itemCategoryID;
	private String categoryName;
	private String categoryDescription;
	private int parentCategoryID;
	private boolean isLeafNode;
	private String imagePath;
	private int categoryOrder;
	// recently added
	private boolean isAbstractNode;
	private String descriptionShort;
	private boolean isEnabled;
	private boolean isWaitlisted;




	private String rt_gidb_service_url;
	private ItemCategory parentCategory = null;

	private int rt_scroll_position;





	// getter and setter


	public int getRt_scroll_position() {
		return rt_scroll_position;
	}

	public void setRt_scroll_position(int rt_scroll_position) {
		this.rt_scroll_position = rt_scroll_position;
	}

	public int getItemCategoryID() {
		return itemCategoryID;
	}

	public void setItemCategoryID(int itemCategoryID) {
		this.itemCategoryID = itemCategoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public int getParentCategoryID() {
		return parentCategoryID;
	}

	public void setParentCategoryID(int parentCategoryID) {
		this.parentCategoryID = parentCategoryID;
	}

	public boolean isLeafNode() {
		return isLeafNode;
	}

	public void setLeafNode(boolean leafNode) {
		isLeafNode = leafNode;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getCategoryOrder() {
		return categoryOrder;
	}

	public void setCategoryOrder(int categoryOrder) {
		this.categoryOrder = categoryOrder;
	}

	public boolean isAbstractNode() {
		return isAbstractNode;
	}

	public void setAbstractNode(boolean abstractNode) {
		isAbstractNode = abstractNode;
	}

	public String getDescriptionShort() {
		return descriptionShort;
	}

	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public boolean isWaitlisted() {
		return isWaitlisted;
	}

	public void setWaitlisted(boolean waitlisted) {
		isWaitlisted = waitlisted;
	}

	public String getRt_gidb_service_url() {
		return rt_gidb_service_url;
	}

	public void setRt_gidb_service_url(String rt_gidb_service_url) {
		this.rt_gidb_service_url = rt_gidb_service_url;
	}

	public ItemCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(ItemCategory parentCategory) {
		this.parentCategory = parentCategory;
	}
}
