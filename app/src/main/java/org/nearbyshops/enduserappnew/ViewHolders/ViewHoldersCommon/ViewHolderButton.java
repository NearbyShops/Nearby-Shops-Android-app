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
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.ButtonData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderButton extends RecyclerView.ViewHolder{




    @BindView(R.id.header) TextView header;
    @BindView(R.id.icon) ImageView icon;


    private Context context;
    private Fragment fragment;

    ButtonData data;

    public static int LAYOUT_TYPE_CLEAR_ALL = 1;
    public static int LAYOUT_TYPE_MENU_ITEM = 2;
    public static int LAYOUT_TYPE_ADD_NEW_LOCATION = 3;




    public static ViewHolderButton create(ViewGroup parent, Context context, Fragment fragment, int layoutType)
    {
        View view = null;

        if(layoutType==LAYOUT_TYPE_CLEAR_ALL)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_button_clear_all,parent,false);
        }
        else if(layoutType==LAYOUT_TYPE_MENU_ITEM)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_button_menu_item,parent,false);

        }
        else if(layoutType==LAYOUT_TYPE_ADD_NEW_LOCATION)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_button_add_new_address,parent,false);

        }




        return new ViewHolderButton(view,context,fragment);
    }





    public ViewHolderButton(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }








    public void setItem(ButtonData data)
    {
        this.data = data;

        header.setText(data.getHeading());
        icon.setImageResource(data.getIcon());
    }









    @OnClick({R.id.list_item})
    void changeLocation()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).buttonClick(data);
        }

        if(context instanceof ListItemClick)
        {
            ((ListItemClick) context).buttonClick(data);
        }
    }






    public interface ListItemClick
    {
        void buttonClick(ButtonData data);
    }



}

