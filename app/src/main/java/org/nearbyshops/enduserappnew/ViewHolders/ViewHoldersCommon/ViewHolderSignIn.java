package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;


public class ViewHolderSignIn extends RecyclerView.ViewHolder{



    private Context context;
    private Fragment fragment;

    @BindView(R.id.message)
    TextView message;




    public static ViewHolderSignIn create(ViewGroup parent, Context context,Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_sign_in, parent, false);

        return new ViewHolderSignIn(view,parent,context,fragment);
    }





    public ViewHolderSignIn(View itemView, ViewGroup parent, Context context,Fragment fragment)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;



        if(!PrefGeneral.isMultiMarketEnabled(context))
        {
            message.setText("Sign In to view, rate and review your favourite Shops !");
        }
        else
        {
            message.setText("Sign In to view, rate and review your favourite Markets !");
        }
    }








    @OnClick(R.id.sign_in_button)
    void selectMarket()
    {
        if(fragment instanceof VHSignIn)
        {
            ((VHSignIn) fragment).signInClick();
        }
    }


    public interface VHSignIn
    {
        void signInClick();
    }

}



