package org.nearbyshops.whitelabelapp.aaListUI.Interfaces

import org.nearbyshops.whitelabelapp.Model.ItemCategory


interface ListItemClickGeneral {

    fun listItemCLick(item:Any,position:Int)
    fun notifyRequestSubCategory(itemCategory: ItemCategory?, scrollPosition: Int)

}