package org.nearbyshops.whitelabelapp.aaListUI.ViewModels

import android.content.Context
import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderItemService
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order
import org.nearbyshops.whitelabelapp.Model.ModelEndPoints.OrderItemEndPoint
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderHeader.HeaderTitle
import org.nearbyshops.whitelabelapp.aaListUI.Interfaces.DatasetNotifier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class ViewModelOrderDetail(
    var context: Context,
    var datasetNotifier: DatasetNotifier
) {


    @Inject
    lateinit var orderItemService: OrderItemService

    lateinit var order:Order





    fun getOrderDetails(clearDataset : Boolean, orderID:Int, limit : Int, offset:Int, dataset:ArrayList<Any>)
    {


        val call = orderItemService.getOrderItem(
            PrefLogin.getAuthorizationHeader(context), orderID, null,
            PrefLocation.getLatitudeSelected(context), PrefLocation.getLongitudeSelected(context),
            null, null, null, null
        )



        call.enqueue(object : Callback<OrderItemEndPoint?> {
            override fun onResponse(
                call: Call<OrderItemEndPoint?>,
                response: Response<OrderItemEndPoint?>
            ) {


                if (response.code() == 200 && response.body() != null) {
                    dataset.clear()

                    // fetch extra order details
                    order = response.body()!!.orderDetails

                    dataset.add(response.body() as OrderItemEndPoint)

                    val shopProfile = (response.body() as OrderItemEndPoint).shopDetails

                    if (shopProfile != null) {
                        shopProfile.isRt_is_delivering =
                            true // a false value supplied in order to stop telling that shop is not delivering
                        dataset.add(shopProfile)
                    }
                    dataset.add(
                        HeaderTitle(
                            "Items in this Order : " + order.getItemCount().toString()
                        )
                    )

                    dataset.addAll(response.body()!!.results)

                    datasetNotifier.notifyDatasetChanged()


                } else {

                    datasetNotifier.notifyRequestFailedWithErrorCode(response.code())
                }


            }

            override fun onFailure(call: Call<OrderItemEndPoint?>, t: Throwable) {

                datasetNotifier.notifyRequestFailed()


            }
        })


    }



    init {
        DaggerComponentBuilder.getInstance()
            .netComponent.Inject(this)
    }

}