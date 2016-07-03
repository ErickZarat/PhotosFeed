package org.erickzarat.android.photosfeed.libs.base;

/**
 * Created by zarathos on 26/06/16
 */
public interface EventBus {
    void register(Object subscriber);
    void unregister(Object subscriber);
    void post(Object event);
}
