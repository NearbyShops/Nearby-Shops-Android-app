package org.nearbyshops.whitelabelapp.aaListUI

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.AdminShop.ViewHolders.ViewHolderShopItemSeller
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail.ViewHolderOrderAddress
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail.ViewHolderOrderBill
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail.ViewHolderOrderItem
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.OrderDetail.ViewHolderOrderTracker
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderDeliveryAddress.ViewHolderDeliveryAddress
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForShop.AdapterHorizontalList
import org.nearbyshops.whitelabelapp.AdminMarket.ViewHolders.ViewHolderMarketAnalytics
import org.nearbyshops.whitelabelapp.AdminMarket.ViewHolders.ViewHolderMarketProfile
import org.nearbyshops.whitelabelapp.AdminShop.ViewHolders.ViewHolderShopAnalytics
import org.nearbyshops.whitelabelapp.AdminShop.ViewHolders.ViewHolderShopProfile
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.OrderItem
import org.nearbyshops.whitelabelapp.Model.ModelRoles.User
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress
import org.nearbyshops.whitelabelapp.Model.Shop
import org.nearbyshops.whitelabelapp.Model.ShopItem
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.AdapterBannerImages
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.Model.BannerImageList
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.AdapterHighlights
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightList
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.ViewHolderHighlightList
import org.nearbyshops.whitelabelapp.ViewHolders.Model.ItemCategoriesList
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.Models.FilterShopsData
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderFilters.ViewHolderFilterShops
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop.ViewHolderShopMedium
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShop.ViewHolderShopSmall
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderAddItem.AddItemData
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderCreateShop
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderCreateShop.CreateShopData
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderShopSuggestions
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderUtility.ViewHolderShopSuggestions.ShopSuggestionsData
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.*
import org.nearbyshops.whitelabelapp.aaListUI.Model.ViewType
import java.util.*


/**
 * Created by sumeet on 25/5/16.
 */
