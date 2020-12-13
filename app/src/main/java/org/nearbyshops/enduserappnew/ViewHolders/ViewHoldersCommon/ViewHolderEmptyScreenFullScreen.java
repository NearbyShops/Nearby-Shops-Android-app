package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderEmptyScreenFullScreen extends RecyclerView.ViewHolder{



    @BindView(R.id.graphic_image) ImageView graphicImage;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.message) TextView message;
    @BindView(R.id.copyrights) TextView designedByFreepik;
    @BindView(R.id.button) TextView button;


    private Context context;
    private Fragment fragment;


    private EmptyScreenDataFullScreen data;




    public static ViewHolderEmptyScreenFullScreen create(ViewGroup parent, Context context,Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_empty_screen_full_screen,parent,false);

        return new ViewHolderEmptyScreenFullScreen(view,context,fragment);
    }





    public ViewHolderEmptyScreenFullScreen(View itemView, Context context,Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }





    public void setItem(EmptyScreenDataFullScreen data)
    {

        this.data = data;

        title.setText(data.getTitle());
        message.setText(data.getMessage());
        graphicImage.setImageResource(data.getDrawableResource());



        if(data.getButtonName()==null || data.getButtonName().equals(""))
        {
            button.setVisibility(View.GONE);
        }
        else
        {
            button.setText(data.getButtonName());
        }




        if(data.isShowCopyrightInfo())
        {
            designedByFreepik.setVisibility(View.VISIBLE);
        }
        else
        {
            designedByFreepik.setVisibility(View.GONE);
        }
    }



    @OnClick(R.id.button)
    void buttonClicked()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).emptyScreenButtonClicked();
        }
        else if(context instanceof ListItemClick)
        {
            ((ListItemClick) context).emptyScreenButtonClicked();
        }
    }





    public interface ListItemClick{
        void emptyScreenButtonClicked();
    }



}

