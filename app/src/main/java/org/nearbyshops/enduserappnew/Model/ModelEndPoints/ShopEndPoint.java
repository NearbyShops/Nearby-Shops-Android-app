package org.nearbyshops.enduserappnew.Model.ModelEndPoints;

import org.nearbyshops.enduserappnew.Model.Shop;

import java.util.ArrayList;

/**
 * Created by sumeet on 30/6/16.
 */
public class ShopEndPoint {

    Integer itemCount;
    Integer offset;
    Integer limit;
    Integer max_limit;
    ArrayList<Shop> results;


    public ArrayList<Shop> getResults() {
        return results;
    }

    public void setResults(ArrayList<Shop> results) {
        this.results = results;
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

    public Integer getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(Integer max_limit) {
        this.max_limit = max_limit;
    }
}
