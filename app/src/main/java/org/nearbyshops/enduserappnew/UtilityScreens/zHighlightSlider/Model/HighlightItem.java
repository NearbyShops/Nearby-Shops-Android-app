package org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model;

public class HighlightItem {


    public static String SLIDE_BOOKING_HELPLINE = "booking_helpline";


    private String titleTop;
    private String title;
    private String description;
    private String footer;

    private String imageURL;

    private String slideID;




    // highlight builders


    public static HighlightItem getBookingHelpline()
    {
        HighlightItem highlightItem = new HighlightItem();

        highlightItem.setSlideID(SLIDE_BOOKING_HELPLINE);
        highlightItem.setTitleTop("");
        highlightItem.setTitle("Shop Locally and get home delivery !");
        highlightItem.setDescription("");
        highlightItem.setFooter("");
        highlightItem.setImageURL("https://images.unsplash.com/photo-1568158879083-c42860933ed7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=300&q=80");

        return highlightItem;
    }





    public static HighlightItem getNegotiatePrices()
    {
        HighlightItem highlightItem = new HighlightItem();


        highlightItem.setTitleTop("");
        highlightItem.setTitle("Daily Essentials and Grocery");
        highlightItem.setDescription("Get daily essentials and grocery");
        highlightItem.setFooter("");
        highlightItem.setImageURL("https://images.unsplash.com/photo-1471113082645-fde63c139087?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=358&q=80");


        return highlightItem;
    }







    // getter and setter

    public String getTitleTop() {
        return titleTop;
    }

    public void setTitleTop(String titleTop) {
        this.titleTop = titleTop;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSlideID() {
        return slideID;
    }

    public void setSlideID(String slideID) {
        this.slideID = slideID;
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




}
