package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models;


import org.nearbyshops.enduserappnew.R;

public class EmptyScreenDataFullScreen {


    private String title;
    private String message;
    private int drawableResource;
    private boolean showCopyrightInfo;
    private String buttonName;




    public static EmptyScreenDataFullScreen getOffline()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("You are offline");
        data.setShowCopyrightInfo(false);
        data.setMessage("No internet ... Please check your internet connection and refresh again !");
        data.setDrawableResource(R.drawable.ic_cloud_offline);

        return data;
    }




    public static EmptyScreenDataFullScreen getNoTransactions()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Transactions to Show !");
        data.setShowCopyrightInfo(false);
        data.setMessage("When you make any monetary transactions they will appear here !");
        data.setDrawableResource(R.drawable.ic_receipt_color);

        return data;
    }



    public static EmptyScreenDataFullScreen getEmptyScreenShopsListMultiMarket()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Shops at your Location");
        data.setShowCopyrightInfo(false);
        data.setMessage("Uh .. oh .. no shops available at your location .. change your market ... and explore other markets !");
        data.setDrawableResource(R.drawable.ic_local_shipping_color);
        data.setButtonName("Change Market");

        return data;
    }



    public static EmptyScreenDataFullScreen getEmptyScreenShopsListSingleMarket()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Shops at your Location");
        data.setShowCopyrightInfo(false);
        data.setMessage("Uh .. oh .. no shops available at your location .. change your location ... and try again");
        data.setDrawableResource(R.drawable.ic_local_shipping_color);
        data.setButtonName("Try Again");

        return data;
    }





    public static EmptyScreenDataFullScreen getErrorTemplate(int errorCode)
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("There is an Error !!");
        data.setShowCopyrightInfo(false);
        data.setMessage("There is an Error ... when asking for help please tell the error code as " + errorCode );
        data.setDrawableResource(R.drawable.ic_close_black_24dp);

        return data;
    }




    public static EmptyScreenDataFullScreen noItemsAndCategories()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Items and Categories");
        data.setShowCopyrightInfo(false);
        data.setMessage("No items in this category");
        data.setDrawableResource(R.drawable.ic_clear_black_24dp);

        return data;
    }



    public static EmptyScreenDataFullScreen emptyScreenShopsListForAdmin()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Shops Here !");
        data.setShowCopyrightInfo(false);
        data.setMessage("Swipe right or left to see more shops !");
        data.setDrawableResource(R.drawable.ic_home_black_24dp);

        return data;
    }



    public static EmptyScreenDataFullScreen emptyScreenDeliveryAddress()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("Please add a Delivery Address !");
        data.setShowCopyrightInfo(false);
        data.setMessage("You have not added any delivery address ... Please add a delivery address !");
        data.setDrawableResource(R.drawable.ic_home_black_24dp);
        data.setButtonName("Add Address");

        return data;
    }



    public static EmptyScreenDataFullScreen emptyScreenOrders()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Orders to Show !");
        data.setShowCopyrightInfo(false);
        data.setMessage("You have not received any orders ... When you receive orders they will appear here !");
        data.setDrawableResource(R.drawable.ic_receipt_color);

        return data;
    }



    public static EmptyScreenDataFullScreen emptyScreenShopImages()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Images to Show !");
        data.setShowCopyrightInfo(false);
        data.setMessage("No extra images available for this Shop !");
        data.setDrawableResource(R.drawable.ic_barcode);

        return data;
    }




    public static EmptyScreenDataFullScreen emptyScreenItemImages()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Images to Show !");
        data.setShowCopyrightInfo(false);
        data.setMessage("No extra images available for this Item !");
        data.setDrawableResource(R.drawable.ic_dashboard_black_24dp);

        return data;
    }







    public static EmptyScreenDataFullScreen emptyScreenQuickStockEditor()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("You have not added any items !");
        data.setShowCopyrightInfo(false);
        data.setMessage("You have not added any items to your shop ... when you add items to your shop they will appear here !");
        data.setDrawableResource(R.drawable.ic_items_grey);

        return data;
    }













    public static EmptyScreenDataFullScreen emptyScreenPFSINventory()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Orders Here !");
        data.setShowCopyrightInfo(false);
        data.setMessage("Swipe right or left to see more orders !");
        data.setDrawableResource(R.drawable.ic_items_grey);

        return data;
    }






    public static EmptyScreenDataFullScreen emptyUsersList()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Users Found !");
        data.setShowCopyrightInfo(false);
        data.setMessage("No Users found ... if you have set any filters try to clear filters and try again !");
        data.setDrawableResource(R.drawable.ic_account_box_black_24px);

        return data;
    }




    public static EmptyScreenDataFullScreen emptyScreenShopStaffList()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("You have not added any Staff Members !");
        data.setShowCopyrightInfo(false);
        data.setMessage("To add staff members to your shop press the Plus Button ( + ) given below !");
        data.setDrawableResource(R.drawable.ic_account_box_black_24px);

        return data;
    }




    public static EmptyScreenDataFullScreen emptyScreenStaffList()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("You have not added any Staff Members !");
        data.setShowCopyrightInfo(false);
        data.setMessage("To add staff members to your Market press the Plus Button ( + ) given below !");
        data.setDrawableResource(R.drawable.ic_account_box_black_24px);

        return data;
    }





    public static EmptyScreenDataFullScreen emptyScreenDeliveryStaff()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("You have not added any Delivery Staff !");
        data.setShowCopyrightInfo(false);
        data.setMessage("To add delivery staff to your shop press the Plus Button ( + ) given below !");
        data.setDrawableResource(R.drawable.ic_person_pin_circle_black_24px);

        return data;
    }













    // getter and setter


    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public boolean isShowCopyrightInfo() {
        return showCopyrightInfo;
    }

    public void setShowCopyrightInfo(boolean showCopyrightInfo) {
        this.showCopyrightInfo = showCopyrightInfo;
    }

    public int getDrawableResource() {
        return drawableResource;
    }

    public void setDrawableResource(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
