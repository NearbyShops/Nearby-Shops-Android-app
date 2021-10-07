package org.nearbyshops.whitelabelapp.Model.ModelStatusCodes;

/**
 * Created by sumeet on 12/6/16.
 */





// :: staff functions
// confirmOrder()
// setOrderPacked()
// handoverToDelivery()
// acceptReturn()
// unpackOrder()
// paymentReceived()


// delivery guy functions
// AcceptPackage() | DeclinePackage()
// Return() | Deliver()



public class OrderStatusPickFromShop {


    public static final int ORDER_PLACED = 1; // Confirm (Staff)
    public static final int ORDER_CONFIRMED = 2; // Pack (Staff)
    public static final int ORDER_PACKED = 3; // payment-received(staff)
    public static final int ORDER_READY_FOR_PICKUP = 4; // payment-received(staff)

    public static final int DELIVERED = 5;// Payment Received-Complete



//    public static final int CANCELLED_BY_SHOP = 19;
//    public static final int CANCELLED_BY_USER = 20;

    public static final int CANCELLED = 20;








    public static String getStatusString(int orderStatus)
    {
        String statusString = "";

        if(orderStatus == ORDER_PLACED)
        {
            statusString = "Order Placed";
        }
        else if(orderStatus == ORDER_CONFIRMED)
        {
            statusString = "Order Confirmed";
        }
        else if(orderStatus == ORDER_PACKED)
        {
            statusString = "Order Packed";
        }
        else if(orderStatus == ORDER_READY_FOR_PICKUP)
        {
            statusString = "Order Ready for Pickup";
        }
        else if(orderStatus == DELIVERED)
        {
            statusString = "Order Delivered";
        }
        else if(orderStatus == CANCELLED)
        {
            statusString = "Order Cancelled";
        }


        return statusString;
    }


}
