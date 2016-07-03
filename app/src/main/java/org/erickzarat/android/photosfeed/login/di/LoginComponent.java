package org.erickzarat.android.photosfeed.login.di;

import dagger.Component;
import org.erickzarat.android.photosfeed.PhotoFeedAppModule;
import org.erickzarat.android.photosfeed.domain.di.DomainModule;
import org.erickzarat.android.photosfeed.libs.di.LibsModule;
import org.erickzarat.android.photosfeed.login.ui.LoginActivity;

import javax.inject.Singleton;

/**
 * Created by zarathos on 3/07/16
 */
@Singleton
@Component(modules = {LoginModule.class, DomainModule.class, LibsModule.class, PhotoFeedAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
