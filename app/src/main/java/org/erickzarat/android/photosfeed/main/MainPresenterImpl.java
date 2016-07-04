package org.erickzarat.android.photosfeed.main;

import android.location.Location;
import android.support.v7.widget.SwitchCompat;
import org.erickzarat.android.photosfeed.libs.base.EventBus;
import org.erickzarat.android.photosfeed.main.events.MainEvent;
import org.erickzarat.android.photosfeed.main.ui.MainView;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by zarathos on 3/07/16
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView view;
    private EventBus eventBus;
    private UploadInteractor uploadInteractor;
    private SessionInteractor sessionInteractor;

    public MainPresenterImpl(MainView view, EventBus eventBus, UploadInteractor uploadInteractor, SessionInteractor sessionInteractor) {
        this.view = view;
        this.eventBus = eventBus;
        this.uploadInteractor = uploadInteractor;
        this.sessionInteractor = sessionInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void logout() {
        sessionInteractor.logout();
    }

    @Override
    public void uploadPhoto(Location location, String path) {
        uploadInteractor.execute(location, path);
    }

    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case MainEvent.UPLOAD_INIT:
                    view.onUploadInit();
                    break;
                case MainEvent.UPLOAD_COMPLETE:
                    view.onUploadComplete();
                    break;
                case MainEvent.UPLOAD_ERROR:
                    view.onUploadError(event.getError());
                    break;
            }
        }
    }
}
