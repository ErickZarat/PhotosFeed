package org.erickzarat.android.photosfeed.login;

/**
 * Created by zarathos on 03/07/16
 */
public interface LoginInteractor {
    void doSignUp(String email, String password);
    void doSignIn(String email, String password);
}
