package org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderDeliveryAddress

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.ListItemDeliveryAddressForSelectionBinding

class ViewHolderDeliveryAddress(
    itemView: View,
    private val context: Context?,
    private val fragment: Fragment
) : RecyclerView.ViewHolder(itemView){


    private lateinit var address: DeliveryAddress

    var binding = ListItemDeliveryAddressForSelectionBinding.bind(itemView)



    fun listItemClick() {

        toggleCheckButton()

        if (fragment is ListItemClick) {
            fragment.listItemClick(address, layoutPosition)
        }
    }

    fun changeAddressClick()
    {
        if(fragment is ListItemClick)
        {
            fragment.changeAddressClick()
        }
    }


    private fun toggleCheckButton()
    {
        if(binding.checkButton.visibility==View.VISIBLE)
        {
            binding.checkButton.visibility = View.INVISIBLE
        }
        else
        {
            binding.checkButton.visibility = View.VISIBLE
        }
    }



    fun setItem(address: DeliveryAddress) {

        this.address = address

        binding.addressName.text = this.address.name
        binding.deliveryAddress.text = this.address.deliveryAddress
        binding.addressPhone.text = "Phone : " + this.address.phoneNumber.toString()
    }



    private fun showToastMessage(message: String) {
        UtilityFunctions.showToastMessage(context, message)
    }


    interface ListItemClick {
        fun listItemClick(address: DeliveryAddress?, position: Int)
        fun changeAddressClick()
    }



    companion object {


        fun create(
            parent: ViewGroup, context: Context?, fragment: Fragment
        ): ViewHolderDeliveryAddress {


            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_delivery_address_for_selection, parent, false)

            return ViewHolderDeliveryAddress(view, context, fragment)
        }
    }




    init {

        // setonClickListeners
        binding.listItem.setOnClickListener { listItemClick() }
        binding.changeAddressButton.setOnClickListener { changeAddressClick() }
    }


}