package org.erickzarat.android.photosfeed.login;

import org.erickzarat.android.photosfeed.login.events.LoginEvent;

/**
 * Created by zarathos on 03/07/16
 */
public interface LoginPresenter {
    void onCreate();
    void onDestroy();
    void onPause();
    void onResume();
    void validateLogin(String email, String password);
    void registerNewUser(String email, String password);
    void onEventMainThread(LoginEvent event);
}
