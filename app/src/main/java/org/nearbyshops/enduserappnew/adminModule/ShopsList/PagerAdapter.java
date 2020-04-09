package org.nearbyshops.enduserappnew.adminModule.ShopsList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.nearbyshops.enduserappnew.adminModule.ShopsList.Fragment.FragmentShopList;

/**
 * Created by sumeet on 24/11/16.
 */


public class PagerAdapter extends FragmentPagerAdapter {


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a FragmentShopApprovals (defined as a static inner class below).

        if (position == 0)
        {
            return FragmentShopList.newInstance(FragmentShopList.MODE_NEW);
        }
        else if(position==1)
        {
            return FragmentShopList.newInstance(FragmentShopList.MODE_ENABLED);
        }
        else if(position==2)
        {
            return FragmentShopList.newInstance(FragmentShopList.MODE_DISABLED);
        }
        else if(position==3)
        {
            return FragmentShopList.newInstance(FragmentShopList.MODE_WAITLISTED);
        }


        return FragmentShopList.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return titleNew;
            case 1:
                return titleEnabled;
            case 2:
                return titleDisabled;
            case 3:
                return titleWaitlisted;
        }
        return null;
    }


    private String titleNew = "New (0/0)";
    private String titleEnabled = "Enabled (0/0)";
    private String titleDisabled = "Disabled (0/0)";
    private String titleWaitlisted = "Waitlisted (0/0)";


//    private String titleDetachedItems = "Detached Items (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleNew = title;
        }
        else if (tabPosition == 1)
        {

            titleEnabled = title;

        }else if(tabPosition == 2)
        {

            titleDisabled = title;

        }else if(tabPosition == 3)
        {
            titleWaitlisted = title;
        }


        notifyDataSetChanged();
    }




}