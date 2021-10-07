package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderDeprecated;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderShopStaff extends RecyclerView.ViewHolder{



    @BindView(R.id.list_item)
    ConstraintLayout listItem;
    @BindView(R.id.profile_picture) ImageView profileImage;
    @BindView(R.id.staff_user_id) TextView staffUserID;
    @BindView(R.id.name) TextView staffName;
//    @BindView(R.id.designation) TextView designation;
    @BindView(R.id.phone) TextView phone;
    @BindView(R.id.distance) TextView distance;



    private Context context;
    private User user;
    private Fragment fragment;
    private RecyclerView.Adapter adapter;



    public static ViewHolderShopStaff create(ViewGroup parent, Context context, Fragment fragment, RecyclerView.Adapter adapter)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_staff_new, parent, false);
        return new ViewHolderShopStaff(view,context,fragment,adapter);
    }






    public ViewHolderShopStaff(View itemView, Context context, Fragment fragment, RecyclerView.Adapter adapter) {
        super(itemView);

        ButterKnife.bind(this,itemView);


        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
    }






//    @OnLongClick(R.id.list_item)
//    boolean listItemLongClick(View v) {
//
//        notificationReceiver.notifyTripRequestSelected();
//        notifyItemChanged(getLayoutPosition());
//
//
//        return notificationReceiver.listItemLongClick(v,
//                (User) dataset.get(getLayoutPosition()),
//                getLayoutPosition()
//        );
//    }



    public void setItem(User user)
    {



//            ShopStaffPermissions permissions = user.getRt_shop_staff_permissions();

            staffUserID.setText("Staff User ID : " + user.getUserID());
            staffName.setText(user.getName());
//            designation.setText(permissions.getDesignation());
//            distance.setText("Distance : " + String.format("%.2f Km",permissions.getRt_distance()));
            phone.setText(user.getPhone());




            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);

            String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/User/Image/" + "five_hundred_"+ user.getProfileImagePath() + ".jpg";
            String image_url = PrefGeneral.getServerURL(context) + "/api/v1/User/Image/" + user.getProfileImagePath();


            Picasso.get()
                    .load(imagePath)
                    .placeholder(drawable)
                    .into(profileImage);

    }




    @OnClick(R.id.list_item)
    void listItemLongClick()
    {


        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemClick(user,getAdapterPosition());
        }
    }





    @OnClick(R.id.phone)
    void phoneClick()
    {
        dialPhoneNumber(phone.getText().toString());
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }







    public interface ListItemClick
    {
        // method for notifying the list object to request sub category

    //        void currentItemClick(Item item, int position);
    //        void itemUpdateClick(ItemSubmission itemSubmission, int position);
    //        void imageUpdatedClick(ItemImage itemImage, int position);

        void notifyTripRequestSelected();
        void listItemClick(User user, int position);
        boolean listItemLongClick(View view, User user, int position);
    }


}// ViewHolder Class declaration ends



