package org.erickzarat.android.photosfeed.photolist;

import org.erickzarat.android.photosfeed.entities.Photo;

/**
 * Created by zarathos on 4/07/16
 */
public interface PhotoListRepository {
    void subscribe();
    void unsubscribe();
    void remove(Photo photo);
}
