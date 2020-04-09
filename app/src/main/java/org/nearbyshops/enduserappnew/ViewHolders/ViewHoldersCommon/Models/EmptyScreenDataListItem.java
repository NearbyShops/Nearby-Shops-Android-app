package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models;

import org.nearbyshops.enduserappnew.R;

public class EmptyScreenDataListItem {


    private String message;
    private String buttonText;
    private String urlForButtonClick;
    private int imageResource;


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
