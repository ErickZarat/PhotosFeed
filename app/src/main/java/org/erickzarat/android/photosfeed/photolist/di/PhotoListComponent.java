package org.erickzarat.android.photosfeed.photolist.di;

import dagger.Component;
import org.erickzarat.android.photosfeed.PhotoFeedAppModule;
import org.erickzarat.android.photosfeed.domain.di.DomainModule;
import org.erickzarat.android.photosfeed.libs.di.LibsModule;
import org.erickzarat.android.photosfeed.photolist.ui.PhotoListFragment;

import javax.inject.Singleton;

/**
 * Created by zarathos on 4/07/16
 */
@Singleton
@Component(modules = {PhotoListModule.class, DomainModule.class, LibsModule.class, PhotoFeedAppModule.class})
public interface PhotoListComponent {
    void inject(PhotoListFragment fragment);
}
