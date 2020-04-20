package org.nearbyshops.enduserappnew.aSDSAdminModule.MarketsList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelMarkets extends AndroidViewModel {


    private Executor executor;
    private LiveData<PagedList<Object>> articleLiveData;

    private MarketsDataFactory feedDataFactory;




    public ViewModelMarkets(@NonNull Application application) {
        super(application);

        init();
    }




    private void init() {

        executor = Executors.newFixedThreadPool(5);

        feedDataFactory = new MarketsDataFactory();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10)
                        .build();


        articleLiveData = (new LivePagedListBuilder(feedDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();

    }





    public void refresh()
    {

        feedDataFactory.getSourceLiveData().getValue().invalidate();
    }





    public LiveData<PagedList<Object>> getArticleLiveData() {
        return articleLiveData;
    }
}
