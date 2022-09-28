package org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model;

import java.util.List;


public class HighlightList {


    private String listTitle;
    private List<Object> highlightItemList;
    private boolean showTutorial;







    // getter and setters
    public boolean isShowTutorial() {
        return showTutorial;
    }

    public void setShowTutorial(boolean showTutorial) {
        this.showTutorial = showTutorial;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public List<Object> getHighlightItemList() {
        return highlightItemList;
    }

    public void setHighlightItemList(List<Object> highlightItemList) {
        this.highlightItemList = highlightItemList;
    }
}
