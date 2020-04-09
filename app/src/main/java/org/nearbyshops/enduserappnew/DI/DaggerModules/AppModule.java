package org.nearbyshops.enduserappnew.DI.DaggerModules;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import org.nearbyshops.enduserappnew.MyApplication;

import javax.inject.Singleton;

/**
 * Created by sumeet on 14/5/16.
 */

@Module
public class AppModule {

    MyApplication mApplication;

    public AppModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

}
