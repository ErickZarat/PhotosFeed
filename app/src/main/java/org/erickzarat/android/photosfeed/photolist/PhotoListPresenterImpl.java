package org.erickzarat.android.photosfeed.photolist;

import org.erickzarat.android.photosfeed.entities.Photo;
import org.erickzarat.android.photosfeed.libs.base.EventBus;
import org.erickzarat.android.photosfeed.photolist.events.PhotoListEvent;
import org.erickzarat.android.photosfeed.photolist.ui.PhotoListView;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by zarathos on 4/07/16
 */
public class PhotoListPresenterImpl implements PhotoListPresenter {
    EventBus eventBus;
    PhotoListView view;
    PhotoListInteractor interactor;
    private final static String EMPTY_LIST = "Listado vacío";

    public PhotoListPresenterImpl(EventBus eventBus, PhotoListView view, PhotoListInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        eventBus.unregister(this);
    }

    @Override
    public void subscribe() {
        if (view != null){
            view.hideList();
            view.showProgress();
        }
        interactor.subscribe();
    }

    @Override
    public void unsubscribe() {
        interactor.unsubscribe();
    }

    @Override
    public void removePhoto(Photo photo) {
        interactor.removePhoto(photo);
    }

    @Override
    @Subscribe
    public void onEventMainThread(PhotoListEvent event) {
        if (view != null) {
            view.hideProgress();
            view.showList();
        }
        String error = event.getError();
        if (error != null) {
            if (error.isEmpty()) {
                view.onPhotosError(EMPTY_LIST);
            } else {
                view.onPhotosError(error);
            }
        } else {
            if (event.getType() == PhotoListEvent.READ_EVENT) {
                view.addPhoto(event.getPhoto());
            } else if (event.getType() == PhotoListEvent.DELETE_EVENT) {
                view.removePhoto(event.getPhoto());
            }
        }

    }
}
