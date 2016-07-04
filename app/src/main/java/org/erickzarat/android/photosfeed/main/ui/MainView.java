package org.erickzarat.android.photosfeed.main.ui;

/**
 * Created by zarathos on 3/07/16
 */
public interface MainView {
    void onUploadInit();
    void onUploadComplete();
    void onUploadError(String error);
}
