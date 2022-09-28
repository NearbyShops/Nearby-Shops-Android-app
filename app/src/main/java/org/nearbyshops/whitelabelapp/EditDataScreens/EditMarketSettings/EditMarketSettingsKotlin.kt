package org.nearbyshops.whitelabelapp.aaMultimarketFiles.EditDataScreens.EditMarketSettingsMM

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_edit_market_settings.*
import okhttp3.ResponseBody
import org.nearbyshops.whitelabelapp.API.API_Admin.MarketSettingService
import org.nearbyshops.whitelabelapp.API.ShopAPI.ShopUtilityService
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.Model.ModelMarket.MarketSettings
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.databinding.FragmentEditMarketSettingsBinding
import org.nearbyshops.whitelabelapp.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class EditMarketSettingsKotlin : Fragment() {


    @Inject
    lateinit var marketService: MarketSettingService


    @Inject
    lateinit var shopUtilityService: ShopUtilityService


    var marketSettings : MarketSettings = MarketSettings()



    private lateinit var binding: FragmentEditMarketSettingsBinding




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveButton.setOnClickListener {
            saveButtonClick()
        }

        getSettings()
        binding.chargeManually.setOnClickListener { chargeNowClick() }
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        retainInstance = true

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_edit_market_settings, container, false)


        // Inflate the layout for this fragment
        binding = FragmentEditMarketSettingsBinding.inflate(inflater)
        return binding.root
    }






    private fun getSettings()
    {

        val pd = ProgressDialog(activity)
        pd.setMessage(getString(R.string.loading_message))
        pd.show()


        var call: Call<MarketSettings> = marketService.getSettings(
            PrefLogin.getAuthorizationHeader(activity)
        )

        call.enqueue(
            object : Callback<MarketSettings>{

                override fun onResponse(
                    call: Call<MarketSettings>,
                    response: Response<MarketSettings>
                ) {

                    pd.dismiss()

                    if(response.isSuccessful)
                    {
                        response.body().let {

                            if (it != null) {

                                marketSettings = it
                                bindViews()
                            }
                        }

                    }
                    else
                    {
                        UtilityFunctions.showToastMessage(activity,"Failed to Get Settings : Error Code - " + response.code())
                    }

                }

                override fun onFailure(call: Call<MarketSettings>, t: Throwable) {

                    UtilityFunctions.showToastMessage(activity,getString(R.string.network_request_failed))
                }

            }
        )


    }





    fun bindViews()
    {
        binding.codEnabled.isChecked = marketSettings.isCodEnabled
        binding.podEnabled.isChecked = marketSettings.isPodEnabled
        binding.razorpayEnabled.isChecked = marketSettings.isOnlinePaymentEnabled

        binding.marketFeeFixed.setText(marketSettings.marketFeeFixed.toString())
        binding.marketFeeCommission.setText(marketSettings.marketFeeCommission.toString())
        binding.marketTaxRate.setText(marketSettings.taxInPercent.toString())

        binding.monthlyCharge.setText(marketSettings.monthlyFeeForVendors.toString())

        binding.baseDeliveryFeePerOrder.setText(marketSettings.baseDeliveryChargePerOrder.toString())
        binding.baseDeliveryFeeMaxDistance.setText(marketSettings.baseDeliveryFeeMaxDistance.toString())
        binding.extraDeliveryCharge.setText(marketSettings.extraDeliveryChargePerKm.toString())

        binding.billAmountForFreeDelivery.setText(marketSettings.billAmountForFreeDelivery.toString())
        binding.deliveryByShopEnabled.isChecked = marketSettings.isDeliveryByShopEnabled
        binding.minimumAmountForDelivery.setText(marketSettings.minimumAmountForDelivery.toString())

        binding.startupModeEnabled.isChecked = marketSettings.isStartupModeEnabled
        binding.demoModeEnabled.isChecked = marketSettings.isDemoModeEnabled

    }



    fun getDataFromViews()
    {
        marketSettings.isCodEnabled = binding.codEnabled.isChecked
        marketSettings.isPodEnabled = binding.podEnabled.isChecked
        marketSettings.isOnlinePaymentEnabled = binding.razorpayEnabled.isChecked

        marketSettings.marketFeeFixed = binding.marketFeeFixed.text.toString().toFloat()
        marketSettings.marketFeeCommission = binding.marketFeeCommission.text.toString().toFloat()
        marketSettings.taxInPercent = binding.marketTaxRate.text.toString().toFloat()

        marketSettings.monthlyFeeForVendors = binding.monthlyCharge.text.toString().toFloat()

        marketSettings.baseDeliveryChargePerOrder = binding.baseDeliveryFeePerOrder.text.toString().toFloat()
        marketSettings.baseDeliveryFeeMaxDistance = binding.baseDeliveryFeeMaxDistance.text.toString().toFloat()
        marketSettings.extraDeliveryChargePerKm = binding.extraDeliveryCharge.text.toString().toFloat()

        marketSettings.billAmountForFreeDelivery = binding.billAmountForFreeDelivery.text.toString().toFloat()
        marketSettings.isDeliveryByShopEnabled = binding.deliveryByShopEnabled.isChecked
        marketSettings.minimumAmountForDelivery = binding.minimumAmountForDelivery.text.toString().toFloat().toDouble()

        marketSettings.isStartupModeEnabled = binding.startupModeEnabled.isChecked
        marketSettings.isDemoModeEnabled = binding.demoModeEnabled.isChecked

    }





    private fun saveButtonClick()
    {
        getDataFromViews()

        progress_bar.visibility = View.VISIBLE
        saveButton.visibility = View.INVISIBLE


        var call: Call<ResponseBody> = marketService.updateSettings(
            PrefLogin.getAuthorizationHeader(activity),
            marketSettings
        )

        call.enqueue(
            object : Callback<ResponseBody>{

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
                {

                    if(response.code()==200)
                    {
                        UtilityFunctions.showToastMessage(activity,"Update Successful")
                    }
                    else
                    {
                        UtilityFunctions.showToastMessage(activity,"Failed Code : " + response.code())
                    }


                    progress_bar.visibility = View.INVISIBLE
                    saveButton.visibility = View.VISIBLE

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    UtilityFunctions.showToastMessage(activity,"Failed ... Please Check your network Connection !")


                    progress_bar.visibility = View.INVISIBLE
                    saveButton.visibility = View.VISIBLE

                }

            }
        )

    }





    private fun chargeNowClick()
    {


        var call  = shopUtilityService.generateMonthlyBill(
            PrefLogin.getAuthorizationHeader(requireContext())
        )

        binding.chargeManually.visibility = View.INVISIBLE
        binding.chargeNowProgress.visibility = View.VISIBLE


        call.enqueue(object :Callback<ResponseBody>{

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                binding.chargeManually.visibility = View.VISIBLE
                binding.chargeNowProgress.visibility = View.INVISIBLE

                if(response.isSuccessful)
                {
                    context?.showToast("Charge Successful !")
                }
                else
                {
                    context?.showToast("Failed Code : " + response.code())
                }

            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                binding.chargeManually.visibility = View.VISIBLE
                binding.chargeNowProgress.visibility = View.INVISIBLE

                context?.showToast("Network Failed ... Please try again !")

            }

        })


    }



    init {
        DaggerComponentBuilder.getInstance()
            .netComponent.Inject(this)

    }



    companion object {
        @JvmStatic
        fun newInstance() = EditMarketSettingsKotlin()
    }

}