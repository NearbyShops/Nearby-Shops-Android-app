package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderWithDeliveryStaff extends ViewHolderOrder {


    @BindView(R.id.button_single) TextView buttonSingle;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    @BindView(R.id.delivery_person_picture) ImageView deliveryPersonPhoto;
    @BindView(R.id.delivery_person_name) TextView deliveryPersonName;
    @BindView(R.id.delivery_person_phone) TextView deliveryPersonPhone;



    private Context context;
    private Order order;
    private Fragment fragment;





    public static ViewHolderOrderWithDeliveryStaff create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_delivery_profile,parent,false);

        return new ViewHolderOrderWithDeliveryStaff(view,context,fragment);
    }







    public ViewHolderOrderWithDeliveryStaff(View itemView, Context context, Fragment fragment) {
        super(itemView,context,fragment);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;
    }








    @OnClick(R.id.close_button)
    void closeButton(View view) {

        if (fragment instanceof ListItemClick) {
            ((ListItemClick) fragment).notifyCancelOrder(order,getItemViewType());
        }
    }





    @OnClick(R.id.list_item)
    void listItemClick ()
    {

        if (fragment instanceof ListItemClick) {

            ((ListItemClick) fragment).notifyOrderSelected(order);
        }
    }




    public void setItem (Order order, String buttonTitle,boolean buttonVisible)
    {
        super.setItem(order);
        this.order = order;


        buttonSingle.setText(buttonTitle);

        if(buttonVisible)
        {
            buttonSingle.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonSingle.setVisibility(View.GONE);
        }


        bindDeliveryProfile();
    }



    private void bindDeliveryProfile()
    {
        User user = order.getRt_delivery_guy_profile();


        deliveryPersonName.setText(user.getName());
        deliveryPersonPhone.setText(user.getPhone());


        String iamgepath = PrefGeneral.getServiceURL(context) + "/api/v1/User/Image/five_hundred_" + user.getProfileImagePath() + ".jpg";

        Picasso.get()
                .load(iamgepath)
                .placeholder(R.drawable.ic_face_black_24px)
                .into(deliveryPersonPhoto);

    }




    @OnClick(R.id.button_single)
    void buttonClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).buttonClickedWithStaff(order,getAdapterPosition(),buttonSingle,progressBar);
        }
    }





    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order, int position);
        void buttonClickedWithStaff(Order order, int position, TextView button, ProgressBar progressBar);
    }


}


