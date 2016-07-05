package org.erickzarat.android.photosfeed.photolist;

import org.erickzarat.android.photosfeed.entities.Photo;

/**
 * Created by zarathos on 4/07/16
 */
public class PhotoListInteractorImpl implements PhotoListInteractor {
    private PhotoListRepository repository;

    public PhotoListInteractorImpl(PhotoListRepository repository) {
        this.repository = repository;
    }

    @Override
    public void subscribe() {
        repository.subscribe();
    }

    @Override
    public void unsubscribe() {
        repository.unsubscribe();
    }

    @Override
    public void removePhoto(Photo photo) {
        repository.remove(photo);
    }
}
