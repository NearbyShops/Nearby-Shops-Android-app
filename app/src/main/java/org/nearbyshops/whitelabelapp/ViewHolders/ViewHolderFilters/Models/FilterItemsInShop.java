package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models;

public class FilterItemsInShop {

    String headerText;

    boolean hideFilters;



    public boolean isHideFilters() {
        return hideFilters;
    }

    public void setHideFilters(boolean hideFilters) {
        this.hideFilters = hideFilters;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }
}
