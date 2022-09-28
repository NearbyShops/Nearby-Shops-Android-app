package org.nearbyshops.whitelabelapp.EditDataScreens.EditItem;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.API.ItemImageService;
import org.nearbyshops.whitelabelapp.API.ItemService;
import org.nearbyshops.whitelabelapp.API.ItemSpecNameService;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItemImage.EditItemImage;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItemImage.EditItemImageFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItemImage.PrefItemImage;
import org.nearbyshops.whitelabelapp.Model.Image;
import org.nearbyshops.whitelabelapp.Model.Item;
import org.nearbyshops.whitelabelapp.Model.ItemCategory;
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ItemImageEndPoint;
import org.nearbyshops.whitelabelapp.Model.ModelImages.ItemImage;
import org.nearbyshops.whitelabelapp.Model.ModelItemSpecs.ItemSpecificationName;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderAddItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;


public class EditItemFragmentNew extends Fragment implements
        ViewHolderItemImage.ListItemClick,
        ViewHolderAddItem.ListItemClick,
        AdapterItemSpecifications.NotifyItemSpecs {




    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


    private ItemCategory itemCategory;

    public static final String ITEM_CATEGORY_INTENT_KEY = "item_cat";


    @Inject
    ItemService itemService;

    @Inject
    ItemImageService itemImageService;

    @Inject
    ItemSpecNameService itemSpecNameService;



    // flag for knowing whether the image is changed or not
    private boolean isImageChanged = false;
    private boolean isImageRemoved = false;





    // bind views
    @BindView(R.id.uploadImage)
    ImageView resultView;



    @BindView(R.id.itemID) EditText itemIDView;
    @BindView(R.id.itemName) EditText itemName;
    @BindView(R.id.itemDescription) EditText itemDescription;
    @BindView(R.id.itemDescriptionLong) EditText itemDescriptionLong;
    @BindView(R.id.quantityUnit) EditText quantityUnit;
    @BindView(R.id.barcode_image) ImageView barcodeSign;


    @BindView(R.id.image_copyrights) TextView imageCopyrights;
    @BindView(R.id.list_price) EditText listPrice;
    @BindView(R.id.discounted_price) EditText discountedPrice;
//    String barcode;
//    String barcodeFormat;

    @BindView(R.id.saveButton) TextView buttonUpdateItem;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    public static final String SHOP_INTENT_KEY = "shop_intent_key";
    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;


    Item item ;
    int itemID;







    public EditItemFragmentNew() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }











    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.content_edit_item_fragment_new, container, false);
        ButterKnife.bind(this,rootView);


        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        if(savedInstanceState==null)
        {

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);

            if(current_mode == MODE_UPDATE)
            {
                item = PrefItem.getItem(getContext());

                getItemDetails();

            }
            else if (current_mode == MODE_ADD)
            {
                String jsonString = getActivity().getIntent().getStringExtra(ITEM_CATEGORY_INTENT_KEY);
                itemCategory = UtilityFunctions.provideGson().fromJson(jsonString,ItemCategory.class);
            }


            if(item !=null) {
                bindDataToViews();
            }
        }


        updateFieldVisibility();




        return rootView;
    }







    private void getItemDetails() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.loading_message));
        pd.show();



        Call<Item> call = itemService.getItemDetails(
                item.getItemID()
        );


        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {


                pd.dismiss();

                if (response.code() == 200) {
                    item = response.body();
                    bindDataToViews();

                } else {
                    showToastMessage("Failed : Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

                showToastMessage("Failed !");
            }
        });


    }






    @OnClick(R.id.uploadImage)
    void imageClick()
    {
//        Intent intent = new Intent(getActivity(), ImageList.class);
//        intent.putExtra("item_id",item.getItemID());
//        startActivity(intent);
    }






    ArrayList<Object> dataset = new ArrayList<>();
    @BindView(R.id.recyclerview_item_images) RecyclerView itemImagesList;
    AdapterItemImages adapterItemImages;
    GridLayoutManager layoutManager;


    private void setupRecyclerView() {

        adapterItemImages = new AdapterItemImages(dataset,getActivity(),this);
        itemImagesList.setAdapter(adapterItemImages);
        layoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false);
        itemImagesList.setLayoutManager(layoutManager);

        makeNetworkCallItemImages(true);
    }







    ArrayList<ItemSpecificationName> datasetSpecs = new ArrayList<>();
    @BindView(R.id.recyclerview_item_specifications)
    RecyclerView itemSpecsList;
    AdapterItemSpecifications adapterItemSpecs;
    GridLayoutManager layoutManagerItemSpecs;


    private void setupRecyclerViewSpecs()
    {
        adapterItemSpecs = new AdapterItemSpecifications(datasetSpecs,getActivity(),this);
        itemSpecsList.setAdapter(adapterItemSpecs);
        layoutManagerItemSpecs = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        itemSpecsList.setLayoutManager(layoutManagerItemSpecs);

        makeNetworkCallSpecs(true);

    }


    private void makeNetworkCallSpecs(final boolean clearDataset)
    {
        Call<List<ItemSpecificationName>> call = itemSpecNameService.getItemSpecName(
          item.getItemID(),null
        );


        call.enqueue(new Callback<List<ItemSpecificationName>>() {
            @Override
            public void onResponse(Call<List<ItemSpecificationName>> call, Response<List<ItemSpecificationName>> response) {

                if(response.code()==200)
                {
                    if(clearDataset)
                    {
                        datasetSpecs.clear();
                    }

                    datasetSpecs.addAll(response.body());

                    adapterItemSpecs.notifyDataSetChanged();
                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not Permitted ! Contact Staff to Request Permission !");
                }
                else
                {
                    showToastMessage("Failed to load specs : code " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<ItemSpecificationName>> call, Throwable t) {

                showToastMessage("Failed !");
            }
        });
    }





    @OnClick(R.id.sync_refresh_item_spec)
    void syncRefreshItemSpecs()
    {
        makeNetworkCallSpecs(true);
    }



    void makeNetworkCallItemImages(final boolean clearDataset)
    {
        if(current_mode==MODE_UPDATE)
        {

            Call<ItemImageEndPoint> call = itemImageService.getItemImages(
                    item.getItemID(),ItemImage.IMAGE_ORDER,null,null
            );



            call.enqueue(new Callback<ItemImageEndPoint>() {
                @Override
                public void onResponse(Call<ItemImageEndPoint> call, Response<ItemImageEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    if(response.body()!=null)
                    {
                        if(response.body().getResults()!=null)
                        {
                            if(clearDataset)
                            {
                                dataset.clear();


                            }




                            dataset.add(new ViewHolderAddItem.AddItemData());

                            dataset.addAll(response.body().getResults());
                            adapterItemImages.notifyDataSetChanged();


//                            showToastMessage("Dataset Changed : Item ID : " + String.valueOf(item.getItemID()) +
//                            "\nDataset Count" + String.valueOf(response.body().getResults().size())
//                            );

                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemImageEndPoint> call, Throwable t) {


                    if(isDestroyed)
                    {
                        return;
                    }


                    showToastMessage("Loading Images Failed !");
                }
            });

        }
    }





    boolean isDestroyed = false;

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







    private void updateFieldVisibility()
    {

        if(current_mode==MODE_ADD)
        {
            buttonUpdateItem.setText("Add Item");
            itemIDView.setVisibility(View.GONE);
            itemImagesList.setVisibility(View.GONE);

            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Item");
            }

        }
        else if(current_mode== MODE_UPDATE)
        {
            itemIDView.setVisibility(View.VISIBLE);
            buttonUpdateItem.setText("Save");
            itemImagesList.setVisibility(View.VISIBLE);


            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Item");
            }
        }
    }





    public static final String TAG_LOG = "TAG_LOG";

    void showLogMessage(String message)
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
            item = new Item();
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

        if(itemName.getText().toString().length()==0)
        {
            itemName.setError("Item Name cannot be empty !");
            itemName.requestFocus();
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



    private void update()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            deleteImage(item.getItemImageURL());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {

                item.setItemImageURL(null);
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






    private void bindDataToViews()
    {
        if(item !=null) {

            itemIDView.setText(String.valueOf(item.getItemID()));
            itemName.setText(item.getItemName());
            itemDescription.setText(item.getItemDescription());

            quantityUnit.setText(item.getQuantityUnit());
            itemDescriptionLong.setText(item.getItemDescriptionLong());

//            item.setBarcode(barcode);
//            item.setBarcodeFormat(barcodeFormat);





            listPrice.setText(String.valueOf(item.getListPrice()));
            discountedPrice.setText(String.valueOf(item.getDiscountedPrice()));



            imageCopyrights.setText(item.getImageCopyrights());

            if(item.getBarcode()!=null && item.getBarcodeFormat()!=null)
            {
                barcodeResults.setText("Barcode : " + item.getBarcode() + "\nFormat : " + item.getBarcodeFormat());
            }





            String imagePath = PrefGeneral.getServerURL(getContext()) + "/api/v1/Item/Image/" + item.getItemImageURL();

//            System.out.println(iamgepath);

            Picasso.get()
                    .load(imagePath)
                    .into(resultView);



            setupRecyclerView();
//            setupRecyclerViewSpecs();
        }


    }






    private void getDataFromViews()
    {
        if(item ==null)
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

//        if(current_mode == MODE_ADD)
//        {
//            deliveryGuySelf.setShopID(UtilityShopHome.getShopDetails(getContext()).getShopID());
//        }


        item.setItemName(itemName.getText().toString());
        item.setItemDescription(itemDescription.getText().toString());

        item.setItemDescriptionLong(itemDescriptionLong.getText().toString());
        item.setQuantityUnit(quantityUnit.getText().toString());


        if(!listPrice.getText().toString().equals(""))
        {
            item.setListPrice(Float.parseFloat(listPrice.getText().toString()));
        }

        if(!discountedPrice.getText().toString().equals(""))
        {
            item.setDiscountedPrice(Float.parseFloat(discountedPrice.getText().toString()));
        }



        item.setImageCopyrights(imageCopyrights.getText().toString());
//        item.setBarcode();
//        item.setBarcodeFormat(barcodeFormat);
    }






    public void retrofitPUTRequest()
    {

        getDataFromViews();


        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = itemService.updateItem(
                PrefLogin.getAuthorizationHeader(getContext()),
                item,item.getItemID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                    PrefItem.saveItem(item,getContext());
                }
                else if(response.code()== 403 || response.code() ==401)
                {
                    showToastMessage("Not Permitted ! Contact Staff to Request Permission !");
                }
                else
                {
                    showToastMessage("Update Failed Code : " + response.code());
                }



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Update failed !");


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }





    private void retrofitPOSTRequest()
    {

        getDataFromViews();
        item.setItemCategoryID(itemCategory.getItemCategoryID());

        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

//        System.out.println("Item Category ID (POST) : " + item.getItemCategoryID());

        Call<Item> call = itemService.insertItem(PrefLogin.getAuthorizationHeader(getContext()), item);

        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {



                if(response.code()==201)
                {
                    showToastMessage("Add successful !");

                    current_mode = MODE_UPDATE;
                    updateFieldVisibility();
                    item = response.body();
                    bindDataToViews();

                    PrefItem.saveItem(item,getContext());

                }
                else if(response.code()== 403 || response.code() ==401)
                {
                    showToastMessage("Not Permitted ! Contact Staff to Request Permission !");
                }
                else
                {
                    showToastMessage("Add failed Code : " + response.code());
                }



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                showToastMessage("Add failed !");


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
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






    @OnClick({R.id.textChangePicture,R.id.uploadImage})
    void pickImage() {

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



        clearCache(getContext());

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);





        ImagePicker.Companion.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(2024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1500, 1500)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();




//        Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
//        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
//        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
//        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
//        startActivityForResult(intent, 1213);

    }




    File imagePickedFile;
    String imageFilePath;






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);

//        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
//
//            String filePath = result.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
////            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
//
//            imageFilePath = filePath;
//
//
//            isImageChanged = true;
//            isImageRemoved = false;
//
//            Picasso.get()
//                    .load(filePath)
//                    .into(resultView);
//
//        }



//        choosePhotoHelper.onActivityResult(requestCode, resultCode, result);


//        showToastMessage("Request Code : " + requestCode);



        if(requestCode==49374 && resultCode ==Activity.RESULT_OK)
        {

            IntentResult resultLocal = IntentIntegrator.parseActivityResult(requestCode, resultCode, result);
            if(result != null) {
                if(resultLocal.getContents() == null) {
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();

                } else {


                    UtilityFunctions.showToastMessage(getActivity(), "Scanned: " + resultLocal.getContents());
                    String resultsText = "Barcode : " + resultLocal.getContents();
                    resultsText = resultsText + "\nFormat : " + resultLocal.getFormatName();

//                    barcodeResults.setText(resultLocal.getContents());
//                    barcodeResults.setText(resultLocal.getFormatName());

                    item.setBarcode(resultLocal.getContents());
                    item.setBarcodeFormat(resultLocal.getFormatName());

                    barcodeResults.setText(resultsText);

                }
            } else {
                super.onActivityResult(requestCode, resultCode, result);
            }


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



//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
//                && result != null
//                && result.getData() != null) {
//
//
//            Uri filePath = result.getData();
//
//            //imageUri = filePath;
//
//            if (filePath != null) {
//
//                startCropActivity(result.getData(),getContext());
//            }
//
//        }
//        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP)
//        {
//
//            resultView.setImageURI(null);
//            resultView.setImageURI(UCrop.getOutput(result));
//
//            isImageChanged = true;
//            isImageRemoved = false;
//
//
//        } else if (resultCode == UCrop.RESULT_ERROR) {
//
//            final Throwable cropError = UCrop.getError(result);
//
//        }

    }





    @BindView(R.id.barcode_results) TextView barcodeResults;










    /*

    // Code for Uploading Image

     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);



        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showToastMessage("Permission Granted !");
                    pickImage();
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





    public void uploadPickedImage(final boolean isModeEdit)
    {

        Log.d("applog", "onClickUploadImage");


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



//        file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");

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






        buttonUpdateItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<Image> imageCall = itemService.uploadImage(
                PrefLogin.getAuthorizationHeader(getContext()),
                fileToUpload
        );


        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {

                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");

                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());


                    item.setItemImageURL(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    item.setItemImageURL(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    item.setItemImageURL(null);

                }

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }



                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {

                showToastMessage("Image Upload failed !");
                item.setItemImageURL(null);


                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }


                buttonUpdateItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }




    private void deleteImage(String filename)
    {
        Call<ResponseBody> call = itemService.deleteImage(
                PrefLogin.getAuthorizationHeader(getContext()),
                filename);



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    if(response.code()==200)
                    {
                        showToastMessage("Image Removed !");
                    }
                    else
                    {
//                        showToastMessage("Image Delete failed");
                    }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                showToastMessage("Image Delete failed");
            }
        });
    }





    @Override
    public void addItemClick() {

        Intent intent = new Intent(getActivity(), EditItemImage.class);
        intent.putExtra(EditItemImageFragment.EDIT_MODE_INTENT_KEY,EditItemImageFragment.MODE_ADD);
        intent.putExtra(EditItemImageFragment.ITEM_ID_INTENT_KEY,item.getItemID());
        startActivity(intent);

    }


    @Override
    public void editItemImage(ItemImage itemImage, int position) {

        Intent intent = new Intent(getActivity(), EditItemImage.class);
        intent.putExtra(EditItemImageFragment.EDIT_MODE_INTENT_KEY,EditItemImageFragment.MODE_UPDATE);
        intent.putExtra(EditItemImageFragment.ITEM_ID_INTENT_KEY,item.getItemID());
        PrefItemImage.saveItemImage(itemImage,getActivity());
        startActivity(intent);
    }





    @OnClick(R.id.sync_refresh)
    void syncRefreshClick()
    {
        makeNetworkCallItemImages(true);
    }


    @Override
    public void removeItemImage(final ItemImage itemImage, final int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Delete Item Image !")
                .setMessage("Are you sure you want to delete this Item Image ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        makeRequestDeleteItemImage(itemImage, position);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }






    private void makeRequestDeleteItemImage(ItemImage itemImage, final int position)
    {
        Call<ResponseBody> call = itemImageService.deleteItemImageData(
                PrefLogin.getAuthorizationHeader(getActivity()),
                itemImage.getImageID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Removed !");

                    dataset.remove(position);
                    adapterItemImages.notifyItemRemoved(position+1);


                }
                else if(response.code()==401 || response.code()==403)
                {
                    showToastMessage("Not Permitted ! Contact Staff to Request Permission !");
                }
                else
                {
                    showToastMessage("Failed Code : " + response.code());
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Failed to Delete !");
            }
        });

    }




    @Override
    public void addItemSpec() {

//        Intent intent = new Intent(getActivity(), FilterItemsActivity.class);
//        intent.putExtra(FilterItemsFragment.ITEM_ID_INTENT_KEY,item.getItemID());
//        startActivity(intent);
    }





    @OnClick(R.id.barcode_image)
    void setupBarcodes() {
//        IntentIntegrator integrator = new IntentIntegrator(getActivity());
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        integrator.setPrompt("Scan a barcode");
//        integrator.setCameraId(0);  // Use a specific camera of the device
//        integrator.setBeepEnabled(false);
//        integrator.setBarcodeImageEnabled(true);
//        integrator.initiateScan();


        IntentIntegrator.forSupportFragment(this)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("Scan a barcode")
                .setCameraId(0)
                .setBeepEnabled(true)
                .setBarcodeImageEnabled(true)
                .setOrientationLocked(true)
                .initiateScan();

    }





}

