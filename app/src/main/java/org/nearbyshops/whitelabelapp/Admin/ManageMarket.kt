package org.nearbyshops.whitelabelapp.Admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import org.nearbyshops.whitelabelapp.AdminCommon.PushNotificationComposer
import org.nearbyshops.whitelabelapp.AdminDelivery.InventoryDeliveryPerson.DeliveryPersonInventory
import org.nearbyshops.whitelabelapp.AdminDelivery.InventoryDeliveryPerson.Fragment.DeliveryFragmentNew
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForAdmin.ItemsDatabaseAdmin
import org.nearbyshops.whitelabelapp.EditDataScreens.EditMarket.EditMarket
import org.nearbyshops.whitelabelapp.EditDataScreens.EditMarket.EditMarketFragment
import org.nearbyshops.whitelabelapp.EditDataScreens.EditMarketSettings.EditMarketSettings
import org.nearbyshops.whitelabelapp.Lists.UsersList.UsersList
import org.nearbyshops.whitelabelapp.Lists.UsersList.UsersListFragment
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral
import org.nearbyshops.whitelabelapp.Preferences.PrefMarketAdminHome
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.FragmentManageMarketBinding
import org.nearbyshops.whitelabelapp.showToast
import org.nearbyshops.whitelabelapp.zLibraryClasses.RoundedCornersTransformation


class ManageMarket : Fragment() {


    private lateinit var binding:FragmentManageMarketBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        binding = FragmentManageMarketBinding.inflate(inflater)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindMarketProfile()

        binding.marketProfile.setOnClickListener { marketProfileClick() }

        binding.pushNotificationComposer.setOnClickListener { pushNotificationsClick() }
        binding.billGenerator.setOnClickListener { billGenerator() }

        binding.deliveryInventory.setOnClickListener { deliveryInventoryClick() }
        binding.pickupInventory.setOnClickListener { pickupInventoryClick() }
        binding.marketStaff.setOnClickListener { marketStaffClick() }
        binding.staffDelivery.setOnClickListener { deliveryStaffClick() }
        binding.marketSettings.setOnClickListener { marketSettingsClick() }
        binding.itemsDatabase.setOnClickListener { itemsDatabaseClick() }
        binding.tutorials.setOnClickListener { tutorialsClick() }
        binding.getSupport.setOnClickListener { supportClick() }
        binding.usersList.setOnClickListener { usersListClick() }


        if(activity is SetToolbarText)
        {
            (activity as SetToolbarText).setToolbar(true,"Manage Market",false,null)
        }
    }




    private fun bindMarketProfile()
    {
        val market = PrefMarketAdminHome.getMarket(requireContext()) ?: return

        binding.marketName.text = market.marketName

        val imagePath = (PrefGeneral.getServerURL(activity) + "/api/Market/Image/five_hundred_" + market.logoImagePath + ".jpg")

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



    private fun marketProfileClick()
    {
        val intent = Intent(requireActivity(), EditMarket::class.java)
        intent.putExtra(EditMarketFragment.EDIT_MODE_INTENT_KEY, EditMarketFragment.MODE_UPDATE)
        startActivity(intent)
    }






    private fun billGenerator()
    {
        context?.showToast("Feature available in  Paid Version !")
    }




    private fun pushNotificationsClick()
    {
        val fm = childFragmentManager
        val dialog = PushNotificationComposer()
        dialog.show(fm, "push_notification_composer")
    }



    private fun deliveryInventoryClick()
    {
        val intent = Intent(requireActivity(), DeliveryPersonInventory::class.java)
        intent.putExtra(DeliveryFragmentNew.SCREEN_MODE_INTENT_KEY, DeliveryFragmentNew.SCREEN_MODE_MARKET_ADMIN)
        startActivity(intent)
    }


    private fun pickupInventoryClick()
    {
//        val intent = Intent(requireActivity(), Picku::class.java)
//        intent.putExtra(DeliveryFragmentNew.SCREEN_MODE_INTENT_KEY, DeliveryFragmentNew.SCREEN_MODE_MARKET_ADMIN)
//        startActivity(intent)
        context?.showToast("Feature Coming Soon !")
    }


    private fun marketStaffClick()
    {
        val intent = Intent(requireActivity(), UsersList::class.java)
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY, UsersListFragment.MODE_ADMIN_STAFF_LIST)
        startActivity(intent)
    }


    private fun deliveryStaffClick()
    {
        val intent = Intent(requireActivity(), UsersList::class.java)
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY, UsersListFragment.MODE_ADMIN_DELIVERY_STAFF_LIST)
        startActivity(intent)
    }




    private fun marketSettingsClick()
    {

        val intent = Intent(requireActivity(), EditMarketSettings::class.java)
//        intent.putExtra(EditMarketSettingsKotlin.EDIT_MODE_INTENT_KEY, EditMarketSettingsKotlin.MODE_UPDATE)
        startActivity(intent)
    }




    private fun usersListClick()
    {
        val intent = Intent(requireActivity(), UsersList::class.java)
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY, UsersListFragment.MODE_ADMIN_USER_LIST)
        startActivity(intent)
    }




    private fun itemsDatabaseClick()
    {
        startActivity(Intent(requireActivity(), ItemsDatabaseAdmin::class.java))
    }



    private fun tutorialsClick()
    {
        val url = getString(R.string.tutorial_market_admin_link)
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }


    private fun supportClick()
    {
        UtilityFunctions.dialPhoneNumber(getString(R.string.app_helpline_market_admin_app),requireActivity())
    }


    companion object {
    }


    init {

    }

}