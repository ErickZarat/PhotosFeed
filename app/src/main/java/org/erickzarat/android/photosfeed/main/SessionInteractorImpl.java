package org.erickzarat.android.photosfeed.main;

/**
 * Created by zarathos on 3/07/16
 */
public class SessionInteractorImpl implements SessionInteractor{
    private MainRepository repository;

    public SessionInteractorImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void logout() {
        repository.logout();
    }
}
