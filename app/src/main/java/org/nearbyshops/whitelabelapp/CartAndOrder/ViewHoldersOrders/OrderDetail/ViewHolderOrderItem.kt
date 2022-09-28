package org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.squareup.picasso.Picasso
import org.nearbyshops.whitelabelapp.Model.Item
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.OrderItem
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.ListItemOrderItemBinding

class ViewHolderOrderItem(itemView: View,
                          private val context: Context,
                          private val fragment: Fragment?):RecyclerView.ViewHolder(itemView)
{



    var binding = ListItemOrderItemBinding.bind(itemView)

    private var orderItem: OrderItem? = null


    fun setItem(orderItem: OrderItem) {
        this.orderItem = orderItem
        val item = orderItem.item

        binding.itemId.text = "Item ID : " + orderItem.itemID
        binding.itemName.text = item.itemName
        binding.quantity.text = "Item Quantity : " + orderItem.itemQuantity + " " + item.quantityUnit
        binding.itemPrice.text = "Price : " + PrefCurrency.getCurrencySymbol(
            context
        ) + " " + orderItem.itemPriceAtOrder + " per " + item.quantityUnit
        binding.itemTotal.text = "Item Total : " + PrefCurrency.getCurrencySymbol(
            context
        ) + " " + UtilityFunctions.refinedStringWithDecimals(
                orderItem.itemPriceAtOrder * orderItem.itemQuantity
            )

        var currency = ""
        currency =
            PrefCurrency.getCurrencySymbol(
                context
            )

        if (orderItem.listPriceAtOrder > 0.0 && orderItem.listPriceAtOrder > orderItem.itemPriceAtOrder) {
            binding.listPrice.text =
                currency + " " + UtilityFunctions.refinedString(orderItem.listPriceAtOrder)
            binding.listPrice.paintFlags = binding.listPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.listPrice.visibility = View.VISIBLE
            val discountPercent =
                (orderItem.listPriceAtOrder - orderItem.itemPriceAtOrder) / orderItem.listPriceAtOrder * 100
            binding.discountIndicator.text = """${String.format("%.0f ", discountPercent)} %
Off"""

            binding.discountIndicator.visibility = View.VISIBLE

        } else {

            binding.discountIndicator.visibility = View.GONE
            binding.listPrice.visibility = View.GONE
        }
        val imagePath = (PrefGeneral.getServerURL(context)
                + "/api/v1/Item/Image/five_hundred_" + item.itemImageURL + ".jpg")
        val placeholder: Drawable? = VectorDrawableCompat
            .create(
                context.resources,
                R.drawable.ic_nature_people_white_48px, context.theme
            )
        Picasso.get()
            .load(imagePath)
            .placeholder(placeholder!!)
            .into(binding.itemImage)

    }





    fun listItemClick() {
//        Item item = orderItem.getItem();
//        ((ListItemClick)fragment).listItemClick(item,getLayoutPosition());
    }



    interface ListItemClick {
        fun listItemClick(item: Item?, position: Int)
    }

    companion object {

        fun create(parent: ViewGroup, context: Context, fragment: Fragment?): ViewHolderOrderItem {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_order_item, parent, false)
            return ViewHolderOrderItem(view, context, fragment)
        }
    }



    init {
        binding.listItem.setOnClickListener { listItemClick() }
    }
}