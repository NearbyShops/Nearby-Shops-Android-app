package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;



public class ViewHolderAddItem extends RecyclerView.ViewHolder{



    private Context context;
    private Fragment fragment;
    private ViewHolderCreateShop.CreateShopData item;



    public static ViewHolderAddItem create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_add_item, parent, false);

        return new ViewHolderAddItem(view,context, fragment);
    }




    public ViewHolderAddItem(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }





    public void setItem(ViewHolderCreateShop.CreateShopData data)
    {
        this.item = data;

    }




    @OnClick(R.id.list_item)
    void listItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).addItemClick();
        }
    }




    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }



    public interface ListItemClick
    {
        void addItemClick();
    }

    public static class AddItemData {

        private String headerString;

        public String getHeaderString() {
            return headerString;
        }

        public void setHeaderString(String headerString) {
            this.headerString = headerString;
        }
    }
}

