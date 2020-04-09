package org.nearbyshops.enduserappnew.ShopReview;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.nearbyshops.enduserappnew.API.ShopReviewService;
import org.nearbyshops.enduserappnew.Model.ModelReviewShop.ShopReviewStatRow;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by sumeet on 25/10/16.
 */

public class ShopReviewStats extends Fragment {



    Shop shop;


    ShopReviewStatRow[] statListNormalized = new ShopReviewStatRow[10];

    @Inject
    ShopReviewService shopReviewService;


    @BindView(R.id.ten_star_bar_color)
    TextView bar_ten_star;

    @BindView(R.id.nine_star_bar_color)
    TextView bar_nine_star;

    @BindView(R.id.eight_star_bar_color)
    TextView bar_eight_star;

    @BindView(R.id.seven_star_bar_color)
    TextView bar_seven_star;

    @BindView(R.id.six_star_bar_color)
    TextView bar_six_star;

    @BindView(R.id.five_star_bar_color)
    TextView bar_five_star;

    @BindView(R.id.four_star_bar_color)
    TextView bar_four_star;

    @BindView(R.id.three_star_bar_color)
    TextView bar_three_star;

    @BindView(R.id.two_star_bar_color)
    TextView bar_two_star;

    @BindView(R.id.one_star_bar_color)
    TextView bar_one_star;




    public ShopReviewStats() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.review_stats_chart,container,false);
        ButterKnife.bind(this,view);
        shop = getArguments().getParcelable("shopExtra");



        if(savedInstanceState==null)
        {
            initializeList();
            makeNetworkCall();
        }
        else
        {
            onViewStateRestored(savedInstanceState);
            showResults();
        }


        return view;
    }


/*
    void setShop(Shop notifiedShop)
    {
        this.shop = notifiedShop;

    }*/


    void makeNetworkCall()
    {


        Call<List<ShopReviewStatRow>> call = shopReviewService.getStats(shop.getShopID());

        call.enqueue(new Callback<List<ShopReviewStatRow>>() {
            @Override
            public void onResponse(Call<List<ShopReviewStatRow>> call, Response<List<ShopReviewStatRow>> response) {

                if(response.body()!=null)
                {
                    showResults(response.body());

                    Log.d("ShopStats",String.valueOf(response.body().size()));

                }

            }

            @Override
            public void onFailure(Call<List<ShopReviewStatRow>> call, Throwable t) {

            }
        });

    }



    void initializeList()
    {

        for(int i = 0; i<10 ; i = i + 1)
        {
            ShopReviewStatRow row = new ShopReviewStatRow();
            row.setRating(i+1);
            row.setReviews_count(0);

            statListNormalized[i] = row;
        }
    }




