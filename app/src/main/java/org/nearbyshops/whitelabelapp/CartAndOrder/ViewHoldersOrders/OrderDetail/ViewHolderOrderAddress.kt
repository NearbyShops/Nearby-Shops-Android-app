package org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.databinding.ListItemOrderAddressBinding


class ViewHolderOrderAddress(itemView: View,
                             private val context: Context, //    private OrderItemEndPoint endPoint;
                             private val fragment: Fragment?
):RecyclerView.ViewHolder(itemView) {



    var binding = ListItemOrderAddressBinding.bind(itemView)
    private var order: Order? = null




    fun setItem(orderAddress: OrderAddress) {
        order = orderAddress.order


        if (order?.deliveryMode == Order.DELIVERY_MODE_PICKUP_FROM_SHOP) {

            binding.deliveryType.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.orangeDark
                )
            )

            binding.deliveryType.text = context.getString(R.string.delivery_type_description_pick_from_shop)

        } else {


            binding.deliveryType.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.phonographyBlue
                )
            )

            binding.deliveryType.text = context.getString(R.string.delivery_type_description_home_delivery)

        }
        if (order?.deliveryOTP != 0) {

            binding.deliveryOtp.visibility = View.VISIBLE
            binding.deliveryOtp.text = "Delivery OTP : " + order?.deliveryOTP.toString()

        } else {

            binding.deliveryOtp.visibility = View.GONE

        }

        val address = order?.deliveryAddress

        if (address != null) {

            binding.customerName.text = address.name
            binding.customerAddress.text = address.deliveryAddress
            binding.customerPhone.text = address.phoneNumber.toString()
        }

        if (order?.paymentMode == Order.PAYMENT_MODE_CASH_ON_DELIVERY) {

            binding.paymentMode.text = "Payment Mode : COD (Cash On Delivery)"

        } else if (order?.paymentMode == Order.PAYMENT_MODE_PAY_ONLINE_ON_DELIVERY) {

            binding.paymentMode.text = "Payment Mode : POD (Pay Online On Delivery)"

        } else if (order?.paymentMode == Order.PAYMENT_MODE_RAZORPAY) {

            binding.paymentMode.text = "Payment Mode : Paid"
        }
    }




    companion object {

        class OrderAddress(var order: Order) : ViewType() {

            init {
                viewType = AdapterKotlin.VIEW_TYPE_ORDER_ADDRESS
            }
        }


        fun create(parent: ViewGroup, context: Context, fragment: Fragment?): ViewHolderOrderAddress {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_order_address, parent, false)
            return ViewHolderOrderAddress(view, context, fragment)
        }
    }

}