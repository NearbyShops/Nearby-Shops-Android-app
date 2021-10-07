package org.nearbyshops.whitelabelapp.DetailScreens.DetailShop;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hsalf.smilerating.SmileRating;
import com.squareup.picasso.Picasso;
import okhttp3.ResponseBody;

import org.nearbyshops.whitelabelapp.API.ShopReviewService;
import org.nearbyshops.whitelabelapp.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.Interfaces.NotifyReviewUpdate;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.text.SimpleDateFormat;



/**
 * Created by sumeet on 12/8/16.
 */




public class RateReviewDialog extends DialogFragment {


    @BindView(R.id.dialog_dismiss_icon)
    ImageView dismiss_dialog_button;

    @BindView(R.id.submit_button)
    TextView submit_button;

    @BindView(R.id.cancel_button)
    TextView cancel_button;

    @BindView(R.id.review_text)
    EditText review_text;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.review_title)
    TextView review_title;

    @BindView(R.id.member_name)
    TextView member_name;

    @BindView(R.id.member_profile_image)
    ImageView member_profile_image;


    @BindView(R.id.item_rating_text)
    TextView itemRatingText;


    @BindView(R.id.smile_rating)
    SmileRating smileRating;

    @BindView(R.id.member_rating) RatingBar memberRatingIndicator;


    int book_id;


    ShopReview review_for_edit;
    boolean isModeEdit = false;


    @Inject
    ShopReviewService bookReviewService;


//    @Inject
//    UserService endUserService;


    public RateReviewDialog() {
        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



//    Unbinder unbinder;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_rate_review_shop, container);

        ButterKnife.bind(this,view);

        if(isModeEdit && review_for_edit!=null)
        {
            submit_button.setText("Update");
            cancel_button.setText("Delete");

            review_title.setText(review_for_edit.getReviewTitle());
            review_text.setText(review_for_edit.getReviewText());

            member_name.setText(" by : " + review_for_edit.getRt_end_user_profile().getName());

            ratingBar.setRating(review_for_edit.getRating());
//            smileRating.setSelectedSmile(review_for_edit.getRating());
            memberRatingIndicator.setRating(review_for_edit.getRating());
            smileRating.setSelectedSmile(review_for_edit.getRating()-1);
            itemRatingText.setText((float) review_for_edit.getRating() + " Star");

//            String imagePath = PrefGeneral.getImageEndpointURL(getActivity())
//                    + review_for_edit.getRt_end_user_profile().getProfileImagePath();



            String imagepath = PrefGeneral.getServerURL(MyApplication.getAppContext()) + "/api/v1/User/Image/five_hundred_"
                    + review_for_edit.getRt_end_user_profile().getProfileImagePath() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_nature_people_white_48px, getActivity().getTheme());

            Picasso.get()
                    .load(imagepath)
                    .placeholder(placeholder)
                    .into(member_profile_image);
        }
        else
        {

            ratingBar.setRating(3);
//            smileRating.setSelectedSmile(review_for_edit.getRating());
            smileRating.setSelectedSmile(3-1);
            itemRatingText.setText((float) 3 + " Star");
            memberRatingIndicator.setRating(3);


        }



//
//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
////                itemRatingText.setText(String.format("%.0f",rating));
//                itemRatingText.setText(String.valueOf(rating));
//            }
//        });

//
//        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
//            @Override
//            public void onRatingSelected(int level, boolean reselected) {
//
//                itemRatingText.setText(String.valueOf(rating));
//            }
//        });


        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {

                itemRatingText.setText((smiley + 1) + " Star");
                memberRatingIndicator.setRating(smiley + 1);
            }
        });


        if(!isModeEdit)
        {
            setMember();
        }

//        setMember();


