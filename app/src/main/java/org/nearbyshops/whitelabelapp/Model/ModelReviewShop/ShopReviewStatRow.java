package org.nearbyshops.whitelabelapp.Model.ModelReviewShop;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sumeet on 17/10/16.
 */
public class ShopReviewStatRow implements Parcelable{

    private int rating;
    private int reviews_count;

    public ShopReviewStatRow() {
    }

    protected ShopReviewStatRow(Parcel in) {
        rating = in.readInt();
        reviews_count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rating);
        dest.writeInt(reviews_count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShopReviewStatRow> CREATOR = new Creator<ShopReviewStatRow>() {
        @Override
        public ShopReviewStatRow createFromParcel(Parcel in) {
            return new ShopReviewStatRow(in);
        }

        @Override
        public ShopReviewStatRow[] newArray(int size) {
            return new ShopReviewStatRow[size];
        }
    };

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }
}
