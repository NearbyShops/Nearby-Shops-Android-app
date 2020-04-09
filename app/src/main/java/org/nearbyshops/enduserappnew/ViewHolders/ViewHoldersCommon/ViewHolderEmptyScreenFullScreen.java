package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewHolderEmptyScreenFullScreen extends RecyclerView.ViewHolder{



    @BindView(R.id.graphic_image) ImageView graphicImage;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.message) TextView message;
    @BindView(R.id.copyrights) TextView designedByFreepik;


    private Context context;


    private EmptyScreenDataFullScreen data;




    public static ViewHolderEmptyScreenFullScreen create(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_empty_screen_full_screen,parent,false);

        return new ViewHolderEmptyScreenFullScreen(view,context);
    }





    public ViewHolderEmptyScreenFullScreen(View itemView, Context context) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
    }





    public void setItem(EmptyScreenDataFullScreen data)
    {

        this.data = data;

        title.setText(data.getTitle());
        message.setText(data.getMessage());
        graphicImage.setImageResource(data.getDrawableResource());

        if(data.isShowDesignedByFreepik())
        {
            designedByFreepik.setVisibility(View.VISIBLE);
        }
        else
        {
            designedByFreepik.setVisibility(View.GONE);
        }
    }


}

