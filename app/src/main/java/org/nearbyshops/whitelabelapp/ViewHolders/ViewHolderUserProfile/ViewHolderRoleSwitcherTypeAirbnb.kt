package org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUserProfile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.LaunchActivity
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User
import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.databinding.ListItemRoleSwitcherBinding

class ViewHolderRoleSwitcherTypeAirbnb(
    itemView: View,
    private val context: Context,
    private val fragment: Fragment ) : RecyclerView.ViewHolder(itemView) {



    var binding = ListItemRoleSwitcherBinding.bind(itemView)

    fun dashboardClick() {

        val user = PrefLogin.getUser(context) ?: return

//        if (user.role == User.ROLE_ADMIN_CODE) {
//
//            val intent = Intent(context, AdminDashboard::class.java)
//            context.startActivity(intent)
//            return
//
//        }
//        else if (user.role == User.ROLE_STAFF_CODE) {
////            Intent intent = new Intent(context, MarketStaffDashboard.class);
////            context.startActivity(intent);
//            context.showToast("Dashboard under development !")
//            return
//        }



        var setLaunchScreen = PrefAppSettings.LAUNCH_SCREEN_MAIN

        if (user.role == User.ROLE_ADMIN_CODE || user.role == User.ROLE_STAFF_CODE) {

            setLaunchScreen = PrefAppSettings.LAUNCH_SCREEN_ADMIN

        }
        else if (user.role == User.ROLE_DELIVERY_GUY_MARKET_CODE || user.role == User.ROLE_DELIVERY_GUY_SHOP_CODE) {

            setLaunchScreen = PrefAppSettings.LAUNCH_SCREEN_DELIVERY

        } else if (user.role == User.ROLE_SHOP_ADMIN_CODE || user.role == User.ROLE_SHOP_STAFF_CODE) {

            setLaunchScreen = PrefAppSettings.LAUNCH_SCREEN_SHOP_ADMIN

        }


        val launchScreen = PrefAppSettings.getLaunchScreen(context)

        if (launchScreen == PrefAppSettings.LAUNCH_SCREEN_MAIN) {

//            context.showToast("Launch screen $setLaunchScreen")

            PrefAppSettings.setLaunchScreen(setLaunchScreen, context)
            startLaunchScreen()
        }
        else {

            PrefAppSettings.setLaunchScreen(PrefAppSettings.LAUNCH_SCREEN_MAIN, context)
            startLaunchScreen()
        }
    }



    private fun startLaunchScreen() {

        val intent = Intent(fragment.requireActivity(), LaunchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        fragment.requireActivity().finish()
        fragment.requireActivity().startActivity(intent)
    }




    fun bindDashboard() {

        val user = PrefLogin.getUser(context) ?: return

        var switchToString = ""


//        if (user.role == User.ROLE_ADMIN_CODE) {
//
//            binding.dashboardName.text = context.getString(R.string.admin_dashboard)
//            binding.dashboardDescription.text = context.getString(R.string.access_admin)
//
//            return
//
//        } else if (user.role == User.ROLE_STAFF_CODE) {
//            binding.dashboardName.text = "Staff Dashboard"
//            binding.dashboardDescription.text = "Press here to access the staff dashboard !"
//
//            return
//        }



        if (user.role == User.ROLE_ADMIN_CODE || user.role == User.ROLE_STAFF_CODE) {

            binding.dashboardDescription.text = context.getString(R.string.access_admin)
            switchToString = "Switch to Admin"
        }
        else if (user.role == User.ROLE_DELIVERY_GUY_MARKET_CODE || user.role == User.ROLE_DELIVERY_GUY_SHOP_CODE) {

            binding.dashboardDescription.text = "Press here to access the delivery Dashboard !"
            switchToString = "Switch to Delivery"
        }
        else if (user.role == User.ROLE_SHOP_ADMIN_CODE || user.role == User.ROLE_SHOP_STAFF_CODE) {

            binding.dashboardDescription.text = context.getString(R.string.access_shop)
            switchToString = "Switch to Selling"
        }


        val launchScreen = PrefAppSettings.getLaunchScreen(context)

        if (launchScreen == PrefAppSettings.LAUNCH_SCREEN_MAIN) {

            binding.dashboardName.text = switchToString

        } else {

            binding.dashboardName.text = "Switch to Buying"
            binding.dashboardDescription.text = "Press here to access the Customer App !"
        }
    }



    companion object {
        @JvmStatic
        fun create(parent: ViewGroup, context: Context, fragment: Fragment): ViewHolderRoleSwitcherTypeAirbnb {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_role_switcher, parent, false)
            return ViewHolderRoleSwitcherTypeAirbnb(view, context, fragment)
        }
    }

    init {
        bindDashboard()
        binding.listItem.setOnClickListener { dashboardClick() }
    }
}