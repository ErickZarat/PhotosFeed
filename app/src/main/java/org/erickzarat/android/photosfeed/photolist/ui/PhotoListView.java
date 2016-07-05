package org.erickzarat.android.photosfeed.photolist.ui;

import org.erickzarat.android.photosfeed.entities.Photo;

/**
 * Created by zarathos on 4/07/16
 */
public interface PhotoListView {
    void showList();
    void hideList();
    void showProgress();
    void hideProgress();

    void addPhoto(Photo photo);
    void removePhoto(Photo photo);
    void onPhotosError(String error);
}
