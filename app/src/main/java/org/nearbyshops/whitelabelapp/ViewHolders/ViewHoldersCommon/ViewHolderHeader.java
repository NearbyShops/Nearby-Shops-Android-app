package org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.nearbyshops.whitelabelapp.R;


public class ViewHolderHeader extends RecyclerView.ViewHolder{



    @BindView(R.id.header) TextView header;


    private Context context;
    private HeaderTitle item;

    public static int LAYOUT_TYPE_SIMPLE = 1;
    public static int LAYOUT_TYPE_MARGIN_10 = 2;
    public static int LAYOUT_TYPE_WHITE_NO_BACKGROUND = 3;
    public static int LAYOUT_TYPE_GREY_BACKGROUND = 4;




    public static ViewHolderHeader create(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_header_type_simple, parent, false);


        return new ViewHolderHeader(view,context);
    }





    public static ViewHolderHeader create(ViewGroup parent, Context context, int layoutType)
    {

        View view = null;

        if(layoutType==LAYOUT_TYPE_SIMPLE)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_header_type_simple, parent, false);
        }
        else if(layoutType== LAYOUT_TYPE_MARGIN_10)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_header_margin_10, parent, false);
        }
        else if(layoutType== LAYOUT_TYPE_WHITE_NO_BACKGROUND)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_header_white, parent, false);
        }
        else if(layoutType== LAYOUT_TYPE_GREY_BACKGROUND)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_header_grey_back, parent, false);
        }


        return new ViewHolderHeader(view,context);
    }




    public static ViewHolderHeader createWhite(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_header_white, parent, false);


        return new ViewHolderHeader(view,context);
    }



    public static ViewHolderHeader createBoldAndBig(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_header_bold, parent, false);


        return new ViewHolderHeader(view,context);
    }


    public static ViewHolderHeader createGreyBG(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_header_grey_back, parent, false);


        return new ViewHolderHeader(view,context);
    }




    public static ViewHolderHeader createHeaderExtraMargin(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_header_extra_margin, parent, false);


        return new ViewHolderHeader(view,context);
    }








    public ViewHolderHeader(View itemView, Context context) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
    }


    public void setItem(HeaderTitle data)
    {
        header.setText(data.getHeading());

        if(data.getBackgroundColor()>0)
        {
            header.setBackgroundColor(ContextCompat.getColor(context,data.backgroundColor));
        }

        if(data.getTextColor()>0)
        {
            header.setTextColor(ContextCompat.getColor(context,data.textColor));
        }
    }


    /**
     * Created by sumeet on 4/12/16.
     */

    public static class HeaderTitle {

        private String heading;
        private int backgroundColor;
        private int textColor;

        public HeaderTitle(String heading) {
            this.heading = heading;
        }


        public HeaderTitle() {
        }

        public HeaderTitle(String heading, int backgroundColor, int textColor) {
            this.heading = heading;
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public String getHeading() {
            return heading;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }
    }
}