class AdapterKotlin(private var dataset: List<Any>,
                    private val context: Context,
                    private val fragment: Fragment
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {




    companion object {

        // utility view types
        const val VIEW_TYPE_FULL_SCREEN_PROGRESS_INDICATOR = 1
        const val VIEW_TYPE_BUTTON_ADD_NEW_ADDRESS = 2
        const val VIEW_TYPE_BUTTON_WITH_MARGIN = 1003

        const val VIEW_TYPE_BANNER_IMAGE = 1004
        const val VIEW_TYPE_HIGHLIGHTS = 3
        const val VIEW_TYPE_HEADER = 4
        const val VIEW_TYPE_SCROLL_PROGRESS_BAR = 5
        const val VIEW_TYPE_EMPTY_SCREEN = 6
        const val VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM = 7
        const val VIEW_TYPE_LIST_ITEM_FULL_GRAPHIC = 8
        const val VIEW_TYPE_SET_LOCATION_MANUALLY = 9
        const val VIEW_TYPE_CREATE_MARKET = 11

        // main view types
        const val view_type_nearby_markets_list = 12
        const val VIEW_TYPE_ITEM_CATEGORY_LIST = 13
        const val VIEW_TYPE_DELIVERY_ADDRESS = 14


        // order view types
        const val VIEW_TYPE_ORDER_TRACKER = 15
        const val VIEW_TYPE_ORDER_ADDRESS = 16
        const val VIEW_TYPE_ORDER_ITEM = 18
        const val VIEW_TYPE_ORDER_BILL = 19

        const val VIEW_TYPE_SHOP = 20

        const val VIEW_TYPE_SHOP_ITEM = 21
        const val VIEW_TYPE_ITEM_CATEGORY = 22



        // admin view types

        const val VIEW_TYPE_SHOP_PROFILE = 23
        const val VIEW_TYPE_SHOP_ANALYTICS = 24

        // market admin view types

        const val VIEW_TYPE_MARKET_PROFILE = 25
        const val VIEW_TYPE_MARKET_ANALYTICS = 26



    }


    private var loadMore = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if(viewType== VIEW_TYPE_DELIVERY_ADDRESS)
        {
            return ViewHolderDeliveryAddress.create(parent,context,fragment)
        }
        else if (viewType == VIEW_TYPE_HIGHLIGHTS) {

            return ViewHolderHighlightList.create(parent, context, fragment)

        }
        else if (viewType == VIEW_TYPE_ITEM_CATEGORY_LIST) {

            return ViewHolderHorizontalList.create(parent, context, fragment)

        }
        else if (viewType == VIEW_TYPE_LIST_ITEM_FULL_GRAPHIC) {

            return ViewHolderListItemFullGraphic.create(parent, context, fragment,this)
        }
        else if (viewType == VIEW_TYPE_HEADER) {

            return ViewHolderHeader.create(parent, context,ViewHolderHeader.LAYOUT_TYPE_SIMPLE)

        }
        else if (viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR) {

            return LoadingViewHolder.create(parent, context)

        }
        else if (viewType == VIEW_TYPE_EMPTY_SCREEN) {

            return ViewHolderEmptyScreenFullScreen.create(parent, context, fragment)

        }
        else if (viewType == VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM) {

            return ViewHolderEmptyScreenListItem.create(parent, context, fragment)

        }
        else if (viewType == VIEW_TYPE_SET_LOCATION_MANUALLY) {

            return ViewHolderSetLocationManually.create(parent, context, fragment)
        }
        else if(viewType == VIEW_TYPE_FULL_SCREEN_PROGRESS_INDICATOR)
        {
            return ViewHolderFullScreenProgressBar.create(parent,context,fragment)
        }
        else if(viewType == VIEW_TYPE_BUTTON_ADD_NEW_ADDRESS)
        {
            return ViewHolderButton.create(parent,context,fragment,ViewHolderButton.LAYOUT_TYPE_ADD_NEW_ADDRESS)
        }
        else if(viewType== VIEW_TYPE_BUTTON_WITH_MARGIN)
        {
            return ViewHolderButton.create(parent,context,fragment,ViewHolderButton.LAYOUT_TYPE_BUTTON_WITH_MARGIN)
        }
        else if(viewType== VIEW_TYPE_SHOP)
        {
            return ViewHolderShopSmall.create(parent, context, fragment, this, ViewHolderShopSmall.LAYOUT_TYPE_SIDE_BY_SIDE)
        }
        else if(viewType== VIEW_TYPE_ORDER_ADDRESS)
        {
            return ViewHolderOrderAddress.create(parent, context, fragment)
        }
        else if(viewType== VIEW_TYPE_ORDER_TRACKER)
        {
            return ViewHolderOrderTracker.create(parent,context,fragment,this)
        }
        else if(viewType== VIEW_TYPE_ORDER_BILL)
        {
            return ViewHolderOrderBill.create(parent,context,fragment,this)
        }
        else if(viewType== VIEW_TYPE_ORDER_ITEM)
        {
            return ViewHolderOrderItem.create(parent, context, fragment)
        }
        else if (viewType == VIEW_TYPE_SHOP_ITEM) {

            return ViewHolderShopItemSeller.create(parent, context, fragment, this, dataset)
        }
        else if (viewType == VIEW_TYPE_SHOP_ANALYTICS) {

            return ViewHolderShopAnalytics.create(parent, context, fragment, this)
        }
        else if (viewType == VIEW_TYPE_SHOP_PROFILE) {

            return ViewHolderShopProfile.create(parent, context, fragment, this)
        }
        else if (viewType == VIEW_TYPE_MARKET_ANALYTICS) {

            return ViewHolderMarketAnalytics.create(parent, context, fragment, this)
        }
        else if (viewType == VIEW_TYPE_MARKET_PROFILE) {

            return ViewHolderMarketProfile.create(parent, context, fragment, this)
        }



        else return ViewHolderMarketAnalytics.create(parent, context, fragment,this)

    }








    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        if (holder is LoadingViewHolder) {

            holder.setLoading(loadMore)

        }
        else if (dataset.isNotEmpty() && dataset[position] is ViewType)
        {
//            val viewType = dataset[position] as ViewType

            when (holder) {

                is ViewHolderOrderTracker -> {

                    holder.setItem(dataset[position] as ViewHolderOrderTracker.Companion.OrderTracker)
                }

                is ViewHolderOrderBill -> {
                    holder.setItem(dataset[position] as ViewHolderOrderBill.Companion.OrderBill)
                }


                is ViewHolderOrderAddress -> {
                    holder.setItem(dataset[position] as ViewHolderOrderAddress.Companion.OrderAddress)
                }


                is ViewHolderOrderItem -> {
                    holder.setItem(dataset[position] as OrderItem)
                }

                is ViewHolderShopSmall -> {
                    holder.setItem(dataset[position] as Shop,false)
                }
            }

        }
        else
        {
            if (holder is ViewHolderShopSmall) {

                holder.setItem(dataset[position] as Shop, false)
            }
            else if(holder is ViewHolderListItemFullGraphic)
            {
                holder.setItem(dataset[position] as ViewHolderListItemFullGraphic.Companion.ListItemFullGraphicData)
            }
            else if(holder is ViewHolderDeliveryAddress)
            {
                holder.setItem(dataset[position] as DeliveryAddress)
            }
            else if (holder is ViewHolderShopMedium) {

                holder.setItem(dataset[position] as Shop)

            } else if (holder is ViewHolderCreateShop) {

                holder.setItem(dataset[position] as CreateShopData)

            }
            else if (holder is ViewHolderFilterShops) {

                holder.setItem(dataset[position] as FilterShopsData)

            } else if (holder is ViewHolderSetLocationManually) {

                holder.bindDashboard()

            } else if (holder is ViewHolderHorizontalList) {

                if (getItemViewType(position) == VIEW_TYPE_BANNER_IMAGE) {

                    val highlights = dataset[position] as BannerImageList
                    val list: MutableList<Any> = ArrayList(highlights.bannerImageList)

                    if (PrefLogin.getUser(context) != null && PrefLogin.getUser(context).role == User.ROLE_ADMIN_CODE) {
                        list.add(AddItemData())
                    }

                    val adapterBannerImages = AdapterBannerImages(list, context, fragment)
                    holder.setItem(adapterBannerImages, highlights.listTitle)


                }
                else if(getItemViewType(position)== VIEW_TYPE_ITEM_CATEGORY_LIST)
                {
                    val list = (dataset[position] as ItemCategoriesList).itemCategories
                    holder.setItem(
                        AdapterHorizontalList(
                            list,
                            context,
                            fragment
                        ), null)
                }

            }
            else if(holder is ViewHolderHighlightList)
            {

                val highlights = dataset[position] as HighlightList
                val list = highlights.highlightItemList

                holder.setItem(
                    AdapterHighlights(list, context, fragment),
                    highlights
                )

            }
            else if (holder is ViewHolderHeader) {

                if (dataset[position] is ViewHolderHeader.HeaderTitle) {

                    holder.setItem(

                        dataset[position] as ViewHolderHeader.HeaderTitle
                    )
                }

            } else if (holder is ViewHolderEmptyScreenFullScreen) {

                holder.setItem(dataset[position] as ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen)

            } else if (holder is ViewHolderEmptyScreenListItem) {

                holder.setItem(dataset[position] as ViewHolderEmptyScreenListItem.EmptyScreenDataListItem)

            } else if (holder is ViewHolderShopSuggestions) {

                holder.setItem(dataset[position] as ShopSuggestionsData)
            }
            else if(holder is ViewHolderFullScreenProgressBar)
            {
                holder.setItem(dataset[position] as ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData)
            }
            else if(holder is ViewHolderButton)
            {
                holder.setItem(dataset[position] as ViewHolderButton.ButtonData)

            }
            else if (holder is ViewHolderShopItemSeller) {

                holder.setShopItem(dataset[position] as ShopItem)
            }
            else if(holder is ViewHolderShopAnalytics)
            {
                holder.setItem(dataset[position] as ViewHolderShopAnalytics.Companion.ShopAnalytics)
            }
            else if(holder is ViewHolderShopProfile)
            {
                holder.setItem(dataset[position] as ViewHolderShopProfile.Companion.ShopProfile)
            }
            else if(holder is ViewHolderMarketAnalytics)
            {
                holder.setItem(dataset[position] as ViewHolderMarketAnalytics.Companion.MarketAnalytics)
            }
            else if(holder is ViewHolderMarketProfile)
            {
                holder.setItem(dataset[position] as ViewHolderMarketProfile.Companion.MarketProfile)
            }


        }


    }


    override fun getItemCount(): Int {
        return dataset.size + 1
    }





    override fun getItemViewType(position: Int): Int {


        if (position == dataset.size) {

            return VIEW_TYPE_SCROLL_PROGRESS_BAR
        }
        else if(dataset[position] is ViewHolderListItemFullGraphic.Companion.ListItemFullGraphicData)
        {
            return VIEW_TYPE_LIST_ITEM_FULL_GRAPHIC
        }
        else if (dataset[position] is ViewHolderHeader.HeaderTitle) {

            return VIEW_TYPE_HEADER
        }
        else if (dataset[position] is ItemCategoriesList) {

            return VIEW_TYPE_ITEM_CATEGORY_LIST
        }
        else if (dataset[position] is ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen) {

            return VIEW_TYPE_EMPTY_SCREEN
        }
        else if (dataset[position] is ViewHolderEmptyScreenListItem.EmptyScreenDataListItem) {

            return VIEW_TYPE_EMPTY_SCREEN_LIST_ITEM
        }
        else if (dataset[position] is BannerImageList) {

            return VIEW_TYPE_BANNER_IMAGE
        }
        else if (dataset[position] is HighlightList) {

            return VIEW_TYPE_HIGHLIGHTS
        }
        else if (dataset[position] is ViewHolderSetLocationManually.SetLocationManually) {

            return VIEW_TYPE_SET_LOCATION_MANUALLY
        }
        else if(dataset[position] is ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData)
        {
            return VIEW_TYPE_FULL_SCREEN_PROGRESS_INDICATOR
        }
        else if(dataset[position] is DeliveryAddress)
        {
            return VIEW_TYPE_DELIVERY_ADDRESS
        }
        else if(dataset[position] is ViewHolderButton.ButtonData)
        {

            val buttonData = (dataset[position] as ViewHolderButton.ButtonData)

            if (buttonData.layoutType==ViewHolderButton.LAYOUT_TYPE_ADD_NEW_ADDRESS) {

                return VIEW_TYPE_BUTTON_ADD_NEW_ADDRESS
            }
            else if (buttonData.layoutType==ViewHolderButton.LAYOUT_TYPE_BUTTON_WITH_MARGIN) {

                return VIEW_TYPE_BUTTON_WITH_MARGIN
            }
            else
            {
                return VIEW_TYPE_BUTTON_WITH_MARGIN
            }

        }
        else if (dataset[position] is ShopItem) {

            return VIEW_TYPE_SHOP_ITEM
        }
        else if (dataset[position] is Shop) {

            return VIEW_TYPE_SHOP
        }
        else if(dataset[position] is ViewHolderShopProfile.Companion.ShopProfile)
        {
            return VIEW_TYPE_SHOP_PROFILE
        }
        else if(dataset[position] is ViewHolderShopAnalytics.Companion.ShopAnalytics)
        {
            return VIEW_TYPE_SHOP_ANALYTICS
        }
        else if(dataset[position] is ViewHolderMarketProfile.Companion.MarketProfile)
        {
            return VIEW_TYPE_MARKET_PROFILE
        }
        else if(dataset[position] is ViewHolderMarketAnalytics.Companion.MarketAnalytics)
        {
            return VIEW_TYPE_MARKET_ANALYTICS
        }
        else if(dataset[position] is ViewType)
        {
            return (dataset[position] as ViewType).viewType
        }


        else return -1

    }



    fun setLoadMore(loadMore: Boolean) {
        this.loadMore = loadMore
    }





}