package org.nearbyshops.whitelabelapp.CartAndOrder.DeliveryAddress

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.nearbyshops.whitelabelapp.API.DeliveryAddressService
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.aaListUI.AdapterKotlin
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton.ButtonData
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton
import org.nearbyshops.whitelabelapp.databinding.BottomSheetDeliveryAddressSelectionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class DeliveryAddressSelectionFragment : BottomSheetDialogFragment() {

    private var dataset = ArrayList<Any>()

    private lateinit var binding: BottomSheetDeliveryAddressSelectionBinding


    @Inject
    lateinit var deliveryAddressService:DeliveryAddressService



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        return inflater.inflate(R.layout.bottom_sheet_delivery_address_selection, container, false)


        binding = BottomSheetDeliveryAddressSelectionBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.closeButton.setOnClickListener{ closeButtonClick() }
        setupRecyclerView()
        makeNetworkCall()
    }





    fun setupRecyclerView()
    {
        // setup recycler view
        binding.listBottomDialog.layoutManager = GridLayoutManager(context,1)
        binding.listBottomDialog.adapter =  AdapterKotlin(dataset,activity as Context,requireParentFragment())
        binding.listBottomDialog.addItemDecoration(DividerItemDecoration(activity,RecyclerView.VERTICAL))
    }




    fun closeButtonClick()
    {
        dismiss()
    }

    fun makeNetworkCall()
    {

        val user = PrefLogin.getUser(activity)

        var call:Call<List<DeliveryAddress>> = deliveryAddressService.getAddresses(
            user.userID
        )

        binding.progressBar.visibility = View.VISIBLE

        call.enqueue(
            object :Callback<List<DeliveryAddress>>{

                override fun onResponse(
                    call: Call<List<DeliveryAddress>>,
                    response: Response<List<DeliveryAddress>>
                ) {

                    if(response.isSuccessful)
                    {
                        dataset.clear()
                        dataset.add(
                            ButtonData(
                                null,
                                0,
                                0,
                                ViewHolderButton.LAYOUT_TYPE_ADD_NEW_ADDRESS
                            )
                        )
                        response.body()?.let { dataset.addAll(it) }
                        binding.listBottomDialog.adapter?.notifyDataSetChanged()
                    }
                    else
                    {
                        UtilityFunctions.showToastMessage(activity,"Failed Code : " + response.code())
                    }


                    binding.progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<List<DeliveryAddress>>, t: Throwable) {

                    UtilityFunctions.showToastMessage(activity,getString(R.string.network_request_failed))
                    binding.progressBar.visibility = View.INVISIBLE
                }

            }
        )

    }




    init {
        DaggerComponentBuilder.getInstance()
            .netComponent.Inject(this)
    }

    companion object {
        fun newInstance(): DeliveryAddressSelectionFragment =
            DeliveryAddressSelectionFragment().apply {}
    }
}