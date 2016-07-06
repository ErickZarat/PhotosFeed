package org.erickzarat.android.photosfeed.photomap.di;

import dagger.Component;
import org.erickzarat.android.photosfeed.PhotoFeedAppModule;
import org.erickzarat.android.photosfeed.domain.di.DomainModule;
import org.erickzarat.android.photosfeed.libs.di.LibsModule;
import org.erickzarat.android.photosfeed.photomap.ui.PhotoMapFragment;

import javax.inject.Singleton;

/**
 * Created by zarathos on 4/07/16
 */
@Singleton
@Component(modules = {PhotoMapModule.class, DomainModule.class, LibsModule.class, PhotoFeedAppModule.class})
public interface PhotoMapComponent {
    void inject(PhotoMapFragment fragment);
}