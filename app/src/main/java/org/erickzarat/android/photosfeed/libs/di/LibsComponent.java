package org.erickzarat.android.photosfeed.libs.di;

import dagger.Component;
import org.erickzarat.android.photosfeed.PhotoFeedAppModule;

import javax.inject.Singleton;

/**
 * Created by zarathos on 3/07/16
 */
@Singleton
@Component(modules = {LibsModule.class, PhotoFeedAppModule.class})
public interface LibsComponent {
}
