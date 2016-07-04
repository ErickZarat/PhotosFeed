package org.erickzarat.android.photosfeed.main;

import android.location.Location;

/**
 * Created by zarathos on 3/07/16
 */
public class UploadInteractorImpl implements  UploadInteractor {
    private MainRepository repository;

    public UploadInteractorImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Location location, String path) {
        repository.uploadPhoto(location, path);
    }
}
