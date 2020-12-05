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
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;

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

//    Create your own Market and help local Economy ... Its free !



    public static ViewHolderEmptyScreenListItem create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
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
            ((ListItemClick) fragment).buttonClick(data.getUrlForButtonClick());
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
        void buttonClick(String url);
    }

}



