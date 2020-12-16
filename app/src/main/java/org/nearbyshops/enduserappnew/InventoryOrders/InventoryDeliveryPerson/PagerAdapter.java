package org.nearbyshops.enduserappnew.InventoryOrders.InventoryDeliveryPerson;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.nearbyshops.enduserappnew.InventoryOrders.InventoryDeliveryPerson.Fragment.DeliveryFragmentNew;
import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusHomeDelivery;


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
            return DeliveryFragmentNew.newInstance(OrderStatusHomeDelivery.DELIVERY_BOY_UNASSIGNED);
        }
        else if(position==1)
        {
            return DeliveryFragmentNew.newInstance(OrderStatusHomeDelivery.DELIVERY_BOY_ASSIGNED);
        }
        else if(position == 2)
        {
            return DeliveryFragmentNew.newInstance(OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
        }
        else if(position==3)
        {
            return DeliveryFragmentNew.newInstance(OrderStatusHomeDelivery.RETURN_REQUESTED);
        }
        else if(position==4)
        {
            return DeliveryFragmentNew.newInstance(OrderStatusHomeDelivery.DELIVERED);
        }




        return null;
    }





    @Override
    public int getCount() {
        // Show 3 total pages.
        return 5;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return titleOrderPacked;
            case 1:
                return titlePendingHandover;
            case 2:
                return titleConfirmed;
            case 3:
                return titleReturnPending;
            case 4:
                return titlePaymentPending;

        }
        return null;
    }





    private String titleOrderPacked = "Getting Packed";
    private String titlePendingHandover = "Pickup Pending";
    private String titleConfirmed = "Out For Delivery";
    private String titleReturnPending = "Return Pending";
    private String titlePaymentPending = "Payment Pending";



//    private String titlePendingHandover = "Handover Requested (0/0)";
//    private String titleConfirmed = "Out For Delivery (0/0)";
//    private String titleReturnPending = "Return Pending (0/0)";
//    private String titlePaymentPending = "Payment Pending (0/0)";



    public void setTitle(String title, int tabPosition)
    {


        if(tabPosition==0)
        {
            titleOrderPacked = title;
        }
        else if(tabPosition == 1){

            titlePendingHandover = title;
        }
        else if (tabPosition == 2)
        {
            titleConfirmed = title;

        }else if(tabPosition == 3)
        {
            titleReturnPending = title;
        }
        else if(tabPosition==4)
        {
            titlePaymentPending=title;
        }



        notifyDataSetChanged();
    }



}