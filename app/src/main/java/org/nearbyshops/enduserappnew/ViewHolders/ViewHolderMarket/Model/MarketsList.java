package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.Model;


import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationGlobal;

import java.util.List;

public class MarketsList {


    private String listTitle;
    private List<ServiceConfigurationGlobal> dataset;


    // constructors

    public MarketsList() {
    }

    public MarketsList(String listTitle, List<ServiceConfigurationGlobal> dataset) {
        this.listTitle = listTitle;
        this.dataset = dataset;
    }


    // getter and setters


    public List<ServiceConfigurationGlobal> getDataset() {
        return dataset;
    }

    public void setDataset(List<ServiceConfigurationGlobal> dataset) {
        this.dataset = dataset;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }
}
