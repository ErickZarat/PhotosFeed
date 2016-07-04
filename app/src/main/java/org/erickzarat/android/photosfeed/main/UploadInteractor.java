package org.erickzarat.android.photosfeed.main;

import android.location.Location;

/**
 * Created by zarathos on 3/07/16
 */
public interface UploadInteractor {
    void execute(Location location, String path);
}
