package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;

@Deprecated
final class FetchProvidersForEmailObserver extends Maybe<List<String>> {

    private final FirebaseAuth instance;

    private final String email;

    FetchProvidersForEmailObserver(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    protected void subscribeActual(MaybeObserver<? super List<String>> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(listener);
    }

    private static final class Listener extends OnCompleteDisposable<SignInMethodQueryResult> {

        private final MaybeObserver<? super List<String>> observer;

        Listener(@NonNull MaybeObserver<? super List<String>> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    List<String> providers = task.getResult().getSignInMethods();
                    if (null != providers) {
                        observer.onSuccess(providers);
                    } else {
                        observer.onComplete();
                    }
                }
            }
        }
    }
}
