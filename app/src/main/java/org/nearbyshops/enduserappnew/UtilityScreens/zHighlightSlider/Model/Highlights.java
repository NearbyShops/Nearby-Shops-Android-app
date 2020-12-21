package org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model;

import java.util.ArrayList;
import java.util.List;


public class Highlights {



    // Zero Commission | Lowest Price | Flexible Fares - No Package Restrictions | Multiple Destinations | Share Cab With Friends |

    //

    private List<Object> highlightList;

    private String listTitle;





    public static Highlights getHighlightsCabRental()
    {

        List<Object> list = new ArrayList<>();


        list.add(HighlightItem.getNegotiatePrices());
        list.add(HighlightItem.getBookingHelpline());

        Highlights highlights = new Highlights();
        highlights.setHighlightList(list);
        highlights.setListTitle(null);


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
