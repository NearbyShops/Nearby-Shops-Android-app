package org.nearbyshops.whitelabelapp.Model.ModelReviewItem;

import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;

import java.sql.Timestamp;

/**
 * Created by sumeet on 8/8/16.
 */
public class ItemReview {


    // Table Name
    public static final String TABLE_NAME = "ITEM_REVIEW";

    // column Names
    public static final String ITEM_REVIEW_ID = "ITEM_REVIEW_ID";
    public static final String ITEM_ID = "ITEM_ID";
    public static final String END_USER_ID = "END_USER_ID";
    public static final String RATING = "RATING";
    public static final String REVIEW_TEXT = "REVIEW_TEXT";
    public static final String REVIEW_DATE = "REVIEW_DATE";
    public static final String REVIEW_TITLE = "REVIEW_TITLE";

    // review_date, title


    // create Table statement
    public static final String createTableItemReviewPostgres =

            "CREATE TABLE IF NOT EXISTS " + ItemReview.TABLE_NAME + "("

            + " " + ItemReview.ITEM_REVIEW_ID + " SERIAL PRIMARY KEY,"
            + " " + ItemReview.ITEM_ID + " INT,"
            + " " + ItemReview.END_USER_ID + " INT,"
            + " " + ItemReview.RATING + " INT,"
            + " " + ItemReview.REVIEW_TEXT + " text,"
            + " " + ItemReview.REVIEW_TITLE + " text,"
            + " " + ItemReview.REVIEW_DATE + "  timestamp with time zone NOT NULL DEFAULT now(),"

            + " FOREIGN KEY(" + ItemReview.ITEM_ID +") REFERENCES " + Item.TABLE_NAME + "(" + Item.ITEM_ID + "),"
            + " FOREIGN KEY(" + ItemReview.END_USER_ID +") REFERENCES " + User.TABLE_NAME + "(" + User.USER_ID + "),"
            + " UNIQUE (" + ItemReview.ITEM_ID + "," + ItemReview.END_USER_ID + ")"
            + ")";



    // Instance Variables

    private Integer itemReviewID;
    private Integer itemID;
    private Integer endUserID;
    private Integer rating;
    private String reviewText;
    private String reviewTitle;
    private Timestamp reviewDate;

    private User rt_end_user_profile;
    private Integer rt_thanks_count;


    // getter and Setter Methods


    public Integer getRt_thanks_count() {
        return rt_thanks_count;
    }

    public void setRt_thanks_count(Integer rt_thanks_count) {
        this.rt_thanks_count = rt_thanks_count;
    }

    public Integer getItemReviewID() {
        return itemReviewID;
    }

    public void setItemReviewID(Integer itemReviewID) {
        this.itemReviewID = itemReviewID;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
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
