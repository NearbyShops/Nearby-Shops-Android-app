package org.nearbyshops.whitelabelapp.Login.InvalidLogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.LaunchActivity
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.FragmentInvalidLoginBinding


class InvalidLoginMessageFragment : Fragment() {


    private lateinit var binding:FragmentInvalidLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        binding = FragmentInvalidLoginBinding.inflate(inflater)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(activity is SetToolbarText)
        {
            (activity as SetToolbarText).setToolbar(true,"Invalid Login",false,null)
        }

        bindMessage()

        binding.buttonLogout.setOnClickListener { logoutClick() }
    }



    private fun bindMessage()
    {
        if(requireActivity().resources.getInteger(R.integer.app_type)==requireActivity().resources.getInteger(R.integer.app_type_market_admin_app))
        {
            binding.title.text = "Wrong Login !"
            binding.message.text = "Dear User you have logged in to the wrong application. If you want to create a market please login with different credentials."
        }
        else if(requireActivity().resources.getInteger(R.integer.app_type)==requireActivity().resources.getInteger(R.integer.app_type_vendor_app))
        {
            binding.title.text = "Wrong Login !"
            binding.message.text = "Dear User you have logged in to the wrong application. If you want to create a shop please login with different credentials."
        }
        else if(requireActivity().resources.getInteger(R.integer.app_type)==requireActivity().resources.getInteger(R.integer.app_type_delivery_app))
        {
            binding.title.text = "Wrong Login !"
            binding.message.text = "You are not a delivery staff. Only Delivery staff is allowed to login !"
        }

    }



    private fun logoutClick()
    {
        UtilityFunctions.logout(requireActivity())
        requireActivity().finish()
        startActivity(Intent(requireContext(), LaunchActivity::class.java))
    }




    companion object {
    }


    init {

    }

}