package org.nearbyshops.enduserappnew.ViewHolders.Model;

import org.nearbyshops.enduserappnew.Model.ItemCategory;

import java.util.List;



public class ItemCategoriesList {

    private List<ItemCategory> itemCategories;

    private Integer selectedCategoryID;
    private int scrollPositionForSelected;




    public int getScrollPositionForSelected() {
        return scrollPositionForSelected;
    }

    public void setScrollPositionForSelected(int scrollPositionForSelected) {
        this.scrollPositionForSelected = scrollPositionForSelected;
    }

    public Integer getSelectedCategoryID() {
        return selectedCategoryID;
    }

    public void setSelectedCategoryID(Integer selectedCategoryID) {
        this.selectedCategoryID = selectedCategoryID;
    }

    public List<ItemCategory> getItemCategories() {
        return itemCategories;
    }

    public void setItemCategories(List<ItemCategory> itemCategories) {
        this.itemCategories = itemCategories;
    }
}
