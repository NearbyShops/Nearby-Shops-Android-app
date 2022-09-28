package org.nearbyshops.whitelabelapp.UtilityScreens.PhotoSliderViewPager.Model;

import java.util.ArrayList;
import java.util.List;

public class PhotoSliderData {

    String photoURL;
    String message;
    String mainMessage;
    boolean hideTextBlock;

    public PhotoSliderData(String photoURL, String message, String mainMessage) {
        this.photoURL = photoURL;
        this.message = message;
        this.mainMessage = mainMessage;
    }

    public PhotoSliderData(String photoURL, String message, String mainMessage, boolean hideTextBlock) {
        this.photoURL = photoURL;
        this.message = message;
        this.mainMessage = mainMessage;
        this.hideTextBlock = hideTextBlock;
    }

    public boolean isHideTextBlock() {
        return hideTextBlock;
    }

    public void setHideTextBlock(boolean hideTextBlock) {
        this.hideTextBlock = hideTextBlock;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMainMessage() {
        return mainMessage;
    }

    public void setMainMessage(String mainMessage) {
        this.mainMessage = mainMessage;
    }







    static PhotoSliderData getMarketAdminSlideOne()
    {

//        https://images.unsplash.com/photo-1472851294608-062f824d29cc?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80

        return new PhotoSliderData(
                "https://images.unsplash.com/photo-1580440282860-8555b1ae102c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80",
                "No need to develop your app and Spend Money on Online Marketing",
                "Start Your Ecommerce Business in Zero Investment"
        );
    }



    static PhotoSliderData getMarketAdminSlideTwo()
    {

        return new PhotoSliderData(
                "https://images.unsplash.com/photo-1609767768775-30f9e0319f97?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80",
                "Create Market for Institute, Office Campus, Residential Area or any Local Community",
                "Create Your Free Local Market"
        );

    }




    static PhotoSliderData getMarketAdminSlideThree()
    {
        return new PhotoSliderData(
                "https://images.unsplash.com/photo-1506484381205-f7945653044d?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80",
                "Add Local Stores and create your own multi-store marketplace ",
                "Add Local Stores to your Market"
        );
    }




    static PhotoSliderData getMarketAdminSlideFour()
    {
        return new PhotoSliderData(
                "https://images.unsplash.com/photo-1607227063002-677dc5fdf96f?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=967&q=80",
                "Deliver anything from your local stores and earn income for Delivery",
                "Deliver Food, Groceries, Hardware or Anything Else"
        );
    }



    static PhotoSliderData getMarketAdminSlideFive()
    {
//        https://images.unsplash.com/photo-1444653614773-995cb1ef9efa?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=805&q=80

        return new PhotoSliderData(
                "https://images.unsplash.com/photo-1579621970795-87facc2f976d?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80",
                "Charge for Delivery and Earn Commission over Each Sale",
                "Earn Income from your Local Market"
        );
    }





    static PhotoSliderData getMarketAdminSlideSix()
    {
        return new PhotoSliderData(
                "https://images.unsplash.com/photo-1535202610320-919f9b13de77?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=475&q=80",
                "No need to pay monthly fee or Any initial setup cost",
                "Zero Setup Fee and No monthly Payment"
        );
    }







    static PhotoSliderData getVendorSlideOne()
    {
        return new PhotoSliderData(
                "https://i.imgur.com/0NtPoHX.png",
                "Create your Online Store in 10 Minutes with no Monthly Fee or Setup Cost",
                "Create your Free Online Store",true
        );
    }



    static PhotoSliderData getVendorSlideTwo()
    {
        return new PhotoSliderData(
                "https://i.imgur.com/ZMYKy7a.png",
                "Add many items to shop quickly from our database of existing items",
                "Add Items to Shop",true
        );
    }



    static PhotoSliderData getVendorSlideThree()
    {
        return new PhotoSliderData(
                "https://i.imgur.com/XAfZEmf.png",
                "Sell or Deliver food, grocery, Hardware or Anything Else to your Customers",
                "Sell Food, Grocery, Hardware or Anything Else",true
        );
    }





    static PhotoSliderData getVendorSlideFour()
    {
        return new PhotoSliderData(
                "https://i.imgur.com/BaNMMo3.png",
                "Customers can pay to you in cash or make an Online Payment",
                "Collect Payment in Cash or Online Payment",true
        );
    }



    static PhotoSliderData getVendorSlideFive()
    {
        return new PhotoSliderData(
                "https://i.imgur.com/eI51Yg7.png",
                "Manage your Shop Easily with all the features available on a Single Dashboard",
                "Manage Your Shop From Single Dashboard",true
        );
    }





    static PhotoSliderData getCustomerSlideOne()
    {

        return new PhotoSliderData(
                "https://images.unsplash.com/photo-1584799580661-53b7c6b99430?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80",
                "Order Anything from your Local Vendors",
                "Discover Local Shops in Your Area"
        );
    }




    public static List<PhotoSliderData> getMarketAdminSlides()
    {

        List<PhotoSliderData> list = new ArrayList<>();

        list.add(PhotoSliderData.getMarketAdminSlideOne());
        list.add(PhotoSliderData.getMarketAdminSlideTwo());
        list.add(PhotoSliderData.getMarketAdminSlideThree());
        list.add(PhotoSliderData.getMarketAdminSlideFour());
        list.add(PhotoSliderData.getMarketAdminSlideFive());
        list.add(PhotoSliderData.getMarketAdminSlideSix());

        return list;
    }




    public static List<PhotoSliderData> getVendorAppSlides()
    {

        List<PhotoSliderData> list = new ArrayList<>();

        list.add(PhotoSliderData.getVendorSlideOne());
        list.add(PhotoSliderData.getVendorSlideTwo());
        list.add(PhotoSliderData.getVendorSlideThree());
        list.add(PhotoSliderData.getVendorSlideFour());
        list.add(PhotoSliderData.getVendorSlideFive());

        return list;
    }





    public static List<PhotoSliderData> getCustomerSlides()
    {
        int arrayCount = 1;

        List<PhotoSliderData> list = new ArrayList<>();
        list.add(PhotoSliderData.getCustomerSlideOne());

        return list;
    }


}
