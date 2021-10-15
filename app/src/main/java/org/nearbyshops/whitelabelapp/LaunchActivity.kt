package org.nearbyshops.whitelabelapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.nearbyshops.whitelabelapp.Admin.AdminDashboardBottom
import org.nearbyshops.whitelabelapp.AdminDelivery.DeliveryDashboard
import org.nearbyshops.whitelabelapp.AdminShop.ShopDashboardBottom
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.AddShop
import org.nearbyshops.whitelabelapp.Login.InvalidLogin.InvalidLogin
import org.nearbyshops.whitelabelapp.Login.Login
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User
import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin


class LaunchActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var intent:Intent? = null


        var appType = resources.getInteger(R.integer.app_type)


        if(appType == resources.getInteger(R.integer.app_type_main_app))
        {

            // Send user to MainActivity as soon as this activity loads
            intent = Intent(this, Home::class.java)

        }
        else if(appType == resources.getInteger(R.integer.app_type_market_admin_app))
        {
            // market admin app

            val user = PrefLogin.getUser(this)


            if(user!=null)
            {
                // logged in
                if(user.role== User.ROLE_ADMIN_CODE || user.role== User.ROLE_STAFF_CODE)
                {
                    intent = Intent(this, AdminDashboardBottom::class.java)
                }
                else
                {

                    intent = Intent(this, InvalidLogin::class.java)
                }

            }
            else
            {

                intent = Intent(this, Login::class.java)
            }
        }
        else if(appType == resources.getInteger(R.integer.app_type_vendor_app))
        {
            // market admin app

            val user = PrefLogin.getUser(this)


            if(user!=null)
            {
                // logged in
                if(user.role== User.ROLE_SHOP_ADMIN_CODE || user.role== User.ROLE_SHOP_STAFF_CODE)
                {
                    intent = Intent(this, ShopDashboardBottom::class.java)
                }
                else if(user.role== User.ROLE_END_USER_CODE)
                {

                    intent = Intent(this, AddShop::class.java)
                }
                else
                {
                    intent = Intent(this, InvalidLogin::class.java)
                }


            }
            else
            {

                intent = Intent(this, Login::class.java)
            }

        }
        else if(appType == resources.getInteger(R.integer.app_type_delivery_app))
        {
            // market admin app

            val user = PrefLogin.getUser(this)


            if(user!=null)
            {
                // logged in
                if(user.role== User.ROLE_DELIVERY_GUY_MARKET_CODE || user.role== User.ROLE_DELIVERY_GUY_SHOP_CODE)
                {
                    intent = Intent(this, DeliveryDashboard::class.java)
                }
                else
                {
                    intent = Intent(this, InvalidLogin::class.java)
                }

            }
            else
            {

                intent = Intent(this, Login::class.java)
            }
        }



        startActivity(intent)

        // remove this activity from the stack
        finish()

    }





}