package org.nearbyshops.enduserappnew.aSellerModule.InventoryOrders.PickFromShopInventory;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.nearbyshops.enduserappnew.Model.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.enduserappnew.aSellerModule.InventoryOrders.Fragment.OrdersInventoryFragment;


/**
 * Created by sumeet on 23/12/16.
 */


public class PagerAdapter extends FragmentPagerAdapter {



    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        if(position==0)
        {
            return OrdersInventoryFragment.newInstance(OrderStatusPickFromShop.ORDER_PLACED,true);
        }
        else if(position==1)
        {
            return OrdersInventoryFragment.newInstance(OrderStatusPickFromShop.ORDER_CONFIRMED,true);
        }
        else if(position==2)
        {
            return OrdersInventoryFragment.newInstance(OrderStatusPickFromShop.ORDER_PACKED,true);
        }
        else if(position==3)
        {
            return OrdersInventoryFragment.newInstance(OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP,true);
        }
        else if(position==4)
        {
            return OrdersInventoryFragment.newInstance(OrderStatusPickFromShop.DELIVERED,true);
        }



        return OrdersInventoryFragment.newInstance(OrderStatusPickFromShop.ORDER_PLACED,true);
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
                return titlePlaced;
            case 1:
                return titleConfirmed;
            case 2:
                return titlePacked;
            case 3:
                return titleReadyForPickup;
            case 4:
                return titleDeliveryPending;
        }


        return null;
    }






    private String titlePlaced = "Placed (0/0)";
    private String titleConfirmed = "Confirmed (0/0)";
    private String titlePacked = "Packed (0/0)";
    private String titlePaymentsPending = "Payments Pending (0/0)";
    private String titleReadyForPickup = "Ready For Pickup (0/0)";
    private String titleDeliveryPending = "Delivered (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titlePlaced = "Placed " + title;
        }
        else if (tabPosition == 1)
        {

            titleConfirmed = "Confirmed " + title;
        }
        else if(tabPosition==2)
        {
            titlePacked = "Packed " + title;
        }
        else if(tabPosition==3)
        {
            titleReadyForPickup = "Ready for Pickup " + title;
        }
        else if(tabPosition==4)
        {
            titleDeliveryPending = "Delivered " + title;
        }


//        else if(tabPosition==4)
//        {
//            titlePaymentsPending = title;
//        }

        notifyDataSetChanged();
    }




}
