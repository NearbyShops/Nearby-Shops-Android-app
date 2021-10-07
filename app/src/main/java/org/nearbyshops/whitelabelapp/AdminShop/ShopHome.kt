package org.nearbyshops.whitelabelapp.AdminShop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import org.nearbyshops.whitelabelapp.Admin.ViewModel.ViewModelShopForAdmin
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShop
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShopFragment
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.Model.Shop
import org.nearbyshops.whitelabelapp.MyApplication
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.databinding.FragmentShopDashboardKotlinBinding
import org.nearbyshops.whitelabelapp.showToast
import org.nearbyshops.whitelabelapp.zLibraryClasses.RoundedCornersTransformation


class ShopHome : Fragment() {


    private lateinit var binding:FragmentShopDashboardKotlinBinding

    lateinit var viewModelShopForAdmin: ViewModelShopForAdmin


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


//        return inflater.inflate(R.layout.fragment_manage_shop, container, false)
        // Inflate the layout for this fragment
        binding = FragmentShopDashboardKotlinBinding.inflate(inflater)
        return binding.root
    }


    fun setupViewModel()
    {
        viewModelShopForAdmin = ViewModelShopForAdmin(MyApplication.application)

        viewModelShopForAdmin.event.observe(viewLifecycleOwner, Observer<Int> {

            binding.progressSwitch.visibility = View.INVISIBLE

            if(it ==ViewModelShopForAdmin.EVENT_SHOP_OPENED || it==ViewModelShopForAdmin.EVENT_SHOP_CLOSED)
            {
                bindShopOpenStatus()
            }
            else if(it ==ViewModelShopForAdmin.EVENT_NETWORK_FAILED)
            {
                binding.shopOpenSwitch.isChecked = !binding.shopOpenSwitch.isChecked
            }

        })

        viewModelShopForAdmin.message.observe(viewLifecycleOwner, Observer {

            requireContext().showToast(it)
        })
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindShopProfile()
        bindShopOpenStatus()
        setupViewModel()


        binding.shopProfile.setOnClickListener { shopProfileClick() }

        binding.earningsHeader.setOnClickListener { earningsClick() }
        binding.salesTotal.setOnClickListener { totalSalesClick() }
        binding.customersServedHeader.setOnClickListener { customersClick() }
        binding.ordersDeliveredHeader.setOnClickListener { ordersClick() }
        binding.shopOpenSwitch.setOnClickListener { shopSwitchClick() }


        if(activity is SetToolbarText)
        {
            val shop = PrefShopAdminHome.getShop(requireActivity()) ?:return
            (requireActivity() as SetToolbarText).setToolbar(true,shop.shopName,false,null)
        }

    }




    private fun bindShopProfile()
    {
        val shop = PrefShopAdminHome.getShop(requireContext()) ?: return


        binding.shopName.text = shop.shopName


        var imagePath = PrefGeneral.getServerURL(context) + "/api/v1/Shop/Image/five_hundred_" + shop.logoImagePath + ".jpg"

        val radius = 10
        val margin = 10
        val transformation: Transformation = RoundedCornersTransformation(radius, margin)

        Picasso.get()
            .load(imagePath)
            .centerCrop()
            .resizeDimen(R.dimen.shop_image_width,R.dimen.shop_image_height)
            .transform(transformation)
            .placeholder(R.drawable.ic_home_black_24dp)
            .into(binding.shopProfilePhoto)
    }




    private fun shopProfileClick()
    {
        val shop = PrefShopAdminHome.getShop(requireContext()) ?:return


        //     open edit shop in edit mode
        val intent = Intent(activity, EditShop::class.java)
        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE)
        intent.putExtra("shop_id", shop.shopID)
        startActivity(intent)
    }


//    private fun bindNotice() {
//        val shop = PrefShopAdminHome.getShop(activity) ?: return
//        if (shop.registrationStatus == Shop.STATUS_SHOP_APPROVED) {
//            if (shop.accountBalance < shop.rt_min_balance) {
//                binding.notice.setText(getString(R.string.notice_low_balance))
//                notice.setVisibility(View.VISIBLE)
//            } else {
//                notice.setVisibility(View.GONE)
//            }
//        } else {
//            notice.setText(getString(R.string.notice_account_deactivated))
//            notice.setVisibility(View.VISIBLE)
//        }
//    }



    private fun bindShopOpenStatus() {

        val shop = PrefShopAdminHome.getShop(activity) ?: return

        if (shop.registrationStatus == Shop.STATUS_SHOP_ENABLED) {

            binding.shopOpenSwitch.isEnabled = true

            if (shop.isOpen()) {

                binding.shopOpenSwitch.isChecked = true

                if (shop.accountBalance < shop.rt_min_balance) {

                    binding.shopOpenStatus.visibility = View.GONE

                } else {

                    binding.shopOpenStatus.visibility = View.VISIBLE
                    binding.shopOpenStatus.setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.open)
                    )
                }

                binding.header.text = "Shop Open"

            } else {

                binding.shopOpenStatus.setImageDrawable(
                    ContextCompat.getDrawable(requireActivity(), R.drawable.shop_closed_small)
                )

                binding.shopOpenSwitch.isChecked = false
                binding.header.text = "Shop Closed"
            }
        } else {

            binding.shopOpenStatus.visibility = View.GONE
            binding.shopOpenSwitch.isEnabled = false
            binding.header.text = "Shop Not Enabled"
        }
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



    private fun shopSwitchClick()
    {
        if(binding.shopOpenSwitch.isChecked)
        {
            binding.progressSwitch.visibility = View.VISIBLE
            viewModelShopForAdmin.setShopOpen()
        }
        else{

            binding.progressSwitch.visibility = View.VISIBLE
            viewModelShopForAdmin.setShopClosed()
        }
    }

    companion object {
    }


    init {

    }

}