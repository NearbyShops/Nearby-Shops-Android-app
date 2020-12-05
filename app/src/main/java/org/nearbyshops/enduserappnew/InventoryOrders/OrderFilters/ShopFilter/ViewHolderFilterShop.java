package org.nearbyshops.enduserappnew.InventoryOrders.OrderFilters.ShopFilter;

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

import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderFilterShop extends RecyclerView.ViewHolder {




    @BindView(R.id.check_icon) ImageView checkIcon;
    @BindView(R.id.pickup_distance) TextView pickupDistance;
    @BindView(R.id.shop_name) TextView shopName;



    private Context context;
    private Shop shop;
    private Fragment fragment;



    private RecyclerView.Adapter adapter;
    private ShopSelection shopSelection;




    public ViewHolderFilterShop(@NonNull View itemView, Context context, Fragment fragment,
                                ShopSelection shopSelection, RecyclerView.Adapter adapter) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
        this.shopSelection = shopSelection;
        this.adapter = adapter;
    }





    public static ViewHolderFilterShop create(ViewGroup parent, Context context, Fragment fragment,
                                              ShopSelection shopSelection, RecyclerView.Adapter adapter)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_filter_shop,parent,false);
        return new ViewHolderFilterShop(view,context,fragment, shopSelection,adapter);
    }




    public void setItem(Shop shop)
    {
        this.shop = shop;

        shopName.setText(shop.getShopName() + " (" + shop.getRt_order_count() + ")");
        pickupDistance.setText(UtilityFunctions.refinedStringWithDecimals(shop.getRt_distance()) + " Km" );

        bindCheckIcon(false);
    }







    void bindCheckIcon(boolean notifyChange)
    {
        if(shop.getShopID()== shopSelection.getShopID())
        {
            checkIcon.setVisibility(View.VISIBLE);
        }
        else
        {
            checkIcon.setVisibility(View.INVISIBLE);
        }


        if(notifyChange)
        {
            adapter.notifyDataSetChanged();
        }
    }







    @OnClick(R.id.list_item)
    public void itemCategoryListItemClick()
    {

        if(shop.getShopID()== shopSelection.getShopID())
        {
            shopSelection.setShopID(-1);
        }
        else
        {
            shopSelection.setShopID(shop.getShopID());
        }




        bindCheckIcon(true);


        if(fragment instanceof ListItemClick)
        {
            ((ListItemClick) fragment).listItemFilterShopClick(shopSelection.getShopID());
        }

    }





    void showToast(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    public interface ListItemClick
    {
        void listItemFilterShopClick(int shopID);
    }



    public interface ShopSelection
    {
        void setShopID(int shopID);
        int getShopID();
    }

}// ViewHolder Class declaration ends