//    @State
    int reviews_count_total = 0;


    void showResults(List<ShopReviewStatRow> listStats)
    {
        for(ShopReviewStatRow row : listStats)
        {
            reviews_count_total = reviews_count_total + row.getReviews_count();
//            statListNormalized[row.getRating()-1].setReviews_count(row.getReviews_count());
        }

        showResults();

//        bar_one_star.setLayoutParams(new LinearLayout.LayoutParams(75,15));
//        bar_two_star.setLayoutParams(new LinearLayout.LayoutParams((statListNormalized[1].getReviews_count()/reviews_count_total) * 150,15));
//
//        bar_three_star.setLayoutParams(new LinearLayout.LayoutParams((statListNormalized[2].getReviews_count()/reviews_count_total) * 150,15));
//        bar_four_star.setLayoutParams(new LinearLayout.LayoutParams((statListNormalized[3].getReviews_count()/reviews_count_total) * 150,15));
//        bar_five_star.setLayoutParams(new LinearLayout.LayoutParams(((statListNormalized[4].getReviews_count()/reviews_count_total) * 150),15));
//        bar_six_star.setLayoutParams(new LinearLayout.LayoutParams((statListNormalized[5].getReviews_count()/reviews_count_total) * 150,15));
//        bar_seven_star.setLayoutParams(new LinearLayout.LayoutParams((statListNormalized[6].getReviews_count()/reviews_count_total) * 150,15));
//
//        bar_eight_star.setLayoutParams(new LinearLayout.LayoutParams(((statListNormalized[7].getReviews_count()/reviews_count_total) * 150),15));
//        bar_nine_star.setLayoutParams(new LinearLayout.LayoutParams((statListNormalized[8].getReviews_count()/reviews_count_total) * 150,15));
//        bar_ten_star.setLayoutParams(new LinearLayout.LayoutParams(((statListNormalized[9].getReviews_count()/reviews_count_total) * 150),15));



/*
        bar_three_star.setWidth(((statListNormalized[2].getReviews_count()/reviews_count_total) * 150));
        bar_four_star.setWidth(((statListNormalized[3].getReviews_count()/reviews_count_total) * 150));
        bar_five_star.setWidth(((statListNormalized[4].getReviews_count()/reviews_count_total) * 150));

        bar_six_star.setWidth(((statListNormalized[5].getReviews_count()/reviews_count_total) * 150));
        bar_seven_star.setWidth(((statListNormalized[6].getReviews_count()/reviews_count_total) * 150));
        bar_eight_star.setWidth(((statListNormalized[7].getReviews_count()/reviews_count_total) * 150));
        bar_nine_star.setWidth(((statListNormalized[8].getReviews_count()/reviews_count_total) * 150));
        bar_ten_star.setWidth(((statListNormalized[9].getReviews_count()/reviews_count_total) * 150));*/


    }



    void showResults()
    {

        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        // displaying results

//        bar_one_star.setText("Bar One ");

        double value_bar_one = (((double)statListNormalized[0].getReviews_count()/reviews_count_total) * 150);
        double value_bar_two = (((double)statListNormalized[1].getReviews_count()/reviews_count_total) * 150);
        double value_bar_three = (((double)statListNormalized[2].getReviews_count()/reviews_count_total) * 150);
        double value_bar_four = (((double)statListNormalized[3].getReviews_count()/reviews_count_total) * 150);
        double value_bar_five = (((double)statListNormalized[4].getReviews_count()/reviews_count_total) * 150);
        double value_bar_six = (((double)statListNormalized[5].getReviews_count()/reviews_count_total) * 150);
        double value_bar_seven = (((double)statListNormalized[6].getReviews_count()/reviews_count_total) * 150);
        double value_bar_eight = (((double)statListNormalized[7].getReviews_count()/reviews_count_total) * 150);
        double value_bar_nine = (((double)statListNormalized[8].getReviews_count()/reviews_count_total) * 150);
        double value_bar_ten = (((double)statListNormalized[9].getReviews_count()/reviews_count_total) * 150);


        Log.d("ShopStats", "Value for Bar eight : " + statListNormalized[7].getReviews_count());
        Log.d("ShopStats", "Reviews Count Total : " + reviews_count_total);

        Log.d("ShopStats", "Division : " + (double) statListNormalized[7].getReviews_count() / reviews_count_total);
        Log.d("ShopStats", "Division Bar Eight: " + value_bar_eight);

//        bar_one_star.getLayoutParams().width = value_bar_one;


        bar_one_star.getLayoutParams().width = (int)(value_bar_one * metrics.density);
        bar_two_star.getLayoutParams().width = (int)(value_bar_two * metrics.density);

        bar_three_star.getLayoutParams().width = (int)(value_bar_three * metrics.density);
        bar_four_star.getLayoutParams().width = (int)(value_bar_four * metrics.density);
        bar_five_star.getLayoutParams().width = (int)(value_bar_five * metrics.density);
        bar_six_star.getLayoutParams().width = (int)(value_bar_six * metrics.density);
        bar_seven_star.getLayoutParams().width = (int)(value_bar_seven * metrics.density);
        bar_eight_star.getLayoutParams().width = (int)(value_bar_eight * metrics.density);
        bar_nine_star.getLayoutParams().width = (int)(value_bar_nine * metrics.density);
        bar_ten_star.getLayoutParams().width = (int)(value_bar_ten * metrics.density);


    }



}
