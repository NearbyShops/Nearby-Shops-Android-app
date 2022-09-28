package org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.CartAndOrder.DetailOrder.CallSupportDialog
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.OrderItemEndPoint
import org.nearbyshops.whitelabelapp.Model.ModelStaff.DeliveryGuyData
import org.nearbyshops.whitelabelapp.Model.ModelStatusCodes.OrderStatusHomeDelivery
import org.nearbyshops.whitelabelapp.Model.ModelStatusCodes.OrderStatusPickFromShop
import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.ListItemOrderOrderTrackerBinding


class ViewHolderOrderTracker(
    itemView: View,
    private val context: Context?,
    private val fragment: Fragment,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.ViewHolder(itemView){



    private val binding = ListItemOrderOrderTrackerBinding.bind(itemView)

    private lateinit var order: Order
    private var endPoint: OrderItemEndPoint? = null


    fun listItemClick() {

        if (fragment is ListItemClick) {
            fragment.listItemClick(order, layoutPosition)
        }
    }



    fun setItem(orderTracker: OrderTracker) {

        this.order = orderTracker.orderItemEndPoint.orderDetails
        this.endPoint = orderTracker.orderItemEndPoint

        bindDeliveryTime()
    }




    fun bindDeliveryTime() {


        if (order.deliveryMode == Order.DELIVERY_MODE_PICKUP_FROM_SHOP) {

            binding.distanceRemaining.visibility = View.GONE
            binding.minutesRemaining.text = OrderStatusPickFromShop.getStatusString(order.statusCurrent)
            binding.statusDescription.visibility = View.GONE

            return
        }


        if (order.deliveryMode == Order.DELIVERY_MODE_HOME_DELIVERY) {

            val data: DeliveryGuyData? = endPoint?.deliveryData
            val address = order.deliveryAddress


            if (data == null) {

                binding.distanceRemaining.visibility = View.GONE
                binding.minutesRemaining.text = "Waiting for Pickup !"
                binding.statusDescription.visibility = View.GONE
                return
            }


            println("Delivery Data : " + UtilityFunctions.provideGson().toJson(data))
            println("Delivery Address : " + UtilityFunctions.provideGson().toJson(address))

            val addressLocation = Location("GPS")
            addressLocation.latitude = address.latitude
            addressLocation.longitude = address.longitude

            val deliveryBoyLocation = Location("GPS")
            deliveryBoyLocation.latitude = data.latCurrent
            deliveryBoyLocation.longitude = data.lonCurrent

            val distance = (addressLocation.distanceTo(deliveryBoyLocation) / 1000).toDouble()
            val minutes = (distance / 20 * 60).toInt()


            if (order.statusCurrent < OrderStatusHomeDelivery.DELIVERED) {

                binding.distanceRemaining.text = String.format("%.2f Km", distance) + " away"
                binding.minutesRemaining.text = "$minutes Minutes "
                binding.statusDescription.visibility = View.VISIBLE

            } else {

                binding.distanceRemaining.visibility = View.GONE
                binding.minutesRemaining.text = OrderStatusHomeDelivery.getStatusString(order.statusCurrent)
                binding.statusDescription.visibility = View.GONE
            }
        }
    }





    private fun callSupportClick() {

        val fm = fragment.childFragmentManager
        val dialog =
            CallSupportDialog()

        dialog.show(fm, "call_support")

        val data = endPoint?.deliveryData

        if (data != null) {
            dialog.setDeliveryBoyPhone(data.deliveryGuyProfile.phone)
        }

//        val market = PrefAppSettings.getAppConfig(context)

//        if (market != null) {
//            dialog.setMarketPhone(market.appHelplineNumber)
//        }
    }


    private fun showToastMessage(message: String) {
        UtilityFunctions.showToastMessage(context, message)
    }




    interface ListItemClick {
        fun listItemClick(market: Order?, position: Int)
        fun editClick(market: Order?, position: Int)
    }






    init {
        // setonClickListeners
        binding.listItem.setOnClickListener{ listItemClick() }
        binding.callSupport.setOnClickListener { callSupportClick() }
    }





    companion object {


        class OrderTracker(var orderItemEndPoint: OrderItemEndPoint): ViewType()
        {
            init {
                viewType= AdapterKotlin.VIEW_TYPE_ORDER_TRACKER
            }
        }


        fun create(
            parent: ViewGroup, context: Context?, fragment: Fragment,
            adapter: RecyclerView.Adapter<*>
        ): ViewHolderOrderTracker {

            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_order_order_tracker, parent, false)

            return ViewHolderOrderTracker(view, context, fragment, adapter)
        }
    }


}