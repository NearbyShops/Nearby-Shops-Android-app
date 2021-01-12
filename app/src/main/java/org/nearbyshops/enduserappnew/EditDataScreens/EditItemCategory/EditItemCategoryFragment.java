package org.nearbyshops.enduserappnew.EditDataScreens.EditItemCategory;


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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.API.ItemCategoryService;
import org.nearbyshops.enduserappnew.Model.Image;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditItemCategoryFragment extends Fragment {

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


    ItemCategory parent;

    public static final String ITEM_CATEGORY_INTENT_KEY = "item_cat";


    @Inject
    ItemCategoryService itemCategoryService;


    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    // bind views
    @BindView(R.id.uploadImage)
    ImageView resultView;


//    @BindView(R.id.shop_open) CheckBox shopOpen;
//    @BindView(R.id.shop_id) EditText shopID;


    @BindView(R.id.itemCategoryID) EditText itemCategoryID;
    @BindView(R.id.itemCategoryName)
    TextInputEditText itemCategoryName;
    @BindView(R.id.itemCategoryDescription) EditText itemCategoryDescription;
    @BindView(R.id.descriptionShort) EditText descriptionShort;
    @BindView(R.id.isAbstractNode) CheckBox isAbstractNode;
    @BindView(R.id.isLeafNode) CheckBox isLeafNode;

    @BindView(R.id.category_order) EditText itemCategoryOrder;

//    @BindView(R.id.itemID) EditText itemID;
//    @BindView(R.id.itemName) EditText itemName;
//    @BindView(R.id.itemDescription) EditText itemDescription;
//    @BindView(R.id.itemDescriptionLong) EditText itemDescriptionLong;
//    @BindView(R.id.quantityUnit) EditText quantityUnit;



    @BindView(R.id.saveButton) TextView saveButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    boolean isDestroyed = false;


    public static final String SHOP_INTENT_KEY = "shop_intent_key";
    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;


    private int current_mode = MODE_ADD;


    private ItemCategory itemCategory = new ItemCategory();

    public EditItemCategoryFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_edit_item_category, container, false);
        ButterKnife.bind(this,rootView);



        Toolbar toolbar = rootView.findViewById(R.id.toolbar);


        if(getActivity()!=null)
        {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }

//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        if(savedInstanceState==null)
        {
//            shopAdmin = getActivity().getIntent().getParcelableExtra(SHOP_ADMIN_INTENT_KEY);

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);
//            parent = getActivity().getIntent().getParcelableExtra(ITEM_CATEGORY_INTENT_KEY);

            String jsonString = getActivity().getIntent().getStringExtra(ITEM_CATEGORY_INTENT_KEY);
            parent = UtilityFunctions.provideGson().fromJson(jsonString,ItemCategory.class);


            if(current_mode == MODE_UPDATE)
            {
                itemCategory = PrefItemCategory.getItemCategory(getContext());
                getItemCategoryDetails();
            }
            else if (current_mode == MODE_ADD)
            {
//                item.setItemCategoryID(itemCategory.getItemCategoryID());
//                System.out.println("Item Category ID : " + item.getItemCategoryID());
            }

        }



