package org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import org.nearbyshops.whitelabelapp.API.UserService;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.Interfaces.NotifyChangePassword;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShop;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.whitelabelapp.Lists.TransactionHistory.TransactionHistory;
import org.nearbyshops.whitelabelapp.Model.Image;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangeEmail.ChangeEmail;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangeEmail.PrefChangeEmail;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangePhone.ChangePhone;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangePhone.PrefChangePhone;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;


public class FragmentEditProfile extends Fragment {



    private boolean isDestroyed = false;

    public static int PICK_IMAGE_REQUEST = 21;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


    @Inject
    UserService userService;


    // flag for knowing whether the image is changed or not
    private boolean isImageChanged = false;
    private boolean isImageRemoved = false;



    @BindView(R.id.account_balance) TextView balance;
    @BindView(R.id.radio_end_user) RadioButton radioEndUser;
    @BindView(R.id.radio_shop_admin) RadioButton radioShopAdmin;
    @BindView(R.id.radio_shop_staff) RadioButton radioShopStaff;
    @BindView(R.id.radio_delivery_vendor) RadioButton radioDeliveryVendor;
    @BindView(R.id.radio_admin) RadioButton radioAdmin;
    @BindView(R.id.radio_staff) RadioButton radioStaff;
    @BindView(R.id.radio_delivery_market) RadioButton radioDeliveryMarket;

    // bind views
    @BindView(R.id.uploadImage) ImageView resultView;
    @BindView(R.id.choice_male) RadioButton choiceMale;
    @BindView(R.id.choice_female) RadioButton choiceFemale;


    @BindView(R.id.local_user_id) EditText localUserID;
    @BindView(R.id.global_user_id) EditText globalUserID;
    @BindView(R.id.name) EditText name;
    @BindView(R.id.secret_code) EditText secretCode;
    @BindView(R.id.username) EditText username;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.phone) EditText phone;
    @BindView(R.id.about) EditText about;
    @BindView(R.id.saveButton) TextView saveButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @BindView(R.id.make_account_private) CheckBox isAccountPrivate;

    @BindView(R.id.local_user_id_block) TextInputLayout localUserIDBlock;




//    @BindViews({R.id.label_change_password, R.id.label_add_or_change_email})
//    List<TextView> label_instructions;

    @BindView(R.id.label_change_password) TextView labelChangePassword;
    @BindView(R.id.label_add_or_change_email) TextView labelChangeEmail;
    @BindView(R.id.label_add_or_change_phone) TextView labelChangePhone;


    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE_BY_ADMIN = 55;
    public static final int MODE_UPDATE_BY_SUPER_ADMIN = 56;

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    private int current_mode;

    @BindView(R.id.admin_options_block)
    LinearLayout adminOptionsBlock;



    private User user = new User();



    @Inject
    Gson gson;



    public FragmentEditProfile() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(savedInstanceState==null)
        {

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);

            if(current_mode == MODE_UPDATE)
            {
                user = PrefLogin.getUser(getContext());

                if(user !=null) {
                    bindUserData();
                }
            }
            else if(current_mode==MODE_UPDATE_BY_ADMIN)
            {

                String jsonString = getActivity().getIntent().getStringExtra("user_profile");
                user = UtilityFunctions.provideGson().fromJson(jsonString,User.class);
                bindUserData();

                getUserDetails();
            }
            else if(current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
            {

                String jsonString = getActivity().getIntent().getStringExtra("user_profile");
                user = UtilityFunctions.provideGson().fromJson(jsonString,User.class);
                bindUserData();

                getUserDetails();
            }
        }




        updateFieldVisibility();


        if(user !=null) {
            loadImage(user.getProfileImagePath());
            System.out.println("Loading Image !");
        }


        setActionBarTitle();
