package org.nearbyshops.whitelabelapp.AdminCommon.SalesReport;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.Model.ModelBilling.Transaction;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.LoadingViewHolder;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader;
import org.nearbyshops.whitelabelapp.AdminCommon.SalesReport.ViewHolders.ViewHolderSalesReport;

import java.util.List;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;


//    public static final int VIEW_TYPE_REFERRED_USER = 1;
    public static final int VIEW_TYPE_TRANSACTION_RECORD = 1;
    public static final int VIEW_TYPE_FILTER_TRANSACTIONS = 2;


    public static final int VIEW_TYPE_EMPTY_SCREEN = 4;
    public static final int VIEW_TYPE_HEADER = 5;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;

    private boolean loadMore;




    public Adapter(List<Object> dataset, Context context, Fragment fragment) {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_TRANSACTION_RECORD) {

            return ViewHolderSalesReport.create(parent,context,fragment);
        }
        else if (viewType == VIEW_TYPE_HEADER) {

            return ViewHolderHeader.create(parent,context);
        }
        else if (viewType == VIEW_TYPE_PROGRESS_BAR) {

            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenFullScreen.create(parent,context,fragment);
        }

        return null;
    }








    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolderSalesReport) {

            ((ViewHolderSalesReport) holder).setItem((Transaction)dataset.get(position));

        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

                ((ViewHolderHeader) holder).setItem((ViewHolderHeader.HeaderTitle) dataset.get(position));
            }

        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);

        }
        else if(holder instanceof ViewHolderEmptyScreenFullScreen)
        {
            ((ViewHolderEmptyScreenFullScreen) holder).setItem((ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen) dataset.get(position));
        }

    }







    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);


        if (position == dataset.size()) {

            return VIEW_TYPE_PROGRESS_BAR;

        }
        else if (dataset.get(position) instanceof ViewHolderHeader.HeaderTitle) {

            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }
        else if (dataset.get(position) instanceof Transaction) {

            return VIEW_TYPE_TRANSACTION_RECORD;
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