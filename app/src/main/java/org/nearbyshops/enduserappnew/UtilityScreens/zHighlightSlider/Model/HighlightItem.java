package org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model;

import android.content.Context;

import org.nearbyshops.enduserappnew.R;

public class HighlightItem {


    public static String SLIDE_BOOKING_HELPLINE = "booking_helpline";


    private String titleTop;
    private String title;
    private String description;
    private String footer;

    private String imageURL;

    private String slideID;




    // highlight builders


    public static HighlightItem slideOneFrontScreen(Context context)
    {

        HighlightItem highlightItem = new HighlightItem();

        highlightItem.setTitleTop("");
        highlightItem.setTitle("Get Orders Delivered in 2 hours from your favourite Local Stores");
        highlightItem.setDescription("");
        highlightItem.setFooter("");
        highlightItem.setImageURL(context.getString(R.string.slide_one_front_screen));

        return highlightItem;
    }



    public static HighlightItem slideThreeFrontScreen(Context context)
    {
        HighlightItem highlightItem = new HighlightItem();

        highlightItem.setSlideID(SLIDE_BOOKING_HELPLINE);
        highlightItem.setTitleTop("");
        highlightItem.setTitle("Fully Indian brand ... No Chinese Investment !");
        highlightItem.setDescription("");
        highlightItem.setFooter("");
        highlightItem.setImageURL(context.getString(R.string.slide_three_front_screen));

        return highlightItem;
    }



    public static HighlightItem slideTwoFrontScreen(Context context)
    {

        HighlightItem highlightItem = new HighlightItem();

        highlightItem.setSlideID(SLIDE_BOOKING_HELPLINE);
        highlightItem.setTitleTop("");
        highlightItem.setTitle("Order from your favourite local store !");
        highlightItem.setDescription("");
        highlightItem.setFooter("");
        highlightItem.setImageURL(context.getString(R.string.slide_two_front_screen));

        return highlightItem;
    }





    public static HighlightItem slideOne(Context context)
    {

        HighlightItem highlightItem = new HighlightItem();

        highlightItem.setSlideID(SLIDE_BOOKING_HELPLINE);
        highlightItem.setTitleTop("");
        highlightItem.setTitle("Order from your favourite local store !");
        highlightItem.setDescription("");
        highlightItem.setFooter("");
        highlightItem.setImageURL(context.getString(R.string.slide_one_item_screen));

        return highlightItem;
    }




    public static HighlightItem slideTwo(Context context)
    {

        HighlightItem highlightItem = new HighlightItem();

        highlightItem.setSlideID(SLIDE_BOOKING_HELPLINE);
        highlightItem.setTitleTop("");
        highlightItem.setTitle("Order from your favourite local store !");
        highlightItem.setDescription("");
        highlightItem.setFooter("");
        highlightItem.setImageURL(context.getString(R.string.slide_two_item_screen));


        return highlightItem;
    }



    public static HighlightItem slideThree(Context context)
    {

        HighlightItem highlightItem = new HighlightItem();

        highlightItem.setSlideID(SLIDE_BOOKING_HELPLINE);
        highlightItem.setTitleTop("");
        highlightItem.setTitle("Order from your favourite local store !");
        highlightItem.setDescription("");
        highlightItem.setFooter("");
        highlightItem.setImageURL(context.getString(R.string.slide_three_item_screen));

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
