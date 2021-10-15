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
import org.nearbyshops.whitelabelapp.showToast

class ViewHolderRoleSwitcherTypeAirbnb(
    itemView: View,
    private val context: Context,
    private val fragment: Fragment ) : RecyclerView.ViewHolder(itemView) {



    var binding = ListItemRoleSwitcherBinding.bind(itemView)




    private fun dashboardClick() {

        context.showToast("Feature available in Paid Version !")
    }




    fun bindDashboard() {

        val user = PrefLogin.getUser(context) ?: return

        var switchToString = ""


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


        binding.dashboardName.text = switchToString

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