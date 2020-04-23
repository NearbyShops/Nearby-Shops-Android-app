package org.nearbyshops.enduserappnew.Lists.UsersList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.UserFilters;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderFilters.ViewHolderFilterUsers;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUserProfile.ViewHolderUserProfileItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.ViewHolderHeader;

import java.util.List;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // keeping track of selections
//    Map<Integer, Vehicle> selectedVehicleTypes = new HashMap<>();


    private List<Object> dataset;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_STAFF_PROFILE = 1;

    public static final int VIEW_TYPE_HEADER = 2;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 3;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 4;
    public static final int VIEW_TYPE_FILTER_USERS = 5;


    private boolean loadMore;





    public Adapter(List<Object> dataset, Context context, Fragment fragment) {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == VIEW_TYPE_STAFF_PROFILE) {

            return ViewHolderUserProfileItem.create(parent,context,fragment);
        }
        else if (viewType == VIEW_TYPE_HEADER) {

            return ViewHolderHeader.create(parent,context);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context,fragment);
        }
        else if(viewType==VIEW_TYPE_FILTER_USERS)
        {
            return ViewHolderFilterUsers.create(parent,context,fragment);
        }


        return null;
    }









    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof ViewHolderUserProfileItem) {

            ((ViewHolderUserProfileItem) holder).setItem((User) dataset.get(position));

        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);
        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((EmptyScreenDataFullScreen) dataset.get(position));
        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((HeaderTitle) dataset.get(position));
            }

        }
    }






    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);


        if (position == dataset.size()) {

            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof UserFilters)
        {
            return VIEW_TYPE_FILTER_USERS;
        }
        else if (dataset.get(position) instanceof HeaderTitle) {

            return VIEW_TYPE_HEADER;
        }
        else if (dataset.get(position) instanceof User) {

            return VIEW_TYPE_STAFF_PROFILE;
        }
        else if(dataset.get(position) instanceof EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }


        return -1;
    }




    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }



    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }

}