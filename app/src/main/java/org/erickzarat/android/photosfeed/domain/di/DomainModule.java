package org.erickzarat.android.photosfeed.domain.di;

import android.content.Context;
import android.location.Geocoder;
import com.firebase.client.Firebase;
import dagger.Module;
import dagger.Provides;
import org.erickzarat.android.photosfeed.domain.FirebaseAPI;
import org.erickzarat.android.photosfeed.domain.Util;

import javax.inject.Singleton;

/**
 * Created by zarathos on 3/07/16
 */
@Module
public class DomainModule {
    String firebaseURL;

    public DomainModule(String firebaseUrl) {
        this.firebaseURL = firebaseUrl;
    }

    @Provides
    @Singleton
    FirebaseAPI providesFirebaseAPI(Firebase firebase) {
        return new FirebaseAPI(firebase);
    }

    @Provides
    @Singleton
    Firebase providesFirebase(String firebaseURL) {
        return new Firebase(firebaseURL);
    }

    @Provides
    @Singleton
    String providesFirebaseURL(){
        return this.firebaseURL;
    }

    @Provides
    @Singleton
    Util providesUtil(Geocoder geocoder) {
        return new Util(geocoder);
    }

    @Provides
    @Singleton
    Geocoder providesGeocoder(Context context) {
        return new Geocoder(context);
    }
}
