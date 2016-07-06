package org.erickzarat.android.photosfeed.photomap;

import org.erickzarat.android.photosfeed.photomap.events.PhotoMapEvent;

/**
 * Created by zarathos on 4/07/16
 */
public interface PhotoMapPresenter {
    void onCreate();
    void onDestroy();

    void subscribe();
    void unsubscribe();

    void onEventMainThread(PhotoMapEvent event);
}
