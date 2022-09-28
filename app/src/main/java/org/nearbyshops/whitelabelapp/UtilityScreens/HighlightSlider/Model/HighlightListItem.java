package org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model;

public class HighlightListItem {


    private String imageURL;
    private String linkToOpenURL;
    private int slideNumber;


    public HighlightListItem(String imageURL) {
        this.imageURL = imageURL;
    }

    public HighlightListItem(String imageURL, int slideNumber) {
        this.imageURL = imageURL;
        this.slideNumber = slideNumber;
    }

    // getter and setter


    public int getSlideNumber() {
        return slideNumber;
    }

    public void setSlideNumber(int slideNumber) {
        this.slideNumber = slideNumber;
    }

    public String getLinkToOpenURL() {
        return linkToOpenURL;
    }

    public void setLinkToOpenURL(String linkToOpenURL) {
        this.linkToOpenURL = linkToOpenURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
