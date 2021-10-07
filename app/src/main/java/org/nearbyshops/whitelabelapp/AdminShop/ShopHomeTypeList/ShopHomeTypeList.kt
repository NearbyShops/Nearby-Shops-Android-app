package org.nearbyshops.whitelabelapp.Lists.MarketsList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_markets_list.*
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForShop.ItemsDatabase
import org.nearbyshops.whitelabelapp.AdminShop.ViewHolders.ViewHolderShopAnalytics
import org.nearbyshops.whitelabelapp.AdminShop.ViewHolders.ViewHolderShopProfile
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShop
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShopFragment
import org.nearbyshops.whitelabelapp.Interfaces.RefreshFragment
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.Preferences.PrefShopAdminHome
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctionsKotlin
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightsBuilder
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.ViewHolderHighlightListItem
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import java.util.*


class ShopHomeTypeList :
    Fragment(), SwipeRefreshLayout.OnRefreshListener, ViewHolderHighlightListItem.ListItemClick, RefreshFragment{




    private val dataset = ArrayList<Any>()
    private var adapter: AdapterKotlin? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        retainInstance = true

        return inflater.inflate(R.layout.fragment_shop_home_list, container, false);
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        setupSwipeContainer()
        makeRefreshNetworkCall()


        if(activity is SetToolbarText)
        {
            val shop = PrefShopAdminHome.getShop(requireActivity()) ?:return
//            hop.shopName
            (activity as SetToolbarText).setToolbar(true,"Home",false,null)
        }
    }



    private fun setupSwipeContainer() {


        swipeContainer.setOnRefreshListener(this)
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light,
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light
        )
    }



    private fun setupRecyclerView() {
        adapter = AdapterKotlin(dataset, requireContext(),this)
        recyclerView.adapter = adapter

        adapter?.setLoadMore(false)

//        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
    }



    private fun makeRefreshNetworkCall() {
        swipeContainer.post {
            swipeContainer.isRefreshing = true
            onRefresh()
        }
    }


    override fun onRefresh() {
        loadData()
    }





    fun loadData() {
        dataset.clear()

        dataset.add(ViewHolderShopProfile.Companion.ShopProfile())
        dataset.add(HighlightsBuilder.getHighlightsCreateShop(requireContext()))
        dataset.add(ViewHolderShopAnalytics.Companion.ShopAnalytics())


        adapter?.notifyDataSetChanged()
        swipeContainer.isRefreshing = false
    }




    override fun refreshFragment() {
        setupRecyclerView()
        makeRefreshNetworkCall()
    }




    init {
        DaggerComponentBuilder.getInstance()
            .netComponent.Inject(this)
    }







    companion object {

        @JvmStatic
        fun newInstance() = ShopHomeTypeList()
    }




    override fun listItemCLick(item: Any, slideNumber: Int) {

        if(slideNumber==1)
        {
            // update shop profile

            val shop = PrefShopAdminHome.getShop(context) ?:return
            val intent = Intent(context, EditShop::class.java)
            intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE)
            intent.putExtra("shop_id", shop.shopID)
            context?.startActivity(intent)

        }
        else if(slideNumber==2)
        {
            // add items to shop
            startActivity(Intent(requireContext(), ItemsDatabase::class.java))
        }
        else if(slideNumber==3)
        {
            // share your shop
            UtilityFunctionsKotlin.shareShopClick(requireContext())
        }
    }




}