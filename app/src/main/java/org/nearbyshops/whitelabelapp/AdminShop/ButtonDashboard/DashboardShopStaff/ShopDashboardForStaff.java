package org.nearbyshops.whitelabelapp.AdminShop.ButtonDashboard.DashboardShopStaff;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory.OrderHistory;
import org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory.OrdersHistoryFragment;
import org.nearbyshops.whitelabelapp.Model.Shop;
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome;
import org.nearbyshops.whitelabelapp.R;
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions;
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForShop.ItemsDatabase;
import org.nearbyshops.whitelabelapp.AdminShop.ItemsInShopByCatSeller.ItemsInShopByCat;
import org.nearbyshops.whitelabelapp.AdminShop.ItemsInShopSeller.ItemsInShop;
import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryHomeDelivery.HomeDelivery;
import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryPickFromShop.PickFromShopInventory;
import org.nearbyshops.whitelabelapp.AdminShop.QuickStockEditor.QuickStockEditor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopDashboardForStaff extends AppCompatActivity {




    public static final String SHOP_ID_INTENT_KEY = "shop_id_key";
    @BindView(R.id.shop_name) TextView shopName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home_for_staff);
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

        Intent intent = new Intent(this, OrderHistory.class);
        intent.putExtra(OrdersHistoryFragment.IS_FILTER_BY_SHOP,true);

        startActivity(intent);
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




    @OnClick(R.id.option_add_items)
    void optionItemsByCategory()
    {
        startActivity(new Intent(this, ItemsDatabase.class));
    }



    @OnClick(R.id.option_items_in_stock)
    void optionItemsInStock()
    {
        startActivity(new Intent(this, ItemsInShop.class));
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
        UtilityFunctions.openURL(getString(R.string.tutorial_shop_dashboard),getApplication());
    }


}
