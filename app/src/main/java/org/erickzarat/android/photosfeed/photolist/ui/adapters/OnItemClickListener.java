package org.erickzarat.android.photosfeed.photolist.ui.adapters;

import android.widget.ImageView;
import org.erickzarat.android.photosfeed.entities.Photo;

/**
 * Created by zarathos on 4/07/16
 */
public interface OnItemClickListener {
    void onPlaceClick(Photo photo);
    void onShareClick(Photo photo, ImageView img);
    void onDeleteClick(Photo photo);
}
