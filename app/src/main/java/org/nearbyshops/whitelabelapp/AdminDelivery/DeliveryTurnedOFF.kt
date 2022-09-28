package org.nearbyshops.whitelabelapp.AdminDelivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.databinding.FragmentDeliveryOffBinding


class DeliveryTurnedOFF : Fragment() {


    private lateinit var binding:FragmentDeliveryOffBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


//        return inflater.inflate(R.layout.fragment_manage_shop, container, false)
        // Inflate the layout for this fragment
        binding = FragmentDeliveryOffBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(activity is SetToolbarText)
        {
            (activity as SetToolbarText).setToolbar(true,"Delivery OFF",false,null)
        }
    }


    fun shopProfileClick()
    {

    }






    companion object;


    init {

    }

}