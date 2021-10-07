package org.nearbyshops.whitelabelapp.AdminShop.ViewHolders

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import org.nearbyshops.whitelabelapp.Admin.ViewModel.ViewModelShopForAdmin
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShop
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShopFragment
import org.nearbyshops.whitelabelapp.MyApplication
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctionsKotlin
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.ListItemClickGeneral
import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType
import org.nearbyshops.whitelabelapp.databinding.ListItemShopProfileBinding
import org.nearbyshops.whitelabelapp.showToast
import org.nearbyshops.whitelabelapp.zLibraryClasses.RoundedCornersTransformation


class ViewHolderShopProfile(
    itemView: View,
    private val context: Context,
    private val fragment: Fragment,
    private val adapter: RecyclerView.Adapter<*>
) : RecyclerView.ViewHolder(itemView){


    private lateinit var shopProfile: ShopProfile

    var binding = ListItemShopProfileBinding.bind(itemView)



    private lateinit var viewModelShopForAdmin: ViewModelShopForAdmin




    fun setupViewModel()
    {
        viewModelShopForAdmin = ViewModelShopForAdmin(MyApplication.application)

        viewModelShopForAdmin.event.observe(fragment, Observer<Int> {

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

        viewModelShopForAdmin.message.observe(fragment, Observer {

            context?.showToast(it)
        })
    }



    fun listItemClick() {

        if (fragment is ListItemClickGeneral) {
            fragment.listItemCLick(shopProfile, layoutPosition)
        }
    }




    fun setItem(shopProfile: ShopProfile) {

        this.shopProfile = shopProfile




    }




    private fun shopProfileClick()
    {
        val shop = PrefShopAdminHome.getShop(context) ?:return


        //     open edit shop in edit mode
        val intent = Intent(context, EditShop::class.java)
        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE)
        intent.putExtra("shop_id", shop.shopID)
        context?.startActivity(intent)
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



    private fun bindShopOpenStatus() {

        val shop = PrefShopAdminHome.getShop(context) ?: return

//        if (shop.registrationStatus == Shop.STATUS_SHOP_APPROVED) {
//
//
//        } else {
//
//            binding.shopOpenStatus.visibility = View.GONE
//            binding.shopOpenSwitch.isEnabled = false
//            binding.header.text = "Shop Not Enabled"
//        }


//        binding.header.text = shop.shopName
        binding.shopOpenSwitch.isEnabled = true

        if (shop.isOpen()) {

            binding.shopOpenSwitch.isChecked = true

            if (shop.accountBalance < shop.rt_min_balance) {

                binding.shopOpenStatus.visibility = View.GONE

            } else {

                binding.shopOpenStatus.visibility = View.VISIBLE
                binding.shopOpenStatus.setImageDrawable(
                    context.let {
                        ContextCompat.getDrawable(it, R.drawable.open)
                    }
                )
            }

            binding.header.text = "Shop Open"


        } else {

            binding.shopOpenStatus.setImageDrawable(
                context.let {
                    ContextCompat.getDrawable(it, R.drawable.shop_closed_small)
                }
            )

            binding.shopOpenSwitch.isChecked = false
            binding.header.text = "Shop Closed"
        }


    }





    private fun bindShopProfile()
    {
        val shop = PrefShopAdminHome.getShop(context) ?: return


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







    private fun shareButtonClick()
    {

        val shop = PrefShopAdminHome.getShop(context) ?: return

        if(shop.customerHelplineNumber==null)
        {
            showToastMessage("Please add a phone number before sharing !")
            return
        }



        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT, "Hi ! \n\n" +
                    "We have launched our online store. Please check our store by clicking the following link\n\n" +

                    "https://app.nearbyshops.org/?link=https://app.nearbyshops.org/market?shop_id="
                            + shop.shopID + "&apn=org.nearbyshops.enduserappnew \n\n"
                            + "If you have questions about ordering online, please call us on "
                            + shop.customerHelplineNumber + " and we would be happy to help you. " +

                            "\n\nThank You" + "\n" + shop.shopName
        )

//        + " app download using this link " + UtilityFunctions.getAppLink(context)

        sendIntent.type = "text/plain"

        val shareIntent = Intent.createChooser(
            sendIntent,
            "Share " + UtilityFunctions.getAppName(context)
        )

        context.startActivity(shareIntent)

    }


    private fun shareButtonClickUtility()
    {
        UtilityFunctionsKotlin.shareShopClick(context)
    }



    private fun showToastMessage(message: String) {
        UtilityFunctions.showToastMessage(context, message)
    }





    init {

        setupViewModel()
        bindShopProfile()
        bindShopOpenStatus()

        // setonClickListeners
        binding.shareButton.setOnClickListener { shareButtonClickUtility() }
        binding.shopProfile.setOnClickListener { shopProfileClick() }
        binding.shopOpenSwitch.setOnClickListener { shopSwitchClick() }
    }





    companion object {

        class ShopProfile(): ViewType()
        {
            init {
                viewType= AdapterKotlin.VIEW_TYPE_SHOP_PROFILE
            }
        }


        fun create(
            parent: ViewGroup, context: Context, fragment: Fragment,
            adapter: RecyclerView.Adapter<*>
        ): ViewHolderShopProfile {


            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_shop_profile, parent, false)

            return ViewHolderShopProfile(view, context, fragment, adapter)
        }
    }


}