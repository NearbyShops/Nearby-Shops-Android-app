package org.nearbyshops.whitelabelapp.Lists.MarketsList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.Interfaces.NotifyBackPressed
import org.nearbyshops.whitelabelapp.Interfaces.NotifySearch
import org.nearbyshops.whitelabelapp.Interfaces.SetToolbarText
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForShop.ItemsDatabase
import org.nearbyshops.whitelabelapp.Model.ItemCategory
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderEmptyScreenFullScreen.EmptyScreenDataFullScreen
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderFullScreenProgressBar
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.DatasetNotifier
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.ListItemClickGeneral
import org.nearbyshops.whitelabelapp.aaListUI.UtilityUI
import org.nearbyshops.whitelabelapp.aaListUI.ViewModels.ViewModelDelegator
import org.nearbyshops.whitelabelapp.databinding.FragmentListServerUiBinding
import java.util.*


class ListUIFragment(
    val screenType:Int,val screenMode:Int
):Fragment(), SwipeRefreshLayout.OnRefreshListener, DatasetNotifier, NotifyBackPressed,
    NotifySearch,ListItemClickGeneral, ViewHolderButton.ListItemClick,ViewHolderEmptyScreenFullScreen.ListItemClick{


    lateinit var viewModel: ViewModelDelegator
    private val dataset = ArrayList<Any>()
    var clearDataset = false
    private lateinit var adapter: AdapterKotlin

    lateinit var binding:FragmentListServerUiBinding


    private val limit = 10
    private var offset = 0
    private var item_count = 0


    lateinit var currentCategory: ItemCategory


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        retainInstance = true
//        return inflater.inflate(R.layout.fragment_list_server_ui, container, false)


       binding = FragmentListServerUiBinding.inflate(inflater)
       return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelDelegator(activity as Context,this)
        setupRecyclerView()
        setupSwipeContainer()
        setupBottomButton()

        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall()
        }



        if(activity is SetToolbarText)
        {
            (activity as SetToolbarText).setToolbar(true,
                UtilityUI.getScreenName(screenType),false,null)
        }

    }





    private fun setupBottomButton()
    {
//        binding.bottomButton.setOnClickListener { bottomButtonClick() }
//
//        if(screenType==ListUIFragment.screen_type_items_in_shop_seller)
//        {
//            binding.bottomButton.text = "Add Items to Shop"
//            binding.bottomButton.visibility = View.VISIBLE
//        }
    }



    override fun buttonClick(data: ViewHolderButton.ButtonData?) {
        if(screenType== screen_type_items_in_shop_seller)
        {
            showItemsDatabase()
        }
    }


    override fun emptyScreenButtonClicked() {
        if(screenType== screen_type_items_in_shop_seller)
        {
            showItemsDatabase()
        }
    }




    fun showItemsDatabase()
    {
        startActivityForResult(Intent(requireContext(), ItemsDatabase::class.java),125)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==125)
        {
            makeRefreshNetworkCall()
        }
    }


    override fun showEmptyScreen() {
    }

    private fun setupSwipeContainer() {

        binding.swipeContainer.setOnRefreshListener(this)
        binding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light,
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light
        )
    }




    private fun setupRecyclerView() {

        adapter = AdapterKotlin(dataset, requireContext(), this)
        binding.recyclerView.adapter = adapter
        val layoutManager = GridLayoutManager(activity, 12)
        binding.recyclerView.layoutManager = layoutManager

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                return 12
            }
        }


        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (layoutManager.findLastVisibleItemPosition() == dataset.size) {

                    // trigger fetch next page
                    if (offset + limit > layoutManager.findLastVisibleItemPosition()) {
                        return
                    }

                    if (offset + limit <= item_count) {

                        offset = offset + limit

                        viewModel.makeNetworkCall(1,searchQuery,currentCategory.itemCategoryID,
                            screenType, screenMode,false, limit, offset, dataset)

                    }
                }
            }
        })
    }






    fun makeRefreshNetworkCall()
    {
        binding.swipeContainer.post(Runnable { binding.swipeContainer.isRefreshing = true })
        onRefresh()
    }




    override fun onRefresh() {

        if(item_count==0)
        {
            dataset.clear()
            dataset.add(ViewHolderFullScreenProgressBar.Companion.FullScreenProgressBarData())
            adapter.notifyDataSetChanged()
        }


        offset = 0
        viewModel.makeNetworkCall(2,searchQuery,currentCategory.itemCategoryID,
            screenType,screenMode,true,limit,offset,dataset)
    }






    override fun notifyDatasetChanged() {

        if (offset + limit >= item_count) {

            adapter.setLoadMore(false)

        } else {

            adapter.setLoadMore(true)
        }

        binding.swipeContainer.isRefreshing = false


        if(item_count==0)
        {
            // show empty screen
            if(screenType==ListUIFragment.screen_type_items_in_shop_seller)
            {
                dataset.add(0,EmptyScreenDataFullScreen.emptyScreenQuickStockEditor())
            }
        }

        adapter.notifyDataSetChanged()
    }




    override fun notifyRequestFailedWithErrorCode(code: Int) {

        showMessage("Failed Code : $code")

//        dataset.clear()
        dataset.add(EmptyScreenDataFullScreen.getErrorTemplate(code))
        adapter.notifyDataSetChanged()
    }


    override fun notifyRequestFailed() {

        dataset.clear()
        dataset.add(EmptyScreenDataFullScreen.getOffline())
        adapter.notifyDataSetChanged()
    }




    override fun updateItemCount(itemCount: Int) {

        this.item_count = itemCount
    }




    override fun showMessage(message: String) {
        UtilityFunctions.showToastMessage(activity,message)
    }





    override fun backPressed(): Boolean {
        var currentCategoryID = 1 // the ID of root category is always supposed to be 1

        if (currentCategory.parentCategory != null) {

            currentCategory = currentCategory.parentCategory
            currentCategoryID = currentCategory.itemCategoryID

        } else {

            currentCategoryID = currentCategory.parentCategoryID
        }
        if (currentCategoryID != -1) {
            makeRefreshNetworkCall()
        }
        return currentCategoryID == -1
    }






    override fun notifyRequestSubCategory(itemCategory: ItemCategory?, scrollPosition: Int) {

        val temp = currentCategory
        currentCategory = itemCategory!!
        currentCategory.parentCategory = temp

        makeRefreshNetworkCall()

        // End Search Mode
        searchQuery = null
    }



    override fun listItemCLick(item: Any, position: Int) {

    }







    private var searchQuery: String? = null

    override fun search(searchString: String) {
        searchQuery = searchString
        makeRefreshNetworkCall()
    }

    override fun endSearchMode() {
        searchQuery = null
        makeRefreshNetworkCall()
    }



    init {
        DaggerComponentBuilder.getInstance()
            .netComponent.Inject(this)

        currentCategory = ItemCategory()
        currentCategory.itemCategoryID = 1
        currentCategory.categoryName = ""
        currentCategory.parentCategoryID = -1
    }




    companion object {

        // screen types
        const val screen_type_markets_list = 1
        const val screen_type_order_detail = 2
        const val screen_type_items_in_shop_seller = 3

        // screen modes
        const val MODE_OUT_OF_STOCK = 1
        const val MODE_LOW_STOCK = 2
        const val MODE_RECENTLY_UPDATED = 3
        const val MODE_RECENTLY_ADDED = 4
        const val MODE_PRICE_NOT_SET = 5
    }


}