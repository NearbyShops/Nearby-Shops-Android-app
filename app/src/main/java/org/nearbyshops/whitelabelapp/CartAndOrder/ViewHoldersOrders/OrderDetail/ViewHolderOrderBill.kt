package org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.ListItemClickGeneral
import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.ListItemOrderBillBinding


class ViewHolderOrderBill(
    itemView: View,
    private val context: Context?,
    private val fragment: Fragment,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.ViewHolder(itemView){


    private lateinit var order: Order

    var binding = ListItemOrderBillBinding.bind(itemView)



    fun listItemClick() {

        if (fragment is ListItemClickGeneral) {
            fragment.listItemCLick(order, layoutPosition)
        }
    }




    fun setItem(orderBill: OrderBill) {

        this.order = orderBill.order

        // bind billing details
        binding.itemTotalValue.text = PrefCurrency.getCurrencySymbol(
            context
        ) + " " + String.format("%.2f", order.itemTotal)
        binding.deliveryChargeValue.text = PrefCurrency.getCurrencySymbol(
            context
        ) + " " + String.format("%.2f", order.deliveryCharges)
        binding.netPayableValue.text = PrefCurrency.getCurrencySymbol(
            context
        ) + " " + String.format("%.2f", order.netPayable)

        if (order.savingsOverMRP > 0) {

            binding.savingsBlock.visibility = View.VISIBLE
            binding.savingsValue.text = PrefCurrency.getCurrencySymbol(
                context
            ) + " " + String.format("%.2f", order.savingsOverMRP)

        } else {

            binding.savingsBlock.visibility = View.GONE
        }
    }





    private fun showToastMessage(message: String) {
        UtilityFunctions.showToastMessage(context, message)
    }





    init {

        // setonClickListeners
        binding.listItem.setOnClickListener { listItemClick() }
    }



    companion object {

        class OrderBill(val order: Order): ViewType()
        {
            init {
                viewType= AdapterKotlin.VIEW_TYPE_ORDER_BILL
            }
        }


        fun create(
            parent: ViewGroup, context: Context?, fragment: Fragment,
            adapter: RecyclerView.Adapter<*>
        ): ViewHolderOrderBill {


            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_order_bill, parent, false)

            return ViewHolderOrderBill(view, context, fragment, adapter)
        }
    }


}