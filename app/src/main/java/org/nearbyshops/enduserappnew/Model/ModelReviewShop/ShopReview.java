package org.nearbyshops.enduserappnew.Model.ModelReviewShop;

import org.nearbyshops.enduserappnew.Model.ModelRoles.User;

import java.sql.Timestamp;

/**
 * Created by sumeet on 8/8/16.
 */
public class ShopReview{


    // Table Name
    public static final String TABLE_NAME = "SHOP_REVIEW";

    // column Names
    public static final String SHOP_REVIEW_ID = "SHOP_REVIEW_ID";
    public static final String SHOP_ID = "SHOP_ID";
    public static final String END_USER_ID = "END_USER_ID";
    public static final String RATING = "RATING";
    public static final String REVIEW_TEXT = "REVIEW_TEXT";
    public static final String REVIEW_DATE = "REVIEW_DATE";
    public static final String REVIEW_TITLE = "REVIEW_TITLE";

    // review_date, title


    // Instance Variables

    private int shopReviewID;
    private int shopID;
    private int endUserID;
    private int rating;
    private String reviewText;
    private String reviewTitle;
    private Timestamp reviewDate;

    private User rt_end_user_profile;
    private int rt_thanks_count;



    // getter and setter methods

    public int getRt_thanks_count() {
        return rt_thanks_count;
    }

    public void setRt_thanks_count(int rt_thanks_count) {
        this.rt_thanks_count = rt_thanks_count;
    }

    public Integer getShopReviewID() {
        return shopReviewID;
    }

    public void setShopReviewID(Integer shopReviewID) {
        this.shopReviewID = shopReviewID;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(Integer endUserID) {
        this.endUserID = endUserID;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }


    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }


    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }


    public User getRt_end_user_profile() {
        return rt_end_user_profile;
    }

    public void setRt_end_user_profile(User rt_end_user_profile) {
        this.rt_end_user_profile = rt_end_user_profile;
    }
}
