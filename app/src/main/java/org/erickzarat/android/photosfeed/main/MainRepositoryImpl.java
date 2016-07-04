package org.erickzarat.android.photosfeed.main;

import android.location.Location;
import org.erickzarat.android.photosfeed.domain.FirebaseAPI;
import org.erickzarat.android.photosfeed.entities.Photo;
import org.erickzarat.android.photosfeed.libs.base.EventBus;
import org.erickzarat.android.photosfeed.libs.base.ImageStorage;
import org.erickzarat.android.photosfeed.libs.base.ImageStorageFinishedListener;
import org.erickzarat.android.photosfeed.main.events.MainEvent;

import java.io.File;

/**
 * Created by zarathos on 3/07/16
 */
public class MainRepositoryImpl implements MainRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;
    private ImageStorage imageStorage;

    public MainRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI, ImageStorage imageStorage) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
        this.imageStorage = imageStorage;
    }

    @Override
    public void logout() {
        firebaseAPI.logout();
    }

    @Override
    public void uploadPhoto(Location location, String path) {
        final String newPhotoId = firebaseAPI.create();
        final Photo photo = new Photo();
        photo.setId(newPhotoId);
        photo.setEmail(firebaseAPI.getAuthEmail());

        if(location != null){
            photo.setLongitude(location.getLongitude());
            photo.setLatitutde(location.getLatitude());
        }

        post(MainEvent.UPLOAD_INIT);

        ImageStorageFinishedListener listener =  new ImageStorageFinishedListener() {
            @Override
            public void onSuccess() {
                String url = imageStorage.getImageUrl(newPhotoId);
                photo.setUrl(url);
                firebaseAPI.update(photo);

                post(MainEvent.UPLOAD_COMPLETE);
            }

            @Override
            public void onError(String error) {
                post(MainEvent.UPLOAD_ERROR, error);
            }
        };

        imageStorage.upload(new File(path), photo.getId(), listener);
    }

    private void post(int type){
        post(type, null);
    }

    private void post(int type, String error){
        MainEvent event = new MainEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
