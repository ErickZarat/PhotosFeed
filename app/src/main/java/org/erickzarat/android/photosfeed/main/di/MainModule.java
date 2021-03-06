package org.erickzarat.android.photosfeed.main.di;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import org.erickzarat.android.photosfeed.domain.FirebaseAPI;
import org.erickzarat.android.photosfeed.libs.base.EventBus;
import org.erickzarat.android.photosfeed.libs.base.ImageStorage;
import org.erickzarat.android.photosfeed.main.*;
import org.erickzarat.android.photosfeed.main.ui.MainView;
import org.erickzarat.android.photosfeed.main.ui.adapters.MainSectionsPagerAdapter;

import javax.inject.Singleton;

/**
 * Created by zarathos on 3/07/16
 */
@Module
public class MainModule {
    private MainView view;
    private String[] titles;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    public MainModule(MainView view, String[] titles, Fragment[] fragments, FragmentManager fragmentManager) {
        this.view = view;
        this.titles = titles;
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
    }

    @Provides
    @Singleton
    MainView providesMainView() {
        return this.view;
    }

    @Provides @Singleton
    MainPresenter providesMainPresenter(MainView view, EventBus eventBus, UploadInteractor uploadInteractor, SessionInteractor sessionInteractor){
        return new MainPresenterImpl(view, eventBus, uploadInteractor, sessionInteractor);
    }

    @Provides @Singleton
    UploadInteractor providesUploadInteractor(MainRepository repository){
        return new UploadInteractorImpl(repository);
    }

    @Provides @Singleton
    SessionInteractor providesSessionInteractor(MainRepository repository){
        return  new SessionInteractorImpl(repository);
    }

    @Provides @Singleton
    MainRepository providesMainRepository(EventBus eventBus, FirebaseAPI firebaseAPI, ImageStorage imageStorage){
        return new MainRepositoryImpl(eventBus, firebaseAPI, imageStorage);
    }

    @Provides @Singleton
    MainSectionsPagerAdapter providesAdapter(FragmentManager fm, String[] titles, Fragment[] fragments){
        return new MainSectionsPagerAdapter(fm, titles, fragments);
    }

    @Provides @Singleton
    FragmentManager providesAdapterFragmentManager(){
        return this.fragmentManager;
    }

    @Provides @Singleton
    Fragment[] providesFragmentArrayForAdapter(){
        return this.fragments;
    }

    @Provides @Singleton
    String[] providesStringArrayForAdapter(){
        return this.titles;
    }
}
