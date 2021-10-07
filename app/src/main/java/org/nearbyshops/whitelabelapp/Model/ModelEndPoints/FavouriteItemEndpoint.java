package org.nearbyshops.whitelabelapp.Model.ModelEndPoints;


import org.nearbyshops.whitelabelapp.Model.ModelReviewItem.FavouriteItem;

import java.util.List;

/**
 * Created by sumeet on 9/8/16.
 */
public class FavouriteItemEndpoint {

    private int itemCount;
    private Integer offset;
    private Integer limit;
    private Integer max_limit;
    private List<FavouriteItem> results;


    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
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


    public List<FavouriteItem> getResults() {
        return results;
    }

    public void setResults(List<FavouriteItem> results) {
        this.results = results;
    }
}
