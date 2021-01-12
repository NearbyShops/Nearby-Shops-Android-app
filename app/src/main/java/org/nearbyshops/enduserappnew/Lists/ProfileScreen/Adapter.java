package org.nearbyshops.enduserappnew.Lists.ProfileScreen;


import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.UtilityScreens.FavouriteShops.ViewHolderFavouriteShopsList;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.FavouriteShopEndpoint;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile.Model.RoleDashboardMarker;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile.Model.RoleDashboardMarkerSDS;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile.ViewHolderRoleDashboard;
import org.nearbyshops.enduserappnew.multimarketfiles.ViewHolderRoleDashboardSDS;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile.ViewHolderUserProfile;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.CreateShopData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.MarketHelplineData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderCreateShop;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderMarketHelpline;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.ButtonData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.PoweredByData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SetLocationManually;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.SignInMarker;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.ViewHolderPoweredBy;
import org.nearbyshops.enduserappnew.multimarketfiles.SwitchMarketData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderButton;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenListItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderHeader;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSetLocationManually;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderSignIn;
import org.nearbyshops.enduserappnew.multimarketfiles.ViewHolderSwitchMarket;

import java.util.List;

/**
 * Created by sumeet on 13/6/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> dataset = null;



//    private static final int view_type_current_market = 1;
//    private static final int view_type_saved_markets_list = 2;
    private static final int view_type_user_profile = 3;
//    private static final int view_type_markets_header = 4;
//    private static final int VIEW_TYPE_Market = 5;
    private static final int view_type_sign_in = 6;
    private static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 7;
    private static final int VIEW_TYPE_create_market = 8;
    private static final int view_type_role_dashboard = 9;
    private static final int view_type_role_dashboard_SDS = 10;
    public static final int VIEW_TYPE_SET_LOCATION_MANUALLY = 11;

    public static final int VIEW_TYPE_EMPTY_SCREEN = 12;

    public static final int VIEW_TYPE_CREATE_SHOP = 13;

    public static final int VIEW_TYPE_FAVOURITE_SHOPS = 14;
    public static final int VIEW_TYPE_BUTTON = 15;
    public static final int VIEW_TYPE_SWITCH_MARKET = 16;

    public static final int VIEW_TYPE_POWERED_BY = 17;

    public static final int VIEW_TYPE_MARKET_HELPLINE = 18;



    private Fragment fragment;
    private boolean loadMore;



    public Adapter(List<Object> dataset, Fragment fragment) {

        this.dataset = dataset;
        this.fragment = fragment;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;


        if(viewType == view_type_user_profile)
        {
            return ViewHolderUserProfile.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType == view_type_sign_in)
        {
            return ViewHolderSignIn.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType == VIEW_TYPE_CREATE_SHOP)
        {
            return ViewHolderCreateShop.create(parent,fragment.getActivity(),fragment);
        }
        else if (viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR) {

            return LoadingViewHolder.create(parent,fragment.getActivity());
        }
        else if(viewType==VIEW_TYPE_create_market)
        {
            return ViewHolderEmptyScreenListItem.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType ==view_type_role_dashboard)
        {
            return ViewHolderRoleDashboard.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType ==view_type_role_dashboard_SDS)
        {
            return ViewHolderRoleDashboardSDS.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType==VIEW_TYPE_SET_LOCATION_MANUALLY)
        {
            return ViewHolderSetLocationManually.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,fragment.getActivity(), fragment);
        }
        else if(viewType==VIEW_TYPE_FAVOURITE_SHOPS)
        {
            return ViewHolderFavouriteShopsList.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType==VIEW_TYPE_BUTTON)
        {
            return ViewHolderButton.create(parent,fragment.getActivity(),fragment,ViewHolderButton.LAYOUT_TYPE_MENU_ITEM);
        }
        else if(viewType==VIEW_TYPE_SWITCH_MARKET)
        {
            return ViewHolderSwitchMarket.create(parent,fragment.getActivity(),fragment);
            
        }
        else if(viewType==VIEW_TYPE_POWERED_BY)
        {
            return ViewHolderPoweredBy.create(parent,fragment.getActivity(),fragment);
        }
        else if(viewType==VIEW_TYPE_MARKET_HELPLINE)
        {
            return ViewHolderMarketHelpline.create(parent,fragment.getActivity(),fragment);
        }


        return null;
    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (position == dataset.size()) {

            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof SignInMarker)
        {
            return view_type_sign_in;
        }
        else if(dataset.get(position) instanceof User)
        {
            return view_type_user_profile;
        }
        else if(dataset.get(position) instanceof EmptyScreenDataListItem)
        {

            return VIEW_TYPE_create_market;
        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if(dataset.get(position) instanceof RoleDashboardMarker)
        {
            return view_type_role_dashboard;
        }
        else if(dataset.get(position) instanceof RoleDashboardMarkerSDS)
        {
            return view_type_role_dashboard_SDS;
        }
        else if(dataset.get(position) instanceof SetLocationManually)
        {
            return VIEW_TYPE_SET_LOCATION_MANUALLY;
        }
        else if(dataset.get(position) instanceof CreateShopData)
        {
            return VIEW_TYPE_CREATE_SHOP;
        }
        else if(dataset.get(position) instanceof FavouriteShopEndpoint)
        {
            return VIEW_TYPE_FAVOURITE_SHOPS;
        }
        else if(dataset.get(position) instanceof ButtonData)
        {
            return VIEW_TYPE_BUTTON;
        }
        else if(dataset.get(position) instanceof SwitchMarketData)
        {
            return VIEW_TYPE_SWITCH_MARKET;
        }
        else if(dataset.get(position) instanceof PoweredByData)
        {
            return VIEW_TYPE_POWERED_BY;
        }
        else if(dataset.get(position) instanceof MarketHelplineData)
        {
            return VIEW_TYPE_MARKET_HELPLINE;
        }


        return -1;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {


        if(holderVH instanceof ViewHolderRoleDashboard)
        {
            ((ViewHolderRoleDashboard) holderVH).bindDashboard();
        }
        else if(holderVH instanceof ViewHolderRoleDashboardSDS)
        {
            ((ViewHolderRoleDashboardSDS) holderVH).bindDashboard();
        }
        else if(holderVH instanceof ViewHolderCreateShop)
        {
            ((ViewHolderCreateShop) holderVH).setItem((CreateShopData) dataset.get(position));

        }
        else if(holderVH instanceof ViewHolderSwitchMarket)
        {
            ((ViewHolderSwitchMarket) holderVH).bindDashboard();
        }
        else if(holderVH instanceof ViewHolderUserProfile)
        {
            ViewHolderUserProfile viewHolderUserProfile = (ViewHolderUserProfile) holderVH;
            viewHolderUserProfile.setItem((User) dataset.get(position));

        }
        else if(holderVH instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holderVH).setItem((EmptyScreenDataFullScreen) dataset.get(position));
        }
        else if(holderVH instanceof ViewHolderEmptyScreenListItem)
        {
            if(dataset.get(position) instanceof EmptyScreenDataListItem)
            {
                ((ViewHolderEmptyScreenListItem) holderVH).setItem((EmptyScreenDataListItem) dataset.get(position));
            }
        }
        else if(holderVH instanceof ViewHolderHeader)
        {
            ((ViewHolderHeader) holderVH).setItem((HeaderTitle) dataset.get(position));
        }
        else if (holderVH instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holderVH).setLoading(loadMore);
        }
        else if (holderVH instanceof ViewHolderButton) {


            ((ViewHolderButton) holderVH).setItem((ButtonData) dataset.get(position));


        }
        else if(holderVH instanceof ViewHolderSetLocationManually)
        {
            ((ViewHolderSetLocationManually) holderVH).bindDashboard();
        }
        else if(holderVH instanceof ViewHolderFavouriteShopsList)
        {
            ((ViewHolderFavouriteShopsList) holderVH).refreshList();
//            ((ViewHolderFavouriteShopsList) holderVH).setTitle("Favourite Shops");
        }


    }




    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }




    public void setData(List<Object> data)
    {
        dataset = data;
    }




    @Override
    public int getItemCount() {

        return (dataset.size()+1);
    }



}
