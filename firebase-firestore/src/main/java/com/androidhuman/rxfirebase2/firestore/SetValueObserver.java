package com.androidhuman.rxfirebase2.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

final class SetValueObserver<T> extends Completable {

    private final DocumentReference instance;

    private final T value;

    @Nullable
    private final SetOptions options;

    SetValueObserver(@NonNull DocumentReference instance, T value) {
        this(instance, value, null);
    }

    SetValueObserver(
            @NonNull DocumentReference instance,
            T value, @Nullable SetOptions options) {
        this.instance = instance;
        this.value = value;
        this.options = options;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        if (null != options) {
            instance.set(value, options)
                    .addOnCompleteListener(listener);
        } else {
            instance.set(value)
                    .addOnCompleteListener(listener);
        }
    }

    static final class Listener extends OnCompleteDisposable<Void> {

        private final CompletableObserver observer;

        Listener(@NonNull CompletableObserver observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    Exception ex = task.getException();
                    if (null != ex) {
                        observer.onError(ex);
                    } else {
                        observer.onError(new UnknownError());
                    }
                } else {
                    observer.onComplete();
                }
            }
        }
    }
}
