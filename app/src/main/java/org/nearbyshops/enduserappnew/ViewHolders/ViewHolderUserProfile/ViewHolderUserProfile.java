package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.EditProfile;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.enduserappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

public class ViewHolderUserProfile extends RecyclerView.ViewHolder {


    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.user_id) TextView userID;
    @BindView(R.id.user_name) TextView userName;
//    @BindView(R.id.phone) TextView phone;



    private Context context;
    private Fragment fragment;




    public static ViewHolderUserProfile create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user_profile,parent,false);

        return new ViewHolderUserProfile(view,context,fragment);
    }




    public ViewHolderUserProfile(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);

        this.context = context;
        this.fragment = fragment;

        ButterKnife.bind(this,itemView);
    }



    public void setItem(User user)
    {

        userID.setText("User ID : " + user.getUserID());


        Drawable placeholder = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);
        String imagePath = PrefServiceConfig.getServiceURL_SDS(context) + "/api/v1/User/Image/" + "five_hundred_"+ user.getProfileImagePath() + ".jpg";


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(profileImage);


//        phone.setText(user.getPhone());
        userName.setText(user.getName());
    }







    @OnClick(R.id.list_item)
    void onlistItemClick()
    {
        Intent  intent = new Intent(context, EditProfile.class);
        intent.putExtra(EditProfile.TAG_IS_GLOBAL_PROFILE,true);
        intent.putExtra(FragmentEditProfile.EDIT_MODE_INTENT_KEY, FragmentEditProfile.MODE_UPDATE);
        context.startActivity(intent);
    }






    @OnClick(R.id.log_out_button)
    void logOutClick()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("Confirm Logout !")
                .setMessage("Do you want to log out !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Cancelled !");
                    }
                })
                .show();

    }





    private void logout()
    {
        // log out
//        PrefLogin.saveUserProfile(null,context);
//        PrefLogin.saveCredentials(context,null,null);
//
//        PrefLoginGlobal.saveUserProfile(null,context);
//        PrefLoginGlobal.saveCredentials(context,null,null);




        UtilityFunctions.logout(context);


//        if(context instanceof NotifyAboutLogin)
//        {
//            ((NotifyAboutLogin) context).loggedOut();
//        }



        if(fragment instanceof NotifyAboutLogin)
        {
            ((NotifyAboutLogin) fragment).loggedOut();
        }

    }





    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }



}
