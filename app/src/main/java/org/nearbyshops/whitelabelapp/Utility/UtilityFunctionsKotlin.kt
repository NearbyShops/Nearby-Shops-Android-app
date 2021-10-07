package org.nearbyshops.whitelabelapp.Utility

import android.content.Context
import android.content.Intent
import org.nearbyshops.whitelabelapp.Preferences.PrefMarketAdminHome
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome
import org.nearbyshops.whitelabelapp.R

class UtilityFunctionsKotlin {


    companion object{


        public fun shareShopClick(context:Context)
        {

            val shop = PrefShopAdminHome.getShop(context) ?: return

            if(shop.customerHelplineNumber==null)
            {
                UtilityFunctions.showToastMessage(context,"Please add a phone number before sharing !")
                return
            }



            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT, "Hello ! \n\n" +
                        "We have launched our online store. Please check our store by downloading the following link\n\n"

                        + context.getString(R.string.app_download_link)

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


        public fun shareMarketClick(context:Context)
        {

            val market = PrefMarketAdminHome.getMarket(context) ?: return

            if(market.helplineNumber==null)
            {
                UtilityFunctions.showToastMessage(context,"Please add a phone number before sharing !")
                return
            }



            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT, "Hello ! \n\n" +
                        "We have launched our online market. Please check our market by clicking the following link\n\n"
                        + context.getString(R.string.app_download_link)

                        + "\n\nIf you have questions about ordering online, please call us on "
                        + market.helplineNumber + " and we would be happy to help you. " +

                        "\n\nThank You" + "\n" + market.marketName
            )

//        + " app download using this link " + UtilityFunctions.getAppLink(context)

            sendIntent.type = "text/plain"

            val shareIntent = Intent.createChooser(
                sendIntent,
                "Share " + UtilityFunctions.getAppName(context)
            )

            context.startActivity(shareIntent)
        }


        public fun shareInviteLinkToVendors(context:Context)
        {

            val market = PrefMarketAdminHome.getMarket(context) ?: return

            if(market.helplineNumber==null)
            {
                UtilityFunctions.showToastMessage(context,"Please add a phone number before sharing !")
                return
            }



            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT, "Dear Shop Owner ! \n\n" +
                        "We have launched our online market. We invite you to join our Market which will help you to increase your business" +
                        " and reach more customers." +
                        "\n\nJoin us by clicking the link given below\n\n"

                        + context.getString(R.string.app_download_link)

                        + "\n\nIf you have questions about joining, please call us on "
                        + market.helplineNumber + " and we would be happy to help you. " +

                        "\n\nThank You" + "\n" + market.marketName
            )

//        + " app download using this link " + UtilityFunctions.getAppLink(context)

            sendIntent.type = "text/plain"

            val shareIntent = Intent.createChooser(
                sendIntent,
                "Share " + UtilityFunctions.getAppName(context)
            )

            context.startActivity(shareIntent)
        }

    }
}