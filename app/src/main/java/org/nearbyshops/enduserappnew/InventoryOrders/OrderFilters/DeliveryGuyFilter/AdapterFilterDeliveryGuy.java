package org.nearbyshops.enduserappnew.InventoryOrders.OrderFilters.DeliveryGuyFilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AdapterFilterDeliveryGuy extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



    private List<Object> dataset;
    private Context context;
    private Fragment fragment;

    private int deliveryGuyID = -1;


    public static final int VIEW_TYPE_DELIVERY_GUY_FILTER = 1;



    public AdapterFilterDeliveryGuy(List<Object> dataset, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        if(viewType==VIEW_TYPE_DELIVERY_GUY_FILTER)
        {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_filter_shop,parent,false);
            return new ViewHolderFilterDeliveryGuy(view,context,fragment);
        }


        return null;
    }






    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

      if(holder instanceof ViewHolderFilterDeliveryGuy)
        {
            ((ViewHolderFilterDeliveryGuy) holder).setItem((User) dataset.get(position));
        }

    }






    @Override
    public int getItemViewType(int position) {

        if(dataset.get(position) instanceof User)
        {
            return VIEW_TYPE_DELIVERY_GUY_FILTER;
        }

        return 0;
    }




    @Override
    public int getItemCount() {

        return dataset.size();
    }



    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }





    public class ViewHolderFilterDeliveryGuy extends RecyclerView.ViewHolder {





        @BindView(R.id.check_icon) ImageView checkIcon;
        @BindView(R.id.pickup_distance) TextView pickupDistance;
        @BindView(R.id.shop_name) TextView shopName;



        private Context context;
        private User shop;
        private Fragment fragment;




        public ViewHolderFilterDeliveryGuy(@NonNull View itemView, Context context, Fragment fragment) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            this.context = context;
            this.fragment = fragment;
        }





        public void setItem(User shop)
        {
            this.shop = shop;

    //        shopName.setText(shop.getShopName() + " (" + shop.getRt_order_count() + ")");
    //        pickupDistance.setText(UtilityFunctions.refinedStringWithDecimals(shop.getRt_distance()) + " Km" );

            bindCheckIcon(false);
        }







        void bindCheckIcon(boolean notifyChange)
        {
            if(shop.getUserID()== deliveryGuyID)
            {
                checkIcon.setVisibility(View.VISIBLE);
            }
            else
            {
                checkIcon.setVisibility(View.INVISIBLE);
            }


            if(notifyChange)
            {
                notifyDataSetChanged();
            }
        }








        @OnClick(R.id.list_item)
        public void itemCategoryListItemClick()
        {

            if(shop.getUserID()== deliveryGuyID)
            {
                deliveryGuyID=-1;
            }
            else
            {
                deliveryGuyID=shop.getUserID();
            }




            bindCheckIcon(true);


            if(fragment instanceof ListItemClick)
            {
                ((ListItemClick) fragment).listItemFilterShopClick(shop.getUserID());
            }

        }



    }// ViewHolder Class declaration ends








    public interface ListItemClick
    {
        void listItemFilterShopClick(int shopID);
    }

}