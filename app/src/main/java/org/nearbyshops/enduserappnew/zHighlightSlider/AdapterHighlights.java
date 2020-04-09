package org.nearbyshops.enduserappnew.zHighlightSlider;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.nearbyshops.enduserappnew.zHighlightSlider.Model.HighlightItem;
import org.nearbyshops.enduserappnew.zHighlightSlider.Model.TutorialStep;
import org.nearbyshops.enduserappnew.zHighlightSlider.ViewHolders.ViewHolderHighlight;
import org.nearbyshops.enduserappnew.zHighlightSlider.ViewHolders.ViewHolderTutorialStep;

import java.util.List;


public class AdapterHighlights extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<Object> dataset;
    private Context context;
    private Fragment fragment;


    public static final int VIEW_TYPE_HIGHLIGHT = 1;
    public static final int VIEW_TYPE_TUTORIAL_STEP = 2;


    private final static int VIEW_TYPE_PROGRESS_BAR = 6;



    private boolean loadMore;





    public AdapterHighlights(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        if (viewType == VIEW_TYPE_TUTORIAL_STEP) {

            return ViewHolderTutorialStep.create(viewGroup,context,fragment,this);
        }
        else if(viewType==VIEW_TYPE_HIGHLIGHT)
        {
            return ViewHolderHighlight.create(viewGroup,context,fragment,this);
        }


        return null;
    }






    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof ViewHolderHighlight)
        {
            ((ViewHolderHighlight) holder).setItem((HighlightItem) dataset.get(position));
        }
        else if(holder instanceof ViewHolderTutorialStep)
        {
            ((ViewHolderTutorialStep) holder).setItem((TutorialStep) dataset.get(position));
        }
    }






    @Override
    public int getItemCount() {
        return dataset.size();
    }




    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);


        if (position == dataset.size()) {

            return VIEW_TYPE_PROGRESS_BAR;
        }
        else if (dataset.get(position) instanceof HighlightItem) {

            return VIEW_TYPE_HIGHLIGHT;
        }
        else if (dataset.get(position) instanceof TutorialStep) {

            return VIEW_TYPE_TUTORIAL_STEP;
        }

        return -1;
    }






    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }



}
