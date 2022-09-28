package org.nearbyshops.whitelabelapp.AdminMarket.ViewHolders

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
import org.nearbyshops.whitelabelapp.databinding.ListItemMarketAnalyticsBinding


class ViewHolderMarketAnalytics(
    itemView: View,
    private val context: Context?,
    private val fragment: Fragment,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.ViewHolder(itemView){


    private lateinit var marketAnalytics: MarketAnalytics

    var binding = ListItemMarketAnalyticsBinding.bind(itemView)



    fun listItemClick() {

        if (fragment is ListItemClickGeneral) {
            fragment.listItemCLick(marketAnalytics, layoutPosition)
        }
    }




    fun setItem(marketAnalytics: MarketAnalytics) {

        this.marketAnalytics = marketAnalytics

        // bind views

    }



    private fun earningsClick()
    {
//        requireContext().showToast("Earnings Click !")
    }



    private fun totalSalesClick()
    {
//        requireContext().showToast("Total Sales Click !")
    }


    private fun customersClick()
    {
//        requireContext().showToast("Customers Click !")
    }



    private fun ordersClick()
    {
//        requireContext().showToast("Orders Click !")
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

        class MarketAnalytics(): ViewType()
        {
            init {
                viewType= AdapterKotlin.VIEW_TYPE_MARKET_ANALYTICS
            }
        }


        fun create(
            parent: ViewGroup, context: Context?, fragment: Fragment,
            adapter: RecyclerView.Adapter<*>
        ): ViewHolderMarketAnalytics {


            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_market_analytics, parent, false)

            return ViewHolderMarketAnalytics(view, context, fragment, adapter)
        }
    }


}