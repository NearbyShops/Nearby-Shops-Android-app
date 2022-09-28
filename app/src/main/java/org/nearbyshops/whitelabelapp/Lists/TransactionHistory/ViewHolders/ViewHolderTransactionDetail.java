package org.nearbyshops.whitelabelapp.Lists.TransactionHistory.ViewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.whitelabelapp.Model.ModelBilling.Transaction;
import org.nearbyshops.whitelabelapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderTransactionDetail extends RecyclerView.ViewHolder{



    @BindView(R.id.list_item) TableLayout listItem;
    @BindView(R.id.transaction_id) TextView transactionID;
    @BindView(R.id.transaction_timestamp) TextView transactionTimestamp;
    @BindView(R.id.transaction_title) TextView transactionTitle;
    @BindView(R.id.transaction_description) TextView description;
    @BindView(R.id.transaction_amount) TextView transactionAmount;
    @BindView(R.id.transaction_balance) TextView transactionBalance;


    private Transaction transaction;
    private Context context;
    private Fragment fragment;




    public static ViewHolderTransactionDetail create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_transaction_new,parent,false);

        return new ViewHolderTransactionDetail(view,context,fragment);
    }






    private ViewHolderTransactionDetail(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }






    public void setItem(Transaction transactionDetails)

    {
        this.transaction = transactionDetails;

        transactionID.setText("ID : " + String.valueOf(transaction.getTransactionID()));
        transactionTimestamp.setText(transaction.getTimestampOccurred().toLocaleString());
        transactionTitle.setText(transaction.getTitle());
        description.setText(transaction.getDescription());


        String creditOrDebit = "";

        if(transaction.getTransactionAmount()>0)
        {
            creditOrDebit = "\n[Credit]";

            transactionAmount.setTextColor(ContextCompat.getColor(context,R.color.darkGreen));
        }
        else
        {
            creditOrDebit = "\n[Debit]";
            transactionAmount.setTextColor(ContextCompat.getColor(context,R.color.orangeDark));
        }



        transactionAmount.setText(
                    "" + context.getString(R.string.rupee_symbol)
                            + String.format(" %.2f ", transaction.getTransactionAmount()) + creditOrDebit
        );






            transactionBalance.setText(""
                    + context.getString(R.string.rupee_symbol)
                    + String.format(" %.2f", transaction.getServiceBalanceAfterTransaction())
            );



    }





    @OnClick(R.id.list_item)
    void listItemClick()
    {
        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemClick(transaction,getAdapterPosition());
        }
    }





    public interface ListItemClick
    {
        void listItemClick(Transaction transaction, int position);
    }


}
