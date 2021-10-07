package org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.RecyclerView;



import org.nearbyshops.whitelabelapp.R;

import butterknife.ButterKnife;





public class LoadingViewHolder extends  RecyclerView.ViewHolder{




//    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }





    public static LoadingViewHolder create(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_progress_bar,parent,false);

        return new LoadingViewHolder(view);
    }







    public void setLoading(boolean loadMore)
    {
        if (loadMore) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }
        else {

            progressBar.setVisibility(View.GONE);
        }
    }


}
