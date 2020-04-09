package org.nearbyshops.enduserappnew.zHighlightSlider.ViewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.zHighlightSlider.Model.TutorialStep;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderTutorialStep extends RecyclerView.ViewHolder{


    // keeping track of selections
//    private Map<Integer, VehicleType> selectedItem;
//    private VehicleType selectedItemSingle;


    private Context context;
    private TutorialStep highlight;
    private Fragment fragment;

    private RecyclerView.Adapter adapter;



    @BindView(R.id.list_item)
    CardView listItem;

    @BindView(R.id.title_top) TextView titleTop;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.description) TextView description;





    public static ViewHolderTutorialStep create(ViewGroup parent, Context context, Fragment fragment, RecyclerView.Adapter adapter) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tutorial_step,parent,false);
        return new ViewHolderTutorialStep(view,context,fragment,adapter);
    }






    public ViewHolderTutorialStep(View itemView, Context context, Fragment fragment, RecyclerView.Adapter adapter)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);


        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
    }







    public void setItem(TutorialStep highlight)
    {

        this.highlight = highlight;

        titleTop.setText(highlight.getTitleTop());
        title.setText(highlight.getTitle());
        description.setText(highlight.getDescription());

    }




    @OnClick(R.id.list_item)
    void listItemClick()
    {

        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyItemSelected(highlight);
        }

    }




    public interface ListItemClick
    {
        void notifyItemSelected(TutorialStep selectedItem);
    }


}// ViewHolder Class declaration ends


