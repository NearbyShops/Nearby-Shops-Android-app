package org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model.HighlightItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderHighlight extends RecyclerView.ViewHolder{


    // keeping track of selections
//    private Map<Integer, VehicleType> selectedItem;
//    private VehicleType selectedItemSingle;


    private Context context;
    private HighlightItem highlightItem;
    private Fragment fragment;

    private RecyclerView.Adapter adapter;





    @BindView(R.id.list_item)
    CardView listItem;

    @BindView(R.id.image) ImageView image;

    @BindView(R.id.title_top) TextView titleTop;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.footer) TextView footer;






    public static ViewHolderHighlight create(ViewGroup parent, Context context, Fragment fragment, RecyclerView.Adapter adapter) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_highlight,parent,false);
        return new ViewHolderHighlight(view,context,fragment,adapter);
    }






    public ViewHolderHighlight(View itemView, Context context, Fragment fragment, RecyclerView.Adapter adapter)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);


        this.context = context;
        this.fragment = fragment;
        this.adapter = adapter;
    }







    public void setItem(HighlightItem highlightItem)
    {

        this.highlightItem = highlightItem;


        titleTop.setText(highlightItem.getTitleTop());
        title.setText(highlightItem.getTitle());
        description.setText(highlightItem.getDescription());
        footer.setText(highlightItem.getFooter());


        Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);

//        String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/VehicleType/Image/" + "five_hundred_"+ this.highlightItem.getImagePath() + ".jpg";
//        String image_url = PrefGeneral.getServiceURL(context) + "/api/v1/VehicleType/Image/" + this.highlightItem.getImagePath();


        Picasso.get()
                .load(highlightItem.getImageURL())
                .placeholder(drawable)
                .into(image);

    }







    @OnClick(R.id.list_item)
    void listItemClick()
    {

        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).notifyItemSelected(highlightItem);
        }

    }




    public interface ListItemClick
    {
        void notifyItemSelected(HighlightItem selectedItem);
    }


}// ViewHolder Class declaration ends


