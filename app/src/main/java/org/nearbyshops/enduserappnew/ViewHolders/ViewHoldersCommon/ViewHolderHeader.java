package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;


public class ViewHolderHeader extends RecyclerView.ViewHolder{



    @BindView(R.id.header) TextView header;


    private Context context;
    private HeaderTitle item;

    public static int LAYOUT_TYPE_SIMPLE = 1;
    public static int LAYOUT_TYPE_MARGIN_10 = 2;




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
    }





}

