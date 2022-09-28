package org.nearbyshops.whitelabelapp.InventoryOrders.InventoryHomeDelivery;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryHomeDelivery.Fragment.InventoryHDFragmentNew;
import org.nearbyshops.whitelabelapp.Model.ModelStatusCodes.OrderStatusHomeDelivery;


/**
 * Created by sumeet on 13/6/16.
 */





public class PagerAdapter extends FragmentPagerAdapter {


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {


        if(position==0)
        {
            return InventoryHDFragmentNew.newInstance(OrderStatusHomeDelivery.ORDER_PLACED);
        }
        else if(position==1)
        {
            return InventoryHDFragmentNew.newInstance(OrderStatusHomeDelivery.ORDER_CONFIRMED);
        }
        else if(position==2)
        {
            return InventoryHDFragmentNew.newInstance(OrderStatusHomeDelivery.ORDER_PACKED);
        }
        else if(position==3)
        {
            return InventoryHDFragmentNew.newInstance(OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
        }
        else if(position==4)
        {
            return InventoryHDFragmentNew.newInstance(OrderStatusHomeDelivery.RETURN_REQUESTED);
        }
        else if(position==5)
        {
            return InventoryHDFragmentNew.newInstance(OrderStatusHomeDelivery.RETURNED_ORDERS);
        }
        else if(position==6)
        {
            return InventoryHDFragmentNew.newInstance(OrderStatusHomeDelivery.DELIVERED);
        }
        else if(position==7)
        {
            return InventoryHDFragmentNew.newInstance(OrderStatusHomeDelivery.PAYMENT_RECEIVED);
        }



        return null;
    }






    @Override
    public int getCount() {
        // Show 3 total pages.
        return 8;
    }





    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return titlePlaced;
            case 1:
                return titleConfirmed;
            case 2:
                return titlePacked;
            case 3:
                return titleOutForDelivery;
            case 4:
                return titleReturnRequested;
            case 5:
                return titleReturnedOrders;
            case 6:
                return titlePaymentReceived;

            case 7:
                return titleDelivered;


//            case 3:
//                return titlePendingHandover;

        }
        return null;
    }





    private String titlePlaced = "Placed";
    private String titleConfirmed = "Confirmed";
    private String titlePacked = "Packed";

    private String titlePendingHandover = "Handover Requested";
    private String titleOutForDelivery = "Out For Delivery";
    private String titleReturnRequested = "Return Requested";
    private String titleReturnedOrders = "Returned Orders";
    private String titlePaymentReceived = "Payment Pending";
    private String titleDelivered = "Delivered";



//    private String titlePlaced = "Placed (0/0)";
//    private String titleConfirmed = "Confirmed (0/0)";
//    private String titlePacked = "Packed (0/0)";
//
//    private String titlePendingHandover = "Handover Requested (0/0)";
//    private String titleOutForDelivery = "Out For Delivery (0/0)";
//    private String titleReturnRequested = "Return Requested (0/0)";
//    private String titleReturnedOrders = "Returned Orders (0/0)";
//    private String titleDelivered = "Delivered (0/0)";
//    private String titlePaymentReceived = "Payment Received (0/0)";



    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titlePlaced = title;
        }
        else if (tabPosition == 1)
        {

            titleConfirmed = title;
        }else if(tabPosition == 2)
        {
            titlePacked = title;

        }
        else if(tabPosition == 3){

            titlePendingHandover = title;
        }
        else if (tabPosition == 4)
        {
            titleOutForDelivery = title;

        }
        else if(tabPosition==5)
        {
            titleReturnRequested= title;
        }
        else if(tabPosition==6)
        {
            titleReturnedOrders=title;
        }
        else if(tabPosition==7)
        {
            titleDelivered=title;
        }
        else if(tabPosition==8)
        {
            titlePaymentReceived=title;
        }


        notifyDataSetChanged();
    }




}

