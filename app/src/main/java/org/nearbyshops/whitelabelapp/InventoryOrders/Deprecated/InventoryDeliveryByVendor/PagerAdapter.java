package org.nearbyshops.whitelabelapp.InventoryOrders.Deprecated.InventoryDeliveryByVendor;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.nearbyshops.whitelabelapp.Model.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.whitelabelapp.InventoryOrders.Deprecated.InventoryDeliveryByVendor.Fragment.DeliveryByVendorFragment;


/**
 * Created by sumeet on 15/6/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {



    public PagerAdapter(FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }





    @Override
    public Fragment getItem(int position) {


        if(position==0)
        {
            return DeliveryByVendorFragment.newInstance(OrderStatusHomeDelivery.HANDOVER_REQUESTED);

        }else if(position == 1)
        {
            return DeliveryByVendorFragment.newInstance(OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
        }
        else if(position==2)
        {
            return DeliveryByVendorFragment.newInstance(OrderStatusHomeDelivery.RETURN_REQUESTED);
        }
        else if(position==3)
        {
            return DeliveryByVendorFragment.newInstance(OrderStatusHomeDelivery.DELIVERED);
        }



        return null;
    }




    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

//            case 0:
//                return titleOrderPacked;

            case 0:
                return titlePendingHandover;
            case 1:
                return titleConfirmed;
            case 2:
                return titleReturnPending;
            case 3:
                return titlePaymentPending;

        }
        return null;
    }








    private String titleOrderPacked = "Order Packed";
    private String titlePendingHandover = "Handover Requested";
    private String titleConfirmed = "Out For Delivery";
    private String titleReturnPending = "Return Pending";
    private String titlePaymentPending = "Payment Pending";



//    private String titlePendingHandover = "Handover Requested (0/0)";
//    private String titleConfirmed = "Out For Delivery (0/0)";
//    private String titleReturnPending = "Return Pending (0/0)";
//    private String titlePaymentPending = "Payment Pending (0/0)";



    public void setTitle(String title, int tabPosition)
    {


        if(tabPosition == 0){

            titlePendingHandover = title;
        }
        else if (tabPosition == 1)
        {
            titleConfirmed = title;

        }else if(tabPosition == 2)
        {
            titleReturnPending = title;
        }
        else if(tabPosition==3)
        {
            titlePaymentPending=title;
        }

        notifyDataSetChanged();
    }



}