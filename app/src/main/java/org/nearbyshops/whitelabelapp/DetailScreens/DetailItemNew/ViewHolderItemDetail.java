package org.nearbyshops.whitelabelapp.DetailScreens.DetailItemNew;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderItemDetail extends RecyclerView.ViewHolder{




    @BindView(R.id.item_name) TextView itemName;

    @BindView(R.id.item_rating_numeric) TextView itemRatingNumeric;
    @BindView(R.id.item_rating) RatingBar itemRating;
    @BindView(R.id.rating_count) TextView ratingCount;


    @BindView(R.id.item_description) TextView itemDescription;
    @BindView(R.id.read_full_button) TextView readFullButton;


    private Context context;
    private Item item;
    private Fragment fragment;


    public static ViewHolderItemDetail create(ViewGroup parent, Context context, Fragment fragment) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_detail, parent, false);
        return new ViewHolderItemDetail(view, context, fragment);
    }





    public ViewHolderItemDetail(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


    }







    public void setItem(Item item) {
        this.item = item;


        itemName.setText(this.item.getItemName());

//        itemRatingNumeric.setText(String.format("%.2f",item.getRt_rating_avg()));
//        itemRating.setRating(item.getRt_rating_avg());
//        ratingCount.setText("(" + item.getRt_rating_count() + " ratings )");

        itemDescription.setText(this.item.getItemDescriptionLong());


        if(item.getItemDescriptionLong()==null || item.getItemDescriptionLong().equals(""))
        {
            readFullButton.setVisibility(View.GONE);
        }



        if(this.item.getRt_rating_count()==0)
        {
            itemRatingNumeric.setText(" New ");
            itemRatingNumeric.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));

            ratingCount.setVisibility(View.GONE);
            itemRating.setVisibility(View.GONE);
        }
        else
        {
            itemRatingNumeric.setText(String.format("%.2f", this.item.getRt_rating_avg()));
            itemRatingNumeric.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));

            ratingCount.setText("( " + (int) this.item.getRt_rating_count() + " Ratings )");

            ratingCount.setVisibility(View.VISIBLE);
            itemRating.setVisibility(View.VISIBLE);
        }

    }




    @OnClick(R.id.read_full_button)
    void readFullButtonClick() {

        itemDescription.setMaxLines(Integer.MAX_VALUE);
        readFullButton.setVisibility(View.GONE);
    }




}
