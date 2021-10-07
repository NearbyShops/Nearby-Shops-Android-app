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


public class ViewHolderDeliveryProfile extends RecyclerView.ViewHolder{




    @BindView(R.id.list_item) ConstraintLayout listItem;
    @BindView(R.id.profile_picture) ImageView profileImage;
    @BindView(R.id.staff_user_id) TextView staffUserID;
    @BindView(R.id.name) TextView staffName;
    @BindView(R.id.user_role) TextView designation;
    @BindView(R.id.phone) TextView phone;



    private Context context;
    private User user;
    private Fragment fragment;
    private RecyclerView.Adapter adapter;





    public static ViewHolderDeliveryProfile create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_staff_new, parent, false);
        return new ViewHolderDeliveryProfile(view,context,fragment);
    }





    public ViewHolderDeliveryProfile(View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this,itemView);


        this.context = context;
        this.fragment = fragment;
    }





    @OnClick(R.id.list_item)
    void listItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemLongClick(user,getAdapterPosition());
        }

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




    public void setItem(User user)
    {
        this.user = user;


//        DeliveryGuyData permissions = user.getRt_delivery_guy_data();

        staffUserID.setText("Staff User ID : " + user.getUserID());
        staffName.setText(user.getName());
        designation.setText("Delivery Guy Self");
//        distance.setText("Distance : " + String.format("%.2f Km",permissions.getRt_distance()));
        phone.setText(user.getPhone());


        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);

        String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/User/Image/" + "five_hundred_"+ user.getProfileImagePath() + ".jpg";
        String image_url = PrefGeneral.getServerURL(context) + "/api/v1/User/Image/" + user.getProfileImagePath();


        Picasso.get()
                .load(imagePath)
                .placeholder(drawable)
                .into(profileImage);
    }







    public interface ListItemClick
    {
        void listItemClick(User user, int position);
        boolean listItemLongClick(User user, int position);
    }



}// ViewHolder Class declaration ends


