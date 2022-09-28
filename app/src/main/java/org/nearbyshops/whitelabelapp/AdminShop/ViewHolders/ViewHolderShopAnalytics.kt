package org.nearbyshops.whitelabelapp.AdminShop.ViewHolders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.ListItemClickGeneral
import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.ListItemShopAnalyticsBinding


class ViewHolderShopAnalytics(
    itemView: View,
    private val context: Context?,
    private val fragment: Fragment,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.ViewHolder(itemView){


    private lateinit var shopAnalytics: ShopAnalytics

    var binding = ListItemShopAnalyticsBinding.bind(itemView)



    fun listItemClick() {

        if (fragment is ListItemClickGeneral) {
            fragment.listItemCLick(shopAnalytics, layoutPosition)
        }
    }




    fun setItem(shopAnalytics: ShopAnalytics) {

        this.shopAnalytics = shopAnalytics

        // bind views

    }



    private fun earningsClick()
    {
//        context?.showToast("Earnings Click !")
    }



    private fun totalSalesClick()
    {
//        context?.showToast("Total Sales Click !")
    }


    private fun customersClick()
    {
//        context?.showToast("Customers Click !")
    }



    private fun ordersClick()
    {
//        context?.showToast("Orders Click !")
    }





    private fun showToastMessage(message: String) {
        UtilityFunctions.showToastMessage(context, message)
    }





    init {

        // setonClickListeners
        binding.earningsHeader.setOnClickListener { earningsClick() }
        binding.salesTotal.setOnClickListener { totalSalesClick() }
        binding.customersServedHeader.setOnClickListener { customersClick() }
        binding.ordersDeliveredHeader.setOnClickListener { ordersClick() }
    }





    companion object {

        class ShopAnalytics(): ViewType()
        {
            init {
                viewType= AdapterKotlin.VIEW_TYPE_SHOP_ANALYTICS
            }
        }


        fun create(
            parent: ViewGroup, context: Context?, fragment: Fragment,
            adapter: RecyclerView.Adapter<*>
        ): ViewHolderShopAnalytics {


            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_shop_analytics, parent, false)

            return ViewHolderShopAnalytics(view, context, fragment, adapter)
        }
    }


}