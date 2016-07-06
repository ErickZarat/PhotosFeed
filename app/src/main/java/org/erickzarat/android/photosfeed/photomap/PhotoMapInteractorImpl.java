package org.erickzarat.android.photosfeed.photomap;

/**
 * Created by zarathos on 4/07/16
 */
public class PhotoMapInteractorImpl implements PhotoMapInteractor {
    private PhotoMapRepository repository;

    public PhotoMapInteractorImpl(PhotoMapRepository repository) {
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
}
