package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.WhatsMessageData;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderWhatsApp extends RecyclerView.ViewHolder{




    private Context context;
    private Fragment fragment;
    private WhatsMessageData data;



    public static ViewHolderWhatsApp create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_whatsapp_messaging, parent, false);

        return new ViewHolderWhatsApp(view,context, fragment);
    }




    public ViewHolderWhatsApp(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }





    public void setItem(WhatsMessageData data)
    {
        this.data = data;


    }






    @OnClick(R.id.list_item)
    void listItemClick()
    {
        String url = "https://api.whatsapp.com/send?phone=" + data.getShop().getCustomerHelplineNumber();

        System.out.println("Whatapp URL : " + url);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }





    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }



    public interface ListItemClick
    {
        void listItemClick();
    }

}

