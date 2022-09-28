package org.nearbyshops.whitelabelapp.Admin

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
import org.nearbyshops.whitelabelapp.Admin.ShopsListForAdmin.ShopsDatabase
import org.nearbyshops.whitelabelapp.AdminMarket.ViewHolders.ViewHolderMarketAnalytics
import org.nearbyshops.whitelabelapp.AdminMarket.ViewHolders.ViewHolderMarketProfile
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.EditDataScreens.EditMarket.EditMarket
import org.nearbyshops.whitelabelapp.EditDataScreens.EditMarket.EditMarketFragment
import org.nearbyshops.whitelabelapp.Interfaces.RefreshFragment
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctionsKotlin
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model.HighlightsBuilder
import org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.ViewHolderHighlightListItem
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import java.util.*


class MarketHome : Fragment(), SwipeRefreshLayout.OnRefreshListener, ViewHolderHighlightListItem.ListItemClick, RefreshFragment{




    private val dataset = ArrayList<Any>()
    private var adapter: AdapterKotlin? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        retainInstance = true

        return inflater.inflate(R.layout.fragment_market_home_list_sm, container, false);
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        setupSwipeContainer()
        makeRefreshNetworkCall()


        if(activity is SetToolbarText)
        {
//            val shop = PrefShopAdminHome.getShop(requireActivity()) ?:return
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







    private fun loadData() {
        dataset.clear()

        dataset.add(ViewHolderMarketProfile.Companion.MarketProfile())
        dataset.add(HighlightsBuilder.getHighlightsSetupMarketSM(requireContext()))
        dataset.add(ViewHolderMarketAnalytics.Companion.MarketAnalytics())

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
        fun newInstance() = MarketHome()
    }





    override fun listItemCLick(item: Any, slideNumber: Int) {

        if(slideNumber==1)
        {
            // update shop profile
            val intent = Intent(requireActivity(), EditMarket::class.java)
            intent.putExtra(EditMarketFragment.EDIT_MODE_INTENT_KEY, EditMarketFragment.MODE_UPDATE)
            startActivity(intent)

        }
        else if(slideNumber==2)
        {
            // add Shops to market
            startActivity(Intent(activity, ShopsDatabase::class.java))
        }
        else if(slideNumber==3)
        {
            // share your shop
            UtilityFunctionsKotlin.shareMarketClick(requireContext())
        }
    }




}