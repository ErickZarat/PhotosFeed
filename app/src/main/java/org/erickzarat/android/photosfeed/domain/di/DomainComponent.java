package org.erickzarat.android.photosfeed.domain.di;

import dagger.Component;
import org.erickzarat.android.photosfeed.PhotoFeedAppModule;

import javax.inject.Singleton;

/**
 * Created by zarathos on 2/07/16
 */
@Singleton
@Component(modules = {DomainModule.class, PhotoFeedAppModule.class})
public interface DomainComponent {
}
