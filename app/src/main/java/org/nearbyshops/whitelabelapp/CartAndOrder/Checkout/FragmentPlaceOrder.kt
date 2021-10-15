package org.nearbyshops.whitelabelapp.CartAndOrder.Checkout

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.fragment_place_order_kotlin.*
import okhttp3.ResponseBody
import org.json.JSONObject
import org.nearbyshops.whitelabelapp.API.CartStatsService
import org.nearbyshops.whitelabelapp.API.OrdersAPI.OrderService
import org.nearbyshops.whitelabelapp.API.RazorPayService
import org.nearbyshops.whitelabelapp.CartAndOrder.DeliveryAddress.DeliveryAddressSelectionFragment
import org.nearbyshops.whitelabelapp.CartAndOrder.ViewHoldersOrders.ViewHolderDeliveryAddress.ViewHolderDeliveryAddress
import org.nearbyshops.whitelabelapp.DaggerComponentBuilder
import org.nearbyshops.whitelabelapp.EditDataScreens.EditDeliveryAddress.EditAddressFragment
import org.nearbyshops.whitelabelapp.EditDataScreens.EditDeliveryAddress.EditDeliveryAddress
import org.nearbyshops.whitelabelapp.Model.ModelBilling.RazorPayOrder
import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order
import org.nearbyshops.whitelabelapp.Model.ModelStats.CartStats
import org.nearbyshops.whitelabelapp.Model.ModelStats.DeliveryAddress
import org.nearbyshops.whitelabelapp.Model.ModelUtility.DeliveryConfig
import org.nearbyshops.whitelabelapp.MyApplication
import org.nearbyshops.whitelabelapp.Preferences.PrefAppSettings
import org.nearbyshops.whitelabelapp.Preferences.PrefCurrency
import org.nearbyshops.whitelabelapp.Preferences.PrefLocation
import org.nearbyshops.whitelabelapp.Preferences.PrefLogin
import org.nearbyshops.whitelabelapp.R
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions
import org.nearbyshops.whitelabelapp.Utility.UtilityFunctions.showToastMessage
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHoldersCommon.ViewHolderButton.ButtonData
import org.nearbyshops.whitelabelapp.databinding.FragmentPlaceOrderKotlinBinding
import org.nearbyshops.whitelabelapp.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class FragmentPlaceOrder : Fragment() , ViewHolderDeliveryAddress.ListItemClick,ViewHolderButton.ListItemClick,
    PaymentResultListener {




    private var orderTotal = 0.0
    var shopID = 0
    var order:Order = Order()
    var deliveryConfig:DeliveryConfig? = null

    lateinit var selectedAddress:DeliveryAddress
    lateinit var cartStats: CartStats
    var razorPayKey: String = "rzp_test_7nAt5ORw1yAE4a"
    lateinit var razorPayOrder: RazorPayOrder

    var pricingFetched = false // indicates whether pricing is provided to the customer or not


    @Inject
    lateinit var transactionService: RazorPayService

    @Inject
    lateinit var cartStatsService: CartStatsService

    @Inject
    lateinit var orderService: OrderService


    lateinit var binding : FragmentPlaceOrderKotlinBinding



//        return inflater.inflate(R.layout.fragment_place_order_kotlin, container, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        retainInstance=true

        // Inflate the layout for this fragment
        binding = FragmentPlaceOrderKotlinBinding.inflate(inflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val shop = PrefShopHome.getShop(requireContext())
        shop_name.text = activity?.intent?.getStringExtra("shop_name")


        // set defaults
//        order.paymentMode=Order.PAYMENT_MODE_CASH_ON_DELIVERY
        order.deliveryMode = Order.DELIVERY_MODE_HOME_DELIVERY
        shopID = activity?.intent?.getIntExtra("shop_id", 0) ?: 0


        bindAllViews()

        // set click listeners
        pickup.setOnClickListener { pickupClick() }
        delivery.setOnClickListener{ deliveryClick() }
        place_order_button.setOnClickListener{ placeOrderClick() }
        pay_by_cash.setOnClickListener{ payByCashClick() }
        pay_online.setOnClickListener{ payOnlineClick() }
        change_address_button.setOnClickListener{ changeAddressClick() }
    }





    fun getCartStats() {


        pricingFetched = false


        val pd = ProgressDialog(activity)
        pd.setMessage(getString(R.string.loading_message))
        pd.show()


        val user = PrefLogin.getUser(activity)
        val call = cartStatsService.getCartStats(
            user.userID, shopID,
            selectedAddress.id
        )


        call.enqueue(object : Callback<CartStats?> {
            override fun onResponse(call: Call<CartStats?>, response: Response<CartStats?>) {

                if (response.code() == 200 && response.body()!=null) {

                    cartStats = response.body() as CartStats
                    orderTotal = cartStats.cart_Total
                    deliveryConfig = cartStats.deliveryConfig



                    print(deliveryConfig.toString())


                    pricingFetched = true

                    bindPricing()

                    pd.dismiss()

                }
            }

            override fun onFailure(call: Call<CartStats?>, t: Throwable) {

                requireContext().showToast("Network connection failed. Check Internet connectivity !")
                pd.dismiss()
            }
        })
    }




    private fun bindPricing()
    {
        var deliveryCharge = 0.0
        var distanceFee = 0.0


        if(deliveryConfig!=null)
        {

            if(order.deliveryMode!=Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
            {
                deliveryCharge = deliveryConfig!!.baseDeliveryCharge
                distanceFee = deliveryConfig!!.distanceFee
            }


            if(distanceFee > 0.0)
            {
                binding.distanceFeeText.visibility = View.VISIBLE
                binding.distanceFeeValue.visibility = View.VISIBLE
                binding.distanceFeeValue.text = PrefCurrency.getCurrencySymbol(
                    context
                ) + " " + String.format("%.2f", distanceFee)
            }
            else
            {
                binding.distanceFeeText.visibility = View.GONE
                binding.distanceFeeValue.visibility = View.GONE
            }
        }



        val netPayable = cartStats.cart_Total + deliveryCharge + distanceFee



        // bind billing details
        binding.itemTotalValue.text = PrefCurrency.getCurrencySymbol(
            context
        ) + " " + String.format("%.2f", cartStats.cart_Total)
        binding.deliveryChargeValue.text = PrefCurrency.getCurrencySymbol(
            context
        ) + " " + String.format("%.2f", deliveryCharge)
        binding.netPayableValue.text = PrefCurrency.getCurrencySymbol(
            context
        ) + " " + String.format("%.2f", netPayable)

        binding.toPay.text = "Pay  " + PrefCurrency.getCurrencySymbol(
            context
        ) + String.format(" %.2f", netPayable)


        if (order.savingsOverMRP > 0) {

            binding.savingsBlock.visibility = View.VISIBLE
            binding.savingsValue.text = PrefCurrency.getCurrencySymbol(
                context
            ) + " " + String.format("%.2f", cartStats.savingsOverMRP)

        } else {

            binding.savingsBlock.visibility = View.GONE
        }
    }




    private fun pickupClick()
    {
        order.deliveryMode = Order.DELIVERY_MODE_PICKUP_FROM_SHOP
        bindDeliveryMode()
        bindPricing()
    }





    private fun deliveryClick()
    {
        order.deliveryMode = Order.DELIVERY_MODE_HOME_DELIVERY
        bindDeliveryMode()
        bindPricing()
    }



    private fun bindDeliveryMode()
    {
        if(order.deliveryMode==Order.DELIVERY_MODE_PICKUP_FROM_SHOP)
        {
            pickup.setBackgroundResource(R.drawable.border_background_selected)
            pickup.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_circle_green,0)

            delivery.setBackgroundResource(R.drawable.border_background)
            delivery.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }
        else if(order.deliveryMode==Order.DELIVERY_MODE_HOME_DELIVERY)
        {
            delivery.setBackgroundResource(R.drawable.border_background_selected)
            delivery.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_circle_green,0)

            pickup.setBackgroundResource(R.drawable.border_background)
            pickup.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }

    }




    private fun payByCashClick()
    {
        binding.paymentMessage.text = "Pay using Cash or GooglePay, PhonePay, PayTM or BHIM app"
        order.paymentMode=Order.PAYMENT_MODE_CASH_ON_DELIVERY
        bindPaymentMethod()
    }




    private fun payOnlineClick()
    {
//        context?.showToast("Online Payment Not Available for this Shop !")
//        return

        binding.paymentMessage.text = "Make Online Payment !"
        order.paymentMode=Order.PAYMENT_MODE_RAZORPAY
        bindPaymentMethod()
    }


    private fun bindPaymentMethod()
    {
        if(order.paymentMode==Order.PAYMENT_MODE_CASH_ON_DELIVERY)
        {
            pay_by_cash.setBackgroundResource(R.drawable.border_background_selected)
            pay_by_cash.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_circle_green,0)

            pay_online.setBackgroundResource(R.drawable.border_background)
            pay_online.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }
        else if(order.paymentMode==Order.PAYMENT_MODE_RAZORPAY)
        {
            pay_online.setBackgroundResource(R.drawable.border_background_selected)
            pay_online.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_circle_green,0)

            pay_by_cash.setBackgroundResource(R.drawable.border_background)
            pay_by_cash.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        }

    }






    private fun bindAddress()
    {
        if(PrefLocation.getDeliveryAddress(activity)!=null)
        {
            selectedAddress = PrefLocation.getDeliveryAddress(activity)
            getCartStats()

            address_name.text = selectedAddress.name
            delivery_address.text = selectedAddress.deliveryAddress
            address_phone.text = "Phone : " + selectedAddress.phoneNumber.toString()
        }
    }




    override fun changeAddressClick()
    {
        DeliveryAddressSelectionFragment.newInstance()
            .show(childFragmentManager, "address_selection")
    }


    private fun bindAllViews()
    {
        bindDeliveryMode()
        bindPaymentMethod()
        bindAddress()
    }





    private fun placeOrderClick()
    {
        order.endUserID = PrefLogin.getUser(activity).userID
        order.deliveryAddressID = selectedAddress.id


        if(!pricingFetched)
        {
            requireContext().showToast("Sorry ! We are unable to connect to the server !")
            return
        }
        else if(order.deliveryMode==0)
        {
            requireContext().showToast("Please Select Pickup or Delivery !")
            scroll_view.smoothScrollTo(0,0)
            return
        }
        else if(order.paymentMode==0)
        {
            requireContext().showToast("Please Select Payment Method !")
            scroll_view.smoothScrollTo(0,1000)
            return
        }


//        order.netPayable =
        placeOrder(false)
    }





    fun placeOrder(paymentDone: Boolean) {


        place_order_button.visibility = View.INVISIBLE
        progress_bar_place_order.visibility = View.VISIBLE


        if (order.paymentMode == Order.PAYMENT_MODE_RAZORPAY && !paymentDone) {
            createOrderRazorPay()
            return
        }



        val call: Call<ResponseBody> = orderService.postOrder(order, cartStats.cartID)

        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {


                place_order_button.visibility = View.VISIBLE
                progress_bar_place_order.visibility = View.INVISIBLE

                if (response.code() == 201) {


//                    UtilityFunctions.showToastMessage(activity,"Successful !")
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, PlaceOrderResult())
                        .commit()


                } else {

                    UtilityFunctions.showToastMessage(activity,"Failed Code : " + response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {


                place_order_button.visibility = View.VISIBLE
                progress_bar_place_order.visibility = View.INVISIBLE
                showToastMessage(activity,getString(R.string.network_request_failed))
            }
        })
    }


    private fun createOrderRazorPay() {

        razorPayOrder = RazorPayOrder()

        val call: Call<RazorPayOrder> = transactionService.createOrder(orderTotal)

        call.enqueue(object : Callback<RazorPayOrder> {
            override fun onResponse(call: Call<RazorPayOrder>, response: Response<RazorPayOrder>) {

                if (response.code() == 200) {

                    razorPayOrder.rzpOrderID = response.body()?.rzpOrderID
                    startPaymentRazorpay()

                }
                else
                {

                    place_order_button.visibility = View.VISIBLE
                    progress_bar_place_order.visibility = View.INVISIBLE

                    showToastMessage(activity,"RazorPay failed ... Code : " + response.code() + " Choose another method !")

                }
            }

            override fun onFailure(call: Call<RazorPayOrder>, t: Throwable) {

                showToastMessage(activity,"Create Order Failed !")

                place_order_button.visibility = View.VISIBLE
                progress_bar_place_order.visibility = View.INVISIBLE
            }
        })
    }



    fun startPaymentRazorpay() {
        val checkout = Checkout()
        checkout.setKeyID(razorPayKey)
        checkout.setImage(R.drawable.custom_icon);

        val activity: Activity? = activity
        try {
            val options = JSONObject()
            options.put("name", "Nearby Shops")
            options.put("description", "Payment for Your Order")
            options.put("image", "https://upload.wikimedia.org/wikipedia/commons/c/c2/Spark_App_Logo.svg")
            options.put("order_id", razorPayOrder.rzpOrderID)
            options.put("currency", "INR")
            options.put("amount", orderTotal.toString())
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("Razorpay", "Error in starting Razorpay Checkout", e)
        }
    }


    override fun onPaymentSuccess(s: String?) {

        place_order_button.visibility = View.VISIBLE
        progress_bar_place_order.visibility = View.INVISIBLE

//        showToastMessage(s);
        razorPayOrder.rzpPaymentID = s
        order.razorPayOrder = razorPayOrder
        placeOrder(true)
    }


    override fun onPaymentError(i: Int, s: String?) {
        place_order_button.visibility = View.VISIBLE
        progress_bar_place_order.visibility = View.INVISIBLE
        showToastMessage(activity,s)
    }



    override fun listItemClick(address: DeliveryAddress?, position: Int) {

        if (address != null) {

            val addressBottomSheet = childFragmentManager.findFragmentByTag("address_selection");

            if(addressBottomSheet is DeliveryAddressSelectionFragment)
            {
                addressBottomSheet.dismiss()
            }

            PrefLocation.saveDeliveryAddress(address,activity)
            bindAddress()
        }
    }



    override fun buttonClick(data: ButtonData?) {

        if (data?.layoutType == ViewHolderButton.LAYOUT_TYPE_ADD_NEW_ADDRESS) {

            val intent = Intent(activity, EditDeliveryAddress::class.java)
            intent.putExtra(EditAddressFragment.EDIT_MODE_INTENT_KEY, EditAddressFragment.MODE_ADD)
            intent.putExtra("select_added_address", true)
            startActivityForResult(intent, 25)

        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==25 && resultCode==50)
        {
            val addressBottomSheet = childFragmentManager.findFragmentByTag("address_selection");

            if(addressBottomSheet is DeliveryAddressSelectionFragment)
            {
                addressBottomSheet.dismiss()
            }

            bindAddress()
        }
    }





    init {


        DaggerComponentBuilder.getInstance()
            .netComponent.Inject(this)

        Checkout.preload(MyApplication.getAppContext())
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentPlaceOrder()
    }



}