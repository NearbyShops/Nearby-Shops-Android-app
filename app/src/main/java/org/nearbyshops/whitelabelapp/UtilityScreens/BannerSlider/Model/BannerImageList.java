package org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.Model;

import java.util.List;


public class BannerImageList {



    // Zero Commission | Lowest Price | Flexible Fares - No Package Restrictions | Multiple Destinations | Share Cab With Friends |

    //

    private List<BannerImage> bannerImageList;

    private String listTitle;



    // getter and setters


    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public List<BannerImage> getBannerImageList() {
        return bannerImageList;
    }

    public void setBannerImageList(List<BannerImage> bannerImageList) {
        this.bannerImageList = bannerImageList;
    }
}
