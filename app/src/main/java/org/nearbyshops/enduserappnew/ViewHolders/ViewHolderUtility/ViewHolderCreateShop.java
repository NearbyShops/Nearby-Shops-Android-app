package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.EditDataScreens.EditShop.EditShop;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.CreateShopData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderCreateShop extends RecyclerView.ViewHolder{



    @BindView(R.id.header) TextView header;
    @BindView(R.id.description) TextView description;


    private Context context;
    private Fragment fragment;
    private CreateShopData item;



    public static ViewHolderCreateShop create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_create_shop, parent, false);

        return new ViewHolderCreateShop(view,context, fragment);
    }




    public ViewHolderCreateShop(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }





    public void setItem(CreateShopData data)
    {
        this.item = data;
//        header.setText(data.getHeaderString());


        if(PrefGeneral.getMultiMarketMode(context))
        {

            Market globalConfig = PrefServiceConfig.getServiceConfigLocal(context);

            if(globalConfig==null)
            {
                return;
            }


            String marketNameString = "in " + globalConfig.getServiceName() + ", " + globalConfig.getCity();

            header.setText("Create Your Shop");
            description.setText(marketNameString);
        }
        else
        {
            header.setText("Create Your Shop !");
            description.setVisibility(View.GONE);
        }

    }






    @OnClick(R.id.list_item)
    void listItemClick()
    {


        if(PrefLogin.getUser(context)==null)
        {

            Intent intent = new Intent(fragment.getActivity(), Login.class);
            fragment.startActivityForResult(intent,57121);
            return;
        }


        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("Create your Shop !")
                .setMessage("Do you want to Create your Shop and become a seller !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                            viewModelShop.becomeASeller();
//                            progressDialog = new ProgressDialog(context);
//                            progressDialog.setMessage("Please wait ... converting you to a seller !");
//                            progressDialog.show();


                        //     open edit shop in edit mode
                        Intent intent = new Intent(context, EditShop.class);
                        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_ADD);
//                            context.startActivity(intent);
                        fragment.startActivityForResult(intent,890);


                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Cancelled !");
                    }
                })
                .show();

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