//        dismiss_dialog_button = (ImageView) view.findViewById(R.id.dialog_dismiss_icon);

        return view;
    }




    @OnClick(R.id.dialog_dismiss_icon)
    void dismiss_dialog()
    {
        dismiss();
        showToastMessage("Dismissed !");
    }





    private void cancel_button()
    {
        dismiss();
        showToastMessage("Cancelled !");
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }





    private void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }






    public void setMode(ShopReview reviewForUpdate, boolean isModeEdit, int book_id)
    {

        this.book_id = book_id;
        review_for_edit = reviewForUpdate;
        this.isModeEdit = isModeEdit;
    }









    private void setMember()
    {

        User endUser = PrefLogin.getUser(getActivity());


        if(endUser!=null)
        {
            member_name.setText(" by " + endUser.getName());


//            String imagePath = PrefGeneral.getImageEndpointURL(getActivity())
//                    + endUser.getProfileImagePath();




            String imagepath = PrefGeneral.getServerURL(MyApplication.getAppContext()) + "/api/v1/User/Image/five_hundred_"
                    + endUser.getProfileImagePath() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_nature_people_white_48px, null);

            Picasso.get()
                    .load(imagepath)
                    .placeholder(placeholder)
                    .into(member_profile_image);
        }


    }






    @OnClick(R.id.submit_button)
    void update_submit_click()
    {

        if(isModeEdit)
        {
            updateReview();
        }else
        {
            submitReview();
        }
    }





    @OnClick(R.id.cancel_button)
    void cancel_delete_click()
    {

//        showToastMessage("Delete Click !");


        if(isModeEdit)
        {
            // delete the review

            Call<ResponseBody> call = bookReviewService.deleteShopReview(review_for_edit.getShopReviewID());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {
                        showToastMessage("Deleted !");


                        if(getParentFragment() instanceof NotifyReviewUpdate)
                        {
                            ((NotifyReviewUpdate)getParentFragment()).notifyReviewDeleted();
                        }

                        dismiss();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
        else
        {
            cancel_button();
        }

    }





    private void submitReview()
    {
        ShopReview bookReview = new ShopReview();
//        bookReview.setReviewDate(new java.sql.Date(System.currentTimeMillis()));
        bookReview.setRating(smileRating.getRating());
        bookReview.setReviewTitle(review_title.getText().toString());
        bookReview.setReviewText(review_text.getText().toString());
        bookReview.setShopID(book_id);
        bookReview.setEndUserID(PrefLogin.getUser(getActivity()).getUserID());

        Call<ShopReview> call = bookReviewService.insertShopReview(bookReview);

        call.enqueue(new Callback<ShopReview>() {
            @Override
            public void onResponse(Call<ShopReview> call, Response<ShopReview> response) {

                if(response.code()==201)
                {
                    showToastMessage("Submitted !");

                    if(getParentFragment() instanceof NotifyReviewUpdate)
                    {
                        ((NotifyReviewUpdate)getParentFragment()).notifyReviewSubmitted();
                    }

                    dismiss();

                }
            }

            @Override
            public void onFailure(Call<ShopReview> call, Throwable t) {


                showToastMessage("Failed !");

            }
        });
    }






    private void updateReview()
    {
        if(review_for_edit!=null)
        {

            review_for_edit.setRating(smileRating.getRating());
            review_for_edit.setReviewTitle(review_title.getText().toString());
            review_for_edit.setReviewText(review_text.getText().toString());


            long date = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(date);

//            review_for_edit.setReviewDate(new java.sql.Date(date));

            Call<ResponseBody> call = bookReviewService.updateShopReview(review_for_edit,review_for_edit.getShopReviewID());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code()==200)
                    {
                        showToastMessage(getString(R.string.udate_successful_api_response));

                        if(getParentFragment() instanceof NotifyReviewUpdate)
                        {
                            ((NotifyReviewUpdate)getParentFragment()).notifyReviewUpdated();
                        }

                        dismiss();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage(getString(R.string.api_response_no_item_updated));
                }
            });

        }
    }



}
