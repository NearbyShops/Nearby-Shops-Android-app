package org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersOrders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.OrderDetail;
import org.nearbyshops.enduserappnew.InventoryOrders.ViewModelOrders;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderOrderMarketAdmin extends ViewHolderOrder {


    @BindView(R.id.button_left) TextView buttonLeft;
    @BindView(R.id.progress_left) ProgressBar progressLeft;

    @BindView(R.id.button_right) TextView buttonRight;
    @BindView(R.id.progress_right) ProgressBar progressRight;

    @BindView(R.id.pickup_directions) TextView pickupDirections;
    @BindView(R.id.delivery_directions) TextView deliveryDirections;
    @BindView(R.id.delivery_boy_info) TextView deliveryBoyInfo;




    private Context context;
    private Order order;
    private Fragment fragment;



    public static ViewHolderOrderMarketAdmin create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_double,parent,false);

        return new ViewHolderOrderMarketAdmin(view,context,fragment);
    }







    public ViewHolderOrderMarketAdmin(View itemView, Context context, Fragment fragment) {
        super(itemView,context,fragment);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;

        setupViewModel();
    }






    @OnClick(R.id.close_button)
    void closeButton(View view) {

    }





    @OnClick(R.id.list_item)
    void listItemClick ()
    {

        fragment.startActivity(OrderDetail.getLaunchIntent(order.getOrderID(),fragment.getActivity()));

        if (fragment instanceof ListItemClick) {

            ((ListItemClick) fragment).notifyOrderSelected(order);
        }
    }






    public void setItem (Order order)
    {
        super.setItem(order);
        this.order = order;



        setupButtons();
    }




    void setupButtons()
    {
        String buttonLeftTitle = "";
        String buttonRightTitle = "";


        buttonLeft.setVisibility(View.GONE);
        buttonRight.setVisibility(View.GONE);

        if(order.getDeliveryGuySelfID()>0 && order.getRt_delivery_guy_profile()!=null)
        {
            deliveryBoyInfo.setVisibility(View.VISIBLE);

            User deliveryBoy = order.getRt_delivery_guy_profile();
            deliveryBoyInfo.setText("Delivered By : " + deliveryBoy.getName() + " | ID : " + String.valueOf(deliveryBoy.getUserID()) + "");
        }
        else
        {
            deliveryBoyInfo.setVisibility(View.GONE);
        }



        pickupDirections.setText(String.format("%.2f Kms | Pickup Directions",order.getRt_pickupDistance()));
        deliveryDirections.setText(String.format("%.2f Kms | Delivery Directions",order.getRt_deliveryDistance()));

        pickupDirections.setVisibility(View.GONE);
        deliveryDirections.setVisibility(View.VISIBLE);
    }









    @OnClick({R.id.pickup_directions})
    void getDirectionsClick()
    {

        Shop shop = order.getShop();

        if(shop!=null)
        {
            getDirections(shop.getLatCenter(),shop.getLonCenter());
        }

    }




    @OnClick({R.id.delivery_directions})
    void getDeliveryDirectionsClick()
    {

        DeliveryAddress deliveryAddress = order.getDeliveryAddress();

        if(deliveryAddress!=null)
        {
            getDirections(deliveryAddress.getLatitude(),deliveryAddress.getLongitude());
        }
    }





    private void getDirections(double lat, double lon)
    {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        fragment.startActivity(mapIntent);
    }







    void seeOnMap(double lat,double lon,String label)
    {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lon + "(" + label + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        fragment.startActivity(mapIntent);
    }







    private ViewModelOrders viewModelOrders;



    void setupViewModel()
    {

        viewModelOrders = new ViewModelOrders(MyApplication.application);


        viewModelOrders.getEvent().observe(fragment, new Observer<Integer>() {
            @Override
            public void onChanged(Integer event) {



                if(event == ViewModelOrders.EVENT_RESPONSE_OK)
                {
                    if(fragment instanceof ListItemClick)
                    {
                        ((ListItemClick) fragment).statusUpdateSuccessful(order,getAdapterPosition());
                    }

                }


                if(event == ViewModelOrders.EVENT_RESPONSE_OK || event== ViewModelOrders.EVENT_NETWORK_FAILED)
                {
                    buttonLeft.setVisibility(View.VISIBLE);
                    progressLeft.setVisibility(View.INVISIBLE);

                    if(buttonRight.getVisibility()==View.INVISIBLE)
                    {
                        buttonRight.setVisibility(View.VISIBLE);
                        progressRight.setVisibility(View.INVISIBLE);
                    }
                }


            }
        });




        viewModelOrders.getMessage().observe(fragment, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                showToastMessage(s);
            }
        });



    }






    void showToastMessage(String message)
    {
        UtilityFunctions.showToastMessage(context,message);
    }





    public interface ListItemClick {

        void notifyOrderSelected(Order order);
//        void notifyCancelOrder(Order order, int position);


        void statusUpdateSuccessful(Order order, int position);
    }


}


