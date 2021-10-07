package org.nearbyshops.whitelabelapp.aaListUI.ViewModels

import android.content.Context
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.Lists.MarketsList.ListUIFragment
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.DatasetNotifier
import java.util.ArrayList

class ViewModelDelegator(
    var context: Context,
    var datasetNotifier: DatasetNotifier
) {



    var viewModelOrderDetail = ViewModelOrderDetail(context,datasetNotifier)
    var viewModelQuickStockEditor = ViewModelQuickStockEditor(context,datasetNotifier)



    fun makeNetworkCall(orderID:Int?, searchQuery:String?,itemCategoryID:Int?,
                        screenType:Int,screenMode:Int, clearDataset : Boolean, limit : Int, offset:Int,
                        dataset:ArrayList<Any>)
    {
        if(screenType==ListUIFragment.screen_type_order_detail)
        {
            viewModelOrderDetail.getOrderDetails(clearDataset,orderID!!,limit,offset,dataset)
        }
        else if(screenType==ListUIFragment.screen_type_items_in_shop_seller)
        {
            viewModelQuickStockEditor.getItemsInShop(itemCategoryID,searchQuery,screenMode,clearDataset,limit,offset,dataset)
        }
    }








    init {
        DaggerComponentBuilder.getInstance()
            .netComponent.Inject(this)
    }

}