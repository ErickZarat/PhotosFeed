package org.erickzarat.android.photosfeed.libs.base;

import android.widget.ImageView;

/**
 * Created by zarathos on 26/06/16
 */
public interface ImageLoader {
    void load(ImageView imageView, String URL);
    void setOnImageLoadingListener(Object listener);
}
