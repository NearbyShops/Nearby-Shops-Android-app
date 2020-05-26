package org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import org.nearbyshops.enduserappnew.EditDataScreens.EditShop.EditShop;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.enduserappnew.ItemCatalogue.ItemsDatabase.ItemsDatabase;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.ModelMarket.Market;
import org.nearbyshops.enduserappnew.Model.ModelStats.ShopStats;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.CreateShopData;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderUtility.Models.ShopSuggestionsData;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelShop;
import org.nearbyshops.enduserappnew.aSellerModule.DashboardShop.ShopDashboard;
import org.nearbyshops.enduserappnew.aSellerModule.DashboardShopStaff.ShopDashboardForStaff;
import org.nearbyshops.enduserappnew.aSellerModule.QuickStockEditor.QuickStockEditor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewHolderShopStatus extends RecyclerView.ViewHolder{




    @BindView(R.id.items_in_shop) TextView itemsInSHop;
    @BindView(R.id.set_prices) TextView setPrices;
    @BindView(R.id.add_stock) TextView addStock;
    @BindView(R.id.confirm_orders) TextView confirmOrders;


    Shop shop;



    private Context context;
    private Fragment fragment;
    private ShopSuggestionsData item;

    private ViewModelShop viewModelShop;




    public static ViewHolderShopStatus create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shop_status, parent, false);

        return new ViewHolderShopStatus(view,context, fragment);
    }




    public ViewHolderShopStatus(View itemView, Context context, Fragment fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;


        setupViewModel();
    }





    public void setItem(ShopSuggestionsData data)
    {
        this.item = data;

        viewModelShop.getShopForShopAdmin(true);
    }




    private void bindSuggestions()
    {
        if(shop==null)
        {
            return;
        }


        ShopStats shopStats = shop.getShopStats();

        if(shopStats==null)
        {
            return;
        }


        if(shopStats.getItemsInShopCount()<=10)
        {
            itemsInSHop.setVisibility(View.VISIBLE);
        }
        else
        {
            itemsInSHop.setVisibility(View.GONE);
        }


        if(shopStats.getOutOfStockCount()>5)
        {
            addStock.setVisibility(View.VISIBLE);
        }
        else
        {
            addStock.setVisibility(View.GONE);
        }



        if(shopStats.getPriceNotSetCount()>0)
        {
            setPrices.setVisibility(View.VISIBLE);
        }
        else
        {
            setPrices.setVisibility(View.GONE);
        }



        if((shopStats.getOrdersNotConfirmedHD()+shopStats.getOrdersNotConfirmedPFS() )>0)
        {
            confirmOrders.setVisibility(View.VISIBLE);
        }
        else
        {
            confirmOrders.setVisibility(View.GONE);
        }


    }




    @OnClick(R.id.confirm_orders)
    void confirmOrdersClick()
    {
        if(PrefShopAdminHome.getShop(context)!=null)
        {
            Intent intent = new Intent(context, ShopDashboard.class);
            context.startActivity(intent);
        }
    }




    @OnClick(R.id.items_in_shop)
    void addItems()
    {
        if(PrefShopAdminHome.getShop(context)!=null)
        {
            Intent intent = new Intent(context, ItemsDatabase.class);
            context.startActivity(intent);
        }
    }



    @OnClick({R.id.set_prices})
    void setPrices()
    {
        if(PrefShopAdminHome.getShop(context)!=null)
        {
            Intent intent = new Intent(context, QuickStockEditor.class);
            intent.putExtra("current_page",2);
            context.startActivity(intent);
        }
    }



    @OnClick({R.id.add_stock})
    void addStock()
    {
        if(PrefShopAdminHome.getShop(context)!=null)
        {
            Intent intent = new Intent(context, QuickStockEditor.class);
            intent.putExtra("current_page",1);
            context.startActivity(intent);
        }
    }





    @OnClick(R.id.list_item)
    void listItemClick()
    {


    }




    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }



    public interface ListItemClick
    {
        void listItemClick();
    }







    private void setupViewModel()
    {

        viewModelShop = new ViewModelShop(MyApplication.application);


        viewModelShop.getShopLive().observe(fragment, new Observer<Shop>() {
            @Override
            public void onChanged(Shop shop) {

                PrefShopAdminHome.saveShop(shop,context);
                ViewHolderShopStatus.this.shop = shop;
                bindSuggestions();
            }
        });



        viewModelShop.getEvent().observe(fragment, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

            }
        });




        viewModelShop.getMessage().observe(fragment, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastMessage(s);

            }
        });

    }

}

