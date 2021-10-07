package org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.whitelabelapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderEmptyScreenListItem extends RecyclerView.ViewHolder{



    private Context context;
    private Fragment fragment;

    @BindView(R.id.title) TextView title;
    @BindView(R.id.message) TextView message;
    @BindView(R.id.button) TextView button;
    @BindView(R.id.image) ImageView graphicImage;


    private EmptyScreenDataListItem data;


    public static int LAYOUT_TYPE_GRAPHIC_WITH_TEXT = 1;
    public static int LAYOUT_TYPE_FULL_GRAPHIC = 2;



//    Create your own Market and help local Economy ... Its free !




    public static ViewHolderEmptyScreenListItem create(ViewGroup parent, Context context, Fragment fragment, int layoutType)
    {
        View view = null;

        if(layoutType==LAYOUT_TYPE_GRAPHIC_WITH_TEXT)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_empty_screen, parent, false);
        }
        else if(layoutType==LAYOUT_TYPE_FULL_GRAPHIC)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_full_graphic, parent, false);
        }

        return new ViewHolderEmptyScreenListItem(view,parent,context, fragment);
    }



    public static ViewHolderEmptyScreenListItem create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = null;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_empty_screen, parent, false);

        return new ViewHolderEmptyScreenListItem(view,parent,context, fragment);
    }




    public ViewHolderEmptyScreenListItem(View itemView, ViewGroup parent, Context context, Fragment fragment)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }



    @OnClick(R.id.button)
    void selectMarket()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemButtonClick(data.getUrlForButtonClick());
        }
    }



    public void setItem(EmptyScreenDataListItem data)
    {
        this.data = data;

        message.setText(data.getMessage());

        title.setText(data.getTitle());

        if(data.getButtonText()==null)
        {
            button.setVisibility(View.GONE);
        }
        else
        {
            button.setVisibility(View.VISIBLE);
            button.setText(data.getButtonText());
        }





        if(data.getImageResource()==0)
        {
            graphicImage.setVisibility(View.GONE);
        }
        else
        {
            graphicImage.setVisibility(View.VISIBLE);
            graphicImage.setImageResource(data.getImageResource());
        }

    }



    public interface ListItemClick
    {
        void listItemButtonClick(String url);
    }




    public static class EmptyScreenDataListItem {


        private String title;
        private String message;
        private String buttonText;
        private String urlForButtonClick;
        private int imageResource;
        private int layoutType;



        public static EmptyScreenDataListItem noSearchResults()
        {
            EmptyScreenDataListItem data = new EmptyScreenDataListItem();
            data.setTitle("Nothing found in Search");
    //        data.setShowCopyrightInfo(false);
            data.setMessage("No results found for your search query ... kindly try again with a different query");
//            data.setImageResource(R.drawable.);
    //        data.setButtonText("Change Market");

            return data;
        }



        public static EmptyScreenDataListItem getEmptyScreenShopsListMultiMarket()
        {
            EmptyScreenDataListItem data = new EmptyScreenDataListItem();
            data.setTitle("No Shops at your Location");
    //        data.setShowCopyrightInfo(false);
            data.setMessage("Uh .. oh .. no shops available at your location .. try to change your market ... or explore other markets !");
            data.setImageResource(R.drawable.ic_barcode);
            data.setButtonText("Change Market");

            return data;
        }



        public static EmptyScreenDataListItem getEmptyScreenShopsListSingleMarket()
        {
            EmptyScreenDataListItem data = new EmptyScreenDataListItem();
            data.setTitle("No Shops at your Location");
    //        data.setShowCopyrightInfo(false);
            data.setMessage("Uh .. oh .. no shops available at your location .. change your location ... and try again");
            data.setImageResource(R.drawable.ic_barcode);
            data.setButtonText("Try Again");

            data.setLayoutType(LAYOUT_TYPE_GRAPHIC_WITH_TEXT);

            return data;
        }




        public static EmptyScreenDataListItem getFullGraphicInfo(int imageResourceCode)
        {
            EmptyScreenDataListItem data = new EmptyScreenDataListItem();
            data.setImageResource(imageResourceCode);
            data.setLayoutType(LAYOUT_TYPE_FULL_GRAPHIC);

            return data;
        }




        public static EmptyScreenDataListItem getCreateMarketData(Context context)
        {
            EmptyScreenDataListItem data = new EmptyScreenDataListItem();
            data.setTitle("Create Market");
            data.setMessage("Create your own Market ... help local Economy ... !");
            data.setButtonText("Create Market");

            return data;
        }




        public static EmptyScreenDataListItem createMarketNoMarketsAvailable(Context context)
        {
            EmptyScreenDataListItem data = new EmptyScreenDataListItem();
            data.setTitle("Create Market");
            data.setMessage("No markets available in your area. \n\nCreate your own local market using free open-source technology and Help Local Vendors and Local Economy");
            data.setButtonText("Create Market");

    //        data.setImageResource(R.drawable.ic_local_florist_black_24dp);

            return data;
        }





        public static EmptyScreenDataListItem getNotAvailableAtYourLocation(boolean showButton)
        {
            EmptyScreenDataListItem data = new EmptyScreenDataListItem();
            data.setTitle("No Shops Available");
            data.setMessage("No shops are currently available at your location ! Create your shop to help local economy !");

            if(showButton)
            {
                data.setButtonText("Create Shop");
            }

            data.setLayoutType(ViewHolderEmptyScreenListItem.LAYOUT_TYPE_GRAPHIC_WITH_TEXT);

//            data.setImageResource(R.drawable.ic_location_off);
            data.setImageResource(R.drawable.ic_storefront);

            return data;
        }




        public int getLayoutType() {
            return layoutType;
        }

        public void setLayoutType(int layoutType) {
            this.layoutType = layoutType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getImageResource() {
            return imageResource;
        }

        public void setImageResource(int imageResource) {
            this.imageResource = imageResource;
        }

        public String getUrlForButtonClick() {
            return urlForButtonClick;
        }

        public void setUrlForButtonClick(String urlForButtonClick) {
            this.urlForButtonClick = urlForButtonClick;
        }

        public String getButtonText() {
            return buttonText;
        }

        public void setButtonText(String buttonText) {
            this.buttonText = buttonText;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}



