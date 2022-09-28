package org.nearbyshops.whitelabelapp.Model.ModelEndPoints;

import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ShopItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class ShopItemEndPoint {



    private int itemCount;
    private int offset;
    private int limit;
    private int max_limit;
    private ArrayList<ShopItem> results;

    private List<ItemCategory> subcategories;


    private Item itemDetails;





    public Item getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(Item itemDetails) {
        this.itemDetails = itemDetails;
    }

    public List<ItemCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<ItemCategory> subcategories) {
        this.subcategories = subcategories;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public ArrayList<ShopItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<ShopItem> results) {
        this.results = results;
    }

    public Integer getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(Integer max_limit) {
        this.max_limit = max_limit;
    }
}
