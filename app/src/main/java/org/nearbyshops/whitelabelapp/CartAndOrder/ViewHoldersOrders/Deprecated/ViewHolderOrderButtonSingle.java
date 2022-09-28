package org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.Deprecated;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderOrder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderButtonSingle extends ViewHolderOrder {


    @BindView(R.id.button_single) TextView buttonSingle;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    private Context context;
    private Order order;
    private Fragment fragment;



    public static ViewHolderOrderButtonSingle create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_single,parent,false);

        return new ViewHolderOrderButtonSingle(view,context,fragment);
    }






    public ViewHolderOrderButtonSingle(View itemView, Context context, Fragment fragment) {
        super(itemView,context,fragment);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;
    }






    @OnClick(R.id.close_button)
    void closeButton(View view) {

        if (fragment instanceof ListItemClick) {
            ((ListItemClick) fragment).notifyCancelOrder(order,getAdapterPosition());
        }
    }





    @OnClick(R.id.list_item)
    void listItemClick ()
    {

        if (fragment instanceof ListItemClick) {

            ((ListItemClick) fragment).notifyOrderSelected(order);
        }
    }






    public void setItem (Order order, String buttonTitle)
    {
        super.setItem(order);
        this.order = order;

        buttonSingle.setText(buttonTitle);
    }





    @OnClick(R.id.button_single)
    void leftButtonClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).buttonClicked(order,getAdapterPosition(),buttonSingle,progressBar);
        }
    }





    public interface ListItemClick {
        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order, int position);
        void buttonClicked(Order order, int position, TextView button, ProgressBar progressBar);
    }

}


