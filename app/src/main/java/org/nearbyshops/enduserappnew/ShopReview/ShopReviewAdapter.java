package org.nearbyshops.enduserappnew.ShopReview;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import okhttp3.ResponseBody;

import org.nearbyshops.enduserappnew.API.ShopReviewThanksService;
import org.nearbyshops.enduserappnew.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.enduserappnew.Model.ModelReviewShop.ShopReviewThanks;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopReview.Interfaces.NotifyLoginByAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by sumeet on 19/12/15.
 */


public class ShopReviewAdapter extends RecyclerView.Adapter<ShopReviewAdapter.ViewHolder>{


    private List<ShopReview> dataset;
    private AppCompatActivity context;
    private Map<Integer, ShopReviewThanks> thanksMap;

    @Inject
    ShopReviewThanksService thanksService;



    public ShopReviewAdapter(List<ShopReview> dataset, Map<Integer, ShopReviewThanks> thanksMap, AppCompatActivity context) {

        this.dataset = dataset;
        this.context = context;
        this.thanksMap = thanksMap;

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    public ShopReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_review,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ShopReviewAdapter.ViewHolder holder, final int position) {

        holder.rating.setRating(dataset.get(position).getRating());
        holder.review_date.setText(dataset.get(position).getReviewDate().toLocaleString());


        holder.review_title.setText(dataset.get(position).getReviewTitle());
        holder.review_text.setText(dataset.get(position).getReviewText());

        holder.thanksCount.setText("(" + dataset.get(position).getRt_thanks_count() + ")");

        if(thanksMap!=null)
        {
            if(thanksMap.containsKey(dataset.get(position).getShopReviewID()))
            {
                holder.thanksButton.setText("Thanked !");
                holder.thanksButton.setTextColor(ContextCompat.getColor(context, R.color.phonographyBlue));
            }
            else
            {
                holder.thanksButton.setText("Thanks");
                holder.thanksButton.setTextColor(ContextCompat.getColor(context, R.color.grey800));
            }
        }


//        String imagePath = UtilityGeneral.getImageEndpointURL(context)
//                + dataset.get(position).getRt_end_user_profile().getProfileImageURL();



        User endUser = dataset.get(position).getRt_end_user_profile();


        if(endUser!=null)
        {
            holder.member_name.setText(endUser.getName());
            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/User/Image/" + dataset.get(position).getRt_end_user_profile().getProfileImagePath();

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.profile_image);
        }



    }


    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.member_name)
        TextView member_name;

        @BindView(R.id.rating)
        RatingBar rating;

        @BindView(R.id.review_date)
        TextView review_date;

        @BindView(R.id.review_title)
        TextView review_title;

        @BindView(R.id.review_text)
        TextView review_text;

        @BindView(R.id.thanks_button)
        TextView thanksButton;

        @BindView(R.id.thanks_count)
        TextView thanksCount;

        @BindView(R.id.list_item_shop_review)
        RelativeLayout listItem;



        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }



        @OnClick({R.id.thanks_button, R.id.thanks_count})
        void listItemClick()
        {
            final ShopReview shopReview = dataset.get(getAdapterPosition());
            User endUser = PrefLogin.getUser(context);

            if(endUser==null)
            {
//                showToastMessage("Please LoginUsingOTP to use this feature !");

                if(context instanceof NotifyLoginByAdapter)
                {
                    ((NotifyLoginByAdapter)context).NotifyLoginAdapter();
                }

                return;
            }



            if(thanksMap.containsKey(shopReview.getShopReviewID()))
            {
                Call<ResponseBody> deleteCall = thanksService.deleteThanks(shopReview.getShopReviewID(),endUser.getUserID());

                deleteCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if(response.code() ==200)
                        {
                            // delete successful
                            shopReview.setRt_thanks_count(shopReview.getRt_thanks_count()-1);
                            thanksMap.remove(shopReview.getShopReviewID());
                            notifyItemChanged(getLayoutPosition());
//                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });


            }
            else
            {
                ShopReviewThanks shopReviewThanks = new ShopReviewThanks();
                shopReviewThanks.setEndUserID(endUser.getUserID());
                shopReviewThanks.setShopReviewID(dataset.get(getAdapterPosition()).getShopReviewID());

                Call<ShopReviewThanks> insertCall = thanksService.insertThanks(shopReviewThanks);

                insertCall.enqueue(new Callback<ShopReviewThanks>() {
                    @Override
                    public void onResponse(Call<ShopReviewThanks> call, Response<ShopReviewThanks> response) {


                        if(response.code() ==201)
                        {
                            // insert Successful
                            shopReview.setRt_thanks_count(shopReview.getRt_thanks_count()+1);
                            thanksMap.put(response.body().getShopReviewID(),response.body());

                            notifyItemChanged(getLayoutPosition());
//                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ShopReviewThanks> call, Throwable t) {

                    }
                });
            }

        }



    }// ViewHolder Class declaration ends





    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}