package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class UserLinkWithCredentialObserver extends Single<FirebaseUser> {

    private final FirebaseUser user;

    private final AuthCredential credential;

    UserLinkWithCredentialObserver(FirebaseUser user, AuthCredential credential) {
        this.user = user;
        this.credential = credential;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super FirebaseUser> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        user.linkWithCredential(credential)
                .addOnCompleteListener(listener);
    }

    private static final class Listener extends OnCompleteDisposable<AuthResult> {

        private final SingleObserver<? super FirebaseUser> observer;

        Listener(@NonNull SingleObserver<? super FirebaseUser> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    observer.onSuccess(task.getResult().getUser());
                }
            }
        }
    }
}
