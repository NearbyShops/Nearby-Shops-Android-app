package org.nearbyshops.whitelabelapp;

import org.nearbyshops.whitelabelapp.Model.ModelCartOrder.Order;

import java.util.ArrayList;


/**
 * Created by sumeet on 15/3/16.
 */
public class ApplicationState {

    private static ApplicationState instance = null;

    private MyApplication myApplication;

    ArrayList<Order> selectedOrdersForDelivery = new ArrayList<>();








    public static ApplicationState getInstance()
    {

        if(instance == null)
        {
            instance = new ApplicationState();

            return instance;
        }

        return instance;
    }



    MyApplication getMyApplication() {
        return myApplication;
    }

    void setMyApplication(MyApplication myApplication) {
        this.myApplication = myApplication;
    }


    public ArrayList<Order> getSelectedOrdersForDelivery() {
        return selectedOrdersForDelivery;
    }

    public void setSelectedOrdersForDelivery(ArrayList<Order> selectedOrdersForDelivery) {
        this.selectedOrdersForDelivery = selectedOrdersForDelivery;
    }
}
