package org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderButton extends RecyclerView.ViewHolder{




    @BindView(R.id.header) TextView header;
    @BindView(R.id.icon) ImageView icon;
    @BindView(R.id.list_item) LinearLayout listItem;


    private Context context;
    private Fragment fragment;

    ButtonData data;

    public static int LAYOUT_TYPE_CLEAR_ALL = 1;
    public static int LAYOUT_TYPE_MENU_ITEM = 2;
    public static int LAYOUT_TYPE_ADD_NEW_ADDRESS = 3;
    public static int LAYOUT_TYPE_BUTTON_WITH_MARGIN = 4;




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
        else if(layoutType== LAYOUT_TYPE_ADD_NEW_ADDRESS)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_button_add_new_item,parent,false);
        }
        else if(layoutType== LAYOUT_TYPE_BUTTON_WITH_MARGIN)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_button_with_margin,parent,false);
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

        if(data.getHeading()!=null)
        {
            header.setText(data.getHeading());
        }

        if(data.getIcon()>0)
        {
            icon.setImageResource(data.getIcon());
        }


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





    public static class ButtonData {



        public ButtonData(String heading) {
            this.heading = heading;
        }

        public ButtonData(String heading, int icon, int requestCode) {
            this.heading = heading;
            this.icon = icon;
            this.requestCode = requestCode;
        }

        public ButtonData(String heading, int icon, int requestCode, int layoutType) {
            this.heading = heading;
            this.icon = icon;
            this.requestCode = requestCode;
            this.layoutType = layoutType;
        }

        public ButtonData() {
        }



        private String heading;
        private int icon;
        private int requestCode;
        private int layoutType;



        public int getLayoutType() {
            return layoutType;
        }

        public void setLayoutType(int layoutType) {
            this.layoutType = layoutType;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public String getHeading() {
            return heading;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }
    }
}

