package org.erickzarat.android.photosfeed.domain;

import com.firebase.client.*;
import org.erickzarat.android.photosfeed.entities.Photo;

import java.util.Map;

/**
 * Created by zarathos on 2/07/16
 */
public class FirebaseAPI {
    private Firebase firebase;
    private ChildEventListener photosEventListener;

    public FirebaseAPI(Firebase firebase) {
        this.firebase = firebase;
    }

    public void checkForData(final FirebaseActionListenerCallback listenerCallback){
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    listenerCallback.onSuccess();
                } else {
                    listenerCallback.onError(null);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
            }
        });
    }

    public void subscribe(final FirebaseEventListenerCallback listenerCallback) {
        if (photosEventListener == null) {
            photosEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listenerCallback.onChildAdded(dataSnapshot);
                }

                @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    listenerCallback.onChildRemoved(dataSnapshot);
                }

                @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    listenerCallback.onCancelled(firebaseError);
                }
            };
            firebase.addChildEventListener(photosEventListener);
        }
    }

    public void unsubscribe() {
        if(photosEventListener != null)
            firebase.removeEventListener(photosEventListener);
    }

    public String create() {
        return firebase.push().getKey();
    }

    public void update(Photo photo) {
        this.firebase.child(photo.getId()).setValue(photo);
    }

    public void remove(Photo photo, FirebaseActionListenerCallback listenerCallback) {
        this.firebase.child(photo.getId()).removeValue();
        listenerCallback.onSuccess();
    }

    public String getAuthEmail(){
        String email = null;
        if (firebase.getAuth() != null) {
            Map<String, Object> providerData = firebase.getAuth().getProviderData();
            email = providerData.get("email").toString();
        }
        return email;
    }

    public void logout() {
        firebase.unauth();
    }

    public void login(String email, String password, final FirebaseActionListenerCallback listenerCallback){
        firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                listenerCallback.onSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
            }
        });
    }

    public void signUp(String email, String password, final FirebaseActionListenerCallback listenerCallback){
        firebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                listenerCallback.onSuccess();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
            }
        });
    }

    public void checkForSession(FirebaseActionListenerCallback listenerCallback) {
        if (firebase.getAuth() != null) {
            listenerCallback.onSuccess();
        } else {
            listenerCallback.onError(null);
        }
    }
}
