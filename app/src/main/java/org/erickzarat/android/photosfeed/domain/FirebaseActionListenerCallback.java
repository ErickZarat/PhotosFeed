package org.erickzarat.android.photosfeed.domain;

import com.firebase.client.FirebaseError;

/**
 * Created by zarathos on 2/07/16
 */
public interface FirebaseActionListenerCallback {
    void onSuccess();
    void onError(FirebaseError error);
}
