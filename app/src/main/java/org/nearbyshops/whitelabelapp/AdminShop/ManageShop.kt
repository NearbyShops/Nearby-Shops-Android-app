package org.nearbyshops.whitelabelapp.AdminShop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForShop.ItemsDatabase
import org.nearbyshops.whitelabelapp.AdminShop.QuickStockEditor.QuickStockEditor
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShop
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShopFragment
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryHomeDelivery.HomeDelivery
import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryPickFromShop.PickFromShopInventory
import org.nearbyshops.whitelabelapp.Lists.UsersList.UsersList
import org.nearbyshops.whitelabelapp.Lists.UsersList.UsersListFragment
import org.nearbyshops.whitelabelapp.Preferences.PrefGeneral
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.FragmentManageShopBinding
import org.nearbyshops.whitelabelapp.zLibraryClasses.RoundedCornersTransformation


class ManageShop : Fragment() {


    private lateinit var binding:FragmentManageShopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


//        return inflater.inflate(R.layout.fragment_manage_shop, container, false)
        // Inflate the layout for this fragment
        binding = FragmentManageShopBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindShopProfile()

        binding.shopProfile.setOnClickListener { shopProfileClick() }
        binding.pickupInventory.setOnClickListener { pickupInventoryClick() }
        binding.deliveryInventory.setOnClickListener { homeDeliveryInventoryClick() }
        binding.staffShop.setOnClickListener { shopStaffClick() }
        binding.staffDelivery.setOnClickListener { deliveryStaffClick() }
        binding.quickStockEditor.setOnClickListener { quickStockEditorClick() }
        binding.itemsDatabase.setOnClickListener { itemsDatabaseClick() }
        binding.tutorials.setOnClickListener { tutorialsClick() }
        binding.getSupport.setOnClickListener { supportClick() }




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
        val shop = PrefShopAdminHome.getShop(requireContext()) ?: return

        //     open edit shop in edit mode
        val intent = Intent(activity, EditShop::class.java)
        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE)
        intent.putExtra("shop_id", shop.shopID)
        startActivity(intent)
    }





    private fun pickupInventoryClick()
    {
        startActivity(Intent(requireContext(), PickFromShopInventory::class.java))
    }


    private fun homeDeliveryInventoryClick()
    {
        startActivity(Intent(requireContext(), HomeDelivery::class.java))
    }



    private fun shopStaffClick()
    {
        val intent = Intent(requireContext(), UsersList::class.java)
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY, UsersListFragment.MODE_SHOP_ADMIN_SHOP_STAFF_LIST)
        startActivity(intent)
    }


    private fun deliveryStaffClick()
    {
        val intent = Intent(requireContext(), UsersList::class.java)
        intent.putExtra(UsersListFragment.USER_MODE_INTENT_KEY, UsersListFragment.MODE_SHOP_ADMIN_DELIVERY_STAFF_LIST)
        startActivity(intent)
    }


    private fun quickStockEditorClick()
    {
        startActivity(Intent(requireContext(), QuickStockEditor::class.java))
    }


    private fun itemsDatabaseClick()
    {
        startActivity(Intent(requireContext(), ItemsDatabase::class.java))
    }




    private fun tutorialsClick()
    {
        val url = getString(R.string.tutorial_shop_dashboard)
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }



    private fun supportClick()
    {
        UtilityFunctions.dialPhoneNumber(getString(R.string.app_helpline_vendor_app),requireActivity())
    }




    companion object {
    }


    init {

    }

}