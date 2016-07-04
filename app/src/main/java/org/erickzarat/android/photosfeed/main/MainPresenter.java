package org.erickzarat.android.photosfeed.main;

import android.location.Location;
import org.erickzarat.android.photosfeed.main.events.MainEvent;

/**
 * Created by zarathos on 3/07/16
 */
public interface MainPresenter {
    void onCreate();
    void onDestroy();

    void logout();
    void uploadPhoto(Location location, String path);
    void onEventMainThread(MainEvent event);
}