//        if(validator==null)
//        {
//            validator = new Validator(this);
//            validator.setValidationListener(this);
//        }

        updateIDFieldVisibility();


        return rootView;
    }






    private void updateIDFieldVisibility()
    {

        if(current_mode==MODE_ADD)
        {
            saveButton.setText("Add");
            itemCategoryID.setVisibility(View.GONE);

            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Item Category");
            }
        }
        else if(current_mode== MODE_UPDATE)
        {
            itemCategoryID.setVisibility(View.VISIBLE);
            saveButton.setText("Save");


            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Item Category");
            }
        }
    }








    private void getItemCategoryDetails() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please with ... Getting Item Category details !");
        pd.show();



        Call<ItemCategory> call = itemCategoryService.getItemCategoryDetails(
                itemCategory.getItemCategoryID()
        );


        call.enqueue(new Callback<ItemCategory>() {
            @Override
            public void onResponse(Call<ItemCategory> call, Response<ItemCategory> response) {

                pd.dismiss();

                if (response.code() == 200) {
                    itemCategory = response.body();
                    bindDataToViews();

                } else {
                    showToastMessage("Failed : Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ItemCategory> call, Throwable t) {
                showToastMessage("Failed !");
            }
        });

    }





    public static final String TAG_LOG = "TAG_LOG";




    private void showLogMessage(String message)
    {
        Log.i(TAG_LOG,message);
        System.out.println(message);
    }





    @OnClick(R.id.saveButton)
    public void UpdateButtonClick()
    {

        if(!validateData())
        {
//            showToastMessage("Please correct form data before save !");
            return;
        }


        if(current_mode == MODE_ADD)
        {
            itemCategory = new ItemCategory();
            addAccount();
        }
        else if(current_mode == MODE_UPDATE)
        {
            update();
        }
    }






    boolean validateData()
    {
        boolean isValid = true;

        if(itemCategoryName.getText().toString().length()==0)
        {
            itemCategoryName.setError("Name cannot be empty !");
            itemCategoryName.requestFocus();
            isValid= false;
        }



        return isValid;
    }




    void addAccount()
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





    void update()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            deleteImage(itemCategory.getImagePath());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {

                itemCategory.setImagePath(null);
                retrofitPUTRequest();

            }else
            {

                uploadPickedImage(true);
//                retrofitPUTRequest();
            }


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        }else {

            retrofitPUTRequest();
        }
    }



    void bindDataToViews()
    {
        if(itemCategory !=null) {


            itemCategoryID.setText(String.valueOf(itemCategory.getItemCategoryID()));
            itemCategoryName.setText(itemCategory.getCategoryName());
            itemCategoryDescription.setText(itemCategory.getCategoryDescription());
            isLeafNode.setChecked(itemCategory.isLeafNode());
            isAbstractNode.setChecked(itemCategory.isAbstractNode());
            descriptionShort.setText(itemCategory.getDescriptionShort());
            itemCategoryOrder.setText(String.valueOf(itemCategory.getCategoryOrder()));


            String imagePath = PrefGeneral.getServiceURL(getContext()) + "/api/v1/ItemCategory/Image/" + itemCategory.getImagePath();

            System.out.println(imagePath);

            Picasso.get()
                    .load(imagePath)
                    .into(resultView);
        }
    }





    void getDataFromViews()
    {
        if(itemCategory ==null)
        {
            if(current_mode == MODE_ADD)
            {
//                item = new Item();
            }
            else
            {
                return;
            }
        }


        itemCategory.setCategoryName(itemCategoryName.getText().toString());
        itemCategory.setCategoryDescription(itemCategoryDescription.getText().toString());
        itemCategory.setDescriptionShort(descriptionShort.getText().toString());
        itemCategory.setLeafNode(isLeafNode.isChecked());
        itemCategory.setAbstractNode(isAbstractNode.isChecked());

        if(itemCategoryOrder.getText().toString().length()>0)
        {
            itemCategory.setCategoryOrder(Integer.parseInt(itemCategoryOrder.getText().toString()));
        }
    }













    private void retrofitPUTRequest()
    {

        getDataFromViews();



        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

//        saveButtonNew.setVisibility(View.INVISIBLE);
//        progressBarNew.setVisibility(View.VISIBLE);

        MultipartBody.Part fileToUpload = null;


        if(isImageChanged && !isImageRemoved)
        {
            File file;
            file = new File(imageFilePath);

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("img", file.getName(), requestBody);
        }





        Call<ResponseBody> call = itemCategoryService.updateItemCategory(
                PrefLogin.getAuthorizationHeaders(getContext()),
                itemCategory.getItemCategoryID(),
                itemCategory
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
                    showToastMessage("Update Successful !");
                    PrefItemCategory.saveItemCategory(itemCategory,getContext());
                }
                else if(response.code()== 403 || response.code() ==401)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Update Failed Code : " + response.code());
                }


                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                saveButtonNew.setVisibility(View.VISIBLE);
