package org.nearbyshops.enduserappnew.aSellerModule.QuickStockEditor;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by sumeet on 16/6/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // getItem is called to instantiate the fragment for the given page.
        // Return a FragmentOutOfStock_ (defined as a static inner class below).

        if(position == 0)
        {
            return FragmentShopItem.newInstance(FragmentShopItem.MODE_LOW_STOCK);
        }
        else if(position == 1)
        {
            return FragmentShopItem.newInstance(FragmentShopItem.MODE_OUT_OF_STOCK);

        }else if(position == 2)
        {
            return FragmentShopItem.newInstance(FragmentShopItem.MODE_PRICE_NOT_SET);
        }
        else if(position == 3)
        {
            return FragmentShopItem.newInstance(FragmentShopItem.MODE_RECENTLY_ADDED);
        }
        else if(position == 4)
        {
            return FragmentShopItem.newInstance(FragmentShopItem.MODE_RECENTLY_UPDATED);
        }


        return  FragmentShopItem.newInstance(FragmentShopItem.MODE_LOW_STOCK);
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

                return titleLowStock;
            case 1:

                return titleOutOfStock;
            case 2:

                return titlePriceNotSet;
            case 3:

                return titleRecentlyAdded;

            case 4:
                return titleRecentlyUpdated;

        }
        return null;
    }





    private String titleRecentlyAdded = "Recently Added (0/0)";
    private String titleLowStock = "Low Stock (0/0)";
    private String titleOutOfStock = "Out of Stock (0/0)";
    private String titlePriceNotSet = "Price not Set (0/0)";
    private String titleRecentlyUpdated = "Recently Updated (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleLowStock = title;
        }
        else if (tabPosition == 1)
        {
            titleOutOfStock = title;

        }else if(tabPosition == 2)
        {
            titlePriceNotSet = title;

        }else if(tabPosition == 3)
        {

            titleRecentlyAdded = title;
        }
        else if(tabPosition == 4)
        {
            titleRecentlyUpdated = title;
        }


        notifyDataSetChanged();
    }

}
