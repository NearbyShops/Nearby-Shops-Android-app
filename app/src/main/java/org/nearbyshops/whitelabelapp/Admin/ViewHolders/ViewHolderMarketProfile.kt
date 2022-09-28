package org.nearbyshops.whitelabelapp.AdminMarket.ViewHolders

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import org.nearbyshops.whitelabelapp.Admin.ViewModel.ViewModelMarketsForAdmin
import org.nearbyshops.whitelabelapp.MyApplication
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral
import org.nearbyshops.whitelabelapp.Preferences.PrefMarketAdminHome
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctionsKotlin
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.ListItemClickGeneral
import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType
import org.nearbyshops.whitelabelapp.EditDataScreens.EditMarket.EditMarket
import org.nearbyshops.whitelabelapp.EditDataScreens.EditMarket.EditMarketFragment
import org.nearbyshops.whitelabelapp.databinding.ListItemMarketProfileBinding
import org.nearbyshops.whitelabelapp.showToast
import org.nearbyshops.whitelabelapp.zLibraryClasses.RoundedCornersTransformation


class ViewHolderMarketProfile(
    itemView: View,
    private val context: Context,
    private val fragment: Fragment,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.ViewHolder(itemView){


    private lateinit var marketProfile: MarketProfile

    var binding = ListItemMarketProfileBinding.bind(itemView)



    private lateinit var viewModelMarketForAdmin: ViewModelMarketsForAdmin




    fun setupViewModel()
    {
        viewModelMarketForAdmin = ViewModelMarketsForAdmin(MyApplication.application)

        viewModelMarketForAdmin.event.observe(fragment,
            { eventCode ->

                if (eventCode == ViewModelMarketsForAdmin.EVENT_MARKET_STATUS_CHANGED) {

                    val market = PrefMarketAdminHome.getMarket(context)
                    market.isOpen = !market.isOpen
                    PrefMarketAdminHome.saveMarket(market, context)
                    bindMarketOpenStatus()
                }

                binding.progressSwitch.visibility = View.INVISIBLE

                if (eventCode == ViewModelMarketsForAdmin.EVENT_NETWORK_FAILED) {
                    binding.marketSwitch.isChecked = !binding.marketSwitch.isChecked
                }
            })

        viewModelMarketForAdmin.message.observe(fragment,
            { s -> context.showToast(s) })
    }



    fun listItemClick() {

        if (fragment is ListItemClickGeneral) {
            fragment.listItemCLick(marketProfile, layoutPosition)
        }
    }




    fun setItem(marketProfile: MarketProfile) {

        this.marketProfile = marketProfile

    }




    private fun marketProfileClick()
    {
        val intent = Intent(context, EditMarket::class.java)
//        intent.putExtra(EditMarketSettingsFragment.EDIT_MODE_INTENT_KEY, EditMarketFragment.MODE_UPDATE)
        context.startActivity(intent)
    }


    private fun shopSwitchClick()
    {
        if(binding.marketSwitch.isChecked)
        {
            binding.progressSwitch.visibility = View.VISIBLE
            viewModelMarketForAdmin.setMarketOpen(true)
        }
        else{

            binding.progressSwitch.visibility = View.VISIBLE
            viewModelMarketForAdmin.setMarketOpen(false)
        }
    }





    private fun bindMarketOpenStatus() {

        val market = PrefMarketAdminHome.getMarket(context) ?: return

//        if (market.registrationStatus == Market.REGISTRATION_STATUS_ENABLED) {
//
//            binding.marketSwitch.isEnabled = true
//
//
//        } else {
//
//            binding.marketSwitch.visibility = View.GONE
//            binding.marketSwitch.isEnabled = false
//            binding.header.text = "Market Disabled"
//        }


        if (market.isOpen) {

            binding.marketSwitch.isChecked = true

            binding.marketOpenStatus.visibility = View.VISIBLE
            binding.marketOpenStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.open))

            binding.header.text = "Delivery Open"

        } else {

            binding.marketOpenStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shop_closed_small))

            binding.marketSwitch.isChecked = false
            binding.header.text = "Delivery Closed"
        }
    }





    private fun bindMarketProfile()
    {
        val market = PrefMarketAdminHome.getMarket(context) ?: return

        binding.marketName.text = market.marketName

        val imagePath = (PrefGeneral.getServerURL(context) + "/api/Market/Image/five_hundred_" + market.logoImagePath + ".jpg")

        val radius = 10
        val margin = 10
        val transformation: Transformation = RoundedCornersTransformation(radius, margin)

        Picasso.get()
            .load(imagePath)
            .centerCrop()
            .resizeDimen(R.dimen.shop_image_width, R.dimen.shop_image_height)
            .transform(transformation)
            .placeholder(R.drawable.ic_home_black_24dp)
            .into(binding.marketProfilePhoto)
    }






    private fun shareButtonClickUtility()
    {
        UtilityFunctionsKotlin.shareMarketClick(context)
    }



    private fun showToastMessage(message: String) {
        UtilityFunctions.showToastMessage(context, message)
    }





    init {

        setupViewModel()
        bindMarketProfile()
        bindMarketOpenStatus()

        // setonClickListeners
        binding.shareButton.setOnClickListener { shareButtonClickUtility() }
        binding.shopProfile.setOnClickListener { marketProfileClick() }
        binding.marketSwitch.setOnClickListener { shopSwitchClick() }
    }





    companion object {

        class MarketProfile(): ViewType()
        {
            init {
                viewType= AdapterKotlin.VIEW_TYPE_MARKET_PROFILE
            }
        }


        fun create(
            parent: ViewGroup, context: Context, fragment: Fragment,
            adapter: RecyclerView.Adapter<*>
        ): ViewHolderMarketProfile {


            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_market_profile, parent, false)

            return ViewHolderMarketProfile(view, context, fragment, adapter)
        }
    }


}