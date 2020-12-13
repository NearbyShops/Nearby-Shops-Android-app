package org.nearbyshops.enduserappnew.aSellerModule.DashboardShop;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.enduserappnew.Lists.UsersList.UsersListFragment;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Preferences.PrefShopAdminHome;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.ItemCatalogue.ItemsDatabase.ItemsDatabase;
import org.nearbyshops.enduserappnew.aSellerModule.ItemsInShopByCatSeller.ItemsInShopByCat;
import org.nearbyshops.enduserappnew.aSellerModule.ItemsInShopSeller.ItemsInShop;
import org.nearbyshops.enduserappnew.Lists.OrderHistory.OrderHistory;
import org.nearbyshops.enduserappnew.Lists.OrderHistory.OrdersHistoryFragment;
import org.nearbyshops.enduserappnew.InventoryOrders.InventoryHomeDelivery.HomeDelivery;
import org.nearbyshops.enduserappnew.InventoryOrders.InventoryPickFromShop.PickFromShopInventory;
import org.nearbyshops.enduserappnew.aSellerModule.QuickStockEditor.QuickStockEditor;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopDashboard extends AppCompatActivity {




    public static final String SHOP_ID_INTENT_KEY = "shop_id_key";
    @BindView(R.id.shop_name) TextView shopName;


    @BindView(R.id.header_tutorials)
    LinearLayout headerTutorial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        ButterKnife.bind(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupNotifications();


        bindToolbarHeader();
    }




    private void bindToolbarHeader()
    {
        Shop shop = PrefShopAdminHome.getShop(this);
        shopName.setText(shop.getShopName());
    }








    @OnClick(R.id.option_add_items)
    void optionItemsByCategory()
    {
        startActivity(new Intent(this, ItemsDatabase.class));
    }





    @OnClick(R.id.option_orders)
    void ordersClick()
    {

            startActivity(new Intent(this, HomeDelivery.class));
    }





    @OnClick(R.id.option_orders_pick_from_shop)
    void ordersPickFromShop()
    {
//        startActivity(new Intent(this, OrdersPickFromShop.class));
        startActivity(new Intent(this, PickFromShopInventory.class));
    }




    @OnClick(R.id.shop_home_order_history)
    void orderHistory()
    {

//        Intent intent = new Intent(this, OrderHistory.class);
//        intent.putExtra(OrdersHistoryFragment.IS_FILTER_BY_SHOP,true);

        startActivity(OrderHistory.getLaunchIntent(OrdersHistoryFragment.MODE_SHOP_ADMIN,this));
    }








    @OnClick(R.id.shop_home_quick_stock_editor)
    void quickStockEditorClick(View view)
    {
        startActivity(new Intent(this, QuickStockEditor.class));

    }



    @OnClick(R.id.option_edit_stock)
    void editStockClick(View view)
    {
        startActivity(new Intent(this, ItemsInShopByCat.class));
    }




    @OnClick(R.id.option_items_in_stock)
    void optionItemsInStock()
    {
        startActivity(new Intent(this, ItemsInShop.class));
    }





    @OnClick(R.id.option_staff_accounts)
    void optionStaffAccounts()
    {

        Intent intent = new Intent(this, UsersList.class);
//        intent.putExtra("default_role", User.ROLE_SHOP_STAFF_CODE);
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY,UsersListFragment.MODE_SHOP_ADMIN_SHOP_STAFF_LIST);
        startActivity(intent);
    }




    @OnClick(R.id.option_delivery_guy_accounts)
    void optionDeliveryAccounts()
    {
        Intent intent = new Intent(this, UsersList.class);
//        intent.putExtra("default_role", User.ROLE_DELIVERY_GUY_SELF_CODE);
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY,UsersListFragment.MODE_SHOP_ADMIN_DELIVERY_STAFF_LIST);
        startActivity(intent);
    }




    void setupNotifications()
    {
        Shop shop = PrefShopAdminHome.getShop(this);

        if(shop!=null)
        {
            int currentapiVersion = Build.VERSION.SDK_INT;

            if (currentapiVersion >= Build.VERSION_CODES.KITKAT){
                // Do something for lollipop and above versions

//                Intent intent = new Intent(this, SSEIntentService.class);
//                intent.putExtra(SSEIntentService.SHOP_ID, shop.getShopID());
//                startService(intent);
            }
        }
    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }





    @OnClick(R.id.header_tutorials)
    void headerTutorialsClick()
    {
        UtilityFunctions.openURL("https://blog.nearbyshops.org/tag/tutorials-for-shop-owners/",this);
    }

}
