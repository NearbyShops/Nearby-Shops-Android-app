package org.nearbyshops.whitelabelapp.CartAndOrder.Checkout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.nearbyshops.whitelabelapp.Home
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.databinding.FragmentPlaceOrderResultBinding


class PlaceOrderResult : Fragment() {


    private lateinit var binding:FragmentPlaceOrderResultBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        binding = FragmentPlaceOrderResultBinding.inflate(inflater)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.trackOrderButton.setOnClickListener { trackOrderClick() }
        binding.continueShoppingButton.setOnClickListener { continueShoppingClick() }


        if(activity is SetToolbarText)
        {
            (activity as SetToolbarText).setToolbar(true,"Manage Market",false,null)
        }
    }



    private fun trackOrderClick()
    {

    }


    private fun continueShoppingClick()
    {

        val i = Intent(activity, Home::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)

//        requireActivity().finish()
//        val intent = Intent(requireActivity(), LaunchActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        requireActivity().startActivity(intent)
    }


    companion object {
    }


    init {

    }

}