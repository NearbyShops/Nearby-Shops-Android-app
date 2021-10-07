package org.nearbyshops.whitelabelapp.Model.ModelEndPoints;



import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.Model.BannerImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class ShopEndPoint {

    private int itemCount;
    private int offset;
    private int limit;
    private int max_limit;
    private String isoCountryCode;

    private ArrayList<Shop> results;

    private List<ItemCategory> subcategories;
    private List<BannerImage> bannerImages;


    public String getIsoCountryCode() {
        return isoCountryCode;
    }

    public void setIsoCountryCode(String isoCountryCode) {
        this.isoCountryCode = isoCountryCode;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(int max_limit) {
        this.max_limit = max_limit;
    }

    public ArrayList<Shop> getResults() {
        return results;
    }

    public void setResults(ArrayList<Shop> results) {
        this.results = results;
    }

    public List<ItemCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<ItemCategory> subcategories) {
        this.subcategories = subcategories;
    }

    public List<BannerImage> getBannerImages() {
        return bannerImages;
    }

    public void setBannerImages(List<BannerImage> bannerImages) {
        this.bannerImages = bannerImages;
    }


}
