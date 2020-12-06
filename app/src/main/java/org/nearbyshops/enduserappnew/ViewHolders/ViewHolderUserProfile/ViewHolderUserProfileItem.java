package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderUserProfileItem extends RecyclerView.ViewHolder{



    @BindView(R.id.list_item) LinearLayout listItem;
    @BindView(R.id.profile_image) ImageView profileImage;

    @BindView(R.id.user_role_badge) TextView userRoleBadge;
    @BindView(R.id.user_name) TextView nameOfUser;
    @BindView(R.id.user_id) TextView userID;
    @BindView(R.id.phone) TextView phone;

    @BindView(R.id.account_creation_date) TextView accountCreationDate;



    private Context context;
    private User user;
    private Fragment fragment;




    public static ViewHolderUserProfileItem create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user_profile_new,parent,false);
        return new ViewHolderUserProfileItem(view,context,fragment);
    }





    public ViewHolderUserProfileItem(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }







    public void setItem(User user)
    {

        this.user = user;


        if(user.getRole()==User.ROLE_END_USER_CODE)
        {
            userRoleBadge.setText("EndUser");
            userRoleBadge.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_1));
        }
        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
        {
            userRoleBadge.setText("Shop Admin");
            userRoleBadge.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
        }
        else if(user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {
            userRoleBadge.setText("Shop Staff");
            userRoleBadge.setBackgroundColor(ContextCompat.getColor(context,R.color.phonographyBlue));
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_CODE)
        {
            userRoleBadge.setText("Delivery Guy");
            userRoleBadge.setBackgroundColor(ContextCompat.getColor(context,R.color.blueGrey800));
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_SELF_CODE)
        {
            userRoleBadge.setText("Delivery Guy Shop");
            userRoleBadge.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
        }
        else if(user.getRole()==User.ROLE_STAFF_CODE)
        {
            userRoleBadge.setText("Staff");
            userRoleBadge.setBackgroundColor(ContextCompat.getColor(context,R.color.orangeDark));
        }
        else if(user.getRole()==User.ROLE_ADMIN_CODE)
        {
            userRoleBadge.setText("Admin");
            userRoleBadge.setBackgroundColor(ContextCompat.getColor(context,R.color.mapbox_blue));
        }



        accountCreationDate.setText(user.getTimestampCreated().toLocaleString());


        userID.setText("User ID : " + user.getUserID());
        nameOfUser.setText(user.getName());
        phone.setText(user.getPhone());



        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);


        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/User/Image/" + "five_hundred_"+ user.getProfileImagePath() + ".jpg";
        String image_url = PrefGeneral.getServiceURL(context) + "/api/v1/User/Image/" + user.getProfileImagePath();

        String imagePathSDS = PrefServiceConfig.getServiceURL_SDS(context) + "/api/v1/User/Image/" + "five_hundred_"+ user.getProfileImagePath() + ".jpg";




        Picasso.get()
                .load(imagePathSDS)
                .placeholder(drawable)
                .into(profileImage);
    }





    @OnClick(R.id.list_item)
    void listItemClick()
    {
        ((ListItemClick)fragment).listItemClick(user,getAdapterPosition());
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
//        void listItemClick(User user);
        void listItemClick(User user, int position);
    }



}

