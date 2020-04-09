package org.nearbyshops.enduserappnew.Lists.TransactionHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.Model.ModelBilling.Transaction;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHoldersCommon.Models.HeaderTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;
    private Fragment fragment;

    public static final int VIEW_TYPE_TRIP_REQUEST = 1;
    public static final int VIEW_TYPE_HEADER = 5;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;
//    private final static int VIEW_TYPE_FILTER = 7;
//    private final static int VIEW_TYPE_FILTER_SUBMISSIONS = 8;


    public Adapter(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver, Fragment fragment) {

//        DaggerComponentBuilder.getInstance()
//                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == VIEW_TYPE_TRIP_REQUEST) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_transaction_new, parent, false);

            return new ViewHolderTripRequest(view);
        }
        else if (viewType == VIEW_TYPE_HEADER) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_header_type_simple, parent, false);

            return new ViewHolderHeader(view);

        } else if (viewType == VIEW_TYPE_PROGRESS_BAR) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar, parent, false);

            return new LoadingViewHolder(view);

        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolderTripRequest) {

            bindTripRequest((ViewHolderTripRequest) holder, position);
        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof HeaderTitle) {
                HeaderTitle header = (HeaderTitle) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        } else if (holder instanceof LoadingViewHolder) {

            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            int itemCount = 0;


            if (fragment instanceof TransactionFragment) {
                itemCount = (((TransactionFragment) fragment).item_count_vehicle + 1 );
            }


//            itemCount = dataset.size();

            if (position == 0 || position == itemCount) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setIndeterminate(true);
            }

        }
    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (position == dataset.size()) {
            return VIEW_TYPE_PROGRESS_BAR;
        } else if (dataset.get(position) instanceof HeaderTitle) {
            return VIEW_TYPE_HEADER;
        } else if (dataset.get(position) instanceof Transaction) {
            return VIEW_TYPE_TRIP_REQUEST;
        }

        return -1;
    }





    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }






    void bindTripRequest(ViewHolderTripRequest holder, int position)
    {
        if(dataset.get(position) instanceof Transaction)
        {
            Transaction transaction = (Transaction) dataset.get(position);


            holder.transactionID.setText("ID : " + transaction.getTransactionID());
            holder.transactionTimestamp.setText(transaction.getTimestampOccurred().toLocaleString());
            holder.transactionTitle.setText(transaction.getTitle());
            holder.description.setText(transaction.getDescription());

            String creditOrDebit = "";

            if(transaction.isCredit())
            {
                creditOrDebit = "\n[Credit]";
            }
            else
            {
                creditOrDebit = "\n[Debit]";
            }


            holder.transactionAmount.setText(
                            "" + context.getString(R.string.rupee_symbol)
                            + String.format(" %.2f ", transaction.getTransactionAmount()) + creditOrDebit
            );



            
//            if(transaction.getServiceBalanceAfterTransaction()>=0)
//            {
//                holder.transactionBalance.setText(""
//                        + context.getString(R.string.rupee_symbol)
//                        + String.format(" %.2f\n[To Pay]",transaction.getServiceBalanceAfterTransaction())
//                );
//            }
//            else
//            {
//                holder.transactionBalance.setText(""
//                        + context.getString(R.string.rupee_symbol)
//                        + String.format(" %.2f\n[Surplus]", - transaction.getServiceBalanceAfterTransaction())
//                );
//            }



            holder.transactionBalance.setText(""
                    + context.getString(R.string.rupee_symbol)
                    + String.format(" %.2f", transaction.getServiceBalanceAfterTransaction())
            );





//            if(selectedVehicleTypes.containsKey(vehicleType.getVehicleID()))
//            {
//                //context.getResources().getColor(R.color.gplus_color_2)
//                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
//            }
//            else
//            {
//                //context.getResources().getColor(R.color.white)
//                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
//            }

        }
    }








    class ViewHolderTripRequest extends RecyclerView.ViewHolder{



        @BindView(R.id.list_item) TableLayout listItem;
        @BindView(R.id.transaction_id) TextView transactionID;
        @BindView(R.id.transaction_timestamp) TextView transactionTimestamp;
        @BindView(R.id.transaction_title) TextView transactionTitle;
        @BindView(R.id.transaction_description) TextView description;
        @BindView(R.id.transaction_amount) TextView transactionAmount;
        @BindView(R.id.transaction_balance) TextView transactionBalance;





        public ViewHolderTripRequest(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {



//                    TripRequest tripRequest = (TripRequest) dataset.get(getLayoutPosition());



//                    if(selectedVehicleTypes.containsKey(
//                            vehicleType.getVehicleTypeID()
//                    ))
//                    {
//                        selectedVehicleTypes.remove(vehicleType.getVehicleTypeID());
//
//                    }else
//                    {
//
//                        selectedVehicleTypes.put(vehicleType.getVehicleTypeID(),vehicleType);
//                    }



                    notificationReceiver.notifyTripRequestSelected();
                    notifyItemChanged(getLayoutPosition());



                    return notificationReceiver.listItemLongClick(v,
                            (Transaction) dataset.get(getLayoutPosition()),
                            getLayoutPosition()
                    );
                }
            });
        }





        @OnClick(R.id.list_item)
        void listItemLongClick()
        {

            notificationReceiver.listItemClick((Transaction) dataset.get(getLayoutPosition()),
                    getLayoutPosition()
            );


        }


    }// ViewHolder Class declaration ends






    class ViewHolderHeader extends RecyclerView.ViewHolder{

        @BindView(R.id.header) TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends





    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category

//        void currentItemClick(Item item, int position);
//        void itemUpdateClick(ItemSubmission itemSubmission, int position);
//        void imageUpdatedClick(ItemImage itemImage, int position);

        void notifyTripRequestSelected();
        void listItemClick(Transaction transaction, int position);
        boolean listItemLongClick(View view, Transaction transaction, int position);
    }



    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



}