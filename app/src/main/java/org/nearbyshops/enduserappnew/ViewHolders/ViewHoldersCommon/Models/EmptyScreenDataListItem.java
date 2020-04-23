package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models;

import org.nearbyshops.enduserappnew.R;

public class EmptyScreenDataListItem {


    private String title;
    private String message;
    private String buttonText;
    private String urlForButtonClick;
    private int imageResource;



    public static EmptyScreenDataListItem getEmptyScreenShopsListMultiMarket()
    {
        EmptyScreenDataListItem data = new EmptyScreenDataListItem();
        data.setTitle("No Shops at your Location");
//        data.setShowCopyrightInfo(false);
        data.setMessage("Uh .. oh .. no shops available at your location .. try to change your market ... or explore other markets !");
        data.setImageResource(R.drawable.ic_barcode);
        data.setButtonText("Change Market");

        return data;
    }



    public static EmptyScreenDataListItem getEmptyScreenShopsListSingleMarket()
    {
        EmptyScreenDataListItem data = new EmptyScreenDataListItem();
        data.setTitle("No Shops at your Location");
//        data.setShowCopyrightInfo(false);
        data.setMessage("Uh .. oh .. no shops available at your location .. change your location ... and try again");
        data.setImageResource(R.drawable.ic_barcode);
        data.setButtonText("Try Again");

        return data;
    }




    public static EmptyScreenDataListItem getCreateMarketData()
    {
        EmptyScreenDataListItem data = new EmptyScreenDataListItem();
        data.setMessage("Create your own Market ... help local Economy ... !");
        data.setButtonText("Create Market");
        data.setUrlForButtonClick("https://nearbyshops.org/volunteer.html");

        return data;
    }




    public static EmptyScreenDataListItem createMarketNoMarketsAvailable()
    {
        EmptyScreenDataListItem data = new EmptyScreenDataListItem();
        data.setMessage("No markets available in your area. \n\nCreate your own local market using free open-source technology and Help Local Vendors and Local Economy");
        data.setButtonText("Create Market");
        data.setUrlForButtonClick("https://nearbyshops.org/volunteer.html");

        data.setImageResource(R.drawable.ic_local_florist_black_24dp);

        return data;
    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getUrlForButtonClick() {
        return urlForButtonClick;
    }

    public void setUrlForButtonClick(String urlForButtonClick) {
        this.urlForButtonClick = urlForButtonClick;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
