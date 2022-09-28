package org.nearbyshops.whitelabelapp.aaListUI

import org.nearbyshops.whitelabelapp.Lists.MarketsList.ListUIFragment



class UtilityUI {


    init {

    }


    companion object {



        fun getScreenName(screenType:Int):String
        {
            if(screenType==ListUIFragment.screen_type_markets_list)
            {
                return "Markets"
            }
            else if(screenType==ListUIFragment.screen_type_items_in_shop_seller)
            {
                return "Items In Shop"
            }

            return ""
        }
    }

}