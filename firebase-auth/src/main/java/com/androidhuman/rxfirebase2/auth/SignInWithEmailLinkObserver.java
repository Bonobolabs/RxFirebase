package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class SignInWithEmailLinkObserver extends Single<AuthResult> {

    private final FirebaseAuth instance;

    private final String email;

    private final String emailLink;

    SignInWithEmailLinkObserver(FirebaseAuth instance, String email, String emailLink) {
        this.instance = instance;
        this.email = email;
        this.emailLink = emailLink;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super AuthResult> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.signInWithEmailLink(email, emailLink)
                .addOnCompleteListener(listener);
    }

    private static final class Listener extends OnCompleteDisposable<AuthResult> {

        private final SingleObserver<? super AuthResult> observer;

        Listener(@NonNull SingleObserver<? super AuthResult> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    observer.onSuccess(task.getResult());
                }
            }
        }
    }
}
