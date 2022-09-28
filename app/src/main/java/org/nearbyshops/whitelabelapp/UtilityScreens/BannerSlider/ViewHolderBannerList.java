package org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.whitelabelapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewHolderBannerList extends RecyclerView.ViewHolder {


    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.list_title) TextView listTitle;


    private Context context;
    private Fragment fragment;




    public static ViewHolderBannerList create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_horizontal_list,parent,false);

        return new ViewHolderBannerList(view,context,fragment);
    }





    public ViewHolderBannerList(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }






    public void setItem(RecyclerView.Adapter adapter, String listTitleString)
    {

        if(listTitleString==null)
        {
            listTitle.setVisibility(View.GONE);
        }
        else
        {
            listTitle.setVisibility(View.VISIBLE);
            listTitle.setText(listTitleString);
        }


        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }





    public void scrollToPosition(int scrollToPosition)
    {
        if(scrollToPosition>0 && recyclerView!=null)
        {
            recyclerView.scrollToPosition(scrollToPosition);
        }
    }




    public void setTextSize(float size)
    {
        listTitle.setTextSize(size);
    }

}
