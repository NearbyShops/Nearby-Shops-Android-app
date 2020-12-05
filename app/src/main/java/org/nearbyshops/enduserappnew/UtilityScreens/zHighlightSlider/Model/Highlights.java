package org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public class Highlights {



    // Zero Commission | Lowest Price | Flexible Fares - No Package Restrictions | Multiple Destinations | Share Cab With Friends |

    //

    private List<Object> highlightList;

    private String listTitle;





    public static Highlights getHighlightsFrontScreen(Context context)
    {

        if(context==null)
        {
            return null;
        }


        List<Object> list = new ArrayList<>();


        list.add(HighlightItem.slideOneFrontScreen(context));
        list.add(HighlightItem.slideTwoFrontScreen(context));
        list.add(HighlightItem.slideThreeFrontScreen(context));

        Highlights highlights = new Highlights();
        highlights.setHighlightList(list);
        highlights.setListTitle("Shop Locally and Get Home Delivery");


        return highlights;
    }




    public static Highlights getHighlightsItemsScreen(Context context)
    {

        if(context==null)
        {
            return null;
        }


        List<Object> list = new ArrayList<>();


        list.add(HighlightItem.slideOne(context));
        list.add(HighlightItem.slideTwo(context));
        list.add(HighlightItem.slideThree(context));

        Highlights highlights = new Highlights();
        highlights.setHighlightList(list);
        highlights.setListTitle("Find Items in your Market");


        return highlights;
    }









    // getter and setters


    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public List<Object> getHighlightList() {
        return highlightList;
    }

    public void setHighlightList(List<Object> highlightList) {
        this.highlightList = highlightList;
    }
}