//                progressBarNew.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }



                showToastMessage("Update Failed ! ");

                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                saveButtonNew.setVisibility(View.VISIBLE);
//                progressBarNew.setVisibility(View.INVISIBLE);


            }
        });

    }





    private void retrofitPOSTRequest()
    {
        getDataFromViews();
        itemCategory.setParentCategoryID(parent.getItemCategoryID());

//        System.out.println("Item Category ID (POST) : " + item.getItemCategoryID());


        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

//        saveButtonNew.setVisibility(View.INVISIBLE);
//        progressBarNew.setVisibility(View.VISIBLE);


        Call<ItemCategory> call = itemCategoryService.insertItemCategory(PrefLogin.getAuthorizationHeaders(getContext()), itemCategory);

        call.enqueue(new Callback<ItemCategory>() {
            @Override
            public void onResponse(Call<ItemCategory> call, Response<ItemCategory> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==201)
                {
                    showToastMessage("Add successful !");

                    current_mode = MODE_UPDATE;
                    updateIDFieldVisibility();
                    itemCategory = response.body();
                    bindDataToViews();

                    PrefItemCategory.saveItemCategory(itemCategory,getContext());

                }
                else if(response.code()== 403 || response.code() ==401)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Add failed Code : " + response.code());
                }




                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                saveButtonNew.setVisibility(View.VISIBLE);
//                progressBarNew.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<ItemCategory> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Add failed !");


                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


//                saveButtonNew.setVisibility(View.VISIBLE);
//                progressBarNew.setVisibility(View.INVISIBLE);
            }
        });

    }







    /*
        Utility Methods
     */





    void showToastMessage(String message)
    {
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



    public static void clearCache(Context context)
    {
        File file = new File(context.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();
    }



    @OnClick(R.id.textChangePicture)
    void pickShopImage() {



        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


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



        if (resultCode == Activity.RESULT_OK) {
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




//    @BindView(R.id.progress_bar_new) ProgressBar progressBarNew;
//    @BindView(R.id.saveButtonNew) TextView saveButtonNew;


    public void uploadPickedImage(final boolean isModeEdit)
    {

        Log.d("applog", "onClickUploadImage");


        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


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



//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);





        showToastMessage("Uploading Image !");
//        progressBarNew.setVisibility(View.VISIBLE);
//        saveButtonNew.setVisibility(View.INVISIBLE);

        saveButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<Image> imageCall = itemCategoryService.uploadImage(PrefLogin.getAuthorizationHeaders(getContext()),
                fileToUpload
        );




        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {

                if(isDestroyed)
                {
                    return;
                }





                showToastMessage("Upload finished !");
//                progressBarNew.setVisibility(View.INVISIBLE);
//                saveButtonNew.setVisibility(View.VISIBLE);

                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);




                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");

                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());


                    itemCategory.setImagePath(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    itemCategory.setImagePath(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    itemCategory.setImagePath(null);

                }

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }



            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }



                showToastMessage("Image Upload failed !");
                itemCategory.setImagePath(null);


                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }



                saveButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);




//                saveButtonNew.setVisibility(View.VISIBLE);
//                progressBarNew.setVisibility(View.INVISIBLE);
            }
        });

    }



    void deleteImage(String filename)
    {
        Call<ResponseBody> call = itemCategoryService.deleteImage(
                PrefLogin.getAuthorizationHeaders(getContext()),
                filename);





//        saveButton.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);

//        saveButtonNew.setVisibility(View.INVISIBLE);
//        progressBarNew.setVisibility(View.VISIBLE);


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

//                saveButtonNew.setVisibility(View.VISIBLE);
//                progressBarNew.setVisibility(View.INVISIBLE);

            }



            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


//                showToastMessage("Image Delete failed");


//                saveButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);

//                saveButtonNew.setVisibility(View.VISIBLE);
//                progressBarNew.setVisibility(View.INVISIBLE);
            }
        });
    }





    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }
}
