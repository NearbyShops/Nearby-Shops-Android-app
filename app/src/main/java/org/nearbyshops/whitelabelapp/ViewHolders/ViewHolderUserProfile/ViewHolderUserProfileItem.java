package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUserProfile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.MyApplication;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderUserProfileItem extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {



    @BindView(R.id.list_item) LinearLayout listItem;
    @BindView(R.id.profile_image) ImageView profileImage;

    @BindView(R.id.user_role_badge) TextView userRoleBadge;
    @BindView(R.id.user_name) TextView nameOfUser;
    @BindView(R.id.user_id) TextView userID;
    @BindView(R.id.phone) TextView phone;

    @BindView(R.id.account_creation_date) TextView accountCreationDate;

    @BindView(R.id.more_options) ImageView moreOptions;


    ViewModelUser viewModelUser;




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

        setupViewModel();
    }







    public void setItem(User user, boolean moreOptionsEnabled)
    {


        if(moreOptionsEnabled)
        {
            moreOptions.setVisibility(View.VISIBLE);
        }
        else
        {
            moreOptions.setVisibility(View.INVISIBLE);
        }




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
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE)
        {
            userRoleBadge.setText("Delivery Guy");
            userRoleBadge.setBackgroundColor(ContextCompat.getColor(context,R.color.blueGrey800));
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE)
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


        String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/User/Image/" + "five_hundred_"+ user.getProfileImagePath() + ".jpg";
        String image_url = PrefGeneral.getServerURL(context) + "/api/v1/User/Image/" + user.getProfileImagePath();


        Picasso.get()
                .load(imagePath)
                .placeholder(drawable)
                .into(profileImage);
    }





    @OnClick(R.id.list_item)
    void listItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick)fragment).listItemClick(user,getAdapterPosition());
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




    @OnClick(R.id.more_options)
    void optionsOverflowClick(View v)
    {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.more_options_user_list_item, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }




    @Override
    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.action_remove:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);


                builder.setTitle("Confirm Delete User !")
                        .setMessage("Are you sure you want to delete this User ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                viewModelUser.deleteUser(user.getUserID());


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                showToastMessage("Cancelled !");

                            }
                        })
                        .show();
                break;


            case R.id.action_edit:


                if(fragment instanceof ListItemClick)
                {
                    ((ListItemClick)fragment).listItemClick(user,getAdapterPosition());
                }

                break;



            default:

                break;

        }




        return false;

    }





    private void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(context,message);
    }




    public interface ListItemClick
    {
//        void listItemClick(User user);
        void listItemClick(User user, int position);
        void userDeleted(User user, int position);
    }



    void setupViewModel()
    {
        viewModelUser = new ViewModelUser(MyApplication.application);


        viewModelUser.getEvent().observe(fragment, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {


                if(integer == ViewModelUser.EVENT_delete_success)
                {
                    if(fragment instanceof ListItemClick)
                    {
                        ((ListItemClick) fragment).userDeleted(user,getLayoutPosition());
                    }
                }

            }
        });


        viewModelUser.getMessage().observe(fragment, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                showToastMessage(s);
            }
        });
    }

}

