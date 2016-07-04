package org.erickzarat.android.photosfeed.main.di;

import dagger.Component;
import org.erickzarat.android.photosfeed.PhotoFeedAppModule;
import org.erickzarat.android.photosfeed.domain.di.DomainModule;
import org.erickzarat.android.photosfeed.libs.di.LibsModule;
import org.erickzarat.android.photosfeed.main.ui.MainActivity;

import javax.inject.Singleton;

/**
 * Created by zarathos on 3/07/16
 */
@Singleton
@Component(modules = {MainModule.class, LibsModule.class, DomainModule.class, PhotoFeedAppModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
