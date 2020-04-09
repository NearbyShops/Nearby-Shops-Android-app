package org.nearbyshops.enduserappnew.Lists.OrderHistoryPaging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class OrdersDataFactory extends DataSource.Factory {


    private MutableLiveData<OrdersDataSource> sourceLiveData =
            new MutableLiveData<>();

    private OrdersDataSource latestSource;




    @NonNull
    @Override
    public DataSource create() {

        latestSource = new OrdersDataSource();
        sourceLiveData.postValue(latestSource);
        return latestSource;
    }



    public MutableLiveData<OrdersDataSource> getSourceLiveData() {
        return sourceLiveData;
    }



}
