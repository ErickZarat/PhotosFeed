package org.erickzarat.android.photosfeed.photomap.ui;

import org.erickzarat.android.photosfeed.entities.Photo;

/**
 * Created by zarathos on 4/07/16
 */
public interface PhotoMapView {
    void addPhoto(Photo photo);
    void removePhoto(Photo photo);
    void onPhotosError(String error);
}
