package org.nearbyshops.whitelabelapp.Model.ModelEndPoints;


import org.nearbyshops.whitelabelapp.Model.ModelReviewItem.ItemReview;

import java.util.List;

/**
 * Created by sumeet on 9/8/16.
 */
public class ItemReviewEndPoint {

    private Integer itemCount;
    private Integer offset;
    private Integer limit;
    private Integer max_limit;
    private List<ItemReview> results;


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


    public List<ItemReview> getResults() {
        return results;
    }

    public void setResults(List<ItemReview> results) {
        this.results = results;
    }
}
