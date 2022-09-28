package org.nearbyshops.whitelabelapp.AdminDelivery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.Model.ModelStaff.DeliveryGuyData
import org.nearbyshops.whitelabelapp.Preferences.PrefDeliveryGuyHome
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Services.NonStopServices.PersistentLocationService
import org.nearbyshops.whitelabelapp.databinding.FragmentDeliveryDashboardKotlinBinding
import org.nearbyshops.whitelabelapp.showToast


class DeliveryHome : Fragment() {


    private lateinit var binding:FragmentDeliveryDashboardKotlinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


//        return inflater.inflate(R.layout.fragment_manage_shop, container, false)
        // Inflate the layout for this fragment
        binding = FragmentDeliveryDashboardKotlinBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindDeliveryStatus()

        binding.shopProfile.setOnClickListener { shopProfileClick() }

        binding.earningsHeader.setOnClickListener { earningsClick() }
        binding.salesTotal.setOnClickListener { totalSalesClick() }
        binding.customersServedHeader.setOnClickListener { customersClick() }
        binding.ordersDeliveredHeader.setOnClickListener { ordersClick() }

        binding.deliverySwitch.setOnClickListener { deliverySwitchClick() }


        if(activity is SetToolbarText)
        {
            (activity as SetToolbarText).setToolbar(true,"Delivery Dashboard",false,null)
        }
    }


    fun shopProfileClick()
    {

    }


    fun deliverySwitchClick()
    {
        if(binding.deliverySwitch.isChecked)
        {
            PrefDeliveryGuyHome.saveDeliveryGuyStatus(DeliveryGuyData.STATUS_ONLINE,requireContext());
        }
        else
        {
            PrefDeliveryGuyHome.saveDeliveryGuyStatus(DeliveryGuyData.STATUS_OFFLINE,requireContext());
        }

        bindDeliveryStatus()
    }




    private fun bindDeliveryStatus()
    {
        val deliveryStatus  = PrefDeliveryGuyHome.getDeliveryStatus(requireContext())
        binding.deliverySwitch.isChecked = deliveryStatus==DeliveryGuyData.STATUS_ONLINE

        if(deliveryStatus==DeliveryGuyData.STATUS_ONLINE)
        {

            requireActivity().startService(Intent(requireActivity(), PersistentLocationService::class.java))

            binding.header.text= "Delivery ON" // Notifications ON
            binding.onlineStatus.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.open))
        }
        else{

            requireActivity().stopService(Intent(requireActivity(), PersistentLocationService::class.java))

            binding.onlineStatus.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.shop_closed_small))
            binding.header.text= "Delivery OFF" // Notifications OFF
        }

    }




    private fun earningsClick()
    {
        requireContext().showToast("Earnings Click !")
    }



    private fun totalSalesClick()
    {
        requireContext().showToast("Total Sales Click !")
    }


    private fun customersClick()
    {
        requireContext().showToast("Customers Click !")
    }



    private fun ordersClick()
    {
        requireContext().showToast("Orders Click !")
    }



    companion object;


    init {

    }

}