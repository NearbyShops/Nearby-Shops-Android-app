package org.nearbyshops.enduserappnew.Model.ModelEndPoints;

import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.UtilityScreens.BannerSlider.Model.BannerImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class ShopEndPoint {

    Integer itemCount;
    Integer offset;
    Integer limit;
    Integer max_limit;
    ArrayList<Shop> results;

    private List<ItemCategory> subcategories;
    private List<BannerImage> bannerImages;



    // getter and setter methods

    public List<BannerImage> getBannerImages() {
        return bannerImages;
    }

    public void setBannerImages(List<BannerImage> bannerImages) {
        this.bannerImages = bannerImages;
    }

    public List<ItemCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<ItemCategory> subcategories) {
        this.subcategories = subcategories;
    }

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
