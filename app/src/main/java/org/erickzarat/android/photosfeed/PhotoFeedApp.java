package org.erickzarat.android.photosfeed;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.firebase.client.Firebase;
import org.erickzarat.android.photosfeed.domain.di.DomainModule;
import org.erickzarat.android.photosfeed.libs.di.LibsModule;
import org.erickzarat.android.photosfeed.login.di.DaggerLoginComponent;
import org.erickzarat.android.photosfeed.login.di.LoginComponent;
import org.erickzarat.android.photosfeed.login.di.LoginModule;
import org.erickzarat.android.photosfeed.login.ui.LoginView;
import org.erickzarat.android.photosfeed.main.di.DaggerMainComponent;
import org.erickzarat.android.photosfeed.main.di.MainComponent;
import org.erickzarat.android.photosfeed.main.di.MainModule;
import org.erickzarat.android.photosfeed.main.ui.MainActivity;
import org.erickzarat.android.photosfeed.main.ui.MainView;
import org.erickzarat.android.photosfeed.photolist.di.DaggerPhotoListComponent;
import org.erickzarat.android.photosfeed.photolist.di.PhotoListComponent;
import org.erickzarat.android.photosfeed.photolist.di.PhotoListModule;
import org.erickzarat.android.photosfeed.photolist.ui.PhotoListView;
import org.erickzarat.android.photosfeed.photolist.ui.adapters.OnItemClickListener;
import org.erickzarat.android.photosfeed.photomap.di.DaggerPhotoMapComponent;
import org.erickzarat.android.photosfeed.photomap.di.PhotoMapComponent;
import org.erickzarat.android.photosfeed.photomap.di.PhotoMapModule;
import org.erickzarat.android.photosfeed.photomap.ui.PhotoMapView;

/**
 * Created by zarathos on 2/07/16
 */
public class PhotoFeedApp extends Application{
    private final static String EMAIL_KEY = "email";
    private final static String SHARED_PREFERENCES_NAME = "UserPrefs";
    private final static String FIREBASE_URL = "https://zarathosphotosfeed.firebaseio.com/";

    private DomainModule domainModule;
    private PhotoFeedAppModule photoFeedModule;

    @Override
    public void onCreate(){
        super.onCreate();
        initFirebase();
        initModules();
    }

    private void initModules() {
        photoFeedModule = new PhotoFeedAppModule(this);
        domainModule = new DomainModule(FIREBASE_URL);
    }

    private void initFirebase() {
        Firebase.setAndroidContext(this);
    }

    public String getEmailKey() {
        return EMAIL_KEY;
    }

    public String getSharedPreferencesName() {
        return SHARED_PREFERENCES_NAME;
    }

    public LoginComponent getLoginComponent(LoginView view){
        return DaggerLoginComponent
                .builder()
                .photoFeedAppModule(photoFeedModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(null))
                .loginModule(new LoginModule(view))
                .build();
    }

    public MainComponent getMainComponent(MainView view, FragmentManager manager, Fragment[] fragments, String[] titles){
        return DaggerMainComponent
                .builder()
                .photoFeedAppModule(photoFeedModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(null))
                .mainModule(new MainModule(view, titles, fragments, manager))
                .build();
    }

    public PhotoListComponent getPhotoListComponent(Fragment fragment, PhotoListView view, OnItemClickListener onItemClickListener) {
        return DaggerPhotoListComponent
                .builder()
                .photoFeedAppModule(photoFeedModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(fragment))
                .photoListModule(new PhotoListModule(view, onItemClickListener))
                .build();

    }

    public PhotoMapComponent getPhotoMapComponent(Fragment fragment, PhotoMapView view) {
        return DaggerPhotoMapComponent
                .builder()
                .photoFeedAppModule(photoFeedModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(fragment))
                .photoMapModule(new PhotoMapModule(view))
                .build();

    }

}
