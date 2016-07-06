package org.erickzarat.android.photosfeed.photomap;

import org.erickzarat.android.photosfeed.libs.base.EventBus;
import org.erickzarat.android.photosfeed.photomap.events.PhotoMapEvent;
import org.erickzarat.android.photosfeed.photomap.ui.PhotoMapView;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by zarathos on 4/07/16
 */
public class PhotoMapPresenterImpl implements PhotoMapPresenter {
    private EventBus eventBus;
    private PhotoMapView view;
    private PhotoMapInteractor interactor;

    public PhotoMapPresenterImpl(EventBus eventBus, PhotoMapView view, PhotoMapInteractor interactor) {
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
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void subscribe() {
        interactor.subscribe();
    }

    @Override
    public void unsubscribe() {
        interactor.unsubscribe();
    }

    @Override
    @Subscribe
    public void onEventMainThread(PhotoMapEvent event) {
        if (this.view != null) {
            String error = event.getError();
            if (error != null) {
                view.onPhotosError(error);
            } else {
                if (event.getType() == PhotoMapEvent.READ_EVENT) {
                    view.addPhoto(event.getPhoto());
                } else if (event.getType() == PhotoMapEvent.DELETE_EVENT) {
                    view.removePhoto(event.getPhoto());
                }
            }
        }
    }
}
