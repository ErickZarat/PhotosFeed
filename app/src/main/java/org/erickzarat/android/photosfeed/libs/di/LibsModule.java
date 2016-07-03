package org.erickzarat.android.photosfeed.libs.di;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import dagger.Module;
import dagger.Provides;
import org.erickzarat.android.photosfeed.libs.CloudinaryImageStorage;
import org.erickzarat.android.photosfeed.libs.GlideImageLoader;
import org.erickzarat.android.photosfeed.libs.GreenRobotEventBus;
import org.erickzarat.android.photosfeed.libs.base.EventBus;
import org.erickzarat.android.photosfeed.libs.base.ImageLoader;
import org.erickzarat.android.photosfeed.libs.base.ImageStorage;

import javax.inject.Singleton;

/**
 * Created by zarathos on 26/06/16
 */
@Module
public class LibsModule {

    private Fragment fragment;

    public LibsModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager){
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager providesRequestManager(Fragment fragment){
        return Glide.with(fragment);
    }

    @Provides
    @Singleton
    Fragment providesFragment(){
        return this.fragment;
    }

    @Provides
    @Singleton
    EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus){
        return new GreenRobotEventBus(eventBus);
    }

    @Provides
    @Singleton
    org.greenrobot.eventbus.EventBus providesLibraryEventBus(){
        return org.greenrobot.eventbus.EventBus.getDefault();
    }

    @Provides
    @Singleton
    ImageStorage providesImageStorage(Cloudinary cloudinary) {
        return new CloudinaryImageStorage(cloudinary);
    }

    @Provides
    @Singleton
    Cloudinary providesCloudinary(Context context) {
        return new Cloudinary(Utils.cloudinaryUrlFromContext(context));
    }
}
