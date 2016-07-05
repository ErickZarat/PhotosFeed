package org.erickzarat.android.photosfeed.photolist;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import org.erickzarat.android.photosfeed.domain.FirebaseAPI;
import org.erickzarat.android.photosfeed.domain.FirebaseActionListenerCallback;
import org.erickzarat.android.photosfeed.domain.FirebaseEventListenerCallback;
import org.erickzarat.android.photosfeed.entities.Photo;
import org.erickzarat.android.photosfeed.libs.base.EventBus;
import org.erickzarat.android.photosfeed.photolist.events.PhotoListEvent;

/**
 * Created by zarathos on 4/07/16
 */
public class PhotoListRepositoryImpl implements PhotoListRepository {
    private EventBus eventBus;
    private FirebaseAPI firebase;

    public PhotoListRepositoryImpl(FirebaseAPI firebase, EventBus eventBus) {
        this.firebase = firebase;
        this.eventBus = eventBus;
    }

    @Override
    public void subscribe() {
        firebase.checkForData(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(FirebaseError error) {
                if (error != null) {
                    post(PhotoListEvent.READ_EVENT, error.getMessage());
                } else {
                    post(PhotoListEvent.READ_EVENT, "");
                }

            }
        });

        firebase.subscribe(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                photo.setId(dataSnapshot.getKey());

                String email = firebase.getAuthEmail();

                boolean publishedByMy = photo.getEmail().equals(email);
                photo.setPublishedByMe(publishedByMy);
                post(PhotoListEvent.READ_EVENT, photo);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                photo.setId(dataSnapshot.getKey());

                post(PhotoListEvent.DELETE_EVENT, photo);
            }

            @Override
            public void onCancelled(FirebaseError error) {
                post(PhotoListEvent.READ_EVENT, error.getMessage());
            }
        });
    }

    @Override
    public void unsubscribe() {
        firebase.unsubscribe();
    }

    @Override
    public void remove(final Photo photo) {
        firebase.remove(photo, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(PhotoListEvent.DELETE_EVENT, photo);
            }

            @Override
            public void onError(FirebaseError error) {
                post(PhotoListEvent.DELETE_EVENT, error.getMessage());
            }
        });
    }

    private void post(int type, Photo photo){
        post(type, null, photo);
    }

    private void post(int type, String error){
        post(type, error, null);
    }

    private void post(int type, String error, Photo photo){
        PhotoListEvent event = new PhotoListEvent();
        event.setType(type);
        event.setError(error);
        event.setPhoto(photo);
        eventBus.post(event);
    }
}
