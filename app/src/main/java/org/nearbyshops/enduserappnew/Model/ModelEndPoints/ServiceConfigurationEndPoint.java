package org.nearbyshops.enduserappnew.Model.ModelEndPoints;



import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class ServiceConfigurationEndPoint {


    private int itemCount;
    private int offset;
    private int limit;
    private int max_limit;
    private List<Market> savedMarkets;
    private ArrayList<Market> results;



    // getter and setter methods


    public List<Market> getSavedMarkets() {
        return savedMarkets;
    }

    public void setSavedMarkets(List<Market> savedMarkets) {
        this.savedMarkets = savedMarkets;
    }

    public ArrayList<Market> getResults() {
        return results;
    }

    public void setResults(ArrayList<Market> results) {
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
