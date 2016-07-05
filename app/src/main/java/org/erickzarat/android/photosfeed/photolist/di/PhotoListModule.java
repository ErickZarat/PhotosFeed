package org.erickzarat.android.photosfeed.photolist.di;

import dagger.Module;
import dagger.Provides;
import org.erickzarat.android.photosfeed.domain.FirebaseAPI;
import org.erickzarat.android.photosfeed.domain.Util;
import org.erickzarat.android.photosfeed.entities.Photo;
import org.erickzarat.android.photosfeed.libs.base.EventBus;
import org.erickzarat.android.photosfeed.libs.base.ImageLoader;
import org.erickzarat.android.photosfeed.photolist.*;
import org.erickzarat.android.photosfeed.photolist.ui.PhotoListView;
import org.erickzarat.android.photosfeed.photolist.ui.adapters.OnItemClickListener;
import org.erickzarat.android.photosfeed.photolist.ui.adapters.PhotoListAdapter;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zarathos on 4/07/16
 */
@Module
public class PhotoListModule {
    PhotoListView view;
    OnItemClickListener onItemClickListener;

    public PhotoListModule(PhotoListView view, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides @Singleton
    PhotoListView providesPhotoListView() {
        return this.view;
    }

    @Provides @Singleton
    PhotoListPresenter providesPhotoListPresenter(EventBus eventBus, PhotoListView view, PhotoListInteractor listInteractor) {
        return new PhotoListPresenterImpl(eventBus, view, listInteractor);
    }

    @Provides @Singleton
    PhotoListInteractor providesPhotoListInteractor(PhotoListRepository repository) {
        return new PhotoListInteractorImpl(repository);
    }

    @Provides @Singleton
    PhotoListRepository providesPhotoListRepository(FirebaseAPI firebase, EventBus eventBus) {
        return new PhotoListRepositoryImpl(firebase, eventBus);
    }

    @Provides @Singleton
    PhotoListAdapter providesPhotosAdapter(Util utils, List<Photo> photoList, ImageLoader imageLoader, OnItemClickListener onItemClickListener) {
        return new PhotoListAdapter(utils, photoList, imageLoader, onItemClickListener);
    }

    @Provides @Singleton
    OnItemClickListener providesOnItemClickListener() {
        return this.onItemClickListener;
    }

    @Provides
    @Singleton
    List<Photo> providesPhotosList() {
        return new ArrayList<Photo>();
    }
}
