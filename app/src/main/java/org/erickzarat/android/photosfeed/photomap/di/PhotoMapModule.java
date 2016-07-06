package org.erickzarat.android.photosfeed.photomap.di;

import dagger.Module;
import dagger.Provides;
import org.erickzarat.android.photosfeed.domain.FirebaseAPI;
import org.erickzarat.android.photosfeed.libs.base.EventBus;
import org.erickzarat.android.photosfeed.photomap.*;
import org.erickzarat.android.photosfeed.photomap.ui.PhotoMapView;

import javax.inject.Singleton;

/**
 * Created by zarathos on 4/07/16
 */
@Module
public class PhotoMapModule {
    PhotoMapView view;

    public PhotoMapModule(PhotoMapView view) {
        this.view = view;
    }

    @Provides @Singleton
    PhotoMapView providesPhotoContentView() {
        return this.view;
    }

    @Provides @Singleton
    PhotoMapPresenter providesPhotoContentPresenter(EventBus eventBus, PhotoMapView view, PhotoMapInteractor listInteractor) {
        return new PhotoMapPresenterImpl(eventBus, view, listInteractor);
    }

    @Provides @Singleton
    PhotoMapInteractor providesPhotoContentInteractor(PhotoMapRepository repository) {
        return new PhotoMapInteractorImpl(repository);
    }

    @Provides
    @Singleton
    PhotoMapRepository providesPhotoContentRepository(FirebaseAPI firebase, EventBus eventBus) {
        return new PhotoMapRepositoryImpl(firebase, eventBus);
    }
}