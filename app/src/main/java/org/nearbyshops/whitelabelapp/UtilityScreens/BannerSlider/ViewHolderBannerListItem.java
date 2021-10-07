package org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.whitelabelapp.API.BannerImageService;
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailShopItem.ShopItemDetail;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditBannerImage.EditBannerImage;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditBannerImage.EditBannerImageFragment;
import org.nearbyshops.whitelabelapp.Lists.ItemsInShopByCategory.ItemsInShopByCat;
import org.nearbyshops.whitelabelapp.Lists.ShopsAvailableNew.ShopsAvailable;
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User;
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral;
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.Model.BannerImage;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewHolderBannerListItem extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {



    private Context context;
    private BannerImage bannerImage;
    private Fragment fragment;

    private RecyclerView.Adapter adapter;



    @Inject
    BannerImageService bannerImageService;




    @BindView(R.id.list_item)
    CardView listItem;

    @BindView(R.id.banner_image)
    ImageView bannerImageView;

    @BindView(R.id.image_loading) ProgressBar bannerImageLoading;



    @BindView(R.id.more_options) ImageView moreOptions;



    public static ViewHolderBannerListItem create(ViewGroup parent, Context context, Fragment fragment, RecyclerView.Adapter adapter) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_banner_image,parent,false);
        return new ViewHolderBannerListItem(view,context,fragment,adapter);
    }






    public ViewHolderBannerListItem(View itemView, Context context, Fragment fragment, RecyclerView.Adapter adapter)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);


        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
    }







    public void setItem(BannerImage highlight)
    {
        this.bannerImage = highlight;

        if(PrefLogin.getUser(context)!=null && PrefLogin.getUser(context).getRole()== User.ROLE_ADMIN_CODE)
        {
            moreOptions.setVisibility(View.VISIBLE);
        }

        String imagePath = PrefGeneral.getServerURL(context) + "/api/v1/BannerImages/Image/" + "seven_hundred_"+ bannerImage.getImageFilename() + ".jpg";

        bannerImageLoading.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(imagePath)
                .into(bannerImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        bannerImageLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        bannerImageLoading.setVisibility(View.GONE);
                    }
                });
    }





    @OnClick(R.id.more_options)
    void optionsOverflowClick(View v)
    {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.list_item_banner_item_overflow, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }







    @Override
    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.action_remove:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirm Delete Banner !")
                        .setMessage("Are you sure you want to delete this Banner ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteBanner();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                UtilityFunctions.showToastMessage(context,"Cancelled !");
                            }
                        })
                        .show();
                break;


            case R.id.action_edit:


                Intent intent = new Intent(context, EditBannerImage.class);
                intent.putExtra(EditBannerImageFragment.EDIT_MODE_INTENT_KEY,EditBannerImageFragment.MODE_UPDATE);
                intent.putExtra(EditBannerImageFragment.BANNER_ID_INTENT_KEY, bannerImage.getBannerImageID());
                context.startActivity(intent);


//                UtilityFunctions.showToastMessage(context,"Edit Clicked !");

                break;



            default:

                break;

        }




        return false;

    }





    @OnClick(R.id.list_item)
    void listItemClick()
    {

        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyBannerSelected(bannerImage);
        }


        if(bannerImage.getShopIdToOpen()>0 && bannerImage.getItemIDToOpen() >0)
        {

            Intent intent = new Intent(context, ShopItemDetail.class);
            intent.putExtra("item_id",bannerImage.getItemIDToOpen());
            intent.putExtra("shop_id",bannerImage.getShopIdToOpen());
            context.startActivity(intent);

        }
        else if(bannerImage.getShopIdToOpen()>0 && bannerImage.getItemIDToOpen()==0)
        {
//            ShopDetail.class
            // make shop home null
            PrefShopHome.saveShop(null,context);
            Intent intent = new Intent(context, ItemsInShopByCat.class);
            intent.putExtra("shop_id",bannerImage.getShopIdToOpen());
            context.startActivity(intent);
        }
        else if(bannerImage.getShopIdToOpen()==0 && bannerImage.getItemIDToOpen()>0)
        {

            Intent intent = new Intent(context, ShopsAvailable.class);
            intent.putExtra("item_id",bannerImage.getItemIDToOpen());
//            intent.putExtra("item_json",UtilityFunctions.provideGson().toJson(item));
            context.startActivity(intent);

        }

    }




    public interface ListItemClick
    {
        void notifyBannerSelected(BannerImage selectedItem);
        void bannerDeleted();
    }






    void deleteBanner()
    {

        Call<ResponseBody> call = bannerImageService.deleteBannerImage(
                PrefLogin.getAuthorizationHeader(context),
                bannerImage.getBannerImageID()
        );



        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {

                    UtilityFunctions.showToastMessage(context,"Delete Successful !");

                    adapter.notifyDataSetChanged();


                    if(fragment instanceof ListItemClick)
                    {
                        ((ListItemClick) fragment).bannerDeleted();
                    }
                }
                else
                {
                    UtilityFunctions.showToastMessage(context,"Failed Code : " + response.code());
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                UtilityFunctions.showToastMessage(context,"Delete Failed ! ");
            }
        });



    }


}// ViewHolder Class declaration ends


