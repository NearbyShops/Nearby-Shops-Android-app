package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.PoweredByData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.ButtonData;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderPoweredBy extends RecyclerView.ViewHolder{





    private Context context;
    private Fragment fragment;

    PoweredByData data;

    public static int LAYOUT_TYPE_CLEAR_ALL = 1;
    public static int LAYOUT_TYPE_MENU_ITEM = 2;
    public static int LAYOUT_TYPE_ADD_NEW_LOCATION = 3;




    public static ViewHolderPoweredBy create(ViewGroup parent, Context context, Fragment fragment, int layoutType)
    {
        View view = null;

        if(layoutType==LAYOUT_TYPE_CLEAR_ALL)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_powered_by,parent,false);
        }
        else if(layoutType==LAYOUT_TYPE_MENU_ITEM)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_powered_by,parent,false);

        }



        return new ViewHolderPoweredBy(view,context,fragment);
    }





    public static ViewHolderPoweredBy create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = null;


        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_powered_by,parent,false);

        return new ViewHolderPoweredBy(view,context,fragment);
    }




    public ViewHolderPoweredBy(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }








    public void setItem(PoweredByData data)
    {
        this.data = data;
    }






    @OnClick({R.id.list_item})
    void changeLocation()
    {
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse("#"));
//        context.startActivity(i);
    }






    public interface ListItemClick
    {
        void buttonClick(ButtonData data);
    }



}

