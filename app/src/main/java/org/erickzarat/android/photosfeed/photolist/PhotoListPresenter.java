package org.erickzarat.android.photosfeed.photolist;

import org.erickzarat.android.photosfeed.entities.Photo;
import org.erickzarat.android.photosfeed.photolist.events.PhotoListEvent;

/**
 * Created by zarathos on 4/07/16
 */
public interface PhotoListPresenter {
    void onCreate();
    void onDestroy();

    void subscribe();
    void unsubscribe();

    void removePhoto(Photo photo);
    void onEventMainThread(PhotoListEvent event);
}
