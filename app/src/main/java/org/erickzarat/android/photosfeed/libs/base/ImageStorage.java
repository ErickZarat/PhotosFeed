package org.erickzarat.android.photosfeed.libs.base;

import java.io.File;

/**
 * Created by zarathos on 3/07/16
 */
public interface ImageStorage {
    String getImageUrl(String id);
    void upload(File file, String id, ImageStorageFinishedListener listener);
}
