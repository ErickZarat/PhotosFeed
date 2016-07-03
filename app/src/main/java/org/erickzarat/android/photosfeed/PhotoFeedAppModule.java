package org.erickzarat.android.photosfeed;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import org.erickzarat.android.photosfeed.domain.di.DomainModule;

import javax.inject.Singleton;

/**
 * Created by zarathos on 2/07/16
 */
@Module
public class PhotoFeedAppModule {
    PhotoFeedApp photoFeedApp;

    public PhotoFeedAppModule(PhotoFeedApp photoFeedApp) {
        this.photoFeedApp = photoFeedApp;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return photoFeedApp.getApplicationContext();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(photoFeedApp.getSharedPreferencesName(), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return photoFeedApp;
    }
}
