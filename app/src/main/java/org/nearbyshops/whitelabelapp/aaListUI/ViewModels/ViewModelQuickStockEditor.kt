package org.nearbyshops.whitelabelapp.aaListUI.ViewModels

import android.content.Context
import org.nearbyshops.whitelabelapp.API.ShopItemService
import org.nearbyshops.whitelabelapp.AdminShop.zBottomDashboard.Deprecated.FragmentItemsInShop
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.ShopItemEndPoint
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.ViewHolders.Model.ItemCategoriesList
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.DatasetNotifier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class ViewModelQuickStockEditor(
    var context: Context,
    var datasetNotifier: DatasetNotifier
) {



    @Inject
    lateinit var shopItemService: ShopItemService





    fun getItemsInShop(itemCategoryID:Int?,searchString:String?,
                       screenMode:Int, clearDataset : Boolean, limit : Int, offset:Int, dataset:ArrayList<Any>)
    {


            val currentShop = PrefShopAdminHome.getShop(context) ?:return


            var call: Call<ShopItemEndPoint?>? = null

            if (screenMode == FragmentItemsInShop.MODE_OUT_OF_STOCK) {

                call = shopItemService.getShopItemsByShop(
                    itemCategoryID,
                    false, clearDataset,
                    currentShop.shopID, null, null, null,
                    null, null,
                    true, null, null, null, searchString,
                    null,null,
                    "LAST_UPDATE_DATE_TIME",
                    limit, offset, clearDataset,
                    false
                )

            } else if (screenMode == FragmentItemsInShop.MODE_LOW_STOCK) {

                call = shopItemService.getShopItemsByShop(
                    itemCategoryID,
                    true, clearDataset,
                    currentShop.shopID, null,
                    null, null, null,
                    null, null,
                    null,
                    null, null,
                    searchString,
                    false, false,
                    "available_item_quantity",
                    limit, offset,
                    clearDataset, false
                )

            } else if (screenMode == FragmentItemsInShop.MODE_RECENTLY_ADDED) {

                call = shopItemService.getShopItemsByShop(
                    itemCategoryID,
                    true, clearDataset,
                    currentShop.shopID, null,
                    null, null, null,
                    null, null,
                    null,
                    null, null,
                    searchString,
                    false, false,
                    "date_time_added desc",
                    limit, offset,
                    clearDataset, false
                )

            }else if (screenMode == FragmentItemsInShop.MODE_RECENTLY_UPDATED) {

                call = shopItemService.getShopItemsByShop(
                    itemCategoryID,
                    true, clearDataset,
                    currentShop.shopID, null,
                    null, null, null,
                    null, null,
                    null,
                    null, null,
                    searchString,
                    false, false,
                    " last_update_date_time desc ",
                    limit, offset,
                    clearDataset, false
                )

            } else if (screenMode == FragmentItemsInShop.MODE_PRICE_NOT_SET) {

                call = shopItemService.getShopItemsByShop(
                    itemCategoryID,
                    true, clearDataset,
                    currentShop.shopID, null,
                    null, null, null,
                    null, null,
                    true,
                    null, null,
                    searchString,
                    false,false,
                    " LAST_UPDATE_DATE_TIME ",
                    limit, offset,
                    clearDataset, false
                )
            }
            if (call == null) {
                return
            }


            call.enqueue(object : Callback<ShopItemEndPoint?> {

                override fun onResponse(
                    call: Call<ShopItemEndPoint?>,
                    response: Response<ShopItemEndPoint?>
                ) = if (response.code()==200) {

                    if (clearDataset) {

                        dataset.clear()
                        datasetNotifier.updateItemCount(response.body()!!.itemCount)
                    }




                    if(response.body()?.subcategories?.size !=null && response.body()?.subcategories?.size!! > 0)
                    {
                        dataset.add(ViewHolderHeader.HeaderTitle("Items Categories", R.color.light_grey,R.color.blueGrey800))
                    }



                    if (response.body()!!.results.size == 0) {

                        dataset.addAll(response.body()!!.subcategories)
                    }
                    else {

                        val list = ItemCategoriesList()
                        list.itemCategories = response.body()!!.subcategories


                        if(list.itemCategories!=null && list.itemCategories.size>0)
                        {
                            dataset.add(list)
                            dataset.add(ViewHolderButton.ButtonData("Add Item to Shop",0,ViewHolderButton.LAYOUT_TYPE_BUTTON_WITH_MARGIN))
                        }
                    }





//                    dataset.add(ViewHolderHeader.HeaderTitle("Items in Shop"))
                    dataset.addAll(response.body()!!.results)
                    datasetNotifier.notifyDatasetChanged()

                }
                else
                {
                    datasetNotifier.notifyRequestFailedWithErrorCode(response.code())
                }

                override fun onFailure(call: Call<ShopItemEndPoint?>, t: Throwable) {

                    datasetNotifier.notifyRequestFailed()
                }
            })


    }



    init {
        DaggerComponentBuilder.getInstance()
            .netComponent.Inject(this)
    }

}