//        setupViewModel();

        return rootView;
    }





    private void getUserDetails()
    {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.loading_message));
        pd.show();


        Call<User> call = userService.getUserDetails(
                PrefLogin.getAuthorizationHeader(getActivity()),
                user.getUserID()
        );



        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(!isVisible())
                {
                    return;
                }

                pd.dismiss();



                if(response.code()==200)
                {
                    user = response.body();

                    bindUserData();
                }
                else
                {
                    showToastMessage("Failed to get User Details : Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                if(!isVisible())
                {
                    return;
                }

                showToastMessage("Failed !");

            }
        });

    }





    @OnClick(R.id.label_change_password)
    void changePasswordClick()
    {
        if(getActivity() instanceof NotifyChangePassword)
        {
            ((NotifyChangePassword)getActivity()).changePasswordClick();
        }
    }



    @OnClick(R.id.label_add_or_change_email)
    void changeEmailClick()
    {

        PrefChangeEmail.saveUser(null,getActivity());
        Intent intent = new Intent(getActivity(), ChangeEmail.class);
        startActivityForResult(intent,10);
    }




    @OnClick(R.id.label_add_or_change_phone)
    void changePhoneClick()
    {
        PrefChangePhone.saveUser(null,getActivity());
        Intent intent = new Intent(getActivity(), ChangePhone.class);
        startActivityForResult(intent,10);
    }







    private void setActionBarTitle()
    {
        if(getActivity() instanceof AppCompatActivity)
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

            if(actionBar!=null)
            {
                if(current_mode == MODE_ADD)
                {
                    actionBar.setTitle("Sign Up");
                }
                else if(current_mode==MODE_UPDATE || current_mode==MODE_UPDATE_BY_ADMIN)
                {
                    actionBar.setTitle("Edit Profile");
                }

            }
        }
    }






    private void updateFieldVisibility()
    {

        if(current_mode==MODE_UPDATE_BY_ADMIN)
        {

            labelChangePhone.setVisibility(GONE);
            labelChangeEmail.setVisibility(GONE);
            labelChangePassword.setVisibility(GONE);

            email.setEnabled(true);
            phone.setEnabled(true);

            password.setVisibility(View.GONE);
//            password.setEnabled(true);
//            password.setText("Enter text to set Password");
//            password.setInputType(InputType.TYPE_CLASS_TEXT);

            adminOptionsBlock.setVisibility(View.VISIBLE);

        }
        else if(current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
        {


            labelChangePhone.setVisibility(GONE);
            labelChangeEmail.setVisibility(GONE);
            labelChangePassword.setVisibility(GONE);

            adminOptionsBlock.setVisibility(GONE);
        }
        else
        {
            adminOptionsBlock.setVisibility(GONE);

            labelChangePhone.setVisibility(VISIBLE);
            labelChangeEmail.setVisibility(VISIBLE);
            labelChangePassword.setVisibility(VISIBLE);


            if(current_mode==MODE_ADD)
            {
                saveButton.setText("Sign Up");
                localUserID.setVisibility(GONE);

                password.setEnabled(true);
                password.setText("");
                email.setEnabled(true);


            }
            else if(current_mode== MODE_UPDATE)
            {
                localUserID.setVisibility(VISIBLE);
                saveButton.setText("Save");

                password.setEnabled(false);
                password.setText("Password");
                email.setEnabled(false);
            }
        }


//            localUserIDBlock.setVisibility(GONE);
        globalUserID.setVisibility(GONE);
        localUserIDBlock.setHint("User ID");

        setActionBarTitle();
    }






    public static final String TAG_LOG = "edit_profile";

    private void showLogMessage(String message)
    {
        Log.d(TAG_LOG,message);
        System.out.println(message);
    }







    private void loadImage(String imagePath) {

        String imagePathLocal = "";

        imagePathLocal = PrefGeneral.getServerURL(getContext()) + "/api/v1/User/Image/five_hundred_" + imagePath + ".jpg";

        Picasso.get()
                .load(imagePathLocal)
                .into(resultView);
    }







    @OnClick(R.id.saveButton)
    public void UpdateButtonClick()
    {

        if(!validateData())
        {
            return;
        }

        if(current_mode == MODE_ADD)
        {
            addAccount();
        }
        else if(current_mode == MODE_UPDATE || current_mode==MODE_UPDATE_BY_ADMIN || current_mode==MODE_UPDATE_BY_SUPER_ADMIN)
        {
            update();
        }
    }




    private boolean validateData()
    {
        boolean isValid = true;

//        if(phone.getText().toString().length()==0)
//        {
//            phone.setError("Please enter Phone Number");
//            phone.requestFocus();
//            isValid= false;
//        }


//
//        if(email.getText().toString().length()==0)
//        {
//            email.requestFocus();
//            email.setError("E-mail cannot be empty !");
//            isValid = false;
//        }


        if(password.getText().toString().length()==0)
        {
            password.requestFocus();
            password.setError("Password cannot be empty");
            isValid = false;
        }



//        if(username.getText().toString().length()==0)
//        {
//            username.requestFocus();
//            username.setError("Username cannot be empty");
//            isValid= false;
//        }


        if(name.getText().toString().length()==0)
        {

//            Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_close_black_24dp);
            name.requestFocus();
            name.setError("Name cannot be empty");
            isValid = false;
        }


        return isValid;
    }





//    @OnTextChanged(R.id.username)
//    void usernameCheck()
//    {
//
//
//        if(user !=null && user.getUsername()!=null
//                &&
//                username.getText().toString().equals(user.getUsername()))
//        {
//            username.setTextColor(ContextCompat.getColor(getContext(),R.color.gplus_color_1));
//            return;
//        }
//
//
//        saveButton.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
//
//
//        Call<ResponseBody> call = userService.checkUsernameExists(username.getText().toString());
//
//
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if(response.code()==200)
//                {
//                    //username already exists
//                    username.setTextColor(ContextCompat.getColor(getContext(),R.color.gplus_color_4));
//                    username.setError("Username already exist !");
//                }
//                else if(response.code() == 204)
//                {
//                    username.setTextColor(ContextCompat.getColor(getContext(),R.color.gplus_color_1));
//                }
//
//
//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//
//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
//
//            }
//        });
//    }





    private void addAccount()
    {
        if(isImageChanged)
        {
            if(!isImageRemoved)
            {
                // upload image with add
                uploadPickedImage(false);
            }


            // reset the flags
            isImageChanged = false;
            isImageRemoved = false;

        }
        else
        {
            // post request
            retrofitPOSTRequest();
        }

    }


    private void update()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            deleteImage(user.getProfileImagePath());


            if(isImageRemoved)
            {

                user.setProfileImagePath(null);
                retrofitPUTRequest();

            }else
            {

                uploadPickedImage(true);
            }


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        }else {

            retrofitPUTRequest();
        }
    }



    void bindAdministrativeOptions()
    {

        balance.setText("Balance : " + UtilityFunctions.refinedStringWithDecimals(user.getServiceAccountBalance()));

        if(user.getRole()==User.ROLE_END_USER_CODE)
        {
            radioEndUser.setChecked(true);
        }
        else if(user.getRole()==User.ROLE_SHOP_STAFF_CODE)
        {
            radioShopStaff.setChecked(true);
        }
        else if(user.getRole()==User.ROLE_SHOP_ADMIN_CODE)
        {
            radioShopAdmin.setChecked(true);
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_SHOP_CODE)
        {
            radioDeliveryVendor.setChecked(true);
        }
        else if(user.getRole()==User.ROLE_STAFF_CODE)
        {
            radioStaff.setChecked(true);
        }
        else if(user.getRole()==User.ROLE_ADMIN_CODE)
        {
            radioAdmin.setChecked(true);
        }
        else if(user.getRole()==User.ROLE_DELIVERY_GUY_MARKET_CODE)
        {
            radioDeliveryMarket.setChecked(true);
        }

    }




    private void bindUserData()
    {


        if(user !=null) {

            localUserID.setText(String.valueOf(user.getUserID()));
            name.setText(user.getName());
            secretCode.setText(String.valueOf(user.getSecretCode()));

            username.setText(user.getUsername());
//            password.setText(user.getPassword());
            email.setText(user.getEmail());
            about.setText(user.getAbout());
            phone.setText(user.getPhone());
            isAccountPrivate.setChecked(user.isAccountPrivate());



            if(user.getGender()!=null)
            {
                if(user.getGender())
                {
                    choiceMale.setChecked(true);
                }
                else
                {
                    choiceFemale.setChecked(true);
                }
            }

            bindAdministrativeOptions();
        }
    }






    private void getDataFromViews()
    {

        user.setName(name.getText().toString());

        user.setSecretCode(Integer.parseInt(secretCode.getText().toString()));

        user.setUsername(username.getText().toString());
        user.setAbout(about.getText().toString());
        user.setGender(choiceMale.isChecked());
        user.setAccountPrivate(isAccountPrivate.isChecked());


        if(username.getText().toString().length()==0)
        {
            user.setUsername(null);
        }
        else
        {
            user.setUsername(username.getText().toString());
        }



        if(email.getText().toString().length()==0)
        {
            user.setEmail(null);
        }
        else
        {
            user.setEmail(email.getText().toString());
        }

        if(phone.getText().toString().length()==0)
        {
            user.setPhone(null);
        }
        else
        {
            user.setPhone(phone.getText().toString());
        }



        if(password.getText().toString().length()==0)
        {
            user.setPassword(null);
        }
        else
        {
            user.setPassword(password.getText().toString());
        }

    }






    private void retrofitPUTRequest()
    {

        getDataFromViews();


        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(VISIBLE);



//        boolean isGlobalProfile = getActivity().getIntent().getBooleanExtra(EditProfile.TAG_IS_GLOBAL_PROFILE,false);

        Call<ResponseBody> call;
        Call<ResponseBody> callUpdateLocal = null;



        if(current_mode==MODE_UPDATE)
        {
            // update Item Call
            call = userService.updateProfileEndUser(
                    PrefLogin.getAuthorizationHeader(getActivity()),
                    user
            );

        }
        else if(current_mode==MODE_UPDATE_BY_ADMIN)
        {
            // update Item Call
            call = userService.updateProfileByAdmin(
                    PrefLogin.getAuthorizationHeader(getActivity()),
                    user
            );

        }
        else
        {


            showToastMessage("Current Mode : " + current_mode);
            saveButton.setVisibility(VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            return;


        }







        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");



                    if(getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_UPDATE)==MODE_UPDATE)
                    {
                        PrefLogin.saveUserProfile(user,getContext());
                    }


                }
                else
                {
                    showToastMessage("Update failed code : " + response.code());
                }


                saveButton.setVisibility(VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Update Failed !");


                saveButton.setVisibility(VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });






        if(callUpdateLocal!=null)
        {
            callUpdateLocal.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {
//                        showToastMessage("Updated Locally !");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }



    }






    private void retrofitPOSTRequest()
    {
        getDataFromViews();



//        saveButton.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
//
//
//        Call<User> call = userService.insertUser(user);
//
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//                if(response.code()==201)
//                {
//                    showToastMessage("Add successful !");
//
//                    current_mode = MODE_UPDATE;
//                    updateFieldVisibility();
//                    user = response.body();
//                    bindUserData();
//
//                    PrefLogin.saveCredentials(
//                            getContext(), user.getUsername(),
//                            user.getPassword());
//
//                }
//                else
//                {
//                    showToastMessage("Add failed !");
//                }
//
//
//
//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//                showToastMessage("Sign up not completed due to network failure !");
//
//
//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
//
//            }
//        });

    }








    /*
        Utility Methods
     */






    private void showToastMessage(String message)
    {
//        if(getActivity()!=null)
//        {
//            Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
//        }

        UtilityFunctions.showToastMessage(getActivity(),message);
    }




    @BindView(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);

        isImageChanged = true;
        isImageRemoved = true;
    }




    private static void clearCache(Context context)
    {
        File file = new File(context.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();
    }



    @OnClick({R.id.textChangePicture,R.id.uploadImage})
    void pickShopImage() {

//        ImageCropUtility.showFileChooser(()getActivity());



        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }




        ImagePicker.Companion.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(2024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1500, 1500)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();


    }




    File imagePickedFile;
    String imageFilePath;





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);

        showLogMessage("FragmentEditProfile : onActivityResult()"
        + "\nRequest Code : " + requestCode
        + "\nResponse Code : " + resultCode
        );

        if(requestCode==10 && resultCode == 10)
        {

            // result obtained after email change
            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);

            if(current_mode == MODE_UPDATE)
            {
                user = PrefLogin.getUser(getContext());

                if(user !=null) {
                    bindUserData();
                }
            }


            showLogMessage("FragmentEditProfile : onActivityResult()");

        }

        else if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK

            resultView.setImageURI(result.getData());


            //You can get File object from intent
            imagePickedFile = ImagePicker.Companion.getFile(result);
            imageFilePath = ImagePicker.Companion.getFilePath(result);



            isImageChanged = true;
            isImageRemoved = false;



        } else if (resultCode == ImagePicker.RESULT_ERROR) {

            showToastMessage(ImagePicker.Companion.getError(result));

        }
        else {
            showToastMessage("Task Cancelled !");
        }
    }









    /*

    // Code for Uploading Image

     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showToastMessage("Permission Granted !");
                    pickShopImage();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {


                    showToastMessage("Permission Denied for Read External Storage . ");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }



    private void uploadPickedImage(final boolean isModeEdit)
    {

//        Log.d("applog", "onClickUploadImage");


        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }


//        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");


        File file;
        file = new File(imageFilePath);


        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("img", file.getName(), requestBody);


        // Marker

        RequestBody requestBodyBinary = null;

        InputStream in = null;

        try {
            in = new FileInputStream(file);

            byte[] buf;
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;

            requestBodyBinary = RequestBody.create(MediaType.parse("application/octet-stream"), buf);

        } catch (Exception e) {
            e.printStackTrace();
        }




        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(VISIBLE);




//        boolean isGlobalProfile = getActivity().getIntent().getBooleanExtra(EditProfile.TAG_IS_GLOBAL_PROFILE,false);

        Call<Image> imageCall = userService.uploadImage(
                PrefLogin.getAuthorizationHeader(getContext()),
                fileToUpload
        );





        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");


                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());



                    user.setProfileImagePath(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    user.setProfileImagePath(null);

                }
                else
                {
                    showToastMessage("Image Upload failed Code : " + response.code());
                    user.setProfileImagePath(null);

                }

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }


                saveButton.setVisibility(VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


            }



            @Override
            public void onFailure(Call<Image> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Image Upload failed !");
                user.setProfileImagePath(null);

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }


                saveButton.setVisibility(VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }



    private void deleteImage(String filename)
    {


//        saveButton.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);




//        boolean isGlobalProfile = getActivity().getIntent().getBooleanExtra(EditProfile.TAG_IS_GLOBAL_PROFILE,false);

        Call<ResponseBody> call = userService.deleteImage(
                PrefLogin.getAuthorizationHeader(getContext()),
                filename
        );








        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                    {
                        showToastMessage("Image Removed !");
                    }
                    else
                    {
//                        showToastMessage("Image Delete failed");
                    }



//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                showToastMessage("Image Delete failed");


                if(isDestroyed)
                {
                    return;
                }



//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }








    @Override
    public void onDestroy() {
        super.onDestroy();
//        isDestroyed = true;
    }


    @Override
    public void onResume() {
        super.onResume();

        isDestroyed = false;
    }





    @OnClick(R.id.add_credit)
    void addCreditClick()
    {
        showToastMessage("Add Credit !");
    }


    @OnClick(R.id.radio_end_user)
    void radioEndUserClick()
    {
        user.setRole(User.ROLE_END_USER_CODE);
    }



    @OnClick(R.id.radio_shop_admin)
    void radioShopAdminClick()
    {
        user.setRole(User.ROLE_SHOP_ADMIN_CODE);
    }


    @OnClick(R.id.radio_shop_staff)
    void radioShopStaffClick()
    {
        user.setRole(User.ROLE_SHOP_STAFF_CODE);
    }


    @OnClick(R.id.radio_delivery_vendor)
    void radioDeliveryVendorClick()
    {
        user.setRole(User.ROLE_DELIVERY_GUY_SHOP_CODE);
    }


    @OnClick(R.id.radio_admin)
    void radioAdminClick()
    {
        user.setRole(User.ROLE_ADMIN_CODE);
    }


    @OnClick(R.id.radio_staff)
    void radioStaffClick()
    {
        user.setRole(User.ROLE_STAFF_CODE);
    }


    @OnClick(R.id.radio_delivery_market)
    void radioDeliveryMarketClick()
    {
        user.setRole(User.ROLE_DELIVERY_GUY_MARKET_CODE);
    }






    @OnClick(R.id.account_transaction_history)
    void shopTransactionsClick()
    {
        Intent intent = new Intent(getActivity(), TransactionHistory.class);
        intent.putExtra("user_id",user.getUserID());
        startActivity(intent);
    }







    @OnClick(R.id.shop_dashboard)
    void shopDashboardClick()
    {
        // Open Edit fragment in edit mode
        Intent intent = new Intent(getActivity(), EditShop.class);
        intent.putExtra("shop_admin_id",user.getUserID());

        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE_BY_ADMIN);
        startActivity(intent);

//        viewModelShop.getShopIDForShopAdmin(user.getUserID());
//        requestCodeGetShop = 4150;
    }



//
//
//
//    private ViewModelShop viewModelShop;
//    private int requestCodeGetShop = 0;
//
//
//    private void setupViewModel()
//    {
//
//        viewModelShop = new ViewModelShop(MyApplication.application);
//
//
//        viewModelShop.getShopLive().observe(getViewLifecycleOwner(), new Observer<Shop>() {
//            @Override
//            public void onChanged(Shop shop) {
//
//
//                if(requestCodeGetShop==4150)
//                {
//
//                    // Open Edit fragment in edit mode
//                    Intent intent = new Intent(getActivity(), EditShop.class);
//                    intent.putExtra("shop_id",shop.getShopID());
//                    intent.putExtra("shop_admin_id",user.getUserID());
//
//                    intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE_BY_ADMIN);
//                    startActivity(intent);
//                }
//
//
//            }
//        });
//
//
//
//        viewModelShop.getEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//
//
//                if(integer==ViewModelShop.EVENT_SHOP_NOT_CREATED)
//                {
//                    if(requestCodeGetShop==4150)
//                    {
//                        Intent intent = new Intent(getActivity(), EditShop.class);
//                        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_CREATE_SHOP_BY_ADMIN);
//                        intent.putExtra("shop_admin_id",user.getUserID());
//                        startActivity(intent);
//                    }
//                }
//
//
//            }
//        });
//
//
//        viewModelShop.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                showToastMessage(s);
//
//            }
//        });
//
//    }

}
