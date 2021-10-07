package org.nearbyshops.whitelabelapp.aaMultimarketFiles.EditDataScreens.EditMarketSettingsMM

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_edit_market_settings.*
import okhttp3.ResponseBody
import org.nearbyshops.whitelabelapp.API.API_Admin.MarketSettingService
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.Model.ModelMarket.MarketSettings
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class EditMarketSettingsKotlin : Fragment() {


    @Inject
    lateinit var marketService: MarketSettingService


    var marketSettings : MarketSettings = MarketSettings()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveButton.setOnClickListener {
            saveButtonClick()
        }

        getSettings()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        retainInstance = true

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_market_settings, container, false)
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
        cod_enabled.isChecked = marketSettings.isCodEnabled
        pod_enabled.isChecked = marketSettings.isPodEnabled
        razorpay_enabled.isChecked = marketSettings.isOnlinePaymentEnabled

        market_fee_fixed.setText(marketSettings.marketFeeFixed.toString())
        market_fee_commission.setText(marketSettings.marketFeeCommission.toString())
        market_tax_rate.setText(marketSettings.taxInPercent.toString())

        base_delivery_fee_per_order.setText(marketSettings.baseDeliveryChargePerOrder.toString())
        base_delivery_fee_max_distance.setText(marketSettings.baseDeliveryFeeMaxDistance.toString())
        extra_delivery_charge.setText(marketSettings.extraDeliveryChargePerKm.toString())

        bill_amount_for_free_delivery.setText(marketSettings.billAmountForFreeDelivery.toString())
        delivery_by_shop_enabled.isChecked = marketSettings.isDeliveryByShopEnabled
        minimum_amount_for_delivery.setText(marketSettings.minimumAmountForDelivery.toString())

        startup_mode_enabled.isChecked = marketSettings.isStartupModeEnabled
        demo_mode_enabled.isChecked = marketSettings.isDemoModeEnabled

    }


    fun getDataFromViews()
    {
        marketSettings.isCodEnabled = cod_enabled.isChecked
        marketSettings.isPodEnabled = pod_enabled.isChecked
        marketSettings.isOnlinePaymentEnabled = razorpay_enabled.isChecked

        marketSettings.marketFeeFixed = market_fee_fixed.text.toString().toFloat()
        marketSettings.marketFeeCommission = market_fee_commission.text.toString().toFloat()
        marketSettings.taxInPercent = market_tax_rate.text.toString().toFloat()

        marketSettings.baseDeliveryChargePerOrder = base_delivery_fee_per_order.text.toString().toFloat()
        marketSettings.baseDeliveryFeeMaxDistance = base_delivery_fee_max_distance.text.toString().toFloat()
        marketSettings.extraDeliveryChargePerKm = extra_delivery_charge.text.toString().toFloat()

        marketSettings.billAmountForFreeDelivery = bill_amount_for_free_delivery.text.toString().toFloat()
        marketSettings.isDeliveryByShopEnabled = delivery_by_shop_enabled.isChecked
        marketSettings.minimumAmountForDelivery = minimum_amount_for_delivery.text.toString().toFloat().toDouble()

        marketSettings.isStartupModeEnabled = startup_mode_enabled.isChecked
        marketSettings.isDemoModeEnabled = demo_mode_enabled.isChecked

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




    init {
        DaggerComponentBuilder.getInstance()
            .netComponent.Inject(this)
    }



    companion object {
        @JvmStatic
        fun newInstance() = EditMarketSettingsKotlin()
    }

}