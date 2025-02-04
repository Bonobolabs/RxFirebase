package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.PhoneAuthProvider;

import android.app.Activity;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public final class RxPhoneAuthProvider {

    @CheckResult
    @NonNull
    public static Observable<PhoneAuthEvent> verifyPhoneNumber(
            @NonNull PhoneAuthProvider provider, String phoneNumber,
            long timeout, TimeUnit timeUnit, Activity activity) {
        return verifyPhoneNumber(provider, phoneNumber,
                timeout, timeUnit, activity, null);
    }

    @CheckResult
    @NonNull
    public static Observable<PhoneAuthEvent> verifyPhoneNumber(
            @NonNull PhoneAuthProvider provider, String phoneNumber,
            long timeout, TimeUnit timeUnit, Activity activity,
            @Nullable PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        return new PhoneAuthProviderVerifyPhoneNumberActivityObserver(
                provider, phoneNumber, timeout, timeUnit, activity, forceResendingToken);
    }
}
